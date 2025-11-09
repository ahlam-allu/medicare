// View/LoginView.java
package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.border.LineBorder;

public class LoginView extends JFrame {
    public JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton lblError;

    public LoginView() {
        initComponents();
    }

    private void initComponents() {
        setTitle("MediCare Plus - Secure Login");
        setSize(460, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(236, 240, 241));

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // ICON
        JLabel lblIcon = new JLabel("MediCare", SwingConstants.CENTER);
        lblIcon.setFont(new Font("Segoe UI", Font.BOLD, 60));
        lblIcon.setForeground(Color.WHITE);
        lblIcon.setBackground(new Color(46, 204, 113));
        lblIcon.setOpaque(true);
        lblIcon.setBorder(new LineBorder(new Color(46, 204, 113), 4, true));
        lblIcon.setBounds(71, 30, 360, 80);
        panel.add(lblIcon);

        // TITLE
        JLabel lblTitle = new JLabel("Welcome Back, Admin", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(44, 62, 80));
        lblTitle.setBounds(71, 130, 360, 35);
        panel.add(lblTitle);

        // ADMIN EMAIL (FIXED)
        JPanel emailBox = new JPanel(new BorderLayout());
        emailBox.setBackground(new Color(239, 254, 172));
        emailBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        emailBox.setBounds(71, 177, 360, 70);

        JLabel lblEmailLabel = new JLabel("admin@gmail.com");
        lblEmailLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblEmailLabel.setForeground(new Color(44, 62, 80));

        JLabel lblEmailIcon = new JLabel();
        lblEmailIcon.setIcon(new ImageIcon(createEmailIcon()));

        JPanel emailPanel = new JPanel(new BorderLayout(10, 0));
        emailPanel.setOpaque(false);
        emailPanel.add(lblEmailIcon, BorderLayout.WEST);
        emailPanel.add(lblEmailLabel, BorderLayout.CENTER);
        emailBox.add(emailPanel, BorderLayout.CENTER);
        panel.add(emailBox);

        txtPassword = new JPasswordField("1234");
        txtPassword.setBackground(new Color(255, 251, 169));
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
            BorderFactory.createEmptyBorder(14, 14, 14, 14)
        ));
        txtPassword.setBounds(71, 338, 360, 55);
        panel.add(txtPassword);

        // ERROR
        lblError = new JButton("login");
        lblError.setBackground(new Color(0, 0, 0));
        lblError.setForeground(new Color(231, 76, 60));
        lblError.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblError.setHorizontalAlignment(SwingConstants.CENTER);
        lblError.setBounds(91, 424, 323, 35);
        panel.add(lblError);

        // LOGIN BUTTON
        btnLogin = new JButton("Access Dashboard");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBackground(new Color(46, 204, 113));
        btnLogin.setBorder(BorderFactory.createEmptyBorder(16, 0, 16, 0));
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogin.setFocusPainted(false);
        btnLogin.setBounds(0, 450, 360, 60);

        btnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btnLogin.setBackground(new Color(39, 174, 96));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btnLogin.setBackground(new Color(46, 204, 113));
            }
        });

        panel.add(btnLogin);

        // FOOTER
        JLabel lblFooter = new JLabel("MediCare Plus © 2025", SwingConstants.CENTER);
        lblFooter.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblFooter.setForeground(new Color(149, 165, 166));
        lblFooter.setBounds(0, 540, 360, 20);
        panel.add(lblFooter);

        getContentPane().add(panel);
        
        JLabel lblSub_1 = new JLabel("Enter password to access the system", SwingConstants.CENTER);
        lblSub_1.setForeground(new Color(127, 140, 141));
        lblSub_1.setFont(new Font("Dialog", Font.BOLD, 15));
        lblSub_1.setBounds(54, 287, 360, 25);
        panel.add(lblSub_1);
    }

    private Image createEmailIcon() {
        BufferedImage img = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(127, 140, 141));
        g2.fillRoundRect(0, 0, 20, 20, 6, 6);
        g2.setColor(Color.WHITE);
        g2.fillRect(4, 7, 12, 2);
        g2.fillRect(6, 9, 8, 2);
        g2.fillRect(8, 11, 4, 2);
        g2.dispose();
        return img;
    }

    public void addLoginListener(ActionListener listener) {
        btnLogin.addActionListener(listener);
        txtPassword.addActionListener(listener);
    }

    public String getPassword() {
        return new String(txtPassword.getPassword());
    }

    public void showError(String msg) {
        lblError.setText(msg);
    }

    public void clearError() {
        lblError.setText("");
    }
}