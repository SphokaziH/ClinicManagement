package com.mycompany.clinicmanagement.ui.modals;

import com.mycompany.clinicmanagement.models.StaffModel;
import com.mycompany.clinicmanagement.ui.components.ComboItem;
import javax.swing.*;
import java.awt.*;

public class AddStaffModal extends JDialog {

    // Person Attributes
    private JTextField txtFirst = new JTextField();
    private JTextField txtLast = new JTextField();
    private JComboBox<String> cbGender = new JComboBox<>(new String[]{"M", "F", "Other"});
    private JTextField txtDOB = new JTextField();
    private JTextField txtAddress = new JTextField();
    private JTextField txtContact = new JTextField();

    // Staff & Role Attributes
    private JComboBox<ComboItem> cbDepartment = new JComboBox<>();
    private JComboBox<String> cbRole = new JComboBox<>(new String[]{"Doctor", "Nurse", "Admin"});
    private JLabel lblSpecialInfo = new JLabel("License Number:");
    private JTextField txtSpecialInfo = new JTextField();
    private JTextField txtSalary = new JTextField();

    private StaffModel model = new StaffModel();
    private boolean isEditMode;
    private String staffID;

    public AddStaffModal(Frame owner, Object[] data) {
        super(owner, data == null ? "Add Staff Member" : "Edit Staff Member", true);
        this.isEditMode = (data != null);
        setSize(450, 700);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(0, 1, 2, 2));
        form.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        // 1. Load Departments from DB first
        for (ComboItem item : model.getAllDepartments()) {
            cbDepartment.addItem(item);
        }

        // Add Person Components to form
        form.add(new JLabel("First Name:"));
        form.add(txtFirst);
        form.add(new JLabel("Last Name:"));
        form.add(txtLast);
        form.add(new JLabel("Gender:"));
        form.add(cbGender);
        form.add(new JLabel("Date of Birth (YYYY-MM-DD):"));
        form.add(txtDOB);
        form.add(new JLabel("Contact Details:"));
        form.add(txtContact);
        form.add(new JLabel("Physical Address:"));
        form.add(txtAddress);

        form.add(new JSeparator());

        // Add Staff/Role Components to form
        form.add(new JLabel("Department:"));
        form.add(cbDepartment);
        form.add(new JLabel("Role:"));
        form.add(cbRole);
        form.add(lblSpecialInfo);
        form.add(txtSpecialInfo);
        form.add(new JLabel("Monthly Salary:"));
        form.add(txtSalary);

        // Logic to update label based on role selection
        cbRole.addActionListener(e -> {
            String selected = (String) cbRole.getSelectedItem();
            if ("Doctor".equals(selected)) {
                lblSpecialInfo.setText("License Number:");
            } else if ("Nurse".equals(selected)) {
                lblSpecialInfo.setText("Shift Type (Day/Night):");
            } else {
                lblSpecialInfo.setText("Admin Position:");
            }
        });

        // 2. Pre-fill data if in Edit Mode using the 13-attribute record
        if (isEditMode) {
            this.staffID = data[0].toString();      // Index 0: ID
            txtFirst.setText(data[1].toString());   // Index 1: First Name
            txtLast.setText(data[2].toString());    // Index 2: Last Name
            cbGender.setSelectedItem(data[3].toString()); // Index 3: Gender
            txtContact.setText(data[4].toString()); // Index 4: Contact
            txtAddress.setText(data[5].toString()); // Index 5: Address
            txtDOB.setText(data[6].toString());     // Index 6: DOB
            txtSalary.setText(data[7].toString());  // Index 7: Salary

            // Select Department by matching ID (Index 9)
            int targetDeptId = Integer.parseInt(data[9].toString());
            for (int i = 0; i < cbDepartment.getItemCount(); i++) {
                if (cbDepartment.getItemAt(i).getId() == targetDeptId) {
                    cbDepartment.setSelectedIndex(i);
                    break;
                }
            }

            // Determine Role and Special Info by checking Indices 10, 11, 12
            if (data[10] != null) {
                cbRole.setSelectedItem("Doctor");
                txtSpecialInfo.setText(data[10].toString());
            } else if (data[11] != null) {
                cbRole.setSelectedItem("Nurse");
                txtSpecialInfo.setText(data[11].toString());
            } else if (data[12] != null) {
                cbRole.setSelectedItem("Admin");
                txtSpecialInfo.setText(data[12].toString());
            }
        }

        JButton btnSave = new JButton(isEditMode ? "Update Record" : "Register Staff");
        btnSave.addActionListener(e -> handleSave());

        add(new JScrollPane(form), BorderLayout.CENTER);
        add(btnSave, BorderLayout.SOUTH);
    }

    private void handleSave() {
        try {
            int deptId = ((ComboItem) cbDepartment.getSelectedItem()).getId();

            if (isEditMode) {
                model.updateStaff(
                        Integer.parseInt(staffID),
                        txtFirst.getText(), txtLast.getText(),
                        cbGender.getSelectedItem().toString(),
                        txtDOB.getText(), txtAddress.getText(), txtContact.getText(),
                        deptId, cbRole.getSelectedItem().toString(),
                        txtSalary.getText(), txtSpecialInfo.getText()
                );
            } else {
                model.insertStaff(
                        txtFirst.getText(), txtLast.getText(),
                        cbGender.getSelectedItem().toString(),
                        txtDOB.getText(), txtAddress.getText(), txtContact.getText(),
                        deptId, cbRole.getSelectedItem().toString(),
                        txtSalary.getText(), txtSpecialInfo.getText()
                );
            }
            JOptionPane.showMessageDialog(this, "Staff member record processed successfully.");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
