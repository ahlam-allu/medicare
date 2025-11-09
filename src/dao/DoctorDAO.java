package dao;

import Model.Doctor;
import Model.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO {

    public List<Doctor> getAll() {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctors ORDER BY name";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Doctor d = new Doctor(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("specialty"),
                    rs.getString("phone"),
                    rs.getString("email")
                );
                d.setAvailableSlots(getSlots(d.getId()));
                doctors.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    private List<String> getSlots(int doctorId) {
        List<String> slots = new ArrayList<>();
        String sql = "SELECT time_slot FROM doctor_slots WHERE doctor_id = ? ORDER BY time_slot";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, doctorId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                slots.add(rs.getString("time_slot"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return slots;
    }

    public void add(Doctor d) {
        String sql = "INSERT INTO doctors (name, specialty, phone, email) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, d.getName());
            pstmt.setString(2, d.getSpecialty());
            pstmt.setString(3, d.getPhone());
            pstmt.setString(4, d.getEmail());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                d.setId(id);
                saveSlots(id, d.getAvailableSlots());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Doctor d) {
        String sql = "UPDATE doctors SET name=?, specialty=?, phone=?, email=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, d.getName());
            pstmt.setString(2, d.getSpecialty());
            pstmt.setString(3, d.getPhone());
            pstmt.setString(4, d.getEmail());
            pstmt.setInt(5, d.getId());
            pstmt.executeUpdate();
            saveSlots(d.getId(), d.getAvailableSlots());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try (Connection conn = DBConnection.getConnection()) {
            String delSlots = "DELETE FROM doctor_slots WHERE doctor_id = ?";
            try (PreparedStatement p = conn.prepareStatement(delSlots)) {
                p.setInt(1, id);
                p.executeUpdate();
            }
            String delDoc = "DELETE FROM doctors WHERE id = ?";
            try (PreparedStatement p = conn.prepareStatement(delDoc)) {
                p.setInt(1, id);
                p.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveSlots(int doctorId, List<String> slots) {
        try (Connection conn = DBConnection.getConnection()) {
            String del = "DELETE FROM doctor_slots WHERE doctor_id = ?";
            try (PreparedStatement p = conn.prepareStatement(del)) {
                p.setInt(1, doctorId);
                p.executeUpdate();
            }
            String ins = "INSERT OR IGNORE INTO doctor_slots (doctor_id, time_slot) VALUES (?, ?)";
            try (PreparedStatement p = conn.prepareStatement(ins)) {
                for (String s : slots) {
                    p.setInt(1, doctorId);
                    p.setString(2, s);
                    p.addBatch();
                }
                p.executeBatch();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}