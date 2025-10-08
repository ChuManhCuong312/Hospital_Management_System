package model;

public class Prescription {
    private String id;
    private String appointmentId;
    private String name;
    private String type;
    private String usage;
    private int quantity;
    private double price;
    private double total;
    private String status;

    public Prescription() {}

    public Prescription(String id, String appointmentId, String name, String type, String usage,
                        int quantity, double price, double total, String status) {
        this.id = id;
        this.appointmentId = appointmentId;
        this.name = name;
        this.type = type;
        this.usage = usage;
        this.quantity = quantity;
        this.price = price;
        this.total = total;
        this.status = status;
    }

    // Dòng dữ liệu từ file CSV: id,appointmentId,name,type,usage,quantity,price,total,status
    public static Prescription fromString(String line) {
        try {
            String[] p = line.split(",");
            if (p.length < 9) return null;
            return new Prescription(
                    p[0].trim(),  // id
                    p[1].trim(),  // appointmentId
                    p[2].trim(),  // name
                    p[3].trim(),  // type
                    p[4].trim(),  // usage
                    Integer.parseInt(p[5].trim()),  // quantity
                    Double.parseDouble(p[6].trim()), // price
                    Double.parseDouble(p[7].trim()), // total
                    p[8].trim()   // status
            );
        } catch (Exception e) {
            return null;
        }
    }

    // Chuyển về dạng lưu file CSV
    public String toDataString() {
        return String.join(",",
                id, appointmentId, name, type, usage,
                String.valueOf(quantity), String.valueOf(price),
                String.valueOf(total), status);
    }

    // Getters
    public String getId() { return id; }
    public String getAppointmentId() { return appointmentId; }
    public String getName() { return name; }
    public String getType() { return type; }
    public String getUsage() { return usage; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public double getTotal() { return total; }
    public String getStatus() { return status; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }
    public void setName(String name) { this.name = name; }
    public void setType(String type) { this.type = type; }
    public void setUsage(String usage) { this.usage = usage; }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.total = this.quantity * this.price;
    }
    public void setPrice(double price) {
        this.price = price;
        this.total = this.price * this.quantity;
    }
    public void setStatus(String status) { this.status = status; }
}
