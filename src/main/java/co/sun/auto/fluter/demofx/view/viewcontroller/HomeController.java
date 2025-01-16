package co.sun.auto.fluter.demofx.view.viewcontroller;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import co.sun.auto.fluter.demofx.HelloApplication;
import co.sun.auto.fluter.demofx.controller.AppController;
import co.sun.auto.fluter.demofx.controller.CardController;
import co.sun.auto.fluter.demofx.controller.DBController;
import co.sun.auto.fluter.demofx.model.Citizen;
import co.sun.auto.fluter.demofx.util.ViewUtils;
import co.sun.auto.fluter.demofx.view.global.GlobalLoader;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class HomeController {
    private final AppController appController = AppController.getInstance();
    private final CardController cardController = CardController.getInstance();
    public VBox vboxContent;
    public Button btnConnectCard;
    public Button btnLogout;
    public ImageView imageHome;
    public TableView<Citizen> tblCitizenData;
    public TextField txtCitizenId;
    public TextField txtName;
    public ComboBox<String> cmbGender;
    public TextField txtHometown;
    public DatePicker datePickerBirth;
    public Button btnSearchCitizen;
    public Button btnRemoveFilter;

    @FXML
    private TableColumn<Citizen, String> colCitizenId;
    @FXML
    private TableColumn<Citizen, String> colFullName;
    @FXML
    private TableColumn<Citizen, String> colBirthDate;
    @FXML
    private TableColumn<Citizen, String> colGender;
    @FXML
    private TableColumn<Citizen, String> colHometown;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @FXML
    public void initialize() {
        colCitizenId.setCellValueFactory(cellData -> cellData.getValue().citizenIdProperty());
        colFullName.setCellValueFactory(cellData -> cellData.getValue().fullNameProperty());
        colBirthDate.setCellValueFactory(cellData -> cellData.getValue().birthDateProperty());
        colGender.setCellValueFactory(cellData -> cellData.getValue().genderProperty());
        colHometown.setCellValueFactory(cellData -> cellData.getValue().hometownProperty());

        // Schedule the task to update the UI every 2 seconds
        scheduler.scheduleAtFixedRate(this::updateUIinTime, 0, 2, TimeUnit.SECONDS);
    }

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
                            // Get try remaining times to check card is active or not
                            cardController.isCardActive((isCardActive) -> {
                                if (!isCardActive) {
                                    // TODO: show card lock
                                    ViewUtils.showNoticePopup("Thẻ đã bị khoá!", () -> {
                                        cardController.disconnectCardTest((isSuccess) -> {
                                            if (isSuccess) {
                                                Platform.runLater(() -> {
                                                    btnConnectCard.setText("Kết nối thẻ");
                                                    showNoCardInserted();
                                                });
                                            } else {

                                            }
                                        });
                                    });
                                    popup.close();
                                    return;
                                }

                                System.out.println("Card is active");
                                //Get CardID from card
                                String cardId = cardController.getCardId();

                                // If cannot get CitizenId from card => card is EMPTY or not initialized
                                // If has CitizenId => card is initialized => need to challenge card
                                System.out.println("[DEBUG] CardId: " + cardId);
                                if (cardId != null) {
                                    boolean isCardVerified = cardController.challengeCard(cardId);

                                    if (!isCardVerified) {
                                        // Challenge failed => card is rejected
                                        ViewUtils.showNoticePopup("Thẻ bị từ chối!", () -> {

                                        });
                                        return;
                                    }
                                }

                                System.out.println("Challenge success");

                                // Challenge success => verify pin code
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
        ViewUtils.showConfirmPopup("Rút thẻ?", "No", "Yes", new ViewUtils.OnConfirmAction() {
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
                showPopupEditInfo(true, null);
            } else {
                showInfoScene(citizen);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getCardInfoWithoutCreate() {
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
            } else {
                showInfoScene(citizen);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showErrorPinCode() {
        try {
            GlobalLoader.fxmlLoaderPopup1T1I2B = new FXMLLoader(HelloApplication.class.getResource("Popup_1t1i2b.fxml"));
            // Load popup FXML
            Parent root = GlobalLoader.fxmlLoaderPopup1T1I2B.load();

            // Lấy controller của pop-up
            Popup1T1I2B controller = GlobalLoader.fxmlLoaderPopup1T1I2B.getController();

            // Tạo cửa sổ popup
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL); // Chặn tương tác với các cửa sổ khác
            popupStage.setTitle("Nhập mã pin");
            popupStage.setScene(new Scene(root));

            controller.init("Bạn đã nhập sai mã PIN, vui lòng thử lại", "******", "Hủy", "Xác nhận", popupStage);
            controller.listener = new Popup1T1I2B.OnPopup1T1I2BListener() {
                @Override
                public void onLeftBtnClick(Popup1T1I2B popup) {
                    popup.close();
                }

                @Override
                public void onRightBtnClick(String value, Popup1T1I2B popup) {
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
                };
            };
            popupStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showOutOfPinAttempt() {
        try {
//            GlobalLoader.fxmlLoaderPopup1T2B = new FXMLLoader(HelloApplication.class.getResource("Popup_1t2b.fxml"));
//            Parent root = GlobalLoader.fxmlLoaderPopup1T2B.load();
//            Popup1T2B controller = GlobalLoader.fxmlLoaderPopup1T2B.getController();
//            Stage popupStage = new Stage();
//            popupStage.initModality(Modality.APPLICATION_MODAL);
//            popupStage.setTitle("Thông báo");
//
//            controller.init("Thông báo", "Bạn đã nhập sai mã PIN quá số lần cho phép", "OK", "Cấp lại mã pin", popupStage);
//            popupStage.setScene(new Scene(root));
//
//            controller.listener = new Popup1T2B.OnPopup1T2BListener() {
//                @Override
//                public void onLeftBtnClick(Popup1T2B popup) {
//                    popup.close();
//                }
//
//                @Override
//                public void onRightBtnClick(Popup1T2B popup) {
//                    popup.close();
//                    Platform.runLater(() -> {
//                        showChangePinPopup();
//                    });
//                }
//            };
//            popupStage.showAndWait();

            ViewUtils.showNoticePopup("Thẻ đã bị khoá do quá số lần sai cho phép!", () -> {
                Platform.runLater(this::showNoCardInserted);
            });
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

            controller.onSwitchScene = (sceneName) -> {
                Platform.runLater(() -> {
                    switchScene(sceneName);
                });
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void switchScene(String sceneName) {
        try {
            GlobalLoader.fxmlSceneViewInfoCard = new FXMLLoader(HelloApplication.class.getResource(sceneName));
            AnchorPane anchorPane = GlobalLoader.fxmlSceneViewInfoCard.load();
            vboxContent.getChildren().clear();
            vboxContent.getChildren().add(anchorPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showPopupEditInfo(boolean setUpPin, Citizen citizen) {
        try {
            GlobalLoader.fxmlPopupEditInfo = new FXMLLoader(HelloApplication.class.getResource("popup-edit-info.fxml"));
            Parent root = GlobalLoader.fxmlPopupEditInfo.load();

            PopupEditInfo controller = GlobalLoader.fxmlPopupEditInfo.getController();

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Chỉnh sửa thông tin");
            popupStage.setScene(new Scene(root));

            controller.init(popupStage, citizen);

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

                    ViewUtils.showConfirmPopup("Xác nhận huỷ?", "No", "Yes", new ViewUtils.OnConfirmAction() {
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
            controller.listener = () -> Platform.runLater(() -> showPopupEditInfo(true, null));
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

    @FXML
    private void onManageCitizenTabSelected() {
        System.out.println("Tab 'Quản lý công dân' đã được chọn!");
        // Thêm các chức năng khác bạn muốn thực hiện ở đây
        List<Citizen> citizens = DBController.getAllCitizens();

        // Fill data to table
        ObservableList<Citizen> data = FXCollections.observableArrayList(citizens);
        Platform.runLater(() -> {
            tblCitizenData.setItems(data);
        });

        // Double-click event handler
        tblCitizenData.setRowFactory(tv -> {

            TableRow<Citizen> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && !row.isEmpty()) {
                    Citizen selectedPerson = row.getItem();
                    System.out.println("Double-clicked on: " + selectedPerson.getFullName());
                    // TODO: show info
                    Platform.runLater(() -> {
                        showAdminView(selectedPerson);
                    });
                }
            });
            return row;
        });
    }

    private void showAdminView(Citizen citizen) {
        try {
            GlobalLoader.fxmlAdminView = new FXMLLoader(HelloApplication.class.getResource("scene_view_info_adminView.fxml"));
            Parent root = GlobalLoader.fxmlAdminView.load();

            AdminView controller = GlobalLoader.fxmlAdminView.getController();
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Thông tin công dân");
            popupStage.setScene(new Scene(root));

            controller.init(popupStage, citizen);

            ViewUtils.checkShowLockAndUnlockBtn(controller, citizen);

            popupStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onSearchCitizen() {
        String citizenId = txtCitizenId.getText();
        String fullName = txtName.getText();
        String gender = cmbGender.getValue();
        String birthDate = datePickerBirth.getValue() != null ? datePickerBirth.getValue().toString() : null;
        String hometown = txtHometown.getText();

        // Fetch filtered data
        List<Citizen> filteredCitizens = DBController.filterCitizens(citizenId, fullName, gender, birthDate, hometown);

        // Update TableView
        ObservableList<Citizen> citizenObservableList = FXCollections.observableArrayList(filteredCitizens);
        Platform.runLater(() -> {
            tblCitizenData.setItems(citizenObservableList);
        });
    }

    @FXML
    private void onRemoveFilter() {
        txtCitizenId.clear();
        txtName.clear();
        cmbGender.getSelectionModel().clearSelection();
        txtHometown.clear();
        datePickerBirth.getEditor().clear();
        onSearchCitizen();
    }

    private void updateUIinTime() {
        // Update UI in time
        Platform.runLater(() -> {
            // Update UI here
            updateInsertBtnText();
            getCardInfoWithoutCreate();
        });
    }

    private void updateInsertBtnText() {
        if (cardController.isCardConnected()) {
            btnConnectCard.setText("Bỏ thẻ");
        } else {
            btnConnectCard.setText("Kết nối thẻ");
        }
    }
}
