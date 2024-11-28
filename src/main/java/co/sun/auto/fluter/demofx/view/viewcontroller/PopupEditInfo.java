package co.sun.auto.fluter.demofx.view.viewcontroller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class PopupEditInfo {
    @FXML
    private Button uploadButton;

    @FXML
    private TextField filePathField;

    @FXML
    void handleUpload(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn File");
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            filePathField.setText(selectedFile.getAbsolutePath());
        }
    }
}
