package co.sun.auto.fluter.demofx.model;

public class VehicleRegister {
    public String citizenId;
    public String vehicleRegisterId;
    public String vehicleBrand;
    public String vehicleModel;
    public String vehicleColor;
    public String vehiclePlate;
    public String vehicleFrame;
    public String vehicleEngine;
    public String vehicleRegisterDate;
    public String vehicleExpiredDate;
    public String vehicleRegisterPlace;
    public String vehicleCapacity;

    public VehicleRegister() {
    }


    public VehicleRegister(String citizenId, String vehicleRegisterId, String vehicleBrand, String vehicleModel, String vehicleColor, String vehiclePlate, String vehicleFrame, String vehicleEngine, String vehicleRegisterDate, String vehicleExpiredDate, String vehicleRegisterPlace, String vehicleCapacity) {
        this.citizenId = citizenId;
        this.vehicleRegisterId = vehicleRegisterId;
        this.vehicleBrand = vehicleBrand;
        this.vehicleModel = vehicleModel;
        this.vehicleColor = vehicleColor;
        this.vehiclePlate = vehiclePlate;
        this.vehicleFrame = vehicleFrame;
        this.vehicleEngine = vehicleEngine;
        this.vehicleRegisterDate = vehicleRegisterDate;
        this.vehicleExpiredDate = vehicleExpiredDate;
        this.vehicleRegisterPlace = vehicleRegisterPlace;
        this.vehicleCapacity = vehicleCapacity;
    }

    public String getVehicleCapacity() {
        return vehicleCapacity;
    }

    public void setVehicleCapacity(String vehicleCapacity) {
        this.vehicleCapacity = vehicleCapacity;
    }

    //to string
    @Override
    public String toString() {
        return "VehicleRegister{" + "citizenId='" + citizenId + '\'' + ", vehicleBrand='" + vehicleBrand + '\'' + ", vehicleModel='" + vehicleModel + '\'' + ", vehicleColor='" + vehicleColor + '\'' + ", vehiclePlate='" + vehiclePlate + '\'' + ", vehicleFrame='" + vehicleFrame + '\'' + ", vehicleEngine='" + vehicleEngine + '\'' + ", vehicleRegisterDate='" + vehicleRegisterDate + '\'' + ", vehicleExpiredDate='" + vehicleExpiredDate + '\'' + ", vehicleRegisterPlace='" + vehicleRegisterPlace + '\'' + '}';
    }

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }

    public String getVehicleBrand() {
        return vehicleBrand;
    }

    public void setVehicleBrand(String vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public String getVehiclePlate() {
        return vehiclePlate;
    }

    public void setVehiclePlate(String vehiclePlate) {
        this.vehiclePlate = vehiclePlate;
    }

    public String getVehicleFrame() {
        return vehicleFrame;
    }

    public void setVehicleFrame(String vehicleFrame) {
        this.vehicleFrame = vehicleFrame;
    }

    public String getVehicleEngine() {
        return vehicleEngine;
    }

    public void setVehicleEngine(String vehicleEngine) {
        this.vehicleEngine = vehicleEngine;
    }

    public String getVehicleRegisterDate() {
        return vehicleRegisterDate;
    }

    public void setVehicleRegisterDate(String vehicleRegisterDate) {
        this.vehicleRegisterDate = vehicleRegisterDate;
    }

    public String getVehicleExpiredDate() {
        return vehicleExpiredDate;
    }

    public void setVehicleExpiredDate(String vehicleExpiredDate) {
        this.vehicleExpiredDate = vehicleExpiredDate;
    }

    public String getVehicleRegisterPlace() {
        return vehicleRegisterPlace;
    }

    public void setVehicleRegisterPlace(String vehicleRegisterPlace) {
        this.vehicleRegisterPlace = vehicleRegisterPlace;
    }

    public String getVehicleRegisterId() {
        return vehicleRegisterId;
    }

    public void setVehicleRegisterId(String vehicleRegisterId) {
        this.vehicleRegisterId = vehicleRegisterId;
    }
}
