package co.sun.auto.fluter.demofx;

import co.sun.auto.fluter.demofx.view.global.GlobalLoader;
import co.sun.auto.fluter.demofx.view.viewcontroller.SignIn;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        GlobalLoader.fxmlLoaderLogin = new FXMLLoader(HelloApplication.class.getResource("sign-in.fxml"));
        GlobalLoader.fxmlLoaderHome = new FXMLLoader(HelloApplication.class.getResource("home.fxml"));

        Scene scene = new Scene(GlobalLoader.fxmlLoaderLogin.load(), 1000, 800);
        stage.setTitle("Sign-in!");

        SignIn controller = GlobalLoader.fxmlLoaderLogin.getController();

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
