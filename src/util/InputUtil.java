package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import model.Bill;

public class InputUtil {
    private static final Scanner sc = new Scanner(System.in);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String inputString(String msg) {
        System.out.print(msg);
        return sc.nextLine().trim();
    }

    public static int inputInt(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("❌ Vui lòng nhập số hợp lệ!");
            }
        }
    }

    public static double inputDouble(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("❌ Vui lòng nhập số thực hợp lệ!");
            }
        }
    }

    public static boolean validateBill(Bill b) {

        if (b.getExaminationId() == null || b.getExaminationId().isBlank()) {
            System.out.println("❌ Mã khám không được để trống!");
            return false;
        }
        if (b.getPatientId() == null || b.getPatientId().isBlank()) {
            System.out.println("❌ Mã bệnh nhân không được để trống!");
            return false;
        }

        try {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate.parse(b.getDate(), fmt);
        } catch (DateTimeParseException e) {
            System.out.println("❌ Ngày lập không hợp lệ! Định dạng đúng: dd/MM/yyyy");
            return false;
        }

        if (b.getExaminationFee() < 0 || b.getMedicineFee() < 0 || b.getDiscount() < 0) {
            System.out.println("❌ Phí khám, tiền thuốc và giảm giá không được âm!");
            return false;
        }

        if (b.getTotal() != b.getExaminationFee() + b.getMedicineFee() - b.getDiscount()) {
            System.out.println("⚠️ Tổng tiền không khớp với các giá trị thành phần!");
            return false;
        }

        if (!b.getPaymentStatus().equalsIgnoreCase("Đã") &&
                !b.getPaymentStatus().equalsIgnoreCase("Chưa")) {
            System.out.println("❌ Trạng thái thanh toán chỉ được là 'Đã' hoặc 'Chưa'.");
            return false;
        }

        return true;
    }

    public static LocalDate inputDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                return LocalDate.parse(input, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Định dạng không hợp lệ! Hãy nhập theo dạng yyyy-MM-dd (vd: 2025-10-07)");
            }
        }
    }

}
