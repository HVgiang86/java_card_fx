package co.sun.auto.fluter.demofx.view.viewcontroller;

import co.sun.auto.fluter.demofx.constant.Const;
import co.sun.auto.fluter.demofx.util.ViewUtils;
import co.sun.auto.fluter.demofx.view.controllerinterface.PopupController;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Popup2I2B extends PopupController {
    public OnPopup2I2BListener listener = null;
    public TextField newPinField;
    public TextField confirmPinField;
    public Button btnExit;
    public Button btnConfirm;

    public void init(String inputField1, String inputField2, String leftBtn, String rightBtn, Stage stage) {
        this.newPinField.setText(inputField1);
        this.confirmPinField.setText(inputField2);
        this.btnExit.setText(leftBtn);
        this.btnConfirm.setText(rightBtn);
        this.stage = stage;
    }

    public void onLeftBtnClick(ActionEvent actionEvent) {
        if (listener == null) {
            return;
        }
        listener.onLeftBtnClick(this);
    }

    public void onRightBtnClick(ActionEvent actionEvent) {
        if (listener == null) {
            return;
        }

        if (newPinField.getText().isEmpty() || confirmPinField.getText().isEmpty()) {
            ViewUtils.alert("Vui lòng nhập đủ thông tin");
            return;
        }

        if (newPinField.getText().length() != Const.PIN_LENGTH || confirmPinField.getText().length() != Const.PIN_LENGTH) {
            ViewUtils.alert("Mã PIN phải có " + Const.PIN_LENGTH + " ký tự");
            return;
        }

        if (!newPinField.getText().equals(confirmPinField.getText())) {
            ViewUtils.alert("Mã PIN không khớp");
            return;
        }

        listener.onRightBtnClick(newPinField.getText(), this);
    }

    public interface OnPopup2I2BListener {
        void onLeftBtnClick(Popup2I2B popup);

        void onRightBtnClick(String value, Popup2I2B popup);
    }

}
