// src/View/AppointmentStatusView.java
package View;

import Model.Appointment;
import dao.AppointmentDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AppointmentStatusView extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JComboBox<String> cmbFilter;

    public AppointmentStatusView() {
        initComponents();
        refresh();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));

        JLabel title = new JLabel("Appointment Status");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(44, 62, 80));
        header.add(title, BorderLayout.WEST);

        cmbFilter = new JComboBox<>(new String[]{"All", "Pending", "Confirmed", "Cancelled"});
        cmbFilter.addActionListener(e -> refresh());
        header.add(cmbFilter, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID", "Patient", "Doctor", "Date", "Time", "Status"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        styleTable(table);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(scroll, BorderLayout.CENTER);
    }

    private void styleTable(JTable t) {
        t.setRowHeight(35);
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        t.getTableHeader().setBackground(new Color(52, 73, 94));
        t.getTableHeader().setForeground(Color.WHITE);
    }

    private void refresh() {
        String filter = (String) cmbFilter.getSelectedItem();
        List<Appointment> list = new AppointmentDAO().getAll();
        model.setRowCount(0);
        for (Appointment a : list) {
            if ("All".equals(filter) || a.getStatus().equals(filter)) {
                model.addRow(new Object[]{a.getId(), a.getPatientName(), a.getDoctorName(), a.getDate(), a.getTime(), a.getStatus()});
            }
        }
    }
}