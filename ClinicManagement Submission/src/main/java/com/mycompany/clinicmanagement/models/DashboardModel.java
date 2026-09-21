package com.mycompany.clinicmanagement.models;

import com.mycompany.clinicmanagement.utils.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.clinicmanagement.ui.components.ComboItem;

public class DashboardModel {

    public Object[][] getDashboardAppointments() {
        List<Object[]> dataList = new ArrayList<>();
        String sql = "SELECT a.AppointmentID, CONCAT(p.FirstName, ' ', p.LastName) AS 'Patient Name', "
                + "CONCAT('Dr. ', d.LastName) AS 'Doctor Name', a.Status, a.AppointmentDate,a.AppointmentTime, a.RoomNumber "
                + "FROM Appointment a "
                + "JOIN Person p ON a.PatientID = p.PersonID "
                + "JOIN Person d ON a.DoctorID = d.PersonID "
                + "ORDER BY a.AppointmentDate, a.AppointmentTime";

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                dataList.add(new Object[]{
                    rs.getString(1), rs.getString(2), rs.getString(3),
                    rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dataList.toArray(new Object[0][]);
    }

    public List<ComboItem> getPatientComboItems() {
        List<ComboItem> items = new ArrayList<>();
        String sql = "SELECT p.PersonID, CONCAT(p.FirstName, ' ', p.LastName) AS fullname FROM Person p "
                + "JOIN Patient pt ON p.PersonID = pt.PatientID";

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                items.add(new ComboItem(rs.getInt(1), rs.getString(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<ComboItem> getDoctorNames() {
        List<ComboItem> items = new ArrayList<>();
        String sql = "SELECT d.StaffID,CONCAT('Dr. ', LastName) FROM Person p "
                + "JOIN Doctor d ON p.PersonID = d.StaffID";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                items.add(new ComboItem(rs.getInt(1), rs.getString(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<ComboItem> getRoomList() {
        List<ComboItem> items = new ArrayList<>();
        // RoomNumber is the ID, Concat is the display Name
        String sql = "SELECT RoomNumber, CONCAT(RoomNumber, ' - ', Type) AS RoomDisplay FROM Room";

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                items.add(new ComboItem(rs.getInt("RoomNumber"), rs.getString("RoomDisplay")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    // Logic for adding a new appointment to the database(Sql needed here)
    public void insertAppointment(int patientId, int doctorId, String date, String time, String status, int roomNumber) {
        // 1. Get the next available ID
        int nextId = 1;
        String sqlMax = "SELECT MAX(AppointmentID) FROM Appointment";
        String sqlInsert = "INSERT INTO Appointment (AppointmentID, AppointmentDate, AppointmentTime, Status, PatientID, DoctorID, RoomNumber) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Calculate Next ID
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sqlMax)) {
                if (rs.next()) {
                    nextId = rs.getInt(1) + 1;
                }
            }

            // Perform Insert
            try (PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {
                pstmt.setInt(1, nextId);
                pstmt.setString(2, date);    // Must be YYYY-MM-DD
                pstmt.setString(3, time);    // Must be HH:MM:SS
                pstmt.setString(4, status);
                pstmt.setInt(5, patientId);
                pstmt.setInt(6, doctorId);
                pstmt.setInt(7, roomNumber);

                pstmt.executeUpdate();
                System.out.println("Appointment saved successfully with ID: " + nextId);
            }
        } catch (SQLException e) {
            System.err.println("Insert Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Logic for updating an existing appointment record(Sql needed here)
    public void updateAppointment(int appointmentId, int patientId, int doctorId, String date, String time, String status, int roomNumber) {
        String sql = "UPDATE Appointment SET AppointmentDate = ?, AppointmentTime = ?, "
                + "Status = ?, PatientID = ?, DoctorID = ?, RoomNumber = ? "
                + "WHERE AppointmentID = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Setting the values from the UI
            pstmt.setString(1, date);
            pstmt.setString(2, time);
            pstmt.setString(3, status);
            pstmt.setInt(4, patientId);
            pstmt.setInt(5, doctorId);
            pstmt.setInt(6, roomNumber);
            pstmt.setInt(7, appointmentId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Appointment ID " + appointmentId + " updated successfully.");
            }

        } catch (SQLException e) {
            System.err.println("Error updating appointment: " + e.getMessage());
            e.printStackTrace();
        }
    }
    // 1. Get Total Patients

    public int getTotalPatients() {
        String sql = "SELECT COUNT(*) FROM Patient";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

// 2. Get Total Appointments
    public int getTotalAppointments() {
        String sql = "SELECT COUNT(*) FROM Appointment";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getTotalRevenue() {
        String sql = "SELECT SUM(TotalAmount) FROM Billing"; // Ensure your table name/column matches
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                double amount = rs.getDouble(1);
                return String.format("R %.2f", amount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "R 0.00";
    }

    public int getStaffCount() {

        String sql = "SELECT COUNT(*) FROM Staff";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
