package service;

import model.Doctor;
import validator.DoctorValidator;
import utils.FileUtils;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DoctorService {

    private static final Logger logger = Logger.getLogger(DoctorService.class.getName());
    private List<Doctor> doctors = new ArrayList<>();
    private final String fileName = "doctors.txt";

    // CRUD
    public DoctorService() {
        loadFromFile();
    }

    public String addDoctorFromInput(Scanner sc) {
        Doctor d = inputDoctor(sc);
        if (d == null) return "Thêm thất bại.";
        return addDoctor(d);
    }

    public String updateDoctorFromInput(Scanner sc) {
        showAll();
        System.out.print("Nhập mã BS cần sửa: ");
        String ma = sc.nextLine().trim().toUpperCase();
        Doctor old = findByMaBS(ma);
        if (old == null) return "Không tìm thấy mã BS.";

        Doctor updated = inputDoctorForUpdate(sc, old);
        return updateDoctor(updated);
    }

    public String deleteDoctorFromInput(Scanner sc) {
        showAll();
        System.out.print("Nhập mã BS cần xóa: ");
        String ma = sc.nextLine().trim().toUpperCase();
        return deleteDoctor(ma);
    }

    // input
    private Doctor inputDoctor(Scanner sc) {
        String ma, ten, ck, bc, sdt, email, lich, tt;
        int kn;

        while (true) {
            System.out.print("Mã BS (D + số): ");
            ma = sc.nextLine().trim().toUpperCase();
            if (!DoctorValidator.isValidDoctorId(ma)) {
                System.out.println("Mã không hợp lệ (vd: D001)");
                continue;
            }
            if (findByMaBS(ma) != null) {
                System.out.println("Mã đã tồn tại.");
                continue;
            }
            break;
        }

        System.out.print("Tên: ");
        ten = sc.nextLine().trim();

        System.out.print("Chuyên khoa: ");
        ck = sc.nextLine().trim();
        ck = DoctorValidator.normalizeSpecialty(ck);

        while (true) {
            System.out.print("Bằng cấp (CKI/CKII): ");
            bc = sc.nextLine().trim().toUpperCase();
            if (!DoctorValidator.isValidDegree(bc)) {
                System.out.println("Bằng cấp không hợp lệ. Chỉ được nhập CKI hoặc CKII.");
            } else {
                break;
            }
        }

        while (true) {
            System.out.print("Kinh nghiệm (năm): ");
            String knInput = sc.nextLine().trim();
            try {
                kn = Integer.parseInt(knInput);
                if (!DoctorValidator.isValidExperience(kn)) {
                    System.out.println("Kinh nghiệm phải >= 0.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Kinh nghiệm phải là số nguyên.");
            }
        }

        while (true) {
            System.out.print("SĐT (10 số): ");
            sdt = sc.nextLine().trim();
            if (!DoctorValidator.isValidPhone(sdt)) {
                System.out.println("SĐT không hợp lệ. Phải gồm đúng 10 chữ số.");
                continue;
            }
            if (DoctorValidator.isPhoneExist(sdt, doctors, "")) {
                System.out.println("SĐT đã tồn tại.");
                continue;
            }
            break;
        }

        while (true) {
            System.out.print("Email (@example.com): ");
            email = sc.nextLine().trim();
            if (!DoctorValidator.isValidEmail(email)) {
                System.out.println("Email không hợp lệ. Phải có dạng tên@example.com.");
                continue;
            }
            if (DoctorValidator.isEmailExist(email, doctors, "")) {
                System.out.println("Email đã tồn tại.");
                continue;
            }
            break;
        }

        while (true) {
            System.out.print("Lịch làm việc (HH:mm-HH:mm): ");
            lich = sc.nextLine().trim();
            if (!DoctorValidator.isValidWorkSchedule(lich)) {
                System.out.println("Lịch làm việc sai định dạng. Ví dụ: 08:00-16:00");
                continue;
            }
            break;
        }

        while (true) {
            System.out.print("Trạng thái (Hoạt động/Không hoạt động): ");
            tt = sc.nextLine().trim();
            if (DoctorValidator.validateAndNormalizeStatus(tt) == null) {
                System.out.println("Trạng thái không hợp lệ.");
                continue;
            }
            tt = DoctorValidator.validateAndNormalizeStatus(tt);
            break;
        }

        Doctor d = new Doctor(ma, ten, ck, bc, kn, sdt, email, lich, tt);
        String err = DoctorValidator.validateDoctor(d, doctors, "", "");
        if (err != null) {
            System.out.println("Lỗi: " + err);
            return null;
        }
        return d;
    }

    private Doctor inputDoctorForUpdate(Scanner sc, Doctor old) {
        System.out.println("=== CẬP NHẬT (Enter để giữ nguyên) ===");

        System.out.print("Tên [" + old.getName() + "]: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) name = old.getName();

        System.out.print("Chuyên khoa [" + old.getSpecialty() + "]: ");
        String ck = sc.nextLine().trim();
        if (ck.isEmpty()) ck = old.getSpecialty();
        else ck = DoctorValidator.normalizeSpecialty(ck);

        while (true) {
            System.out.print("Bằng cấp [" + old.getDegree() + "]: ");
            String bc = sc.nextLine().trim();
            if (bc.isEmpty()) bc = old.getDegree();
            if (!DoctorValidator.isValidDegree(bc)) {
                System.out.println("Bằng cấp không hợp lệ. Chỉ được nhập CKI hoặc CKII.");
                continue;
            }
            bc = bc.toUpperCase();
            old.setDegree(bc);

            break;
        }

        int kn;
        while (true) {
            System.out.print("Kinh nghiệm [" + old.getExperience() + "]: ");
            String knInput = sc.nextLine().trim();
            if (knInput.isEmpty()) {
                kn = old.getExperience();
                break;
            }
            try {
                kn = Integer.parseInt(knInput);
                if (!DoctorValidator.isValidExperience(kn)) {
                    System.out.println("Kinh nghiệm phải >= 0.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Kinh nghiệm phải là số nguyên.");
            }
        }

        while (true) {
            System.out.print("SĐT [" + old.getPhone() + "]: ");
            String sdt = sc.nextLine().trim();
            if (sdt.isEmpty()) sdt = old.getPhone();
            if (!DoctorValidator.isValidPhone(sdt)) {
                System.out.println("SĐT không hợp lệ.");
                continue;
            }
            if (DoctorValidator.isPhoneExist(sdt, doctors, old.getPhone())) {
                System.out.println("SĐT đã tồn tại.");
                continue;
            }
            old.setPhone(sdt);
            break;
        }

        while (true) {
            System.out.print("Email [" + old.getEmail() + "]: ");
            String email = sc.nextLine().trim();
            if (email.isEmpty()) email = old.getEmail();
            if (!DoctorValidator.isValidEmail(email)) {
                System.out.println("Email không hợp lệ.");
                continue;
            }
            if (DoctorValidator.isEmailExist(email, doctors, old.getEmail())) {
                System.out.println("Email đã tồn tại.");
                continue;
            }
            old.setEmail(email);
            break;
        }

        while (true) {
            System.out.print("Lịch làm việc [" + old.getWorkSchedule() + "]: ");
            String lich = sc.nextLine().trim();
            if (lich.isEmpty()) lich = old.getWorkSchedule();
            if (!DoctorValidator.isValidWorkSchedule(lich)) {
                System.out.println("Lịch làm việc sai định dạng.");
                continue;
            }
            old.setWorkSchedule(lich);
            break;
        }

        while (true) {
            System.out.print("Trạng thái [" + old.getStatus() + "]: ");
            String tt = sc.nextLine().trim();
            if (tt.isEmpty()) tt = old.getStatus();
            if (DoctorValidator.validateAndNormalizeStatus(tt) == null) {
                System.out.println("Trạng thái không hợp lệ.");
                continue;
            }
            tt = DoctorValidator.validateAndNormalizeStatus(tt);
            old.setStatus(tt);
            break;
        }

        return new Doctor(old.getDoctorId(), name, ck, old.getDegree(), kn, old.getPhone(), old.getEmail(), old.getWorkSchedule(), old.getStatus());
    }

    // hiển thị
    public void showAll() {
        if (doctors.isEmpty()) {
            System.out.println("Danh sách trống.");
            return;
        }
        printHeader();
        doctors.forEach(this::printDoctor);
    }

    public void showFiltered(List<Doctor> list) {
        if (list.isEmpty()) System.out.println("Không tìm thấy");
        else {
            printHeader();
            list.forEach(this::printDoctor);
        }
    }

    private void printHeader() {
        String line = "+--------+----------------------+----------------------+--------+-----+--------------+---------------------------+---------------+-------------------+";
        System.out.println(line);
        System.out.printf("| %-6s | %-20s | %-20s | %-6s | %-3s | %-12s | %-25s | %-13s | %-17s |%n",
                "Mã BS", "Tên", "Chuyên khoa", "BC", "KN", "SĐT", "Email", "Lịch", "Trạng thái");
        System.out.println(line);
    }

    private void printDoctor(Doctor d) {
        System.out.printf("| %-6s | %-20s | %-20s | %-6s | %-3d | %-12s | %-25s | %-13s | %-17s |%n",
                d.getDoctorId(), d.getName(), d.getSpecialty(), d.getDegree(),
                d.getExperience(), d.getPhone(), d.getEmail(), d.getWorkSchedule(), d.getStatus());
    }

    // tìm kiếm
    public void findByIdFromInput(Scanner sc) {
        System.out.print("Nhập mã BS: ");
        String ma = sc.nextLine().trim().toUpperCase();
        Doctor d = findByMaBS(ma);
        if (d == null) System.out.println("Không tìm thấy");
        else printDoctor(d);
    }

    public void findByNameFromInput(Scanner sc) {
        System.out.print("Nhập tên: ");
        String name = sc.nextLine().trim();
        showFiltered(findByName(name));
    }

    public void findBySpecialtyFromInput(Scanner sc) {
        System.out.print("Nhập chuyên khoa: ");
        String ck = sc.nextLine().trim();
        showFiltered(findBySpecialty(ck));
    }

    // core logic
    public String addDoctor(Doctor d) {
        String err = DoctorValidator.validateDoctor(d, doctors, "", "");
        if (err != null) return err;
        doctors.add(d);
        return saveToFile() ? "Thêm thành công" : "Lỗi ghi file";
    }

    public String updateDoctor(Doctor updated) {
        Doctor old = findByMaBS(updated.getDoctorId());
        if (old == null) return "Không tìm thấy mã BS";
        String err = DoctorValidator.validateDoctor(updated, doctors, old.getPhone(), old.getEmail());
        if (err != null) return err;
        doctors.set(doctors.indexOf(old), updated);
        return saveToFile() ? "Cập nhật thành công" : "Lỗi ghi file";
    }

    public String deleteDoctor(String maBS) {
        Doctor d = findByMaBS(maBS);
        if (d == null) return "Không tìm thấy mã BS";
        doctors.remove(d);
        return saveToFile() ? "Xóa thành công" : "Lỗi ghi file";
    }

    public Doctor findByMaBS(String maBS) {
        return doctors.stream().filter(d -> d.getDoctorId().equalsIgnoreCase(maBS)).findFirst().orElse(null);
    }

    public List<Doctor> findByName(String name) {
        return doctors.stream().filter(d -> d.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());
    }

    public List<Doctor> findBySpecialty(String ck) {
        return doctors.stream().filter(d -> d.getSpecialty().equalsIgnoreCase(ck)).collect(Collectors.toList());
    }

    public List<Doctor> filter(String ck, Integer minKN) {
        return doctors.stream()
                .filter(d -> (ck == null || ck.isBlank() || d.getSpecialty().equalsIgnoreCase(ck)))
                .filter(d -> (minKN == null || d.getExperience() >= minKN))
                .collect(Collectors.toList());
    }

    // file
    public boolean exportToFile(String exportFileName) {
        List<String> lines = doctors.stream().map(Doctor::toFileString).toList();
        return FileUtils.writeLines(exportFileName, lines);
    }

    public String importFromFile(String importFileName) {
        List<String> lines = FileUtils.readAllLines(importFileName);
        if (lines.isEmpty()) return "File rỗng hoặc không tồn tại.";

        int added = 0, skipped = 0;
        for (String line : lines) {
            Doctor d = Doctor.fromFileString(line);
            if (d == null) { skipped++; continue; }
            String err = DoctorValidator.validateDoctor(d, doctors, "", "");
            if (err != null) { skipped++; continue; }
            doctors.add(d);
            added++;
        }
        saveToFile();
        return String.format("Import hoàn tất: %d thêm mới, %d bị lỗi.", added, skipped);
    }

    public void loadFromFile() {
        doctors.clear();
        for (String line : FileUtils.readAllLines(fileName)) {
            Doctor d = Doctor.fromFileString(line);
            if (d != null) doctors.add(d);
        }
    }

    public boolean saveToFile() {
        List<String> lines = doctors.stream().map(Doctor::toFileString).toList();
        return FileUtils.writeLines(fileName, lines);
    }
}
