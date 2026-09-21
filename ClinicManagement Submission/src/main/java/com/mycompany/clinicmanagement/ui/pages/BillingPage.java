package com.mycompany.clinicmanagement.ui.pages;

import com.mycompany.clinicmanagement.models.BillingModel;
import com.mycompany.clinicmanagement.ui.components.ComboItem;
import com.mycompany.clinicmanagement.ui.modals.AddCardModal;
import com.mycompany.clinicmanagement.ui.modals.AddInsuranceModal;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BillingPage extends JPanel implements Refreshable {

    private JTable invoiceTable;
    private DefaultTableModel tableModel;
    private BillingModel model;

    private JComboBox<ComboItem> cbAppointments;
    private JTextField txtTotalAmount = new JTextField(10);
    private JTextField txtAmountToPay = new JTextField(10);
    private JComboBox<String> cbPaymentType = new JComboBox<>(new String[]{"Cash", "Card", "Insurance"});

    public BillingPage() {
        this.model = new BillingModel();
        setLayout(new BorderLayout(0, 15));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 1. TOP: GENERATE INVOICE
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.setBorder(BorderFactory.createTitledBorder("Invoicing"));
        cbAppointments = new JComboBox<>();
        JButton btnCreateInvoice = new JButton("Create Invoice");
        topPanel.add(new JLabel("Select Appointment:"));
        topPanel.add(cbAppointments);
        topPanel.add(new JLabel("Amount (R):"));
        topPanel.add(txtTotalAmount);
        topPanel.add(btnCreateInvoice);
        add(topPanel, BorderLayout.NORTH);

        // 2. CENTER: INVOICE GRID
        String[] headers = {"Inv #", "Patient", "Date", "Total", "Paid", "Balance", "Status"};
        tableModel = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        invoiceTable = new JTable(tableModel);
        add(new JScrollPane(invoiceTable), BorderLayout.CENTER);

        // 3. BOTTOM: PAYMENT PANEL
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Payments"));
        JButton btnPay = new JButton("Post Payment");
        JButton btnDelete = new JButton("Delete Bill");
        btnDelete.setForeground(Color.RED);
        bottomPanel.add(new JLabel("Amount to Pay:"));
        bottomPanel.add(txtAmountToPay);
        bottomPanel.add(new JLabel("Type:"));
        bottomPanel.add(cbPaymentType);
        bottomPanel.add(btnPay);
        bottomPanel.add(btnDelete);
        add(bottomPanel, BorderLayout.SOUTH);

        // LOGIC 
        btnDelete.addActionListener(e -> {
            int row = invoiceTable.getSelectedRow();
            if (row == -1) {
                return;
            }
            int bID = (int) tableModel.getValueAt(row, 0);

            int confirm = JOptionPane.showConfirmDialog(this, "Permanently delete Bill #" + bID + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (model.deleteBilling(bID)) {
                    refreshData();
                }
            }
        });

        btnCreateInvoice.addActionListener(e -> {
            ComboItem item = (ComboItem) cbAppointments.getSelectedItem();
            if (item == null) {
                return;
            }
            try {
                double amt = Double.parseDouble(txtTotalAmount.getText());
                if (model.createInvoice(item.getId(), amt)) {
                    JOptionPane.showMessageDialog(this, "Invoice created!");
                    refreshData();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid Amount");
            }
        });

        btnPay.addActionListener(e -> {
            int row = invoiceTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a bill from the table first.");
                return;
            }

            int bID = (int) tableModel.getValueAt(row, 0);
            double balance = (double) tableModel.getValueAt(row, 5);
            String selectedType = (String) cbPaymentType.getSelectedItem();

            try {
                double amt = Double.parseDouble(txtAmountToPay.getText().trim());
                if (amt <= 0 || amt > balance) {
                    JOptionPane.showMessageDialog(this, "Invalid amount. Max: R" + balance);
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(this,
                        "Proceed with R" + amt + " payment via " + selectedType + "?",
                        "Confirm Payment", JOptionPane.YES_NO_OPTION);

                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }

                if ("Cash".equals(selectedType)) {
                    // Cash stays as is: Create base then finalize
                    int pID = model.insertBasePayment(bID, amt);
                    if (pID != -1) {
                        model.finalizeCashPayment(pID);
                        JOptionPane.showMessageDialog(this, "Cash Payment Recorded.");
                        refreshData();
                        txtAmountToPay.setText("");
                    }
                } else if ("Card".equals(selectedType)) {
                    // Passing billingID and amount instead of pID
                    new AddCardModal((Frame) SwingUtilities.getWindowAncestor(this), bID, amt, this).setVisible(true);
                } else if ("Insurance".equals(selectedType)) {
                    // Passing billingID and amount instead of pID
                    new AddInsuranceModal((Frame) SwingUtilities.getWindowAncestor(this), bID, amt, this).setVisible(true);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid numeric amount.");
            }
        });
        refreshData();
    }

    public void refreshData() {
        // These headers must align with the data order in BillingModel.getAllBills()
        String[] headers = {"Bill ID", "Date", "Total Amount", "Appt ID", "Status", "Balance Due"};
        tableModel.setDataVector(model.getAllBills(), headers);

        cbAppointments.removeAllItems();
        for (ComboItem item : model.getUnbilledAppointments()) {
            cbAppointments.addItem(item);
        }
    }
}
