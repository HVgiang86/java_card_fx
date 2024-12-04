package co.sun.auto.fluter.demofx.view.viewcontroller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Popup1T1I2B {
    public Label label;
    public TextField inputField;
    public Button leftBtn;
    public Button rightBtn;
    public Stage stage;
    public OnPopup1T1I2BListener listener = null;

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
        listener.onLeftBtnClick();
        stage.close();

    }

    public void onRightBtnClick(ActionEvent actionEvent) {
        if (listener == null) {
            return;
        }
        listener.onRightBtnClick(inputField.getText());
    }

    public interface OnPopup1T1I2BListener {
        void onLeftBtnClick();

        void onRightBtnClick(String value);
    }
}
