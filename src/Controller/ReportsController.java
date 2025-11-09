package Controller;

import View.ReportsView;
import dao.ReportsDAO;
import javax.swing.*;
import java.awt.event.ActionEvent;

public class ReportsController {
    private final ReportsView view;
    private final ReportsDAO dao;

    public ReportsController(ReportsView view) {
        this.view = view;
        this.dao = new ReportsDAO();
        init();
    }

    private void init() {
        view.addLoadDailyListener(this::loadDaily);
        view.addLoadWorkloadListener(this::loadWorkload);
        view.addLoadRevenueListener(this::loadRevenue);
        loadWorkload(null);
        loadRevenue(null);
    }

    private void loadDaily(ActionEvent e) {
        String date = view.getSelectedDate();
        if (date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            view.setDailyAppointments(dao.getDailyAppointments(date));
        } else {
            JOptionPane.showMessageDialog(view, "Invalid date! Use YYYY-MM-DD", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadWorkload(ActionEvent e) {
        view.setDoctorWorkload(dao.getDoctorWorkload());
    }

    private void loadRevenue(ActionEvent e) {
        view.setRevenue(dao.getRevenueSummary());
    }
}