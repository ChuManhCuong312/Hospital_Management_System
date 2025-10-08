package service;

import exception.InvalidDataException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import model.Patient;
import util.FileUtil;
import util.InputUtil;
import util.PatientValidator;

public class PatientService {
    private static final String FILE_PATH = "data/patients.txt";
    // ==================== CREATE OPERATIONS ====================

    /**
     * Thêm bệnh nhân mới vào hệ thống
     */
    public void addPatient(Patient patient) {
        try {
            if (patient == null) {
                System.out.println("Thông tin bệnh nhân không hợp lệ!");
                return;
            }

            // Validate dữ liệu bệnh nhân
            PatientValidator.validatePatient(patient);

            // Kiểm tra ID trùng lặp
            if (findById(patient.getId()) != null) {
                System.out.println("ID bệnh nhân đã tồn tại: " + patient.getId());
                return;
            }

            FileUtil.appendToFile(FILE_PATH, patient.toDataString());
            System.out.println("Đã thêm bệnh nhân: " + patient.getName() + " (ID: " + patient.getId() + ")");

        } catch (InvalidDataException e) {
            System.out.println("Dữ liệu không hợp lệ: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Lỗi khi thêm bệnh nhân: " + e.getMessage());
        }
    }

    /**
     * Thêm bệnh nhân từ input người dùng
     */
    public String addPatientFromInput(Scanner scanner) {
        try {
            System.out.println("\n=== THÊM BỆNH NHÂN MỚI ===");

            String id = InputUtil.inputString("Nhập ID bệnh nhân (P001, P002, ...): ");
            String name = InputUtil.inputString("Nhập họ tên: ");
            int age = InputUtil.inputInt("Nhập tuổi: ");
            String gender = InputUtil.inputString("Nhập giới tính (Nam/Nữ): ");
            String address = InputUtil.inputString("Nhập địa chỉ (Enter để bỏ qua): ");
            String phone = InputUtil.inputString("Nhập số điện thoại (Enter để bỏ qua): ");
            String diagnosis = InputUtil.inputString("Nhập chẩn đoán: ");
            String bloodGroup = InputUtil.inputString("Nhập nhóm máu (A/B/AB/O, Enter để bỏ qua): ");
            String doctorId = InputUtil.inputString("Nhập ID bác sĩ (D001, D002, ..., Enter để bỏ qua): ");
            String status = InputUtil.inputString("Nhập trạng thái (Đang điều trị/Đã xuất viện/Chờ khám, Enter để bỏ qua): ");

            // Chuẩn hóa dữ liệu
            id = id.trim();
            name = name.trim();
            address = address.trim();
            phone = phone.trim();
            diagnosis = diagnosis.trim();
            bloodGroup = bloodGroup.trim();
            doctorId = doctorId.trim();
            status = status.trim();

            // Validate dữ liệu
            PatientValidator.validateId(id);
            PatientValidator.validateName(name);
            PatientValidator.validateAge(age);
            PatientValidator.validateGender(gender);
            PatientValidator.validateAddress(address);
            PatientValidator.validatePhone(phone);
            PatientValidator.validateDiagnosis(diagnosis);
            PatientValidator.validateBloodGroup(bloodGroup);
            PatientValidator.validateDoctorId(doctorId);
            PatientValidator.validateStatus(status);

            // Tạo đối tượng Patient với đầy đủ thông tin
            Patient patient = new Patient(id, name, age, gender, address, phone, 
                                        diagnosis, bloodGroup, doctorId, status);

            // Kiểm tra ID trùng lặp
            if (findById(patient.getId()) != null) {
                return "ID bệnh nhân đã tồn tại: " + patient.getId();
            }

            addPatient(patient);
            return "Thêm bệnh nhân thành công!";

        } catch (InvalidDataException e) {
            return "Dữ liệu không hợp lệ: " + e.getMessage();
        } catch (Exception e) {
            return "Lỗi khi thêm bệnh nhân: " + e.getMessage();
        }
    }

    // ==================== READ OPERATIONS ====================

