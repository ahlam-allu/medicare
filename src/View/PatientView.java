// View/PatientView.java
package View;

import Model.Patient;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PatientView extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtName, txtPhone, txtEmail, txtAge;
    private JComboBox<String> cmbGender;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;

    public PatientView() { initComponents(); }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(236, 240, 241));

        JPanel header = createHeader("Manage Patients");
        add(header, BorderLayout.NORTH);

        JPanel formPanel = createFormPanel();
        JPanel tablePanel = createTablePanel();
        JPanel buttonPanel = createButtonPanel();

        JPanel center = new JPanel(new BorderLayout());
        center.add(formPanel, BorderLayout.NORTH);
        center.add(tablePanel, BorderLayout.CENTER);
        center.add(buttonPanel, BorderLayout.SOUTH);

        add(center, BorderLayout.CENTER);
    }

    private JPanel createHeader(String titleText) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(new Color(189, 195, 199), 1, 20),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));
        JLabel title = new JLabel(titleText);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(44, 62, 80));
        header.add(title, BorderLayout.WEST);
        return header;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(new Color(189, 195, 199), 1, 16),
            BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        String[] labels = {"Name", "Phone", "Email", "Age", "Gender"};
        txtName = new JTextField(20);
        txtPhone = new JTextField(15);
        txtEmail = new JTextField(25);
        txtAge = new JTextField(8);
        cmbGender = new JComboBox<>(new String[]{"Male", "Female", "Other"});

        JComponent[] fields = {txtName, txtPhone, txtEmail, txtAge, cmbGender};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            formPanel.add(new JLabel(labels[i] + ":" + " "), gbc);
            gbc.gridx = 1;
            formPanel.add(fields[i], gbc);
        }

        return formPanel;
    }

    private JPanel createTablePanel() {
        String[] cols = {"ID", "Name", "Phone", "Email", "Gender", "Age"};
        tableModel = new DefaultTableModel(cols, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        table = new JTable(tableModel);
        styleTable(table);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        btnPanel.setOpaque(false);

        btnAdd = createModernButton("Add Patient", new Color(46, 204, 113));
        btnUpdate = createModernButton("Update", new Color(52, 152, 219));
        btnDelete = createModernButton("Delete", new Color(231, 76, 60));
        btnClear = createModernButton("Clear", new Color(149, 165, 166));

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        return btnPanel;
    }

    // === DATA & LOGIC ===
    public void setPatients(List<Patient> patients) {
        tableModel.setRowCount(0);
        for (Patient p : patients) {
            tableModel.addRow(new Object[]{p.getId(), p.getName(), p.getPhone(), p.getEmail(), p.getGender(), p.getAge()});
        }
    }

    public Patient getSelectedPatient() {
        int row = table.getSelectedRow();
        if (row == -1) return null;
        return new Patient(
            (int) tableModel.getValueAt(row, 0),
            (String) tableModel.getValueAt(row, 1),
            (String) tableModel.getValueAt(row, 2),
            (String) tableModel.getValueAt(row, 3),
            (String) tableModel.getValueAt(row, 4),
            (int) tableModel.getValueAt(row, 5)
        );
    }

    public void selectPatient(Patient p) {
        txtName.setText(p.getName());
        txtPhone.setText(p.getPhone());
        txtEmail.setText(p.getEmail());
        txtAge.setText(String.valueOf(p.getAge()));
        cmbGender.setSelectedItem(p.getGender());
    }

    public Patient getFormPatient() {
        String name = txtName.getText().trim();
        String phone = txtPhone.getText().trim();
        String email = txtEmail.getText().trim();
        String ageStr = txtAge.getText().trim();
        String gender = (String) cmbGender.getSelectedItem();

        if (name.isEmpty() || phone.isEmpty() || ageStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name, Phone, Age required!", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        try {
            int age = Integer.parseInt(ageStr);
            return new Patient(0, name, phone, email, gender, age);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Age must be a number!", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public void clearForm() {
        txtName.setText(""); txtPhone.setText(""); txtEmail.setText(""); txtAge.setText("");
        cmbGender.setSelectedIndex(0);
        table.clearSelection();
    }

    // === LISTENERS ===
    public void addAddListener(Runnable r) { btnAdd.addActionListener(e -> r.run()); }
    public void addUpdateListener(Runnable r) { btnUpdate.addActionListener(e -> r.run()); }
    public void addDeleteListener(Runnable r) { btnDelete.addActionListener(e -> r.run()); }
    public void addClearListener(Runnable r) { btnClear.addActionListener(e -> r.run()); }
    public void addTableSelectionListener(Runnable r) {
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) r.run();
        });
    }

    // === UI HELPERS ===
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
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(new Color(0, 0, 0));
        btn.setBackground(bg);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setBackground(bg.brighter()); }
            public void mouseExited(java.awt.event.MouseEvent evt) { btn.setBackground(bg); }
        });
        return btn;
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