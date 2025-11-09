// src/View/NotificationsView.java
package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class NotificationsView extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JLabel lblCount;
    private JButton btnRefresh;

    public NotificationsView() {
        initComponents();
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

        JLabel title = new JLabel("Notifications & Alerts");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(44, 62, 80));
        header.add(title, BorderLayout.WEST);

        lblCount = new JLabel("0 Alerts");
        lblCount.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblCount.setForeground(new Color(231, 76, 60));
        header.add(lblCount, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"Type", "Count"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        styleTable(table);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(scroll, BorderLayout.CENTER);

        btnRefresh = new JButton("Refresh");
        btnRefresh.setBackground(new Color(52, 152, 219));
        btnRefresh.setForeground(Color.WHITE);
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(btnRefresh);
        add(bottom, BorderLayout.SOUTH);
    }

    private void styleTable(JTable t) {
        t.setRowHeight(35);
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        t.getTableHeader().setBackground(new Color(52, 73, 94));
        t.getTableHeader().setForeground(Color.WHITE);
    }

    public void setNotifications(List<Object[]> data) {
        model.setRowCount(0);
        int total = 0;
        for (Object[] row : data) {
            model.addRow(row);
            total += (int) row[1];
        }
        lblCount.setText(total + " Active Alerts");
        lblCount.setForeground(total > 0 ? new Color(231, 76, 60) : new Color(46, 204, 113));
    }

    public void addRefreshListener(java.awt.event.ActionListener l) {
        btnRefresh.addActionListener(l);
    }
}