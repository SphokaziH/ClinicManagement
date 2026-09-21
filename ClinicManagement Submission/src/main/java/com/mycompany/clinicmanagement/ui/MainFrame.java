package com.mycompany.clinicmanagement.ui;

import com.mycompany.clinicmanagement.ui.pages.*;
import com.mycompany.clinicmanagement.ui.components.SidebarButton;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JPanel contentArea;
    private CardLayout cardLayout;

    public MainFrame() {
        setTitle("Clinic Management System");
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initLayout();
    }

    private void initLayout() {
        setLayout(new BorderLayout());
        add(createSidebar(), BorderLayout.WEST);

        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);

        contentArea.add(new DashboardPage(), "Dashboard");
        contentArea.add(new PatientsPage(), "Patients");
        contentArea.add(new StaffPage(), "Staff");
        contentArea.add(new MedicationPage(), "Medication");
        contentArea.add(new MedicalDocumentsPage(), "Medical Records");
        contentArea.add(new BillingPage(), "Billing & Payments");
        contentArea.add(new ReportsPage(), "Reports");

        add(contentArea, BorderLayout.CENTER);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(0, 114, 181), 0, getHeight(), new Color(0, 31, 78));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JLabel logo = new JLabel("Application");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("SansSerif", Font.BOLD, 22));
        logo.setBorder(BorderFactory.createEmptyBorder(30, 25, 40, 10));
        sidebar.add(logo);

        // Navigation Buttons
        String[] navItems = {
            "Dashboard", "Patients", "Staff",
            "Medical Records", "Medication", "Billing & Payments", "Reports"
        };

        for (String item : navItems) {
            SidebarButton btn = new SidebarButton(item);
            btn.addActionListener(e -> {
                cardLayout.show(contentArea, item);

                // REFRESH LOGIC: Find the page and call refreshData()
                for (Component comp : contentArea.getComponents()) {
                    if (comp.isVisible() && comp instanceof Refreshable) {
                        ((Refreshable) comp).refreshData();
                    }
                }
            });
            sidebar.add(btn);
        }

        SidebarButton logoutBtn = new SidebarButton("Logout");
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new com.mycompany.clinicmanagement.ui.LoginForm(); // Open Login
                this.dispose(); // Close Main Dashboard
            }
        });
        sidebar.add(Box.createVerticalGlue()); // Pushes logout to the bottom
        sidebar.add(logoutBtn);

        return sidebar;
    }
}
