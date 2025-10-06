package service;

import java.util.*;
import java.util.stream.Collectors;
import model.Patient;
import util.FileUtil;

public class PatientService {
    private static final String FILE_PATH = "data/patients.txt";
    private List<Patient> patients;

    public PatientService() {
        this.patients = new ArrayList<>();
        loadPatientsFromFile();
    }

    /**
     * 1. Thêm bệnh nhân mới → ghi vào patients.txt
     */
    public boolean addPatient(Patient patient) {
        // Kiểm tra mã bệnh nhân đã tồn tại chưa
        if (findById(patient.getPatientId()) != null) {
            System.err.println("Lỗi: Mã bệnh nhân " + patient.getPatientId() + " đã tồn tại!");
            return false;
        }

        patients.add(patient);
        boolean success = appendPatientToFile(patient);
        
        if (success) {
            System.out.println("✓ Đã thêm bệnh nhân thành công: " + patient.getFullName());
        } else {
            patients.remove(patient); // Rollback nếu ghi file thất bại
            System.err.println("✗ Lỗi khi ghi vào file!");
        }
        
        return success;
    }

    /**
     * 2. Sửa thông tin bệnh nhân
     */
    public boolean updatePatient(String patientId, Patient updatedPatient) {
        Patient existingPatient = findById(patientId);
        
        if (existingPatient == null) {
            System.err.println("Lỗi: Không tìm thấy bệnh nhân với mã " + patientId);
            return false;
        }

        // Cập nhật thông tin
        int index = patients.indexOf(existingPatient);
        patients.set(index, updatedPatient);

        // Ghi lại toàn bộ file
        boolean success = saveAll(patients);
        
        if (success) {
            System.out.println("✓ Đã cập nhật thông tin bệnh nhân: " + updatedPatient.getFullName());
        } else {
            patients.set(index, existingPatient); // Rollback
            System.err.println("✗ Lỗi khi cập nhật file!");
        }
        
        return success;
    }

    /**
     * 3. Xóa bệnh nhân
     */
    public boolean deletePatient(String patientId) {
        Patient patient = findById(patientId);
        
        if (patient == null) {
            System.err.println("Lỗi: Không tìm thấy bệnh nhân với mã " + patientId);
            return false;
        }

        patients.remove(patient);
        boolean success = saveAll(patients);
        
        if (success) {
            System.out.println("✓ Đã xóa bệnh nhân: " + patient.getFullName());
        } else {
            patients.add(patient); // Rollback
            System.err.println("✗ Lỗi khi cập nhật file!");
        }
        
        return success;
    }

