package co.sun.auto.fluter.demofx.view.viewcontroller;

import co.sun.auto.fluter.demofx.view.controllerinterface.PopupController;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Popup2I2B extends PopupController {
    public TextField inputField1;
    public TextField inputField2;
    public Button leftBtn;
    public Button rightBtn;
    public OnPopup2I2BListener listener = null;

    public void init(String inputField1, String inputField2, String leftBtn, String rightBtn, Stage stage) {
        this.inputField1.setText(inputField1);
        this.inputField2.setText(inputField2);
        this.leftBtn.setText(leftBtn);
        this.rightBtn.setText(rightBtn);
        this.stage = stage;
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

    public interface OnPopup2I2BListener {
        void onLeftBtnClick(Popup2I2B popup);

        void onRightBtnClick(Popup2I2B popup);
    }

}
