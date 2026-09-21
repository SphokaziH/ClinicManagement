package com.mycompany.clinicmanagement.ui.modals;

import javax.swing.*;
import java.awt.*;
import com.mycompany.clinicmanagement.*;
import com.mycompany.clinicmanagement.models.DashboardModel;
import com.mycompany.clinicmanagement.ui.components.ComboItem;
import java.time.LocalDate;

public class AddAppointmentModal extends JDialog {

    private JComboBox<ComboItem> cbPatient, cbDoctor, cbRoom;
    private JComboBox<String> cbStatus;
    private JTextField txtDate, txtTime;
    private DashboardModel dataModel = new DashboardModel();
    private boolean isEditMode;
    private String appointmentId;

    public AddAppointmentModal(Frame parent, Object[] existingData) {
        super(parent, "Appointment Details", true);
        this.isEditMode = (existingData != null);
        setSize(450, 650);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel(isEditMode ? "Edit Appointment" : "New Appointment", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(12, 1, 5, 2));
        form.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        System.out.println("Patients: " + dataModel.getPatientComboItems());

        // Dropdowns with data from tables
        cbPatient = new JComboBox<>(dataModel.getPatientComboItems().toArray(new ComboItem[0]));
        cbDoctor = new JComboBox<>(dataModel.getDoctorNames().toArray(new ComboItem[0]));
        cbRoom = new JComboBox<>(dataModel.getRoomList().toArray(new ComboItem[0]));
        cbStatus = new JComboBox<>(new String[]{"Scheduled", "Completed", "Cancelled"});

        // Date and Time (Defaulting to Current Date)
        txtDate = new JTextField(LocalDate.now().toString());
        txtTime = new JTextField("09:00:00");

        form.add(new JLabel("Patient:"));
        form.add(cbPatient);
        form.add(new JLabel("Doctor:"));
        form.add(cbDoctor);
        form.add(new JLabel("Room:"));
        form.add(cbRoom);
        form.add(new JLabel("Date (YYYY-MM-DD):"));
        form.add(txtDate);
        form.add(new JLabel("Scheduled Time (HH:mm:ss):"));
        form.add(txtTime);
        form.add(new JLabel("Status:"));
        form.add(cbStatus);

        // Pre-fill fields if editing an existing row
        if (isEditMode) {
            appointmentId = existingData[0].toString();
            selectByString(cbPatient, existingData[1].toString());
            selectByString(cbDoctor, existingData[2].toString());
            cbStatus.setSelectedItem(existingData[3].toString());
            txtDate.setText(existingData[4].toString());
            txtTime.setText(existingData[5].toString());
            selectRoomById(Integer.parseInt(existingData[6].toString()));
        }

        add(form, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton(isEditMode ? "Update" : "Save Appointment");
        btnSave.addActionListener(e -> handleSave());

        footer.add(new JButton("Cancel") {
            {
                addActionListener(al -> dispose());
            }
        });
        footer.add(btnSave);
        add(footer, BorderLayout.SOUTH);
    }

    private void selectByString(JComboBox<ComboItem> combo, String name) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).toString().equals(name)) {
                combo.setSelectedIndex(i);
                break;
            }
        }
    }

    private void selectRoomById(int id) {
        for (int i = 0; i < cbRoom.getItemCount(); i++) {
            if (cbRoom.getItemAt(i).getId() == id) {
                cbRoom.setSelectedIndex(i);
                break;
            }
        }
    }

    private void handleSave() {
        try {
            int pId = ((ComboItem) cbPatient.getSelectedItem()).getId();
            int dId = ((ComboItem) cbDoctor.getSelectedItem()).getId();
            int rNum = ((ComboItem) cbRoom.getSelectedItem()).getId();
            String date = txtDate.getText();
            String time = txtTime.getText();
            String status = cbStatus.getSelectedItem().toString();

            if (isEditMode) {
                dataModel.updateAppointment(Integer.parseInt(appointmentId), pId, dId, date, time, status, rNum);
                JOptionPane.showMessageDialog(this, "Appointment updated successfully!");
            } else {
                dataModel.insertAppointment(pId, dId, date, time, status, rNum);
                JOptionPane.showMessageDialog(this, "New appointment added successfully!");
            }

            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
