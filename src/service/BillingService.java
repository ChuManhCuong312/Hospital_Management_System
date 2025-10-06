package service;

import model.Bill;
import util.FileUtil;
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

}
