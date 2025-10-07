package model;

public class Appointment {
    private String id;
    private String patientId;
    private String doctorId;
    private String date;
    private String time;
    private String purpose;
    private String status;
    private double fee;
    private String note;

    public Appointment() {}

    public Appointment(String id, String patientId, String doctorId,
                       String date, String time, String purpose) {
        this.id = id; this.patientId = patientId; this.doctorId = doctorId;
        this.date = date; this.time = time; this.purpose = purpose;
        this.status = "Chờ khám"; this.fee = 0; this.note = "";
    }

    public Appointment(String id, String patientId, String doctorId,
                       String date, String time, String purpose, String status,
                       double fee, String note) {
        this.id = id; this.patientId = patientId; this.doctorId = doctorId;
        this.date = date; this.time = time; this.purpose = purpose;
        this.status = status; this.fee = fee; this.note = note;
    }

    public static Appointment fromString(String line) {
        try {
            String[] p = line.split(",");
            if (p.length < 9) return null;
            return new Appointment(p[0], p[1], p[2], p[3], p[4], p[5], p[6], Double.parseDouble(p[7]), p[8]);
        } catch (Exception e) {
            return null;
        }
    }

    public String toDataString() {
        return String.join(",", id, patientId, doctorId, date, time, purpose, status, String.valueOf(fee), note);
    }

    public String getId() { return id; }
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getPurpose() { return purpose; }
    public String getStatus() { return status; }
    public double getFee() { return fee; }
    public String getNote() { return note; }

    public void setPatientId(String patientId) { this.patientId = patientId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
    public void setDate(String date) { this.date = date; }
    public void setTime(String time) { this.time = time; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    public void setStatus(String status) { this.status = status; }
    public void setFee(double fee) { this.fee = fee; }
    public void setNote(String note) { this.note = note; }
}
