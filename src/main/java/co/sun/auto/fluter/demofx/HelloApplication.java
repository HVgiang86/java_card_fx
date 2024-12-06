package co.sun.auto.fluter.demofx;

import co.sun.auto.fluter.demofx.view.global.GlobalLoader;
import co.sun.auto.fluter.demofx.view.viewcontroller.Popup1t1i2b;
import co.sun.auto.fluter.demofx.view.viewcontroller.SignIn;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application implements SignIn.OnSignInListener, Popup1t1i2b.OnPopup1T1I2BListener {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        GlobalLoader.fxmlLoaderLogin = new FXMLLoader(HelloApplication.class.getResource("sign-in.fxml"));
        GlobalLoader.fxmlLoaderHome = new FXMLLoader(HelloApplication.class.getResource("home.fxml"));
        GlobalLoader.fxmlLoaderPopup1T1I2B = new FXMLLoader(HelloApplication.class.getResource("Popup_1t1i2b.fxml"));

        Scene scene = new Scene(GlobalLoader.fxmlLoaderLogin.load(), 1000, 800);
        stage.setTitle("Sign-in!");

        SignIn controller = GlobalLoader.fxmlLoaderLogin.getController();
        controller.listener = this;

        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void onSignInClick() {
        try {
            Parent root = GlobalLoader.fxmlLoaderPopup1T1I2B.load();

            Popup1t1i2b controller = GlobalLoader.fxmlLoaderPopup1T1I2B.getController();
            controller.listener = this;


            Stage popupStage = new Stage();

            controller.init("Popup Window", "Input Field", "Left Button", "Right Button", popupStage);
            popupStage.setTitle("Popup Window");
            popupStage.initModality(Modality.APPLICATION_MODAL); // Block events to other windows
            popupStage.setScene(new Scene(root));
            popupStage.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onLeftBtnClick() {

    }

    @Override
    public void onRightBtnClick(String value) {
        System.out.println("Value: " + value);
    }
}
