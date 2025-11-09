// Controller/AppointmentController.java
package Controller;

import Model.Appointment;
import View.AppointmentView;
import dao.AppointmentDAO;
import javax.swing.*;

public class AppointmentController {
    private final AppointmentView view;
    private final AppointmentDAO dao;

    public AppointmentController(AppointmentView view) {
        this.view = view;
        this.dao = new AppointmentDAO();
        refresh();
        view.addBookListener(this::book);
        view.addEditListener(this::edit);
        view.addCancelListener(this::cancel);
        view.addClearListener(view::clearForm);
        view.addTableSelectionListener(this::loadSelected);
    }

    private void refresh() { view.setAppointments(dao.getAll()); }

    private void book() {
        Appointment a = view.getFormAppointment();
        if (a != null) {
            dao.add(a);
            refresh(); view.clearForm();
            success("Appointment booked!");
        }
    }

    private void edit() {
        Appointment selected = view.getSelectedAppointment();
        Appointment form = view.getFormAppointment();
        if (selected == null) { error("Select an appointment!"); return; }
        if (form == null) return;

        Appointment updated = new Appointment(
            selected.getId(),
            form.getPatientId(),
            form.getPatientName(),
            form.getDoctorId(),
            form.getDoctorName(),
            form.getDate(),
            form.getTime(),
            form.getStatus()
        );

        dao.update(updated);
        refresh(); view.clearForm();
        success("Appointment updated!");
    }

    private void cancel() {
        Appointment a = view.getSelectedAppointment();
        if (a == null) { error("Select an appointment!"); return; }
        if (confirm("Cancel this appointment?")) {
            dao.delete(a.getId());
            refresh(); view.clearForm();
            success("Appointment cancelled!");
        }
    }

    private void loadSelected() {
        Appointment a = view.getSelectedAppointment();
        if (a != null) view.selectAppointment(a);
    }

    private void success(String msg) { JOptionPane.showMessageDialog(view, msg, "Success", JOptionPane.INFORMATION_MESSAGE); }
    private void error(String msg) { JOptionPane.showMessageDialog(view, msg, "Error", JOptionPane.ERROR_MESSAGE); }
    private boolean confirm(String msg) { return JOptionPane.showConfirmDialog(view, msg, "Confirm", JOptionPane.YES_NO_OPTION) == 0; }
}