package service;

import model.Appointment;
import util.FileUtil;
import util.InputUtil;

import java.util.*;

public class AppointmentService {
    private static final String FILE_PATH = "data/appointments.txt";

    public void addAppointment(Appointment a) {
        FileUtil.appendToFile(FILE_PATH, a.toString());
        System.out.println("✅ Đã tạo lịch hẹn ID: " + a.getId());
    }

    public List<Appointment> getAllAppointments() {
        List<String> lines = FileUtil.readFile(FILE_PATH);
        List<Appointment> appointments = new ArrayList<>();
        for (String line : lines) {
            Appointment a = Appointment.fromString(line);
            if (a != null) appointments.add(a);
        }
        return appointments;
    }

    public void viewAll() {
        List<Appointment> list = getAllAppointments();
        if (list.isEmpty()) {
            System.out.println("⚠️  Chưa có lịch hẹn nào.");
            return;
        }

        // Column headers
        String[] headers = {
                "ID", "Mã BN", "Mã BS", "Ngày", "Triệu chứng",
                "Chẩn đoán", "Đơn thuốc", "Chi phí", "Trạng thái"
        };

        // Convert appointments to list of string arrays
        List<String[]> rows = new ArrayList<>();
        for (Appointment a : list) {
            rows.add(new String[]{
                    String.valueOf(a.getId()),
                    String.valueOf(a.getPatientId()),
                    String.valueOf(a.getDoctorId()),
                    String.valueOf(a.getDate()),
                    String.valueOf(a.getSymptom()),
                    String.valueOf(a.getDiagnose()),
                    String.valueOf(a.getPrescription()),
                    String.valueOf(a.getCost()),
                    String.valueOf(a.getStatus())
            });
        }

        // --- Determine max column widths ---
        int[] colWidths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            colWidths[i] = headers[i].length();
        }

        for (String[] row : rows) {
            for (int i = 0; i < headers.length; i++) {
                if (row[i] != null && row[i].length() > colWidths[i]) {
                    colWidths[i] = row[i].length();
                }
            }
        }

        // Add small padding
        for (int i = 0; i < colWidths.length; i++) {
            colWidths[i] += 2;
        }

        // --- Build border dynamically ---
        StringBuilder border = new StringBuilder("+");
        for (int w : colWidths) {
            border.append("-".repeat(w + 2)).append("+");
        }

        // --- Helper: print a row ---
        java.util.function.Consumer<String[]> printRow = row -> {
            System.out.print("|");
            for (int i = 0; i < headers.length; i++) {
                String value = row[i] != null ? row[i] : "";
                System.out.printf(" %-"+(colWidths[i]+1)+"s|", value);
            }
            System.out.println();
        };

