package co.sun.auto.fluter.demofx.model;

public class HealthInsurance {
    public String citizenId;
    public String insuranceId;
    public String address;
    public String createDate;
    public String expiredDate;
    public String examinationPlace;

    public HealthInsurance(String citizenId, String insuranceId, String address, String createDate, String expiredDate, String examinationPlace) {
        this.citizenId = citizenId;
        this.insuranceId = insuranceId;
        this.address = address;
        this.createDate = createDate;
        this.expiredDate = expiredDate;
        this.examinationPlace = examinationPlace;
    }

    public HealthInsurance() {
    }

    //To string
    @Override
    public String toString() {
        return "HealthInsurance{" + "citizenId='" + citizenId + '\'' + ", insuranceId='" + insuranceId + '\'' + ", address='" + address + '\'' + ", createDate='" + createDate + '\'' + ", expiredDate='" + expiredDate + '\'' + ", examinationPlace='" + examinationPlace + '\'' + '}';
    }

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }

    public String getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(String insuranceId) {
        this.insuranceId = insuranceId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        this.expiredDate = expiredDate;
    }

    public String getExaminationPlace() {
        return examinationPlace;
    }

    public void setExaminationPlace(String examinationPlace) {
        this.examinationPlace = examinationPlace;
    }
}
