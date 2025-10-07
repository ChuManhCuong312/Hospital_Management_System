package service;

import exception.InvalidDataException;
import model.Patient;
import util.FileUtil;
import util.InputUtil;
import util.Validator;

import java.util.ArrayList;
import java.util.List;

public class PatientService {
    private static final String FILE_PATH = "data/patients.txt";

    // Đọc toàn bộ danh sách bệnh nhân từ file
    public List<Patient> getAllPatients() {
        List<String> lines = FileUtil.readFile(FILE_PATH);
        List<Patient> patients = new ArrayList<>();
        for (String line : lines) {
            Patient p = Patient.fromString(line);
            if (p != null) patients.add(p);
        }
        return patients;
    }

    // Ghi toàn bộ danh sách xuống file
    private void saveAll(List<Patient> patients) {
        List<String> lines = new ArrayList<>();
        for (Patient p : patients) {
            lines.add(p.toDataString());
        }
        FileUtil.writeFile(FILE_PATH, lines);
    }

    // =================== THÊM BỆNH NHÂN ===================
    public void addPatient() {
        try {
            System.out.println("===== THÊM BỆNH NHÂN MỚI =====");
            String id = InputUtil.nhapChuoi("Nhập ID bệnh nhân: ");
            Validator.checkNotEmpty(id, "ID không được để trống!");
            if (findById(id) != null)
                throw new InvalidDataException("Bệnh nhân có ID này đã tồn tại!");

            String name = InputUtil.nhapChuoi("Nhập họ tên: ");
            Validator.checkNotEmpty(name, "Tên không được để trống!");

            int age = InputUtil.nhapSoNguyen("Nhập tuổi: ");
            Validator.checkPositive(age, "Tuổi phải lớn hơn 0!");

            String gender = InputUtil.nhapChuoi("Nhập giới tính (Nam/Nữ): ");
            Validator.checkGender(gender);

            String address = InputUtil.nhapChuoi("Nhập địa chỉ: ");
            Validator.checkNotEmpty(address, "Địa chỉ không được để trống!");

            String phone = InputUtil.nhapChuoi("Nhập số điện thoại (10 số): ");
            Validator.checkPhone(phone);

            String diagnosis = InputUtil.nhapChuoi("Nhập chẩn đoán: ");
            Validator.checkNotEmpty(diagnosis, "Chẩn đoán không được để trống!");

            String bloodGroup = InputUtil.nhapChuoi("Nhập nhóm máu (A/B/AB/O): ");
            Validator.checkBloodGroup(bloodGroup);

            String doctorId = InputUtil.nhapChuoi("Nhập mã bác sĩ phụ trách (Enter nếu chưa có): ");

            String status = InputUtil.nhapChuoi("Nhập trạng thái (Đang điều trị/Khỏi/...): ");
            Validator.checkNotEmpty(status, "Trạng thái không được để trống!");

            Patient p = new Patient(id, name, age, gender, address, phone, diagnosis, bloodGroup, doctorId, status);
            FileUtil.appendToFile(FILE_PATH, p.toDataString());
            System.out.println("Đã thêm bệnh nhân: " + name);

        } catch (InvalidDataException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    // Overloaded addPatient to accept a Patient object (used by main)
    public void addPatient(Patient p) {
        try {
            // Basic validations
            Validator.checkNotEmpty(p.getId(), "ID không được để trống!");
            if (findById(p.getId()) != null) throw new InvalidDataException("Bệnh nhân có ID này đã tồn tại!");
            Validator.checkNotEmpty(p.getName(), "Tên không được để trống!");
            Validator.checkPositive(p.getAge(), "Tuổi phải lớn hơn 0!");
            Validator.checkGender(p.getGender());
            Validator.checkNotEmpty(p.getDiagnosis(), "Chẩn đoán không được để trống!");

            FileUtil.appendToFile(FILE_PATH, p.toDataString());
            System.out.println("Đã thêm bệnh nhân: " + p.getName());
        } catch (InvalidDataException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    // =================== HIỂN THỊ DANH SÁCH ===================
    public void viewAll() {
        List<Patient> patients = getAllPatients();
        if (patients.isEmpty()) {
            System.out.println("Không có bệnh nhân nào trong hệ thống!");
            return;
        }

        System.out.println("========================================================================================================================================================");
        System.out.printf("| %-8s | %-20s | %-4s | %-6s | %-20s | %-12s | %-20s | %-6s | %-10s | %-15s |%n",
                "ID", "Họ tên", "Tuổi", "Giới", "Địa chỉ", "SĐT", "Chẩn đoán", "Máu", "Bác sĩ", "Trạng thái");
        System.out.println("========================================================================================================================================================");

        for (Patient p : patients) {
            System.out.printf("| %-8s | %-20s | %-4d | %-6s | %-20s | %-12s | %-20s | %-6s | %-10s | %-15s |%n",
                    p.getId(), p.getName(), p.getAge(), p.getGender(),
                    p.getAddress(), p.getPhone(), p.getDiagnosis(),
                    p.getBloodGroup(), p.getDoctorId(), p.getStatus());
        }

        System.out.println("========================================================================================================================================================");
    }

    public void updatePatient() {
        try {
            System.out.println("===== CẬP NHẬT THÔNG TIN BỆNH NHÂN =====");
            String id = InputUtil.nhapChuoi("Nhập ID bệnh nhân cần cập nhật: ");
            List<Patient> list = getAllPatients();
            boolean found = false;

            for (Patient p : list) {
                if (p.getId().equalsIgnoreCase(id)) {

                    String name = InputUtil.nhapChuoi("Nhập họ tên mới (Enter để bỏ qua): ");
                    if (!name.isEmpty()) Validator.checkNotEmpty(name, "Tên không được để trống!");
                    if (!name.isEmpty()) p.setName(name);

                    int age = InputUtil.nhapSoNguyen("Nhập tuổi mới (nhập <=0 để bỏ qua): ");
                    if (age > 0) Validator.checkPositive(age, "Tuổi phải lớn hơn 0!");
                    if (age > 0) p.setAge(age);

                    String gender = InputUtil.nhapChuoi("Nhập giới tính mới (Nam/Nữ) (Enter để bỏ qua): ");
                    if (!gender.isEmpty()) Validator.checkGender(gender);
                    if (!gender.isEmpty()) p.setGender(gender);

                    String address = InputUtil.nhapChuoi("Nhập địa chỉ mới (Enter để bỏ qua): ");
                    if (!address.isEmpty()) Validator.checkNotEmpty(address, "Địa chỉ không được để trống!");
                    if (!address.isEmpty()) p.setAddress(address);

                    String phone = InputUtil.nhapChuoi("Nhập số điện thoại mới (Enter để bỏ qua): ");
                    if (!phone.isEmpty()) Validator.checkPhone(phone);
                    if (!phone.isEmpty()) p.setPhone(phone);

                    String diagnosis = InputUtil.nhapChuoi("Nhập chẩn đoán mới (Enter để bỏ qua): ");
                    if (!diagnosis.isEmpty()) Validator.checkNotEmpty(diagnosis, "Chẩn đoán không được để trống!");
                    if (!diagnosis.isEmpty()) p.setDiagnosis(diagnosis);

                    String bloodGroup = InputUtil.nhapChuoi("Nhập nhóm máu mới (A/B/AB/O) (Enter để bỏ qua): ");
                    if (!bloodGroup.isEmpty()) Validator.checkBloodGroup(bloodGroup);
                    if (!bloodGroup.isEmpty()) p.setBloodGroup(bloodGroup);

                    String doctorId = InputUtil.nhapChuoi("Nhập mã bác sĩ mới (Enter để bỏ qua): ");
                    if (!doctorId.isEmpty()) p.setDoctorId(doctorId);

                    String status = InputUtil.nhapChuoi("Nhập trạng thái mới (Enter để bỏ qua): ");
                    if (!status.isEmpty()) Validator.checkNotEmpty(status, "Trạng thái không được để trống!");
                    if (!status.isEmpty()) p.setStatus(status);

                    found = true;
                    break;
                }
            }
            if (found) {
                saveAll(list);
                System.out.println("Đã cập nhật thông tin bệnh nhân có ID: " + id);
            } else {
                System.out.println("Không tìm thấy bệnh nhân có ID: " + id);
            }

        } catch (InvalidDataException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    // Overloaded updatePatient used by main
    public void updatePatient(String id, String name, int age, String gender, String diagnosis) throws InvalidDataException {
        List<Patient> list = getAllPatients();
        boolean found = false;
        for (Patient p : list) {
            if (p.getId().equalsIgnoreCase(id)) {
                Validator.checkNotEmpty(name, "Tên không được để trống!");
                Validator.checkPositive(age, "Tuổi phải lớn hơn 0!");
                Validator.checkGender(gender);
                Validator.checkNotEmpty(diagnosis, "Chẩn đoán không được để trống!");

                p.setName(name);
                p.setAge(age);
                p.setGender(gender);
                p.setDiagnosis(diagnosis);
                found = true;
                break;
            }
        }
        if (found) {
            saveAll(list);
            System.out.println("Đã cập nhật thông tin bệnh nhân có ID: " + id);
        } else {
            throw new InvalidDataException("Không tìm thấy bệnh nhân có ID: " + id);
        }
    }

    // =================== XÓA ===================
    public void deletePatient() {
        System.out.println("===== XÓA BỆNH NHÂN =====");
        String id = InputUtil.nhapChuoi("Nhập ID bệnh nhân cần xóa: ");
        List<Patient> list = getAllPatients();
        boolean removed = list.removeIf(p -> p.getId().equalsIgnoreCase(id));

        if (removed) {
            saveAll(list);
            System.out.println("Đã xóa bệnh nhân có ID: " + id);
        } else {
            System.out.println("Không tìm thấy bệnh nhân có ID: " + id);
        }
    }

    // Overloaded deletePatient used by main
    public void deletePatient(String id) {
        List<Patient> list = getAllPatients();
        boolean removed = list.removeIf(p -> p.getId().equalsIgnoreCase(id));
        if (removed) {
            saveAll(list);
            System.out.println("Đã xóa bệnh nhân có ID: " + id);
        } else {
            System.out.println("Không tìm thấy bệnh nhân có ID: " + id);
        }
    }

    // =================== TÌM KIẾM ===================
    public void searchPatient() {
        System.out.println("===== TÌM KIẾM BỆNH NHÂN =====");
        String keyword = InputUtil.nhapChuoi("Nhập từ khóa (tên / SĐT / chẩn đoán): ");
        List<Patient> list = getAllPatients();
        List<Patient> result = new ArrayList<>();

        for (Patient p : list) {
            if (p.getName().toLowerCase().contains(keyword.toLowerCase())
                    || p.getPhone().equalsIgnoreCase(keyword)
                    || p.getDiagnosis().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(p);
            }
        }

        if (result.isEmpty()) {
            System.out.println("Không tìm thấy bệnh nhân phù hợp!");
            return;
        }

        System.out.println("Kết quả tìm kiếm:");
        System.out.println("===================================================================================================================================================");
        System.out.printf("| %-8s | %-20s | %-4s | %-6s | %-20s | %-12s | %-20s | %-6s | %-10s | %-15s |%n",
                "ID", "Họ tên", "Tuổi", "Giới", "Địa chỉ", "SĐT", "Chẩn đoán", "Máu", "Bác sĩ", "Trạng thái");
        System.out.println("===================================================================================================================================================");

        for (Patient p : result) {
            System.out.printf("| %-8s | %-20s | %-4d | %-6s | %-20s | %-12s | %-20s | %-6s | %-10s | %-15s |%n",
                    p.getId(), p.getName(), p.getAge(), p.getGender(),
                    p.getAddress(), p.getPhone(), p.getDiagnosis(),
                    p.getBloodGroup(), p.getDoctorId(), p.getStatus());
        }

        System.out.println("===================================================================================================================================================");
    }

    // =================== TÌM THEO ID ===================
    public Patient findById(String id) {
        for (Patient p : getAllPatients()) {
            if (p.getId().equalsIgnoreCase(id)) return p;
        }
        return null;
    }
}
