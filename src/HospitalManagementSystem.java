import model.*;
import service.*;
import util.InputUtil;

import java.util.Scanner;
import java.util.UUID;

public class HospitalManagementSystem {

    private static final BillingService billingService = new BillingService();

    // ==================== MODULE HÓA ĐƠN ====================
    private static void billingMenu() {
        System.out.println("""
            \n--- QUẢN LÝ HÓA ĐƠN ---
            1. Tạo hóa đơn mới
            2. Xem danh sách hóa đơn
            3. Cập nhật hóa đơn
            4. Xóa hóa đơn
            5. Tìm kiếm hóa đơn
            6. Lọc dữ liệu (theo tiền hoặc ngày)
            7. Xuất dữ liệu ra file
            0. Quay lại
            """);

        int choice = InputUtil.inputInt("Chọn: ");

        switch (choice) {
            case 1 -> {
                String id = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
                String examinationId = InputUtil.inputString("Mã khám (K001): ");
                String patientId = InputUtil.inputString("Mã bệnh nhân (P001): ");
                String date = InputUtil.inputString("Ngày lập (dd/MM/yyyy): ");
                double examinationFee = InputUtil.inputDouble("Phí khám: ");
                double medicineFee = InputUtil.inputDouble("Tiền thuốc: ");
                double discount = InputUtil.inputDouble("Giảm giá: ");
                double total = examinationFee + medicineFee - discount;
                String paymentStatus = InputUtil.inputString("Trạng thái thanh toán (Đã/Chưa): ");

                Bill bill = new Bill(id, examinationId, patientId, date,
                        examinationFee, medicineFee, discount, total, paymentStatus);

                if (InputUtil.validateBill(bill)) {
                    billingService.addBill(bill);
                } else {
                    System.out.println("❌ Dữ liệu hóa đơn không hợp lệ. Không thêm vào hệ thống!");
                }
            }
            case 2 -> billingService.viewAll();
            case 3 -> billingService.updateBill(InputUtil.inputString("Nhập ID hóa đơn cần cập nhật: "));
            case 4 -> billingService.deleteBill(InputUtil.inputString("Nhập ID hóa đơn cần xóa: "));
            case 5 -> billingService.search();
            case 6 -> billingService.filterBills();
            case 7 -> billingService.exportFile("data/export_billing.txt");
            case 0 -> {}
            default -> System.out.println("❌ Lựa chọn không hợp lệ!");
        }
    }

}
