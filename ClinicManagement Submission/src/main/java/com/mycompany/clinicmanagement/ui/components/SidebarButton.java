package com.mycompany.clinicmanagement.ui.components;

import com.mycompany.clinicmanagement.styles.Styles;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SidebarButton extends JButton {

    public SidebarButton(String text) {
        super(text);
        
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);   
        
// Ensure initial background is fully transparent
        setBackground(new Color(0, 0, 0, 0));        

        // Standard Styling
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setForeground(Color.WHITE);
        setFont(new Font("SansSerif", Font.PLAIN, 16));
        setHorizontalAlignment(SwingConstants.LEFT);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Padding (Left margin for the text)
        setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 10));

        // Hover Effect logic
// Inside SidebarButton.java Constructor
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(new Color(255, 255, 255, 40));
                // Remove setOpaque(true);
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(new Color(0, 0, 0, 0)); // Transparent
                repaint();
            }
        });
        // Add this override to ensure background paints correctly over the gradient
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (getBackground().getAlpha() > 0) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        super.paintComponent(g);
    }
}
