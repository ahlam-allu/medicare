package Controller;

import dao.DoctorDAO;
import Model.Doctor;
import View.DoctorView;
import javax.swing.*;

public class DoctorController {
    private final DoctorView view;
    private final DoctorDAO dao;

    public DoctorController(DoctorView view) {
        this.view = view;
        this.dao = new DoctorDAO();
        init();
    }

    private void init() {
        refresh();
        view.addAddListener(this::addDoctor);
        view.addUpdateListener(this::updateDoctor);
        view.addDeleteListener(this::deleteDoctor);
        view.addClearListener(view::clearForm);
        view.addTableSelectionListener(this::loadSelected);
    }

    private void refresh() {
        view.setDoctors(dao.getAll());
    }

    private void addDoctor() {
        Doctor d = view.getFormDoctor();
        if (d == null) return;

        dao.add(d);
        refresh();
        view.clearForm();
        showSuccess("Doctor added successfully!");
    }

    private void updateDoctor() {
        Doctor selected = view.getSelectedDoctor();
        Doctor formData = view.getFormDoctor();

        if (selected == null) {
            showError("Please select a doctor to update!");
            return;
        }
        if (formData == null) return;

        Doctor updated = new Doctor(
            selected.getId(),
            formData.getName(),
            formData.getSpecialty(),
            formData.getPhone(),
            formData.getEmail()
        );

        dao.update(updated);
        refresh();
        view.clearForm();
        showSuccess("Doctor updated successfully!");
    }

    private void deleteDoctor() {
        Doctor d = view.getSelectedDoctor();
        if (d == null) {
            showError("Please select a doctor to delete!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            view,
            "<html>Delete <b>Dr. " + d.getName() + "</b>?<br>This cannot be undone.</html>",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            dao.delete(d.getId());
            refresh();
            view.clearForm();
            showInfo("Doctor deleted!");
        }
    }

    private void loadSelected() {
        Doctor d = view.getSelectedDoctor();
        if (d != null) {
            view.selectDoctor(d);
        }
    }

    private void showSuccess(String msg) {
        JOptionPane.showMessageDialog(view, msg, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(view, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showInfo(String msg) {
        JOptionPane.showMessageDialog(view, msg, "Deleted", JOptionPane.INFORMATION_MESSAGE);
    }
}