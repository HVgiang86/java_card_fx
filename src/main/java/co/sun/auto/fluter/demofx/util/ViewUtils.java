package co.sun.auto.fluter.demofx.util;

import co.sun.auto.fluter.demofx.HelloApplication;
import co.sun.auto.fluter.demofx.controller.CardController;
import co.sun.auto.fluter.demofx.view.global.GlobalLoader;
import co.sun.auto.fluter.demofx.view.viewcontroller.Popup1T1B;
import co.sun.auto.fluter.demofx.view.viewcontroller.Popup1T1I2B;
import co.sun.auto.fluter.demofx.view.viewcontroller.Popup1T1I3B;
import co.sun.auto.fluter.demofx.view.viewcontroller.Popup1T2B;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ViewUtils {
    public static void alert(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    public static void showNoticePopup(String notice, OnAction callback) {
        Platform.runLater(() -> {
            try {
                GlobalLoader.fxmlLoaderPopup1T1B = new FXMLLoader(HelloApplication.class.getResource("Popup_1t1b.fxml"));
                Parent root = GlobalLoader.fxmlLoaderPopup1T1B.load();
                Popup1T1B controller = GlobalLoader.fxmlLoaderPopup1T1B.getController();

                Stage popupStage = new Stage();
                popupStage.initModality(Modality.APPLICATION_MODAL); // Chặn tương tác với các cửa sổ khác
                popupStage.setTitle("Thông báo");
                popupStage.setScene(new Scene(root));

                controller.init(notice, "OK", popupStage);
                controller.listener = () -> {
                    callback.callback();
                    controller.close();
                };

                popupStage.showAndWait(); // Hiển thị popup và đợi người dùng đóng
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void showPinCodeConfirmPopup(String content, String leftBtn, String rightBtn, OnConfirmAction action) {
        Platform.runLater(() -> {
            try {
                GlobalLoader.fxmlLoaderPopup1T1I2B = new FXMLLoader(HelloApplication.class.getResource("Popup_1t1i2b.fxml"));
                Parent root = GlobalLoader.fxmlLoaderPopup1T1I2B.load();
                Popup1T1I2B controller = GlobalLoader.fxmlLoaderPopup1T1I2B.getController();

                Stage popupStage = new Stage();
                popupStage.initModality(Modality.APPLICATION_MODAL); // Chặn tương tác với các cửa sổ khác
                popupStage.setTitle("Vui lòng nhập mã PIN");
                popupStage.setScene(new Scene(root));

                controller.init("Xác nhận", content, leftBtn, rightBtn, popupStage);
                controller.listener = new Popup1T1I2B.OnPopup1T1I2BListener() {
                    @Override
                    public void onLeftBtnClick(Popup1T1I2B popup) {
                        action.onCancel();
                        popup.close();
                    }

                    @Override
                    public void onRightBtnClick(String value, Popup1T1I2B popup) {
                        CardController.getInstance().verifyCard(value, (isVerified, pinTryRemaining) -> {
                            if (isVerified) {
                                action.onConfirm();
                            } else {
                                if (pinTryRemaining <= 0) {
//                                    Platform.runLater(() -> {
//                                        showErrorPinCode();
//                                    });
                                    alert("Thẻ bị khóa, vui lòng thử lại sau!");
                                } else {
                                    alert("Nhập sai mã PIN! Còn " + pinTryRemaining + " lần thử!");
                                }
                            }
                        });
                        popup.close();
                    }
                };
                popupStage.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

//    private static void showErrorPinCode() {
//        try {
//            GlobalLoader.fxmlLoaderPopup1T1I3B = new FXMLLoader(HelloApplication.class.getResource("Popup_1t1i3b.fxml"));
//            // Load popup FXML
//            Parent root = GlobalLoader.fxmlLoaderPopup1T1I3B.load();
//
//            // Lấy controller của pop-up
//            Popup1T1I3B controller = GlobalLoader.fxmlLoaderPopup1T1I3B.getController();
//
//            // Tạo cửa sổ popup
//            Stage popupStage = new Stage();
//            popupStage.initModality(Modality.APPLICATION_MODAL); // Chặn tương tác với các cửa sổ khác
//            popupStage.setTitle("Nhập mã pin");
//            popupStage.setScene(new Scene(root));
//
//            controller.init("Bạn đã nhập sai mã PIN, vui lòng thử lại", "******", "Hủy", "Xác nhận", "Cấp lại mã pin", popupStage);
//            controller.listener = new Popup1T1I3B.OnPopup1T1I3BListener() {
//                @Override
//                public void onLeftBtnClick(Popup1T1I3B popup) {
//                    popup.close();
//                }
//
//                @Override
//                public void onMiddleBtnClick(String value, Popup1T1I3B popup) {
//                    CardController.getInstance().verifyCard(value, (isVerified, pinAttemptsRemain) -> {
//                        if (!isVerified) {
//                            if (pinAttemptsRemain > 0) {
//                                ViewUtils.showNoticePopup("Nhập sai mã pin! Còn " + pinAttemptsRemain + " lần thử!", () -> {
//
//                                });
//                            } else {
//                                popup.close();
//                                Platform.runLater(() -> showOutOfPinAttempt());
//                            }
//
//                            return;
//                        }
//
//                        // Card connected successfully
//                        System.out.println("Card connected successfully!");
//                        btnConnectCard.setText("Bỏ thẻ");
//                        // Close the popup
//                        popup.close();
//                        Platform.runLater(() -> {
//                            getCardInfo();
//                        });
//
//                    });
//                }
//
//                @Override
//                public void onRightBtnClick(Popup1T1I3B popup) {
//                    popup.close();
//                    Platform.runLater(() -> {
//                        showChangePinPopup();
//                    });
//                }
//            };
//            popupStage.showAndWait();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static void showConfirmPopup(String content, String leftBtn, String rightBtn, OnConfirmAction action) {
        Platform.runLater(() -> {
            try {
                GlobalLoader.fxmlLoaderPopup1T2B = new FXMLLoader(HelloApplication.class.getResource("Popup_1t2b.fxml"));
                Parent root = GlobalLoader.fxmlLoaderPopup1T2B.load();
                Popup1T2B controller = GlobalLoader.fxmlLoaderPopup1T2B.getController();

                Stage popupStage = new Stage();
                popupStage.initModality(Modality.APPLICATION_MODAL); // Chặn tương tác với các cửa sổ khác
                popupStage.setTitle("Xác nhận");
                popupStage.setScene(new Scene(root));

                controller.init("Xác nhận", content, leftBtn, rightBtn, popupStage);
                controller.listener = new Popup1T2B.OnPopup1T2BListener() {
                    @Override
                    public void onLeftBtnClick(Popup1T2B popup) {
                        action.onCancel();
                        popup.close();
                    }

                    @Override
                    public void onRightBtnClick(Popup1T2B popup) {
                        action.onConfirm();
                        popup.close();
                    }
                };
                popupStage.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public interface OnAction {
        void callback();
    }

    public interface OnConfirmAction {
        void onCancel();

        void onConfirm();
    }
}
