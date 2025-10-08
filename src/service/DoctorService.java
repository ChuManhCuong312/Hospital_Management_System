package service;

import exception.InvalidDataException;
import model.Doctor;
import util.Validator;
import util.FileUtil;

import java.util.*;
import java.util.stream.Collectors;

public class DoctorService {

    private List<Doctor> doctors = new ArrayList<>();
    private final String fileName = "data/doctors.txt";
    private final Scanner sc = new Scanner(System.in);

    public DoctorService() {
        loadFromFile();
    }

    // --- Thêm bác sĩ ---
    public void addDoctor() {
        try {
            Doctor d = inputDoctor();
            if (d == null) return;
            doctors.add(d);
            saveToFile();
            System.out.println("Thêm thành công.");
        } catch (InvalidDataException e) {
            System.out.println("Lỗi nhập liệu: " + e.getMessage());
        }
    }

    // --- Cập nhật bác sĩ ---
    public void updateDoctor() {
        viewAll();
        System.out.print("Nhập mã BS cần sửa: ");
        String ma = sc.nextLine().trim().toUpperCase();
        Doctor old = findByDoctorId(ma);
        if (old == null) {
            System.out.println("Không tìm thấy mã BS.");
            return;
        }
        Doctor updated = inputDoctorForUpdate(old);
        doctors.set(doctors.indexOf(old), updated);
        saveToFile();
        System.out.println("Cập nhật thành công.");
    }

    // --- Xóa bác sĩ ---
    public void deleteDoctor() {
        viewAll();
        System.out.print("Nhập mã BS cần xóa: ");
        String ma = sc.nextLine().trim().toUpperCase();
        Doctor d = findByDoctorId(ma);
        if (d == null) {
            System.out.println("Không tìm thấy mã BS.");
            return;
        }
        doctors.remove(d);
        saveToFile();
        System.out.println("Xóa thành công.");
    }

    // --- Xem danh sách ---
    public void viewAll() {
        if (doctors.isEmpty()) {
            System.out.println("Danh sách trống.");
            return;
        }
        printHeader();
        doctors.forEach(this::printDoctor);
    }

    // --- Tìm kiếm ---
    public void searchDoctor() {
        System.out.println("1. Tìm theo Mã");
        System.out.println("2. Tìm theo Tên");
        System.out.println("3. Tìm theo Chuyên khoa");
        System.out.print("Chọn: ");
        int choice;
        try {
            choice = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Vui lòng nhập số hợp lệ.");
            return;
        }

        switch (choice) {
            case 1 -> {
                System.out.print("Nhập mã BS: ");
                String ma = sc.nextLine().trim().toUpperCase();
                Doctor d = findByDoctorId(ma);
                if (d == null) System.out.println("Không tìm thấy.");
                else {
                    printHeader();
                    printDoctor(d);
                }
            }
            case 2 -> {
                System.out.print("Nhập tên: ");
                String name = sc.nextLine().trim();
                List<Doctor> list = findByName(name);
                showFiltered(list);
            }
            case 3 -> {
                System.out.print("Nhập chuyên khoa: ");
                String ck = sc.nextLine().trim();
                List<Doctor> list = findBySpecialty(ck);
                showFiltered(list);
            }
            default -> System.out.println("Chọn sai.");
        }
    }
    // --- Lọc ---
    public void filter() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhập chuyên khoa (Enter để bỏ qua): ");
        String specialty = sc.nextLine().trim();
        System.out.print("Nhập kinh nghiệm tối thiểu (Enter để bỏ qua): ");
        String knInput = sc.nextLine().trim();
        Integer minExperience = knInput.isEmpty() ? null : Integer.parseInt(knInput);

        List<Doctor> filtered = doctors.stream()
                .filter(d -> (specialty.isBlank() || d.getSpecialty().equalsIgnoreCase(specialty)))
                .filter(d -> (minExperience == null || d.getExperience() >= minExperience))
                .toList();

