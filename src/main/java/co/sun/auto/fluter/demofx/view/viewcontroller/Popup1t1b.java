package co.sun.auto.fluter.demofx.view.viewcontroller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class Popup1t1b {
    @FXML
    private Label label1t1b; // Label hiển thị thông báo

    @FXML
    private Button btnOK; // Nút OK để đóng popup

    // Phương thức để thiết lập chế độ thông báo
    public void setNotificationMode(String mode) {
        switch (mode) {
            case "CreatePIN":
                label1t1b.setText("Cấp mã PIN thành công!");
                break;
            case "ChangePIN":
                label1t1b.setText("Đổi mã PIN thành công!");
                break;
            case "CardLocked":
                label1t1b.setText("Thẻ công dân đã được khóa!");
                break;
            default:
                break;
        }
    }

    @FXML
    private void handleOKButton() {
        Stage stage = (Stage) btnOK.getScene().getWindow();
        stage.close();
    }

}
