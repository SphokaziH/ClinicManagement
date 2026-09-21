package com.mycompany.clinicmanagement.ui.modals;

import com.mycompany.clinicmanagement.models.PatientModel;
import javax.swing.*;
import java.awt.*;

public class AddPatientModal extends JDialog {

    private JTextField txtFirst, txtLast, txtContact, txtDOB, txtAddress, txtMedAid, txtAllergies, txtEmergency;
    private JComboBox<String> cbGender, cbBloodType;
    private PatientModel dataModel = new PatientModel();
    private boolean isEditMode;
    private int patientID;

    public AddPatientModal(Frame owner, Object[] data) {
        super(owner, data == null ? "Add New Patient" : "Edit Patient Record", true);
        this.isEditMode = (data != null);

        setSize(550, 650);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // Initialization
        txtFirst = new JTextField();
        txtLast = new JTextField();
        cbGender = new JComboBox<>(new String[]{"M", "F", "Other"});
        txtContact = new JTextField();
        txtDOB = new JTextField();
        txtAddress = new JTextField();
        txtMedAid = new JTextField();
        cbBloodType = new JComboBox<>(new String[]{"O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-"});
        txtAllergies = new JTextField();
        txtEmergency = new JTextField();

        // Adding Fields
        form.add(new JLabel("First Name:"));
        form.add(txtFirst);
        form.add(new JLabel("Last Name:"));
        form.add(txtLast);
        form.add(new JLabel("Gender:"));
        form.add(cbGender);
        form.add(new JLabel("Contact Details:"));
        form.add(txtContact);
        form.add(new JLabel("Date of Birth (YYYY-MM-DD):"));
        form.add(txtDOB);
        form.add(new JLabel("Physical Address:"));
        form.add(txtAddress);
        form.add(new JLabel("Medical Aid #:"));
        form.add(txtMedAid);
        form.add(new JLabel("Blood Type:"));
        form.add(cbBloodType);
        form.add(new JLabel("Allergies:"));
        form.add(txtAllergies);
        form.add(new JLabel("Emergency Contact Info:"));
        form.add(txtEmergency);

        // Pre-fill if editing
        if (isEditMode) {
            patientID = Integer.parseInt(data[0].toString());
            txtFirst.setText(data[1].toString());
            txtLast.setText(data[2].toString());
            cbGender.setSelectedItem(data[3].toString());
            txtContact.setText(data[4].toString());
            txtDOB.setText(data[5].toString());
            txtAddress.setText(data[6].toString());
            txtMedAid.setText(data[7].toString());
            cbBloodType.setSelectedItem(data[8].toString());
            txtAllergies.setText(data[9].toString());
            txtEmergency.setText(data[10].toString());
        }

        add(form, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton(isEditMode ? "Update" : "Register Patient");
        btnSave.addActionListener(e -> handleSave());
        footer.add(new JButton("Cancel") {
            {
                addActionListener(a -> dispose());
            }
        });
        footer.add(btnSave);
        add(footer, BorderLayout.SOUTH);
    }

    private void handleSave() {
        try {
            if (isEditMode) {
                // We convert patientID to int here because the database expects an integer
                dataModel.updatePatient(patientID,
                        txtFirst.getText(), txtLast.getText(),
                        cbGender.getSelectedItem().toString(), txtContact.getText(),
                        txtDOB.getText(), txtAddress.getText(), txtMedAid.getText(),
                        cbBloodType.getSelectedItem().toString(), txtAllergies.getText(),
                        txtEmergency.getText());
            } else {
                dataModel.insertPatient(
                        txtFirst.getText(), txtLast.getText(),
                        cbGender.getSelectedItem().toString(), txtContact.getText(),
                        txtDOB.getText(), txtAddress.getText(), txtMedAid.getText(),
                        cbBloodType.getSelectedItem().toString(), txtAllergies.getText(),
                        txtEmergency.getText());
            }

            // If the code reaches here, it means no SQL error occurred
            JOptionPane.showMessageDialog(this, "Patient record saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (Exception e) {
            // If any error occurs in the model (like a missing table), this block runs
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to save record. Please check your database connection or data format.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
