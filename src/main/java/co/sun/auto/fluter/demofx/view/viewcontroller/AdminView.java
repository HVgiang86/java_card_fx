package co.sun.auto.fluter.demofx.view.viewcontroller;

import co.sun.auto.fluter.demofx.HelloApplication;
import co.sun.auto.fluter.demofx.controller.CardController;
import co.sun.auto.fluter.demofx.model.Citizen;
import co.sun.auto.fluter.demofx.util.ViewUtils;
import co.sun.auto.fluter.demofx.view.controllerinterface.PopupController;
import co.sun.auto.fluter.demofx.view.global.GlobalLoader;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.smartcardio.Card;
import java.util.Objects;

public class AdminView extends PopupController {
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
    public Button btnEdit;
    public Button btnIntegratedDocument;
    public Button btnLockCard;
    public Button btnUnlockCard;

    private Citizen mCitizen;

    public void init(Stage stage, Citizen citizen) {
        this.stage = stage;
        this.mCitizen = citizen;
        initInfo(citizen);
    }

    private void initInfo(Citizen citizen) {
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
    }

    public void onChangePinClick(ActionEvent actionEvent) {
        checkCardConnect(new OnDoActionRequireCard() {
            @Override
            public void onDoAction() {
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
                            CardController.getInstance().changePin(oldPin, newPin, (isSuccess) -> {
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

            @Override
            public void cancel() {

            }
        });
    }

    public void onEditClick(ActionEvent actionEvent) {
        checkCardConnect(new OnDoActionRequireCard() {
            @Override
            public void onDoAction() {
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

                                    if (citizen.avatar == null || citizen.avatar.length == 0) {
                                        System.out.println("Avatar is empty");
                                    } else {
                                        CardController.getInstance().sendAvatar(citizen.avatar, (isSuccess) -> {
                                            if (isSuccess) {
                                                System.out.println("Send avatar success");
                                                Platform.runLater(() -> {
//                                                    setAvatarImage(citizen.avatar);
                                                });
                                            } else {
                                                System.out.println("Send avatar failed");
                                            }
                                        });
                                    }

                                    // Save citizen info to card
                                    CardController.getInstance().updateCardData(citizen, (isSuccess) -> {
                                        if (isSuccess) {
                                            Platform.runLater(() -> {
                                                Citizen newCitizen = CardController.getInstance().getCardInfo();
                                                initInfo(newCitizen);
                                            });

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

                            ViewUtils.showConfirmPopup("Xác nhận huỷ?", "No", "Yes", new ViewUtils.OnConfirmAction() {
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

            @Override
            public void cancel() {

            }
        });
    }

    public void integratedDocumentClick(ActionEvent actionEvent) {
        try {
            checkCardConnect(new OnDoActionRequireCard() {
                @Override
                public void onDoAction() {
                    try {
                        GlobalLoader.fxmlLoaderIntegratedDocument = new FXMLLoader(HelloApplication.class.getResource("integrated-documentation.fxml"));
                        Parent root = GlobalLoader.fxmlLoaderIntegratedDocument.load();

                        IntegratedDocumentController controller = GlobalLoader.fxmlLoaderIntegratedDocument.getController();

                        Stage popupStage = new Stage();

                        controller.init(popupStage, mCitizen);
                        popupStage.initModality(Modality.APPLICATION_MODAL);
                        popupStage.setTitle("Tài liệu tích hợp");
                        popupStage.setScene(new Scene(root));

                        popupStage.showAndWait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void cancel() {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onLockCardClick(ActionEvent actionEvent) {
        checkCardConnect(new OnDoActionRequireCard() {
            @Override
            public void onDoAction() {
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
                                    CardController.getInstance().deactiveCard((isSuccess) -> {
                                        if (isSuccess) {
                                            // Close the popup
                                            Platform.runLater(() -> {
                                                ViewUtils.alert("Khoá thẻ thành công");
                                                ViewUtils.checkShowLockAndUnlockBtn(AdminView.this, mCitizen);
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

            @Override
            public void cancel() {

            }
        });
    }

    public void onUnlockCardClick(ActionEvent actionEvent) {
        checkCardConnect(new OnDoActionRequireCard() {
            @Override
            public void onDoAction() {
                try {

                    ViewUtils.showUnlockCardPopup(new ViewUtils.OnConfirmAction() {
                        @Override
                        public void onCancel() {

                        }

                        @Override
                        public void onConfirm() {
                            ViewUtils.checkShowLockAndUnlockBtn(AdminView.this, mCitizen);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void cancel() {

            }
        });

    }

    public void changeLockCardBtnVisible(boolean canLock, boolean canUnlock) {
        btnLockCard.setVisible(canLock);
        btnUnlockCard.setVisible(canUnlock);
    }

    private void checkCardConnect(OnDoActionRequireCard actionRequireCard) {
        try {
            CardController.getInstance().connectCard((isSuccess) -> {
                if (isSuccess) {
                    String cardId = CardController.getInstance().getCardId();

                    if (Objects.equals(cardId, mCitizen.citizenId)) {
                        System.out.println("Card is connected");
                        actionRequireCard.onDoAction();
                    } else {
                        actionRequireCard.cancel();
                    }
                } else {
                    ViewUtils.showNoticePopup("Không thể kết nối với thẻ", actionRequireCard::cancel);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideAllButton() {
        btnEdit.setVisible(false);
        btnIntegratedDocument.setVisible(false);
        btnLockCard.setVisible(false);
        btnUnlockCard.setVisible(false);
    }

    interface OnDoActionRequireCard {
        void onDoAction();
        void cancel();
    }

}
