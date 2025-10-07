package service;

import model.Prescription;
import util.FileUtil;

import java.util.*;
import java.time.LocalDate;

public class PrescriptionService {
    private static final String FILE_PATH = "data/prescriptions.txt";

    // Create
    public void addPrescription(Prescription p) {
        FileUtil.appendToFile(FILE_PATH, p.toString());
        System.out.println("Đã thêm đơn thuốc ID: " + p.getId());
    }

    // Read
    public List<Prescription> getAllPrescriptions() {
        List<String> lines = FileUtil.readFile(FILE_PATH);
        List<Prescription> list = new ArrayList<>();

        for (String line : lines) {
            try {
                Prescription p = Prescription.fromString(line);
                if (p != null)
                    list.add(p);
            } catch (Exception e) {
                System.err.println(" Lỗi đọc dòng: " + line + " → " + e.getMessage());
            }
        }
        return list;
    }

    // View all
    public void viewAll() {
        List<Prescription> list = getAllPrescriptions();
        if (list.isEmpty()) {
            System.out.println("Không có đơn thuốc nào trong hệ thống!");
            return;
        }

        System.out.println(
                "+------+---------+-----------------+------------+------------+----------+--------+----------+----------+------------+");
        System.out.println(
                "|  ID  | Exam ID |  Tên thuốc     |   Ngày     |  Liều dùng |  Cách dùng | SL  |  Giá   |  Tổng  |  Trạng thái |");
        System.out.println(
                "+------+---------+-----------------+------------+------------+----------+--------+----------+----------+------------+");

        for (Prescription p : list) {
            System.out.printf("| %-4s | %-7s | %-15s | %-10s | %-10s | %-10s | %-4d | %-8.2f | %-8.2f | %-10s |%n",
                    p.getId(),
                    p.getExamId(),
                    p.getMedicineName(),
                    p.getDate(),
                    p.getDosage(),
                    p.getUsage(),
                    p.getQuantity(),
                    p.getPrice(),
                    p.getTotal(),
                    p.getStatus());
        }

        System.out.println(
                "+------+---------+-----------------+------------+------------+----------+--------+----------+----------+------------+");
    }

    // Find by id
    public Prescription findById(String id) {
        for (Prescription p : getAllPrescriptions()) {
            if (p.getId().equalsIgnoreCase(id))
                return p;
        }
        return null;
    }

    // update
    public void updatePrescription(String id, String newMedicineName, String newDosage,
            String newUsage, String newStatus, int newQuantity, double newPrice, LocalDate newDate) {

        List<Prescription> list = getAllPrescriptions();
        boolean found = false;

        for (Prescription p : list) {
            if (p.getId().equalsIgnoreCase(id)) {
                if (newMedicineName != null && !newMedicineName.isEmpty())
                    p.setMedicineName(newMedicineName);
                if (newDosage != null && !newDosage.isEmpty())
                    p.setDosage(newDosage);
                if (newUsage != null && !newUsage.isEmpty())
                    p.setUsage(newUsage);
                if (newStatus != null && !newStatus.isEmpty())
                    p.setStatus(newStatus);
                if (newQuantity > 0)
                    p.setQuantity(newQuantity);
                if (newPrice > 0)
                    p.setPrice(newPrice);
                if (newDate != null)
                    p.setDate(newDate);

                // cập nhật total
                p.setTotal(p.getPrice() * p.getQuantity());

                found = true;
                break;
            }
        }

        if (found) {
            saveAll(list);
            System.out.println("✅ Đã cập nhật đơn thuốc có ID: " + id);
        } else {
            System.out.println("❌ Không tìm thấy đơn thuốc có ID: " + id);
        }
    }

    // Xóa đơn thuốc
    public void deletePrescription(String id) {
        Scanner sc = new Scanner(System.in);
        List<Prescription> list = getAllPrescriptions();
        Prescription found = list.stream()
                .filter(p -> p.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);

        if (found == null) {
            System.out.println(" Không tìm thấy đơn thuốc có ID: " + id);
            return;
        }

        // Hiển thị thông tin đơn thuốc trước khi xác nhận
        System.out.println("Thông tin đơn thuốc sẽ bị xóa:");
        System.out.println(found);

        System.out.print("Bạn có chắc muốn xóa đơn thuốc này? (y/n): ");
        String confirm = sc.nextLine();

        if (confirm.equalsIgnoreCase("y")) {
            list.remove(found);
            saveAll(list);
            System.out.println(" Đã xóa đơn thuốc ID: " + id);
        } else {
            System.out.println(" Hủy thao tác xóa.");
        }
    }

    // Ghi lại toàn bộ danh sách vào file
    private void saveAll(List<Prescription> prescriptions) {
        List<String> lines = new ArrayList<>();
        for (Prescription p : prescriptions) {
            lines.add(p.toString());
        }
        FileUtil.writeFile(FILE_PATH, lines);
    }

    // ===== Tìm theo mã khám =====
    public List<Prescription> findByExamId(String examId) {
        List<Prescription> result = new ArrayList<>();
        for (Prescription p : getAllPrescriptions()) {
            if (p.getExamId().equalsIgnoreCase(examId)) {
                result.add(p);
            }
        }
        return result;
    }

    // ===== Tìm đơn thuốc theo ngày =====
    public List<Prescription> findByDate(LocalDate date) {
        List<Prescription> result = new ArrayList<>();
        for (Prescription p : getAllPrescriptions()) {
            if (p.getDate().equals(date)) {
                result.add(p);
            }
        }
        return result;
    }

    // ===== Tìm theo tên thuốc =====
    public List<Prescription> findByMedicineName(String name) {
        List<Prescription> result = new ArrayList<>();
        for (Prescription p : getAllPrescriptions()) {
            if (p.getMedicineName().toLowerCase().contains(name.toLowerCase())) {
                result.add(p);
            }
        }
        return result;
    }
}