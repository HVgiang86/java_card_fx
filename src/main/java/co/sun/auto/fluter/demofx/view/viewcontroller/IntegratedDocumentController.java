package co.sun.auto.fluter.demofx.view.viewcontroller;

import co.sun.auto.fluter.demofx.HelloApplication;
import co.sun.auto.fluter.demofx.controller.CardController;
import co.sun.auto.fluter.demofx.controller.DBController;
import co.sun.auto.fluter.demofx.model.Citizen;
import co.sun.auto.fluter.demofx.model.DrivingLicense;
import co.sun.auto.fluter.demofx.model.HealthInsurance;
import co.sun.auto.fluter.demofx.model.VehicleRegister;
import co.sun.auto.fluter.demofx.util.ViewUtils;
import co.sun.auto.fluter.demofx.view.controllerinterface.PopupController;
import co.sun.auto.fluter.demofx.view.global.GlobalLoader;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.SQLOutput;

public class IntegratedDocumentController extends PopupController {
    private final CardController cardController = CardController.getInstance();
    public ImageView btnVehicleRegistration;
    public ImageView btnDrivingLicense;
    public ImageView btnHealthInsurance;
    private Citizen citizen;

    public void init(Stage stage, Citizen citizen) {
        this.stage = stage;
        this.citizen = citizen;
        System.out.println("Citizen: " + citizen.citizenId);
    }

