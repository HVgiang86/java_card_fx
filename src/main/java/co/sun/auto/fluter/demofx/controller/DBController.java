package co.sun.auto.fluter.demofx.controller;

import co.sun.auto.fluter.demofx.model.Citizen;

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

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
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
                return rs.getString("rsaPublicKey");
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
}