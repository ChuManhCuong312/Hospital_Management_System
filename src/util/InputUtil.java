package util;

import java.util.Scanner;

public class InputUtil {
    private static final Scanner sc = new Scanner(System.in);

    // Nhập chuỗi (trim)
    public static String nhapChuoi(String message) {
        System.out.print(message);
        return sc.nextLine().trim();
    }

    // Nhập số nguyên an toàn
    public static int nhapSoNguyen(String message) {
        while (true) {
            try {
                System.out.print(message);
                String line = sc.nextLine().trim();
                if (line.isEmpty()) return -1; // allow caller to treat -1 as skip
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập một số nguyên hợp lệ!");
            }
        }
    }

    // Nhập số thực
    public static double nhapSoThuc(String message) {
        while (true) {
            try {
                System.out.print(message);
                String line = sc.nextLine().trim();
                if (line.isEmpty()) return -1;
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập một số thực hợp lệ!");
            }
        }
    }

    // Nhập lựa chọn trong khoảng [min,max]
    public static int nhapLuaChon(String message, int min, int max) {
        int choice;
        while (true) {
            choice = nhapSoNguyen(message);
            if (choice >= min && choice <= max) return choice;
            System.out.printf("Lựa chọn không hợp lệ! (Chọn từ %d đến %d)%n", min, max);
        }
    }
}
