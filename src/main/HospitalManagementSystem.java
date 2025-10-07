package main;

import service.*;

import java.util.Scanner;

public class HospitalManagementSystem {

    private static final DoctorService doctorService = new DoctorService();
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("==== QUẢN LÝ BÁC SĨ ====");
            System.out.println("1. Thêm bác sĩ");
            System.out.println("2. Cập nhật bác sĩ");
            System.out.println("3. Xóa bác sĩ");
            System.out.println("4. Tìm kiếm bác sĩ");
            System.out.println("5. Lọc danh sách bác sĩ");
            System.out.println("6. Hiển thị tất cả");
            System.out.println("7. Export file");
            System.out.println("8. Import file");
            System.out.println("0. Thoát");
            System.out.print("Chọn: ");
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số hợp lệ!");
                continue;
            }

            switch (choice) {
                case 1 -> System.out.println(doctorService.addDoctorFromInput(sc));
                case 2 -> System.out.println(doctorService.updateDoctorFromInput(sc));
                case 3 -> System.out.println(doctorService.deleteDoctorFromInput(sc));
                case 4 -> searchMenu();
                case 5 -> filterMenu();
                case 6 -> doctorService.showAll();
                case 7 -> exportFile();
                case 8 -> importFile();
                case 0 -> {
                    System.out.println("Thoát chương trình.");
                    return;
                }
                default -> System.out.println("Chọn sai.");
            }
        } while (true);
    }

    // MENU CON: Tìm kiếm
    private static void searchMenu() {
        int choice;
        do {
            System.out.println("== TÌM KIẾM BÁC SĨ ==");
            System.out.println("1. Theo mã");
            System.out.println("2. Theo tên");
            System.out.println("3. Theo chuyên khoa");
            System.out.println("0. Quay lại");
            System.out.print("Chọn: ");
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số hợp lệ!");
                continue;
            }

            switch (choice) {
                case 1 -> doctorService.findByIdFromInput(sc);
                case 2 -> doctorService.findByNameFromInput(sc);
                case 3 -> doctorService.findBySpecialtyFromInput(sc);
                case 0 -> { return; }
                default -> System.out.println("Chọn sai.");
            }
        } while (true);
    }

    //MENU CON: Lọc
    private static void filterMenu() {
        int choice;
        do {
            System.out.println("== LỌC DANH SÁCH BÁC SĨ ==");
            System.out.println("1. Theo chuyên khoa");
            System.out.println("2. Theo kinh nghiệm tối thiểu");
            System.out.println("3. Theo chuyên khoa + kinh nghiệm");
            System.out.println("0. Quay lại");
            System.out.print("Chọn: ");
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số hợp lệ!");
                continue;
            }

            switch (choice) {
                case 1 -> {
                    System.out.print("Nhập chuyên khoa: ");
                    String ck = sc.nextLine().trim();
                    doctorService.showFiltered(doctorService.filter(ck, null));
                }
                case 2 -> {
                    System.out.print("Nhập kinh nghiệm tối thiểu: ");
                    try {
                        int kn = Integer.parseInt(sc.nextLine());
                        doctorService.showFiltered(doctorService.filter(null, kn));
                    } catch (NumberFormatException e) {
                        System.out.println("Vui lòng nhập số nguyên!");
                    }
                }
                case 3 -> {
                    System.out.print("Nhập chuyên khoa: ");
                    String ck = sc.nextLine().trim();
                    System.out.print("Nhập kinh nghiệm tối thiểu: ");
                    try {
                        int kn = Integer.parseInt(sc.nextLine());
                        doctorService.showFiltered(doctorService.filter(ck, kn));
                    } catch (NumberFormatException e) {
                        System.out.println("Vui lòng nhập số nguyên!");
                    }
                }
                case 0 -> { return; }
                default -> System.out.println("Chọn sai.");
            }
        } while (true);
    }
    // Export / Import
    private static void exportFile() {
        System.out.print("Nhập tên file (.txt): ");
        String fileName = sc.nextLine().trim();
        if (!fileName.endsWith(".txt")) fileName += ".txt";
        if (doctorService.exportToFile(fileName))
            System.out.println("Export thành công vào " + fileName);
        else
            System.out.println("Lỗi export file.");
    }

    private static void importFile() {
        System.out.print("Nhập tên file cần import (.txt): ");
        String fileName = sc.nextLine().trim();
        if (!fileName.endsWith(".txt")) fileName += ".txt";
        System.out.println(doctorService.importFromFile(fileName));
    }
}
