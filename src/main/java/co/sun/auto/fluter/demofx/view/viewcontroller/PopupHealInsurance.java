package co.sun.auto.fluter.demofx.view.viewcontroller;

import co.sun.auto.fluter.demofx.model.HealthInsurance;
import co.sun.auto.fluter.demofx.util.DateUtils;
import co.sun.auto.fluter.demofx.view.controllerinterface.PopupController;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PopupHealInsurance extends PopupController {
    public TextField insuranceId;
    public TextField address;
    public DatePicker createAt;
    public TextField examination;
    public TextField expiredAt;
    public Button exitBtn;
    public Button editBtn;
    public Button removeBtn;

    public PopupHealInsuranceAction action = null;

    public void init(Stage stage, HealthInsurance healthInsurance) {
        this.stage = stage;
        if (healthInsurance == null) {
            return;
        }

        insuranceId.setText(healthInsurance.getInsuranceId());
        address.setText(healthInsurance.getAddress());

        //Convert to LocalDate
        createAt.setValue(DateUtils.convertStringToLocalDate2(healthInsurance.getCreateDate()));
        examination.setText(healthInsurance.getExaminationPlace());
        expiredAt.setText(healthInsurance.getExpiredDate());
    }

    public void onExitClick(ActionEvent actionEvent) {
        if (action == null) {
            return;
        }

        action.onExitClick();
        close();
    }

    public void onEditBtn(ActionEvent actionEvent) {
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

    interface PopupHealInsuranceAction {
        void onExitClick();

        void onEditClick();

        void onRemoveClick();
    }
}
