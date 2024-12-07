package co.sun.auto.fluter.demofx.view.controllerinterface;

import javafx.stage.Stage;

public abstract class SceneController {
    public Stage stage;
    public void detach(){
        stage.close();
    }
}
