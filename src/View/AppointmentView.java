// View/AppointmentView.java
package View;

import Model.Appointment;
import Model.Patient;
import Model.Doctor;
import dao.AppointmentDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AppointmentView extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<Patient> cmbPatient;
    private JComboBox<Doctor> cmbDoctor;
    private JTextField txtDate;
    private JComboBox<String> cmbTimeSlot;
    private JComboBox<String> cmbStatus;
    private JButton btnBook, btnEdit, btnCancel, btnClear;
    private AppointmentDAO dao;

    public AppointmentView() {
        dao = new AppointmentDAO();
        initComponents();
        loadComboBoxes();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(236, 240, 241));

        add(createHeader("Book & Manage Appointments"), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout());
        center.add(createFormPanel(), BorderLayout.NORTH);
        center.add(createTablePanel(), BorderLayout.CENTER);
        center.add(createModernButtonPanel(), BorderLayout.SOUTH);

        add(center, BorderLayout.CENTER);
    }

    private JPanel createHeader(String titleText) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(141, 228, 227));
        header.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(new Color(189, 195, 199), 1, 20),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));
        JLabel title = new JLabel(titleText);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(44, 62, 80));
        header.add(title, BorderLayout.CENTER);
        return header;
    }

    private JPanel createFormPanel() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(new Color(246, 248, 235));
        form.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(new Color(189, 195, 199), 1, 16),
            BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        String[] labels = {"Patient", "Doctor", "Date (YYYY-MM-DD)", "Time", "Status"};
        cmbPatient = new JComboBox<>();
        cmbDoctor = new JComboBox<>();
        txtDate = new JTextField(12);
        cmbTimeSlot = new JComboBox<>(new String[]{"09:00", "10:00", "11:00", "14:00", "15:00", "16:00"});
        cmbStatus = new JComboBox<>(new String[]{"Pending", "Confirmed", "Cancelled"});

        JComponent[] fields = {cmbPatient, cmbDoctor, txtDate, cmbTimeSlot, cmbStatus};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            form.add(createLabel(labels[i]), gbc);
            gbc.gridx = 1;
            form.add(fields[i], gbc);
        }

        return form;
    }

    private JPanel createTablePanel() {
        String[] cols = {"ID", "Patient", "Doctor", "Date", "Time", "Status"};
        tableModel = new DefaultTableModel(cols, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        table = new JTable(tableModel);
        styleTable(table);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scroll);
        return panel;
    }

    // MODERN BUTTONS — BLACK TEXT FOR MAX VISIBILITY
    private JPanel createModernButtonPanel() {
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        btnPanel.setBackground(new Color(159, 159, 159));
        btnPanel.setOpaque(false);

        btnBook = createProButton("Book Appointment", new Color(46, 204, 113), "Book a new appointment");
        btnEdit = createProButton("Edit", new Color(52, 152, 219), "Update selected appointment");
        btnCancel = createProButton("Cancel", new Color(231, 76, 60), "Cancel selected appointment");
        btnClear = createProButton("Clear Form", new Color(149, 165, 166), "Reset all fields");

        btnPanel.add(btnBook);
        btnPanel.add(btnEdit);
        btnPanel.add(btnCancel);
        btnPanel.add(btnClear);

        return btnPanel;
    }

    // PRO BUTTON — BLACK TEXT, SAME STYLE
    private JButton createProButton(String text, Color baseColor, String tooltip) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setForeground(Color.BLACK);  // CHANGED TO BLACK
        btn.setBackground(baseColor);
        btn.setBorder(BorderFactory.createEmptyBorder(14, 32, 14, 32));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        btn.setToolTipText(tooltip);

        // ROUNDED + BORDER
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(baseColor.darker(), 2, true),
            BorderFactory.createEmptyBorder(12, 30, 12, 30)
        ));

        // HOVER EFFECT
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(baseColor.brighter());
                btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(baseColor.brighter().brighter(), 2, true),
                    BorderFactory.createEmptyBorder(12, 30, 12, 30)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(baseColor);
                btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(baseColor.darker(), 2, true),
                    BorderFactory.createEmptyBorder(12, 30, 12, 30)
                ));
            }
        });

        return btn;
    }

    // === DATA & LOGIC ===
    private void loadComboBoxes() {
        dao.getAllPatients().forEach(cmbPatient::addItem);
        dao.getAllDoctors().forEach(cmbDoctor::addItem);
    }

    public void setAppointments(List<Appointment> list) {
        tableModel.setRowCount(0);
        for (Appointment a : list) {
            tableModel.addRow(new Object[]{
                a.getId(),
                a.getPatientName(),
                a.getDoctorName(),
                a.getDate(),
                a.getTime(),
                a.getStatus()
            });
        }
    }

    public Appointment getSelectedAppointment() {
        int row = table.getSelectedRow();
        if (row == -1) return null;
        return new Appointment(
            (int) tableModel.getValueAt(row, 0),
            0, (String) tableModel.getValueAt(row, 1),
            0, (String) tableModel.getValueAt(row, 2),
            (String) tableModel.getValueAt(row, 3),
            (String) tableModel.getValueAt(row, 4),
            (String) tableModel.getValueAt(row, 5)
        );
    }

    public void selectAppointment(Appointment a) {
        cmbPatient.setSelectedItem(findPatientByName(a.getPatientName()));
        cmbDoctor.setSelectedItem(findDoctorByName(a.getDoctorName()));
        txtDate.setText(a.getDate());
        cmbTimeSlot.setSelectedItem(a.getTime());
        cmbStatus.setSelectedItem(a.getStatus());
    }

    public Appointment getFormAppointment() {
        try {
            Patient patient = (Patient) cmbPatient.getSelectedItem();
            Doctor doctor = (Doctor) cmbDoctor.getSelectedItem();
            String date = txtDate.getText().trim();
            String time = (String) cmbTimeSlot.getSelectedItem();
            String status = (String) cmbStatus.getSelectedItem();

            if (patient == null || doctor == null || date.isEmpty() || time == null) {
                JOptionPane.showMessageDialog(this, "All fields required!", "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }

            if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                JOptionPane.showMessageDialog(this, "Date must be YYYY-MM-DD", "Invalid Date", JOptionPane.WARNING_MESSAGE);
                return null;
            }

            return new Appointment(
                0,
                patient.getId(),
                patient.getName(),
                doctor.getId(),
                doctor.getName(),
                date,
                time,
                status
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid data entered!", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public void clearForm() {
        cmbPatient.setSelectedIndex(0);
        cmbDoctor.setSelectedIndex(0);
        txtDate.setText("");
        cmbTimeSlot.setSelectedIndex(0);
        cmbStatus.setSelectedIndex(0);
        table.clearSelection();
    }

    // === LISTENERS ===
    public void addBookListener(Runnable r) { btnBook.addActionListener(e -> r.run()); }
    public void addEditListener(Runnable r) { btnEdit.addActionListener(e -> r.run()); }
    public void addCancelListener(Runnable r) { btnCancel.addActionListener(e -> r.run()); }
    public void addClearListener(Runnable r) { btnClear.addActionListener(e -> r.run()); }
    public void addTableSelectionListener(Runnable r) {
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) r.run();
        });
    }

    // === HELPERS ===
    private Patient findPatientByName(String name) {
        for (int i = 0; i < cmbPatient.getItemCount(); i++) {
            Patient p = cmbPatient.getItemAt(i);
            if (p.getName().equals(name)) return p;
        }
        return null;
    }

    private Doctor findDoctorByName(String name) {
        for (int i = 0; i < cmbDoctor.getItemCount(); i++) {
            Doctor d = cmbDoctor.getItemAt(i);
            if (d.getName().equals(name)) return d;
        }
        return null;
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text + ":");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(new Color(44, 62, 80));
        return lbl;
    }

    private void styleTable(JTable t) {
        t.setRowHeight(40);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        t.getTableHeader().setBackground(new Color(52, 73, 94));
        t.getTableHeader().setForeground(Color.WHITE);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.setShowGrid(false);
        t.setSelectionBackground(new Color(52, 152, 219));
        t.setSelectionForeground(Color.WHITE);
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