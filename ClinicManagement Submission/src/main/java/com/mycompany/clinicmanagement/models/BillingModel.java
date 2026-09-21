package com.mycompany.clinicmanagement.models;

import com.mycompany.clinicmanagement.ui.components.ComboItem;
import com.mycompany.clinicmanagement.utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillingModel {

    public Object[][] getAllBills() {
        List<Object[]> data = new ArrayList<>();
        String sql = "SELECT b.BillingID, b.BillingDate, b.TotalAmount, b.AppointmentID, "
                + "COALESCE(SUM(p.Amount), 0) as AmountPaid "
                + "FROM billing b "
                + "LEFT JOIN payment p ON b.BillingID = p.BillingID "
                + "GROUP BY b.BillingID";

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                double total = rs.getDouble("TotalAmount");
                double paid = rs.getDouble("AmountPaid");
                double balance = total - paid;
                String status = (balance <= 0) ? "Paid" : "Pending";

                data.add(new Object[]{
                    rs.getInt("BillingID"),
                    rs.getDate("BillingDate"),
                    total,
                    rs.getInt("AppointmentID"),
                    status,
                    balance // Hidden or used for validation
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data.toArray(new Object[0][0]);
    }

// Inside BillingModel.java
    public int insertBasePayment(int billingID, double amount) {

        String sql = "INSERT INTO payment (BillingID, Amount, PaymentDate) VALUES (?, ?, CURDATE())";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, billingID);
            ps.setDouble(2, amount);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : -1;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }


    public boolean finalizeCashPayment(int paymentID) {
        String sql = "INSERT INTO cash_payment (PaymentID) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, paymentID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    // 3. Create the Invoice
    public boolean createInvoice(int appointmentID, double amount) {
        String sql = "INSERT INTO billing (AppointmentID, TotalAmount, BillingDate) VALUES (?, ?, CURDATE())";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointmentID);
            ps.setDouble(2, amount);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean deleteBilling(int billingID) {
        String sql = "DELETE FROM billing WHERE BillingID = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, billingID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ComboItem> getUnbilledAppointments() {
        List<ComboItem> items = new ArrayList<>();

        // Using your 4-column logic with specific joins for Name and Room
        String sql = "SELECT a.AppointmentID, per.FirstName, per.LastName, r.RoomNumber "
                + "FROM appointment a "
                + "JOIN patient pat ON a.PatientID = pat.PatientID "
                + "JOIN person per ON pat.PatientID = per.PersonID "
                + // Linking Patient to Person
                "LEFT JOIN room r ON a.RoomNumber = r.RoomNumber "
                + // LEFT JOIN so we don't lose data if room is missing
                "WHERE a.Status = 'Completed' "
                + "AND a.AppointmentID NOT IN (SELECT AppointmentID FROM billing)";

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("AppointmentID");
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                String roomNum = rs.getString("RoomNumber");

                // Handle null room numbers 
                if (roomNum == null) {
                    roomNum = "N/A";
                }

          
                String displayText = String.format("Appt #%d | %s %s (Room %s)",
                        id, firstName, lastName, roomNum);

                items.add(new ComboItem(id, displayText));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching unbilled appointments: " + e.getMessage());
            e.printStackTrace();
        }
        return items;
    }
}
