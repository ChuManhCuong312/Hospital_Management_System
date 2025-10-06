package model;

public class Medicine {
    private String id;
    private String name;
    private int quantity;
    private double price;

    public Medicine(String id, String name, int quantity, double price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    @Override
    public String toString() {
        return id + "," + name + "," + quantity + "," + price;
    }

    public static Medicine fromString(String line) {
        String[] parts = line.split(",");
        if (parts.length != 4) return null;
        return new Medicine(parts[0], parts[1], Integer.parseInt(parts[2]), Double.parseDouble(parts[3]));
    }
}
