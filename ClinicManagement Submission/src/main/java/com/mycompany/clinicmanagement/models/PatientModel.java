package com.mycompany.clinicmanagement.models;

import com.mycompany.clinicmanagement.utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientModel {

    // Helper to extract the full attrubbutes
    private Object[] extractRecord(ResultSet rs) throws SQLException {
        return new Object[]{
            rs.getInt("PersonID"),
            rs.getString("FirstName"),
            rs.getString("LastName"),
            rs.getString("Gender"),
            rs.getString("ContactDetails"),
            rs.getString("DateOfBirth"),
            rs.getString("Address"),
            rs.getString("MedicalAidNumber"),
            rs.getString("BloodType"),
            rs.getString("Allergies"),
            rs.getString("EmergencyContactInfo")
        };
    }

    public Object[][] getAllPatients() {
        List<Object[]> dataList = new ArrayList<>();
        String sql = "SELECT p.*, pt.MedicalAidNumber, pt.BloodType, pt.Allergies, pt.EmergencyContactInfo "
                + "FROM Person p JOIN Patient pt ON p.PersonID = pt.PatientID";

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                dataList.add(extractRecord(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataList.toArray(new Object[0][0]);
    }

    public Object[][] searchPatients(String query) {
        List<Object[]> dataList = new ArrayList<>();
        String sql = "SELECT p.*, pt.MedicalAidNumber, pt.BloodType, pt.Allergies, pt.EmergencyContactInfo "
                + "FROM Person p JOIN Patient pt ON p.PersonID = pt.PatientID "
                + "WHERE CONCAT(p.FirstName, ' ', p.LastName) LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + query + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                dataList.add(extractRecord(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataList.toArray(new Object[0][0]);
    }

    public void insertPatient(String first, String last, String gender, String contact, String dob,
            String address, String medAid, String blood, String allergies, String emergency) {
        String sqlMax = "SELECT MAX(PersonID) FROM Person";
        String sqlPerson = "INSERT INTO Person (PersonID, FirstName, LastName, Gender, ContactDetails, DateOfBirth, Address) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlPatient = "INSERT INTO Patient (PatientID, MedicalAidNumber, BloodType, Allergies, EmergencyContactInfo) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            int nextId = 1;
            try (Statement s = conn.createStatement(); ResultSet rs = s.executeQuery(sqlMax)) {
                if (rs.next()) {
                    nextId = rs.getInt(1) + 1;
                }
            }

            // Step 1: Insert Supertype
            try (PreparedStatement ps = conn.prepareStatement(sqlPerson)) {
                ps.setInt(1, nextId);
                ps.setString(2, first);
                ps.setString(3, last);
                ps.setString(4, gender);
                ps.setString(5, contact);
                ps.setString(6, dob);
                ps.setString(7, address);
                ps.executeUpdate();
            }

            // Step 2: Insert Subtype
            try (PreparedStatement ps = conn.prepareStatement(sqlPatient)) {
                ps.setInt(1, nextId);
                ps.setString(2, medAid);
                ps.setString(3, blood);
                ps.setString(4, allergies);
                ps.setString(5, emergency);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePatient(int id, String first, String last, String gender, String contact, String dob,
            String address, String medAid, String blood, String allergies, String emergency) {
        String sqlPerson = "UPDATE Person SET FirstName=?, LastName=?, Gender=?, ContactDetails=?, DateOfBirth=?, Address=? WHERE PersonID=?";
        String sqlPatient = "UPDATE Patient SET MedicalAidNumber=?, BloodType=?, Allergies=?, EmergencyContactInfo=? WHERE PatientID=?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(sqlPerson)) {
                ps.setString(1, first);
                ps.setString(2, last);
                ps.setString(3, gender);
                ps.setString(4, contact);
                ps.setString(5, dob);
                ps.setString(6, address);
                ps.setInt(7, id);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement(sqlPatient)) {
                ps.setString(1, medAid);
                ps.setString(2, blood);
                ps.setString(3, allergies);
                ps.setString(4, emergency);
                ps.setInt(5, id);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean deletePersonRecord(int personId) {

        String sql = "DELETE FROM Person WHERE PersonID = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, personId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
