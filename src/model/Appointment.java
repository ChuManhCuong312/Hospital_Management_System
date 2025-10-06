package model;

public class Appointment {
    private String id;
    private String patientId;
    private String doctorId;
    private String date;

    public Appointment(String id, String patientId, String doctorId, String date) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
    }

    public String getId() { return id; }
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }
    public String getDate() { return date; }

    @Override
    public String toString() {
        return id + "," + patientId + "," + doctorId + "," + date;
    }

    public static Appointment fromString(String line) {
        String[] parts = line.split(",");
        if (parts.length != 4) return null;
        return new Appointment(parts[0], parts[1], parts[2], parts[3]);
    }
}
