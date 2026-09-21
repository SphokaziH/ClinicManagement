package com.mycompany.clinicmanagement.ui;

import com.mycompany.clinicmanagement.ui.modals.ResetPassword;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;
import javax.swing.*;

public class LoginForm extends JFrame implements ActionListener {

    private static final String FILE_PATH = "credentials.txt";
    public static String globalUser;
    public static String globalPass;

    private JTextField txtUser;
    private JPasswordField txtPass;
    private JLabel lblUser, lblPass, lblTitle;
    private JButton btnLogin, btnForgot, btnExit;
    private JPanel loginPanel;

    public LoginForm() {
        initCredentials();

        setUndecorated(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height);
        setLayout(null);

        // Background
        ImageIcon bgImage = new ImageIcon("src/main/java/images/clinic11.jpg");
        Image scaledImg = bgImage.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
        JLabel background = new JLabel(new ImageIcon(scaledImg));
        setContentPane(background);
        background.setLayout(null);

        // System Exit Button
        btnExit = new JButton("EXIT SYSTEM");
        btnExit.setBounds(screenSize.width - 160, 20, 140, 40);
        btnExit.setBackground(new Color(180, 0, 0));
        btnExit.setForeground(Color.WHITE);
        btnExit.setFont(new Font("Arial", Font.BOLD, 12));
        btnExit.addActionListener(e -> System.exit(0));
        background.add(btnExit);

        // Login Panel
        loginPanel = new JPanel(null);
        loginPanel.setBackground(new Color(70, 130, 180));
        loginPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        loginPanel.setBounds((screenSize.width - 500) / 2, (screenSize.height - 300) / 2, 500, 300);
        background.add(loginPanel);

        lblTitle = new JLabel("CLINIC LOGIN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitle.setBounds(0, 20, 500, 50);
        loginPanel.add(lblTitle);

        // Username Label & Field
        lblUser = new JLabel("Username:");
        lblUser.setFont(new Font("Arial", Font.BOLD, 14));
        lblUser.setBounds(50, 100, 100, 30);
        loginPanel.add(lblUser);

        txtUser = new JTextField();
        txtUser.setBounds(160, 100, 260, 35);
        loginPanel.add(txtUser);

        // Password Label & Field
        lblPass = new JLabel("Password:");
        lblPass.setFont(new Font("Arial", Font.BOLD, 14));
        lblPass.setBounds(50, 160, 100, 30);
        loginPanel.add(lblPass);

        txtPass = new JPasswordField();
        txtPass.setBounds(160, 160, 260, 35);
        loginPanel.add(txtPass);

        // Buttons
        btnLogin = new JButton("Login");
        btnLogin.setBounds(120, 230, 110, 40);
        btnLogin.addActionListener(this);
        loginPanel.add(btnLogin);

        btnForgot = new JButton("Forgot Password?");
        btnForgot.setBounds(240, 230, 160, 40);
        btnForgot.addActionListener(this);
        loginPanel.add(btnForgot);

        setVisible(true);
    }

    private void initCredentials() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            saveToTextFile("admin", "123");
        } else {
            try (Scanner sc = new Scanner(file)) {
                if (sc.hasNextLine()) {
                    globalUser = sc.nextLine();
                }
                if (sc.hasNextLine()) {
                    globalPass = sc.nextLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveToTextFile(String u, String p) {
        try (PrintWriter out = new PrintWriter(new FileWriter(FILE_PATH))) {
            out.println(u);
            out.println(p);
            globalUser = u;
            globalPass = p;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnForgot) {
            new ResetPassword(this);
        } else if (e.getSource() == btnLogin) {
            String inputUser = txtUser.getText();
            String inputPass = new String(txtPass.getPassword());

            if (inputUser.equals(globalUser) && inputPass.equals(globalPass)) {
                JOptionPane.showMessageDialog(this, "Welcome Admin", "Error", JOptionPane.INFORMATION_MESSAGE);
                triggerTransition();
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect Username or Password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void triggerTransition() {
        // Create the frame and prepare it for transparency
        com.mycompany.clinicmanagement.ui.MainFrame frame = new com.mycompany.clinicmanagement.ui.MainFrame();

        
        frame.setUndecorated(true);
        frame.setOpacity(0f);
        frame.setVisible(true);

        Timer anim = new Timer(20, new ActionListener() {
            float progress = 0f;

            @Override
            public void actionPerformed(ActionEvent e) {
                progress += 0.04f; // Increase speed by changing this increment

                if (progress >= 1f) {
                    setOpacity(0f);
                    frame.setOpacity(1f);
                    ((Timer) e.getSource()).stop();
                    dispose();
                } else {
                    // cross-fade logic
                    setOpacity(1f - progress);
                    frame.setOpacity(progress);
                }
            }
        });
        anim.start();
    }
}
