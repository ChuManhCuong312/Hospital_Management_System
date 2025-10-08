package service;

import exception.InvalidDataException;
import model.Appointment;
import util.FileUtil;
import util.InputUtil;
import util.Validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AppointmentService {
    private static final String FILE_PATH = "data/appointments.txt";
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public List<Appointment> getAll() {
        List<String> lines = FileUtil.readFile(FILE_PATH);
        List<Appointment> list = new ArrayList<>();
        for (String l : lines) {
            Appointment a = Appointment.fromString(l);
            if (a != null) list.add(a);
        }
        return list;
    }

    private void saveAll(List<Appointment> list) {
        List<String> lines = new ArrayList<>();
        for (Appointment a : list) lines.add(a.toDataString());
        FileUtil.writeFile(FILE_PATH, lines);
    }

    // ====================== THÊM LỊCH HẸN ======================
    public void addAppointment() {
        try {
            System.out.println("===== THÊM LỊCH HẸN =====");
            String id = InputUtil.nhapChuoi("Nhập mã lịch hẹn: ");
            Validator.checkNotEmpty(id, "Mã lịch hẹn không được để trống!");
            if (findById(id) != null)
                throw new InvalidDataException("Mã lịch hẹn đã tồn tại!");

            String patientId = InputUtil.nhapChuoi("Nhập mã bệnh nhân: ");
            Validator.checkNotEmpty(patientId, "Mã bệnh nhân không được để trống!");

            String doctorId = InputUtil.nhapChuoi("Nhập mã bác sĩ: ");
            Validator.checkNotEmpty(doctorId, "Mã bác sĩ không được để trống!");

            String dateInput = InputUtil.nhapChuoi("Nhập ngày (dd/MM/yyyy): ");
            Validator.checkDate(dateInput);
            LocalDate date = LocalDate.parse(dateInput, INPUT_FORMAT);
            String dateFormatted = date.toString(); // yyyy-MM-dd

            String time = InputUtil.nhapChuoi("Nhập giờ (HH:mm): ");
            Validator.checkTime(time);

            String purpose = InputUtil.nhapChuoi("Nhập mục đích khám: ");
            Validator.checkNotEmpty(purpose, "Mục đích không được để trống!");

            Appointment a = new Appointment(id, patientId, doctorId, dateFormatted, time, purpose);
            FileUtil.appendToFile(FILE_PATH, a.toDataString());
            System.out.println("Đã thêm lịch hẹn: " + id);
        } catch (InvalidDataException e) {
            System.out.println("Lỗi: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Lỗi khi thêm lịch hẹn: " + e.getMessage());
        }
    }

    // ====================== XEM DANH SÁCH ======================
    public void viewAll() {
        List<Appointment> list = getAll();
        if (list.isEmpty()) {
            System.out.println("Không có lịch hẹn nào!");
            return;
        }

        System.out.println("========================================================================================================================================");
        System.out.printf("| %-8s | %-8s | %-8s | %-12s | %-6s | %-20s | %-12s | %-8s | %-25s |%n",
                "Mã LH", "Mã BN", "Mã BS", "Ngày", "Giờ", "Mục đích", "Trạng thái", "Phí", "Ghi chú");
        System.out.println("========================================================================================================================================");
        for (Appointment a : list) {
            System.out.printf("| %-8s | %-8s | %-8s | %-12s | %-6s | %-20s | %-12s | %-8s | %-25s |%n",
                    a.getId(), a.getPatientId(), a.getDoctorId(), a.getDate(), a.getTime(),
                    a.getPurpose(), a.getStatus(), a.getFee(), a.getNote());
        }
        System.out.println("========================================================================================================================================");
    }

    public Appointment findById(String id) {
        for (Appointment a : getAll()) {
            if (a.getId().equalsIgnoreCase(id)) return a;
        }
        return null;
    }

    // ====================== CẬP NHẬT ======================
    public void updateAppointment() {
        try {
            System.out.println("===== CẬP NHẬT LỊCH HẸN =====");
            String id = InputUtil.nhapChuoi("Nhập mã lịch hẹn cần cập nhật: ");
            List<Appointment> list = getAll();
            boolean found = false;

            for (Appointment a : list) {
                if (a.getId().equalsIgnoreCase(id)) {
                    String patientId = InputUtil.nhapChuoi("Mã BN (" + a.getPatientId() + "): ");
                    String doctorId = InputUtil.nhapChuoi("Mã BS (" + a.getDoctorId() + "): ");
                    String date = InputUtil.nhapChuoi("Ngày (" + a.getDate() + ") [dd/MM/yyyy]: ");
                    String time = InputUtil.nhapChuoi("Giờ (" + a.getTime() + "): ");
                    String purpose = InputUtil.nhapChuoi("Mục đích (" + a.getPurpose() + "): ");
                    String status = InputUtil.nhapChuoi("Trạng thái (" + a.getStatus() + "): ");
                    double fee = InputUtil.nhapSoThuc("Phí (" + a.getFee() + "): ");
                    String note = InputUtil.nhapChuoi("Ghi chú (" + a.getNote() + "): ");

                    if (!patientId.isEmpty()) a.setPatientId(patientId);
                    if (!doctorId.isEmpty()) a.setDoctorId(doctorId);
                    if (!date.isEmpty()) {
                        Validator.checkDate(date);
                        LocalDate d = LocalDate.parse(date, INPUT_FORMAT);
                        a.setDate(d.toString());
                    }
                    if (!time.isEmpty()) {
                        Validator.checkTime(time);
                        a.setTime(time);
                    }
                    if (!purpose.isEmpty()) a.setPurpose(purpose);
                    if (!status.isEmpty()) a.setStatus(status);
                    if (fee >= 0) a.setFee(fee);
                    if (!note.isEmpty()) a.setNote(note);

                    found = true;
                    break;
                }
            }

            if (found) {
                saveAll(list);
                System.out.println("Đã cập nhật lịch hẹn: " + id);
            } else System.out.println("Không tìm thấy lịch hẹn!");
        } catch (InvalidDataException e) {
            System.out.println("Lỗi: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật lịch hẹn: " + e.getMessage());
        }
    }

    // ====================== XÓA ======================
    public void deleteAppointment() {
        try {
            System.out.println("===== XÓA LỊCH HẸN =====");
            String id = InputUtil.nhapChuoi("Nhập mã lịch hẹn cần xóa: ");
            List<Appointment> list = getAll();
            boolean removed = list.removeIf(a -> a.getId().equalsIgnoreCase(id));
            if (removed) {
                saveAll(list);
                System.out.println("🗑️ Đã xóa lịch hẹn: " + id);
            } else System.out.println("Không tìm thấy lịch hẹn!");
        } catch (Exception e) {
            System.out.println("Lỗi khi xóa lịch hẹn: " + e.getMessage());
        }
    }

    // ====================== TÌM KIẾM ======================
    public void searchAppointment() {
        try {
            System.out.println("===== TÌM KIẾM LỊCH HẸN =====");
            System.out.println("1. Theo mã bệnh nhân");
            System.out.println("2. Theo mã bác sĩ");
            System.out.println("3. Theo ngày (dd/MM/yyyy)");
            int c = InputUtil.nhapLuaChon("Chọn: ", 1, 3);
            String key = InputUtil.nhapChuoi("Nhập từ khóa: ");

            if (c == 3) {
                Validator.checkDate(key);
                LocalDate d = LocalDate.parse(key, INPUT_FORMAT);
                key = d.toString();
            }

            List<Appointment> res = new ArrayList<>();
            for (Appointment a : getAll()) {
                if (c == 1 && a.getPatientId().equalsIgnoreCase(key)) res.add(a);
                else if (c == 2 && a.getDoctorId().equalsIgnoreCase(key)) res.add(a);
                else if (c == 3 && a.getDate().equalsIgnoreCase(key)) res.add(a);
            }

            if (res.isEmpty()) System.out.println("Không tìm thấy lịch hẹn phù hợp!");
            else {
                System.out.println("Kết quả:");
                for (Appointment a : res)
                    System.out.println(a.toDataString());
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi tìm kiếm lịch hẹn: " + e.getMessage());
        }
    }
}
