package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


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

            String rawDate = p[3].trim();
            String normalizedDate = rawDate;

            // Nếu ngày có dấu "/", thì chuyển sang dạng yyyy-MM-dd
            if (rawDate.contains("/")) {
                try {
                    LocalDate d = LocalDate.parse(rawDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    normalizedDate = d.format(DateTimeFormatter.ISO_LOCAL_DATE); // yyyy-MM-dd
                } catch (Exception ex) {
                    // Nếu lỗi parse thì giữ nguyên
                    normalizedDate = rawDate;
                }
            }

            return new Appointment(
                    p[0].trim(),
                    p[1].trim(),
                    p[2].trim(),
                    normalizedDate,
                    p[4].trim(),
                    p[5].trim(),
                    p[6].trim(),
                    Double.parseDouble(p[7].trim()),
                    p[8].trim()
            );
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