    /**
     * 4. Đọc danh sách bệnh nhân từ file
     */
    public void loadPatientsFromFile() {
        List<String> lines = FileUtil.readFile(FILE_PATH);
        patients.clear();
        
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                try {
                    Patient patient = Patient.fromString(line);
                    if (patient != null) {
                        patients.add(patient);
                    }
                } catch (Exception e) {
                    System.err.println("Lỗi khi đọc dòng: " + line + " - " + e.getMessage());
                }
            }
        }
        
        System.out.println("✓ Đã tải " + patients.size() + " bệnh nhân từ file");
    }

    /**
     * 5. Tìm kiếm bệnh nhân theo ID
     */
    public Patient findById(String id) {
        return patients.stream()
                .filter(p -> p.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Tìm kiếm bệnh nhân theo tên (tìm kiếm gần đúng)
     */
    public List<Patient> findPatientsByName(String name) {
        String searchName = name.toLowerCase().trim();
        return patients.stream()
                .filter(p -> p.getFullName().toLowerCase().contains(searchName))
                .collect(Collectors.toList());
    }

    /**
     * Tìm kiếm bệnh nhân theo số điện thoại
     */
    public Patient findPatientByPhone(String phoneNumber) {
        return patients.stream()
                .filter(p -> p.getPhoneNumber().equals(phoneNumber))
                .findFirst()
                .orElse(null);
    }

    /**
     * Tìm kiếm bệnh nhân theo CCCD
     */
    public Patient findPatientByIdCard(String idCard) {
        return patients.stream()
                .filter(p -> p.getIdCard().equals(idCard))
                .findFirst()
                .orElse(null);
    }

    /**
     * Lấy danh sách tất cả bệnh nhân
     */
    public List<Patient> getAllPatients() {
        return new ArrayList<>(patients);
    }

    /**
     * Lấy số lượng bệnh nhân
     */
    public int getPatientCount() {
        return patients.size();
    }

    /**
     * Hiển thị tất cả bệnh nhân
     */
    public void viewAll() {
        if (patients.isEmpty()) {
            System.out.println("\n╔════════════════════════════════════════════╗");
            System.out.println("║ Chưa có bệnh nhân nào trong hệ thống!     ║");
            System.out.println("╚════════════════════════════════════════════╝\n");
            return;
        }

        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║            DANH SÁCH BỆNH NHÂN (Tổng: " + String.format("%-3d", patients.size()) + ")                    ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");
        
        for (Patient patient : patients) {
            System.out.println(patient);
            System.out.println();
        }
    }

    /**
     * Hiển thị danh sách bệnh nhân dạng bảng
     */
    public void displayPatientsTable() {
        if (patients.isEmpty()) {
            System.out.println("Chưa có bệnh nhân nào trong hệ thống!");
            return;
        }

        System.out.println("\n╔════════╦═══════════════════════╦══════════════╦═══════════╦══════════════╦══════════════╦════════════╗");
        System.out.println("║ Mã BN  ║ Họ tên                ║ Ngày sinh    ║ Giới tính ║ SĐT          ║ Nhóm máu     ║ Trạng thái ║");
        System.out.println("╠════════╬═══════════════════════╬══════════════╬═══════════╬══════════════╬══════════════╬════════════╣");
        
        for (Patient patient : patients) {
            System.out.printf("║ %-6s ║ %-21s ║ %-12s ║ %-9s ║ %-12s ║ %-12s ║ %-10s ║\n",
                    patient.getPatientId(),
                    truncate(patient.getFullName(), 21),
                    patient.getDateOfBirth().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    patient.getGender(),
                    patient.getPhoneNumber(),
                    patient.getBloodType(),
                    patient.getStatus()
            );
        }
        
        System.out.println("╚════════╩═══════════════════════╩══════════════╩═══════════╩══════════════╩══════════════╩════════════╝");
        System.out.println("Tổng số bệnh nhân: " + patients.size());
    }

    /**
     * Helper method để cắt chuỗi nếu quá dài
     */
    private String truncate(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }

    /**
     * Thêm một bệnh nhân vào cuối file
     */
    private boolean appendPatientToFile(Patient patient) {
        try {
            FileUtil.appendToFile(FILE_PATH, patient.toFileString());
            return true;
        } catch (Exception e) {
            System.err.println("Lỗi khi ghi thêm vào file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lưu tất cả bệnh nhân vào file
     */
    private boolean saveAll(List<Patient> patients) {
        try {
            List<String> lines = new ArrayList<>();
            for (Patient p : patients) {
                lines.add(p.toFileString());
            }
            FileUtil.writeFile(FILE_PATH, lines);
            return true;
        } catch (Exception e) {
            System.err.println("Lỗi khi ghi file: " + e.getMessage());
            return false;
        }
    }

    // ============ METHODS TƯƠNG THÍCH VỚI CODE CŨ ============

    /**
     * Method tương thích với code cũ - thêm bệnh nhân đơn giản
     */
    public void addSimplePatient(Patient p) {
        addPatient(p);
    }

    /**
     * Method tương thích với code cũ - cập nhật bệnh nhân đơn giản
     */
    public void updateSimplePatient(String id, String newName, int newAge, String newGender, String newDiagnosis) {
        Patient existingPatient = findById(id);
        if (existingPatient != null) {
            Patient updatedPatient = new Patient(id, newName, newAge, newGender, newDiagnosis);
            updatePatient(id, updatedPatient);
        } else {
            System.out.println("❌ Không tìm thấy bệnh nhân có ID: " + id);
        }
    }
}
