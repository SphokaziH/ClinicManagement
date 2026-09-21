package com.mycompany.clinicmanagement.ui.pages;

import com.mycompany.clinicmanagement.ui.modals.AddAppointmentModal;
import com.mycompany.clinicmanagement.models.DashboardModel;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DashboardPage extends JPanel {

    private JTable appointmentTable;
    private DefaultTableModel tableModel;
    private DashboardModel dataModel;

    public DashboardPage() {
        this.dataModel = new DashboardModel();

        setLayout(new BorderLayout(0, 20));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        add(createStatsGrid(), BorderLayout.NORTH);
        add(createManagementArea(), BorderLayout.CENTER);
    }

    private JPanel createStatsGrid() {
        JPanel statsGrid = new JPanel(new GridLayout(1, 4, 20, 0));
        statsGrid.setOpaque(false);

        statsGrid.add(createCard("Total Patients", String.valueOf(dataModel.getTotalPatients()), new Color(52, 152, 219)));
        statsGrid.add(createCard("Appointments", String.valueOf(dataModel.getTotalAppointments()), new Color(46, 204, 113)));
        statsGrid.add(createCard("Total Revenue", dataModel.getTotalRevenue(), new Color(241, 196, 15)));
        statsGrid.add(createCard("Total Staff", String.valueOf(dataModel.getStaffCount()), new Color(231, 76, 60)));

        return statsGrid;
    }

    public void refreshTableData() {
        // 1. Fetch fresh data from database via the model
        Object[][] freshData = dataModel.getDashboardAppointments();

        // 2. Clear and update the TableModel
        tableModel.setDataVector(freshData, new String[]{
            "Appt ID", "Patient Name", "Doctor", "Status", "Date", "Time", "Room"
        });

        // 3. Notify the UI to redraw
        tableModel.fireTableDataChanged();
    }

    private JPanel createManagementArea() {
        JPanel container = new JPanel(new BorderLayout(0, 15));
        container.setOpaque(false);

        JPanel controlBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        controlBar.setOpaque(false);

        JButton btnAddAppointment = new JButton("+ Add Appointment");
        btnAddAppointment.setPreferredSize(new Dimension(140, 35));
        btnAddAppointment.addActionListener(e -> {
            Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
            new AddAppointmentModal(parent, null).setVisible(true);
            refreshTableData();
        });

        controlBar.add(btnAddAppointment);

        // Setup table with 7 columns to match the model data
        String[] cols = {"Appt ID", "Patient Name", "Doctor", "Status", "Time", "Date", "Room"};
        tableModel = new DefaultTableModel(dataModel.getDashboardAppointments(), cols);
        appointmentTable = new JTable(tableModel);
        appointmentTable.setRowHeight(40);

        container.add(controlBar, BorderLayout.NORTH);
        container.add(new JScrollPane(appointmentTable), BorderLayout.CENTER);

        JPanel actionBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnEdit = new JButton("Edit Selected");
        btnEdit.addActionListener(e -> handleEdit());
        actionBar.add(btnEdit);
        container.add(actionBar, BorderLayout.SOUTH);

        return container;
    }

    private void handleEdit() {
        int row = appointmentTable.getSelectedRow();
        if (row != -1) {
            // Collect all 6 columns of data from the selected row
            Object[] rowData = new Object[tableModel.getColumnCount()];
            for (int i = 0; i < tableModel.getColumnCount(); i++) {
                rowData[i] = tableModel.getValueAt(row, i);
            }

            Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
            new AddAppointmentModal(parent, rowData).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please select an appointment to edit.");
        }
        refreshTableData();
    }

    private JPanel createCard(String title, String val, Color accent) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 4, 0, accent),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(Color.GRAY);
        JLabel lblVal = new JLabel(val);
        lblVal.setFont(new Font("SansSerif", Font.BOLD, 24));

        card.add(lblTitle);
        card.add(Box.createVerticalStrut(10));
        card.add(lblVal);
        return card;
    }
}
