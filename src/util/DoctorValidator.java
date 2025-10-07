package util;

import model.Doctor;
import java.util.List;
import java.util.regex.Pattern;

public class DoctorValidator {

    public static boolean isValidDoctorId(String doctorId) {
        return doctorId != null && doctorId.trim().toUpperCase().matches("D\\d+");
    }

    public static boolean isDoctorIdExist(String doctorId, List<Doctor> list) {
        return list.stream().anyMatch(d -> d.getDoctorId().equalsIgnoreCase(doctorId.trim()));
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("\\d{10}");
    }

    public static boolean isPhoneExist(String phone, List<Doctor> list, String oldPhone) {
        return list.stream().anyMatch(d -> d.getPhone().equals(phone) && !d.getPhone().equals(oldPhone));
    }

    public static boolean isValidEmail(String email) {
        return email != null && Pattern.matches("^[A-Za-z0-9+_.-]*@example\\.com$", email);
    }

    public static boolean isEmailExist(String email, List<Doctor> list, String oldEmail) {
        return list.stream().anyMatch(d -> d.getEmail().equalsIgnoreCase(email) &&
                !d.getEmail().equalsIgnoreCase(oldEmail));
    }

    public static boolean isValidWorkSchedule(String schedule) {
        return schedule != null && schedule.matches("([01]?\\d|2[0-3]):[0-5]\\d-([01]?\\d|2[0-3]):[0-5]\\d");
    }

    public static String validateAndNormalizeStatus(String tt) {
        if (tt == null) return null;
        tt = tt.trim().toLowerCase();
        if (tt.equals("hoạt động") || tt.equals("hoat dong")) return "Hoạt động";
        if (tt.equals("không hoạt động") || tt.equals("khong hoat dong")) return "Không hoạt động";
        return null;
    }

    public static boolean isValidDegree(String bc) {
        if (bc == null) return false;
        bc = bc.trim().toUpperCase();
        return bc.equals("CKI") || bc.equals("CKII");
    }

    public static String normalizeSpecialty(String ck) {
        if (ck == null || ck.isEmpty()) return "";
        ck = ck.trim().toLowerCase();
        return ck.substring(0, 1).toUpperCase() + ck.substring(1);
    }

    public static boolean isValidExperience(int kn) {
        return kn >= 0;
    }

    // Validate toàn bộ Doctor trước khi thêm/sửa
    public static String validateDoctor(Doctor d, List<Doctor> list, String oldSdt, String oldEmail) {
        if (d == null) return "Doctor null";
        if (!isValidDoctorId(d.getDoctorId())) return "Mã bác sĩ không hợp lệ (BS + số)";
        if (isDoctorIdExist(d.getDoctorId(), list) && oldSdt.isEmpty() && oldEmail.isEmpty()) return "Mã bác sĩ đã tồn tại";
        if (!isValidDegree(d.getDegree())) return "Bằng cấp không hợp lệ (CKI/CKII)";
        if (!isValidExperience(d.getExperience())) return "Kinh nghiệm phải >= 0";
        if (!isValidPhone(d.getPhone())) return "SĐT không hợp lệ";
        if (isPhoneExist(d.getPhone(), list, oldSdt)) return "SĐT đã tồn tại";
        if (!isValidEmail(d.getEmail())) return "Email không hợp lệ";
        if (isEmailExist(d.getEmail(), list, oldEmail)) return "Email đã tồn tại";
        if (!isValidWorkSchedule(d.getWorkSchedule())) return "Lịch làm việc sai định dạng";
        if (validateAndNormalizeStatus(d.getStatus()) == null) return "Trạng thái không hợp lệ";
        return null;
    }
}
