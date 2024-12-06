package co.sun.auto.fluter.demofx.util;

import co.sun.auto.fluter.demofx.HelloApplication;
import co.sun.auto.fluter.demofx.view.global.GlobalLoader;
import co.sun.auto.fluter.demofx.view.viewcontroller.Popup1T1B;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ViewUtils {
    public static void showNoticePopup(String notice, OnAction callback) {
        try {
            GlobalLoader.fxmlLoaderPopup1T1B = new FXMLLoader(HelloApplication.class.getResource("Popup_1t1b.fxml"));
            Parent root = GlobalLoader.fxmlLoaderPopup1T1B.load();
            Popup1T1B controller = GlobalLoader.fxmlLoaderPopup1T1B.getController();

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL); // Chặn tương tác với các cửa sổ khác
            popupStage.setTitle("Thông báo");
            popupStage.setScene(new Scene(root));

            controller.init(notice, "OK",  popupStage);
            controller.listener = () -> {
                callback.callback();
                controller.close();
            };
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public interface OnAction{
        void callback();
    }
}
