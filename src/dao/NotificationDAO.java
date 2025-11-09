package dao;

import Model.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

    public List<Object[]> getNotifications() {
        List<Object[]> list = new ArrayList<>();
        String sql = """
            SELECT 'New Appointment' AS type, COUNT(*) AS count
            FROM appointments 
            WHERE DATE(appointment_date) = DATE('now') AND status = 'Pending'
            
            UNION ALL
            
            SELECT 'Pending Assignment' AS type, COUNT(*) AS count
            FROM appointments 
            WHERE doctor_id IS NULL AND status = 'Pending'
            
            UNION ALL
            
            SELECT 'Today Appointments' AS type, COUNT(*) AS count
            FROM appointments 
            WHERE DATE(appointment_date) = DATE('now') AND status != 'Cancelled'
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("type"),
                    rs.getInt("count")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}