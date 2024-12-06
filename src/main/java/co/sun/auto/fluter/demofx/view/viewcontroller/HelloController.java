package co.sun.auto.fluter.demofx.view.viewcontroller;

import co.sun.auto.fluter.demofx.HelloApplication;
import co.sun.auto.fluter.demofx.controller.AppController;
import co.sun.auto.fluter.demofx.util.ViewUtils;
import co.sun.auto.fluter.demofx.view.global.GlobalLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    private final AppController appController = AppController.getInstance();

    @FXML
    protected void onConnectCardClick() {
        try {
            GlobalLoader.fxmlLoaderPopup1T1I2B = new FXMLLoader(HelloApplication.class.getResource("Popup_1t1i2b.fxml"));
            // Load popup FXML
            Parent root = GlobalLoader.fxmlLoaderPopup1T1I2B.load();

            // Lấy controller của pop-up
            Popup1T1I2B controller = GlobalLoader.fxmlLoaderPopup1T1I2B.getController();

            // Tạo cửa sổ popup
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL); // Chặn tương tác với các cửa sổ khác
            popupStage.setTitle("Kết nối thẻ");
            popupStage.setScene(new Scene(root));

            controller.init("Nhập mã pin", "******", "Hủy", "Xác nhận", popupStage);
            controller.listener = new Popup1T1I2B.OnPopup1T1I2BListener() {
                @Override
                public void onLeftBtnClick(Popup1T1I2B popup) {
                    popup.close();
                }

                @Override
                public void onRightBtnClick(String value, Popup1T1I2B popup) {
                    System.out.println("Value: " + value);  // In giá trị nhận được từ nút "Right"

                    appController.verifyCard(value, (s1 -> {
                        if (!s1) {
                            //TODO: show error
                            ViewUtils.showNoticePopup("Nhập sai mã pin!", () -> {

                            });
                            return;
                        }

                        appController.connectCard((s2) -> {
                            if (s2) {
                                //TODO: READ CARD INFORMATION
                                popup.close();
                            } else {
                                //TODO: show error
                                ViewUtils.showNoticePopup("Không thể kết nối thẻ!", () -> {

                                });

                            }
                        });
                    }));
                }
            };
            popupStage.showAndWait(); // Hiển thị popup và đợi người dùng đóng
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
