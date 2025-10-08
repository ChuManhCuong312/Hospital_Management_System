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

            FileUtil.appendToFile(FILE_PATH, patient.toString());
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

            String id = InputUtil.inputString("Nhập ID bệnh nhân: ");
            String name = InputUtil.inputString("Nhập họ tên: ");
            int age = InputUtil.inputInt("Nhập tuổi: ");
            String gender = InputUtil.inputString("Nhập giới tính (Nam/Nữ): ");
            String diagnosis = InputUtil.inputString("Nhập chẩn đoán: ");

            // Validate và chuẩn hóa dữ liệu
            Patient patient = PatientValidator.validateAndNormalize(id, name, age, gender, diagnosis);

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
        System.out.println("+------+----------------+-----+-----------+----------------------+");
        System.out.println("|  ID  |     Họ tên     | Tuổi| Giới tính |      Chẩn đoán       |");
        System.out.println("+------+----------------+-----+-----------+----------------------+");

        for (Patient patient : patients) {
            System.out.printf("| %-4s | %-14s | %-3d | %-9s | %-20s |%n",
                    patient.getId(),
                    patient.getName(),
                    patient.getAge(),
                    patient.getGender(),
                    patient.getDiagnosis());
        }

        System.out.println("+------+----------------+-----+-----------+----------------------+");
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
     * Hiển thị kết quả tìm kiếm
     */
    public void showSearchResults(List<Patient> patients, String searchType) {
        if (patients.isEmpty()) {
            System.out.println("Không tìm thấy bệnh nhân nào với tiêu chí: " + searchType);
            return;
        }

        System.out.println("\n=== KẾT QUẢ TÌM KIẾM: " + searchType.toUpperCase() + " ===");
        System.out.println("+------+----------------+-----+-----------+----------------------+");
        System.out.println("|  ID  |     Họ tên     | Tuổi| Giới tính |      Chẩn đoán       |");
        System.out.println("+------+----------------+-----+-----------+----------------------+");

        for (Patient patient : patients) {
            System.out.printf("| %-4s | %-14s | %-3d | %-9s | %-20s |%n",
                    patient.getId(),
                    patient.getName(),
                    patient.getAge(),
                    patient.getGender(),
                    patient.getDiagnosis());
        }

        System.out.println("+------+----------------+-----+-----------+----------------------+");
        System.out.println("Tìm thấy " + patients.size() + " bệnh nhân");
    }
    // ==================== UPDATE OPERATIONS ====================

    /**
     * Cập nhật thông tin bệnh nhân
     */
    public void updatePatient(String id, String newName, int newAge, String newGender, String newDiagnosis) {
        try {
            List<Patient> patients = getAllPatients();
            boolean found = false;

            for (Patient patient : patients) {
                if (patient.getId().equalsIgnoreCase(id)) {
                    // Validate dữ liệu mới
                    PatientValidator.validateName(newName);
                    PatientValidator.validateAge(newAge);
                    PatientValidator.validateGender(newGender);
                    PatientValidator.validateDiagnosis(newDiagnosis);

                    patient.setName(newName);
                    patient.setAge(newAge);
                    patient.setGender(newGender);
                    patient.setDiagnosis(newDiagnosis);
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
            System.out.println("Chẩn đoán: " + existingPatient.getDiagnosis());

            System.out.println("\nNhập thông tin mới (Enter để giữ nguyên):");
            String newName = InputUtil.inputString("Họ tên mới: ");
            String newAgeStr = InputUtil.inputString("Tuổi mới: ");
            String newGender = InputUtil.inputString("Giới tính mới: ");
            String newDiagnosis = InputUtil.inputString("Chẩn đoán mới: ");

            if (newName.trim().isEmpty()) newName = existingPatient.getName();
            if (newGender.trim().isEmpty()) newGender = existingPatient.getGender();
            if (newDiagnosis.trim().isEmpty()) newDiagnosis = existingPatient.getDiagnosis();

            int newAge = existingPatient.getAge();
            if (!newAgeStr.trim().isEmpty()) {
                try {
                    newAge = Integer.parseInt(newAgeStr);
                } catch (NumberFormatException e) {
                    System.out.println("Tuổi không hợp lệ, giữ nguyên tuổi cũ: " + existingPatient.getAge());
                }
            }

            updatePatient(id, newName, newAge, newGender, newDiagnosis);
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
            System.out.println("Chẩn đoán: " + patient.getDiagnosis());

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


}
