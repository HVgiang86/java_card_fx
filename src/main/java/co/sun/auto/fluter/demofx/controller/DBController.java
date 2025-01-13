package co.sun.auto.fluter.demofx.controller;

import co.sun.auto.fluter.demofx.model.Citizen;
import co.sun.auto.fluter.demofx.model.DrivingLicense;
import co.sun.auto.fluter.demofx.model.HealthInsurance;
import co.sun.auto.fluter.demofx.model.VehicleRegister;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class DBController {

    private static final String URL = "jdbc:sqlite:database.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void createTableIfNotExists() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS Citizen (" +
                "citizenId VARCHAR(12) PRIMARY KEY, " +
                "fullName VARCHAR(255), " +
                "gender VARCHAR(10), " +
                "birthDate VARCHAR(10), " +
                "address VARCHAR(255), " +
                "hometown VARCHAR(255), " +
                "nationality VARCHAR(255), " +
                "ethnicity VARCHAR(255), " +
                "religion VARCHAR(255), " +
                "identification VARCHAR(255), " +
                "publicKey TEXT)";

        // Create DrivingLicense table with id of drving license is auto increment
        String createTableDrivingLicenseSQL = "CREATE TABLE IF NOT EXISTS DrivingLicense (" +
                "licenseId VARCHAR(12) PRIMARY KEY, " +
                "citizenId VARCHAR(12), " +
                "licenseLevel VARCHAR(10), " +
                "createdAt VARCHAR(10), " +
                "createPlace VARCHAR(255), " +
                "expiredAt VARCHAR(10), " +
                "createdBy VARCHAR(255), " +
                "FOREIGN KEY (citizenId) REFERENCES Citizen(citizenId))";

        // Create VehicleRegister table with id of vehicle register is auto increment
        String createTableVehicleRegisterSQL = "CREATE TABLE IF NOT EXISTS VehicleRegister (" +
                "vehicleRegisterId INT AUTO_INCREMENT PRIMARY KEY, " +
                "citizenId VARCHAR(12), " +
                "vehicleBrand VARCHAR(255), " +
                "vehicleModel VARCHAR(255), " +
                "vehicleColor VARCHAR(255), " +
                "vehiclePlate VARCHAR(255), " +
                "vehicleFrame VARCHAR(255), " +
                "vehicleEngine VARCHAR(255), " +
                "vehicleRegisterDate VARCHAR(10), " +
                "vehicleExpiredDate VARCHAR(10), " +
                "vehicleRegisterPlace VARCHAR(255), " +
                "vehicleCapacity VARCHAR(10), " +
                "FOREIGN KEY (citizenId) REFERENCES Citizen(citizenId))";

        // Create HealthInsurance table with id of health insurance is auto increment
        String createTableHealthInsuranceSQL = "CREATE TABLE IF NOT EXISTS HealthInsurance (" +
                "insuranceId INT AUTO_INCREMENT PRIMARY KEY, " +
                "citizenId VARCHAR(12), " +
                "address VARCHAR(255), " +
                "createDate VARCHAR(10), " +
                "expiredDate VARCHAR(10), " +
                "examinationPlace VARCHAR(255), " +
                "FOREIGN KEY (citizenId) REFERENCES Citizen(citizenId))";


        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            stmt.execute(createTableDrivingLicenseSQL);
            stmt.execute(createTableVehicleRegisterSQL);
            stmt.execute(createTableHealthInsuranceSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Citizen getCitizenById(String citizenId) {
        String querySQL = "SELECT * FROM Citizen WHERE citizenId = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(querySQL)) {
            pstmt.setString(1, citizenId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Citizen(
                        rs.getString("citizenId"),
                        rs.getString("fullName"),
                        rs.getString("gender"),
                        rs.getString("birthDate"),
                        rs.getString("address"),
                        rs.getString("hometown"),
                        rs.getString("nationality"),
                        rs.getString("ethnicity"),
                        rs.getString("religion"),
                        rs.getString("identification")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void insertCitizen(Citizen citizen) {
        String insertSQL = "INSERT INTO Citizen (citizenId, fullName, gender, birthDate, address, hometown, nationality, ethnicity, religion, identification) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, citizen.getCitizenId());
            pstmt.setString(2, citizen.getFullName());
            pstmt.setString(3, citizen.getGender());
            pstmt.setString(4, citizen.getBirthDate());
            pstmt.setString(5, citizen.getAddress());
            pstmt.setString(6, citizen.getHometown());
            pstmt.setString(7, citizen.getNationality());
            pstmt.setString(8, citizen.getEthnicity());
            pstmt.setString(9, citizen.getReligion());
            pstmt.setString(10, citizen.getIdentification());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateCitizen(Citizen citizen) {
        String updateSQL = "UPDATE Citizen SET fullName = ?, gender = ?, birthDate = ?, address = ?, hometown = ?, nationality = ?, ethnicity = ?, religion = ?, identification = ? WHERE citizenId = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            pstmt.setString(1, citizen.getFullName());
            pstmt.setString(2, citizen.getGender());
            pstmt.setString(3, citizen.getBirthDate());
            pstmt.setString(4, citizen.getAddress());
            pstmt.setString(5, citizen.getHometown());
            pstmt.setString(6, citizen.getNationality());
            pstmt.setString(7, citizen.getEthnicity());
            pstmt.setString(8, citizen.getReligion());
            pstmt.setString(9, citizen.getIdentification());
            pstmt.setString(11, citizen.getCitizenId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteCitizen(String citizenId) {
        String deleteSQL = "DELETE FROM Citizen WHERE citizenId = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            pstmt.setString(1, citizenId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getPublicKeyById(String citizenId) {
        String querySQL = "SELECT publicKey FROM Citizen WHERE citizenId = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(querySQL)) {
            pstmt.setString(1, citizenId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("publicKey");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void updatePublicKey(String citizenId, String rsaPublicKey) {
        String updateSQL = "UPDATE Citizen SET publicKey = ? WHERE citizenId = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            pstmt.setString(1, rsaPublicKey);
            pstmt.setString(2, citizenId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isCitizenIdExists(String citizenId) {
        String querySQL = "SELECT 1 FROM Citizen WHERE citizenId = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(querySQL)) {
            pstmt.setString(1, citizenId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getLatestCitizenId() {
        String querySQL = "SELECT citizenId FROM Citizen ORDER BY ROWID DESC LIMIT 1";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(querySQL)) {
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println(rs.getString("citizenId"));
                return rs.getString("citizenId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get all citizens
    public static List<Citizen> getAllCitizens() {
        List<Citizen> citizens = new ArrayList<>();
        String querySQL = "SELECT * FROM Citizen";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(querySQL);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Citizen citizen = new Citizen(
                        rs.getString("citizenId"),
                        rs.getString("fullName"),
                        rs.getString("gender"),
                        rs.getString("birthDate"),
                        rs.getString("address"),
                        rs.getString("hometown"),
                        rs.getString("nationality"),
                        rs.getString("ethnicity"),
                        rs.getString("religion"),
                        rs.getString("identification")
                );
                citizens.add(citizen);
            }
            System.out.println(citizens);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return citizens;
    }

    // Get filter citizens
    public static List<Citizen> filterCitizens(String citizenId, String fullName, String gender, String birthDate, String hometown) {
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM Citizen WHERE 1=1");
        List<String> parameters = new ArrayList<>();

        // Add filters dynamically
        if (citizenId != null && !citizenId.isEmpty()) {
            queryBuilder.append(" AND citizenId LIKE ?");
            parameters.add("%" + citizenId + "%");
        }
        if (fullName != null && !fullName.isEmpty()) {
            queryBuilder.append(" AND fullName LIKE ?");
            parameters.add("%" + fullName + "%");
        }
        if (gender != null && !gender.isEmpty()) {
            queryBuilder.append(" AND gender = ?");
            parameters.add(gender);
        }
        if (birthDate != null && !birthDate.isEmpty()) {
            queryBuilder.append(" AND birthDate = ?");
            parameters.add(birthDate);
        }
        if (hometown != null && !hometown.isEmpty()) {
            queryBuilder.append(" AND hometown LIKE ?");
            parameters.add("%" + hometown + "%");
        }

        List<Citizen> citizens = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(queryBuilder.toString())) {
            // Set parameters
            for (int i = 0; i < parameters.size(); i++) {
                pstmt.setString(i + 1, parameters.get(i));
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                citizens.add(new Citizen(
                        rs.getString("citizenId"),
                        rs.getString("fullName"),
                        rs.getString("gender"),
                        rs.getString("birthDate"),
                        rs.getString("address"),
                        rs.getString("hometown"),
                        rs.getString("nationality"),
                        rs.getString("ethnicity"),
                        rs.getString("religion"),
                        rs.getString("identification")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return citizens;
    }

    //TODO: Implement editDrivingLicense
    public static boolean editDrivingLicense(String citizenId, DrivingLicense drivingLicense) {
        return true;
    }

    // Insert driving license
    public static boolean insertDrivingLicense(DrivingLicense drivingLicense) {
        String insertSQL = "INSERT INTO DrivingLicense (licenseId, citizenId, licenseLevel, createdAt, createPlace, expiredAt, createdBy) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, drivingLicense.getLicenseId());
            pstmt.setString(2, drivingLicense.getCitizenId());
            pstmt.setString(3, drivingLicense.getLicenseLevel());
            pstmt.setString(4, drivingLicense.getCreatedAt());
            pstmt.setString(5, drivingLicense.getCreatePlace());
            pstmt.setString(6, drivingLicense.getExpiredAt());
            pstmt.setString(7, drivingLicense.getCreatedBy());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //TODO Remove driving license
    public static boolean removeDrivingLicense(String citizenId) {
        // Remove driving license from database
        String deleteSQL = "DELETE FROM DrivingLicense WHERE citizenId = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            pstmt.setString(1, citizenId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static DrivingLicense getDrivingLicense(String citizenId) {
//        return new DrivingLicense("1234567890", "A1", "08/06/2002", "Hà Nội", "08/06/2002", "Admin");
        String querySQL = "SELECT * FROM DrivingLicense WHERE citizenId = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(querySQL)) {
            pstmt.setString(1, citizenId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new DrivingLicense(
                        rs.getString("citizenId"),
                        rs.getString("licenseId"),
                        rs.getString("licenseLevel"),
                        rs.getString("createdAt"),
                        rs.getString("createPlace"),
                        rs.getString("expiredAt"),
                        rs.getString("createdBy")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean updateDrivingLicense(String citizenId, DrivingLicense drivingLicense) {
        // Update driving license in database
        String updateSQL = "UPDATE DrivingLicense SET licenseId = ?, licenseLevel = ?, createdAt = ?, createPlace = ?, expiredAt = ?, createdBy = ? WHERE citizenId = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            pstmt.setString(1, drivingLicense.getLicenseId());
            pstmt.setString(2, drivingLicense.getLicenseLevel());
            pstmt.setString(3, drivingLicense.getCreatedAt());
            pstmt.setString(4, drivingLicense.getCreatePlace());
            pstmt.setString(5, drivingLicense.getExpiredAt());
            pstmt.setString(6, drivingLicense.getCreatedBy());
            pstmt.setString(7, citizenId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean insertVehicleRegister(VehicleRegister vehicleRegister) {
        String insertSQL = "INSERT INTO VehicleRegister (citizenId, vehicleBrand, vehicleModel, vehicleColor, vehiclePlate, vehicleFrame, vehicleEngine, vehicleRegisterDate, vehicleExpiredDate, vehicleRegisterPlace, vehicleCapacity) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, vehicleRegister.getCitizenId());
            pstmt.setString(2, vehicleRegister.getVehicleBrand());
            pstmt.setString(3, vehicleRegister.getVehicleModel());
            pstmt.setString(4, vehicleRegister.getVehicleColor());
            pstmt.setString(5, vehicleRegister.getVehiclePlate());
            pstmt.setString(6, vehicleRegister.getVehicleFrame());
            pstmt.setString(7, vehicleRegister.getVehicleEngine());
            pstmt.setString(8, vehicleRegister.getVehicleRegisterDate());
            pstmt.setString(9, vehicleRegister.getVehicleExpiredDate());
            pstmt.setString(10, vehicleRegister.getVehicleRegisterPlace());
            pstmt.setString(11, vehicleRegister.getVehicleCapacity());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static VehicleRegister getVehicleRegister(String citizenId) {
//        return new VehicleRegister("1234567890", "12345", "Toyota", "Camry", "Black", "29A-12345", "1234567890", "1234567890", "08/06/2002", "08/06/2002", "Hà Nội", "5");
        String querySQL = "SELECT * FROM VehicleRegister WHERE citizenId = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(querySQL)) {
            pstmt.setString(1, citizenId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String citizenId1 = rs.getString("citizenId");
                String vehicleRegisterId = rs.getString("vehicleRegisterId");
                String vehicleBrand = rs.getString("vehicleBrand");
                String vehicleModel = rs.getString("vehicleModel");
                String vehicleColor = rs.getString("vehicleColor");
                String vehiclePlate = rs.getString("vehiclePlate");
                String vehicleFrame = rs.getString("vehicleFrame");
                String vehicleEngine = rs.getString("vehicleEngine");
                String vehicleRegisterDate = rs.getString("vehicleRegisterDate");
                String vehicleExpiredDate = rs.getString("vehicleExpiredDate");
                String vehicleRegisterPlace = rs.getString("vehicleRegisterPlace");
                String vehicleCapacity = rs.getString("vehicleCapacity");

                if (vehicleRegisterId == null) {
                    vehicleRegisterId = "0";
                }

                int vehicleRegisterIdInt = Integer.parseInt(vehicleRegisterId);

                VehicleRegister vehicleRegister = new VehicleRegister();
                vehicleRegister.setCitizenId(citizenId1);
                vehicleRegister.setVehicleRegisterId(vehicleRegisterIdInt);
                vehicleRegister.setVehicleBrand(vehicleBrand);
                vehicleRegister.setVehicleModel(vehicleModel);
                vehicleRegister.setVehicleColor(vehicleColor);
                vehicleRegister.setVehiclePlate(vehiclePlate);
                vehicleRegister.setVehicleFrame(vehicleFrame);
                vehicleRegister.setVehicleEngine(vehicleEngine);
                vehicleRegister.setVehicleRegisterDate(vehicleRegisterDate);
                vehicleRegister.setVehicleExpiredDate(vehicleExpiredDate);
                vehicleRegister.setVehicleRegisterPlace(vehicleRegisterPlace);
                vehicleRegister.setVehicleCapacity(vehicleCapacity);

                return vehicleRegister;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean removeVehicleRegister(String citizenId) {
        String deleteSQL = "DELETE FROM VehicleRegister WHERE citizenId = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            pstmt.setString(1, citizenId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateVehicleRegister(String citizenId, VehicleRegister vehicleRegister) {
        String updateSQL = "UPDATE VehicleRegister SET vehicleBrand = ?, vehicleModel = ?, vehicleColor = ?, vehiclePlate = ?, vehicleFrame = ?, vehicleEngine = ?, vehicleRegisterDate = ?, vehicleExpiredDate = ?, vehicleRegisterPlace = ?, vehicleCapacity = ? WHERE citizenId = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            pstmt.setString(1, vehicleRegister.getVehicleBrand());
            pstmt.setString(2, vehicleRegister.getVehicleModel());
            pstmt.setString(3, vehicleRegister.getVehicleColor());
            pstmt.setString(4, vehicleRegister.getVehiclePlate());
            pstmt.setString(5, vehicleRegister.getVehicleFrame());
            pstmt.setString(6, vehicleRegister.getVehicleEngine());
            pstmt.setString(7, vehicleRegister.getVehicleRegisterDate());
            pstmt.setString(8, vehicleRegister.getVehicleExpiredDate());
            pstmt.setString(9, vehicleRegister.getVehicleRegisterPlace());
            pstmt.setString(10, vehicleRegister.getVehicleCapacity());
            pstmt.setString(11, citizenId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
    }
        return false;
    }

    public static boolean insertHealthInsurance(HealthInsurance healthInsurance) {
        String insertSQL = "INSERT INTO HealthInsurance (citizenId, insuranceId, address, createDate, expiredDate, examinationPlace) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, healthInsurance.getCitizenId());
            pstmt.setString(2, healthInsurance.getInsuranceId());
            pstmt.setString(3, healthInsurance.getAddress());
            pstmt.setString(4, healthInsurance.getCreateDate());
            pstmt.setString(5, healthInsurance.getExpiredDate());
            pstmt.setString(6, healthInsurance.getExaminationPlace());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static HealthInsurance getHealthInsurance(String citizenId) {
        String querySQL = "SELECT * FROM HealthInsurance WHERE citizenId = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(querySQL)) {
            pstmt.setString(1, citizenId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new HealthInsurance(
                        rs.getString("citizenId"),
                        rs.getString("insuranceId"),
                        rs.getString("address"),
                        rs.getString("createDate"),
                        rs.getString("expiredDate"),
                        rs.getString("examinationPlace")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean removeHealthInsurance(String citizenId) {
        String deleteSQL = "DELETE FROM HealthInsurance WHERE citizenId = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            pstmt.setString(1, citizenId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateHealthInsurance(String citizenId, HealthInsurance healthInsurance) {
        String updateSQL = "UPDATE HealthInsurance SET insuranceId = ?, address = ?, createDate = ?, expiredDate = ?, examinationPlace = ? WHERE citizenId = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            pstmt.setString(1, healthInsurance.getInsuranceId());
            pstmt.setString(2, healthInsurance.getAddress());
            pstmt.setString(3, healthInsurance.getCreateDate());
            pstmt.setString(4, healthInsurance.getExpiredDate());
            pstmt.setString(5, healthInsurance.getExaminationPlace());
            pstmt.setString(6, citizenId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
