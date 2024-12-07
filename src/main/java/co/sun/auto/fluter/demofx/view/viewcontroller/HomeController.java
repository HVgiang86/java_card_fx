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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {
    private final AppController appController = AppController.getInstance();
    private final CardController cardController = CardController.getInstance();
    public VBox vboxContent;
    public Button btnConnectCard;
    public Button btnLogout;

    @FXML
    protected void onConnectCardClick() {
        if (cardController.isCardConnected()) {
            System.out.println("Handle disconnect card");
            Platform.runLater(this::handleDisConnectCard);
        } else {
            btnConnectCard.setText("Kết nối thẻ");
            handleInsertCard();
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

                    cardController.verifyCard(value, (s1 -> {
                        if (!s1) {
                            //TODO: show error
                            ViewUtils.showNoticePopup("Nhập sai mã pin!", () -> {

                            });
                            return;
                        }

                        cardController.connectCardForTest((isConnected) -> {
                            if (isConnected) {
                                // Card connected successfully
                                System.out.println("Card connected successfully!");
                                btnConnectCard.setText("Bỏ thẻ");
                                // Close the popup
                                popup.close();
                                Platform.runLater(() -> {
                                    getCardInfo();
                                });

                            } else {
                                //TODO: show error
                                ViewUtils.showNoticePopup("Không thể kết nối thẻ!", () -> {

                                });
                            }
                        });
                    }));
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
        try {
            //Lấy thông tin thẻ
            Citizen citizen = cardController.getCardInfoTest();

            //Thẻ chưa khởi tạo, hiển thị trang tạo thông tin
            if (citizen == null) {
                showPopupEditInfo();
            } else {
                showInfoScene(citizen);
            }
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

    private void showPopupEditInfo() {
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
                            Platform.runLater(() -> {
                                getCardInfo();
                            });
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
            controller.listener = () -> Platform.runLater(this::showPopupEditInfo);
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
