package com.mycompany.clinicmanagement.ui.pages;

import com.mycompany.clinicmanagement.models.MedicationModel;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

public class MedicationPage extends JPanel {

    private JTable medTable;
    private DefaultTableModel tableModel;
    private MedicationModel model;

    private JTextField txtName = new JTextField(20);
    private JTextArea txtSideEffects = new JTextArea(4, 20);

    public MedicationPage() {
        this.model = new MedicationModel();
        setLayout(new BorderLayout(0, 15));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 1. Table Setup
        String[] headers = {"Medication Name", "Side Effects"};
        tableModel = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        medTable = new JTable(tableModel);
        medTable.setRowHeight(30);
        add(new JScrollPane(medTable), BorderLayout.CENTER);

        // 2. Management Panel (Inputs & Buttons)
        JPanel southPanel = new JPanel(new BorderLayout(10, 10));
        southPanel.setOpaque(false);

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add New Medication"));
        inputPanel.add(new JLabel("Medication Name:"));
        inputPanel.add(txtName);
        inputPanel.add(new JLabel("Side Effects:"));
        inputPanel.add(new JScrollPane(txtSideEffects));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnDelete = new JButton("Delete Selected");
        JButton btnAdd = new JButton("Add to Database");

        btnDelete.setForeground(new Color(200, 0, 0)); // Dark red for delete
        btnPanel.add(btnDelete);
        btnPanel.add(btnAdd);

        southPanel.add(inputPanel, BorderLayout.CENTER);
        southPanel.add(btnPanel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);

        // --- BUTTON LOGIC WITH ERROR HANDLING ---
        btnAdd.addActionListener(e -> {
            String name = txtName.getText().trim();
            String effects = txtSideEffects.getText().trim();

            // Validation: Don't allow empty names
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a medication name.", "Input Required", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (model.addMedicine(name, effects)) {
                JOptionPane.showMessageDialog(this, name + " added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                txtName.setText("");
                txtSideEffects.setText("");
                refreshTable();
            }
        });

        btnDelete.addActionListener(e -> {
            int row = medTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a medication from the table first.", "No Selection", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String name = tableModel.getValueAt(row, 0).toString();

            // "Are you sure?" Confirmation
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to permanently delete " + name + "?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (choice == JOptionPane.YES_OPTION) {
                if (model.deleteMedicine(name)) {
                    JOptionPane.showMessageDialog(this, "Medication deleted.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
                    refreshTable();
                }
            }
        });

        refreshTable();
    }

    private void refreshTable() {
        tableModel.setDataVector(model.getAllMedicines(), new String[]{"Medication Name", "Side Effects"});
    }
}
