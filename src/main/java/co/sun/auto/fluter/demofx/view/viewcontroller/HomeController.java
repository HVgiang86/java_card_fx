package co.sun.auto.fluter.demofx.view.viewcontroller;

import co.sun.auto.fluter.demofx.HelloApplication;
import co.sun.auto.fluter.demofx.controller.AppController;
import co.sun.auto.fluter.demofx.controller.CardController;
import co.sun.auto.fluter.demofx.model.Citizen;
import co.sun.auto.fluter.demofx.util.ViewUtils;
import co.sun.auto.fluter.demofx.view.global.GlobalLoader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HomeController {
    private final AppController appController = AppController.getInstance();
    private final CardController cardController = CardController.getInstance();
    public VBox vboxContent;
    public Button btnConnectCard;
    public Button btnLogout;
    public ImageView imageHome;

    public void init() {
        try {
            Image image = new Image(Objects.requireNonNull(HelloApplication.class.getResourceAsStream("images/ic_insert_card.png")));
            imageHome.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onConnectCardClick() {
        if (cardController.isCardConnected()) {
            System.out.println("Handle disconnect card");
            Platform.runLater(this::handleDisConnectCard);
        } else {
            btnConnectCard.setText("Kết nối thẻ");
            Platform.runLater(this::handleInsertCard);
        }
    }

    private void handleInsertCard() {
        try {
            GlobalLoader.fxmlLoaderPopup1T1I2B = new FXMLLoader(HelloApplication.class.getResource("Popup_1t1i2b.fxml"));
            // Load popup FXML
            Parent root = GlobalLoader.fxmlLoaderPopup1T1I2B.load();

            // Lấy controller của pop-up
            Popup1T1I2B controller = GlobalLoader.fxmlLoaderPopup1T1I2B.getController();

            // Tạo cửa sổ popup
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL); // Chặn tương tác với các cửa sổ khác
            popupStage.setTitle("Kết nối thẻ");
            popupStage.setScene(new Scene(root));

            controller.init("Nhập mã pin (6 ký tự)", "******", "Hủy", "Xác nhận", popupStage);


            controller.listener = new Popup1T1I2B.OnPopup1T1I2BListener() {
                @Override
                public void onLeftBtnClick(Popup1T1I2B popup) {
                    popup.close();
                }

                @Override
                public void onRightBtnClick(String value, Popup1T1I2B popup) {
                    System.out.println("Value: " + value);  // In giá trị nhận được từ nút "Right"

                    cardController.connectCard((isConnected) -> {
                        if (isConnected) {

                            cardController.verifyCard(value, (isVerified, pinAttemptsRemain) -> {
                                if (!isVerified) {
                                    System.out.println("Pin code is incorrect!: " + pinAttemptsRemain);
                                    if (pinAttemptsRemain > 0) {
                                        popup.close();
                                        Platform.runLater(() -> {
                                            showErrorPinCode();
                                        });
                                    }
                                    return;
                                }

                                // Card connected successfully
                                System.out.println("Card connected successfully!");
                                btnConnectCard.setText("Bỏ thẻ");
                                // Close the popup
                                popup.close();
                                Platform.runLater(() -> {
                                    getCardInfo();
                                });

                            });

                        } else {
                            //TODO: show error
                            ViewUtils.showNoticePopup("Không thể kết nối thẻ!", () -> {

                            });
                        }
                    });


                }
            };

            popupStage.showAndWait(); // Hiển thị popup và đợi người dùng đóng

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDisConnectCard() {
        ViewUtils.showConfirmPopup("Rút thẻ?", "Thôi", "OK", new ViewUtils.OnConfirmAction() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onConfirm() {
                cardController.disconnectCardTest((isSuccess) -> {
                    if (isSuccess) {
                        Platform.runLater(() -> {
                            btnConnectCard.setText("Kết nối thẻ");
                            showNoCardInserted();
                        });
                    } else {

                    }
                });

            }
        });
    }

    private void getCardInfo() {
        System.out.println("=========");
        try {
            if (!cardController.isCardConnected()) {
                showNoCardInserted();
                return;
            }

            btnConnectCard.setText("Bỏ thẻ");

            //Lấy thông tin thẻ
            Citizen citizen = cardController.getCardInfo();


            //Thẻ chưa khởi tạo, hiển thị trang tạo thông tin
            if (citizen == null) {
                System.out.println("Citizen null");
                showPopupEditInfo(true);
            } else {
                System.out.println("HAHAHA: " + citizen.toString());
                showInfoScene(citizen);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showErrorPinCode() {
        try {
            GlobalLoader.fxmlLoaderPopup1T1I3B = new FXMLLoader(HelloApplication.class.getResource("Popup_1t1i3b.fxml"));
            // Load popup FXML
            Parent root = GlobalLoader.fxmlLoaderPopup1T1I3B.load();

            // Lấy controller của pop-up
            Popup1T1I3B controller = GlobalLoader.fxmlLoaderPopup1T1I3B.getController();

            // Tạo cửa sổ popup
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL); // Chặn tương tác với các cửa sổ khác
            popupStage.setTitle("Nhập mã pin");
            popupStage.setScene(new Scene(root));

            controller.init("Bạn đã nhập sai mã PIN, vui lòng thử lại", "******", "Hủy", "Xác nhận", "Cấp lại mã pin", popupStage);
            controller.listener = new Popup1T1I3B.OnPopup1T1I3BListener() {
                @Override
                public void onLeftBtnClick(Popup1T1I3B popup) {
                    popup.close();
                }

                @Override
                public void onMiddleBtnClick(String value, Popup1T1I3B popup) {
                    cardController.verifyCard(value, (isVerified, pinAttemptsRemain) -> {
                        if (!isVerified) {
                            if (pinAttemptsRemain > 0) {
                                ViewUtils.showNoticePopup("Nhập sai mã pin! Còn " + pinAttemptsRemain + " lần thử!", () -> {

                                });
                            } else {
                                popup.close();
                                Platform.runLater(() -> showOutOfPinAttempt());
                            }

                            return;
                        }

                        // Card connected successfully
                        System.out.println("Card connected successfully!");
                        btnConnectCard.setText("Bỏ thẻ");
                        // Close the popup
                        popup.close();
                        Platform.runLater(() -> {
                            getCardInfo();
                        });

                    });
                }

                @Override
                public void onRightBtnClick(Popup1T1I3B popup) {
                    popup.close();
                    Platform.runLater(() -> {
                        showChangePinPopup();
                    });
                }
            };
            popupStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showOutOfPinAttempt() {
        try {
            GlobalLoader.fxmlLoaderPopup1T2B = new FXMLLoader(HelloApplication.class.getResource("Popup_1t2b.fxml"));
            Parent root = GlobalLoader.fxmlLoaderPopup1T2B.load();
            Popup1T2B controller = GlobalLoader.fxmlLoaderPopup1T2B.getController();
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Thông báo");

            controller.init("Thông báo", "Bạn đã nhập sai mã PIN quá số lần cho phép", "OK", "Cấp lại mã pin", popupStage);
            popupStage.setScene(new Scene(root));

            controller.listener = new Popup1T2B.OnPopup1T2BListener() {
                @Override
                public void onLeftBtnClick(Popup1T2B popup) {
                    popup.close();
                }

                @Override
                public void onRightBtnClick(Popup1T2B popup) {
                    popup.close();
                    Platform.runLater(() -> {
                        showChangePinPopup();
                    });
                }
            };
            popupStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showChangePinPopup(Citizen citizen) {
        try {
            GlobalLoader.fxmlLoaderPopup2I2B = new FXMLLoader(HelloApplication.class.getResource("Popup_2i2b.fxml"));
            Parent root = GlobalLoader.fxmlLoaderPopup2I2B.load();
            Popup2I2B controller = GlobalLoader.fxmlLoaderPopup2I2B.getController();
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Nhập mã pin mới");

            controller.init("Nhập mã PIN mới", "Nhập lại mã PIN", "Huỷ", "Xác nhận", popupStage);
            controller.listener = new Popup2I2B.OnPopup2I2BListener() {
                @Override
                public void onLeftBtnClick(Popup2I2B popup) {
                    popup.close();
                    Platform.runLater(() -> {
                        getCardInfo();
                    });
                }

                @Override
                public void onRightBtnClick(String value, Popup2I2B popup) {
                    cardController.setupPinCode(value, citizen, (isSuccess) -> {
                        if (isSuccess) {
                            popup.close();

                            ViewUtils.showNoticePopup("Đã thay đổi mã pin thành công!", () -> {
                                Platform.runLater(() -> {
                                    getCardInfo();
                                });
                            });
                        } else {
                            ViewUtils.showNoticePopup("Không thể thay đổi mã pin, vui lòng thư!", () -> {

                            });
                        }
                    });
                }
            };
            popupStage.setScene(new Scene(root));
            popupStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showChangePinPopup() {
        try {
            GlobalLoader.fxmlLoaderPopup2I2B = new FXMLLoader(HelloApplication.class.getResource("Popup_2i2b.fxml"));
            Parent root = GlobalLoader.fxmlLoaderPopup2I2B.load();
            Popup2I2B controller = GlobalLoader.fxmlLoaderPopup2I2B.getController();
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Nhập mã pin mới");

            controller.init("Nhập mã PIN mới", "Nhập lại mã PIN", "Huỷ", "Xác nhận", popupStage);
            controller.listener = new Popup2I2B.OnPopup2I2BListener() {
                @Override
                public void onLeftBtnClick(Popup2I2B popup) {
                    popup.close();
                    Platform.runLater(() -> {
                        getCardInfo();
                    });
                }

                @Override
                public void onRightBtnClick(String value, Popup2I2B popup) {
                    cardController.setupPinCode(value, (isSuccess) -> {
                        if (isSuccess) {
                            popup.close();

                            ViewUtils.showNoticePopup("Đã thay đổi mã pin thành công!", () -> {
                                Platform.runLater(() -> {
                                    getCardInfo();
                                });
                            });
                        } else {
                            ViewUtils.showNoticePopup("Không thể thay đổi mã pin, vui lòng thư!", () -> {

                            });
                        }
                    });
                }
            };
            popupStage.setScene(new Scene(root));
            popupStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showInfoScene(Citizen citizen) {
        try {
            // Đã có thông tin công dân, hiển thị
            GlobalLoader.fxmlSceneViewInfoCard = new FXMLLoader(HelloApplication.class.getResource("scene_view_info_select_card.fxml"));
            AnchorPane anchorPane = GlobalLoader.fxmlSceneViewInfoCard.load();
            vboxContent.getChildren().clear();
            vboxContent.getChildren().add(anchorPane);

            SceneViewInfoCard controller = GlobalLoader.fxmlSceneViewInfoCard.getController();

            controller.setCitizenInfo(citizen);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showPopupEditInfo(boolean setUpPin) {
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
                    // Save citizen info to card
                    cardController.createCardDataTest(citizen, (isSuccess) -> {
                        if (isSuccess) {
                            // Close the popup
                            popupStage.close();

                            if (setUpPin) {
                                Platform.runLater(() -> {
                                    showChangePinPopup(citizen);
                                });
                            } else {
                                Platform.runLater(() -> {
                                    getCardInfo();
                                });
                            }
                        } else {
                            ViewUtils.showNoticePopup("Không thể lưu thông tin, vui lòng thư!", () -> {

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
                            Platform.runLater(() -> {
                                popupStage.close();
                                showNoCardInfo();
                            });
                        }
                    });
                    Platform.runLater(() -> {
                        showNoCardInfo();
                    });
                }
            };

            popupStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showNoCardInfo() {
        try {
            GlobalLoader.fxmlSceneNoCardInfo = new FXMLLoader(HelloApplication.class.getResource("scene_card_no_info.fxml"));
            AnchorPane anchorPane = GlobalLoader.fxmlSceneNoCardInfo.load();
            vboxContent.getChildren().clear();
            vboxContent.getChildren().add(anchorPane);

            CardNoInfo controller = GlobalLoader.fxmlSceneNoCardInfo.getController();
            controller.init();
            controller.listener = () -> Platform.runLater(() -> showPopupEditInfo(true));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showNoCardInserted() {
        try {
            GlobalLoader.fxmlSceneNoCardInserted = new FXMLLoader(HelloApplication.class.getResource("scene_no_card_inserted.fxml"));
            AnchorPane anchorPane = GlobalLoader.fxmlSceneNoCardInserted.load();
            vboxContent.getChildren().clear();
            vboxContent.getChildren().add(anchorPane);
            SceneNoCardInserted controller = GlobalLoader.fxmlSceneNoCardInserted.getController();
            controller.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
