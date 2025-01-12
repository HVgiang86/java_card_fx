package co.sun.auto.fluter.demofx.view.viewcontroller;

import co.sun.auto.fluter.demofx.model.DrivingLicense;
import co.sun.auto.fluter.demofx.util.DateUtils;
import co.sun.auto.fluter.demofx.view.controllerinterface.PopupController;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PopupDrivingLicense extends PopupController {

    public TextField licenseId;
    public TextField licenseLevel;
    public DatePicker createdAt;
    public TextField createPlace;
    public TextField expiredAt;
    public TextField createdBy;
    public Button exitBtn;
    public Button editBtn;
    public Button cancelBtn;

    public PopupDrivingLicenseAction action = null;

    public void init(Stage stage, DrivingLicense drivingLicense) {
        this.stage = stage;
        //Set text with driving license
        licenseId.setText(drivingLicense.getLicenseId());
        licenseLevel.setText(drivingLicense.getLicenseLevel());

        //Set date with driving license
        createdAt.setValue(DateUtils.convertStringToLocalDate2(drivingLicense.getCreatedAt()));

        createPlace.setText(drivingLicense.getCreatePlace());
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

    public void onEditClick(ActionEvent actionEvent) {
        if (action == null) {
            return;
        }

        action.onEditClick();
        close();
    }

    public void onCancelClick(ActionEvent actionEvent) {
        if (action == null) {
            return;
        }

        action.onRemoveLicenseClick();
        close();
    }

    interface PopupDrivingLicenseAction {
        void onExitClick();

        void onEditClick();

        void onRemoveLicenseClick();
    }
}
