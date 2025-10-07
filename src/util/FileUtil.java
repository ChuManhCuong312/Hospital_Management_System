package util;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileUtil {

    // Đọc toàn bộ nội dung file, trả về danh sách từng dòng
    public static List<String> readFile(String path) {
        try {
            File f = new File(path);
            if (!f.exists()) {
                File parent = f.getParentFile();
                if (parent != null && !parent.exists()) parent.mkdirs();
                f.createNewFile();
                return new ArrayList<>();
            }
            return Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            System.out.println("Lỗi đọc file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Ghi đè toàn bộ file với danh sách lines
    public static void writeFile(String path, List<String> lines) {
        try {
            File file = new File(path);
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) parent.mkdirs();
            Files.write(Paths.get(path), lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.out.println("Lỗi ghi file: " + e.getMessage());
        }
    }

    // Ghi thêm một dòng vào cuối file
    public static void appendToFile(String path, String content) {
        try {
            File file = new File(path);
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) parent.mkdirs();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {
                writer.write(content);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Lỗi ghi file: " + e.getMessage());
        }
    }
}
