package model;

public class Medicine {
    private String id;
    private String name;
    private String type;
    private String usage;
    private int quantity;
    private double price;
    private double total;
    private String status;

    public Medicine() {}

    public Medicine(String id, String name, String type, int quantity, double price, double total, String status) {
        this.id = id; this.name = name; this.type = type; this.usage = ""; this.quantity = quantity;
        this.price = price; this.total = total; this.status = status;
    }

    public Medicine(String id, String name, String type, String usage, int quantity, double price, double total, String status) {
        this(id, name, type, quantity, price, total, status);
        this.usage = usage;
    }

    public static Medicine fromString(String line) {
        try {
            String[] p = line.split(",");
            if (p.length < 8) return null;
            return new Medicine(p[0], p[1], p[2], p[3], Integer.parseInt(p[4]), Double.parseDouble(p[5]), Double.parseDouble(p[6]), p[7]);
        } catch (Exception e) {
            return null;
        }
    }

    public String toDataString() {
        return String.join(",", id, name, type, usage, String.valueOf(quantity), String.valueOf(price), String.valueOf(total), status);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
    public String getUsage() { return usage; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public double getTotal() { return total; }
    public String getStatus() { return status; }

    public void setQuantity(int quantity) { this.quantity = quantity; this.total = this.quantity * this.price; }
    public void setPrice(double price) { this.price = price; this.total = this.price * this.quantity; }
    public void setStatus(String status) { this.status = status; }
    public void setName(String name) { this.name = name; }
    public void setType(String type) { this.type = type; }
    public void setUsage(String usage) { this.usage = usage; }
}
