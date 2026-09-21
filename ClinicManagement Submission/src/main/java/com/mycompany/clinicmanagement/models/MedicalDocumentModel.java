package com.mycompany.clinicmanagement.models;

import com.mycompany.clinicmanagement.ui.components.ComboItem;
import com.mycompany.clinicmanagement.utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicalDocumentModel {

    // --- 1. CORE EXECUTION ENGINE (The "Pattern") ---
    public Object[][] getAllDocuments() {
        List<Object[]> dataList = new ArrayList<>();
        String sql = "SELECT md.DocID, CONCAT(p.FirstName, ' ', p.LastName) AS Patient, md.Date, "
                + "CASE WHEN lt.DocID IS NOT NULL THEN 'Lab Test' "
                + "     WHEN pr.DocID IS NOT NULL THEN 'Prescription' "
                + "     ELSE 'Diagnosis' END AS DocType, "
                + "lt.TestType, pr.MedicationName, pr.Dosage, md.Notes "
                + "FROM MedicalDocument md "
                + "JOIN Person p ON md.PatientID = p.PersonID "
                + "LEFT JOIN labtest lt ON md.DocID = lt.DocID "
                + "LEFT JOIN prescription pr ON md.DocID = pr.DocID "
                + "ORDER BY md.DocID DESC";

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                dataList.add(new Object[]{
                    rs.getInt("DocID"),
                    rs.getString("Patient"),
                    rs.getDate("Date"),
                    rs.getString("DocType"),
                    rs.getString("TestType"), // Separate Column
                    rs.getString("MedicationName"), // Separate Column
                    rs.getString("Dosage"), // Separate Column
                    rs.getString("Notes") // Separate Column
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataList.toArray(new Object[0][]);
    }

    /**
     * Search specifically by Patient name or ID.
     */
    public Object[][] searchDocuments(String searchTerm) {
        List<Object[]> dataList = new ArrayList<>();
        String sql = "SELECT md.DocID, CONCAT(p.FirstName, ' ', p.LastName) AS Patient, md.Date, "
                + "CASE WHEN lt.DocID IS NOT NULL THEN 'Lab Test' "
                + "     WHEN pr.DocID IS NOT NULL THEN 'Prescription' "
                + "     ELSE 'Diagnosis' END AS DocType, "
                + "lt.TestType, pr.MedicationName, pr.Dosage, md.Notes "
                + "FROM MedicalDocument md "
                + "JOIN Person p ON md.PatientID = p.PersonID "
                + "LEFT JOIN labtest lt ON md.DocID = lt.DocID "
                + "LEFT JOIN prescription pr ON md.DocID = pr.DocID "
                + "WHERE p.FirstName LIKE ? OR p.LastName LIKE ? OR CAST(md.DocID AS CHAR) LIKE ? "
                + "ORDER BY md.DocID DESC";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String pattern = "%" + searchTerm + "%";
            pstmt.setString(1, pattern);
            pstmt.setString(2, pattern);
            pstmt.setString(3, pattern);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                dataList.add(new Object[]{
                    rs.getInt("DocID"),
                    rs.getString("Patient"),
                    rs.getDate("Date"),
                    rs.getString("DocType"),
                    rs.getString("TestType"),
                    rs.getString("MedicationName"),
                    rs.getString("Dosage"),
                    rs.getString("Notes")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataList.toArray(new Object[0][]);
    }

    // --- 2. TRANSACTIONAL INSERT/UPDATE ---
    public boolean insertDocument(String type, String date, int pID, int sID, String f1, String f2) {
        String sqlMD = "INSERT INTO MedicalDocument (Date, Notes, StaffID, PatientID) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sqlMD, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, date);
                ps.setString(2, type.equals("Diagnosis") ? "DIAGNOSIS: " + f1 + " | " + f2 : f2);
                ps.setInt(3, sID);
                ps.setInt(4, pID);
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int newID = rs.getInt(1);
                    if (type.equals("Prescription")) {
                        insertPrescription(conn, newID, pID, sID, f1, f2);
                    } else if (type.equals("Lab Test")) {
                        insertLabTest(conn, newID, pID, sID, f1);
                    }
                }
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void insertLabTest(Connection conn, int id, int pID, int sID, String testType) throws SQLException {
        String sql = "INSERT INTO labtest (DocID, TestType, PatientID, DoctorID) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, testType);
            ps.setInt(3, pID);
            ps.setInt(4, sID);
            ps.executeUpdate();
        }
    }

    private void insertPrescription(Connection conn, int id, int pID, int sID, String med, String dose) throws SQLException {
        String sql = "INSERT INTO prescription (DocID, MedicationName, Dosage, PatientID, StaffID) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, med);
            ps.setString(3, dose);
            ps.setInt(4, pID);
            ps.setInt(5, sID);
            ps.executeUpdate();
        }
    }

    public boolean updateDocument(int docID, String type, String f1, String f2) {
        String upSuper = "UPDATE MedicalDocument SET Notes = ? WHERE DocID = ?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(upSuper)) {
                ps.setString(1, f2);
                ps.setInt(2, docID);
                ps.executeUpdate();

                if (type.equals("Lab Test")) {
                    try (PreparedStatement ps2 = conn.prepareStatement("UPDATE labtest SET TestType = ? WHERE DocID = ?")) {
                        ps2.setString(1, f1);
                        ps2.setInt(2, docID);
                        ps2.executeUpdate();
                    }
                } else if (type.equals("Prescription")) {
                    try (PreparedStatement ps2 = conn.prepareStatement("UPDATE prescription SET MedicationName = ?, Dosage = ? WHERE DocID = ?")) {
                        ps2.setString(1, f1);
                        ps2.setString(2, f2);
                        ps2.setInt(3, docID);
                        ps2.executeUpdate();
                    }
                }
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void deleteDocument(String docID) {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement("DELETE FROM MedicalDocument WHERE DocID = ?")) {
            ps.setInt(1, Integer.parseInt(docID));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ComboItem> getPatientItems() {
        List<ComboItem> items = new ArrayList<>();
        // Join Patient with Person to get names
        String sql = "SELECT p.PersonID, CONCAT(p.FirstName, ' ', p.LastName) AS FullName "
                + "FROM Patient pat "
                + "JOIN Person p ON pat.PatientID = p.PersonID "
                + "ORDER BY p.LastName ASC";

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                items.add(new ComboItem(rs.getInt("PersonID"), rs.getString("FullName")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<ComboItem> getDoctorItems() {
        List<ComboItem> items = new ArrayList<>();
        // Join Doctor with Person and apply "Dr. [Surname]" format
        String sql = "SELECT p.PersonID, CONCAT('Dr. ', p.LastName) AS DrName "
                + "FROM Doctor d "
                + "JOIN Person p ON d.StaffID = p.PersonID "
                + "ORDER BY p.LastName ASC";

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                items.add(new ComboItem(rs.getInt("PersonID"), rs.getString("DrName")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<ComboItem> getMedicineItems() {
        List<ComboItem> items = new ArrayList<>();
        // Adjusted query: Selecting Name as both the 'ID' and the 'Label' for the dropdown
        String sql = "SELECT Name FROM Medication ORDER BY Name ASC";

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String medName = rs.getString("Name");
                // We use the name for both parts of the ComboItem
                items.add(new ComboItem(0, medName));
            }
            System.out.println("DEBUG: Found " + items.size() + " medicines in database.");
        } catch (SQLException e) {
            System.out.println("DATABASE ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        return items;
    }

}
