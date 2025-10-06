package model;

public class Bill {
    private String id;
    private String patientId;
    private double amount;
    private String date;

    public Bill(String id, String patientId, double amount, String date) {
        this.id = id;
        this.patientId = patientId;
        this.amount = amount;
        this.date = date;
    }

    public String getId() { return id; }
    public String getPatientId() { return patientId; }
    public double getAmount() { return amount; }
    public String getDate() { return date; }

    @Override
    public String toString() {
        return id + "," + patientId + "," + amount + "," + date;
    }

    public static Bill fromString(String line) {
        String[] parts = line.split(",");
        if (parts.length != 4) return null;
        return new Bill(parts[0], parts[1], Double.parseDouble(parts[2]), parts[3]);
    }
}
