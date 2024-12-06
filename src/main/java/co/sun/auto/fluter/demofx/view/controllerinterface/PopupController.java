package co.sun.auto.fluter.demofx.view.controllerinterface;

import javafx.stage.Stage;

public abstract class PopupController {
    public Stage stage;
    public void close(){
        stage.close();
    }
}
