package co.sun.auto.fluter.demofx.view.viewcontroller;

import co.sun.auto.fluter.demofx.view.controllerinterface.PopupController;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Popup1T1B extends PopupController {
    public Label label;
    public OnBtnListener listener = null;
    public Button btnOK;

    public void init(String label, String btn, Stage stage) {
        this.label.setText(label);
        this.btnOK.setText(btn);
        this.stage = stage;
    }

    public void onBtnClick(ActionEvent actionEvent) {
        if (listener == null) {
            return;
        }
        listener.onClick();
    }

    public interface OnBtnListener{
        void onClick();
    }

}
