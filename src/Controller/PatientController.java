package Controller;

import dao.PatientDAO;
import Model.Patient;
import View.PatientView;
import javax.swing.*;

public class PatientController {
    private PatientView view;
    private PatientDAO dao;

    public PatientController(PatientView view) {
        this.view = view;
        this.dao = new PatientDAO();
        init();
    }

    private void init() {
        refresh();
        view.addAddListener(this::addPatient);
        view.addUpdateListener(this::updatePatient);
        view.addDeleteListener(this::deletePatient);
        view.addClearListener(view::clearForm);
        view.addTableSelectionListener(this::loadSelected);
    }

    private void refresh() { view.setPatients(dao.getAll()); }

    private void addPatient() {
        Patient p = view.getFormPatient();
        if (p != null) {
            dao.add(p);
            refresh();
            view.clearForm();
            JOptionPane.showMessageDialog(view, "Patient added!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void updatePatient() {
        Patient selected = view.getSelectedPatient();
        Patient updated = view.getFormPatient();
        if (selected != null && updated != null) {
            updated = new Patient(selected.getId(), updated.getName(), updated.getPhone(),
                    updated.getEmail(), updated.getGender(), updated.getAge());
            dao.update(updated);
            refresh();
            view.clearForm();
        }
    }

    private void deletePatient() {
        Patient p = view.getSelectedPatient();
        if (p != null && JOptionPane.showConfirmDialog(view, "Delete " + p.getName() + "?", "Confirm", JOptionPane.YES_NO_OPTION) == 0) {
            dao.delete(p.getId());
            refresh();
            view.clearForm();
        }
    }

    private void loadSelected() {
        Patient p = view.getSelectedPatient();
        if (p != null) view.selectPatient(p);
    }
}