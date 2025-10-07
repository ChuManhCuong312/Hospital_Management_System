package service;

import exception.InvalidDataException;
import model.Doctor;
import util.FileUtil;
import util.InputUtil;
import util.Validator;

import java.util.ArrayList;
import java.util.List;

public class DoctorService {
    private static final String FILE_PATH = "data/doctors.txt";

    public List<Doctor> getAll() {
        List<String> lines = FileUtil.readFile(FILE_PATH);
        List<Doctor> list = new ArrayList<>();
        for (String l : lines) {
            Doctor d = Doctor.fromString(l);
            if (d != null) list.add(d);
        }
        return list;
    }

    private void saveAll(List<Doctor> list) {
        List<String> lines = new ArrayList<>();
        for (Doctor d : list) lines.add(d.toDataString());
        FileUtil.writeFile(FILE_PATH, lines);
    }

    // Thêm bác sĩ (interactive)
    public void addDoctor() {
        try {
            System.out.println("===== THÊM BÁC SĨ =====");
            String id = InputUtil.nhapChuoi("Nhập mã bác sĩ: ");
            Validator.checkNotEmpty(id, "Mã bác sĩ không được để trống!");
            if (findById(id) != null) throw new InvalidDataException("Mã bác sĩ đã tồn tại!");

            String name = InputUtil.nhapChuoi("Nhập họ tên: ");
            Validator.checkNotEmpty(name, "Tên không được để trống!");

            String specialty = InputUtil.nhapChuoi("Nhập chuyên khoa: ");
            Validator.checkNotEmpty(specialty, "Chuyên khoa không được để trống!");

            String phone = InputUtil.nhapChuoi("Nhập số điện thoại: ");
            if (!phone.isEmpty()) Validator.checkPhone(phone);

            String email = InputUtil.nhapChuoi("Nhập email: ");
            if (!email.isEmpty()) Validator.checkEmail(email);

            String gender = InputUtil.nhapChuoi("Nhập giới tính (Nam/Nữ): ");
            if (!gender.isEmpty()) Validator.checkGender(gender);

            int age = InputUtil.nhapSoNguyen("Nhập tuổi: ");
            if (age > 0) Validator.checkPositive(age, "Tuổi phải lớn hơn 0!");

            String department = InputUtil.nhapChuoi("Nhập khoa/phòng: ");
            String status = InputUtil.nhapChuoi("Nhập trạng thái: ");

            Doctor d = new Doctor(id, name, specialty, phone, email, gender, age, department, status);
            FileUtil.appendToFile(FILE_PATH, d.toDataString());
            System.out.println("Đã thêm bác sĩ: " + name);
        } catch (InvalidDataException e) {
            System.out.println("Lỗi: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Lỗi khi thêm bác sĩ: " + e.getMessage());
        }
    }

