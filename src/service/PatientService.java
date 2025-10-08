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
}
