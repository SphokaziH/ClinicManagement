package com.mycompany.clinicmanagement.ui.pages;

import javax.swing.*;
import java.awt.*;

public class LabTestsPage extends JPanel {

    // Components
    private JTextField txtTestName; // Small
    private JTextArea txtResultDetails; // Large
    private JTextField txtTestDate, txtResultDate;
    private JComboBox<String> cbPatient, cbAppointment;

    public LabTestsPage() {
        setLayout(new BorderLayout(0, 20));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- TOP HALF: THE GRID ---
        String[] columns = {"Test Name", "Patient", "Appt ID", "Test Date", "Result Date", "Status"};
        JTable labTable = new JTable(new javax.swing.table.DefaultTableModel(new Object[][]{}, columns));
        JScrollPane tableScroll = new JScrollPane(labTable);
        tableScroll.setPreferredSize(new Dimension(0, 250));
        add(tableScroll, BorderLayout.NORTH);

        // --- BOTTOM HALF: INPUT FORM ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createTitledBorder("Enter New Lab Test Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Test Name (Small) & Patient ComboBox
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Test Name:"), gbc);
        gbc.gridx = 1;
        txtTestName = new JTextField(10);
        formPanel.add(txtTestName, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Patient:"), gbc);
        gbc.gridx = 3;
        cbPatient = new JComboBox<>(new String[]{"Select Patient..."});
        formPanel.add(cbPatient, gbc);

        // Row 1: Test Date & Appointment ComboBox
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Test Date:"), gbc);
        gbc.gridx = 1;
        txtTestDate = new JTextField(10);
        formPanel.add(txtTestDate, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Appointment:"), gbc);
        gbc.gridx = 3;
        cbAppointment = new JComboBox<>(new String[]{"Select Appointment..."});
        formPanel.add(cbAppointment, gbc);

        // Row 2: Result Date
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Result Date:"), gbc);
        gbc.gridx = 1;
        txtResultDate = new JTextField(10);
        formPanel.add(txtResultDate, gbc);

        // Row 3: Result Details (Large Text Area)
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Result Details:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3; // Stretch across columns
        gbc.gridheight = 2;
        txtResultDetails = new JTextArea(4, 20);
        txtResultDetails.setLineWrap(true);
        formPanel.add(new JScrollPane(txtResultDetails), gbc);

        // Row 5: Action Buttons
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JButton btnAdd = new JButton("Add Result");
        formPanel.add(btnAdd, gbc);

        add(formPanel, BorderLayout.CENTER);
    }
}
