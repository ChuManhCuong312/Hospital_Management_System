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
}
