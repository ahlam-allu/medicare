package View;

import Controller.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

// <-- NEW IMPORT
import javax.swing.SwingUtilities;

public class DashboardFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private CardLayout cardLayout;
    private JPanel contentPanel;

    // View references – avoid ClassCastException
    private PatientView patientView;
    private DoctorView doctorView;
    private AppointmentView appointmentView;
    private AssignDoctorView assignDoctorView;
    private AppointmentStatusView statusView;
    private ReportsView reportsView;
    private NotificationsView notificationsView;

    public DashboardFrame() {
        initComponents();
        initControllers();
        setVisible(true);
    }

    private void initComponents() {
        setTitle("MediCare Plus - Hospital Management");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1450, 820);
        setMinimumSize(new Dimension(1200, 700));
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(245, 247, 250));

        getContentPane().add(createTopBar(), BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerLocation(280);
        split.setContinuousLayout(true);
        split.setBorder(null);

        split.setLeftComponent(createSidebar());
        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);
        split.setRightComponent(contentPanel);

        getContentPane().add(split, BorderLayout.CENTER);
    }

    private JPanel createTopBar() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(0, 102, 204));
        top.setPreferredSize(new Dimension(0, 75));
        top.setBorder(new EmptyBorder(15, 25, 15, 25));

        JLabel title = new JLabel("MediCare Plus");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        top.add(title, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        right.setOpaque(false);
        JLabel user = new JLabel("Admin");
        user.setFont(new Font("Segoe UI", Font.BOLD, 16));
        user.setForeground(Color.WHITE);
        right.add(user);
        right.add(new JLabel(createAvatarIcon()));
        top.add(right, BorderLayout.EAST);
        return top;
    }

    private Icon createAvatarIcon() {
        int s = 44;
        return new Icon() {
            @Override public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(30, 144, 255));
                g2.fillOval(x, y, s, s);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
                String txt = "A";
                FontMetrics fm = g2.getFontMetrics();
                int w = fm.stringWidth(txt);
                int h = fm.getAscent();
                g2.drawString(txt, x + (s - w) / 2, y + h + (s - h) / 2 - 3);
                g2.dispose();
            }
            @Override public int getIconWidth() { return s; }
            @Override public int getIconHeight() { return s; }
        };
    }

    private JPanel createSidebar() {
        JPanel side = new JPanel();
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBackground(new Color(33, 47, 61));
        side.setBorder(new EmptyBorder(20, 0, 20, 0));

        String[][] items = {
            {"Patients", "Manage Patients"},
            {"Doctors", "Manage Doctors"},
            {"Appointments", "Book Appointments"},
            {"AssignDoctor", "Assign Doctor"},
            {"Status", "Appointment Status"},
            {"Reports", "Analytics"},
            {"Notifications", "Alerts"},
            {"Logout", "Exit"}
        };

        for (String[] it : items) {
            JPanel row = new JPanel(new BorderLayout());
            row.setOpaque(false);
            row.setMaximumSize(new Dimension(Short.MAX_VALUE, 60));
            row.setBorder(new EmptyBorder(8, 25, 8, 25));

            JLabel lbl = new JLabel(it[1]);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            lbl.setForeground(Color.WHITE);
            lbl.setName(it[0]);
            row.add(lbl, BorderLayout.CENTER);
            row.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            row.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                    if ("Logout".equals(lbl.getName())) {
                        if (JOptionPane.showConfirmDialog(DashboardFrame.this,
                                "Logout?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                            System.exit(0);
                        }
                    } else {
                        cardLayout.show(contentPanel, lbl.getName());
                    }
                }
                @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                    row.setBackground(new Color(52, 73, 94));
                }
                @Override public void mouseExited(java.awt.event.MouseEvent e) {
                    row.setBackground(new Color(33, 47, 61));
                }
            });
            side.add(row);
        }
        return side;
    }

    private void initControllers() {
        patientView = new PatientView();
        patientView.setBackground(new Color(255, 108, 68));
        doctorView = new DoctorView();
        appointmentView = new AppointmentView();
        assignDoctorView = new AssignDoctorView();
        statusView = new AppointmentStatusView();
        reportsView = new ReportsView();
        notificationsView = new NotificationsView();

        contentPanel.add(wrap(patientView, "Manage Patients"), "Patients");
        contentPanel.add(wrap(doctorView, "Manage Doctors"), "Doctors");
        contentPanel.add(wrap(appointmentView, "Book Appointments"), "Appointments");
        contentPanel.add(wrap(assignDoctorView, "Assign Doctor"), "AssignDoctor");
        contentPanel.add(wrap(statusView, "Appointment Status"), "Status");
        contentPanel.add(wrap(reportsView, "Analytics & Reports"), "Reports");
        contentPanel.add(wrap(notificationsView, "Alerts & Notifications"), "Notifications");

        new PatientController(patientView);
        new DoctorController(doctorView);
        new AppointmentController(appointmentView);
        new AssignDoctorController(assignDoctorView);
        new ReportsController(reportsView);
        new NotificationsController(notificationsView);

        cardLayout.show(contentPanel, "Patients");
    }

    private JScrollPane wrap(JPanel panel, String title) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(15, 15, 15, 15)));

        JLabel hdr = new JLabel(title);
        hdr.setFont(new Font("Segoe UI", Font.BOLD, 22));
        hdr.setForeground(new Color(44, 62, 80));
        card.add(hdr, BorderLayout.NORTH);
        card.add(panel, BorderLayout.CENTER);

        JScrollPane sp = new JScrollPane(card);
        sp.setBorder(null);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        return sp;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // <-- FIXED: SwingUtilities is now imported
        SwingUtilities.invokeLater(DashboardFrame::new);
    }
}