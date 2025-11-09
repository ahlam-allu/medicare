package dao;

import Model.Appointment;
import Model.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    public List<Object[]> getPendingAppointments() {
        List<Object[]> list = new ArrayList<>();
        String sql = """
            SELECT a.id, p.name AS patient_name, a.appointment_date, a.appointment_time, a.status
            FROM appointments a
            JOIN patients p ON a.patient_id = p.id
            WHERE a.doctor_id IS NULL AND a.status = 'Pending'
            ORDER BY a.appointment_date, a.appointment_time
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                list.add(new Object[]{
                    rs.getInt("id"),
                    rs.getString("patient_name"),
                    rs.getString("appointment_date"),
                    rs.getString("appointment_time"),
                    rs.getString("status")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void assignDoctor(int appointmentId, int doctorId, String timeSlot) {
        String sql = "UPDATE appointments SET doctor_id = ?, appointment_time = ?, status = 'Confirmed' WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, doctorId);
            pstmt.setString(2, timeSlot);
            pstmt.setInt(3, appointmentId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }

    public List<Appointment> getAll() {
        List<Appointment> list = new ArrayList<>();
        String sql = """
            SELECT a.id, a.patient_id, p.name AS patient_name, a.doctor_id, d.name AS doctor_name,
                   a.appointment_date, a.appointment_time, a.status
            FROM appointments a
            JOIN patients p ON a.patient_id = p.id
            LEFT JOIN doctors d ON a.doctor_id = d.id
            ORDER BY a.appointment_date, a.appointment_time
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                list.add(new Appointment(
                    rs.getInt("id"),
                    rs.getInt("patient_id"),
                    rs.getString("patient_name"),
                    rs.getInt("doctor_id"),
                    rs.getString("doctor_name") != null ? rs.getString("doctor_name") : "Not Assigned",
                    rs.getString("appointment_date"),
                    rs.getString("appointment_time"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void add(Appointment a) {
        String sql = "INSERT INTO appointments(patient_id, doctor_id, appointment_date, appointment_time, status) VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, a.getPatientId());
            pstmt.setInt(2, a.getDoctorId());
            pstmt.setString(3, a.getDate());
            pstmt.setString(4, a.getTime());
            pstmt.setString(5, a.getStatus());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                a.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Appointment a) {
        String sql = "UPDATE appointments SET patient_id=?, doctor_id=?, appointment_date=?, appointment_time=?, status=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, a.getPatientId());
            pstmt.setInt(2, a.getDoctorId());
            pstmt.setString(3, a.getDate());
            pstmt.setString(4, a.getTime());
            pstmt.setString(5, a.getStatus());
            pstmt.setInt(6, a.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM appointments WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Model.Patient> getAllPatients() {
        List<Model.Patient> list = new ArrayList<>();
        String sql = "SELECT id, name FROM patients ORDER BY name";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Model.Patient(rs.getInt("id"), rs.getString("name"), "", "", "", 0));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Model.Doctor> getAllDoctors() {
        DoctorDAO dao = new DoctorDAO();
        return dao.getAll();
    }
}