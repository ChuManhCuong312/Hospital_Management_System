package service;

import exception.InvalidDataException;
import model.Medicine;
import util.FileUtil;
import util.InputUtil;
import util.Validator;

import java.util.ArrayList;
import java.util.List;

public class MedicineService {
    private static final String FILE_PATH = "data/medicines.txt";

    public List<Medicine> getAll() {
        List<String> lines = FileUtil.readFile(FILE_PATH);
        List<Medicine> list = new ArrayList<>();
        for (String l : lines) {
            Medicine m = Medicine.fromString(l);
            if (m != null) list.add(m);
        }
        return list;
    }

    private void saveAll(List<Medicine> list) {
        List<String> lines = new ArrayList<>();
        for (Medicine m : list) lines.add(m.toDataString());
        FileUtil.writeFile(FILE_PATH, lines);
    }

    // Thêm thuốc
    public void addMedicine() {
        try {
            System.out.println("===== THÊM THUỐC =====");
            String id = InputUtil.nhapChuoi("Nhập mã thuốc: ");
            Validator.checkNotEmpty(id, "Mã thuốc không được để trống!");
            if (findById(id) != null) throw new InvalidDataException("Mã thuốc đã tồn tại!");

            String name = InputUtil.nhapChuoi("Nhập tên thuốc: ");
            Validator.checkNotEmpty(name, "Tên thuốc không được để trống!");

            String type = InputUtil.nhapChuoi("Nhập loại thuốc: ");
            String usage = InputUtil.nhapChuoi("Nhập cách dùng: ");
            int qty = InputUtil.nhapSoNguyen("Nhập số lượng: ");
            Validator.checkPositive(qty, "Số lượng phải lớn hơn 0!");
            double price = InputUtil.nhapSoThuc("Nhập đơn giá: ");
            if (price < 0) throw new InvalidDataException("Đơn giá không hợp lệ!");

            double total = qty * price;
            String status = InputUtil.nhapChuoi("Nhập trạng thái: ");

            Medicine m = new Medicine(id, name, type, usage, qty, price, total, status);
            FileUtil.appendToFile(FILE_PATH, m.toDataString());
            System.out.println("Đã thêm thuốc: " + name);
        } catch (InvalidDataException e) {
            System.out.println("Lỗi: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Lỗi khi thêm thuốc: " + e.getMessage());
        }
    }

    // Cập nhật thuốc
    public void updateMedicine() {
        try {
            System.out.println("===== CẬP NHẬT THUỐC =====");
            String id = InputUtil.nhapChuoi("Nhập mã thuốc cần cập nhật: ");
            List<Medicine> list = getAll();
            boolean found = false;
            for (Medicine m : list) {
                if (m.getId().equalsIgnoreCase(id)) {
                    String name = InputUtil.nhapChuoi("Tên (" + m.getName() + "): ");
                    String type = InputUtil.nhapChuoi("Loại (" + m.getType() + "): ");
                    String usage = InputUtil.nhapChuoi("Cách dùng (" + m.getUsage() + "): ");
                    int qty = InputUtil.nhapSoNguyen("Số lượng (" + m.getQuantity() + "): ");
                    double price = InputUtil.nhapSoThuc("Đơn giá (" + m.getPrice() + "): ");
                    String status = InputUtil.nhapChuoi("Trạng thái (" + m.getStatus() + "): ");

                    if (!name.isEmpty()) m.setName(name);
                    if (!type.isEmpty()) m.setType(type);
                    if (!usage.isEmpty()) m.setUsage(usage);
                    if (qty >= 0) m.setQuantity(qty);
                    if (price >= 0) m.setPrice(price);
                    if (!status.isEmpty()) m.setStatus(status);

                    found = true;
                    break;
                }
            }
            if (found) {
                saveAll(list);
                System.out.println("Đã cập nhật thuốc: " + id);
            } else System.out.println("Không tìm thấy thuốc!");
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật thuốc: " + e.getMessage());
        }
    }

    // Xóa thuốc
    public void deleteMedicine() {
        try {
            System.out.println("===== XÓA THUỐC =====");
            String id = InputUtil.nhapChuoi("Nhập mã thuốc cần xóa: ");
            List<Medicine> list = getAll();
            boolean removed = list.removeIf(m -> m.getId().equalsIgnoreCase(id));
            if (removed) {
                saveAll(list);
                System.out.println("Đã xóa thuốc: " + id);
            } else System.out.println("Không tìm thấy thuốc!");
        } catch (Exception e) {
            System.out.println("Lỗi khi xóa thuốc: " + e.getMessage());
        }
    }

