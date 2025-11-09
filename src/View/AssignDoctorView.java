// src/View/AssignDoctorView.java
package View;

import Model.Doctor;
import dao.DoctorDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AssignDoctorView extends JPanel {
    private JTable tblPending;
    private DefaultTableModel model;
    private JComboBox<Doctor> cmbDoctors;
    private JComboBox<String> cmbSlots;
    private JButton btnAssign, btnRefresh;

    public AssignDoctorView() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));

        add(createHeader("Assign Doctor"), BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID", "Patient", "Date", "Time", "Status"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblPending = new JTable(model);
        styleTable(tblPending);
        JScrollPane scroll = new JScrollPane(tblPending);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new GridBagLayout());
        bottom.setBackground(Color.WHITE);
        bottom.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        cmbDoctors = new JComboBox<>();
        cmbSlots = new JComboBox<>();

        btnAssign = createModernBtn("Assign Doctor", new Color(46, 204, 113));
        btnRefresh = createModernBtn("Refresh", new Color(52, 152, 219));

        addField(bottom, gbc, "Doctor", cmbDoctors, 0);
        addField(bottom, gbc, "Time Slot", cmbSlots, 1);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnPanel.setOpaque(false);
        btnPanel.add(btnAssign);
        btnPanel.add(btnRefresh);
        bottom.add(btnPanel, gbc);

        add(bottom, BorderLayout.SOUTH);

        // THIS IS THE BULLETPROOF TIME SLOT LOADER
        cmbDoctors.addActionListener(e -> {
            if (e.getSource() == cmbDoctors) {
                cmbSlots.removeAllItems();
                Doctor selected = (Doctor) cmbDoctors.getSelectedItem();
                if (selected != null) {
                    List<String> slots = selected.getAvailableSlots();
                    if (slots == null || slots.isEmpty()) {
                        cmbSlots.addItem("No slots available");
                        cmbSlots.setEnabled(false);
                    } else {
                        for (String slot : slots) {
                            cmbSlots.addItem(slot);
                        }
                        cmbSlots.setEnabled(true);
                    }
                } else {
                    cmbSlots.addItem("Select a doctor");
                    cmbSlots.setEnabled(false);
                }
            }
        });
    }

    // THIS IS THE ONLY METHOD YOUR CONTROLLER CALLS — FIXED 100%
    public void setDoctors(List<Doctor> doctors) {
        cmbDoctors.removeAllItems();
        if (doctors != null && !doctors.isEmpty()) {
            for (Doctor d : doctors) {
                cmbDoctors.addItem(d);
            }
            cmbDoctors.setSelectedIndex(0); // THIS TRIGGERS TIME SLOTS
        } else {
           
        }
    }

    private JButton createModernBtn(String text, Color color) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color top = getModel().isPressed() ? color.darker() : color.brighter();
                Color bottom = color.darker();
                if (getModel().isRollover()) top = color.brighter().brighter();
                g2.setPaint(new GradientPaint(0, 0, top, 0, getHeight(), bottom));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(new Color(255, 255, 255, 80));
                g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() / 2, 18, 18);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createEmptyBorder(14, 32, 14, 32));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        return btn;
    }

    private void addField(JPanel p, GridBagConstraints gbc, String label, JComponent field, int row) {
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lbl = new JLabel(label + ":");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        p.add(lbl, gbc);
        gbc.gridx = 1;
        field.setPreferredSize(new Dimension(320, 44));
        p.add(field, gbc);
    }

    private void styleTable(JTable t) {
        t.setRowHeight(45);
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        t.getTableHeader().setBackground(new Color(44, 62, 80));
        t.getTableHeader().setForeground(Color.WHITE);
        t.setSelectionBackground(new Color(52, 152, 219, 80));
    }

    private JPanel createHeader(String title) {
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(25, 35, 25, 35)
        ));
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lbl.setForeground(new Color(44, 62, 80));
        p.add(lbl);
        return p;
    }

    public void setPendingAppointments(List<Object[]> list) {
        model.setRowCount(0);
        if (list != null) list.forEach(model::addRow);
    }

    public int getSelectedAppointmentId() {
        int row = tblPending.getSelectedRow();
        return row == -1 ? -1 : (int) model.getValueAt(row, 0);
    }

    public Doctor getSelectedDoctor() { return (Doctor) cmbDoctors.getSelectedItem(); }
    public String getSelectedTimeSlot() { return (String) cmbSlots.getSelectedItem(); }

    public void addAssignListener(Runnable r) { btnAssign.addActionListener(e -> r.run()); }
    public void addRefreshListener(Runnable r) { btnRefresh.addActionListener(e -> r.run()); }
}