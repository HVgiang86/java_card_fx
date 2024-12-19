package co.sun.auto.fluter.demofx.model;

import io.ebean.Model;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Citizen extends Model {
    @Id
    public String citizenId;
    public String fullName;
    public String gender;
    public String birthDate;
    public String address;
    public String hometown;
    public String nationality;
    public String ethnicity;
    public String religion;
    public String identification;

    public byte[] avatar;

    public Citizen() {
    }

    public Citizen(byte[] avatar) {
        this.avatar = avatar;
    }


    public Citizen(String citizenId, String fullName, String gender, String birthDate, String address, String hometown, String nationality, String ethnicity, String religion, String identification) {
        this.citizenId = citizenId;
        this.fullName = fullName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.address = address;
        this.hometown = hometown;
        this.nationality = nationality;
        this.ethnicity = ethnicity;
        this.religion = religion;
        this.identification = identification;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "Citizen{" + "citizenId='" + citizenId + '\'' + ", fullName='" + fullName + '\'' + ", gender='" + gender + "'" + ", birthDate='" + birthDate + '\'' + ", address='" + address + '\'' + ", hometown='" + hometown + '\'' + ", Nationality='" + nationality + '\'' + ", Ethnicity='" + ethnicity + '\'' + ", Religion='" + religion + '\'' + ", Identification='" + identification + '\'';
    }

    public String toCardInfo() {
        String builder = citizenId + "$" + fullName + "$" + gender + "$" + birthDate + "$" + address + "$" + hometown + "$" + nationality + "$" + ethnicity + "$" + religion + "$" + identification;
        return builder;
    }

    public void fromCardInfo(String cardInfo) {
        String[] parts = cardInfo.split("\\$");
        citizenId = parts[0];
        fullName = parts[1];
        gender = parts[2];
        birthDate = parts[3];
        address = parts[4];
        hometown = parts[5];
        nationality = parts[6];
        ethnicity = parts[7];
        religion = parts[8];
        identification = parts[9];
    }

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public StringProperty citizenIdProperty() {
        return new SimpleStringProperty(citizenId);
    }

    public StringProperty fullNameProperty() {
        return new SimpleStringProperty(fullName);
    }

    public StringProperty genderProperty() {
        return new SimpleStringProperty(gender);
    }

    public StringProperty birthDateProperty() {
        return new SimpleStringProperty(birthDate);
    }

    public StringProperty addressProperty() {
        return new SimpleStringProperty(address);
    }

    public StringProperty hometownProperty() {
        return new SimpleStringProperty(hometown);
    }

    public StringProperty nationalityProperty() {
        return new SimpleStringProperty(nationality);
    }

    public StringProperty ethnicityProperty() {
        return new SimpleStringProperty(ethnicity);
    }

    public StringProperty religionProperty() {
        return new SimpleStringProperty(religion);
    }

    public StringProperty identificationProperty() {
        return new SimpleStringProperty(identification);
    }
}
