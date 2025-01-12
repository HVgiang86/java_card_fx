package co.sun.auto.fluter.demofx.view.viewcontroller;

import co.sun.auto.fluter.demofx.model.VehicleRegister;
import co.sun.auto.fluter.demofx.util.DateUtils;
import co.sun.auto.fluter.demofx.view.controllerinterface.PopupController;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PopupVehicleRegister extends PopupController {
    public TextField brand;
    public TextField model;
    public TextField color;
    public TextField plate;
    public TextField frameNumber;
    public DatePicker createdAt;
    public TextField engineNumber;
    public TextField createPlace;
    public TextField capacity;
    public DatePicker expiredAt;
    public Button removeBtn;
    public Button editBtn;
    public Button exitBtn;

    public PopupVehicleRegisterAction action = null;

    public void init(Stage stage, VehicleRegister vehicleRegister) {
        this.stage = stage;
        //Set text with vehicle register
        brand.setText(vehicleRegister.getVehicleBrand());
        model.setText(vehicleRegister.getVehicleModel());
        color.setText(vehicleRegister.getVehicleColor());
        plate.setText(vehicleRegister.getVehiclePlate());
        frameNumber.setText(vehicleRegister.getVehicleFrame());
        engineNumber.setText(vehicleRegister.getVehicleEngine());
        createPlace.setText(vehicleRegister.getVehicleRegisterPlace());
        capacity.setText(vehicleRegister.getVehicleCapacity());
        createdAt.setValue(DateUtils.convertStringToLocalDate2(vehicleRegister.getVehicleRegisterDate()));
        expiredAt.setValue(DateUtils.convertStringToLocalDate2(vehicleRegister.getVehicleExpiredDate()));

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

    public void onRemoveClick(ActionEvent actionEvent) {
        if (action == null) {
            return;
        }

        action.onRemoveClick();
        close();
    }

    interface PopupVehicleRegisterAction {
        void onExitClick();
        void onEditClick();
        void onRemoveClick();
    }
}
