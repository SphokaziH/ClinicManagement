package com.mycompany.clinicmanagement.ui.modals;

import com.mycompany.clinicmanagement.ui.pages.BillingPage;
import com.mycompany.clinicmanagement.utils.DatabaseConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AddCardModal extends JDialog {

    private JTextField txtCardNum = new JTextField(20);
    private JTextField txtCardType = new JTextField(10);
    private int paymentID;

    private int billingID;
    private double amount;
    private BillingPage parentPage;

    public AddCardModal(Frame parent, int bID, double amt, BillingPage page) {
        super(parent, "Card Details", true);
        this.billingID = bID;
        this.amount = amt;
        this.parentPage = page;
        setLayout(new GridLayout(3, 2, 10, 10));
        setSize(300, 200);

        add(new JLabel("Card Number:"));
        add(txtCardNum);
        add(new JLabel("Card Type:"));
        add(txtCardType);

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(e -> saveDetails());
        add(btnSave);

        setLocationRelativeTo(parent);
    }

    private void saveDetails() {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // 1. Create Base Payment
            String sqlBase = "INSERT INTO payment (BillingID, Amount, PaymentDate) VALUES (?, ?, CURDATE())";
            PreparedStatement psBase = conn.prepareStatement(sqlBase, Statement.RETURN_GENERATED_KEYS);
            psBase.setInt(1, billingID);
            psBase.setDouble(2, amount);
            psBase.executeUpdate();

            ResultSet rs = psBase.getGeneratedKeys();
            if (rs.next()) {
                int pID = rs.getInt(1);

                // 2. Create Card Payment
                String sqlCard = "INSERT INTO card (PaymentID, CardNumber, CardType) VALUES (?, ?, ?)";
                PreparedStatement psCard = conn.prepareStatement(sqlCard);
                psCard.setInt(1, pID);
                psCard.setString(2, txtCardNum.getText());
                psCard.setString(3, txtCardType.getText());
                psCard.executeUpdate();

                conn.commit(); // Save everything
                JOptionPane.showMessageDialog(this, "Card Payment Successful!");
                parentPage.refreshData(); // Refresh the main table
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