    // Thêm bác sĩ từ object (non-interactive)
    public void addDoctor(Doctor d) {
        try {
            Validator.checkNotEmpty(d.getId(), "Mã bác sĩ không được để trống!");
            if (findById(d.getId()) != null) throw new InvalidDataException("Mã bác sĩ đã tồn tại!");
            Validator.checkNotEmpty(d.getName(), "Tên không được để trống!");
            Validator.checkNotEmpty(d.getSpecialty(), "Chuyên khoa không được để trống!");
            if (d.getPhone() != null && !d.getPhone().isEmpty()) Validator.checkPhone(d.getPhone());
            if (d.getEmail() != null && !d.getEmail().isEmpty()) Validator.checkEmail(d.getEmail());
            FileUtil.appendToFile(FILE_PATH, d.toDataString());
            System.out.println("Đã thêm bác sĩ: " + d.getName());
        } catch (InvalidDataException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    public void viewAll() {
        List<Doctor> list = getAll();
        if (list.isEmpty()) {
            System.out.println("Không có bác sĩ nào!");
            return;
        }
        System.out.println("===========================================================================================================================================================");
        System.out.printf("| %-8s | %-20s | %-15s | %-12s | %-25s | %-10s | %-4s | %-20s |  %-12s |%n",
                "Mã BS", "Họ tên", "Chuyên khoa", "SĐT", "Email", "Giới tính", "Tuổi", "Khoa", "Trạng thái");
        System.out.println("===========================================================================================================================================================");
        for (Doctor d : list) {
            System.out.printf("| %-8s | %-20s | %-15s | %-12s | %-25s | %-10s | %-4d | %-20s | %-12s |%n",
                    d.getId(), d.getName(), d.getSpecialty(), d.getPhone(), d.getEmail(),
                    d.getGender(), d.getAge(), d.getDepartment(), d.getStatus());
        }
        System.out.println("===========================================================================================================================================================");
    }

    public Doctor findById(String id) {
        for (Doctor d : getAll()) if (d.getId().equalsIgnoreCase(id)) return d;
        return null;
    }

    // Cập nhật bác sĩ (interactive)
    public void updateDoctor() {
        try {
            System.out.println("===== CẬP NHẬT BÁC SĨ =====");
            String id = InputUtil.nhapChuoi("Nhập mã bác sĩ cần cập nhật: ");
            List<Doctor> list = getAll();
            boolean found = false;
            for (Doctor d : list) {
                if (d.getId().equalsIgnoreCase(id)) {
                    String name = InputUtil.nhapChuoi("Tên (" + d.getName() + "): ");
                    String specialty = InputUtil.nhapChuoi("Chuyên khoa (" + d.getSpecialty() + "): ");
                    String phone = InputUtil.nhapChuoi("SĐT (" + d.getPhone() + "): ");
                    String email = InputUtil.nhapChuoi("Email (" + d.getEmail() + "): ");
                    String gender = InputUtil.nhapChuoi("Giới tính (" + d.getGender() + "): ");
                    int age = InputUtil.nhapSoNguyen("Tuổi (" + d.getAge() + "): ");
                    String department = InputUtil.nhapChuoi("Khoa (" + d.getDepartment() + "): ");
                    String status = InputUtil.nhapChuoi("Trạng thái (" + d.getStatus() + "): ");

                    if (!name.isEmpty()) d.setName(name);
                    if (!specialty.isEmpty()) d.setSpecialty(specialty);
                    if (!phone.isEmpty()) { Validator.checkPhone(phone); d.setPhone(phone); }
                    if (!email.isEmpty()) { Validator.checkEmail(email); d.setEmail(email); }
                    if (!gender.isEmpty()) d.setGender(gender);
                    if (age > 0) d.setAge(age);
                    if (!department.isEmpty()) d.setDepartment(department);
                    if (!status.isEmpty()) d.setStatus(status);

                    found = true;
                    break;
                }
            }
            if (found) {
                saveAll(list);
                System.out.println("Đã cập nhật bác sĩ: " + id);
            } else System.out.println("Không tìm thấy bác sĩ!");
        } catch (InvalidDataException e) {
            System.out.println("Lỗi: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật bác sĩ: " + e.getMessage());
        }
    }

    public void deleteDoctor() {
        try {
            System.out.println("===== XÓA BÁC SĨ =====");
            String id = InputUtil.nhapChuoi("Nhập mã bác sĩ cần xóa: ");
            List<Doctor> list = getAll();
            boolean removed = list.removeIf(d -> d.getId().equalsIgnoreCase(id));
            if (removed) {
                saveAll(list);
                System.out.println("Đã xóa bác sĩ: " + id);
            } else System.out.println("Không tìm thấy bác sĩ!");
        } catch (Exception e) {
            System.out.println("Lỗi khi xóa bác sĩ: " + e.getMessage());
        }
    }

    // Tìm kiếm bác sĩ (mã / tên / chuyên khoa)
    public void searchDoctor() {
        try {
            System.out.println("===== TÌM KIẾM BÁC SĨ =====");
            System.out.println("1. Theo mã");
            System.out.println("2. Theo tên");
            System.out.println("3. Theo chuyên khoa");
            int c = InputUtil.nhapLuaChon("Chọn: ", 1, 3);
            String key = InputUtil.nhapChuoi("Nhập từ khóa: ");
            List<Doctor> res = new ArrayList<>();
            for (Doctor d : getAll()) {
                if (c == 1 && d.getId().equalsIgnoreCase(key)) res.add(d);
                else if (c == 2 && d.getName().toLowerCase().contains(key.toLowerCase())) res.add(d);
                else if (c == 3 && d.getSpecialty().toLowerCase().contains(key.toLowerCase())) res.add(d);
            }
            if (res.isEmpty()) System.out.println("Không tìm thấy bác sĩ phù hợp!");
            else {
                System.out.println("Kết quả:");
                for (Doctor d : res) System.out.println(d.toDataString());
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi tìm kiếm bác sĩ: " + e.getMessage());
        }
    }
}
