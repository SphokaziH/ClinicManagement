package com.mycompany.clinicmanagement;

import com.mycompany.clinicmanagement.ui.MainFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class ClinicManagement {

    public static void main(String[] args) {
        // This makes the app look like a modern Windows/Mac app instead of old Java
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set Look and Feel");
        }

        // Launch the UI on the Event Dispatch Thread (standard Swing practice)
        SwingUtilities.invokeLater(() -> {
            new com.mycompany.clinicmanagement.ui.LoginForm();
        });
    }
}
