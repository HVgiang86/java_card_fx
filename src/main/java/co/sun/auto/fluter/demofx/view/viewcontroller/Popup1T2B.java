package co.sun.auto.fluter.demofx.view.viewcontroller;

import co.sun.auto.fluter.demofx.view.controllerinterface.PopupController;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Popup1T2B extends PopupController {
    public Label label;
    public Button leftBtn;
    public Button rightBtn;
    public OnPopup1T2BListener listener = null;

    public void init(String label, String leftBtn, String rightBtn, Stage stage) {
        this.label.setText(label);
        this.leftBtn.setText(leftBtn);
        this.rightBtn.setText(rightBtn);
        this.stage = stage;
    }

    public void setLabel(String text) {
        label.setText(text);
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

    public interface OnPopup1T2BListener {
        void onLeftBtnClick(Popup1T2B popup);

        void onRightBtnClick(Popup1T2B popup);
    }

}
