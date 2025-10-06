package model;

public class Bill {
    private String id;                
    private String examinationId;     
    private String patientId;         
    private String date;              
    private double examinationFee;    
    private double medicineFee;      
    private double discount;          
    private double total;            
    private String paymentStatus;     

    public Bill(String id, String examinationId, String patientId, String date,
                double examinationFee, double medicineFee, double discount,
                double total, String paymentStatus) {
        this.id = id;
        this.examinationId = examinationId;
        this.patientId = patientId;
        this.date = date;
        this.examinationFee = examinationFee;
        this.medicineFee = medicineFee;
        this.discount = discount;
        this.total = total;
        this.paymentStatus = paymentStatus;
    }

    public String getId() { return id; }
    public String getExaminationId() { return examinationId; }
    public String getPatientId() { return patientId; }
    public String getDate() { return date; }
    public double getExaminationFee() { return examinationFee; }
    public double getMedicineFee() { return medicineFee; }
    public double getDiscount() { return discount; }
    public double getTotal() { return total; }
    public String getPaymentStatus() { return paymentStatus; }

    public void setExaminationFee(double value) { this.examinationFee = value; }
    public void setMedicineFee(double value) { this.medicineFee = value; }
    public void setDiscount(double value) { this.discount = value; }
    public void setTotal(double value) { this.total = value; }
    public void setPaymentStatus(String value) { this.paymentStatus = value; }

    @Override
    public String toString() {
        return id + "," +
               examinationId + "," +
               patientId + "," +
               date + "," +
               examinationFee + "," +
               medicineFee + "," +
               discount + "," +
               total + "," +
               paymentStatus;
    }

    public static Bill fromString(String line) {
        String[] parts = line.split(",", -1);
        if (parts.length != 9) return null;
        return new Bill(parts[0], parts[1], parts[2], parts[3], Double.parseDouble(parts[4]), Double.parseDouble(parts[5]), Double.parseDouble(parts[6]), Double.parseDouble(parts[7]), parts[8]);
    }
}
