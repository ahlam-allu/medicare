package Model;

import java.util.ArrayList;
import java.util.List;

public class Doctor {
    private int id;
    private String name;
    private String specialty;
    private String phone;
    private String email;
    private List<String> availableSlots = new ArrayList<>();

    public Doctor(int id, String name, String specialty, String phone, String email) {
        this(id, name, specialty, phone, email, new ArrayList<>());
    }

    public Doctor(int id, String name, String specialty, String phone, String email, List<String> availableSlots) {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
        this.phone = phone;
        this.email = email;
        this.availableSlots = availableSlots != null ? new ArrayList<>(availableSlots) : new ArrayList<>();
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getSpecialty() { return specialty; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public List<String> getAvailableSlots() { return new ArrayList<>(availableSlots); }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setAvailableSlots(List<String> slots) {
        this.availableSlots = slots != null ? new ArrayList<>(slots) : new ArrayList<>();
    }

    @Override
    public String toString() {
        return name + " - " + specialty;
    }
}