        if (filtered.isEmpty()) {
            System.out.println("Không tìm thấy.");
        } else {
            printHeader();
            filtered.forEach(this::printDoctor);
        }
    }



    // --- Xuất file ---
    public void exportToFile() {
        System.out.print("Nhập tên file xuất: ");
        String fileName = sc.nextLine().trim();
        List<String> lines = doctors.stream().map(Doctor::toDataString).toList();
        FileUtil.writeFile(fileName, lines);
        System.out.println("Xuất file thành công: " + fileName);
    }

    // --- Nhập file ---
    public void importFromFile() {
        System.out.print("Nhập tên file cần nhập: ");
        String fileName = sc.nextLine().trim();

        List<String> lines = FileUtil.readFile(fileName);
        if (lines.isEmpty()) {
            System.out.println("File rỗng hoặc không tồn tại.");
            return;
        }

        int added = 0, skipped = 0;
        for (String line : lines) {
            Doctor d = Doctor.fromString(line);
            if (d == null) { skipped++; continue; }
            boolean exists = findByDoctorId(d.getId()) != null;
            if (exists) { skipped++; continue; }
            doctors.add(d);
            added++;
        }
        saveToFile();
        System.out.printf("Import hoàn tất: %d thêm mới, %d bị lỗi.%n", added, skipped);
    }

    // --- Filter hỗ trợ menu ---
    private void showFiltered(List<Doctor> list) {
        if (list.isEmpty()) System.out.println("Không tìm thấy.");
        else {
            printHeader();
            list.forEach(this::printDoctor);
        }
    }

    // --- Input helper ---
    private Doctor inputDoctor() throws InvalidDataException {
        String ma, ten, ck, bc, sdt, email, lich, tt;
        int kn;

        // Mã BS
        while (true) {
            System.out.print("Mã BS (D + số): ");
            ma = sc.nextLine().trim().toUpperCase();
            if (!ma.matches("D\\d+")) {
                System.out.println("Mã không hợp lệ (vd: D001)");
                continue;
            }
            if (findByDoctorId(ma) != null) {
                System.out.println("Mã đã tồn tại.");
                continue;
            }
            break;
        }

        // Tên
        System.out.print("Tên: ");
        ten = sc.nextLine().trim();
        Validator.checkNotEmpty(ten, "Tên không được để trống");

        // Chuyên khoa
        System.out.print("Chuyên khoa: ");
        ck = sc.nextLine().trim();
        Validator.checkNotEmpty(ck, "Chuyên khoa không được để trống");

        // Bằng cấp
        while (true) {
            System.out.print("Bằng cấp (CKI/CKII): ");
            bc = sc.nextLine().trim().toUpperCase();
            if (!(bc.equals("CKI") || bc.equals("CKII"))) System.out.println("Bằng cấp không hợp lệ.");
            else break;
        }

        // Kinh nghiệm
        while (true) {
            System.out.print("Kinh nghiệm (năm): ");
            try {
                kn = Integer.parseInt(sc.nextLine().trim());
                if (kn < 0) System.out.println("Kinh nghiệm >= 0.");
                else break;
            } catch (NumberFormatException e) {
                System.out.println("Nhập số nguyên hợp lệ.");
            }
        }

        // SĐT
        while (true) {
            System.out.print("SĐT (10 số): ");
            String phone = sc.nextLine().trim();
            Validator.checkPhone(phone);
            boolean exists = doctors.stream().anyMatch(d -> d.getPhone().equals(phone));
            if (exists) System.out.println("SĐT đã tồn tại.");
            else {
                sdt = phone;
                break;
            }
        }

        // Email
        while (true) {
            System.out.print("Email: ");
            String emailInput = sc.nextLine().trim();
            Validator.checkEmail(emailInput);
            boolean exists = doctors.stream().anyMatch(d -> d.getEmail().equalsIgnoreCase(emailInput));
            if (exists) System.out.println("Email đã tồn tại.");
            else {
                email = emailInput;
                break;
            }
        }

        // Lịch làm việc
        while (true) {
            System.out.print("Lịch làm việc (HH:mm-HH:mm): ");
            lich = sc.nextLine().trim();
            String[] parts = lich.split("-");
            if (parts.length != 2) {
                System.out.println("Sai định dạng.");
                continue;
            }
            try {
                Validator.checkTime(parts[0]);
                Validator.checkTime(parts[1]);
                break;
            } catch (InvalidDataException e) {
                System.out.println(e.getMessage());
            }
        }


        // Trạng thái
        System.out.print("Trạng thái: ");
        tt = sc.nextLine().trim();

        return new Doctor(ma, ten, ck, bc, kn, sdt, email, lich, tt);
    }


    private Doctor inputDoctorForUpdate(Doctor old) {
        System.out.println("=== CẬP NHẬT (Enter để giữ nguyên) ===");

        System.out.print("Tên [" + old.getName() + "]: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) name = old.getName();

        System.out.print("Chuyên khoa [" + old.getSpecialty() + "]: ");
        String ck = sc.nextLine().trim();
        if (ck.isEmpty()) ck = old.getSpecialty();

        System.out.print("Bằng cấp [" + old.getDegree() + "]: ");
        String bc = sc.nextLine().trim();
        if (bc.isEmpty()) bc = old.getDegree();

        System.out.print("Kinh nghiệm [" + old.getExperience() + "]: ");
        String knInput = sc.nextLine().trim();
        int kn = knInput.isEmpty() ? old.getExperience() : Integer.parseInt(knInput);

        System.out.print("SĐT [" + old.getPhone() + "]: ");
        String sdt = sc.nextLine().trim();
        if (sdt.isEmpty()) sdt = old.getPhone();

        System.out.print("Email [" + old.getEmail() + "]: ");
        String email = sc.nextLine().trim();
        if (email.isEmpty()) email = old.getEmail();

        System.out.print("Lịch làm việc [" + old.getWorkSchedule() + "]: ");
        String lich = sc.nextLine().trim();
        if (lich.isEmpty()) lich = old.getWorkSchedule();

        System.out.print("Trạng thái [" + old.getStatus() + "]: ");
        String tt = sc.nextLine().trim();
        if (tt.isEmpty()) tt = old.getStatus();

        return new Doctor(old.getId(), name, ck, bc, kn, sdt, email, lich, tt);
    }

    // --- Helper tìm kiếm ---
    private Doctor findByDoctorId(String doctorId) {
        return doctors.stream().filter(d -> d.getId().equalsIgnoreCase(doctorId)).findFirst().orElse(null);
    }

    private List<Doctor> findByName(String name) {
        return doctors.stream().filter(d -> d.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());
    }

    private List<Doctor> findBySpecialty(String ck) {
        return doctors.stream().filter(d -> d.getSpecialty().equalsIgnoreCase(ck)).collect(Collectors.toList());
    }

    // --- In danh sách ---
    private void printHeader() {
        String line = "+--------+----------------------+----------------------+--------+-----+--------------+-----------------------2--------+----------------+-------------------+";
        System.out.println(line);
        System.out.printf("| %-6s | %-20s | %-20s | %-6s | %-3s | %-12s | %-30s | %-13s | %-17s |%n",
                "Mã BS", "Tên", "Chuyên khoa", "BC", "KN", "SĐT", "Email", "Lịch", "Trạng thái");
        System.out.println(line);
    }

    private void printDoctor(Doctor d) {
        System.out.printf("| %-6s | %-20s | %-20s | %-6s | %-3d | %-12s | %-30s | %-13s | %-17s |%n",
                d.getId(), d.getName(), d.getSpecialty(), d.getDegree(),
                d.getExperience(), d.getPhone(), d.getEmail(), d.getWorkSchedule(), d.getStatus());
    }

    // --- File ---
    private void loadFromFile() {
        doctors.clear();
        for (String line : FileUtil.readFile(fileName)) {
            Doctor d = Doctor.fromString(line);
            if (d != null) doctors.add(d);
        }
    }

    private void saveToFile() {
        List<String> lines = doctors.stream().map(Doctor::toDataString).toList();
        FileUtil.writeFile(fileName, lines);
    }
}