    public void onVehicleClick(MouseEvent mouseEvent) {
        try {
            VehicleRegister vehicleRegister = DBController.getVehicleRegister(citizen.citizenId);
            System.out.println("Vehicle register: " + vehicleRegister);

            if (vehicleRegister == null) {
                showEditVehicleRegister(true);
                return;
            }

            GlobalLoader.fxmlSceneVehicleRegistration = new FXMLLoader(HelloApplication.class.getResource("scene-view-vehicle-register.fxml"));
            Parent root = GlobalLoader.fxmlSceneVehicleRegistration.load();

            PopupVehicleRegister controller = GlobalLoader.fxmlSceneVehicleRegistration.getController();

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Đăng ký xe");
            popupStage.setScene(new Scene(root));

            controller.init(popupStage, vehicleRegister);

            controller.action = new PopupVehicleRegister.PopupVehicleRegisterAction() {
                @Override
                public void onExitClick() {

                }

                @Override
                public void onEditClick() {
                    showEditVehicleRegister(false);
                }

                @Override
                public void onRemoveClick() {
                    Platform.runLater(() -> {
                        ViewUtils.showConfirmPopup("Bạn có chắc chắn muốn xóa đăng ký xe?", "Huỷ", "Xác nhận", new ViewUtils.OnConfirmAction() {
                            @Override
                            public void onCancel() {

                            }

                            @Override
                            public void onConfirm() {
                                boolean isSuccess = DBController.removeVehicleRegister(citizen.citizenId);
                                if (isSuccess) {
                                    ViewUtils.showNoticePopup("Xóa đăng ký xe thành công", () -> {

                                    });
                                } else {
                                    ViewUtils.showNoticePopup("Xóa đăng ký xe thất bại", () -> {

                                    });
                                }
                            }
                        });
                    });
                }
            };

            popupStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDrivingLicenseClick(MouseEvent mouseEvent) {
        try {
            DrivingLicense license = DBController.getDrivingLicense(citizen.citizenId);

            if (license == null) {
                showEditDrivingLicense(true);
                return;
            }

            GlobalLoader.fxmlSceneDrivingLicense = new FXMLLoader(HelloApplication.class.getResource("scene-view-driving-license.fxml"));
            Parent root = GlobalLoader.fxmlSceneDrivingLicense.load();

            PopupDrivingLicense controller = GlobalLoader.fxmlSceneDrivingLicense.getController();

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Giấy phép lái xe");
            popupStage.setScene(new Scene(root));

            controller.init(popupStage, license);

            controller.action = new PopupDrivingLicense.PopupDrivingLicenseAction() {

                @Override
                public void onExitClick() {
                }

                @Override
                public void onEditClick() {
                    showEditDrivingLicense(false);
                }

                @Override
                public void onRemoveLicenseClick() {
                    Platform.runLater(() -> {
                        ViewUtils.showConfirmPopup("Bạn có chắc chắn muốn xóa giấy phép lái xe?", "Huỷ", "Xác nhận", new ViewUtils.OnConfirmAction() {
                            @Override
                            public void onCancel() {

                            }

                            @Override
                            public void onConfirm() {
                                boolean isSuccess = DBController.removeDrivingLicense(citizen.citizenId);
                                if (isSuccess) {
                                    ViewUtils.showNoticePopup("Xóa giấy phép lái xe thành công", () -> {

                                    });
                                } else {
                                    ViewUtils.showNoticePopup("Xóa giấy phép lái xe thất bại", () -> {

                                    });
                                }
                            }
                        });
                    });
                }
            };
            popupStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showEditDrivingLicense(boolean isCreate) {
        try {
            GlobalLoader.fxmlEditDrivingLicense = new FXMLLoader(HelloApplication.class.getResource("popup-edit-driving-license.fxml"));
            Parent root = GlobalLoader.fxmlEditDrivingLicense.load();
            PopupEditDrivingLicense controller = GlobalLoader.fxmlEditDrivingLicense.getController();

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);

            if (isCreate) {
                popupStage.setTitle("Thêm mới giấy phép lái xe");
            } else {
                popupStage.setTitle("Chỉnh sửa giấy phép lái xe");
            }
            popupStage.setScene(new Scene(root));

            DrivingLicense license = DBController.getDrivingLicense(citizen.citizenId);
            controller.init(popupStage, license);

            controller.action = new PopupEditDrivingLicense.PopupEditDrivingLicenseAction() {
                @Override
                public void onExitClick() {
                }

                @Override
                public void onSaveClick(DrivingLicense drivingLicense) {
                    drivingLicense.citizenId = citizen.citizenId;
                    boolean isSuccess;
                    if (isCreate) {
                        isSuccess = DBController.insertDrivingLicense(drivingLicense);
                    } else {
                        isSuccess = DBController.updateDrivingLicense(citizen.citizenId, drivingLicense);
                    }

                    if (isSuccess) {
                        System.out.println("Update driving license success: " + drivingLicense);
                        ViewUtils.showNoticePopup("Cập nhật giấy phép lái xe thành công", () -> {

                        });
                    } else {
                        ViewUtils.showNoticePopup("Cập nhật giấy phép lái xe thất bại", () -> {

                        });
                    }
                }
            };

            popupStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showEditVehicleRegister(boolean isCreate) {
        try {
            GlobalLoader.fxmlEditVehicleRegistration = new FXMLLoader(HelloApplication.class.getResource("popup-edit-vehicle-register.fxml"));

            VehicleRegister vehicleRegister = DBController.getVehicleRegister(citizen.citizenId);

            Parent root = GlobalLoader.fxmlEditVehicleRegistration.load();
            PopupEditVehicleRegister controller = GlobalLoader.fxmlEditVehicleRegistration.getController();

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);

            if (isCreate) {
                popupStage.setTitle("Thêm mới đăng ký xe");
            } else {
                popupStage.setTitle("Chỉnh sửa đăng ký xe");
            }

            popupStage.setScene(new Scene(root));

            controller.init(popupStage, vehicleRegister);

            controller.action = new PopupEditVehicleRegister.PopupEditVehicleRegisterAction() {
                @Override
                public void onExitClick() {
                }

                @Override
                public void onSaveClick(VehicleRegister vehicleRegister) {
                    vehicleRegister.citizenId = citizen.citizenId;
                    boolean isSuccess;
                    if (isCreate) {
                        isSuccess = DBController.insertVehicleRegister(vehicleRegister);
                    } else {
                        isSuccess = DBController.updateVehicleRegister(citizen.citizenId, vehicleRegister);
                    }
                    if (isSuccess) {
                        System.out.println("Update vehicle register success: " + vehicleRegister);
                        ViewUtils.showNoticePopup("Cập nhật đăng ký xe thành công", () -> {

                        });
                    } else {
                        ViewUtils.showNoticePopup("Cập nhật đăng ký xe thất bại", () -> {

                        });
                    }
                }
            };

            popupStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onHealthInsuranceClick(MouseEvent mouseEvent) {
        try {
            HealthInsurance healthInsurance = DBController.getHealthInsurance(citizen.citizenId);
            GlobalLoader.fxmlSceneHealthInsurance = new FXMLLoader(HelloApplication.class.getResource("scene-view-health-insurance.fxml"));

            System.out.println("Health insurance: " + healthInsurance);

            if (healthInsurance == null) {
                showEditHealthInsurance(true);
                return;
            }

            Parent root = GlobalLoader.fxmlSceneHealthInsurance.load();

            PopupHealInsurance controller = GlobalLoader.fxmlSceneHealthInsurance.getController();

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Đăng ký xe");
            popupStage.setScene(new Scene(root));

            controller.init(popupStage, healthInsurance);

            controller.action = new PopupHealInsurance.PopupHealInsuranceAction() {
                @Override
                public void onExitClick() {

                }

                @Override
                public void onEditClick() {
                    showEditHealthInsurance(false);
                }

                @Override
                public void onRemoveClick() {
                    Platform.runLater(() -> {
                        ViewUtils.showConfirmPopup("Bạn có chắc muốn xoá thẻ BHYT", "Huỷ", "Xác nhận", new ViewUtils.OnConfirmAction() {
                            @Override
                            public void onCancel() {

                            }

                            @Override
                            public void onConfirm() {
                                boolean isSuccess = DBController.removeHealthInsurance(citizen.citizenId);
                                if (isSuccess) {
                                    ViewUtils.showNoticePopup("Xoá BHYT thành công!", () -> {

                                    });
                                } else {
                                    ViewUtils.showNoticePopup("Xoá BHYT thất bại", () -> {

                                    });
                                }
                            }
                        });
                    });

                }
            };

            popupStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showEditHealthInsurance(boolean isCreate) {
        try {
            GlobalLoader.fxmlEditHealthInsurance = new FXMLLoader(HelloApplication.class.getResource("popup-edit-health-insurance.fxml"));

            HealthInsurance healthInsurance = DBController.getHealthInsurance(citizen.citizenId);

            Parent root = GlobalLoader.fxmlEditHealthInsurance.load();
            PopupEditHealthInsurance controller = GlobalLoader.fxmlEditHealthInsurance.getController();

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);

            if (isCreate) {
                popupStage.setTitle("Thêm mới đăng ký xe");
            } else {
                popupStage.setTitle("Chỉnh sửa đăng ký xe");
            }

            popupStage.setScene(new Scene(root));

            controller.init(popupStage, healthInsurance);

            controller.action = new PopupEditHealthInsurance.PopupEditHealthInsuranceAction() {
                @Override
                public void onExitClick() {
                }

                @Override
                public void onSaveClick(HealthInsurance healthInsurance) {
                    healthInsurance.citizenId = citizen.citizenId;
                    boolean isSuccess;
                    if (isCreate) {
                        isSuccess = DBController.insertHealthInsurance(healthInsurance);
                    } else {
                        isSuccess = DBController.updateHealthInsurance(citizen.citizenId, healthInsurance);
                    }
                    if (isSuccess) {
                        System.out.println("Update health insurance success: " + healthInsurance);
                        ViewUtils.showNoticePopup("Cập nhật BHYT thành công", () -> {

                        });
                    } else {
                        ViewUtils.showNoticePopup("Cập nhật BHYT thất bại", () -> {

                        });
                    }
                }
            };

            popupStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
