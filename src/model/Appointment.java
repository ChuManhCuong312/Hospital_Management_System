package model;

public class Appointment {
    private String id;
    private String patientId;
    private String doctorId;
    private String date;
    private String symptom;
    private String diagnose;
    private String prescription;
    private Double cost;
    private String status;

    public Appointment(String id, String patientId, String doctorId, String date,String symptom,
                       String diagnose,String prescription,Double cost,String status) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.symptom = symptom;
        this.diagnose = diagnose;
        this.prescription = prescription;
        this.cost = cost;
        this.status = status;
    }

    public String getId() { return id; }
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }
    public String getDate() { return date; }
    public String getSymptom() {return symptom;}
    public String getDiagnose() {return diagnose;}
    public String getPrescription() {return prescription;}
    public Double getCost() {return cost;}
    public String getStatus() {return  status;}

    @Override
    public String toString() {
        return id + "," + patientId + "," + doctorId + "," + date + "," + symptom + "," +
                diagnose + "," + prescription + "," + cost + "," + status;
    }

    public static Appointment fromString(String line) {
        String[] parts = line.split(",");
        if (parts.length != 9) return null;

        try {
            Double cost = Double.parseDouble(parts[7]);
            return new Appointment(
                    parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], cost, parts[8]
            );
        } catch (NumberFormatException e) {
            System.err.println("Invalid cost value in line: " + line);
            return null;
        }
    }
}
