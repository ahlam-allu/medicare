package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    private static Connection con;

    public static Connection getConnection() {
        try {
            if (con == null || con.isClosed()) {
                Class.forName("org.sqlite.JDBC");
                con = DriverManager.getConnection("jdbc:sqlite:medicare.db");
                createTables();
            }
        } catch (Exception e) {
            System.err.println("Database connection failed!");
            e.printStackTrace();
        }
        return con;
    }

    public static void closeConnection() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                con = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTables() {
        if (con == null) return;

        String patientSQL = """
            CREATE TABLE IF NOT EXISTS patients (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                phone TEXT NOT NULL,
                email TEXT,
                gender TEXT NOT NULL,
                age INTEGER NOT NULL
            );
            """;

        String doctorSQL = """
            CREATE TABLE IF NOT EXISTS doctors (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                specialty TEXT NOT NULL,
                phone TEXT NOT NULL,
                email TEXT
            );
            """;

        String appointmentSQL = """
            CREATE TABLE IF NOT EXISTS appointments (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                patient_id INTEGER NOT NULL,
                doctor_id INTEGER,
                appointment_date TEXT NOT NULL,
                appointment_time TEXT NOT NULL,
                status TEXT NOT NULL DEFAULT 'Pending',
                FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
                FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE SET NULL
            );
            """;

        String doctorSlotsSQL = """
            CREATE TABLE IF NOT EXISTS doctor_slots (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                doctor_id INTEGER NOT NULL,
                time_slot TEXT NOT NULL CHECK(time_slot LIKE '__:__'),
                FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE CASCADE,
                UNIQUE(doctor_id, time_slot)
            );
            """;

        try (Statement stmt = con.createStatement()) {
            stmt.execute(patientSQL);
            stmt.execute(doctorSQL);
            stmt.execute(appointmentSQL);
            stmt.execute(doctorSlotsSQL);
            System.out.println("All tables checked/created successfully in medicare.db");
        } catch (SQLException e) {
            System.err.println("Error creating tables!");
            e.printStackTrace();
        }
    }
}