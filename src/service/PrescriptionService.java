package service;

import model.Prescription;
import util.FileUtil;
import util.InputUtil;
import util.Validator;
import exception.InvalidDataException;

import java.util.*;
import java.util.stream.Collectors;

public class PrescriptionService {
    private static final String FILE_PATH = "data/prescriptions.txt";

    // view
    public List<Prescription> getAll() {
        List<String> lines = FileUtil.readFile(FILE_PATH);
        List<Prescription> list = new ArrayList<>();
        for (String line : lines) {
            Prescription p = Prescription.fromString(line);
            if (p != null)
                list.add(p);
        }
        return list;
    }

    // tìm bằng id
    public Prescription findById(String id) {
        return getAll().stream()
                .filter(p -> p.getId().equalsIgnoreCase(id))
                .findFirst().orElse(null);
    }

    // thêm đơn thuốc
    public void addPrescription() {
        try {
            System.out.println("===== THÊM ĐƠN THUỐC =====");
            String id = InputUtil.nhapChuoi("Nhập mã đơn thuốc: ");
            Validator.checkNotEmpty(id, "Mã đơn thuốc không được để trống!");
            if (findById(id) != null)
                throw new InvalidDataException("Mã đơn thuốc đã tồn tại!");

            String appointmentId = InputUtil.nhapChuoi("Nhập mã lịch khám (Appointment ID): ");
            Validator.checkNotEmpty(appointmentId, "Mã lịch khám không được để trống!");

            String name = InputUtil.nhapChuoi("Nhập tên thuốc: ");
            String type = InputUtil.nhapChuoi("Nhập loại thuốc: ");
            String usage = InputUtil.nhapChuoi("Nhập cách dùng: ");
            int qty = InputUtil.nhapSoNguyen("Nhập số lượng: ");
            double price = InputUtil.nhapSoThuc("Nhập đơn giá: ");
            double total = qty * price;
            String status = InputUtil.nhapChuoi("Nhập trạng thái: ");

            Prescription p = new Prescription(id, appointmentId, name, type, usage, qty, price, total, status);
            FileUtil.appendToFile(FILE_PATH, p.toDataString());
            System.out.println("Đã thêm đơn thuốc " + id + " cho mã khám " + appointmentId);
        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    // cập nhật đơn thuốc
    public void updatePrescription() {
        try {
            String id = InputUtil.nhapChuoi("Nhập mã đơn thuốc cần cập nhật: ");
            List<Prescription> list = getAll();
            Prescription p = findById(id);
            if (p == null) {
                System.out.println("Không tìm thấy đơn thuốc!");
                return;
            }

            String name = InputUtil.nhapChuoi("Nhập tên thuốc mới (bỏ trống nếu giữ nguyên): ");
            String type = InputUtil.nhapChuoi("Nhập loại thuốc mới (bỏ trống nếu giữ nguyên): ");
            String usage = InputUtil.nhapChuoi("Nhập cách dùng mới (bỏ trống nếu giữ nguyên): ");
            String appointmentId = InputUtil.nhapChuoi("Nhập mã lịch khám mới (bỏ trống nếu giữ nguyên): ");
            String status = InputUtil.nhapChuoi("Nhập trạng thái mới (bỏ trống nếu giữ nguyên): ");
            int qty = InputUtil.nhapSoNguyen("Nhập số lượng mới (0 nếu giữ nguyên): ");
            double price = InputUtil.nhapSoThuc("Nhập đơn giá mới (0 nếu giữ nguyên): ");

            if (!name.isEmpty())
                p.setName(name);
            if (!type.isEmpty())
                p.setType(type);
            if (!usage.isEmpty())
                p.setUsage(usage);
            if (!appointmentId.isEmpty())
                p.setAppointmentId(appointmentId);
            if (!status.isEmpty())
                p.setStatus(status);
            if (qty > 0)
                p.setQuantity(qty);
            if (price > 0)
                p.setPrice(price);

            list = list.stream().map(x -> x.getId().equalsIgnoreCase(id) ? p : x)
                    .collect(Collectors.toList());
            FileUtil.writeFile(FILE_PATH, list.stream()
                    .map(Prescription::toDataString)
                    .collect(Collectors.toList()));

            System.out.println("Cập nhật đơn thuốc thành công!");
        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    // xóa đơn thuốc
    public void deletePrescription() {
        try {
            String id = InputUtil.nhapChuoi("Nhập mã đơn thuốc cần xóa: ");
            List<Prescription> list = getAll();
            Prescription p = findById(id);
            if (p == null) {
                System.out.println("Không tìm thấy đơn thuốc!");
                return;
            }

            list.removeIf(x -> x.getId().equalsIgnoreCase(id));
            FileUtil.writeFile(FILE_PATH,
                    list.stream().map(Prescription::toDataString).collect(Collectors.toList()));
            System.out.println("Đã xóa đơn thuốc " + id);
        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    // xem tất cả đơn thuốc
    public void viewAll() {
        List<Prescription> list = getAll();
        if (list.isEmpty()) {
            System.out.println("Không có dữ liệu!");
            return;
        }
        System.out.println("===== DANH SÁCH ĐƠN THUỐC =====");
        list.forEach(p -> System.out.printf(
                "%-8s | %-8s | %-15s | %-10s | %-15s | %3d | %8.2f | %10.2f | %-10s%n",
                p.getId(), p.getAppointmentId(), p.getName(), p.getType(),
                p.getUsage(), p.getQuantity(), p.getPrice(), p.getTotal(), p.getStatus()));
    }

    // lọc đơn thuốc
    public void filterPrescription() {
        System.out.println("===== LỌC ĐƠN THUỐC =====");
        String keyword = InputUtil.nhapChuoi("Nhập từ khóa (tên thuốc, loại, trạng thái): ").toLowerCase();

        List<Prescription> results = getAll().stream()
                .filter(p -> p.getName().toLowerCase().contains(keyword)
                        || p.getType().toLowerCase().contains(keyword)
                        || p.getStatus().toLowerCase().contains(keyword))
                .collect(Collectors.toList());

        if (results.isEmpty()) {
            System.out.println("Không tìm thấy đơn thuốc phù hợp!");
        } else {
            System.out.println("===== KẾT QUẢ LỌC =====");
            results.forEach(p -> System.out.printf(
                    "%-8s | %-8s | %-15s | %-10s | %-15s | %3d | %8.2f | %10.2f | %-10s%n",
                    p.getId(), p.getAppointmentId(), p.getName(), p.getType(),
                    p.getUsage(), p.getQuantity(), p.getPrice(), p.getTotal(), p.getStatus()));
        }
    }

    public void searchMedicineByPrescriptionCriteria() {
        System.out.println("===== TÌM ĐƠN THUỐC =====");
        System.out.println("1. Tìm theo tên thuốc");
        System.out.println("2. Tìm theo mã lịch khám (Appointment ID)");
        System.out.println("3. Tìm theo ID đơn thuốc");
        System.out.print("Chọn chức năng (1-3): ");

        int choice;
        try {
            choice = Integer.parseInt(new Scanner(System.in).nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Lựa chọn không hợp lệ!");
            return;
        }

        List<Prescription> list = new ArrayList<>();

        switch (choice) {
            case 1: // Tìm theo tên thuốc
                String keyword = InputUtil.nhapChuoi("Nhập tên thuốc cần tìm: ").toLowerCase();
                list = getAll().stream()
                        .filter(p -> p.getName().toLowerCase().contains(keyword))
                        .collect(Collectors.toList());
                break;

            case 2: // Tìm theo mã khám
                String appointmentId = InputUtil.nhapChuoi("Nhập mã lịch khám (Appointment ID): ");
                list = getAll().stream()
                        .filter(p -> p.getAppointmentId().equalsIgnoreCase(appointmentId))
                        .collect(Collectors.toList());
                break;

            case 3: // Tìm theo ID đơn thuốc
                String id = InputUtil.nhapChuoi("Nhập ID đơn thuốc cần tìm: ");
                Prescription found = findById(id);
                if (found != null) {
                    System.out.println("===== KẾT QUẢ =====");
                    System.out.printf(
                            "%-8s | %-8s | %-15s | %-10s | %-15s | %3d | %8.2f | %10.2f | %-10s%n",
                            found.getId(), found.getAppointmentId(), found.getName(), found.getType(),
                            found.getUsage(), found.getQuantity(), found.getPrice(), found.getTotal(),
                            found.getStatus());
                } else {
                    System.out.println("⚠ Không tìm thấy đơn thuốc với ID này!");
                }
                return;

            default:
                System.out.println("Vui lòng chọn 1, 2 hoặc 3!");
                return;
        }

        // Hiển thị kết quả cho lựa chọn 1 và 2
        if (list.isEmpty()) {
            System.out.println("Không tìm thấy đơn thuốc phù hợp!");
        } else {
            System.out.println("===== KẾT QUẢ =====");
            list.forEach(p -> System.out.printf(
                    "%-8s | %-8s | %-15s | %-10s | %-15s | %3d | %8.2f | %10.2f | %-10s%n",
                    p.getId(), p.getAppointmentId(), p.getName(), p.getType(),
                    p.getUsage(), p.getQuantity(), p.getPrice(), p.getTotal(), p.getStatus()));
        }
    }

    public void sortPrescription() {
        System.out.println("===== SẮP XẾP ĐƠN THUỐC =====");
        System.out.println("1. Theo tên thuốc");
        System.out.println("2. Theo tổng tiền");
        System.out.println("3. Theo mã khám");
        int choice = InputUtil.nhapSoNguyen("Chọn cách sắp xếp: ");

        List<Prescription> list = getAll();
        switch (choice) {
            case 1 -> list.sort(Comparator.comparing(Prescription::getName));
            case 2 -> list.sort(Comparator.comparing(Prescription::getTotal).reversed());
            case 3 -> list.sort(Comparator.comparing(Prescription::getAppointmentId));
            default -> {
                System.out.println("⚠ Lựa chọn không hợp lệ!");
                return;
            }
        }

        System.out.println("===== KẾT QUẢ SẮP XẾP =====");
        list.forEach(p -> System.out.printf(
                "%-8s | %-8s | %-15s | %-10s | %-15s | %3d | %8.2f | %10.2f | %-10s%n",
                p.getId(), p.getAppointmentId(), p.getName(), p.getType(),
                p.getUsage(), p.getQuantity(), p.getPrice(), p.getTotal(), p.getStatus()));
    }
}
