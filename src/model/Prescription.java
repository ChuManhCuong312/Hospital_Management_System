package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Prescription {
    private String id;
    private String examId;
    private String medicineName;
    private String dosage;
    private String usage;
    private String status;
    private int quantity;
    private double price;
    private double total;
    private LocalDate date;

    public Prescription(String id, String examId, String medicineName, String dosage,
            String usage, String status, int quantity, double price, double total, LocalDate date) {

        if (id == null || id.trim().isEmpty())
            throw new IllegalArgumentException("Prescription ID is invalid.");
        if (examId == null || examId.trim().isEmpty())
            throw new IllegalArgumentException("Exam ID is invalid.");
        if (medicineName == null || medicineName.trim().isEmpty())
            throw new IllegalArgumentException("Medicine name is invalid.");
        if (quantity <= 0)
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        if (price < 0)
            throw new IllegalArgumentException("Price cannot be negative.");
        if (total < 0)
            throw new IllegalArgumentException("Total cannot be negative.");
        if (date == null)
            throw new IllegalArgumentException("Date cannot be null.");

        this.id = id.trim();
        this.examId = examId.trim();
        this.medicineName = medicineName.trim();
        this.dosage = dosage.trim();
        this.usage = usage.trim();
        this.status = status.trim();
        this.quantity = quantity;
        this.price = price;
        this.total = total;
        this.date = date;
    }

    // --- Getters ---
    public String getId() {
        return id;
    }

    public String getExamId() {
        return examId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public String getDosage() {
        return dosage;
    }

    public String getUsage() {
        return usage;
    }

    public String getStatus() {
        return status;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public double getTotal() {
        return price * quantity;
    }

    public LocalDate getDate() {
        return date;
    }

    // --- Setters ---
    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    // --- toString() ---
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return String.join(",",
                id,
                examId,
                medicineName,
                formatter.format(date),
                dosage,
                usage,
                String.valueOf(quantity),
                String.valueOf(price),
                String.valueOf(getTotal()),
                status);
    }

    // --- fromString() ---
    public static Prescription fromString(String line) {
        String[] parts = line.split(",");
        if (parts.length != 10)
            return null;

        try {
            String id = parts[0].trim();
            String examId = parts[1].trim();
            String medicineName = parts[2].trim();
            LocalDate date = LocalDate.parse(parts[3].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String dosage = parts[4].trim();
            String usage = parts[5].trim();
            int quantity = Integer.parseInt(parts[6].trim());
            double price = Double.parseDouble(parts[7].trim().replace(",", "."));
            double total = Double.parseDouble(parts[8].trim().replace(",", "."));
            String status = parts[9].trim();

            return new Prescription(id, examId, medicineName, dosage, usage, status, quantity, price, total, date);
        } catch (Exception e) {
            System.err.println("⚠️ Lỗi đọc dòng: " + line + " → " + e.getMessage());
            return null;
        }
    }
    // not blank anymore
}