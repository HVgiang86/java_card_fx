package co.sun.auto.fluter.demofx.view.viewcontroller;

import co.sun.auto.fluter.demofx.model.VehicleRegister;
import co.sun.auto.fluter.demofx.util.DateUtils;
import co.sun.auto.fluter.demofx.view.controllerinterface.PopupController;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PopupEditVehicleRegister extends PopupController {
    public TextField brand;
    public TextField model;
    public TextField color;
    public TextField plate;
    public TextField frame;
    public DatePicker registerAt;
    public TextField engine;
    public TextField registerPlace;
    public TextField capacity;
    public DatePicker expiredAt;
    public Button btnExit;
    public Button btnSave;

    public PopupEditVehicleRegisterAction action = null;

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

        VehicleRegister vehicleRegister = new VehicleRegister();
        vehicleRegister.vehicleBrand = brand.getText();
        vehicleRegister.vehicleModel = model.getText();
        vehicleRegister.vehicleColor = color.getText();
        vehicleRegister.vehiclePlate = plate.getText();
        vehicleRegister.vehicleFrame = frame.getText();
        vehicleRegister.vehicleEngine = engine.getText();
        vehicleRegister.vehicleRegisterPlace = registerPlace.getText();
        vehicleRegister.vehicleRegisterDate = DateUtils.convertLocalDateToString(registerAt.getValue());
        vehicleRegister.vehicleExpiredDate = DateUtils.convertLocalDateToString(expiredAt.getValue());
        vehicleRegister.vehicleCapacity = capacity.getText();
        action.onSaveClick(vehicleRegister);
        close();
    }

    public void init(Stage popupStage, VehicleRegister vehicleRegister) {
        this.stage = popupStage;
        brand.setText(vehicleRegister.vehicleBrand);
        model.setText(vehicleRegister.vehicleModel);
        color.setText(vehicleRegister.vehicleColor);
        plate.setText(vehicleRegister.vehiclePlate);
        frame.setText(vehicleRegister.vehicleFrame);
        engine.setText(vehicleRegister.vehicleEngine);
        registerPlace.setText(vehicleRegister.vehicleRegisterPlace);
        registerAt.setValue(DateUtils.convertStringToLocalDate2(vehicleRegister.vehicleRegisterDate));
        expiredAt.setValue(DateUtils.convertStringToLocalDate2(vehicleRegister.vehicleExpiredDate));
        capacity.setText(vehicleRegister.vehicleCapacity);

    }

    public interface PopupEditVehicleRegisterAction {
        void onExitClick();

        void onSaveClick(VehicleRegister vehicleRegister);
    }
}
