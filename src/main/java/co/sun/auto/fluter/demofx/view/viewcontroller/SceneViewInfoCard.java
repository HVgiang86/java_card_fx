package co.sun.auto.fluter.demofx.view.viewcontroller;

import co.sun.auto.fluter.demofx.HelloApplication;
import co.sun.auto.fluter.demofx.controller.CardController;
import co.sun.auto.fluter.demofx.model.Citizen;
import co.sun.auto.fluter.demofx.util.ViewUtils;
import co.sun.auto.fluter.demofx.view.global.GlobalLoader;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;

public class SceneViewInfoCard {
    private final CardController cardController = CardController.getInstance();
    public ImageView avatarImage;
    public TextField txtCitizenId;
    public TextField txtBirthDate;
    public TextField txtAddress;
    public TextField txtEthnicity;
    public TextField txtIdentification;
    public TextField txtFullName;
    public TextField txtGender;
    public TextField txtHometown;
    public TextField txtNationality;
    public TextField txtReligion;
    public Button btnChangePIN;
    public Button btnEdit;
    public Button btnIntegratedDocument;
    public Button btnLockCard;
    public Button btnUnlockCard;
    public OnButtonClick listener = null;
    private Citizen mCitizen;

    public OnSwitchScene onSwitchScene = null;

    interface OnSwitchScene {
        void onSwitchScene(String sceneName);
    }

