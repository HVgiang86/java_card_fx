package co.sun.auto.fluter.demofx.view.viewcontroller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class EditHistoryTable {

    @FXML
    private TableView<EditHistory> tableView;

    @FXML
    private TableColumn<EditHistory, Integer> colSTT;

    @FXML
    private TableColumn<EditHistory, String> colOldInfo;

    @FXML
    private TableColumn<EditHistory, String> colNewInfo;

    @FXML
    private TableColumn<EditHistory, String> colEditDate;

    @FXML
    private TableColumn<EditHistory, String> colEditor;

    @FXML
    private Button exitButton;

    private ObservableList<EditHistory> data;

    @FXML
    public void initialize() {
        // Cấu hình các cột
        colSTT.setCellValueFactory(new PropertyValueFactory<>("stt"));
        colOldInfo.setCellValueFactory(new PropertyValueFactory<>("oldInfo"));
        colNewInfo.setCellValueFactory(new PropertyValueFactory<>("newInfo"));
        colEditDate.setCellValueFactory(new PropertyValueFactory<>("editDate"));
        colEditor.setCellValueFactory(new PropertyValueFactory<>("editor"));

        // Dữ liệu mẫu
        data = FXCollections.observableArrayList(
                new EditHistory(1, "Tên cũ", "Tên mới", "2024-12-01", "Nguyễn Văn A"),
                new EditHistory(2, "Địa chỉ cũ", "Địa chỉ mới", "2024-12-02", "Trần Thị B"),
                new EditHistory(3, "Email cũ", "Email mới", "2024-12-03", "Lê Văn C")
        );

        tableView.setItems(data);
    }

    // Phương thức xử lý nút "Thoát"
    @FXML
    private void handleExit(ActionEvent event) {
        System.exit(0); // Thoát ứng dụng
    }

    // Lớp nội bộ cho dữ liệu
    public static class EditHistory {
        private final Integer stt;
        private final String oldInfo;
        private final String newInfo;
        private final String editDate;
        private final String editor;

        public EditHistory(Integer stt, String oldInfo, String newInfo, String editDate, String editor) {
            this.stt = stt;
            this.oldInfo = oldInfo;
            this.newInfo = newInfo;
            this.editDate = editDate;
            this.editor = editor;
        }

        public Integer getStt() {
            return stt;
        }

        public String getOldInfo() {
            return oldInfo;
        }

        public String getNewInfo() {
            return newInfo;
        }

        public String getEditDate() {
            return editDate;
        }

        public String getEditor() {
            return editor;
        }
    }
}

