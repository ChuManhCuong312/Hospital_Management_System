package model;

public class Doctor {
    private String id;
    private String name;
    private String specialty;
    private String degree;
    private int experience;
    private String phone;
    private String email;
    private String workSchedule;
    private String status;


    public Doctor() {}

    // Minimal convenience constructor
    public Doctor(String id, String name, String specialty, String degree,
                  int experience, String phone, String email, String workSchedule, String status) {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
        this.degree = degree;
        this.experience = experience;
        this.phone = phone;
        this.email = email;
        this.workSchedule = workSchedule;
        this.status = status;
    }



    // Tạo Doctor từ chuỗi lưu file
    public static Doctor fromString(String line) {
        try {
            String[] p = line.split(",");
            if (p.length < 9) return null;
            return new Doctor(
                    p[0].trim(), // id
                    p[1].trim(), // name
                    p[2].trim(), // specialty
                    p[3].trim(), // degree
                    Integer.parseInt(p[4].trim()), // experience
                    p[5].trim(), // phone
                    p[6].trim(), // email
                    p[7].trim(), // workSchedule
                    p[8].trim()  // status
            );
        } catch (Exception e) {
            return null;
        }
    }

    public String toDataString() {
        return String.join(",",
                id, name, specialty, degree,
                String.valueOf(experience), phone, email, workSchedule, status
        );
    }


    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDegree() { return degree; }
    public void setDegree(String degree) { this.degree = degree; }

    public int getExperience() { return experience; }
    public void setExperience(int experience) { this.experience = experience; }

    public String getWorkSchedule() { return workSchedule; }
    public void setWorkSchedule(String workSchedule) { this.workSchedule = workSchedule; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}