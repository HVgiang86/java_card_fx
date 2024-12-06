package co.sun.auto.fluter.demofx.view.viewcontroller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class popup1t1i3b {
    @FXML
    private Label messageLabel; // Label hiển thị thông báo

    @FXML
    private TextField pinTextField; // TextField để nhập mã PIN

    @FXML
    private Button btnExit; // Nút "Thoát"
    @FXML
    private Button btnConfirm; // Nút "Xác nhận"
    @FXML
    private Button btnResetPin; // Nút "Cấp lại mã PIN"

    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    @FXML
    private void handleExitButton() {
        // Đóng popup
        Stage stage = (Stage) btnExit.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleConfirmButton() {
        // Xử lý nút "Xác nhận"
        String enteredPin = pinTextField.getText();
        System.out.println("Mã PIN đã nhập: " + enteredPin);

        // Kiểm tra mã PIN tại đây (nếu cần)
    }

    @FXML
    private void handleResetPinButton() {
        // Xử lý nút "Cấp lại mã PIN"
        System.out.println("Cấp lại mã PIN!");
    }
}
