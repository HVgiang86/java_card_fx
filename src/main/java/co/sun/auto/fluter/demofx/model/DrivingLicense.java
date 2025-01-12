package co.sun.auto.fluter.demofx.model;

public class DrivingLicense {
    public String citizenId;
    public String licenseId;
    public String licenseLevel;
    public String createdAt;
    public String createPlace;
    public String expiredAt;
    public String createdBy;

    public DrivingLicense() {
    }

    public DrivingLicense(String licenseId, String licenseLevel, String createdAt, String createPlace, String expiredAt, String createdBy) {
        this.licenseId = licenseId;
        this.licenseLevel = licenseLevel;
        this.createdAt = createdAt;
        this.createPlace = createPlace;
        this.expiredAt = expiredAt;
        this.createdBy = createdBy;
    }

    public String getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }

    public String getLicenseLevel() {
        return licenseLevel;
    }

    public void setLicenseLevel(String licenseLevel) {
        this.licenseLevel = licenseLevel;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatePlace() {
        return createPlace;
    }

    public void setCreatePlace(String createPlace) {
        this.createPlace = createPlace;
    }

    public String getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(String expiredAt) {
        this.expiredAt = expiredAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    //To String
    @Override
    public String toString() {
        return "DrivingLicense{" +
                "licenseId='" + licenseId + '\'' +
                ", licenseLevel='" + licenseLevel + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", createPlace='" + createPlace + '\'' +
                ", expiredAt='" + expiredAt + '\'' +
                ", createdBy='" + createdBy + '\'' +
                '}';
    }

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }
}
