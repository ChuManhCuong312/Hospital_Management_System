package model;

public class Patient {
    private String id;
    private String name;
    private int age;
    private String gender;
    private String address;
    private String phone;
    private String diagnosis;
    private String bloodGroup;
    private String doctorId;
    private String status;

    public Patient() {}

    public Patient(String id, String name, int age, String gender, String address,
                   String phone, String diagnosis, String bloodGroup, String doctorId, String status) {
        this.id = id; this.name = name; this.age = age; this.gender = gender;
        this.address = address; this.phone = phone; this.diagnosis = diagnosis;
        this.bloodGroup = bloodGroup; this.doctorId = doctorId; this.status = status;
    }

    public Patient(String id, String name, int age, String gender, String diagnosis) {
        this(id, name, age, gender, "", "", diagnosis, "", "", "");
    }

    public static Patient fromString(String line) {
        try {
            String[] p = line.split(",");
            if (p.length < 10) return null;
            return new Patient(p[0], p[1], Integer.parseInt(p[2]), p[3],
                    p[4], p[5], p[6], p[7], p[8], p[9]);
        } catch (Exception e) {
            return null;
        }
    }

    public String toDataString() {
        return String.join(",", id, name, String.valueOf(age), gender, address, phone, diagnosis, bloodGroup, doctorId, status);
    }

    // getters / setters
    public String getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public String getDiagnosis() { return diagnosis; }
    public String getBloodGroup() { return bloodGroup; }
    public String getDoctorId() { return doctorId; }
    public String getStatus() { return status; }

    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setGender(String gender) { this.gender = gender; }
    public void setAddress(String address) { this.address = address; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
    public void setStatus(String status) { this.status = status; }
}
