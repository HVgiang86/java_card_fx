package co.sun.auto.fluter.demofx.view.viewcontroller;

import co.sun.auto.fluter.demofx.HelloApplication;
import co.sun.auto.fluter.demofx.controller.AppController;
import co.sun.auto.fluter.demofx.util.ViewUtils;
import co.sun.auto.fluter.demofx.view.global.GlobalLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {
    private final AppController appController = AppController.getInstance();
    public Button btnConnectCard;
    public Button btnLogout;
    public VBox contentVBox;
    public ImageView homeImage;

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

                        appController.connectCard((isConnected) -> {
                            if (isConnected) {
                                // Card connected successfully
                                System.out.println("Card connected successfully!");

                                // Define APDU parameters
                                byte cla = (byte) 0x00;
                                byte ins = (byte) 0x01; // INS = 0x00
                                byte p1 = (byte) 0x01;
                                byte p2 = (byte) 0x00;
                                byte[] data = null; // No additional data for this example

                                // Sending an APDU with the defined parameters
                                byte[] response = appController.sendApdu(cla, ins, p1, p2, appController.stringToHexArray("8"), (isSuccess) -> {
                                    if (isSuccess) {
                                        System.out.println("APDU command executed successfully!");
                                    } else {
                                        System.out.println("Failed to execute APDU command.");
                                    }
                                });

                                // Logging the response
                                if (response != null) {
                                    System.out.println("Card Response Data: " + appController.hexToString(appController.bytesToHex(response)));
                                } else {
                                    System.out.println("No data received from the card.");
                                }

                                // Close the popup
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
