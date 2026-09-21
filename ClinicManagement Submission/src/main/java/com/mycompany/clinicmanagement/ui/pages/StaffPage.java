package com.mycompany.clinicmanagement.ui.pages;

import com.mycompany.clinicmanagement.models.StaffModel;
import com.mycompany.clinicmanagement.ui.modals.AddStaffModal;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StaffPage extends JPanel {

    private JTable staffTable;
    private DefaultTableModel tableModel;
    private StaffModel dataModel;
    private JComboBox<String> roleFilter;

    public StaffPage() {
        this.dataModel = new StaffModel();
        setLayout(new BorderLayout(0, 20));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Control Bar
        JPanel controlBar = new JPanel(new BorderLayout());
        controlBar.setOpaque(false);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        filterPanel.setOpaque(false);
        roleFilter = new JComboBox<>(new String[]{"All Staff", "Doctor", "Nurse", "Admin"});

        roleFilter.addActionListener(e -> {
            String selected = (String) roleFilter.getSelectedItem();
            refreshTable(dataModel.filterByRole(selected));
        });

        filterPanel.add(new JLabel("Filter by Role:"));
        filterPanel.add(roleFilter);

        JButton btnAdd = new JButton("+ Add Staff Member");
        btnAdd.addActionListener(e -> openModal(null));

        controlBar.add(filterPanel, BorderLayout.WEST);
        controlBar.add(btnAdd, BorderLayout.EAST);

        // Table setup - Columns match the summary view
        tableModel = new DefaultTableModel(null, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        staffTable = new JTable(tableModel);
        staffTable.setRowHeight(40);

        // Initial data load
        refreshTable(dataModel.getAllStaff());

        JPanel actionBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionBar.setOpaque(false);
        JButton btnEdit = new JButton("Edit Staff");
        JButton btnDelete = new JButton("Remove Staff");
        btnDelete.setForeground(Color.RED);

        btnEdit.addActionListener(e -> handleEdit());
        btnDelete.addActionListener(e -> handleDelete());

        actionBar.add(btnEdit);
        actionBar.add(btnDelete);

        add(controlBar, BorderLayout.NORTH);
        add(new JScrollPane(staffTable), BorderLayout.CENTER);
        add(actionBar, BorderLayout.SOUTH);
    }

    private void openModal(Object[] fullRecord) {
        Window parent = SwingUtilities.getWindowAncestor(this);
        AddStaffModal modal = new AddStaffModal((Frame) parent, fullRecord);
        modal.setVisible(true);
        refreshTable(dataModel.getAllStaff()); // Refresh after modal closes
    }

// Inside StaffPage.java
    private final String[] columns = {
        "ID", "First Name", "Last Name", "Gender", "Contact", "Address", "DOB", "Salary", "Hire Date", "Special Info"
    };

    private void refreshTable(Object[][] rawData) {
        Object[][] displayData = new Object[rawData.length][10];
        for (int i = 0; i < rawData.length; i++) {
            System.arraycopy(rawData[i], 0, displayData[i], 0, 9); // Copy first 9 columns

            // Coalesce Special Info for the grid display
            String special = "";
            if (rawData[i][10] != null) {
                special = rawData[i][10].toString();      // License
            } else if (rawData[i][11] != null) {
                special = rawData[i][11].toString(); // Shift
            } else if (rawData[i][12] != null) {
                special = rawData[i][12].toString(); // Admin Role
            }
            displayData[i][9] = special;
        }
        tableModel.setDataVector(displayData, columns);
    }

    private void handleEdit() {
        int row = staffTable.getSelectedRow();
        if (row != -1) {
            Object selectedID = tableModel.getValueAt(row, 0);
            Object[][] allData = dataModel.getAllStaff(); // Get the 13-attribute version

            for (Object[] record : allData) {
                if (record[0].toString().equals(selectedID.toString())) {
                    openModal(record); // Pass all 13 columns to the modal
                    return;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a staff member to edit.");
        }
    }

    private void handleDelete() {
        int row = staffTable.getSelectedRow();

        if (row != -1) {
            try {
                int staffID = Integer.parseInt(tableModel.getValueAt(row, 0).toString());

                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete staff ID: " + staffID + "?",
                        "Confirm Deletion", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    boolean success = dataModel.deletePersonRecord(staffID);

                    if (success) {
                        // Refresh first so the UI reflects the change
                        refreshTable(dataModel.getAllStaff());

                        // Then show the success message
                        JOptionPane.showMessageDialog(this,
                                "Staff member " + staffID + " has been successfully removed.",
                                "Delete Successful",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Database error: Could not delete record. Check if staff has active records.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error: The selected ID is not a valid number.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a staff member to delete.");
        }
    }
}