        // --- Print full table ---
        System.out.println();
        System.out.println(border);
        printRow.accept(headers);
        System.out.println(border);
        for (String[] row : rows) {
            printRow.accept(row);
        }
        System.out.println(border);
    }


    public void editAppointment() {
        String filePath = "data/appointments.txt";
        List<String> lines = FileUtil.readFile(filePath);

        if (lines.isEmpty()) {
            System.out.println("⚠️  Không có lịch hẹn nào trong hệ thống!");
            return;
        }

        viewAll();
        String idToEdit = InputUtil.inputString("Nhập ID lịch hẹn cần sửa: ");
        boolean found = false;
        List<String> updatedLines = new ArrayList<>();

        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length < 9) {
                updatedLines.add(line);
                continue;
            }

            String currentId = parts[0].trim();

            if (currentId.equalsIgnoreCase(idToEdit)) {
                found = true;

                // --- Nhập thông tin mới ---
                String patientId = InputUtil.inputString("ID bệnh nhân: ");
                if (!FileUtil.idExistsInFile("data/patients.txt", patientId)) {
                    System.out.println("❌ Lỗi: ID bệnh nhân không tồn tại trong hệ thống!");
                    return;
                }

                String doctorId = InputUtil.inputString("ID bác sĩ: ");
                if (!FileUtil.idExistsInFile("data/doctors.txt", doctorId)) {
                    System.out.println("❌ Lỗi: ID bác sĩ không tồn tại trong hệ thống!");
                    return;
                }

                String date = InputUtil.inputString("Ngày hẹn (dd/MM/yyyy): ");
                String symptom = InputUtil.inputString("Triệu chứng: ");
                String diagnose = InputUtil.inputString("Chẩn đoán: ");
                String prescription = InputUtil.inputString("Đơn thuốc: ");
                Double cost = InputUtil.inputDouble("Chi phí: ");
                String status = InputUtil.inputString("Trạng thái: ");

                // --- Ghi lại dòng mới ---
                String newLine = String.join(",",
                        currentId, patientId, doctorId, date, symptom, diagnose, prescription, String.valueOf(cost), status
                );

                updatedLines.add(newLine);
            } else {
                updatedLines.add(line);
            }
        }

        if (found) {
            FileUtil.writeFile(filePath, updatedLines);
            System.out.println("✅ Lịch hẹn đã được cập nhật thành công!");
        } else {
            System.out.println("❌ Không tìm thấy lịch hẹn với ID: " + idToEdit);
        }
    }

    public void deleteAppointment(String id) {
        List<Appointment> list = getAllAppointments();

        String confirm = InputUtil.inputString("⚠️ Bạn có chắc chắn muốn hủy lịch hẹn này? (Y/N): ");
        if (!confirm.equalsIgnoreCase("Y")) {
            System.out.println("❎ Hủy thao tác xóa.");
            return;
        }

        boolean removed = list.removeIf(a -> a.getId().equalsIgnoreCase(id));
        if (removed) {
            saveAll(list);
            System.out.println("✅ Đã hủy lịch hẹn ID: " + id);
        } else {
            System.out.println("❌ Không tìm thấy lịch hẹn ID: " + id);
        }
    }

    public void searchAppointment(int choice) {
        String filePath = "data/appointments.txt";
        if (FileUtil.isFileEmpty(filePath)) {
            System.out.println("⚠️  Không có dữ liệu lịch hẹn nào trong hệ thống!");
            return;
        }

        List<String> lines = FileUtil.readFile(filePath);
        List<String> matchedAppointments = new ArrayList<>();

        switch (choice) {
            case 1 -> {
                // 🔍 Search by Patient Name (partial)
                String patientName = InputUtil.inputString("Nhập tên (hoặc một phần tên) bệnh nhân: ");
                List<String> patientLines = FileUtil.readFile("data/patients.txt");
                List<String> matchedPatientIds = new ArrayList<>();

                for (String line : patientLines) {
                    String[] parts = line.split(",");
                    if (parts.length >= 2 && parts[1].toLowerCase().contains(patientName.toLowerCase())) {
                        matchedPatientIds.add(parts[0].trim());
                    }
                }

                if (matchedPatientIds.isEmpty()) {
                    System.out.println("❌ Không tìm thấy bệnh nhân có tên chứa: " + patientName);
                    return;
                }

                for (String line : lines) {
                    String[] parts = line.split(",");
                    if (parts.length >= 9 && matchedPatientIds.contains(parts[1].trim())) {
                        matchedAppointments.add(line);
                    }
                }

                printAppointments(matchedAppointments);
            }

            case 2 -> {
                // 🔍 Search by Doctor Name (partial)
                String doctorName = InputUtil.inputString("Nhập tên (hoặc một phần tên) bác sĩ: ");
                List<String> doctorLines = FileUtil.readFile("data/doctors.txt");
                List<String> matchedDoctorIds = new ArrayList<>();

                for (String line : doctorLines) {
                    String[] parts = line.split(",");
                    if (parts.length >= 2 && parts[1].toLowerCase().contains(doctorName.toLowerCase())) {
                        matchedDoctorIds.add(parts[0].trim());
                    }
                }

                if (matchedDoctorIds.isEmpty()) {
                    System.out.println("❌ Không tìm thấy bác sĩ có tên chứa: " + doctorName);
                    return;
                }

                for (String line : lines) {
                    String[] parts = line.split(",");
                    if (parts.length >= 9 && matchedDoctorIds.contains(parts[2].trim())) {
                        matchedAppointments.add(line);
                    }
                }

                printAppointments(matchedAppointments);
            }

            case 3 -> {
                // 🔍 Search by Date (exact)
                String date = InputUtil.inputString("Nhập ngày hẹn (dd/MM/yyyy): ");

                for (String line : lines) {
                    String[] parts = line.split(",");
                    if (parts.length >= 9 && parts[3].trim().equalsIgnoreCase(date)) {
                        matchedAppointments.add(line);
                    }
                }

                printAppointments(matchedAppointments);
            }

            case 0 -> System.out.println("↩️  Quay lại menu chính.");
            default -> System.out.println("❌ Lựa chọn không hợp lệ!");
        }
    }


    public void FilterAppointment(int choice) {
        String filePath = "data/appointments.txt";
        if (FileUtil.isFileEmpty(filePath)) {
            System.out.println("⚠️  Không có dữ liệu lịch hẹn nào trong hệ thống!");
            return;
        }

        List<String> lines = FileUtil.readFile(filePath);
        List<String> matchedAppointments = new ArrayList<>();

        switch (choice) {
            case 1 -> {
                // 🧑‍⚕️ Filter by Doctor Name (partial)
                String doctorName = InputUtil.inputString("Nhập tên (hoặc một phần tên) bác sĩ: ");
                List<String> doctorLines = FileUtil.readFile("data/doctors.txt");
                List<String> matchedDoctorIds = new ArrayList<>();

                for (String line : doctorLines) {
                    String[] parts = line.split(",");
                    if (parts.length >= 2 && parts[1].toLowerCase().contains(doctorName.toLowerCase())) {
                        matchedDoctorIds.add(parts[0].trim());
                    }
                }

                if (matchedDoctorIds.isEmpty()) {
                    System.out.println("❌ Không tìm thấy bác sĩ có tên chứa: " + doctorName);
                    return;
                }

                for (String line : lines) {
                    String[] parts = line.split(",");
                    if (parts.length >= 9 && matchedDoctorIds.contains(parts[2].trim())) {
                        matchedAppointments.add(line);
                    }
                }

                printAppointments(matchedAppointments);
            }

            case 2 -> {
                // 📅 Filter by Date (exact)
                String date = InputUtil.inputString("Nhập ngày hẹn (dd/MM/yyyy): ");
                for (String line : lines) {
                    String[] parts = line.split(",");
                    if (parts.length >= 9 && parts[3].trim().equalsIgnoreCase(date)) {
                        matchedAppointments.add(line);
                    }
                }

                printAppointments(matchedAppointments);
            }

            case 0 -> System.out.println("↩️  Quay lại menu chính.");
            default -> System.out.println("❌ Lựa chọn không hợp lệ!");
        }
    }


    // ✅ Helper: In AppointmentService
    private void printAppointments(List<String> appointments) {
        if (appointments.isEmpty()) {
            System.out.println("⚠️  Không có lịch hẹn nào để hiển thị!");
            return;
        }

        // Column headers
        String[] headers = {
                "ID", "Mã BN", "Mã BS", "Ngày", "Triệu chứng",
                "Chẩn đoán", "Đơn thuốc", "Chi phí", "Trạng thái"
        };

        // Split all lines into rows
        List<String[]> rows = new ArrayList<>();
        for (String line : appointments) {
            String[] parts = line.split(",", -1); // include empty fields
            if (parts.length >= 9) {
                String[] trimmed = new String[9];
                for (int i = 0; i < 9; i++) trimmed[i] = parts[i].trim();
                rows.add(trimmed);
            }
        }

        // Determine column widths (max of header length and each value)
        int[] colWidths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            colWidths[i] = headers[i].length();
        }

        for (String[] row : rows) {
            for (int i = 0; i < headers.length; i++) {
                if (row[i].length() > colWidths[i]) {
                    colWidths[i] = row[i].length();
                }
            }
        }

        // Add some padding for readability
        for (int i = 0; i < colWidths.length; i++) {
            colWidths[i] += 2;
        }

        // Helper: build a border line like +------+----------+...+
        StringBuilder border = new StringBuilder("+");
        for (int w : colWidths) {
            border.append("-".repeat(w + 2)).append("+");
        }

        // Helper: print a row
        java.util.function.Consumer<String[]> printRow = row -> {
            System.out.print("|");
            for (int i = 0; i < headers.length; i++) {
                System.out.printf(" %-"+(colWidths[i]+1)+"s|", row[i]);
            }
            System.out.println();
        };

        // Print table
        System.out.println();
        System.out.println(border);
        printRow.accept(headers);
        System.out.println(border);
        for (String[] row : rows) {
            printRow.accept(row);
        }
        System.out.println(border);
    }

    public void exportAppointments(String exportFileName) {
        String sourcePath = "data/appointments.txt";
        String destinationPath;

        // Nếu người dùng không nhập phần mở rộng .txt thì tự động thêm
        if (!exportFileName.toLowerCase().endsWith(".txt")) {
            destinationPath = "data/" + exportFileName + ".txt";
        } else {
            destinationPath = "data/" + exportFileName;
        }

        System.out.println("🔄 Đang xuất dữ liệu lịch hẹn...");
        FileUtil.exportFile(sourcePath, destinationPath);
    }
    private void saveAll(List<Appointment> list) {
        List<String> lines = new ArrayList<>();
        for (Appointment a : list) lines.add(a.toString());
        FileUtil.writeFile(FILE_PATH, lines);
    }
}
