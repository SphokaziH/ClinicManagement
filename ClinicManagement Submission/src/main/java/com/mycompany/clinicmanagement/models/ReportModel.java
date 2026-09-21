package com.mycompany.clinicmanagement.models;

import com.mycompany.clinicmanagement.utils.DatabaseConnection;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ReportModel {

    // 1. Doctor Rankings (Bar Graph Data)
    public Map<String, Integer> getDoctorAppointmentRankings() {
        Map<String, Integer> data = new HashMap<>();
        String sql = "SELECT p.LastName, COUNT(a.AppointmentID) as Total "
                + "FROM appointment a "
                + "JOIN doctor d ON a.DoctorID = d.StaffID "
                + "JOIN person p ON d.StaffID = p.PersonID "
                + "GROUP BY d.StaffID ORDER BY Total DESC";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                data.put("Dr. " + rs.getString("LastName"), rs.getInt("Total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public Map<String, Double> getDepartmentEarnings() {
        Map<String, Double> data = new HashMap<>();

        // We use the exact chain: Payment -> Billing -> Appointment -> Doctor -> Staff -> Department
        String sql = "SELECT d.Name, SUM(p.Amount) as Earnings "
                + " FROM payment p "
                + " JOIN billing b ON p.BillingID = b.BillingID "
                + " JOIN appointment a ON b.AppointmentID = a.AppointmentID "
                + " JOIN doctor doc ON a.DoctorID = doc.StaffID "
                + " JOIN staff s ON doc.StaffID = s.StaffID "
                + " JOIN department d ON s.DepartmentID = d.DepartmentID "
                + " GROUP BY d.Name";

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Use "Name" here because that is what your SQL returns
                data.put(rs.getString("Name"), rs.getDouble("Earnings"));
            }
        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
        }
        return data;
    }

// 3. Most Used Medication (Pie Chart Data)
    public Map<String, Integer> getMedicationUsage() {
        Map<String, Integer> data = new HashMap<>();
        // Adjust 'medication' and 'prescription' to match your exact schema names
        String sql = "SELECT MedicationName, COUNT(*) as Count FROM prescription GROUP BY MedicationName LIMIT 5";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                data.put(rs.getString("MedicationName"), rs.getInt("Count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
}
