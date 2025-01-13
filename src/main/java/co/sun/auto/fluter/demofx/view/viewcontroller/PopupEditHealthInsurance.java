package co.sun.auto.fluter.demofx.view.viewcontroller;

import co.sun.auto.fluter.demofx.model.HealthInsurance;
import co.sun.auto.fluter.demofx.util.DateUtils;
import co.sun.auto.fluter.demofx.view.controllerinterface.PopupController;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PopupEditHealthInsurance extends PopupController {
    public TextField insuranceId;
    public TextField address;
    public DatePicker createAt;
    public TextField examinationAddress;
    public TextField expiredAt;
    public Button exitBtn;
    public Button saveBtn;

    public PopupEditHealthInsuranceAction action = null;

    public void init(Stage stage, HealthInsurance healthInsurance) {
        this.stage = stage;
        if (healthInsurance == null) {
            return;
        }

        insuranceId.setText(healthInsurance.getInsuranceId());
        address.setText(healthInsurance.getAddress());

        //Convert to LocalDate
        createAt.setValue(DateUtils.convertStringToLocalDate2(healthInsurance.getCreateDate()));
        examinationAddress.setText(healthInsurance.getExaminationPlace());
        expiredAt.setText(healthInsurance.getExpiredDate());
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

        HealthInsurance healthInsurance = new HealthInsurance();
        healthInsurance.setInsuranceId(insuranceId.getText());
        healthInsurance.setAddress(address.getText());
        healthInsurance.setCreateDate(DateUtils.convertLocalDateToString(createAt.getValue()));
        healthInsurance.setExaminationPlace(examinationAddress.getText());
        healthInsurance.setExpiredDate(expiredAt.getText());
        action.onSaveClick(healthInsurance);
        close();
    }

    public interface PopupEditHealthInsuranceAction {
        void onExitClick();

        void onSaveClick(HealthInsurance healthInsurance);
    }
}
