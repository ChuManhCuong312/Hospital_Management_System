package service;

import exception.InvalidDataException;
import model.Bill;
import util.FileUtil;
import util.InputUtil;
import util.Validator;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class BillingService {
    private static final String FILE_PATH = "data/billing.txt";

    public List<Bill> getAll() {
        List<String> lines = FileUtil.readFile(FILE_PATH);
        List<Bill> list = new ArrayList<>();
        for (String l : lines) {
            Bill b = Bill.fromString(l);
            if (b != null) list.add(b);
        }
        return list;
    }

    private void saveAll(List<Bill> list) {
        List<String> lines = new ArrayList<>();
        for (Bill b : list) lines.add(b.toDataString());
        FileUtil.writeFile(FILE_PATH, lines);
    }

    // Tạo hóa đơn
    public void addBill() {
        try {
            System.out.println("===== TẠO HÓA ĐƠN =====");
            String id = InputUtil.nhapChuoi("Nhập mã hóa đơn: ");
            Validator.checkNotEmpty(id, "Mã hóa đơn không được để trống!");
            if (findById(id) != null) throw new InvalidDataException("Mã hóa đơn đã tồn tại!");

            String appointmentId = InputUtil.nhapChuoi("Nhập mã lịch khám: ");
            String patientId = InputUtil.nhapChuoi("Nhập mã bệnh nhân: ");
            String date = InputUtil.nhapChuoi("Nhập ngày lập (dd/MM/yyyy): ");
            Validator.checkDate(date);

            double examFee = InputUtil.nhapSoThuc("Nhập phí khám: ");
            double medicineFee = InputUtil.nhapSoThuc("Nhập tiền thuốc: ");
            double discount = InputUtil.nhapSoThuc("Nhập giảm giá (%) (ví dụ 10 cho 10%): ");
            if (examFee < 0 || medicineFee < 0 || discount < 0) throw new InvalidDataException("Các khoản phí không hợp lệ!");

            double total = (examFee + medicineFee) * (1 - discount / 100.0);
            String status = "Chưa thanh toán";

            Bill b = new Bill(id, appointmentId, patientId, date, examFee, medicineFee, discount, total, status);
            FileUtil.appendToFile(FILE_PATH, b.toDataString());
            System.out.println("Đã tạo hóa đơn: " + id);
        } catch (InvalidDataException e) {
            System.out.println("Lỗi: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Lỗi khi tạo hóa đơn: " + e.getMessage());
        }
    }

    public void viewAll() {
        List<Bill> list = getAll();
        if (list.isEmpty()) {
            System.out.println("Không có hóa đơn!");
            return;
        }
        System.out.println("============================================================================================================================");
        System.out.printf("| %-8s | %-10s | %-10s | %-12s | %-10s | %-10s | %-8s | %-12s | %-16s |%n",
                "Mã HĐ", "Mã khám", "Mã BN", "Ngày", "Phí khám", "Tiền thuốc", "Giảm%", "Tổng tiền", "Trạng thái");
        System.out.println("============================================================================================================================");
        for (Bill b : list) {
            System.out.printf("| %-8s | %-10s | %-10s | %-12s | %-10.1f | %-10.1f | %-8.1f | %-12.1f | %-16s |%n",
                    b.getId(), b.getAppointmentId(), b.getPatientId(), b.getDate(),
                    b.getExamFee(), b.getMedicineFee(), b.getDiscount(), b.getTotal(), b.getStatus());
        }
        System.out.println("============================================================================================================================");
    }

    public Bill findById(String id) {
        for (Bill b : getAll()) if (b.getId().equalsIgnoreCase(id)) return b;
        return null;
    }

    // Cập nhật hóa đơn (edit fees/discount/status)
    public void updateBill() {
        try {
            System.out.println("===== CẬP NHẬT HÓA ĐƠN =====");
            String id = InputUtil.nhapChuoi("Nhập mã hóa đơn cần cập nhật: ");
            List<Bill> list = getAll();
            boolean found = false;
            for (Bill b : list) {
                if (b.getId().equalsIgnoreCase(id)) {
                    double examFee = InputUtil.nhapSoThuc("Phí khám (" + b.getExamFee() + "): ");
                    double medFee = InputUtil.nhapSoThuc("Tiền thuốc (" + b.getMedicineFee() + "): ");
                    double discount = InputUtil.nhapSoThuc("Giảm giá (%) (" + b.getDiscount() + "): ");
                    String status = InputUtil.nhapChuoi("Trạng thái (" + b.getStatus() + "): ");

                    if (examFee >= 0) b.setExamFee(examFee);
                    if (medFee >= 0) b.setMedicineFee(medFee);
                    if (discount >= 0) b.setDiscount(discount);
                    if (!status.isEmpty()) b.setStatus(status);

                    found = true;
                    break;
                }
            }
            if (found) {
                saveAll(list);
                System.out.println("Đã cập nhật hóa đơn: " + id);
            } else System.out.println("Không tìm thấy hóa đơn!");
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật hóa đơn: " + e.getMessage());
        }
    }

    public void deleteBill() {
        try {
            System.out.println("===== XÓA HÓA ĐƠN =====");
            String id = InputUtil.nhapChuoi("Nhập mã hóa đơn cần xóa: ");
            List<Bill> list = getAll();
            boolean removed = list.removeIf(b -> b.getId().equalsIgnoreCase(id));
            if (removed) {
                saveAll(list);
                System.out.println("Đã xóa hóa đơn: " + id);
            } else System.out.println("Không tìm thấy hóa đơn!");
        } catch (Exception e) {
            System.out.println("Lỗi khi xóa hóa đơn: " + e.getMessage());
        }
    }

    // Tìm kiếm hóa đơn (bệnh nhân / ngày / tổng tiền >= X)
    public void searchBill() {
        try {
            System.out.println("===== TÌM KIẾM HÓA ĐƠN =====");
            System.out.println("1. Theo mã bệnh nhân");
            System.out.println("2. Theo ngày (dd/MM/yyyy)");
            System.out.println("3. Tổng tiền >= X");
            System.out.println("4. Quay lại");
            int c = InputUtil.nhapLuaChon("Chọn: ", 1, 4);
            if (c == 4) {
                return;
            }
            String key = InputUtil.nhapChuoi("Nhập từ khóa: ");
            List<Bill> res = new ArrayList<>();
            for (Bill b : getAll()) {
                if (c == 1 && b.getPatientId().equalsIgnoreCase(key)) res.add(b);
                else if (c == 2 && b.getDate().equalsIgnoreCase(key)) res.add(b);
                else if (c == 3) {
                    try {
                        double x = Double.parseDouble(key);
                        if (b.getTotal() >= x) res.add(b);
                    } catch (Exception ignored) {}
                }
            }
            if (res.isEmpty()) System.out.println("Không tìm thấy hóa đơn!");
            else {
                System.out.println("Kết quả:");
                for (Bill b : res) System.out.println(b.toDataString());
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi tìm kiếm hóa đơn: " + e.getMessage());
        }
    }

    // Lọc hóa đơn: theo khoảng ngày (so sánh chuỗi dd/MM/yyyy) hoặc khoảng tổng tiền
    public void filterBill() {
        try {
            System.out.println("===== LỌC HÓA ĐƠN =====");
            System.out.println("1. Theo khoảng ngày");
            System.out.println("2. Theo khoảng tổng tiền");
            System.out.println("3. Quay lại");
            int c = InputUtil.nhapLuaChon("Chọn: ", 1, 3);
            if (c==3){
                return;
            }
            List<Bill> res = new ArrayList<>();
            if (c == 1) {
                String from = InputUtil.nhapChuoi("Từ ngày (dd/MM/yyyy): ");
                String to = InputUtil.nhapChuoi("Đến ngày (dd/MM/yyyy): ");
                for (Bill b : getAll()) {
                    if (b.getDate().compareTo(from) >= 0 && b.getDate().compareTo(to) <= 0) res.add(b);
                }
            } else {
                double min = InputUtil.nhapSoThuc("Tổng tiền >= ");
                double max = InputUtil.nhapSoThuc("Tổng tiền <= ");
                for (Bill b : getAll()) if (b.getTotal() >= min && b.getTotal() <= max) res.add(b);
            }
            if (res.isEmpty()) System.out.println("Không có hóa đơn theo điều kiện!");
            else {
                System.out.println("Kết quả:");
                for (Bill b : res) System.out.println(b.toDataString());
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi lọc hóa đơn: " + e.getMessage());
        }
    }

    // Xuất hóa đơn ra file txt
    public void exportBill() {
        try {
            System.out.println("===== XUẤT HÓA ĐƠN RA FILE =====");
            String id = InputUtil.nhapChuoi("Nhập mã hóa đơn cần xuất: ");
            Bill b = findById(id);
            if (b == null) { System.out.println("Không tìm thấy hóa đơn!"); return; }
            String fname = "bill_" + id + ".txt";
            try (PrintWriter pw = new PrintWriter(fname)) {
                pw.println("HÓA ĐƠN: " + b.getId());
                pw.println("Mã khám: " + b.getAppointmentId());
                pw.println("Bệnh nhân: " + b.getPatientId());
                pw.println("Ngày: " + b.getDate());
                pw.println("---------------------------------------------");
                pw.printf("Phí khám: %.1f%n", b.getExamFee());
                pw.printf("Tiền thuốc: %.1f%n", b.getMedicineFee());
                pw.printf("Giảm: %.1f%%%n", b.getDiscount());
                pw.printf("TỔNG: %.1f%n", b.getTotal());
                pw.printf("Trạng thái: %s%n", b.getStatus());
            }
            System.out.println("Đã xuất hóa đơn ra file: bill_" + id + ".txt");
        } catch (Exception e) {
            System.out.println("Lỗi khi xuất hóa đơn: " + e.getMessage());
        }
    }
}
