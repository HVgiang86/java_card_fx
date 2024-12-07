package co.sun.auto.fluter.demofx.view.viewcontroller;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import co.sun.auto.fluter.demofx.model.Citizen;

import javafx.event.ActionEvent;;

public class SceneViewInfoCard {
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
    public OnSceneViewInfoCardListener listener = null;


    public interface OnSceneViewInfoCardListener {
        void onChangePINBtnClick(SceneViewInfoCard sceneViewInfoCard);
        void editBtnOnClick(SceneViewInfoCard sceneViewInfoCard);
        void intergrateDocumentBtnOnClick(SceneViewInfoCard sceneViewInfoCard);
        void lockCardBtnOnClick(SceneViewInfoCard sceneViewInfoCard);
        void unlockCardBtnOnClick(SceneViewInfoCard sceneViewInfoCard);
    }

    public void changePINBtnOnClick(ActionEvent actionEvent) {
        if (listener == null) {
            return;
        }
        listener.onChangePINBtnClick(this);
    }

    public void editBtnOnclick(ActionEvent actionEvent) {
        if (listener == null) {
            return;
        }
        listener.editBtnOnClick(this);

    }

    public void intergrateDocumentBtnOnClick(ActionEvent actionEvent) {
        if (listener == null) {
            return;
        }
        listener.intergrateDocumentBtnOnClick(this);
    }

    public void lockCardBtnOnClick(ActionEvent actionEvent) {
        if (listener == null) {
            return;
        }
        listener.lockCardBtnOnClick(this);
    }

    public void unlockCardBtnOnClick(ActionEvent actionEvent) {
        if (listener == null) {
            return;
        }
        listener.unlockCardBtnOnClick(this);
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
    }

    public void setAvatarImage(String url) {
        // Set image for avatarImage
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
}
