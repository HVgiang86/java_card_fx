package co.sun.auto.fluter.demofx.view.viewcontroller;

import co.sun.auto.fluter.demofx.HelloApplication;
import co.sun.auto.fluter.demofx.view.controllerinterface.SceneController;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.Objects;

public class CardNoInfo {
    public Button btnCreatteInfo;
    public ImageView avatarImage;
    public OnCardNoInfoListener listener;

    public void init() {
        try {
            Image image = new Image(Objects.requireNonNull(HelloApplication.class.getResourceAsStream("images/ic_no_info.png")));
            avatarImage.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onCreateInfoClick(ActionEvent actionEvent) {
        if (listener != null) {
            listener.onCardNoInfoCreate();
        }
    }

    public interface OnCardNoInfoListener {
        void onCardNoInfoCreate();
    }
}
