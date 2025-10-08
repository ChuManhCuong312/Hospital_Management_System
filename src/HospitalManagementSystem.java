import model.*;
import service.*;
import util.*;
import exception.InvalidDataException;

public class HospitalManagementSystem {
    private static final PatientService patientService = new PatientService();
    private static final DoctorService doctorService = new DoctorService();
    private static final AppointmentService appointmentService = new AppointmentService();
    private static final PrescriptionService prescriptionService = new PrescriptionService();
    private static final BillingService billingService = new BillingService();

    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("\n========= HỆ THỐNG QUẢN LÝ BỆNH VIỆN =========");
            System.out.println("1. Quản lý bệnh nhân");
            System.out.println("2. Quản lý bác sĩ");
            System.out.println("3. Quản lý lịch khám");
            System.out.println("4. Quản lý đơn thuốc");
            System.out.println("5. Quản lý hóa đơn");
            System.out.println("0. Thoát");
            choice = InputUtil.nhapLuaChon("→ Nhập lựa chọn: ", 0, 5);

            switch (choice) {
                case 1 -> menuPatient();
                case 2 -> menuDoctor();
                case 3 -> menuAppointment();
                case 4 -> menuPrescription();
                case 5 -> menuBilling();
                case 0 -> System.out.println("Cảm ơn bạn đã sử dụng hệ thống!");
            }
        } while (choice != 0);
    }

    // ================== MENU BỆNH NHÂN ======================
    private static void menuPatient() {
        int choice;
        do {
            System.out.println("\n------ QUẢN LÝ BỆNH NHÂN ------");
            System.out.println("1. Thêm bệnh nhân");
            System.out.println("2. Xem danh sách bệnh nhân");
            System.out.println("3. Cập nhật thông tin bệnh nhân");
            System.out.println("4. Xóa bệnh nhân");
            System.out.println("5. Tìm kiếm bệnh nhân (Tên / Mã / SĐT)");
            System.out.println("0. Quay lại");
            choice = InputUtil.nhapLuaChon("→ Nhập lựa chọn: ", 0, 5);

            switch (choice) {
                case 1 -> addPatient();
                case 2 -> patientService.viewAll();
                case 3 -> updatePatient();
                case 4 -> deletePatient();
                case 5 -> patientService.searchPatient();
            }
        } while (choice != 0);
    }

    private static void addPatient() {
        try {
            System.out.println("\nNhập thông tin bệnh nhân:");
            String id = InputUtil.nhapChuoi("ID: ");
            String name = InputUtil.nhapChuoi("Họ tên: ");
            int age = InputUtil.nhapSoNguyen("Tuổi: ");
            String gender = InputUtil.nhapChuoi("Giới tính (Nam/Nữ): ");
            String address = InputUtil.nhapChuoi("Địa chỉ: ");
            String phone = InputUtil.nhapChuoi("Số điện thoại: ");
            String diagnosis = InputUtil.nhapChuoi("Chẩn đoán: ");
            String bloodGroup = InputUtil.nhapChuoi("Nhóm máu: ");
            String doctorId = InputUtil.nhapChuoi("Mã bác sĩ phụ trách: ");
            String status = "Đang điều trị";

            Validator.checkNotEmpty(name, "Tên không được để trống!");
            Validator.checkGender(gender);

            Patient p = new Patient(id, name, age, gender, address, phone, diagnosis, bloodGroup, doctorId, status);
            patientService.addPatient(p);
        } catch (InvalidDataException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    private static void updatePatient() {
        String id = InputUtil.nhapChuoi("Nhập ID bệnh nhân cần cập nhật: ");
        Patient p = patientService.findById(id);
        if (p == null) {
            System.out.println("Không tìm thấy bệnh nhân!");
            return;
        }
        try {
            String name = InputUtil.nhapChuoi("Họ tên (" + p.getName() + "): ");
            int age = InputUtil.nhapSoNguyen("Tuổi (" + p.getAge() + "): ");
            String gender = InputUtil.nhapChuoi("Giới tính (" + p.getGender() + "): ");
            String diagnosis = InputUtil.nhapChuoi("Chẩn đoán (" + p.getDiagnosis() + "): ");

            if (!gender.isEmpty()) Validator.checkGender(gender);

            patientService.updatePatient(id,
                    name.isEmpty() ? p.getName() : name,
                    age <= 0 ? p.getAge() : age,
                    gender.isEmpty() ? p.getGender() : gender,
                    diagnosis.isEmpty() ? p.getDiagnosis() : diagnosis
            );
        } catch (InvalidDataException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    private static void deletePatient() {
        String id = InputUtil.nhapChuoi("Nhập ID bệnh nhân cần xóa: ");
        patientService.deletePatient(id);
    }

    // ================== MENU BÁC SĨ ======================
    private static void menuDoctor() {
        int choice;
        do {
            System.out.println("\n------ QUẢN LÝ BÁC SĨ ------");
            System.out.println("1. Thêm bác sĩ");
            System.out.println("2. Xem danh sách bác sĩ");
            System.out.println("3. Cập nhật thông tin bác sĩ");
            System.out.println("4. Xóa bác sĩ");
            System.out.println("5. Tìm kiếm bác sĩ (Tên / Mã / Chuyên khoa)");
            System.out.println("0. Quay lại");
            choice = InputUtil.nhapLuaChon("→ Nhập lựa chọn: ", 0, 5);

            switch (choice) {
                case 1 -> doctorService.addDoctor();
                case 2 -> doctorService.viewAll();
                case 3 -> doctorService.updateDoctor();
                case 4 -> doctorService.deleteDoctor();
                case 5 -> doctorService.searchDoctor();
            }
        } while (choice != 0);
    }

    // ================== MENU LỊCH KHÁM ======================
    private static void menuAppointment() {
        int choice;
        do {
            System.out.println("\n------ QUẢN LÝ LỊCH KHÁM ------");
            System.out.println("1. Thêm lịch khám");
            System.out.println("2. Xem danh sách lịch khám");
            System.out.println("3. Cập nhật lịch khám");
            System.out.println("4. Xóa lịch khám");
            System.out.println("5. Tìm kiếm phiếu khám (Bệnh nhân / Bác sĩ / Ngày)");
            System.out.println("0. Quay lại");
            choice = InputUtil.nhapLuaChon("→ Nhập lựa chọn: ", 0, 5);

            switch (choice) {
                case 1 -> appointmentService.addAppointment();
                case 2 -> appointmentService.viewAll();
                case 3 -> appointmentService.updateAppointment();
                case 4 -> appointmentService.deleteAppointment();
                case 5 -> appointmentService.searchAppointment();
            }
        } while (choice != 0);
    }

    // ================== MENU ĐƠN THUỐC ======================
    private static void menuPrescription() {
        int choice;
        do {
            System.out.println("\n------ QUẢN LÝ ĐƠN THUỐC ------");
            System.out.println("1. Thêm đơn thuốc");
            System.out.println("2. Cập nhật đơn thuốc");
            System.out.println("3. Xem danh sách đơn thuốc");
            System.out.println("4. Xóa đơn thuốc");
            System.out.println("5. Tìm kiếm đơn thuốc (Bệnh nhân / Bác sĩ / Ngày)");
            System.out.println("6. Lọc đơn thuốc (Theo ngày hoặc bác sĩ)");
            System.out.println("0. Quay lại");
            choice = InputUtil.nhapLuaChon("→ Nhập lựa chọn: ", 0, 6);

            switch (choice) {
                case 1 -> prescriptionService.addPrescription();
                case 2 -> prescriptionService.updatePrescription();
                case 3 -> prescriptionService.viewAll();
                case 4 -> prescriptionService.deletePrescription();
                case 5 -> prescriptionService.searchMedicineByPrescriptionCriteria();
                case 6 -> prescriptionService.filterPrescription();
            }
        } while (choice != 0);
    }
    // ================== MENU HÓA ĐƠN ======================
    private static void menuBilling() {
        int choice;
        do {
            System.out.println("\n------ QUẢN LÝ HÓA ĐƠN ------");
            System.out.println("1. Tạo hóa đơn mới");
            System.out.println("2. Xem danh sách hóa đơn");
            System.out.println("3. Cập nhật hóa đơn (chỉnh sửa)");
            System.out.println("4. Xóa hóa đơn");
            System.out.println("5. Tìm kiếm hóa đơn (Bệnh nhân / Ngày / Tổng tiền)");
            System.out.println("6. Lọc hóa đơn (Theo ngày hoặc khoảng tiền)");
            System.out.println("7. Xuất hóa đơn ra file .txt");
            System.out.println("0. Quay lại");
            choice = InputUtil.nhapLuaChon("→ Nhập lựa chọn: ", 0, 7);

            switch (choice) {
                case 1 -> billingService.addBill();
                case 2 -> billingService.viewAll();
                case 3 -> billingService.updateBill();
                case 4 -> billingService.deleteBill();
                case 5 -> billingService.searchBill();
                case 6 -> billingService.filterBill();
                case 7 -> billingService.exportBill();
            }
        } while (choice != 0);
    }
}
