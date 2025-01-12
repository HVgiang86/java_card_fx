package co.sun.auto.fluter.demofx.view.viewcontroller;

import co.sun.auto.fluter.demofx.controller.CardController;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class IntegratedDocumentController {
    public ImageView btnVehicleRegistration;
    public ImageView btnDrivingLicense;
    public ImageView btnHealthInsurance;

    private final CardController cardController = CardController.getInstance();

    public void onVehicleClick(MouseEvent mouseEvent) {
        try {
//            cardController.showVehicleRegistration();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDrivingLicenseClick(MouseEvent mouseEvent) {
        try {
//            cardController.showDrivingLicense();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onHealthInsuranceClick(MouseEvent mouseEvent) {
        try {
//            cardController.showHealthInsurance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