    /**
     * Lấy tất cả bệnh nhân từ file
     */
    public List<Patient> getAllPatients() {
        List<String> lines = FileUtil.readFile(FILE_PATH);
        List<Patient> patients = new ArrayList<>();

        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            Patient patient = Patient.fromString(line);
            if (patient != null) {
                patients.add(patient);
            }
        }
        return patients;
    }

    /**
     * Hiển thị tất cả bệnh nhân dạng bảng
     */
    public void viewAll() {
        List<Patient> patients = getAllPatients();
        if (patients.isEmpty()) {
            System.out.println("Không có bệnh nhân nào trong hệ thống!");
            return;
        }

        System.out.println("\n=== DANH SÁCH BỆNH NHÂN ===");
        System.out.println("+------+----------------+-----+-----------+----------------------+----------------+----------+------+----------+");
        System.out.println("|  ID  |     Họ tên     | Tuổi| Giới tính |      Chẩn đoán       |    Địa chỉ     |   SĐT    | Nhóm máu| Trạng thái|");
        System.out.println("+------+----------------+-----+-----------+----------------------+----------------+----------+------+----------+");

        for (Patient patient : patients) {
            System.out.printf("| %-4s | %-14s | %-3d | %-9s | %-20s | %-14s | %-8s | %-4s | %-8s |%n",
                    patient.getId(),
                    patient.getName(),
                    patient.getAge(),
                    patient.getGender(),
                    patient.getDiagnosis(),
                    patient.getAddress().isEmpty() ? "N/A" : patient.getAddress(),
                    patient.getPhone().isEmpty() ? "N/A" : patient.getPhone(),
                    patient.getBloodGroup().isEmpty() ? "N/A" : patient.getBloodGroup(),
                    patient.getStatus().isEmpty() ? "N/A" : patient.getStatus());
        }

        System.out.println("+------+----------------+-----+-----------+----------------------+----------------+----------+------+----------+");
        System.out.println("Tổng số bệnh nhân: " + patients.size());
    }


    /**
     * Tìm bệnh nhân theo ID
     */
    public Patient findById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }

        return getAllPatients().stream()
                .filter(patient -> patient.getId().equalsIgnoreCase(id.trim()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Tìm bệnh nhân theo tên
     */
    public List<Patient> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return new ArrayList<>();
        }

        return getAllPatients().stream()
                .filter(patient -> patient.getName().toLowerCase().contains(name.toLowerCase().trim()))
                .collect(Collectors.toList());
    }

    /**
     * Tìm bệnh nhân theo giới tính
     */
    public List<Patient> findByGender(String gender) {
        if (gender == null || gender.trim().isEmpty()) {
            return new ArrayList<>();
        }

        return getAllPatients().stream()
                .filter(patient -> patient.getGender().equalsIgnoreCase(gender.trim()))
                .collect(Collectors.toList());
    }

    /**
     * Tìm bệnh nhân theo chẩn đoán
     */
    public List<Patient> findByDiagnosis(String diagnosis) {
        if (diagnosis == null || diagnosis.trim().isEmpty()) {
            return new ArrayList<>();
        }

        return getAllPatients().stream()
                .filter(patient -> patient.getDiagnosis().toLowerCase().contains(diagnosis.toLowerCase().trim()))
                .collect(Collectors.toList());
    }

    /**
     * Lọc bệnh nhân theo độ tuổi
     */
    public List<Patient> filterByAge(int minAge, int maxAge) {
        return getAllPatients().stream()
                .filter(patient -> patient.getAge() >= minAge && patient.getAge() <= maxAge)
                .collect(Collectors.toList());
    }

    /**
     * Tìm bệnh nhân theo số điện thoại
     */
    public List<Patient> findByPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return new ArrayList<>();
        }

        return getAllPatients().stream()
                .filter(patient -> patient.getPhone() != null && 
                         patient.getPhone().contains(phone.trim()))
                .collect(Collectors.toList());
    }

    /**
     * Tìm bệnh nhân theo nhóm máu
     */
    public List<Patient> findByBloodGroup(String bloodGroup) {
        if (bloodGroup == null || bloodGroup.trim().isEmpty()) {
            return new ArrayList<>();
        }

        return getAllPatients().stream()
                .filter(patient -> patient.getBloodGroup() != null && 
                         patient.getBloodGroup().equalsIgnoreCase(bloodGroup.trim()))
                .collect(Collectors.toList());
    }

    /**
     * Tìm bệnh nhân theo ID bác sĩ
     */
    public List<Patient> findByDoctorId(String doctorId) {
        if (doctorId == null || doctorId.trim().isEmpty()) {
            return new ArrayList<>();
        }

        return getAllPatients().stream()
                .filter(patient -> patient.getDoctorId() != null && 
                         patient.getDoctorId().equalsIgnoreCase(doctorId.trim()))
                .collect(Collectors.toList());
    }

    /**
     * Tìm bệnh nhân theo trạng thái
     */
    public List<Patient> findByStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return new ArrayList<>();
        }

        return getAllPatients().stream()
                .filter(patient -> patient.getStatus() != null && 
                         patient.getStatus().equalsIgnoreCase(status.trim()))
                .collect(Collectors.toList());
    }

    /**
     * Tìm bệnh nhân theo địa chỉ
     */
    public List<Patient> findByAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            return new ArrayList<>();
        }

        return getAllPatients().stream()
                .filter(patient -> patient.getAddress() != null && 
                         patient.getAddress().toLowerCase().contains(address.toLowerCase().trim()))
                .collect(Collectors.toList());
    }

    /**
     * Hiển thị thông tin chi tiết của một bệnh nhân
     */
    public void showPatientDetails(Patient patient) {
        if (patient == null) {
            System.out.println("Không tìm thấy thông tin bệnh nhân!");
            return;
        }

        System.out.println("\n=== THÔNG TIN CHI TIẾT BỆNH NHÂN ===");
        System.out.println("┌─────────────────────────────────────────────────────────────┐");
        System.out.printf("│ ID: %-55s │%n", patient.getId());
        System.out.printf("│ Họ tên: %-50s │%n", patient.getName());
        System.out.printf("│ Tuổi: %-52s │%n", patient.getAge());
        System.out.printf("│ Giới tính: %-47s │%n", patient.getGender());
        System.out.printf("│ Địa chỉ: %-49s │%n", patient.getAddress().isEmpty() ? "Chưa cập nhật" : patient.getAddress());
        System.out.printf("│ Số điện thoại: %-43s │%n", patient.getPhone().isEmpty() ? "Chưa cập nhật" : patient.getPhone());
        System.out.printf("│ Chẩn đoán: %-47s │%n", patient.getDiagnosis());
        System.out.printf("│ Nhóm máu: %-48s │%n", patient.getBloodGroup().isEmpty() ? "Chưa cập nhật" : patient.getBloodGroup());
        System.out.printf("│ ID bác sĩ: %-46s │%n", patient.getDoctorId().isEmpty() ? "Chưa phân công" : patient.getDoctorId());
        System.out.printf("│ Trạng thái: %-45s │%n", patient.getStatus().isEmpty() ? "Chưa cập nhật" : patient.getStatus());
        System.out.println("└─────────────────────────────────────────────────────────────┘");
    }

    /**
     * Hiển thị kết quả tìm kiếm
     */
    public void showSearchResults(List<Patient> patients, String searchType) {
        if (patients.isEmpty()) {
            System.out.println("Không tìm thấy bệnh nhân nào với tiêu chí: " + searchType);
            return;
        }

        System.out.println("\n=== KẾT QUẢ TÌM KIẾM: " + searchType.toUpperCase() + " ===");
        System.out.println("+------+----------------+-----+-----------+----------------------+----------------+----------+------+----------+");
        System.out.println("|  ID  |     Họ tên     | Tuổi| Giới tính |      Chẩn đoán       |    Địa chỉ     |   SĐT    | Nhóm máu| Trạng thái|");
        System.out.println("+------+----------------+-----+-----------+----------------------+----------------+----------+------+----------+");

        for (Patient patient : patients) {
            System.out.printf("| %-4s | %-14s | %-3d | %-9s | %-20s | %-14s | %-8s | %-4s | %-8s |%n",
                    patient.getId(),
                    patient.getName(),
                    patient.getAge(),
                    patient.getGender(),
                    patient.getDiagnosis(),
                    patient.getAddress().isEmpty() ? "N/A" : patient.getAddress(),
                    patient.getPhone().isEmpty() ? "N/A" : patient.getPhone(),
                    patient.getBloodGroup().isEmpty() ? "N/A" : patient.getBloodGroup(),
                    patient.getStatus().isEmpty() ? "N/A" : patient.getStatus());
        }

        System.out.println("+------+----------------+-----+-----------+----------------------+----------------+----------+------+----------+");
        System.out.println("Tìm thấy " + patients.size() + " bệnh nhân");
    }
    // ==================== UPDATE OPERATIONS ====================

    /**
     * Cập nhật thông tin bệnh nhân
     */
    public void updatePatient(String id, String newName, int newAge, String newGender, 
                            String newAddress, String newPhone, String newDiagnosis, 
                            String newBloodGroup, String newDoctorId, String newStatus) {
        try {
            List<Patient> patients = getAllPatients();
            boolean found = false;

            for (Patient patient : patients) {
                if (patient.getId().equalsIgnoreCase(id)) {
                    // Validate dữ liệu mới
                    PatientValidator.validateName(newName);
                    PatientValidator.validateAge(newAge);
                    PatientValidator.validateGender(newGender);
                    PatientValidator.validateAddress(newAddress);
                    PatientValidator.validatePhone(newPhone);
                    PatientValidator.validateDiagnosis(newDiagnosis);
                    PatientValidator.validateBloodGroup(newBloodGroup);
                    PatientValidator.validateDoctorId(newDoctorId);
                    PatientValidator.validateStatus(newStatus);

                    patient.setName(newName);
                    patient.setAge(newAge);
                    patient.setGender(newGender);
                    patient.setAddress(newAddress);
                    patient.setPhone(newPhone);
                    patient.setDiagnosis(newDiagnosis);
                    patient.setBloodGroup(newBloodGroup);
                    patient.setDoctorId(newDoctorId);
                    patient.setStatus(newStatus);
                    found = true;
                    break;
                }
            }

            if (found) {
                saveAll(patients);
                System.out.println("Đã cập nhật thông tin bệnh nhân ID: " + id);
            } else {
                System.out.println("Không tìm thấy bệnh nhân có ID: " + id);
            }

        } catch (InvalidDataException e) {
            System.out.println("Dữ liệu không hợp lệ: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật: " + e.getMessage());
        }
    }

    /**
     * Cập nhật bệnh nhân từ input người dùng
     */
    public String updatePatientFromInput(Scanner scanner) {
        try {
            System.out.println("\n=== CẬP NHẬT THÔNG TIN BỆNH NHÂN ===");

            String id = InputUtil.inputString("Nhập ID bệnh nhân cần cập nhật: ");
            Patient existingPatient = findById(id);

            if (existingPatient == null) {
                return "Không tìm thấy bệnh nhân có ID: " + id;
            }

            System.out.println("Thông tin hiện tại:");
            System.out.println("ID: " + existingPatient.getId());
            System.out.println("Họ tên: " + existingPatient.getName());
            System.out.println("Tuổi: " + existingPatient.getAge());
            System.out.println("Giới tính: " + existingPatient.getGender());
            System.out.println("Địa chỉ: " + existingPatient.getAddress());
            System.out.println("Số điện thoại: " + existingPatient.getPhone());
            System.out.println("Chẩn đoán: " + existingPatient.getDiagnosis());
            System.out.println("Nhóm máu: " + existingPatient.getBloodGroup());
            System.out.println("ID bác sĩ: " + existingPatient.getDoctorId());
            System.out.println("Trạng thái: " + existingPatient.getStatus());

            System.out.println("\nNhập thông tin mới (Enter để giữ nguyên):");
            String newName = InputUtil.inputString("Họ tên mới: ");
            String newAgeStr = InputUtil.inputString("Tuổi mới: ");
            String newGender = InputUtil.inputString("Giới tính mới: ");
            String newAddress = InputUtil.inputString("Địa chỉ mới: ");
            String newPhone = InputUtil.inputString("Số điện thoại mới: ");
            String newDiagnosis = InputUtil.inputString("Chẩn đoán mới: ");
            String newBloodGroup = InputUtil.inputString("Nhóm máu mới: ");
            String newDoctorId = InputUtil.inputString("ID bác sĩ mới: ");
            String newStatus = InputUtil.inputString("Trạng thái mới: ");

            // Giữ nguyên giá trị cũ nếu người dùng không nhập
            if (newName.trim().isEmpty()) newName = existingPatient.getName();
            if (newGender.trim().isEmpty()) newGender = existingPatient.getGender();
            if (newAddress.trim().isEmpty()) newAddress = existingPatient.getAddress();
            if (newPhone.trim().isEmpty()) newPhone = existingPatient.getPhone();
            if (newDiagnosis.trim().isEmpty()) newDiagnosis = existingPatient.getDiagnosis();
            if (newBloodGroup.trim().isEmpty()) newBloodGroup = existingPatient.getBloodGroup();
            if (newDoctorId.trim().isEmpty()) newDoctorId = existingPatient.getDoctorId();
            if (newStatus.trim().isEmpty()) newStatus = existingPatient.getStatus();

            int newAge = existingPatient.getAge();
            if (!newAgeStr.trim().isEmpty()) {
                try {
                    newAge = Integer.parseInt(newAgeStr);
                } catch (NumberFormatException e) {
                    System.out.println("Tuổi không hợp lệ, giữ nguyên tuổi cũ: " + existingPatient.getAge());
                }
            }

            updatePatient(id, newName, newAge, newGender, newAddress, newPhone, 
                         newDiagnosis, newBloodGroup, newDoctorId, newStatus);
            return "Cập nhật thành công!";

        } catch (Exception e) {
            return "Lỗi khi cập nhật: " + e.getMessage();
        }
    }

    // ==================== DELETE OPERATIONS ====================

    /**
     * Xóa bệnh nhân theo ID
     */
    public void deletePatient(String id) {
        List<Patient> patients = getAllPatients();
        boolean removed = patients.removeIf(patient -> patient.getId().equalsIgnoreCase(id));

        if (removed) {
            saveAll(patients);
            System.out.println("Đã xóa bệnh nhân ID: " + id);
        } else {
            System.out.println("Không tìm thấy bệnh nhân có ID: " + id);
        }
    }

    /**
     * Xóa bệnh nhân từ input người dùng
     */
    public String deletePatientFromInput(Scanner scanner) {
        try {
            System.out.println("\n=== XÓA BỆNH NHÂN ===");

            String id = InputUtil.inputString("Nhập ID bệnh nhân cần xóa: ");
            Patient patient = findById(id);

            if (patient == null) {
                return "Không tìm thấy bệnh nhân có ID: " + id;
            }

            System.out.println("Thông tin bệnh nhân sẽ bị xóa:");
            System.out.println("ID: " + patient.getId());
            System.out.println("Họ tên: " + patient.getName());
            System.out.println("Tuổi: " + patient.getAge());
            System.out.println("Giới tính: " + patient.getGender());
            System.out.println("Địa chỉ: " + patient.getAddress());
            System.out.println("Số điện thoại: " + patient.getPhone());
            System.out.println("Chẩn đoán: " + patient.getDiagnosis());
            System.out.println("Nhóm máu: " + patient.getBloodGroup());
            System.out.println("ID bác sĩ: " + patient.getDoctorId());
            System.out.println("Trạng thái: " + patient.getStatus());

            String confirm = InputUtil.inputString("\nBạn có chắc chắn muốn xóa? (yes/no): ");
            if ("yes".equalsIgnoreCase(confirm) || "y".equalsIgnoreCase(confirm)) {
                deletePatient(id);
                return "Xóa bệnh nhân thành công!";
            } else {
                return "Hủy bỏ việc xóa bệnh nhân";
            }

        } catch (Exception e) {
            return "Lỗi khi xóa: " + e.getMessage();
        }
    }

    // ==================== UTILITY OPERATIONS ====================

    /**
     * Lưu tất cả bệnh nhân vào file
     */
    private void saveAll(List<Patient> patients) {
        List<String> lines = patients.stream()
                .map(Patient::toDataString)
                .collect(Collectors.toList());
        FileUtil.writeFile(FILE_PATH, lines);
    }

    /**
     * Xuất dữ liệu ra file
     */
    public boolean exportToFile(String fileName) {
        try {
            List<Patient> patients = getAllPatients();
            List<String> lines = patients.stream()
                    .map(Patient::toDataString)
                    .collect(Collectors.toList());

            FileUtil.writeFile(fileName, lines);
            return true;
        } catch (Exception e) {
            System.out.println("Lỗi khi xuất file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Nhập dữ liệu từ file
     */
    public String importFromFile(String fileName) {
        try {
            List<String> lines = FileUtil.readFile(fileName);
            int importedCount = 0;
            int errorCount = 0;

            for (String line : lines) {
                if (line.trim().isEmpty()) continue;

                Patient patient = Patient.fromString(line);
                if (patient != null) {
                    if (findById(patient.getId()) == null) {
                        FileUtil.appendToFile(FILE_PATH, patient.toDataString());
                        importedCount++;
                    } else {
                        errorCount++;
                    }
                } else {
                    errorCount++;
                }
            }

            return "Import thành công " + importedCount + " bệnh nhân. "
                    + (errorCount > 0 ? errorCount + " dòng lỗi." : "");

        } catch (Exception e) {
            return "Lỗi khi import file: " + e.getMessage();
        }
    }

    /**
     * Thống kê bệnh nhân
     */
    public void showStatistics() {
        List<Patient> patients = getAllPatients();
        if (patients.isEmpty()) {
            System.out.println("Không có dữ liệu để thống kê!");
            return;
        }

        System.out.println("\n=== THỐNG KÊ BỆNH NHÂN ===");
        System.out.println("Tổng số bệnh nhân: " + patients.size());

        // Thống kê theo giới tính
        long maleCount = patients.stream().filter(p -> "Nam".equalsIgnoreCase(p.getGender())).count();
        long femaleCount = patients.stream().filter(p -> "Nữ".equalsIgnoreCase(p.getGender())).count();
        System.out.println("Nam: " + maleCount + " | Nữ: " + femaleCount);

        // Thống kê theo độ tuổi
        double avgAge = patients.stream().mapToInt(Patient::getAge).average().orElse(0);
        int minAge = patients.stream().mapToInt(Patient::getAge).min().orElse(0);
        int maxAge = patients.stream().mapToInt(Patient::getAge).max().orElse(0);
        System.out.println("Tuổi trung bình: " + String.format("%.1f", avgAge));
        System.out.println("Tuổi nhỏ nhất: " + minAge + " | Tuổi lớn nhất: " + maxAge);

        // Top 5 chẩn đoán phổ biến
        Map<String, Long> diagnosisCount = patients.stream()
                .collect(Collectors.groupingBy(Patient::getDiagnosis, Collectors.counting()));

        System.out.println("\nTop 5 chẩn đoán phổ biến:");
        diagnosisCount.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .forEach(entry -> System.out.println("  • " + entry.getKey() + ": " + entry.getValue() + " bệnh nhân"));

        // Thống kê theo nhóm máu
        Map<String, Long> bloodGroupCount = patients.stream()
                .filter(p -> p.getBloodGroup() != null && !p.getBloodGroup().isEmpty())
                .collect(Collectors.groupingBy(Patient::getBloodGroup, Collectors.counting()));

        if (!bloodGroupCount.isEmpty()) {
            System.out.println("\nThống kê theo nhóm máu:");
            bloodGroupCount.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .forEach(entry -> System.out.println("  • " + entry.getKey() + ": " + entry.getValue() + " bệnh nhân"));
        }

        // Thống kê theo trạng thái
        Map<String, Long> statusCount = patients.stream()
                .filter(p -> p.getStatus() != null && !p.getStatus().isEmpty())
                .collect(Collectors.groupingBy(Patient::getStatus, Collectors.counting()));

        if (!statusCount.isEmpty()) {
            System.out.println("\nThống kê theo trạng thái:");
            statusCount.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .forEach(entry -> System.out.println("  • " + entry.getKey() + ": " + entry.getValue() + " bệnh nhân"));
        }

        // Thống kê theo bác sĩ
        Map<String, Long> doctorCount = patients.stream()
                .filter(p -> p.getDoctorId() != null && !p.getDoctorId().isEmpty())
                .collect(Collectors.groupingBy(Patient::getDoctorId, Collectors.counting()));

        if (!doctorCount.isEmpty()) {
            System.out.println("\nThống kê theo bác sĩ:");
            doctorCount.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .limit(5)
                    .forEach(entry -> System.out.println("  • " + entry.getKey() + ": " + entry.getValue() + " bệnh nhân"));
        }
    }
}