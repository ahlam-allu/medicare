// src/View/ReportsView.java
package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class ReportsView extends JPanel {
    private JTabbedPane tabs;
    private JTable tblDaily, tblWorkload, tblRevenue;
    private DefaultTableModel modelDaily, modelWorkload, modelRevenue;
    private JTextField txtDate;

    // ADD THESE: Store button references
    private JButton btnLoadDaily;
    private JButton btnLoadWorkload;
    private JButton btnLoadRevenue;

    public ReportsView() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));

        add(createHeader("Reports & Analytics"), BorderLayout.NORTH);

        tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        tabs.addTab("Daily Appointments", createDailyPanel());
        tabs.addTab("Doctor Workload", createWorkloadPanel());
        tabs.addTab("Revenue Summary", createRevenuePanel());

        add(tabs, BorderLayout.CENTER);
    }

    private JPanel createHeader(String title) {
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lbl.setForeground(new Color(44, 62, 80));
        p.add(lbl);
        return p;
    }

    private JPanel createDailyPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setOpaque(false);
        top.add(new JLabel("Date (YYYY-MM-DD):"));
        txtDate = new JTextField(12);
        txtDate.setText(java.time.LocalDate.now().toString());
        top.add(txtDate);

        btnLoadDaily = new JButton("Load");
        btnLoadDaily.setBackground(new Color(52, 152, 219));
        btnLoadDaily.setForeground(Color.WHITE);
        top.add(btnLoadDaily);

        p.add(top, BorderLayout.NORTH);

        modelDaily = new DefaultTableModel(new String[]{"Patient", "Doctor", "Time", "Status"}, 0);
        tblDaily = new JTable(modelDaily);
        styleTable(tblDaily);
        p.add(new JScrollPane(tblDaily), BorderLayout.CENTER);

        return p;
    }

    private JPanel createWorkloadPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        modelWorkload = new DefaultTableModel(new String[]{"Doctor", "Specialty", "Appointments"}, 0);
        tblWorkload = new JTable(modelWorkload);
        styleTable(tblWorkload);
        p.add(new JScrollPane(tblWorkload), BorderLayout.CENTER);

        btnLoadWorkload = new JButton("Refresh");
        btnLoadWorkload.setBackground(new Color(52, 152, 219));
        btnLoadWorkload.setForeground(Color.WHITE);
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setOpaque(false);
        bottom.add(btnLoadWorkload);
        p.add(bottom, BorderLayout.SOUTH);

        return p;
    }

    private JPanel createRevenuePanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        modelRevenue = new DefaultTableModel(new String[]{"Date", "Revenue"}, 0);
        tblRevenue = new JTable(modelRevenue);
        styleTable(tblRevenue);
        p.add(new JScrollPane(tblRevenue), BorderLayout.CENTER);

        btnLoadRevenue = new JButton("Refresh");
        btnLoadRevenue.setBackground(new Color(52, 152, 219));
        btnLoadRevenue.setForeground(Color.WHITE);
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setOpaque(false);
        bottom.add(btnLoadRevenue);
        p.add(bottom, BorderLayout.SOUTH);

        return p;
    }

    private void styleTable(JTable t) {
        t.setRowHeight(35);
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        t.getTableHeader().setBackground(new Color(52, 73, 94));
        t.getTableHeader().setForeground(Color.WHITE);
    }

    // GETTERS
    public String getSelectedDate() { return txtDate.getText().trim(); }

    // SETTERS
    public void setDailyAppointments(List<Object[]> data) {
        modelDaily.setRowCount(0);
        data.forEach(modelDaily::addRow);
    }
    public void setDoctorWorkload(List<Object[]> data) {
        modelWorkload.setRowCount(0);
        data.forEach(modelWorkload::addRow);
    }
    public void setRevenue(List<Object[]> data) {
        modelRevenue.setRowCount(0);
        data.forEach(modelRevenue::addRow);
    }

    // LISTENERS – NOW SAFE & WORKING
    public void addLoadDailyListener(ActionListener l) {
        btnLoadDaily.addActionListener(l);
    }

    public void addLoadWorkloadListener(ActionListener l) {
        btnLoadWorkload.addActionListener(l);
    }

    public void addLoadRevenueListener(ActionListener l) {
        btnLoadRevenue.addActionListener(l);
    }
}