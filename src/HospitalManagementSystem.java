import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import model.*;
import service.*;
import util.InputUtil;

public class HospitalManagementSystem {

    private static final PatientService patientService = new PatientService();
    private static final DoctorService doctorService = new DoctorService();
    private static final AppointmentService appointmentService = new AppointmentService();
    private static final BillingService billingService = new BillingService();
    private static final MedicineService medicineService = new MedicineService();
    private static final PrescriptionService prescriptionService = new PrescriptionService();

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== HỆ THỐNG QUẢN LÝ BỆNH VIỆN ===");
        while (true) {
            showMainMenu();
            int choice = InputUtil.inputInt("Chọn chức năng: ");
            switch (choice) {
                case 1:
                    patientMenu();
                    break;
                case 2:
                    doctorMenu();
                    break;
                case 3:
                    appointmentMenu();
                    break;
                case 4:
                    billingMenu();
                    break;
                case 5:
                    medicineMenu();
                    break;
                case 6:
                    prescriptionMenu();
                    break;
                case 0:
                    System.out.println("Tạm biệt!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private static void showMainMenu() {
        System.out.println("\n" +
                "                ===== MENU CHÍNH =====\n" +
                "                1. Quản lý Bệnh nhân\n" +
                "                2. Quản lý Bác sĩ\n" +
                "                3. Quản lý Lịch hẹn khám\n" +
                "                4. Quản lý Hóa đơn\n" +
                "                5. Quản lý Thuốc\n" +
                "                6. Quản lý Đơn thuốc\n" +
                "                0. Thoát\n" +
                "                ");
    }

    // ==================== MODULE BỆNH NHÂN ====================
    private static void patientMenu() {
        int choice;
        do {
            System.out.println("\n" +
                    "=== QUẢN LÝ BỆNH NHÂN ===\n" +
                    "1. Thêm bệnh nhân mới\n" +
                    "2. Xem danh sách bệnh nhân\n" +
                    "3. Tìm kiếm bệnh nhân\n" +
                    "4. Cập nhật thông tin bệnh nhân\n" +
                    "5. Xóa bệnh nhân\n" +
                    "6. Lọc bệnh nhân\n" +
                    "7. Thống kê bệnh nhân\n" +
                    "8. Xuất dữ liệu ra file\n" +
                    "9. Nhập dữ liệu từ file\n" +
                    "0. Quay lại menu chính\n");
            
            choice = InputUtil.inputInt("Chọn chức năng: ");
            
            switch (choice) {
                case 1:
                    System.out.println(patientService.addPatientFromInput(sc));
                    break;
                case 2:
                    patientService.viewAll();
                    break;
                case 3:
                    searchPatientMenu();
                    break;
                case 4:
                    System.out.println(patientService.updatePatientFromInput(sc));
                    break;
                case 5:
                    System.out.println(patientService.deletePatientFromInput(sc));
                    break;
                case 6:
                    filterPatientMenu();
                    break;
                case 7:
                    patientService.showStatistics();
                    break;
                case 8:
                    exportPatientFile();
                    break;
                case 9:
                    importPatientFile();
                    break;
                case 0:
                    System.out.println("Quay lại menu chính...");
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        } while (true);
    }

    // ==================== MENU CON: TÌM KIẾM BỆNH NHÂN ====================
    private static void searchPatientMenu() {
        int choice;
        do {
            System.out.println("\n" +
                    "=== TÌM KIẾM BỆNH NHÂN ===\n" +
                    "1. Tìm theo ID\n" +
                    "2. Tìm theo tên\n" +
                    "3. Tìm theo giới tính\n" +
                    "4. Tìm theo chẩn đoán\n" +
                    "0. Quay lại\n");
            
            choice = InputUtil.inputInt("Chọn loại tìm kiếm: ");
            
            switch (choice) {
                case 1:
                    String id = InputUtil.inputString("Nhập ID bệnh nhân: ");
                    Patient patient = patientService.findById(id);
                    if (patient != null) {
                        System.out.println("\n=== THÔNG TIN BỆNH NHÂN ===");
                        System.out.println("ID: " + patient.getId());
                        System.out.println("Họ tên: " + patient.getName());
                        System.out.println("Tuổi: " + patient.getAge());
                        System.out.println("Giới tính: " + patient.getGender());
                        System.out.println("Chẩn đoán: " + patient.getDiagnosis());
                    } else {
                        System.out.println("Không tìm thấy bệnh nhân có ID: " + id);
                    }
                    break;
                case 2:
                    String name = InputUtil.inputString("Nhập tên bệnh nhân: ");
                    List<Patient> patients = patientService.findByName(name);
                    patientService.showSearchResults(patients, "Tìm theo tên: " + name);
                    break;
                case 3:
                    String gender = InputUtil.inputString("Nhập giới tính (Nam/Nữ): ");
                    List<Patient> patientsByGender = patientService.findByGender(gender);
                    patientService.showSearchResults(patientsByGender, "Tìm theo giới tính: " + gender);
                    break;
                case 4:
                    String diagnosis = InputUtil.inputString("Nhập chẩn đoán: ");
                    List<Patient> patientsByDiagnosis = patientService.findByDiagnosis(diagnosis);
                    patientService.showSearchResults(patientsByDiagnosis, "Tìm theo chẩn đoán: " + diagnosis);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        } while (true);
    }

    // ==================== MENU CON: LỌC BỆNH NHÂN ====================
    private static void filterPatientMenu() {
        int choice;
        do {
            System.out.println("\n" +
                    "=== LỌC BỆNH NHÂN ===\n" +
                    "1. Lọc theo độ tuổi\n" +
                    "2. Lọc theo giới tính và độ tuổi\n" +
                    "0. Quay lại\n");
            
            choice = InputUtil.inputInt("Chọn loại lọc: ");
            
            switch (choice) {
                case 1:
                    int minAge = InputUtil.inputInt("Tuổi tối thiểu: ");
                    int maxAge = InputUtil.inputInt("Tuổi tối đa: ");
                    List<Patient> patients = patientService.filterByAge(minAge, maxAge);
                    patientService.showSearchResults(patients, "Lọc theo độ tuổi " + minAge + "-" + maxAge);
                    break;
                case 2:
                    String gender = InputUtil.inputString("Giới tính (Nam/Nữ): ");
                    int minAge2 = InputUtil.inputInt("Tuổi tối thiểu: ");
                    int maxAge2 = InputUtil.inputInt("Tuổi tối đa: ");
                    
                    List<Patient> patients2 = patientService.findByGender(gender);
                    patients2 = patients2.stream()
                            .filter(p -> p.getAge() >= minAge2 && p.getAge() <= maxAge2)
                            .collect(Collectors.toList());
                    
                    patientService.showSearchResults(patients2, "Lọc theo giới tính " + gender + " và độ tuổi " + minAge2 + "-" + maxAge2);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        } while (true);
    }

    // ==================== XUẤT/NHẬP FILE BỆNH NHÂN ====================
    private static void exportPatientFile() {
        System.out.print("Nhập tên file xuất (.txt): ");
        String fileName = sc.nextLine().trim();
        if (!fileName.endsWith(".txt")) {
            fileName += ".txt";
        }
        
        if (patientService.exportToFile(fileName)) {
            System.out.println("Xuất dữ liệu thành công vào file: " + fileName);
        } else {
            System.out.println("Lỗi khi xuất dữ liệu!");
        }
    }

    private static void importPatientFile() {
        System.out.print("Nhập tên file cần nhập (.txt): ");
        String fileName = sc.nextLine().trim();
        if (!fileName.endsWith(".txt")) {
            fileName += ".txt";
        }
        
        System.out.println(patientService.importFromFile(fileName));
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
                case 1:
                    System.out.println(doctorService.addDoctorFromInput(sc));
                    break;
                case 2:
                    System.out.println(doctorService.updateDoctorFromInput(sc));
                    break;
                case 3:
                    System.out.println(doctorService.deleteDoctorFromInput(sc));
                    break;
                case 4:
                    searchMenu();
                    break;
                case 5:
                    filterMenu();
                    break;
                case 6:
                    doctorService.showAll();
                    break;
                case 7:
                    exportFile();
                    break;
                case 8:
                    importFile();
                    break;
                case 0:
                    System.out.println("Thoát chương trình.");
                    return;
                default:
                    System.out.println("Chọn sai.");
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
                case 1:
                    doctorService.findByIdFromInput(sc);
                    break;
                case 2:
                    doctorService.findByNameFromInput(sc);
                    break;
                case 3:
                    doctorService.findBySpecialtyFromInput(sc);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Chọn sai.");
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
                case 1:
                    System.out.print("Nhập chuyên khoa: ");
                    String ck = sc.nextLine().trim();
                    doctorService.showFiltered(doctorService.filter(ck, null));
                    break;
                case 2:
                    System.out.print("Nhập kinh nghiệm tối thiểu: ");
                    try {
                        int kn = Integer.parseInt(sc.nextLine());
                        doctorService.showFiltered(doctorService.filter(null, kn));
                    } catch (NumberFormatException e) {
                        System.out.println("Vui lòng nhập số nguyên!");
                    }
                    break;
                case 3:
                    System.out.print("Nhập chuyên khoa: ");
                    String ck2 = sc.nextLine().trim();
                    System.out.print("Nhập kinh nghiệm tối thiểu: ");
                    try {
                        int kn2 = Integer.parseInt(sc.nextLine());
                        doctorService.showFiltered(doctorService.filter(ck2, kn2));
                    } catch (NumberFormatException e) {
                        System.out.println("Vui lòng nhập số nguyên!");
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Chọn sai.");
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
        System.out.println("\n" +
                "--- QUẢN LÝ LỊCH HẸN ---\n" +
                "1. Tạo lịch hẹn mới\n" +
                "2. Xem tất cả lịch hẹn\n" +
                "3. Hủy lịch hẹn\n" +
                "0. Quay lại\n");
        int choice = InputUtil.inputInt("Chọn: ");
        switch (choice) {
            case 1:
                String id = UUID.randomUUID().toString();
                String patientId = InputUtil.inputString("ID bệnh nhân: ");
                String doctorId = InputUtil.inputString("ID bác sĩ: ");
                String date = InputUtil.inputString("Ngày hẹn (dd/MM/yyyy): ");
                appointmentService.addAppointment(new Appointment(id, patientId, doctorId, date));
                break;
            case 2:
                appointmentService.viewAll();
                break;
            case 3:
                String id2 = InputUtil.inputString("Nhập ID lịch hẹn cần hủy: ");
                appointmentService.deleteAppointment(id2);
                break;
            case 0:
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ!");
        }
    }

    // ==================== MODULE HÓA ĐƠN ====================
    private static void billingMenu() {
        System.out.println("\n" +
                "--- QUẢN LÝ HÓA ĐƠN ---\n" +
                "1. Tạo hóa đơn mới\n" +
                "2. Xem danh sách hóa đơn\n" +
                "3. Cập nhật hóa đơn\n" +
                "4. Xóa hóa đơn\n" +
                "5. Tìm kiếm hóa đơn\n" +
                "6. Lọc dữ liệu (theo tiền hoặc ngày)\n" +
                "7. Xuất dữ liệu ra file\n" +
                "0. Quay lại\n");

        int choice = InputUtil.inputInt("Chọn: ");

        switch (choice) {
            case 1:
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
                    System.out.println("Dữ liệu hóa đơn không hợp lệ. Không thêm vào hệ thống!");
                }
                break;
            case 2:
                billingService.viewAll();
                break;
            case 3:
                billingService.updateBill(InputUtil.inputString("Nhập ID hóa đơn cần cập nhật: "));
                break;
            case 4:
                billingService.deleteBill(InputUtil.inputString("Nhập ID hóa đơn cần xóa: "));
                break;
            case 5:
                billingService.search();
                break;
            case 6:
                billingService.filterBills();
                break;
            case 7:
                billingService.exportFile("data/export_billing.txt");
                break;
            case 0:
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ!");
        }
    }

    // ==================== MODULE THUỐC ====================
    private static void medicineMenu() {
        System.out.println("\n" +
                "--- QUẢN LÝ THUỐC ---\n" +
                "1. Thêm thuốc mới\n" +
                "2. Xem danh sách thuốc\n" +
                "3. Cập nhật số lượng\n" +
                "0. Quay lại\n");
        int choice = InputUtil.inputInt("Chọn: ");
        switch (choice) {
            case 1:
                String id = UUID.randomUUID().toString();
                String name = InputUtil.inputString("Tên thuốc: ");
                int qty = InputUtil.inputInt("Số lượng: ");
                double price = InputUtil.inputDouble("Giá: ");
                medicineService.addMedicine(new Medicine(id, name, qty, price));
                break;
            case 2:
                medicineService.viewAll();
                break;
            case 3:
                String id2 = InputUtil.inputString("Nhập ID thuốc cần cập nhật: ");
                int newQty = InputUtil.inputInt("Số lượng mới: ");
                medicineService.updateQuantity(id2, newQty);
                break;
            case 0:
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ!");
        }
    }

    // ==================== MODULE ĐƠN THUỐC ====================
    private static void prescriptionMenu() {
        System.out.println("\n" +
                "--- QUẢN LÝ ĐƠN THUỐC ---\n" +
                "1. Thêm đơn thuốc\n" +
                "2. Xem tất cả đơn thuốc\n" +
                "3. Cập nhật đơn thuốc\n" +
                "4. Xóa đơn thuốc\n" +
                "5. Tìm kiếm đơn thuốc\n" +
                "0. Quay lại\n");

        int choice = InputUtil.inputInt("Chọn: ");
        switch (choice) {
            case 1:
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
                break;

            case 2:
                prescriptionService.viewAll();
                break;

            case 3:
                String id2 = InputUtil.inputString("Nhập ID đơn thuốc cần cập nhật: ");
                Prescription existing = prescriptionService.findById(id2);
                if (existing == null) {
                    System.out.println("Không tìm thấy đơn thuốc.");
                } else {
                    System.out.println("Thông tin hiện tại: " + existing);
                    String newMedicineName = InputUtil.inputString("Tên thuốc mới (Enter để bỏ qua): ");
                    String newDosage = InputUtil.inputString("Liều dùng mới (Enter để bỏ qua): ");
                    String newUsage = InputUtil.inputString("Cách dùng mới (Enter để bỏ qua): ");
                    String newStatus = InputUtil.inputString("Trạng thái mới (Enter để bỏ qua): ");
                    int newQuantity = InputUtil.inputInt("Số lượng mới (0 = giữ nguyên): ");
                    double newPrice = InputUtil.inputDouble("Giá mới (0 = giữ nguyên): ");
                    LocalDate newDate = InputUtil.inputDate("Ngày mới (Enter để bỏ qua): ");

                    prescriptionService.updatePrescription(id2, newMedicineName, newDosage, newUsage, newStatus,
                            newQuantity, newPrice, newDate);
                }
                break;

            case 4:
                String id3 = InputUtil.inputString("Nhập ID đơn thuốc cần xóa: ");
                prescriptionService.deletePrescription(id3);
                break;

            case 5:
                searchPrescriptionMenu();
                break;

            case 0:
                break;

            default:
                System.out.println("Lựa chọn không hợp lệ!");
        }
    }

    // --- MENU CON: Tìm kiếm đơn thuốc ---
    private static void searchPrescriptionMenu() {
        System.out.println("\n" +
                "--- TÌM KIẾM ĐƠN THUỐC ---\n" +
                "1. Theo mã khám\n" +
                "2. Theo tên thuốc\n" +
                "3. Theo ngày kê\n" +
                "0. Quay lại\n");
        int choice = InputUtil.inputInt("Chọn: ");
        switch (choice) {
            case 1:
                String examId = InputUtil.inputString("Nhập mã khám: ");
                var list = prescriptionService.findByExamId(examId);
                printPrescriptionSearchResult(list);
                break;
            case 2:
                String name = InputUtil.inputString("Nhập tên thuốc: ");
                var list2 = prescriptionService.findByMedicineName(name);
                printPrescriptionSearchResult(list2);
                break;
            case 3:
                LocalDate date = InputUtil.inputDate("Nhập ngày (yyyy-MM-dd): ");
                var list3 = prescriptionService.findByDate(date);
                printPrescriptionSearchResult(list3);
                break;
            case 0:
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ!");
        }
    }

    private static void printPrescriptionSearchResult(java.util.List<Prescription> list) {
        if (list.isEmpty()) {
            System.out.println("Không tìm thấy kết quả nào!");
        } else {
            System.out.println("Kết quả tìm thấy: ");
            for (Prescription p : list) {
                System.out.println(p);
            }
        }
    }

}