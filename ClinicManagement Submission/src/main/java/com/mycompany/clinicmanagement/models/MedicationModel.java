package com.mycompany.clinicmanagement.models;

import com.mycompany.clinicmanagement.utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicationModel {

    public Object[][] getAllMedicines() {
        List<Object[]> dataList = new ArrayList<>();
        // Removed MedicineID from the query
        String sql = "SELECT Name, SideEffects FROM Medication ORDER BY Name ASC";

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                dataList.add(new Object[]{
                    rs.getString("Name"),
                    rs.getString("SideEffects")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataList.toArray(new Object[0][0]);
    }

    public boolean addMedicine(String name, String sideEffects) {
        String sql = "INSERT INTO Medication (Name, SideEffects) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, sideEffects);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteMedicine(String name) {
        // We use Name to delete since there is no ID
        String sql = "DELETE FROM Medication WHERE Name = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
