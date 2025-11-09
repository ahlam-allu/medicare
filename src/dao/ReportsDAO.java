package dao;

import Model.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportsDAO {

    public List<Object[]> getDailyAppointments(String date) {
        List<Object[]> list = new ArrayList<>();
        String sql = """
            SELECT p.name AS patient, COALESCE(d.name, 'Not Assigned') AS doctor, 
                   a.appointment_time, a.status
            FROM appointments a
            JOIN patients p ON a.patient_id = p.id
            LEFT JOIN doctors d ON a.doctor_id = d.id
            WHERE a.appointment_date = ?
            ORDER BY a.appointment_time
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, date);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("patient"),
                    rs.getString("doctor"),
                    rs.getString("appointment_time"),
                    rs.getString("status")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Object[]> getDoctorWorkload() {
        List<Object[]> list = new ArrayList<>();
        String sql = """
            SELECT d.name, d.specialty, COUNT(a.id) AS total
            FROM doctors d
            LEFT JOIN appointments a ON d.id = a.doctor_id AND a.status != 'Cancelled'
            GROUP BY d.id
            ORDER BY total DESC
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("name"),
                    rs.getString("specialty"),
                    rs.getInt("total")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Object[]> getRevenueSummary() {
        List<Object[]> list = new ArrayList<>();
        String sql = """
            SELECT DATE(payment_date) AS date, SUM(amount) AS total
            FROM payments
            WHERE status = 'Paid'
            GROUP BY DATE(payment_date)
            ORDER BY date DESC
            LIMIT 30
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("date"),
                    String.format("%.2f", rs.getDouble("total"))
                });
            }
        } catch (SQLException e) {
            list.add(new Object[]{"No payments", "0.00"});
        }
        return list;
    }
}