    public void viewAll() {
        List<Medicine> list = getAll();
        if (list.isEmpty()) {
            System.out.println("Không có thuốc nào!");
            return;
        }
        System.out.println("=================================================================================================================");
        System.out.printf("| %-8s | %-20s | %-12s | %-25s | %-6s | %-10s | %-10s |%n",
                "Mã", "Tên", "Loại", "Cách dùng", "SL", "Đơn giá", "Tổng");
        System.out.println("=================================================================================================================");
        for (Medicine m : list) {
            System.out.printf("| %-8s | %-20s | %-12s | %-25s | %-6d | %-10.1f | %-10.1f |%n",
                    m.getId(), m.getName(), m.getType(), m.getUsage(), m.getQuantity(), m.getPrice(), m.getTotal());
        }
        System.out.println("=================================================================================================================");
    }

    public Medicine findById(String id) {
        for (Medicine m : getAll()) if (m.getId().equalsIgnoreCase(id)) return m;
        return null;
    }

    // Tìm kiếm thuốc theo tên / mã
    public void searchMedicine() {
        try {
            System.out.println("===== TÌM KIẾM THUỐC =====");
            System.out.println("1. Theo mã");
            System.out.println("2. Theo tên (hoặc 1 phần tên)");
            int c = InputUtil.nhapLuaChon("Chọn: ", 1, 2);
            String key = InputUtil.nhapChuoi("Nhập từ khóa: ");
            List<Medicine> res = new ArrayList<>();
            for (Medicine m : getAll()) {
                if (c == 1 && m.getId().equalsIgnoreCase(key)) res.add(m);
                else if (c == 2 && m.getName().toLowerCase().contains(key.toLowerCase())) res.add(m);
            }
            if (res.isEmpty()) System.out.println("Không tìm thấy thuốc phù hợp!");
            else {
                System.out.println("Kết quả:");
                for (Medicine m : res) System.out.println(m.toDataString());
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi tìm kiếm thuốc: " + e.getMessage());
        }
    }

    // Lọc thuốc theo loại hoặc trạng thái
    public void filterMedicine() {
        List<Medicine> list = getAll(); // lấy danh sách thuốc từ file
        if (list.isEmpty()) {
            System.out.println("Không có thuốc nào trong danh sách!");
            return;
        }

        System.out.println("\n--- LỌC THUỐC ---");
        System.out.println("1. Lọc theo loại thuốc");
        System.out.println("2. Lọc theo trạng thái");
        int choice = InputUtil.nhapLuaChon("Chọn tiêu chí: ", 1, 2);

        boolean found = false;
        System.out.printf("%-10s %-20s %-10s %-25s %-10s %-10s %-10s %-10s\n",
                "ID", "Tên thuốc", "Loại", "Cách dùng", "SL", "Giá", "Tổng", "Trạng thái");

        if (choice == 1) {
            String type = InputUtil.nhapChuoi("Nhập loại thuốc cần lọc: ").toLowerCase();
            for (Medicine m : list) {
                if (m.getType().toLowerCase().contains(type)) {
                    System.out.printf("%-10s %-20s %-10s %-25s %-10d %-10.2f %-10.2f %-10s\n",
                            m.getId(), m.getName(), m.getType(), m.getUsage(),
                            m.getQuantity(), m.getPrice(), m.getTotal(), m.getStatus());
                    found = true;
                }
            }
        } else {
            String status = InputUtil.nhapChuoi("Nhập trạng thái cần lọc: ").toLowerCase();
            for (Medicine m : list) {
                if (m.getStatus().toLowerCase().contains(status)) {
                    System.out.printf("%-10s %-20s %-10s %-15s %-10d %-10.2f %-10.2f %-10s\n",
                            m.getId(), m.getName(), m.getType(), m.getUsage(),
                            m.getQuantity(), m.getPrice(), m.getTotal(), m.getStatus());
                    found = true;
                }
            }
        }

        if (!found) System.out.println("Không tìm thấy thuốc nào phù hợp!");
    }
}
