package co.sun.auto.fluter.demofx.view.viewcontroller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SignIn {

    public OnSignInListener listener = null;

    @FXML
    private Button loginButton;

    public void handleSignIn(ActionEvent actionEvent) {
//        try {
        listener.onSignInClick();

//            if (GlobalLoader.fxmlLoaderHome == null) {
//                return;
//            }
//
//            Parent root = GlobalLoader.fxmlLoaderHome.load();
//
//            // Lấy cửa sổ hiện tại và thay thế nội dung bằng giao diện home.fxml
//            Stage stage = (Stage) loginButton.getScene().getWindow();
//            stage.setScene(new Scene(root));
//            stage.show();

//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public interface OnSignInListener {
        void onSignInClick();
    }
}
