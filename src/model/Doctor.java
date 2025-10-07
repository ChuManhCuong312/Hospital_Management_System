package model;

public class Doctor {
    private String id;
    private String name;
    private String specialty;
    private String phone;
    private String email;
    private String gender;
    private int age;
    private String department;
    private String status;

    public Doctor() {}

    public Doctor(String id, String name, String specialty, String phone, String email,
                  String gender, int age, String department, String status) {
        this.id = id; this.name = name; this.specialty = specialty; this.phone = phone;
        this.email = email; this.gender = gender; this.age = age; this.department = department; this.status = status;
    }

    // minimal convenience constructor
    public Doctor(String id, String name, String specialty, String phone, String email) {
        this(id, name, specialty, phone, email, "", 0, "", "");
    }

    public static Doctor fromString(String line) {
        try {
            String[] p = line.split(",");
            if (p.length < 9) return null;
            return new Doctor(p[0], p[1], p[2], p[3], p[4], p[5], Integer.parseInt(p[6]), p[7], p[8]);
        } catch (Exception e) {
            return null;
        }
    }

    public String toDataString() {
        return String.join(",", id, name, specialty, phone, email, gender, String.valueOf(age), department, status);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getSpecialty() { return specialty; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getGender() { return gender; }
    public int getAge() { return age; }
    public String getDepartment() { return department; }
    public String getStatus() { return status; }

    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    public void setEmail(String email) { this.email = email; }
    public void setStatus(String status) { this.status = status; }
    public void setGender(String gender) { this.gender = gender; }
    public void setAge(int age) { this.age = age; }
    public void setDepartment(String department) { this.department = department; }
}
