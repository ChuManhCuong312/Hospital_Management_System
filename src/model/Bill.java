package model;

public class Bill {
    private String id;
    private String appointmentId;
    private String patientId;
    private String date;
    private double examFee;
    private double medicineFee;
    private double discount;
    private double total;
    private String status;

    public Bill() {}

    public Bill(String id, String appointmentId, String patientId, String date,
                double examFee, double medicineFee, double discount,
                double total, String status) {
        this.id = id; this.appointmentId = appointmentId; this.patientId = patientId;
        this.date = date; this.examFee = examFee; this.medicineFee = medicineFee;
        this.discount = discount; this.total = total; this.status = status;
    }

    public static Bill fromString(String line) {
        try {
            String[] p = line.split(",");
            if (p.length < 9) return null;
            return new Bill(p[0], p[1], p[2], p[3],
                    Double.parseDouble(p[4]), Double.parseDouble(p[5]),
                    Double.parseDouble(p[6]), Double.parseDouble(p[7]), p[8]);
        } catch (Exception e) {
            return null;
        }
    }

    public String toDataString() {
        return String.join(",", id, appointmentId, patientId, date,
                String.valueOf(examFee), String.valueOf(medicineFee),
                String.valueOf(discount), String.valueOf(total), status);
    }

    public String getId() { return id; }
    public String getAppointmentId() { return appointmentId; }
    public String getPatientId() { return patientId; }
    public String getDate() { return date; }
    public double getExamFee() { return examFee; }
    public double getMedicineFee() { return medicineFee; }
    public double getDiscount() { return discount; }
    public double getTotal() { return total; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public void setExamFee(double examFee) { this.examFee = examFee; recalc(); }
    public void setMedicineFee(double medicineFee) { this.medicineFee = medicineFee; recalc(); }
    public void setDiscount(double discount) { this.discount = discount; recalc(); }
    private void recalc() { this.total = (examFee + medicineFee) * (1 - discount / 100.0); }
}
