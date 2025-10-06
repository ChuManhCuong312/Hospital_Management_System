import java.util.Scanner;
import java.util.UUID;
import model.*;
import service.*;
import util.InputUtil;

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
                1. Thêm bệnh nhân mới (đầy đủ thông tin)
                2. Thêm bệnh nhân đơn giản
                3. Xem danh sách chi tiết
                4. Xem danh sách dạng bảng
                5. Tìm kiếm bệnh nhân
                6. Cập nhật thông tin
                7. Xóa bệnh nhân
                0. Quay lại
                """);
        int choice = InputUtil.inputInt("Chọn: ");
        switch (choice) {
            case 1 -> addFullPatient();
            case 2 -> addSimplePatient();
            case 3 -> patientService.viewAll();
            case 4 -> patientService.displayPatientsTable();
            case 5 -> searchPatient();
            case 6 -> updatePatient();
            case 7 -> {
                String id = InputUtil.inputString("Nhập mã bệnh nhân cần xóa: ");
                patientService.deletePatient(id);
            }
            case 0 -> {}
            default -> System.out.println("❌ Lựa chọn không hợp lệ!");
        }
    }

    private static void addFullPatient() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║                    THÊM BỆNH NHÂN MỚI                         ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝\n");

        Patient patient = new Patient();

        patient.setPatientId(InputUtil.inputString("Mã bệnh nhân: "));
        patient.setFullName(InputUtil.inputString("Họ tên: "));
        patient.setDateOfBirth(InputUtil.inputDate("Ngày sinh (dd/MM/yyyy): "));
        patient.setGender(InputUtil.inputString("Giới tính: "));
        patient.setIdCard(InputUtil.inputString("CCCD: "));
        patient.setPhoneNumber(InputUtil.inputString("SĐT: "));
        patient.setAddress(InputUtil.inputString("Địa chỉ: "));
        patient.setBloodType(InputUtil.inputString("Nhóm máu: "));
        patient.setMedicalHistory(InputUtil.inputString("Tiền sử bệnh: "));
        patient.setStatus(InputUtil.inputString("Trạng thái: "));

        patientService.addPatient(patient);
    }

    private static void addSimplePatient() {
        String id = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        String name = InputUtil.inputString("Tên: ");
        int age = InputUtil.inputInt("Tuổi: ");
        String gender = InputUtil.inputString("Giới tính: ");
        String diagnosis = InputUtil.inputString("Chẩn đoán: ");
        patientService.addSimplePatient(new Patient(id, name, age, gender, diagnosis));
    }

    private static void searchPatient() {
        System.out.println("""
                \n--- TÌM KIẾM BỆNH NHÂN ---
                1. Tìm theo mã bệnh nhân
                2. Tìm theo tên
                3. Tìm theo số điện thoại
                4. Tìm theo CCCD
                0. Quay lại
                """);
        int choice = InputUtil.inputInt("Chọn phương thức tìm kiếm: ");

        switch (choice) {
            case 1 -> {
                String id = InputUtil.inputString("Nhập mã bệnh nhân: ");
                Patient patient = patientService.findById(id);
                if (patient != null) {
                    System.out.println(patient);
                } else {
                    System.out.println("\n✗ Không tìm thấy bệnh nhân với mã: " + id);
                }
            }
            case 2 -> {
                String name = InputUtil.inputString("Nhập tên bệnh nhân: ");
                var patientsByName = patientService.findPatientsByName(name);
                if (patientsByName.isEmpty()) {
                    System.out.println("\n✗ Không tìm thấy bệnh nhân nào với tên: " + name);
                } else {
                    System.out.println("\n✓ Tìm thấy " + patientsByName.size() + " bệnh nhân:");
                    for (Patient p : patientsByName) {
                        System.out.println(p);
                        System.out.println();
                    }
                }
            }
            case 3 -> {
                String phone = InputUtil.inputString("Nhập số điện thoại: ");
                Patient patientByPhone = patientService.findPatientByPhone(phone);
                if (patientByPhone != null) {
                    System.out.println(patientByPhone);
                } else {
                    System.out.println("\n✗ Không tìm thấy bệnh nhân với SĐT: " + phone);
                }
            }
            case 4 -> {
                String idCard = InputUtil.inputString("Nhập CCCD: ");
                Patient patientByIdCard = patientService.findPatientByIdCard(idCard);
                if (patientByIdCard != null) {
                    System.out.println(patientByIdCard);
                } else {
                    System.out.println("\n✗ Không tìm thấy bệnh nhân với CCCD: " + idCard);
                }
            }
            case 0 -> {}
            default -> System.out.println("\n✗ Lựa chọn không hợp lệ!");
        }
    }

    private static void updatePatient() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║                   SỬA THÔNG TIN BỆNH NHÂN                     ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝\n");

        String patientId = InputUtil.inputString("Nhập mã bệnh nhân cần sửa: ");
        Patient existingPatient = patientService.findById(patientId);

        if (existingPatient == null) {
            System.out.println("\n✗ Không tìm thấy bệnh nhân với mã: " + patientId + "\n");
            return;
        }

        System.out.println("\nThông tin hiện tại:");
        System.out.println(existingPatient);
        System.out.println("\nNhập thông tin mới (Enter để giữ nguyên):\n");

        Patient updatedPatient = new Patient();
        updatedPatient.setPatientId(patientId); // Giữ nguyên mã bệnh nhân

        String newName = InputUtil.inputString("Họ tên [" + existingPatient.getFullName() + "]: ");
        updatedPatient.setFullName(newName.isEmpty() ? existingPatient.getFullName() : newName);

        String dateStr = InputUtil.inputString("Ngày sinh (dd/MM/yyyy) [" + existingPatient.getDateOfBirth().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "]: ");
        if (dateStr.isEmpty()) {
            updatedPatient.setDateOfBirth(existingPatient.getDateOfBirth());
        } else {
            updatedPatient.setDateOfBirth(InputUtil.inputDate(dateStr));
        }

        String newGender = InputUtil.inputString("Giới tính [" + existingPatient.getGender() + "]: ");
        updatedPatient.setGender(newGender.isEmpty() ? existingPatient.getGender() : newGender);

        String newIdCard = InputUtil.inputString("CCCD [" + existingPatient.getIdCard() + "]: ");
        updatedPatient.setIdCard(newIdCard.isEmpty() ? existingPatient.getIdCard() : newIdCard);

        String newPhone = InputUtil.inputString("SĐT [" + existingPatient.getPhoneNumber() + "]: ");
        updatedPatient.setPhoneNumber(newPhone.isEmpty() ? existingPatient.getPhoneNumber() : newPhone);

        String newAddress = InputUtil.inputString("Địa chỉ [" + existingPatient.getAddress() + "]: ");
        updatedPatient.setAddress(newAddress.isEmpty() ? existingPatient.getAddress() : newAddress);

        String newBloodType = InputUtil.inputString("Nhóm máu [" + existingPatient.getBloodType() + "]: ");
        updatedPatient.setBloodType(newBloodType.isEmpty() ? existingPatient.getBloodType() : newBloodType);

        String newMedicalHistory = InputUtil.inputString("Tiền sử bệnh [" + existingPatient.getMedicalHistory() + "]: ");
        updatedPatient.setMedicalHistory(newMedicalHistory.isEmpty() ? existingPatient.getMedicalHistory() : newMedicalHistory);

        String newStatus = InputUtil.inputString("Trạng thái [" + existingPatient.getStatus() + "]: ");
        updatedPatient.setStatus(newStatus.isEmpty() ? existingPatient.getStatus() : newStatus);

        patientService.updatePatient(patientId, updatedPatient);
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
