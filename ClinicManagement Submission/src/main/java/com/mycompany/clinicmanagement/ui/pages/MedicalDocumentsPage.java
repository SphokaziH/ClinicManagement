package com.mycompany.clinicmanagement.ui.pages;

import com.mycompany.clinicmanagement.models.MedicalDocumentModel;
import com.mycompany.clinicmanagement.ui.modals.AddMedicalDocumentModal;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MedicalDocumentsPage extends JPanel {

    private JTable docTable;
    private DefaultTableModel tableModel;
    private MedicalDocumentModel model;
    private JTextField txtSearch;

    public MedicalDocumentsPage() {
        this.model = new MedicalDocumentModel();
        setLayout(new BorderLayout(0, 15));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- 1. TOP PANEL: SEARCH & ADD ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchBar.setOpaque(false);
        txtSearch = new JTextField(20);
        JButton btnSearch = new JButton("Search Patient");
        searchBar.add(new JLabel("Patient Name/ID:"));
        searchBar.add(txtSearch);
        searchBar.add(btnSearch);

        JButton btnAdd = new JButton("+ New Document");
        topPanel.add(searchBar, BorderLayout.WEST);
        topPanel.add(btnAdd, BorderLayout.EAST);

        // --- 2. CENTER: TABLE ---
        // Consistent 7-column header to match the MedicalDocumentModel results
        String[] headers = {"ID", "Patient", "Date", "Type", "Detail", "Dosage", "Notes"};
        tableModel = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        docTable = new JTable(tableModel);
        docTable.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(docTable);

        // --- 3. BOTTOM: ACTIONS ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnEdit = new JButton("Edit Selected");
        JButton btnDelete = new JButton("Delete Record");
        btnDelete.setForeground(Color.RED);
        bottomPanel.add(btnEdit);
        bottomPanel.add(btnDelete);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // --- 4. LOGIC ---
        // Search Logic
        btnSearch.addActionListener(e -> refreshTable(txtSearch.getText().trim()));

        // Add Logic: Passing null for 'data' signifies "New Record" mode
        btnAdd.addActionListener(e -> {
            AddMedicalDocumentModal modal = new AddMedicalDocumentModal(
                    (Frame) SwingUtilities.getWindowAncestor(this),
                    model,
                    null
            );
            modal.setVisible(true);
            refreshTable("");
        });

// Edit Logic Fix
        btnEdit.addActionListener(e -> {
            int row = docTable.getSelectedRow();
            if (row != -1) {
                // Get all columns from the selected row
                Object[] rowData = new Object[tableModel.getColumnCount()];
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    rowData[i] = tableModel.getValueAt(row, i);
                }

                // Pass 'this' as the parent frame and 'rowData' to the modal
                AddMedicalDocumentModal modal = new AddMedicalDocumentModal(
                        (Frame) SwingUtilities.getWindowAncestor(this), model, rowData
                );
                modal.setVisible(true);
                refreshTable(""); // Refresh after edit
            } else {
                JOptionPane.showMessageDialog(this, "Please select a row to edit.");
            }
        });

// Delete Logic with Feedback
        btnDelete.addActionListener(e -> {
            int row = docTable.getSelectedRow();
            if (row != -1) {
                String id = tableModel.getValueAt(row, 0).toString();
                if (JOptionPane.showConfirmDialog(this, "Delete Document #" + id + "?") == JOptionPane.YES_OPTION) {
                    model.deleteDocument(id);
                    JOptionPane.showMessageDialog(this, "Deleted successfully.");
                    refreshTable("");
                }
            }
        });

        refreshTable(""); // Initial load
    }

    /**
     * Refreshes the JTable using separate search and view-all functions
     * following the StaffModel pattern.
     */
    private void refreshTable(String searchTerm) {
        Object[][] data;
        if (searchTerm == null || searchTerm.isEmpty()) {
            data = model.getAllDocuments();
        } else {
            data = model.searchDocuments(searchTerm);
        }

        // Updated Headers to match the new Object[] structure
        String[] headers = {"ID", "Patient", "Date", "Type", "Test Type", "Medication", "Dosage", "Notes"};
        tableModel.setDataVector(data, headers);
    }

}
