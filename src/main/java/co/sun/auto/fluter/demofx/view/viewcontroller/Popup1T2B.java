package co.sun.auto.fluter.demofx.view.viewcontroller;

import co.sun.auto.fluter.demofx.view.controllerinterface.PopupController;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Popup1T2B extends PopupController {
    public OnPopup1T2BListener listener = null;
    public Button btnLeft;
    public Button btnRight;
    public Label txtLabel;
    public Label txtTitle;

    public void init(String title, String label, String leftBtn, String rightBtn, Stage stage) {
        this.txtLabel.setText(label);
        this.btnLeft.setText(leftBtn);
        this.btnRight.setText(rightBtn);
        this.txtTitle.setText(title);
        this.stage = stage;
    }

    public void setLabel(String text) {
        txtLabel.setText(text);
    }

    public void setLeftBtn(String text) {
        btnLeft.setText(text);
    }

    public void setRightBtn(String text) {
        btnRight.setText(text);
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
