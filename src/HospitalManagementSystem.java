import model.*;
import service.*;
import util.InputUtil;

import java.util.Scanner;
import java.util.UUID;

public class HospitalManagementSystem {

    private static final PatientService patientService = new PatientService();
    private static final DoctorService doctorService = new DoctorService();
    private static final AppointmentService appointmentService = new AppointmentService();
    private static final BillingService billingService = new BillingService();
    private static final MedicineService medicineService = new MedicineService();

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== 🏥 HỆ THỐNG QUẢN LÝ BỆNH VIỆN ===");
        while (true) {
            showMainMenu();
            int choice = InputUtil.inputInt("👉 Chọn chức năng: ");
            switch (choice) {
                case 1 -> patientMenu();
                case 2 -> doctorMenu();
                case 3 -> appointmentMenu();
                case 4 -> billingMenu();
                case 5 -> medicineMenu();
                case 0 -> {
                    System.out.println("👋 Tạm biệt!");
                    System.exit(0);
                }
                default -> System.out.println("❌ Lựa chọn không hợp lệ!");
            }
        }
    }

    private static void showMainMenu() {
        System.out.println("""
                
                ===== MENU CHÍNH =====
                1. Quản lý Bệnh nhân
                2. Quản lý Bác sĩ
                3. Quản lý Lịch hẹn khám
                4. Quản lý Hóa đơn
                5. Quản lý Thuốc
                0. Thoát
                """);
    }

    // ==================== MODULE BỆNH NHÂN ====================
    private static void patientMenu() {
        System.out.println("""
                \n--- QUẢN LÝ BỆNH NHÂN ---
                1. Thêm bệnh nhân
                2. Xem danh sách
                3. Cập nhật
                4. Xóa
                0. Quay lại
                """);
        int choice = InputUtil.inputInt("Chọn: ");
        switch (choice) {
            case 1 -> {
                String id = UUID.randomUUID().toString();
                String name = InputUtil.inputString("Tên: ");
                int age = InputUtil.inputInt("Tuổi: ");
                String gender = InputUtil.inputString("Giới tính: ");
                String diagnosis = InputUtil.inputString("Chẩn đoán: ");
                patientService.addPatient(new Patient(id, name, age, gender, diagnosis));
            }
            case 2 -> patientService.viewAll();
            case 3 -> {
                String id = InputUtil.inputString("Nhập ID bệnh nhân cần cập nhật: ");
                Patient p = patientService.findById(id);
                if (p == null) {
                    System.out.println("❌ Không tìm thấy bệnh nhân.");
                } else {
                    String newName = InputUtil.inputString("Tên mới: ");
                    int newAge = InputUtil.inputInt("Tuổi mới: ");
                    String newGender = InputUtil.inputString("Giới tính mới: ");
                    String newDiagnosis = InputUtil.inputString("Chẩn đoán mới: ");
                    patientService.updatePatient(id, newName, newAge, newGender, newDiagnosis);
                }
            }
            case 4 -> {
                String id = InputUtil.inputString("Nhập ID bệnh nhân cần xóa: ");
                patientService.deletePatient(id);
            }
            case 0 -> {}
            default -> System.out.println("❌ Lựa chọn không hợp lệ!");
        }
    }

    // ==================== MODULE BÁC SĨ ====================
    private static void doctorMenu() {
        System.out.println("""
                \n--- QUẢN LÝ BÁC SĨ ---
                1. Thêm bác sĩ
                2. Xem danh sách
                3. Xóa bác sĩ
                0. Quay lại
                """);
        int choice = InputUtil.inputInt("Chọn: ");
        switch (choice) {
            case 1 -> {
                String id = UUID.randomUUID().toString();
                String name = InputUtil.inputString("Tên: ");
                String spec = InputUtil.inputString("Chuyên khoa: ");
                String phone = InputUtil.inputString("Số điện thoại: ");
                doctorService.addDoctor(new Doctor(id, name, spec, phone));
            }
            case 2 -> doctorService.viewAll();
            case 3 -> {
                String id = InputUtil.inputString("Nhập ID bác sĩ cần xóa: ");
                doctorService.deleteDoctor(id);
            }
            case 0 -> {}
            default -> System.out.println("❌ Lựa chọn không hợp lệ!");
        }
    }

    // ==================== MODULE KHÁM BỆNH ====================
    private static void appointmentMenu() {
        System.out.println("""
                \n--- QUẢN LÝ LỊCH HẸN ---
                1. Tạo lịch hẹn mới
                2. Xem tất cả lịch hẹn
                3. Hủy lịch hẹn
                0. Quay lại
                """);
        int choice = InputUtil.inputInt("Chọn: ");
        switch (choice) {
            case 1 -> {
                String id = UUID.randomUUID().toString();
                String patientId = InputUtil.inputString("ID bệnh nhân: ");
                String doctorId = InputUtil.inputString("ID bác sĩ: ");
                String date = InputUtil.inputString("Ngày hẹn (dd/MM/yyyy): ");
                appointmentService.addAppointment(new Appointment(id, patientId, doctorId, date));
            }
            case 2 -> appointmentService.viewAll();
            case 3 -> {
                String id = InputUtil.inputString("Nhập ID lịch hẹn cần hủy: ");
                appointmentService.deleteAppointment(id);
            }
            case 0 -> {}
            default -> System.out.println("❌ Lựa chọn không hợp lệ!");
        }
    }

    // ==================== MODULE HÓA ĐƠN ====================
    private static void billingMenu() {
        System.out.println("""
                \n--- QUẢN LÝ HÓA ĐƠN ---
                1. Tạo hóa đơn mới
                2. Xem danh sách hóa đơn
                0. Quay lại
                """);
        int choice = InputUtil.inputInt("Chọn: ");
        switch (choice) {
            case 1 -> {
                String id = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
                String examinationId = InputUtil.inputString("Mã khám: ");
                String patientId = InputUtil.inputString("Mã bệnh nhân: ");
                String date = InputUtil.inputString("Ngày lập (yyyy-MM-dd): ");
                double examinationFee = InputUtil.inputDouble("Phí khám: ");
                double medicineFee = InputUtil.inputDouble("Tiền thuốc: ");
                double discount = InputUtil.inputDouble("Giảm giá: ");
                double total = examinationFee + medicineFee - discount;// Tự động tính tổng tiền
                String paymentStatus = InputUtil.inputString("Trạng thái thanh toán (Đã/Chưa): ");
                billingService.addBill(new Bill(id, examinationId, patientId, date, examinationFee, medicineFee, discount, total, paymentStatus));
            }
            case 2 -> billingService.viewAll();
            case 0 -> {}
            default -> System.out.println("❌ Lựa chọn không hợp lệ!");
        }
    }

    // ==================== MODULE THUỐC ====================
    private static void medicineMenu() {
        System.out.println("""
                \n--- QUẢN LÝ THUỐC ---
                1. Thêm thuốc mới
                2. Xem danh sách thuốc
                3. Cập nhật số lượng
                0. Quay lại
                """);
        int choice = InputUtil.inputInt("Chọn: ");
        switch (choice) {
            case 1 -> {
                String id = UUID.randomUUID().toString();
                String name = InputUtil.inputString("Tên thuốc: ");
                int qty = InputUtil.inputInt("Số lượng: ");
                double price = InputUtil.inputDouble("Giá: ");
                medicineService.addMedicine(new Medicine(id, name, qty, price));
            }
            case 2 -> medicineService.viewAll();
            case 3 -> {
                String id = InputUtil.inputString("Nhập ID thuốc cần cập nhật: ");
                int newQty = InputUtil.inputInt("Số lượng mới: ");
                medicineService.updateQuantity(id, newQty);
            }
            case 0 -> {}
            default -> System.out.println("❌ Lựa chọn không hợp lệ!");
        }
    }
}
