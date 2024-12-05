package co.sun.auto.fluter.demofx.view.viewcontroller;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class Popup1t1i2b {
    @FXML
    private Label label1;
    @FXML
    private TextField pinField;
    @FXML
    private Button btnConfirm;
    @FXML
    private Button btnExit;
    private String mode;
    @FXML
    public void setMode(String mode) {
        this.mode = mode;

        switch(mode) {
            case "EnterPINCode":
                label1.setText("Nhập mã PIN (6 ký tự)");
                pinField.setPromptText("******");
                break;
            case "AcceptPINCode":
                label1.setText("Xác nhận mã PIN");
                pinField.setPromptText("******");
                break;
            case "Re-enterPINCode":
                label1.setText("Nhập lại mã PIN");
                pinField.setPromptText("******");
                break;
            default:
                break;
        }
    }

    @FXML
    public void handleAccept() {
        // Xử lý nút "Xác nhận"
        //Kiểm tra mã PIN
        //Đóng cưa sổ
    }

    @FXML
    public void handleCancel() {
        // Xử lý nút "Hủy"
        //Đóng cửa sổ
    }
}
