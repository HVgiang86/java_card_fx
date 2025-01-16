package co.sun.auto.fluter.demofx.view.viewcontroller;

import co.sun.auto.fluter.demofx.util.ViewUtils;
import co.sun.auto.fluter.demofx.validator.Validator;
import co.sun.auto.fluter.demofx.view.controllerinterface.PopupController;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Popup1T1I2B extends PopupController {
    public Label label;
    public TextField inputField;
    public Button leftBtn;
    public Button rightBtn;
    public OnPopup1T1I2BListener listener = null;
    StringBuilder actualPin = new StringBuilder();

    public void init(String label, String inputField, String leftBtn, String rightBtn, Stage stage) {
        this.label.setText(label);
        this.inputField.setText(inputField);
        this.leftBtn.setText(leftBtn);
        this.rightBtn.setText(rightBtn);
        this.stage = stage;
    }

    public void setLabel(String text) {
        label.setText(text);
    }

    public void setInputField(String text) {
        inputField.setText(text);
    }

    public void setLeftBtn(String text) {
        leftBtn.setText(text);
    }

    public void setRightBtn(String text) {
        rightBtn.setText(text);
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

        String pinError = Validator.validatePinCode(inputField.getText());

        if (pinError != null) {
            ViewUtils.alert(pinError);
            return;
        }

        listener.onRightBtnClick(inputField.getText(), this);
    }

    public interface OnPopup1T1I2BListener {
        void onLeftBtnClick(Popup1T1I2B popup);

        void onRightBtnClick(String value, Popup1T1I2B popup);
    }
}
