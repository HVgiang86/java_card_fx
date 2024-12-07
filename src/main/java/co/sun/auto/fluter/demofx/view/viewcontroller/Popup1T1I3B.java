package co.sun.auto.fluter.demofx.view.viewcontroller;

import co.sun.auto.fluter.demofx.view.controllerinterface.PopupController;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Popup1T1I3B extends PopupController {
    public Label label;
    public TextField inputField;
    public Button leftBtn;
    public Button middleBtn;
    public Button rightBtn;
    public OnPopup1T1I3BListener listener = null;

    public void init(String label, String inputField, String leftBtn, String middleBtn, String rightBtn, Stage stage) {
        this.label.setText(label);
        this.inputField.setText(inputField);
        this.leftBtn.setText(leftBtn);
        this.middleBtn.setText(middleBtn);
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

    public void setMiddleBtn(String text) {
        middleBtn.setText(text);
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

    public void onMiddleBtnClick(ActionEvent actionEvent) {
        if (listener == null) {
            return;
        }
        listener.onMiddleBtnClick(inputField.getText(), this);
    }

    public void onRightBtnClick(ActionEvent actionEvent) {
        if (listener == null) {
            return;
        }
        listener.onRightBtnClick(this);
    }

    public interface OnPopup1T1I3BListener {
        void onLeftBtnClick(Popup1T1I3B popup);

        void onMiddleBtnClick(String value, Popup1T1I3B popup);

        void onRightBtnClick(Popup1T1I3B popup);
    }
}
