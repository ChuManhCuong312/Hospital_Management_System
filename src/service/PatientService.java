package service;

import model.Patient;
import util.FileUtil;

import java.util.*;

public class PatientService {
    private static final String FILE_PATH = "data/patients.txt";

    public void addPatient(Patient p) {
        FileUtil.appendToFile(FILE_PATH, p.toString());
        System.out.println("Đã thêm bệnh nhân: " + p.getName());
    }

    public List<Patient> getAllPatients() {
        List<String> lines = FileUtil.readFile(FILE_PATH);
        List<Patient> patients = new ArrayList<>();
        for (String line : lines) {
            Patient p = Patient.fromString(line);
            if (p != null) patients.add(p);
        }
        return patients;
    }

    public void viewAll() {
        List<Patient> patients = getAllPatients();
        if (patients.isEmpty()) {
            System.out.println("Không có bệnh nhân nào trong hệ thống!");
            return;
        }

        System.out.println("+------+----------------+------+-----------+----------------------+");
        System.out.println("|  ID  |     Họ tên     | Tuổi | Giới tính |      Chẩn đoán       |");
        System.out.println("+------+----------------+------+-----------+----------------------+");

        for (Patient p : patients) {
            System.out.printf("| %-4s | %-14s | %-4d | %-9s | %-20s |%n",
                    p.getId(),
                    p.getName(),
                    p.getAge(),
                    p.getGender(),
                    p.getDiagnosis());
        }

        System.out.println("+------+----------------+------+-----------+----------------------+");
    }

    public Patient findById(String id) {
        for (Patient p : getAllPatients()) {
            if (p.getId().equalsIgnoreCase(id)) return p;
        }
        return null;
    }

    public void updatePatient(String id, String newName, int newAge, String newGender, String newDiagnosis) {
        List<Patient> list = getAllPatients();
        boolean found = false;

        for (Patient p : list) {
            if (p.getId().equalsIgnoreCase(id)) {
                p.setName(newName);
                p.setAge(newAge);
                p.setGender(newGender);
                p.setDiagnosis(newDiagnosis);
                found = true;
                break;
            }
        }

        if (found) {
            saveAll(list);
            System.out.println("Đã cập nhật thông tin bệnh nhân ID: " + id);
        } else {
            System.out.println("Không tìm thấy bệnh nhân có ID: " + id);
        }
    }

    public void deletePatient(String id) {
        List<Patient> list = getAllPatients();
        boolean removed = list.removeIf(p -> p.getId().equalsIgnoreCase(id));
        if (removed) {
            saveAll(list);
            System.out.println("Đã xóa bệnh nhân ID: " + id);
        } else {
            System.out.println("Không tìm thấy bệnh nhân có ID: " + id);
        }
    }

    private void saveAll(List<Patient> patients) {
        List<String> lines = new ArrayList<>();
        for (Patient p : patients) lines.add(p.toString());
        FileUtil.writeFile(FILE_PATH, lines);
    }
}
