package com.mycompany.clinicmanagement.ui.modals;

import com.mycompany.clinicmanagement.models.MedicalDocumentModel;
import com.mycompany.clinicmanagement.ui.components.ComboItem;
import com.mycompany.clinicmanagement.ui.pages.Refreshable;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AddMedicalDocumentModal extends JDialog implements Refreshable {

    private JComboBox<String> cbType;
    private JComboBox<ComboItem> cbPatient;
    private JComboBox<ComboItem> cbDoctor;
    private JComboBox<ComboItem> cbMedicine;
    private JPanel dynamicFieldsPanel;

    private JTextField txtField1 = new JTextField(20);
    private JTextArea txtField2 = new JTextArea(4, 20);

    private MedicalDocumentModel model;
    private Object[] existingData;
    private boolean isEditMode;

    public AddMedicalDocumentModal(Frame parent, MedicalDocumentModel model, Object[] data) {
        super(parent, data == null ? "Add Medical Document" : "Edit Medical Document", true);
        this.model = model;
        this.existingData = data;
        this.isEditMode = (data != null);

        // STABILITY FIX: Set size and layout first
        setLayout(new BorderLayout(10, 10));
        setMinimumSize(new Dimension(450, 600));

        initStaticUI();

        // Wire up the type switcher
        cbType.addActionListener(e -> {
            updateUIBasedOnType();
            pack(); // Resize window to fit new fields
            setLocationRelativeTo(getParent());
        });

        if (isEditMode) {
            populateFields();
        } else {
            updateUIBasedOnType();
        }

        setLocationRelativeTo(parent);
    }

    private void initStaticUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 1. Static Top Fields (Type, Patient, Doctor)
        JPanel staticPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        cbType = new JComboBox<>(new String[]{"Diagnosis", "Prescription", "Lab Test"});
        cbPatient = new JComboBox<>();
        cbDoctor = new JComboBox<>();
        cbMedicine = new JComboBox<>();

        // Load data into combos
        for (ComboItem p : model.getPatientItems()) {
            cbPatient.addItem(p);
        }
        for (ComboItem d : model.getDoctorItems()) {
            cbDoctor.addItem(d);
        }

        staticPanel.add(new JLabel("Document Type:"));
        staticPanel.add(cbType);
        staticPanel.add(new JLabel("Patient:"));
        staticPanel.add(cbPatient);
        staticPanel.add(new JLabel("Assign Doctor:"));
        staticPanel.add(cbDoctor);

        mainPanel.add(staticPanel, BorderLayout.NORTH);

        // 2. Dynamic Center Panel (This changes based on Type)
        dynamicFieldsPanel = new JPanel(new BorderLayout());
        dynamicFieldsPanel.setBorder(BorderFactory.createTitledBorder("Details"));
        mainPanel.add(dynamicFieldsPanel, BorderLayout.CENTER);

        // 3. Bottom Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Save Document");
        JButton btnCancel = new JButton("Cancel");

        btnSave.addActionListener(e -> handleSave());
        btnCancel.addActionListener(e -> dispose());

        btnPanel.add(btnCancel);
        btnPanel.add(btnSave);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void updateUIBasedOnType() {
        dynamicFieldsPanel.removeAll();
        String selected = (String) cbType.getSelectedItem();

        JPanel inner = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;

        if ("Prescription".equals(selected)) {
            gbc.gridx = 0;
            gbc.gridy = 0;
            inner.add(new JLabel("Medication:"), gbc);
            gbc.gridx = 1;
            gbc.weightx = 1.0;

            cbMedicine.removeAllItems();
            for (ComboItem m : model.getMedicineItems()) {
                cbMedicine.addItem(m);
            }
            inner.add(cbMedicine, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.weightx = 0;
            inner.add(new JLabel("Dosage:"), gbc);
            gbc.gridx = 1;
            gbc.weightx = 1.0;
            inner.add(new JScrollPane(txtField2), gbc);

        } else if ("Lab Test".equals(selected)) {
            gbc.gridx = 0;
            gbc.gridy = 0;
            inner.add(new JLabel("Test Name:"), gbc);
            gbc.gridx = 1;
            gbc.weightx = 1.0;
            inner.add(txtField1, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.weightx = 0;
            inner.add(new JLabel("Results:"), gbc);
            gbc.gridx = 1;
            gbc.weightx = 1.0;
            inner.add(new JScrollPane(txtField2), gbc);

        } else { // Diagnosis
            gbc.gridx = 0;
            gbc.gridy = 0;
            inner.add(new JLabel("Diagnosis:"), gbc);
            gbc.gridx = 1;
            gbc.weightx = 1.0;
            inner.add(new JScrollPane(txtField2), gbc);
        }

        dynamicFieldsPanel.add(inner, BorderLayout.NORTH);
        dynamicFieldsPanel.revalidate();
        dynamicFieldsPanel.repaint();
    }

    private void populateFields() {
        try {
            // Index 3 is the Document Type
            String type = existingData[3].toString();
            cbType.setSelectedItem(type);
            updateUIBasedOnType();

            // Index 7 is Notes (Common for all types)
            txtField2.setText(existingData[7] != null ? existingData[7].toString() : "");

            if ("Prescription".equals(type)) {
                // Index 5 is Medication Name
                String medName = existingData[5] != null ? existingData[5].toString() : "";
                for (int i = 0; i < cbMedicine.getItemCount(); i++) {
                    if (cbMedicine.getItemAt(i).toString().equals(medName)) {
                        cbMedicine.setSelectedIndex(i);
                        break;
                    }
                }
                // Index 6 is Dosage
                txtField2.setText(existingData[6] != null ? existingData[6].toString() : "");
            } else if ("Lab Test".equals(type)) {
                // Index 4 is Test Type
                txtField1.setText(existingData[4] != null ? existingData[4].toString() : "");
            }
        } catch (Exception e) {
            System.err.println("Error populating: " + e.getMessage());
        }
    }

    private void handleSave() {
        String type = (String) cbType.getSelectedItem();
        String field1 = type.equals("Prescription")
                ? cbMedicine.getSelectedItem().toString() : txtField1.getText().trim();

        if (!type.equals("Diagnosis") && field1.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please provide the Name or Medication.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 1. CONFIRMATION DIALOG
        String actionWord = isEditMode ? "update this" : "add a new";
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to " + actionWord + " medical document?",
                "Confirm Save",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return; // Exit if the user clicks No
        }

        try {
            boolean success;
            if (isEditMode) {
                int docID = Integer.parseInt(existingData[0].toString());
                success = model.updateDocument(docID, type, field1, txtField2.getText().trim());
            } else {
                ComboItem patient = (ComboItem) cbPatient.getSelectedItem();
                ComboItem doctor = (ComboItem) cbDoctor.getSelectedItem();
                success = model.insertDocument(type, java.time.LocalDate.now().toString(),
                        patient.getId(), doctor.getId(), field1, txtField2.getText().trim());
            }

            if (success) {
                // 2. SUCCESS DIALOG
                String message = isEditMode ? "Document updated successfully." : "New medical document saved successfully.";
                JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);

                dispose(); // Close the modal
            } else {
                JOptionPane.showMessageDialog(this, "The database could not save the record.", "Save Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "System Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void refreshData() {
        populateFields();
    }
}
