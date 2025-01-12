package co.sun.auto.fluter.demofx.view.viewcontroller;

import co.sun.auto.fluter.demofx.controller.AppController;
import co.sun.auto.fluter.demofx.controller.CardController;
import co.sun.auto.fluter.demofx.model.ApplicationState;
import co.sun.auto.fluter.demofx.model.Citizen;
import co.sun.auto.fluter.demofx.util.DateUtils;
import co.sun.auto.fluter.demofx.util.ImageUtils;
import co.sun.auto.fluter.demofx.util.ViewUtils;
import co.sun.auto.fluter.demofx.validator.Validator;
import co.sun.auto.fluter.demofx.view.controllerinterface.PopupController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class PopupEditInfo extends PopupController {
    public TextField txtName;
    public DatePicker dateBirthDate;
    public TextField txtHometown;
    public TextField txtAddress;
    public TextField txtNationality;
    public TextField txtEthnicity;
    public TextField txtReligion;
    public TextField txtIdentification;
    public Button btnCancel;
    public Button btnSave;

    byte[] avatar = null;

    public OnPopupEditInfoListener listener;
    public ComboBox dropGender;

    @FXML
    private Button uploadButton;
    @FXML
    private TextField filePathField;

    public void init(Stage stage) {
        this.stage = stage;
        txtName.setText("123456123456123456123456");
        txtHometown.setText("123456123456123456123456");
        txtAddress.setText("123456123456123456123456");
        txtNationality.setText("123456123456123456123456");
        txtEthnicity.setText("123456123456123456123456");
        txtReligion.setText("123456123456123456123456");
        txtIdentification.setText("123456123456123456123456");
        avatar = null;
    }

    @FXML
    void handleUpload(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn Ảnh");

        // Set file type filters
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.JPEG", "*.jpeg"));

        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            try {
                avatar = ImageUtils.convertImageToByteArray( selectedFile.getAbsolutePath(), "JPEG");
                CardController.getInstance().avatarTest = avatar;
                System.out.println("Avatar length: " + avatar.length);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public void onCancelClick(ActionEvent actionEvent) {
        if (listener != null) {
            listener.onCancelClick();
        }
    }

    public void onSaveClick(ActionEvent actionEvent) {
        String name = txtName.getText();

        String birthDate;
        if (dateBirthDate.getValue() != null) {
            birthDate = dateBirthDate.getValue().toString();
        } else {
            birthDate = "";
        }

        String normalizeBirthDate = DateUtils.convertDate(birthDate);
        String address = txtAddress.getText();
        String hometown = txtHometown.getText();
        String nationality = txtNationality.getText();
        String ethnicity = txtEthnicity.getText();
        String religion = txtReligion.getText();

        String gender;
        if (dropGender.getValue() != null) {
            gender = dropGender.getValue().toString();
        } else {
            gender = "";
        }

        String identification = txtIdentification.getText();

        String validateName = Validator.validateFieldNotEmpty(txtName.getText(), "Họ tên");
        String validateBirthDate = Validator.validateBirthDate(normalizeBirthDate);
        String validateAddress = Validator.validateFieldNotEmpty(address, "Địa chỉ");
        String validateHometown = Validator.validateFieldNotEmpty(hometown, "Quê quán");
        String validateNationality = Validator.validateFieldNotEmpty(nationality, "Quốc tịch");
        String validateEthnicity = Validator.validateFieldNotEmpty(ethnicity, "Dân tộc");
        String validateReligion = Validator.validateFieldNotEmpty(religion, "Tôn giáo");
        String validateGender = Validator.validateFieldNotEmpty(gender, "Giới tính");
        String validateIdentification = Validator.validateFieldNotEmpty(identification, "Đặc điểm nhận dạng");

        if (validateName != null) {
            ViewUtils.alert(validateName);
            return;
        }
//        if (validateBirthDate != null) {
//            ViewUtils.alert(validateBirthDate);
//            return;
//        }
        if (validateAddress != null) {
            ViewUtils.alert(validateAddress);
            return;
        }
        if (validateHometown != null) {
            ViewUtils.alert(validateHometown);
            return;
        }
        if (validateNationality != null) {
            ViewUtils.alert(validateNationality);
            return;
        }
        if (validateEthnicity != null) {
            ViewUtils.alert(validateEthnicity);
            return;
        }
        if (validateReligion != null) {
            ViewUtils.alert(validateReligion);
            return;
        }
        //TODO: FOR TEST
//        if (validateGender != null) {
//            ViewUtils.alert(validateGender);
//            return;
//        }
        if (validateIdentification != null) {
            ViewUtils.alert(validateIdentification);
            return;
        }

        String cardNumber = AppController.getInstance().generateCardNumber();
//        Citizen citizen = new Citizen(cardNumber, name, gender, normalizeBirthDate, address, hometown, nationality, ethnicity, religion, identification);

        Citizen citizen = new Citizen(cardNumber, name, "Nam", "08/06/2002", address, hometown, nationality, ethnicity, religion, identification);
        citizen.setAvatar(avatar);
        listener.onSaveClick(citizen);
    }

    public interface OnPopupEditInfoListener {
        void onSaveClick(Citizen citizen);

        void onCancelClick();
    }

}
