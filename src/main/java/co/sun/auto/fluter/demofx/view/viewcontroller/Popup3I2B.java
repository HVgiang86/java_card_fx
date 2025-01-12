package co.sun.auto.fluter.demofx.view.viewcontroller;

import co.sun.auto.fluter.demofx.constant.Const;
import co.sun.auto.fluter.demofx.view.controllerinterface.PopupController;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Popup3I2B extends PopupController {
    public OnPopup3I2BListener listener = null;

    public TextField oldPin;
    public TextField newPinField;
    public TextField confirmPinField;
    public Button btnExit;
    public Button btnConfirm;

    public void init(String leftBtn, String rightBtn, Stage stage) {
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

        if (oldPin.getText().isEmpty() || newPinField.getText().isEmpty() || confirmPinField.getText().isEmpty()) {
            return;
        }

        if (newPinField.getText().length() != Const.PIN_LENGTH || confirmPinField.getText().length() != Const.PIN_LENGTH) {
            return;
        }

        if (!newPinField.getText().equals(confirmPinField.getText())) {
            return;
        }

        listener.onRightBtnClick(oldPin.getText(), newPinField.getText(), this);
    }

    public interface OnPopup3I2BListener {
        void onLeftBtnClick(Popup3I2B popup);

        void onRightBtnClick(String oldPin, String newPin, Popup3I2B popup);
    }

}
