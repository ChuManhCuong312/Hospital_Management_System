package service;

import model.Medicine;
import util.FileUtil;
import java.util.*;

public class MedicineService {
    private static final String FILE_PATH = "data/medicines.txt";

    public void addMedicine(Medicine m) {
        FileUtil.appendToFile(FILE_PATH, m.toString());
        System.out.println("Đã thêm thuốc: " + m.getName());
    }

    public List<Medicine> getAllMedicines() {
        List<String> lines = FileUtil.readFile(FILE_PATH);
        List<Medicine> meds = new ArrayList<>();
        for (String line : lines) {
            Medicine m = Medicine.fromString(line);
            if (m != null) meds.add(m);
        }
        return meds;
    }

    public void viewAll() {
        List<Medicine> list = getAllMedicines();
        if (list.isEmpty()) {
            System.out.println("Chưa có thuốc nào trong kho.");
            return;
        }

        System.out.println("\n+------+----------------------+----------+------------+");
        System.out.println("|  ID  |      Tên thuốc       |  Số lượng|    Giá     |");
        System.out.println("+------+----------------------+----------+------------+");

        for (Medicine m : list) {
            System.out.printf("| %-4s | %-20s | %-8d | %-10.2f |%n",
                    m.getId(),
                    m.getName(),
                    m.getQuantity(),
                    m.getPrice());
        }

        System.out.println("+------+----------------------+----------+------------+");
    }

    public void updateQuantity(String id, int newQty) {
        List<Medicine> list = getAllMedicines();
        boolean found = false;

        for (Medicine m : list) {
            if (m.getId().equalsIgnoreCase(id)) {
                m.setQuantity(newQty);
                found = true;
                break;
            }
        }

        if (found) {
            saveAll(list);
            System.out.println("Đã cập nhật số lượng thuốc ID: " + id);
        } else {
            System.out.println("Không tìm thấy thuốc có ID: " + id);
        }
    }

    private void saveAll(List<Medicine> list) {
        List<String> lines = new ArrayList<>();
        for (Medicine m : list) lines.add(m.toString());
        FileUtil.writeFile(FILE_PATH, lines);
    }
}
