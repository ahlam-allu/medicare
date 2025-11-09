package Model;

public class Patient {
    private int id;
    private String name, phone, email, gender;
    private int age;

    public Patient(int id, String name, String phone, String email, String gender, int age) {
        this.id = id; this.name = name; this.phone = phone;
        this.email = email; this.gender = gender; this.age = age;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getGender() { return gender; }
    public int getAge() { return age; }

    @Override
    public String toString() {
        return name;
    }
}