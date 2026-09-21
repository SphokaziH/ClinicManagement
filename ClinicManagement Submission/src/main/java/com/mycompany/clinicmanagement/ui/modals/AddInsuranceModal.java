package com.mycompany.clinicmanagement.ui.modals;

import com.mycompany.clinicmanagement.ui.components.ComboItem;
import com.mycompany.clinicmanagement.ui.pages.BillingPage;
import com.mycompany.clinicmanagement.utils.DatabaseConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddInsuranceModal extends JDialog {

    private JTextField txtProvider = new JTextField(20);
    private JTextField txtPolicyNumber = new JTextField(20);
    private int paymentID;
    private int billingID;
    private double amount;
    private BillingPage parentPage;

    public AddInsuranceModal(Frame parent, int bID, double amt, BillingPage page) {
        super(parent, "Insurance Details", true);

        this.billingID = bID;
        this.amount = amt;
        this.parentPage = page;

        // UI Setup
        setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        mainPanel.add(new JLabel("Insurance Provider:"));
        mainPanel.add(txtProvider);

        mainPanel.add(new JLabel("Policy Number:"));
        mainPanel.add(txtPolicyNumber);

        JButton btnSave = new JButton("Save Insurance Info");
        btnSave.setBackground(new Color(40, 167, 69)); // Green for success
        btnSave.setForeground(Color.WHITE);

        btnSave.addActionListener(e -> saveInsuranceDetails());

        add(mainPanel, BorderLayout.CENTER);
        add(btnSave, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
    }

    private void saveInsuranceDetails() {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Base Payment
            String sqlBase = "INSERT INTO payment (BillingID, Amount, PaymentDate) VALUES (?, ?, CURDATE())";
            PreparedStatement psBase = conn.prepareStatement(sqlBase, Statement.RETURN_GENERATED_KEYS);
            psBase.setInt(1, billingID);
            psBase.setDouble(2, amount);
            psBase.executeUpdate();

            ResultSet rs = psBase.getGeneratedKeys();
            if (rs.next()) {
                int pID = rs.getInt(1);

                // 2. Insurance Record
                String sqlIns = "INSERT INTO insurance (PaymentID, InsuranceProvider, PolicyNumber) VALUES (?, ?, ?)";
                PreparedStatement psIns = conn.prepareStatement(sqlIns);
                psIns.setInt(1, pID);
                psIns.setString(2, txtProvider.getText());
                psIns.setString(3, txtPolicyNumber.getText());
                psIns.executeUpdate();

                conn.commit();
                JOptionPane.showMessageDialog(this, "Insurance Payment Recorded!");
                parentPage.refreshData();
                dispose();
            }
        } catch (SQLException e) {
            if (conn != null) try {
                conn.rollback();
            } catch (SQLException ex) {
            }
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}
