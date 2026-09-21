package com.mycompany.clinicmanagement.ui.pages;

import com.mycompany.clinicmanagement.models.PatientModel;
import com.mycompany.clinicmanagement.ui.modals.AddPatientModal;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PatientsPage extends JPanel {

    private JTable patientTable;
    private DefaultTableModel tableModel;
    private PatientModel dataModel;
    private JTextField txtSearch;
    // Defined once to ensure consistency across search and refresh
    private final String[] columns = {
        "ID", "First Name", "Last Name", "Gender", "Contact",
        "DOB", "Address", "Medical Aid", "Blood Type", "Allergies", "Emergency Contact"
    };

    public PatientsPage() {
        this.dataModel = new PatientModel();
        setLayout(new BorderLayout(0, 20));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // --- Control Bar ---
        JPanel controlBar = new JPanel(new BorderLayout());
        controlBar.setOpaque(false);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setOpaque(false);
        txtSearch = new JTextField(20);
        JButton btnSearch = new JButton("Search");

        // Implementation of Search
        btnSearch.addActionListener(e -> {
            String query = txtSearch.getText();
            Object[][] results = dataModel.searchPatients(query);
            tableModel.setDataVector(results, columns);
        });

        searchPanel.add(new JLabel("Find Patient:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        JButton btnAdd = new JButton("+ Add New Patient");
        btnAdd.addActionListener(e -> openModal(null));

        controlBar.add(searchPanel, BorderLayout.WEST);
        controlBar.add(btnAdd, BorderLayout.EAST);

        // --- Table Setup ---
        tableModel = new DefaultTableModel(dataModel.getAllPatients(), columns);
        patientTable = new JTable(tableModel);
        patientTable.setRowHeight(40);

        // --- Action Bar ---
        JPanel actionBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionBar.setOpaque(false);

        JButton btnEdit = new JButton("Edit Selected");
        JButton btnDelete = new JButton("Delete Patient");
        btnDelete.setForeground(Color.RED);

        btnEdit.addActionListener(e -> handleEdit());
        btnDelete.addActionListener(e -> handleDelete());

        actionBar.add(btnEdit);
        actionBar.add(btnDelete);

        add(controlBar, BorderLayout.NORTH);
        add(new JScrollPane(patientTable), BorderLayout.CENTER);
        add(actionBar, BorderLayout.SOUTH);
    }

    // Helper to refresh table from database
    private void refreshTable() {
        tableModel.setDataVector(dataModel.getAllPatients(), columns);
    }

    private void openModal(Object[] existingData) {
        Window parent = SwingUtilities.getWindowAncestor(this);
        AddPatientModal modal = new AddPatientModal((Frame) parent, existingData);
        modal.setVisible(true);
        // Refresh the UI after the modal is closed to show changes
        refreshTable();
    }

    private void handleEdit() {
        int row = patientTable.getSelectedRow();
        if (row != -1) {
            int colCount = tableModel.getColumnCount();
            Object[] data = new Object[colCount];

            for (int i = 0; i < colCount; i++) {
                data[i] = tableModel.getValueAt(row, i);
            }

            openModal(data);
        } else {
            JOptionPane.showMessageDialog(this, "Select a patient from the table to edit.");
        }
    }

    private void handleDelete() {
        int row = patientTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient to delete.");
            return;
        }

        int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure? This will permanently remove this patient.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean deleted = dataModel.deletePersonRecord(id);
            if (deleted) {
                JOptionPane.showMessageDialog(this, "Patient deleted successfully.");
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error: Could not delete patient. Ensure no other records depend on this patient.",
                        "Delete Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
