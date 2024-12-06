package co.sun.auto.fluter.demofx.view.viewcontroller;

import co.sun.auto.fluter.demofx.view.controllerinterface.PopupController;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Popup1T2I2B extends PopupController {
    public Label label;
    public TextField inputField1;
    public TextField inputField2;
    public Button leftBtn;
    public Button rightBtn;
    public OnPopup1T2I2BListener listener = null;

    public void init(String label, String inputField1, String inputField2, String leftBtn, String rightBtn, Stage stage) {
        this.label.setText(label);
        this.inputField1.setText(inputField1);
        this.inputField2.setText(inputField2);
        this.leftBtn.setText(leftBtn);
        this.rightBtn.setText(rightBtn);
        this.stage = stage;
    }

    public void setLabel(String text) {
        label.setText(text);
    }

    public void setInputField1(String text) {
        inputField1.setText(text);
    }

    public void setInputField2(String text) {
        inputField2.setText(text);
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
        listener.onRightBtnClick(this);
    }

    public interface OnPopup1T2I2BListener {
        void onLeftBtnClick(Popup1T2I2B popup);
        void onRightBtnClick(Popup1T2I2B popup);
    }
}
