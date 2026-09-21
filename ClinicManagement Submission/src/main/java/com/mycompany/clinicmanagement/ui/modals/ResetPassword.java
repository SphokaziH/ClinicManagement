package com.mycompany.clinicmanagement.ui.modals;

import com.mycompany.clinicmanagement.ui.LoginForm;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ResetPassword extends JDialog implements ActionListener {

    private JTextField txtUser;
    private JPasswordField txtPass;
    private JLabel lblUser, lblPass, lblTitle;
    private JButton btnReset, btnExit;
    private LoginForm parentFrame;

    public ResetPassword(LoginForm parent) {
        super(parent, "Reset Credentials", true);
        this.parentFrame = parent;

        setUndecorated(true); // Removes the default white border/bar for a cleaner look
        setSize(600, 450);
        setLocationRelativeTo(parent);

        ImageIcon bgImage = new ImageIcon("src/main/java/images/clinic11.jpg");
        JPanel background = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bgImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        background.setLayout(null);
        setContentPane(background);

        // EXIT BUTTON  
        btnExit = new JButton("X");
        btnExit.setBounds(550, 10, 40, 30);
        btnExit.setBackground(new Color(200, 0, 0));
        btnExit.setForeground(Color.WHITE);
        btnExit.setFocusable(false);
        btnExit.addActionListener(e -> dispose());
        background.add(btnExit);

        // MAIN PANEL 
        JPanel resetPanel = new JPanel(null);
        resetPanel.setBackground(new Color(70, 130, 180));
        resetPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        resetPanel.setBounds(50, 50, 500, 350);
        background.add(resetPanel);

        // Title
        lblTitle = new JLabel("RESET PASSWORD", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setBounds(0, 30, 500, 40);
        resetPanel.add(lblTitle);

        // USERNAME LABEL & FIELD
        lblUser = new JLabel("New Username:");
        lblUser.setFont(new Font("Arial", Font.BOLD, 14));
        lblUser.setBounds(50, 120, 150, 30);
        resetPanel.add(lblUser);

        txtUser = new JTextField();
        txtUser.setBounds(200, 120, 240, 35);
        resetPanel.add(txtUser);

        // PASSWORD LABEL & FIELD
        lblPass = new JLabel("New Password:");
        lblPass.setFont(new Font("Arial", Font.BOLD, 14));
        lblPass.setBounds(50, 190, 150, 30);
        resetPanel.add(lblPass);

        txtPass = new JPasswordField();
        txtPass.setBounds(200, 190, 240, 35);
        resetPanel.add(txtPass);

        // RESET BUTTON
        btnReset = new JButton("Update & Login");
        btnReset.setFont(new Font("Arial", Font.BOLD, 14));
        btnReset.setBounds(160, 270, 180, 40);
        btnReset.addActionListener(this);
        resetPanel.add(btnReset);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String u = txtUser.getText();
        String p = new String(txtPass.getPassword());

        if (!u.isEmpty() && !p.isEmpty()) {
            LoginForm.saveToTextFile(u, p);
            JOptionPane.showMessageDialog(this, "Success! Credentials updated.");
            dispose();
            parentFrame.triggerTransition();
        } else {
            JOptionPane.showMessageDialog(this, "Please enter both details.");
        }
    }
}
