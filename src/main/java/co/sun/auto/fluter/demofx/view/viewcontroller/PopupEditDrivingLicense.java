package co.sun.auto.fluter.demofx.view.viewcontroller;

import co.sun.auto.fluter.demofx.model.DrivingLicense;
import co.sun.auto.fluter.demofx.util.DateUtils;
import co.sun.auto.fluter.demofx.view.controllerinterface.PopupController;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PopupEditDrivingLicense extends PopupController {

    public TextField licenseId;
    public TextField licenseLevel;
    public DatePicker createdAt;
    public TextField CreatePlace;
    public TextField expiredAt;
    public TextField createdBy;
    public Button exitBtn;
    public Button saveBtn;

    public PopupEditDrivingLicenseAction action = null;

    public void init(Stage stage, DrivingLicense drivingLicense) {
        this.stage = stage;
        if (drivingLicense == null) {
            return;
        }
        //Set text with driving license
        licenseId.setText(drivingLicense.getLicenseId());
        licenseLevel.setText(drivingLicense.getLicenseLevel());

        //Set date with driving license
        createdAt.setValue(DateUtils.convertStringToLocalDate2(drivingLicense.getCreatedAt()));

        CreatePlace.setText(drivingLicense.getCreatePlace());
        expiredAt.setText(drivingLicense.getExpiredAt());
        createdBy.setText(drivingLicense.getCreatedBy());
    }

    public void onExitClick(ActionEvent actionEvent) {
        if (action == null) {
            return;
        }

        action.onExitClick();
        close();
    }

    public void onSaveClick(ActionEvent actionEvent) {
        if (action == null) {
            return;
        }

        DrivingLicense drivingLicense = new DrivingLicense();
        drivingLicense.setLicenseId(licenseId.getText());
        drivingLicense.setLicenseLevel(licenseLevel.getText());
        drivingLicense.setCreatedAt(DateUtils.convertLocalDateToString(createdAt.getValue()));
        drivingLicense.setCreatePlace(CreatePlace.getText());
        drivingLicense.setExpiredAt(expiredAt.getText());
        drivingLicense.setCreatedBy(createdBy.getText());

        action.onSaveClick(drivingLicense);
        close();
    }

    interface PopupEditDrivingLicenseAction {
        void onExitClick();

        void onSaveClick(DrivingLicense drivingLicense);
    }
}
