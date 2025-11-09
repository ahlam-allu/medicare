// View/DoctorView.java
package View;

import Model.Doctor;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DoctorView extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtName, txtSpecialty, txtPhone, txtEmail;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;

    public DoctorView() { initComponents(); }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(236, 240, 241));

        add(createHeader("Manage Doctors"), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout());
        center.add(createFormPanel(), BorderLayout.NORTH);
        center.add(createTablePanel(), BorderLayout.CENTER);
        center.add(createButtonPanel(), BorderLayout.SOUTH);

        add(center, BorderLayout.CENTER);
    }

    private JPanel createFormPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(new Color(189, 195, 199), 1, 16),
            BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        String[] labels = {"Name", "Specialty", "Phone", "Email"};
        txtName = new JTextField(22);
        txtSpecialty = new JTextField(22);
        txtPhone = new JTextField(18);
        txtEmail = new JTextField(28);

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            p.add(new JLabel(labels[i] + ":" + " "), gbc);
            gbc.gridx = 1;
            p.add(new JComponent[]{txtName, txtSpecialty, txtPhone, txtEmail}[i], gbc);
        }
        return p;
    }

    private JPanel createTablePanel() {
        String[] cols = {"ID", "Name", "Specialty", "Phone", "Email"};
        tableModel = new DefaultTableModel(cols, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        table = new JTable(tableModel);
        styleTable(table);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scroll);
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        p.setOpaque(false);
        btnAdd = createModernButton("Add Doctor", new Color(46, 204, 113));
        btnUpdate = createModernButton("Update", new Color(52, 152, 219));
        btnDelete = createModernButton("Delete", new Color(231, 76, 60));
        btnClear = createModernButton("Clear", new Color(149, 165, 166));
        p.add(btnAdd); p.add(btnUpdate); p.add(btnDelete); p.add(btnClear);
        return p;
    }

    public void setDoctors(List<Doctor> doctors) {
        tableModel.setRowCount(0);
        for (Doctor d : doctors) {
            tableModel.addRow(new Object[]{d.getId(), d.getName(), d.getSpecialty(), d.getPhone(), d.getEmail()});
        }
    }

    public Doctor getSelectedDoctor() {
        int r = table.getSelectedRow();
        if (r == -1) return null;
        return new Doctor(
            (int) tableModel.getValueAt(r, 0),
            (String) tableModel.getValueAt(r, 1),
            (String) tableModel.getValueAt(r, 2),
            (String) tableModel.getValueAt(r, 3),
            (String) tableModel.getValueAt(r, 4)
        );
    }

    public void selectDoctor(Doctor d) {
        txtName.setText(d.getName());
        txtSpecialty.setText(d.getSpecialty());
        txtPhone.setText(d.getPhone());
        txtEmail.setText(d.getEmail());
    }

    public Doctor getFormDoctor() {
        String name = txtName.getText().trim();
        String spec = txtSpecialty.getText().trim();
        String phone = txtPhone.getText().trim();
        String email = txtEmail.getText().trim();

        if (name.isEmpty() || spec.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name, Specialty, Phone required!", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return new Doctor(0, name, spec, phone, email);
    }

    public void clearForm() {
        txtName.setText(""); txtSpecialty.setText(""); txtPhone.setText(""); txtEmail.setText("");
        table.clearSelection();
    }

    public void addAddListener(Runnable r) { btnAdd.addActionListener(e -> r.run()); }
    public void addUpdateListener(Runnable r) { btnUpdate.addActionListener(e -> r.run()); }
    public void addDeleteListener(Runnable r) { btnDelete.addActionListener(e -> r.run()); }
    public void addClearListener(Runnable r) { btnClear.addActionListener(e -> r.run()); }
    public void addTableSelectionListener(Runnable r) {
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) r.run();
        });
    }

    private void styleTable(JTable t) {
        t.setRowHeight(38);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        t.getTableHeader().setBackground(new Color(52, 73, 94));
        t.getTableHeader().setForeground(Color.WHITE);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.setShowGrid(false);
    }

    private JButton createModernButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setForeground(new Color(0, 0, 0));
        b.setBackground(bg);
        b.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setFocusPainted(false);
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { b.setBackground(bg.brighter()); }
            public void mouseExited(java.awt.event.MouseEvent e) { b.setBackground(bg); }
        });
        return b;
    }

    private JPanel createHeader(String t) {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(Color.WHITE);
        h.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(new Color(189, 195, 199), 1, 20),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));
        JLabel l = new JLabel(t);
        l.setFont(new Font("Segoe UI", Font.BOLD, 26));
        l.setForeground(new Color(44, 62, 80));
        h.add(l, BorderLayout.WEST);
        return h;
    }

    private static class RoundedBorder implements javax.swing.border.Border {
        private final Color color; private final int thick; private final int radius;
        RoundedBorder(Color c, int t, int r) { color = c; thick = t; radius = r; }
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color); g2.setStroke(new BasicStroke(thick));
            g2.drawRoundRect(x, y, w - 1, h - 1, radius, radius); g2.dispose();
        }
        public Insets getBorderInsets(Component c) { return new Insets(radius, radius, radius, radius); }
        public boolean isBorderOpaque() { return false; }
    }
}