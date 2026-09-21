package com.mycompany.clinicmanagement.models;

import com.mycompany.clinicmanagement.ui.components.ComboItem;
import com.mycompany.clinicmanagement.utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StaffModel {

    // Helper to run the query with the new "DerivedRole" column for filtering
    private Object[][] executeStaffQuery(String roleFilter) {
        List<Object[]> dataList = new ArrayList<>();

        // This SQL creates a 'DerivedRole' column on the fly
        String sql = "SELECT * FROM ("
                + "  SELECT p.PersonID, p.FirstName, p.LastName, p.Gender, p.ContactDetails, "
                + "  p.Address, p.DateOfBirth, s.Salary, s.HireDate, s.DepartmentID, "
                + "  doc.LicenseNumber, n.ShiftDetails, adm.SpecificRole, "
                + "  CASE "
                + "    WHEN doc.StaffID IS NOT NULL THEN 'Doctor' "
                + "    WHEN n.StaffID IS NOT NULL THEN 'Nurse' "
                + "    WHEN adm.StaffID IS NOT NULL THEN 'Admin' "
                + "  END AS DerivedRole "
                + "  FROM Person p "
                + "  JOIN Staff s ON p.PersonID = s.StaffID "
                + "  LEFT JOIN Doctor doc ON s.StaffID = doc.StaffID "
                + "  LEFT JOIN Nurse n ON s.StaffID = n.StaffID "
                + "  LEFT JOIN Admin adm ON s.StaffID = adm.StaffID"
                + ") AS FullStaff ";

        // Add filtering logic if it's not "All Staff"
        if (roleFilter != null && !roleFilter.equals("All Staff")) {
            sql += " WHERE DerivedRole = ?";
        }

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            if (roleFilter != null && !roleFilter.equals("All Staff")) {
                ps.setString(1, roleFilter);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    dataList.add(new Object[]{
                        rs.getInt(1), // ID
                        rs.getString(2), // First
                        rs.getString(3), // Last
                        rs.getString(4), // Gender
                        rs.getString(5), // Contact
                        rs.getString(6), // Address
                        rs.getString(7), // DOB
                        rs.getString(8), // Salary
                        rs.getString(9), // HireDate
                        rs.getInt(10), // DeptID
                        rs.getString(11),// License
                        rs.getString(12),// Shift
                        rs.getString(13) // AdminRole
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataList.toArray(new Object[0][0]);
    }

    public Object[][] getAllStaff() {
        return executeStaffQuery("All Staff");
    }

    public Object[][] filterByRole(String role) {
        return executeStaffQuery(role);
    }

    public void insertStaff(String first, String last, String gender, String dob, String address,
            String contact, int deptId, String role, String salary, String special) throws SQLException {

        String sqlId = "SELECT COALESCE(MAX(PersonID), 0) + 1 FROM Person";
        String insPerson = "INSERT INTO Person (PersonID, FirstName, LastName, Gender, DateOfBirth, Address, ContactDetails) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String insStaff = "INSERT INTO Staff (StaffID, DepartmentID, Salary, HireDate) VALUES (?, ?, ?, CURDATE())";

        // Subtype SQL depends on the role
        String insSubtype = "";
        if (role.equals("Doctor")) {
            insSubtype = "INSERT INTO Doctor (StaffID, LicenseNumber) VALUES (?, ?)";
        } else if (role.equals("Nurse")) {
            insSubtype = "INSERT INTO Nurse (StaffID, ShiftDetails) VALUES (?, ?)";
        } else {
            insSubtype = "INSERT INTO Admin (StaffID, SpecificRole) VALUES (?, ?)";
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // 1. Generate the next ID (+1 logic)
                int newId;
                try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sqlId)) {
                    rs.next();
                    newId = rs.getInt(1);
                }

                // 2. Insert Person
                try (PreparedStatement ps = conn.prepareStatement(insPerson)) {
                    ps.setInt(1, newId);
                    ps.setString(2, first);
                    ps.setString(3, last);
                    ps.setString(4, gender);
                    ps.setString(5, dob);
                    ps.setString(6, address);
                    ps.setString(7, contact);
                    ps.executeUpdate();
                }

                // 3. Insert Staff
                try (PreparedStatement ps = conn.prepareStatement(insStaff)) {
                    ps.setInt(1, newId);
                    ps.setInt(2, deptId);
                    ps.setDouble(3, Double.parseDouble(salary));
                    ps.executeUpdate();
                }

                // 4. Insert Role Subtype
                try (PreparedStatement ps = conn.prepareStatement(insSubtype)) {
                    ps.setInt(1, newId);
                    ps.setString(2, special);
                    ps.executeUpdate();
                }

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    // Update and Delete remain similar, but ensure they use the correct indices
    public void updateStaff(int id, String first, String last, String gender, String dob, String address,
            String contact, int deptId, String role, String salary, String special) throws SQLException {

        String upPerson = "UPDATE Person SET FirstName=?, LastName=?, Gender=?, DateOfBirth=?, Address=?, ContactDetails=? WHERE PersonID=?";
        String upStaff = "UPDATE Staff SET DepartmentID=?, Salary=? WHERE StaffID=?";

        // Clean up any existing subtype entries first (to handle role changes)
        String delDoctor = "DELETE FROM Doctor WHERE StaffID=?";
        String delNurse = "DELETE FROM Nurse WHERE StaffID=?";
        String delAdmin = "DELETE FROM Admin WHERE StaffID=?";

        String insSubtype = "";
        if (role.equals("Doctor")) {
            insSubtype = "INSERT INTO Doctor (StaffID, LicenseNumber) VALUES (?, ?)";
        } else if (role.equals("Nurse")) {
            insSubtype = "INSERT INTO Nurse (StaffID, ShiftDetails) VALUES (?, ?)";
        } else {
            insSubtype = "INSERT INTO Admin (StaffID, SpecificRole) VALUES (?, ?)";
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Update Supertype & Staff
                try (PreparedStatement ps = conn.prepareStatement(upPerson)) {
                    ps.setString(1, first);
                    ps.setString(2, last);
                    ps.setString(3, gender);
                    ps.setString(4, dob);
                    ps.setString(5, address);
                    ps.setString(6, contact);
                    ps.setInt(7, id);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = conn.prepareStatement(upStaff)) {
                    ps.setInt(1, deptId);
                    ps.setDouble(2, Double.parseDouble(salary));
                    ps.setInt(3, id);
                    ps.executeUpdate();
                }

                // Remove from all subtypes, then insert into the correct one 
                try (PreparedStatement ps = conn.prepareStatement(delDoctor)) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = conn.prepareStatement(delNurse)) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = conn.prepareStatement(delAdmin)) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = conn.prepareStatement(insSubtype)) {
                    ps.setInt(1, id);
                    ps.setString(2, special);
                    ps.executeUpdate();
                }

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public boolean deletePersonRecord(int personId) {
        // This query now triggers a chain reaction:
        // 1. Deletes from Person
        // 2. Automatically deletes Patient/Staff entry
        // 3. Automatically deletes all their Appointments
        // 4. Automatically deletes all related Bills
        String sql = "DELETE FROM Person WHERE PersonID = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, personId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ComboItem> getAllDepartments() {
        List<ComboItem> departments = new ArrayList<>();
        String sql = "SELECT DepartmentID, Name FROM Department";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                departments.add(new ComboItem(rs.getInt(1), rs.getString(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return departments;
    }
}
