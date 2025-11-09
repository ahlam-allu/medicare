package Controller;

import View.AssignDoctorView;
import Model.Doctor;
import dao.AppointmentDAO;
import dao.DoctorDAO;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AssignDoctorController {
    private final AssignDoctorView view;
    private final AppointmentDAO appointmentDAO;
    private final DoctorDAO doctorDAO;

    public AssignDoctorController(AssignDoctorView view) {
        this.view = view;
        this.appointmentDAO = new AppointmentDAO();
        this.doctorDAO = new DoctorDAO();
        init();
    }

    private void init() {
        view.addAssignListener(this::assign);
        view.addRefreshListener(this::refresh);
        loadData();
    }

    private void loadData() {
        refreshAppointments();
        refreshDoctors();
    }

    private void assign() {
        int aptId = view.getSelectedAppointmentId();
        Doctor doc = view.getSelectedDoctor();
        String slot = view.getSelectedTimeSlot();

        if (aptId == -1) { err("Select appointment"); return; }
        if (doc == null) { err("Select doctor"); return; }
        if (slot == null || slot.isEmpty()) { err("Select time"); return; }

        if (isBooked(doc.getId(), slot)) {
            warn("Dr. " + doc.getName() + " is booked at " + slot);
            return;
        }

        appointmentDAO.assignDoctor(aptId, doc.getId(), slot);
        refresh();
        success("Assigned Dr. " + doc.getName() + " at " + slot);
    }

    private void refresh() {
        refreshAppointments();
        refreshDoctors();
    }

    private void refreshAppointments() {
        view.setPendingAppointments(appointmentDAO.getPendingAppointments());
    }

    private void refreshDoctors() {
        view.setDoctors(doctorDAO.getAll());
    }

    private boolean isBooked(int docId, String slot) {
        String sql = "SELECT 1 FROM appointments WHERE doctor_id = ? AND appointment_time = ? AND status IN ('Confirmed', 'Pending') LIMIT 1";
        try (Connection conn = appointmentDAO.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, docId);
            ps.setString(2, slot);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            err("DB Error");
            return true;
        }
    }

    private void err(String msg) { JOptionPane.showMessageDialog(view, msg, "Error", JOptionPane.ERROR_MESSAGE); }
    private void warn(String msg) { JOptionPane.showMessageDialog(view, "<html><b>" + msg + "</b></html>", "Warning", JOptionPane.WARNING_MESSAGE); }
    private void success(String msg) { JOptionPane.showMessageDialog(view, "<html><b>" + msg + "</b></html>", "Success", JOptionPane.INFORMATION_MESSAGE); }
}