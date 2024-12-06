package co.sun.auto.fluter.demofx;

import co.sun.auto.fluter.demofx.view.global.GlobalLoader;
import co.sun.auto.fluter.demofx.view.viewcontroller.HelloController;
import co.sun.auto.fluter.demofx.view.viewcontroller.Popup1T1I2B;
import co.sun.auto.fluter.demofx.view.viewcontroller.SignIn;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HelloApplication extends Application implements SignIn.OnSignInListener {

    private final List<Stage> popupStages = new ArrayList<>();
    private Stage primaryStage; // Stage chính cho màn hình Sign-in

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage; // Lưu lại Stage chính

        GlobalLoader.fxmlLoaderLogin = new FXMLLoader(HelloApplication.class.getResource("sign-in.fxml"));
        GlobalLoader.fxmlLoaderHome = new FXMLLoader(HelloApplication.class.getResource("home.fxml"));
        GlobalLoader.fxmlLoaderPopup1T1I2B = new FXMLLoader(HelloApplication.class.getResource("Popup_1t1i2b.fxml"));
        GlobalLoader.fxmlLoaderPopup1T1B = new FXMLLoader(HelloApplication.class.getResource("Popup_1t1b.fxml"));

        Scene scene = new Scene(GlobalLoader.fxmlLoaderLogin.load(), 1000, 800);
        primaryStage.setTitle("Sign-in!");

        SignIn controller = GlobalLoader.fxmlLoaderLogin.getController();
        controller.listener = this;

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void onSignInClick() {
        try {
            Parent root = GlobalLoader.fxmlLoaderHome.load();

            HelloController controller = GlobalLoader.fxmlLoaderHome.getController();

            Stage popupStage = new Stage();
            popupStage.setTitle("Citizen Manager");
            popupStage.initModality(Modality.APPLICATION_MODAL); // Chặn sự kiện với cửa sổ khác
            popupStage.setScene(new Scene(root));

            popupStages.add(popupStage); // Lưu popup vào danh sách quản lý

            popupStage.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onRightBtnClick(String value) {
        try {
            // Đóng tất cả popup trước khi mở màn hình Home
            for (Stage popup : popupStages) {
                if (popup.isShowing()) {
                    popup.close();
                }
            }
            popupStages.clear(); // Xóa danh sách sau khi đã đóng

            // Đóng cửa sổ chính (Sign-in window)
            if (primaryStage.isShowing()) {
                primaryStage.close();
            }

            // Mở màn hình Home
            Parent root = GlobalLoader.fxmlLoaderHome.load();
            Stage homeStage = new Stage();

            homeStage.setTitle("Home Window");
            homeStage.setScene(new Scene(root));
            homeStage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
