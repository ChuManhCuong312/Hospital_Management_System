package util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    // Đọc toàn bộ dữ liệu từ file (mỗi dòng là 1 bản ghi)
    public static List<String> readFile(String filePath) {
        List<String> lines = new ArrayList<>();
        File file = new File(filePath);

        // Nếu file chưa tồn tại thì tự tạo mới
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Lỗi khi tạo file: " + e.getMessage());
                return lines;
            }
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line.trim());
                }
            }
        } catch (IOException e) {
            System.out.println("Lỗi khi đọc file " + filePath + ": " + e.getMessage());
        }
        return lines;
    }

    // Ghi toàn bộ danh sách dòng vào file (ghi đè)
    public static void writeFile(String filePath, List<String> lines) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Lỗi khi ghi file " + filePath + ": " + e.getMessage());
        }
    }

    // Ghi thêm 1 dòng vào cuối file (append)
    public static void appendToFile(String filePath, String line) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Lỗi khi ghi thêm vào file " + filePath + ": " + e.getMessage());
        }
    }

    // Xóa toàn bộ nội dung file (dùng khi muốn làm sạch dữ liệu)
    public static void clearFile(String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            // Ghi rỗng để xóa dữ liệu
        } catch (IOException e) {
            System.out.println("Lỗi khi xóa nội dung file " + filePath + ": " + e.getMessage());
        }
    }

    // Kiểm tra file có trống không
    public static boolean isFileEmpty(String filePath) {
        File file = new File(filePath);
        if (!file.exists() || file.length() == 0) return true;
        return false;
    }
}
