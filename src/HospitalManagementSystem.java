import model.*;
import service.*;
import util.InputUtil;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.UUID;

public class HospitalManagementSystem {

    private static final PatientService patientService = new PatientService();
    private static final DoctorService doctorService = new DoctorService();
    private static final AppointmentService appointmentService = new AppointmentService();
    private static final BillingService billingService = new BillingService();
    private static final MedicineService medicineService = new MedicineService();
    private static final PrescriptionService prescriptionService = new PrescriptionService();

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
                case 6 -> prescriptionMenu();
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
                6. Quản lý Đơn thuốc
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
            case 0 -> {
            }
            default -> System.out.println("❌ Lựa chọn không hợp lệ!");
        }
    }

    // ==================== MODULE BÁC SĨ ====================
    private static void doctorMenu() {
        int choice;
        do {
            System.out.println("==== QUẢN LÝ BÁC SĨ ====");
            System.out.println("1. Thêm bác sĩ");
            System.out.println("2. Cập nhật bác sĩ");
            System.out.println("3. Xóa bác sĩ");
            System.out.println("4. Tìm kiếm bác sĩ");
            System.out.println("5. Lọc danh sách bác sĩ");
            System.out.println("6. Hiển thị tất cả");
            System.out.println("7. Export file");
            System.out.println("8. Import file");
            System.out.println("0. Thoát");
            System.out.print("Chọn: ");
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số hợp lệ!");
                continue;
            }

            switch (choice) {
                case 1 -> System.out.println(doctorService.addDoctorFromInput(sc));
                case 2 -> System.out.println(doctorService.updateDoctorFromInput(sc));
                case 3 -> System.out.println(doctorService.deleteDoctorFromInput(sc));
                case 4 -> searchMenu();
                case 5 -> filterMenu();
                case 6 -> doctorService.showAll();
                case 7 -> exportFile();
                case 8 -> importFile();
                case 0 -> {
                    System.out.println("Thoát chương trình.");
                    return;
                }
                default -> System.out.println("Chọn sai.");
            }
        } while (true);
    }

    // MENU CON: Tìm kiếm
    private static void searchMenu() {
        int choice;
        do {
            System.out.println("== TÌM KIẾM BÁC SĨ ==");
            System.out.println("1. Theo mã");
            System.out.println("2. Theo tên");
            System.out.println("3. Theo chuyên khoa");
            System.out.println("0. Quay lại");
            System.out.print("Chọn: ");
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số hợp lệ!");
                continue;
            }

            switch (choice) {
                case 1 -> doctorService.findByIdFromInput(sc);
                case 2 -> doctorService.findByNameFromInput(sc);
                case 3 -> doctorService.findBySpecialtyFromInput(sc);
                case 0 -> {
                    return;
                }
                default -> System.out.println("Chọn sai.");
            }
        } while (true);
    }

    // MENU CON: Lọc
    private static void filterMenu() {
        int choice;
        do {
            System.out.println("== LỌC DANH SÁCH BÁC SĨ ==");
            System.out.println("1. Theo chuyên khoa");
            System.out.println("2. Theo kinh nghiệm tối thiểu");
            System.out.println("3. Theo chuyên khoa + kinh nghiệm");
            System.out.println("0. Quay lại");
            System.out.print("Chọn: ");
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số hợp lệ!");
                continue;
            }

            switch (choice) {
                case 1 -> {
                    System.out.print("Nhập chuyên khoa: ");
                    String ck = sc.nextLine().trim();
                    doctorService.showFiltered(doctorService.filter(ck, null));
                }
                case 2 -> {
                    System.out.print("Nhập kinh nghiệm tối thiểu: ");
                    try {
                        int kn = Integer.parseInt(sc.nextLine());
                        doctorService.showFiltered(doctorService.filter(null, kn));
                    } catch (NumberFormatException e) {
                        System.out.println("Vui lòng nhập số nguyên!");
                    }
                }
                case 3 -> {
                    System.out.print("Nhập chuyên khoa: ");
                    String ck = sc.nextLine().trim();
                    System.out.print("Nhập kinh nghiệm tối thiểu: ");
                    try {
                        int kn = Integer.parseInt(sc.nextLine());
                        doctorService.showFiltered(doctorService.filter(ck, kn));
                    } catch (NumberFormatException e) {
                        System.out.println("Vui lòng nhập số nguyên!");
                    }
                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("Chọn sai.");
            }
        } while (true);
    }

    // Export / Import
    private static void exportFile() {
        System.out.print("Nhập tên file (.txt): ");
        String fileName = sc.nextLine().trim();
        if (!fileName.endsWith(".txt"))
            fileName += ".txt";
        if (doctorService.exportToFile(fileName))
            System.out.println("Export thành công vào " + fileName);
        else
            System.out.println("Lỗi export file.");
    }

    private static void importFile() {
        System.out.print("Nhập tên file cần import (.txt): ");
        String fileName = sc.nextLine().trim();
        if (!fileName.endsWith(".txt"))
            fileName += ".txt";
        System.out.println(doctorService.importFromFile(fileName));
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
            case 0 -> {
            }
            default -> System.out.println("❌ Lựa chọn không hợp lệ!");
        }
    }

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
            case 0 -> {
            }
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
            case 0 -> {
            }
            default -> System.out.println("❌ Lựa chọn không hợp lệ!");
        }
    }

    // ==================== MODULE ĐƠN THUỐC ====================
    // ==================== MODULE ĐƠN THUỐC ====================
    private static void prescriptionMenu() {
        System.out.println("""
                \n--- 💊 QUẢN LÝ ĐƠN THUỐC ---
                1. Thêm đơn thuốc
                2. Xem tất cả đơn thuốc
                3. Cập nhật đơn thuốc
                4. Xóa đơn thuốc
                5. Tìm kiếm đơn thuốc
                0. Quay lại
                """);

        int choice = InputUtil.inputInt("Chọn: ");
        switch (choice) {
            case 1 -> { // 🟢 Thêm
                String id = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
                String examId = InputUtil.inputString("Mã khám: ");
                String medicineName = InputUtil.inputString("Tên thuốc: ");
                LocalDate date = InputUtil.inputDate("Ngày kê (yyyy-MM-dd): ");
                String dosage = InputUtil.inputString("Liều dùng: ");
                String usage = InputUtil.inputString("Cách dùng: ");
                int quantity = InputUtil.inputInt("Số lượng: ");
                double price = InputUtil.inputDouble("Giá: ");
                String status = InputUtil.inputString("Trạng thái (Đang dùng / Ngừng / Hoàn thành): ");
                double total = quantity * price;

                Prescription p = new Prescription(id, examId, medicineName, dosage, usage, status, quantity, price,
                        total, date);

                prescriptionService.addPrescription(p);
            }

            case 2 -> prescriptionService.viewAll(); // 🟢 Xem tất cả

            case 3 -> { // 🟢 Cập nhật
                String id = InputUtil.inputString("Nhập ID đơn thuốc cần cập nhật: ");
                Prescription existing = prescriptionService.findById(id);
                if (existing == null) {
                    System.out.println("❌ Không tìm thấy đơn thuốc.");
                } else {
                    System.out.println("Thông tin hiện tại: " + existing);
                    String newMedicineName = InputUtil.inputString("Tên thuốc mới (Enter để bỏ qua): ");
                    String newDosage = InputUtil.inputString("Liều dùng mới (Enter để bỏ qua): ");
                    String newUsage = InputUtil.inputString("Cách dùng mới (Enter để bỏ qua): ");
                    String newStatus = InputUtil.inputString("Trạng thái mới (Enter để bỏ qua): ");
                    int newQuantity = InputUtil.inputInt("Số lượng mới (0 = giữ nguyên): ");
                    double newPrice = InputUtil.inputDouble("Giá mới (0 = giữ nguyên): ");
                    LocalDate newDate = InputUtil.inputDate("Ngày mới (Enter để bỏ qua): ");

                    prescriptionService.updatePrescription(id, newMedicineName, newDosage, newUsage, newStatus,
                            newQuantity, newPrice, newDate);
                }
            }

            case 4 -> { // 🟢 Xóa
                String id = InputUtil.inputString("Nhập ID đơn thuốc cần xóa: ");
                prescriptionService.deletePrescription(id);
            }

            case 5 -> { // 🟢 Tìm kiếm
                searchPrescriptionMenu();
            }

            case 0 -> {
            }

            default -> System.out.println("❌ Lựa chọn không hợp lệ!");
        }
    }

    // --- MENU CON: Tìm kiếm đơn thuốc ---
    private static void searchPrescriptionMenu() {
        System.out.println("""
                \n--- 🔍 TÌM KIẾM ĐƠN THUỐC ---
                1. Theo mã khám
                2. Theo tên thuốc
                3. Theo ngày kê
                0. Quay lại
                """);
        int choice = InputUtil.inputInt("Chọn: ");
        switch (choice) {
            case 1 -> {
                String examId = InputUtil.inputString("Nhập mã khám: ");
                var list = prescriptionService.findByExamId(examId);
                printPrescriptionSearchResult(list);
            }
            case 2 -> {
                String name = InputUtil.inputString("Nhập tên thuốc: ");
                var list = prescriptionService.findByMedicineName(name);
                printPrescriptionSearchResult(list);
            }
            case 3 -> {
                LocalDate date = InputUtil.inputDate("Nhập ngày (yyyy-MM-dd): ");
                var list = prescriptionService.findByDate(date);
                printPrescriptionSearchResult(list);
            }
            case 0 -> {
            }
            default -> System.out.println("❌ Lựa chọn không hợp lệ!");
        }
    }

    private static void printPrescriptionSearchResult(java.util.List<Prescription> list) {
        if (list.isEmpty()) {
            System.out.println("❌ Không tìm thấy kết quả nào!");
        } else {
            System.out.println("✅ Kết quả tìm thấy: ");
            for (Prescription p : list) {
                System.out.println(p);
            }
        }
    }

}
