package co.sun.auto.fluter.demofx.view.viewcontroller;

import co.sun.auto.fluter.demofx.HelloApplication;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class SceneNoCardInserted {
    public ImageView avatarImage;

    public void init() {
        try {
            Image image = new Image(Objects.requireNonNull(HelloApplication.class.getResourceAsStream("images/ic_insert_card.png")));
            avatarImage.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
