package service;

import model.Bill;
import util.FileUtil;
import util.InputUtil;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class BillingService {
    private static final String FILE_PATH = "data/billing.txt";

    public void addBill(Bill b) {
        FileUtil.appendToFile(FILE_PATH, b.toString());
        System.out.println("✅ Đã tạo hóa đơn ID: " + b.getId());
    }

    public List<Bill> getAllBills() {
        List<String> lines = FileUtil.readFile(FILE_PATH);
        List<Bill> bills = new ArrayList<>();
        for (String line : lines) {
            Bill b = Bill.fromString(line);
            if (b != null) bills.add(b);
        }
        return bills;
    }

    public void viewAll() {
        List<Bill> list = getAllBills();
        if (list.isEmpty()) {
            System.out.println("⚠️  Chưa có hóa đơn nào.");
            return;
        }

        System.out.println("\n+--------+----------+----------+------------+------------+------------+------------+------------+-----------------+");
        System.out.println("| Mã HĐ  | Mã khám  | Mã BN    |  Ngày lập  |  Phí khám  | Tiền thuốc |  Giảm giá  | Tổng tiền  | Trạng thái TT  |");
        System.out.println("+--------+----------+----------+------------+------------+------------+------------+------------+-----------------+");

        for (Bill b : list) {
            System.out.printf("| %-6s | %-8s | %-8s | %-10s | %10.2f | %10.2f | %10.2f | %10.2f | %-15s |%n",
                    b.getId(),
                    b.getExaminationId(),
                    b.getPatientId(),
                    b.getDate(),
                    b.getExaminationFee(),
                    b.getMedicineFee(),
                    b.getDiscount(),
                    b.getTotal(),
                    b.getPaymentStatus());
        }

        System.out.println("+--------+----------+----------+------------+------------+------------+------------+------------+-----------------+");
    }

    public void updateBill(String id) {
        List<Bill> bills = getAllBills();
        boolean found = false;

        for (Bill b : bills) {
            if (b.getId().equalsIgnoreCase(id)) {
                found = true;
                System.out.println("🔄 Đang cập nhật hóa đơn: " + id);

                String input;

                input = InputUtil.inputString("Nhập phí khám mới (Enter để giữ nguyên): ");
                if (!input.isBlank()) b.setExaminationFee(Double.parseDouble(input));

                input = InputUtil.inputString("Nhập tiền thuốc mới (Enter để giữ nguyên): ");
                if (!input.isBlank()) b.setMedicineFee(Double.parseDouble(input));

                input = InputUtil.inputString("Nhập giảm giá mới (Enter để giữ nguyên): ");
                if (!input.isBlank()) b.setDiscount(Double.parseDouble(input));

                b.setTotal(b.getExaminationFee() + b.getMedicineFee() - b.getDiscount());

                input = InputUtil.inputString("Nhập trạng thái thanh toán mới (Enter để giữ nguyên): ");
                if (!input.isBlank()) b.setPaymentStatus(input);

                System.out.println("✅ Cập nhật thành công hóa đơn " + id);
                break;
            }
        }

        if (!found) {
            System.out.println("❌ Không tìm thấy hóa đơn có ID: " + id);
            return;
        }

        List<String> newLines = new ArrayList<>();
        for (Bill b : bills) newLines.add(b.toString());
        FileUtil.writeFile(FILE_PATH, newLines);
    }

    public void deleteBill(String id) {
        List<Bill> bills = getAllBills();
        boolean found = bills.removeIf(b -> b.getId().equalsIgnoreCase(id));

        if (found) {
            List<String> newLines = new ArrayList<>();
            for (Bill b : bills) newLines.add(b.toString());
            FileUtil.writeFile(FILE_PATH, newLines);
            System.out.println("🗑️  Đã xóa hóa đơn có ID: " + id);
        } else {
            System.out.println("❌ Không tìm thấy hóa đơn có ID: " + id);
        }
    }

    public void search() {
        String key = InputUtil.inputString("Nhập mã bệnh nhân hoặc ngày hoặc tổng tiền cần tìm: ");
        List<Bill> results = new ArrayList<>();

        for (Bill b : getAllBills()) {
            if (b.getPatientId().equalsIgnoreCase(key)
                    || b.getDate().equalsIgnoreCase(key)
                    || String.valueOf(b.getTotal()).equals(key)) {
                results.add(b);
            }
        }

        if (results.isEmpty()) System.out.println("❌ Không tìm thấy kết quả nào.");
        else {
            System.out.println("🔍 Kết quả tìm kiếm:");
            results.forEach(b -> System.out.println(b.toString()));
        }
    }

    public void filterBills() {
        System.out.println("""
            \n--- LỌC HÓA ĐƠN ---
            1. Theo khoảng tiền
            2. Theo khoảng ngày
            0. Quay lại
            """);

        int choice = InputUtil.inputInt("Chọn loại lọc: ");

        switch (choice) {
            case 1 -> filterByAmountRange();
            case 2 -> filterByDateRange();
            case 0 -> System.out.println("⬅️  Quay lại menu trước.");
            default -> System.out.println("❌ Lựa chọn không hợp lệ!");
        }
    }

    private void filterByAmountRange() {
        double min = InputUtil.inputDouble("Nhập tổng tiền tối thiểu: ");
        double max = InputUtil.inputDouble("Nhập tổng tiền tối đa: ");

        List<Bill> filtered = new ArrayList<>();
        for (Bill b : getAllBills()) {
            if (b.getTotal() >= min && b.getTotal() <= max) filtered.add(b);
        }

        if (filtered.isEmpty()) {
            System.out.println("⚠️  Không có hóa đơn nào trong khoảng tiền này.");
        } else {
            System.out.println("💰 Danh sách hóa đơn trong khoảng " + min + " - " + max + ":");
            printBills(filtered);
        }
    }

    private void filterByDateRange() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy"); 

        LocalDate fromDate, toDate;
        while (true) {
            try {
                String from = InputUtil.inputString("Nhập ngày bắt đầu (dd/MM/yyyy): ");
                String to = InputUtil.inputString("Nhập ngày kết thúc (dd/MM/yyyy): ");
                fromDate = LocalDate.parse(from, fmt);
                toDate = LocalDate.parse(to, fmt);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("❌ Ngày không hợp lệ! Vui lòng nhập đúng định dạng dd/MM/yyyy.");
            }
        }

        List<Bill> filtered = new ArrayList<>();
        for (Bill b : getAllBills()) {
            try {
                LocalDate billDate = LocalDate.parse(b.getDate(), fmt);
                if ((billDate.isEqual(fromDate) || billDate.isAfter(fromDate))
                        && (billDate.isEqual(toDate) || billDate.isBefore(toDate))) {
                    filtered.add(b);
                }
            } catch (Exception e) {
                System.out.println("⚠️  Lỗi khi đọc ngày của hóa đơn ID: " + b.getId());
            }
        }

        if (filtered.isEmpty()) {
            System.out.println("⚠️  Không có hóa đơn nào trong khoảng ngày này.");
        } else {
            System.out.println("📅 Danh sách hóa đơn từ " + fromDate.format(fmt) + " đến " + toDate.format(fmt) + ":");
            printBills(filtered);
        }
    }


    private void printBills(List<Bill> list) {
        System.out.println("\n+--------+----------+----------+------------+------------+------------+------------+------------+-----------------+");
        System.out.println("| Mã HĐ  | Mã khám  | Mã BN    |  Ngày lập  |  Phí khám  | Tiền thuốc |  Giảm giá  | Tổng tiền  | Trạng thái TT  |");
        System.out.println("+--------+----------+----------+------------+------------+------------+------------+------------+-----------------+");

        for (Bill b : list) {
            System.out.printf("| %-6s | %-8s | %-8s | %-10s | %10.2f | %10.2f | %10.2f | %10.2f | %-15s |%n",
                    b.getId(),
                    b.getExaminationId(),
                    b.getPatientId(),
                    b.getDate(),
                    b.getExaminationFee(),
                    b.getMedicineFee(),
                    b.getDiscount(),
                    b.getTotal(),
                    b.getPaymentStatus());
        }

        System.out.println("+--------+----------+----------+------------+------------+------------+------------+------------+-----------------+");
    }

    public void exportFile(String outPath) {
        String patientId = InputUtil.inputString("Nhập mã bệnh nhân cần xuất hóa đơn: ");

        List<Bill> bills = getAllBills();
        List<Bill> filtered = new ArrayList<>();

        for (Bill b : bills) {
            if (b.getPatientId().equalsIgnoreCase(patientId)) {
                filtered.add(b);
            }
        }

        if (filtered.isEmpty()) {
            System.out.println("⚠️  Không tìm thấy hóa đơn nào của bệnh nhân có mã: " + patientId);
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(outPath, false), "UTF-8"))) {

            for (Bill b : filtered) {
                writer.write(b.toString());
                writer.newLine();
            }

            System.out.println("📤 Đã xuất " + filtered.size() + " hóa đơn của bệnh nhân "
                    + patientId + " ra file: " + outPath);

        } catch (IOException e) {
            System.out.println("⚠️  Lỗi khi ghi file: " + e.getMessage());
        }
    }

}
