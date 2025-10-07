package model;

public class Doctor {
    private String doctorId, name, specialty, degree, phone, email, workSchedule, status;
    private int experience;

    public Doctor(String doctorId, String name, String specialty, String bangCap,
                  int experience, String phone, String email, String workSchedule, String status) {
        this.doctorId = doctorId;
        this.name = name;
        this.specialty = specialty;
        this.degree = bangCap;
        this.experience = experience;
        this.phone = phone;
        this.email = email;
        this.workSchedule = workSchedule;
        this.status = status;
    }

    // Getters & Setters
    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    public String getDegree() { return degree; }
    public void setDegree(String degree) { this.degree = degree; }
    public int getExperience() { return experience; }
    public void setExperience(int experience) { this.experience = experience; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getWorkSchedule() { return workSchedule; }
    public void setWorkSchedule(String workSchedule) { this.workSchedule = workSchedule; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // Chuyển sang chuỗi lưu file
    public String toFileString() {
        return String.join(",",
                doctorId, name, specialty, degree,
                String.valueOf(experience), phone, email, workSchedule, status);
    }

    // Tạo Doctor từ dòng file
    public static Doctor fromFileString(String line) {
        String[] parts = line.split(",");
        if (parts.length != 9) return null;
        try {
            return new Doctor(
                    parts[0].trim(),
                    parts[1].trim(),
                    parts[2].trim(),
                    parts[3].trim(),
                    Integer.parseInt(parts[4].trim()),
                    parts[5].trim(),
                    parts[6].trim(),
                    parts[7].trim(),
                    parts[8].trim()
            );
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
