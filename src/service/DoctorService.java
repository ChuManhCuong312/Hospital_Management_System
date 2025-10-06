package service;

import model.Doctor;
import util.FileUtil;
import java.util.*;

public class DoctorService {
    private static final String FILE_PATH = "data/doctors.txt";

    public void addDoctor(Doctor d) {
        FileUtil.appendToFile(FILE_PATH, d.toString());
        System.out.println("Đã thêm bác sĩ: " + d.getName());
    }

    public List<Doctor> getAllDoctors() {
        List<String> lines = FileUtil.readFile(FILE_PATH);
        List<Doctor> doctors = new ArrayList<>();
        for (String line : lines) {
            Doctor d = Doctor.fromString(line);
            if (d != null) doctors.add(d);
        }
        return doctors;
    }

    public void viewAll() {
        List<Doctor> doctors = getAllDoctors();
        if (doctors.isEmpty()) {
            System.out.println("Không có bác sĩ nào trong hệ thống!");
            return;
        }

        System.out.println("+------+---------------------+------------------------+------------+");
        System.out.println("|  ID  |       Họ tên        |      Chuyên khoa       |     SĐT    |");
        System.out.println("+------+---------------------+------------------------+------------+");

        for (Doctor d : doctors) {
            System.out.printf("| %-4s | %-19s | %-22s | %-9s |%n",
                    d.getId(),
                    d.getName(),
                    d.getSpecialization(),
                    d.getPhone());
        }

        System.out.println("+------+----------------+------------------++--------------+");
    }


    public Doctor findById(String id) {
        for (Doctor d : getAllDoctors()) {
            if (d.getId().equalsIgnoreCase(id)) return d;
        }
        return null;
    }

    public void deleteDoctor(String id) {
        List<Doctor> list = getAllDoctors();
        boolean removed = list.removeIf(d -> d.getId().equalsIgnoreCase(id));
        if (removed) {
            saveAll(list);
            System.out.println("Đã xóa bác sĩ ID: " + id);
        } else {
            System.out.println("ZKhông tìm thấy bác sĩ có ID: " + id);
        }
    }

    private void saveAll(List<Doctor> doctors) {
        List<String> lines = new ArrayList<>();
        for (Doctor d : doctors) lines.add(d.toString());
        FileUtil.writeFile(FILE_PATH, lines);
    }
}
