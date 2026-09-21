package com.mycompany.clinicmanagement.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Standardize these across the group
    private static final String URL = "jdbc:mysql://localhost:3306/hospital";
    private static final String USER = "root";

    // Each member can change this locally
    private static final String PASSWORD = "Tana2005$";

    public static Connection getConnection() throws SQLException {
        // Register Driver (only needed once, but safe to keep)
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // Return a NEW connection every time
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

        conn.setAutoCommit(true);

        return conn;
    }

}