    public void onPinChangeClick(ActionEvent actionEvent) {
        if (listener != null) {
            listener.onPinChangeClick(mCitizen);
        }

        try {
            GlobalLoader.fxmlLoaderPopup3I2B = new FXMLLoader(HelloApplication.class.getResource("Popup_3i2b.fxml"));
            Parent root = GlobalLoader.fxmlLoaderPopup3I2B.load();

            Popup3I2B controller = GlobalLoader.fxmlLoaderPopup3I2B.getController();

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Đổi mã PIN");
            popupStage.setScene(new Scene(root));

            controller.init("Huỷ", "Xác nhận", popupStage);

            controller.listener= new Popup3I2B.OnPopup3I2BListener() {
                @Override
                public void onLeftBtnClick(Popup3I2B popup) {
                    popupStage.close();
                }

                @Override
                public void onRightBtnClick(String oldPin, String newPin, Popup3I2B popup) {
                    // Call controller to change pin
                    cardController.changePin(oldPin, newPin, (isSuccess) -> {
                        if (isSuccess) {
                            popupStage.close();
                        } else {
                            ViewUtils.showNoticePopup("Không thể thay đổi mã PIN, vui lòng thư lai!", () -> {

                            });
                        }
                    });
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //EDIT INFO:" DONE UI
    public void onEditInfoClick(ActionEvent actionEvent) {
        if (listener != null) {
            listener.onEditInfoClick(mCitizen);
        }

        try {
            GlobalLoader.fxmlPopupEditInfo = new FXMLLoader(HelloApplication.class.getResource("popup-edit-info.fxml"));
            Parent root = GlobalLoader.fxmlPopupEditInfo.load();

            PopupEditInfo controller = GlobalLoader.fxmlPopupEditInfo.getController();

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Chỉnh sửa thông tin");
            popupStage.setScene(new Scene(root));

            controller.init(popupStage);

            controller.listener = new PopupEditInfo.OnPopupEditInfoListener() {
                @Override
                public void onSaveClick(Citizen citizen) {
                    ViewUtils.showPinCodeConfirmPopup("Vui lòng nhập mã PIN", "Huỷ", "Xác nhận", new ViewUtils.OnConfirmAction() {
                        @Override
                        public void onCancel() {

                        }

                        @Override
                        public void onConfirm() {
                            System.out.println("Save citizen info to card");
                            // Save citizen info to card
                            cardController.updateCardData(citizen, (isSuccess) -> {
                                if (isSuccess) {
                                    // Close the popup
                                    popupStage.close();

                                } else {
                                    ViewUtils.showNoticePopup("Không thể lưu thông tin, vui lòng thư lai!", () -> {

                                    });
                                }
                            });
                        }
                    });
                }

                @Override
                public void onCancelClick() {

                    ViewUtils.showConfirmPopup("Xác nhận huỷ?", "Thôi", "Huỷ", new ViewUtils.OnConfirmAction() {
                        @Override
                        public void onCancel() {

                        }

                        @Override
                        public void onConfirm() {
                            Platform.runLater(popupStage::close);
                        }
                    });
                }
            };

            popupStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onIntegratedDocumentClick(ActionEvent actionEvent) {
        if (listener != null) {
            listener.onIntegratedDocumentClick(mCitizen);
        }

        try {
            GlobalLoader.fxmlLoaderIntegratedDocument = new FXMLLoader(HelloApplication.class.getResource("integrated-documentation.fxml"));
            Parent root = GlobalLoader.fxmlLoaderIntegratedDocument.load();

            IntegratedDocumentController controller = GlobalLoader.fxmlLoaderIntegratedDocument.getController();

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Tài liệu tích hợp");
            popupStage.setScene(new Scene(root));

            popupStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onActiveCardClick(ActionEvent actionEvent) {
        if (listener != null) {
            listener.onActiveCardClick(mCitizen);
        }

        try {

            ViewUtils.showUnlockCardPopup( new ViewUtils.OnConfirmAction() {
                @Override
                public void onCancel() {

                }

                @Override
                public void onConfirm() {
                    ViewUtils.showPinCodeConfirmPopup("Vui lòng nhập mã PIN", "Huỷ", "Xác nhận", new ViewUtils.OnConfirmAction() {
                        @Override
                        public void onCancel() {

                        }

                        @Override
                        public void onConfirm() {
                            System.out.println("Deactive card");
                            // Deactive card

                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDeactiveCardClick(ActionEvent actionEvent) {
        if (listener != null) {
            listener.onDeactiveCardClick(mCitizen);
        }

        try {

            ViewUtils.showConfirmPopup("Bạn có chắc muốn khoá thẻ không?", "No", "Yes", new ViewUtils.OnConfirmAction() {
                @Override
                public void onCancel() {

                }

                @Override
                public void onConfirm() {
                    ViewUtils.showPinCodeConfirmPopup("Vui lòng nhập mã PIN", "Huỷ", "Xác nhận", new ViewUtils.OnConfirmAction() {
                        @Override
                        public void onCancel() {

                        }

                        @Override
                        public void onConfirm() {
                            System.out.println("Deactive card");
                            // Deactive card
                            cardController.deactiveCard((isSuccess) -> {
                                if (isSuccess) {
                                    // Close the popup
                                    Platform.runLater(() -> {
                                        ViewUtils.alert("Khoá thẻ thành công");
                                    });
                                } else {
                                    ViewUtils.showNoticePopup("Không thể khoá thẻ, vui lòng thư lai!", () -> {

                                    });
                                }
                            });
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCitizenInfo(Citizen citizen) {
        txtCitizenId.setText(citizen.getCitizenId());
        txtBirthDate.setText(citizen.getBirthDate());
        txtAddress.setText(citizen.getAddress());
        txtEthnicity.setText(citizen.getEthnicity());
        txtIdentification.setText(citizen.getIdentification());
        txtFullName.setText(citizen.getFullName());
        txtGender.setText(citizen.getGender());
        txtHometown.setText(citizen.getHometown());
        txtNationality.setText(citizen.getNationality());
        txtReligion.setText(citizen.getReligion());
        setAvatarImage(citizen.getAvatar());
//        setAvatarImageTest(citizen.getAvatar());
        mCitizen = citizen;
    }

    public void setAvatarImage(byte[] data) {
        if (data == null) {
            return;
        }

        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);

            Image image = new Image(bis);
            avatarImage.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setAvatarImageTest(byte[] data) {

        try {

            System.out.println("Avatar length: " + CardController.getInstance().avatarTest.length);
            ByteArrayInputStream bis = new ByteArrayInputStream(CardController.getInstance().avatarTest);

            Image image = new Image(bis);

            avatarImage.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setTxtCitizenId(String text) {
        txtCitizenId.setText(text);
    }

    public void setTxtCitizenId(TextField txtCitizenId) {
        this.txtCitizenId = txtCitizenId;
    }

    public void setTxtBirthDate(TextField txtBirthDate) {
        this.txtBirthDate = txtBirthDate;
    }

    public void setTxtAddress(TextField txtAddress) {
        this.txtAddress = txtAddress;
    }

    public void setTxtEthnicity(TextField txtEthnicity) {
        this.txtEthnicity = txtEthnicity;
    }

    public void setTxtIdentification(TextField txtIdentification) {
        this.txtIdentification = txtIdentification;
    }

    public void setTxtFullName(TextField txtFullName) {
        this.txtFullName = txtFullName;
    }

    public void setTxtGender(TextField txtGender) {
        this.txtGender = txtGender;
    }

    public void setTxtHometown(TextField txtHometown) {
        this.txtHometown = txtHometown;
    }

    public void setTxtNationality(TextField txtNationality) {
        this.txtNationality = txtNationality;
    }

    public void setTxtReligion(TextField txtReligion) {
        this.txtReligion = txtReligion;
    }

    public void setBtnChangePIN(Button btnChangePIN) {
        this.btnChangePIN = btnChangePIN;
    }

    public void setBtnEdit(Button btnEdit) {
        this.btnEdit = btnEdit;
    }

    public void setBtnIntegratedDocument(Button btnIntegratedDocument) {
        this.btnIntegratedDocument = btnIntegratedDocument;
    }

    public void setBtnLockCard(Button btnLockCard) {
        this.btnLockCard = btnLockCard;
    }

    public void setBtnUnlockCard(Button btnUnlockCard) {
        this.btnUnlockCard = btnUnlockCard;
    }

    public interface OnButtonClick {
        void onPinChangeClick(Citizen citizen);

        void onEditInfoClick(Citizen citizen);

        void onIntegratedDocumentClick(Citizen citizen);

        void onActiveCardClick(Citizen citizen);

        void onDeactiveCardClick(Citizen citizen);
    }

}
