package model;

public class Doctor {
    private String id;
    private String name;
    private String specialization;
    private String phone;

    public Doctor(String id, String name, String specialization, String phone) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.phone = phone;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getSpecialization() { return specialization; }
    public String getPhone() { return phone; }

    @Override
    public String toString() {
        return id + "," + name + "," + specialization + "," + phone;
    }

    public static Doctor fromString(String line) {
        String[] parts = line.split(",");
        if (parts.length != 4) return null;
        return new Doctor(parts[0], parts[1], parts[2], parts[3]);
    }
}
