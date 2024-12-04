package co.sun.auto.fluter.demofx.view.viewcontroller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class SignIn {

    @FXML
    private Button loginButton;

    @FXML
    public void handleSignIn() {
        try {
            // Tải tệp home.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/sun/auto/fluter/demofx/view/home.fxml"));
            Parent root = loader.load();

            // Lấy cửa sổ hiện tại và thay thế nội dung bằng giao diện home.fxml
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
