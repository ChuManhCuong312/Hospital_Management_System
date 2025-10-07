package util;

import exception.InvalidDataException;
import java.util.regex.Pattern;
import model.Patient;

/**
 * Utility class để validate dữ liệu bệnh nhân
 */
public class PatientValidator {
    
    // Regex patterns
    private static final Pattern ID_PATTERN = Pattern.compile("^[A-Za-z0-9_-]+$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[\\p{L}\\s]+$");
    private static final Pattern GENDER_PATTERN = Pattern.compile("^(Nam|Nữ|nam|nữ|MALE|FEMALE|male|female)$");
    
    // Constants
    private static final int MIN_AGE = 0;
    private static final int MAX_AGE = 150;
    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 100;
    private static final int MIN_DIAGNOSIS_LENGTH = 3;
    private static final int MAX_DIAGNOSIS_LENGTH = 200;

    /**
     * Validate toàn bộ thông tin bệnh nhân
     */
    public static void validatePatient(Patient patient) throws InvalidDataException {
        if (patient == null) {
            throw new InvalidDataException("Thông tin bệnh nhân không được null");
        }
        
        validateId(patient.getId());
        validateName(patient.getName());
        validateAge(patient.getAge());
        validateGender(patient.getGender());
        validateDiagnosis(patient.getDiagnosis());
    }

    /**
     * Validate ID bệnh nhân
     */
    public static void validateId(String id) throws InvalidDataException {
        if (id == null || id.trim().isEmpty()) {
            throw new InvalidDataException("ID bệnh nhân không được để trống");
        }
        
        if (id.length() < 3 || id.length() > 20) {
            throw new InvalidDataException("ID bệnh nhân phải có độ dài từ 3-20 ký tự");
        }
        
        if (!ID_PATTERN.matcher(id).matches()) {
            throw new InvalidDataException("ID bệnh nhân chỉ được chứa chữ cái, số, dấu gạch ngang và gạch dưới");
        }
    }

    /**
     * Validate tên bệnh nhân
     */
    public static void validateName(String name) throws InvalidDataException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidDataException("Tên bệnh nhân không được để trống");
        }
        
        String trimmedName = name.trim();
        if (trimmedName.length() < MIN_NAME_LENGTH || trimmedName.length() > MAX_NAME_LENGTH) {
            throw new InvalidDataException("Tên bệnh nhân phải có độ dài từ " + MIN_NAME_LENGTH + " đến " + MAX_NAME_LENGTH + " ký tự");
        }
        
        if (!NAME_PATTERN.matcher(trimmedName).matches()) {
            throw new InvalidDataException("Tên bệnh nhân chỉ được chứa chữ cái và khoảng trắng");
        }
        
        // Kiểm tra tên không được chỉ có khoảng trắng
        if (trimmedName.replaceAll("\\s+", "").isEmpty()) {
            throw new InvalidDataException("Tên bệnh nhân không được chỉ có khoảng trắng");
        }
    }

    /**
     * Validate tuổi bệnh nhân
     */
    public static void validateAge(int age) throws InvalidDataException {
        if (age < MIN_AGE || age > MAX_AGE) {
            throw new InvalidDataException("Tuổi bệnh nhân phải từ " + MIN_AGE + " đến " + MAX_AGE + " tuổi");
        }
    }

    /**
     * Validate giới tính bệnh nhân
     */
    public static void validateGender(String gender) throws InvalidDataException {
        if (gender == null || gender.trim().isEmpty()) {
            throw new InvalidDataException("Giới tính không được để trống");
        }
        
        if (!GENDER_PATTERN.matcher(gender.trim()).matches()) {
            throw new InvalidDataException("Giới tính phải là 'Nam' hoặc 'Nữ'");
        }
    }

    /**
     * Validate chẩn đoán bệnh nhân
     */
    public static void validateDiagnosis(String diagnosis) throws InvalidDataException {
        if (diagnosis == null || diagnosis.trim().isEmpty()) {
            throw new InvalidDataException("Chẩn đoán không được để trống");
        }
        
        String trimmedDiagnosis = diagnosis.trim();
        if (trimmedDiagnosis.length() < MIN_DIAGNOSIS_LENGTH || trimmedDiagnosis.length() > MAX_DIAGNOSIS_LENGTH) {
            throw new InvalidDataException("Chẩn đoán phải có độ dài từ " + MIN_DIAGNOSIS_LENGTH + " đến " + MAX_DIAGNOSIS_LENGTH + " ký tự");
        }
        
        // Kiểm tra chẩn đoán không được chỉ có khoảng trắng
        if (trimmedDiagnosis.replaceAll("\\s+", "").isEmpty()) {
            throw new InvalidDataException("Chẩn đoán không được chỉ có khoảng trắng");
        }
    }

    /**
     * Validate và chuẩn hóa dữ liệu đầu vào
     */
    public static Patient validateAndNormalize(String id, String name, int age, String gender, String diagnosis) 
            throws InvalidDataException {
        
        // Chuẩn hóa dữ liệu
        String normalizedId = id != null ? id.trim() : "";
        String normalizedName = name != null ? name.trim() : "";
        String normalizedGender = gender != null ? gender.trim() : "";
        String normalizedDiagnosis = diagnosis != null ? diagnosis.trim() : "";
        
        // Chuẩn hóa giới tính
        if (normalizedGender.equalsIgnoreCase("male") || normalizedGender.equalsIgnoreCase("nam")) {
            normalizedGender = "Nam";
        } else if (normalizedGender.equalsIgnoreCase("female") || normalizedGender.equalsIgnoreCase("nữ")) {
            normalizedGender = "Nữ";
        }
        
        // Validate từng trường
        validateId(normalizedId);
        validateName(normalizedName);
        validateAge(age);
        validateGender(normalizedGender);
        validateDiagnosis(normalizedDiagnosis);
        
        // Tạo và trả về Patient đã được validate
        return new Patient(normalizedId, normalizedName, age, normalizedGender, normalizedDiagnosis);
    }

    /**
     * Kiểm tra xem một chuỗi có phải là số nguyên hợp lệ không
     */
    public static boolean isValidInteger(String str) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        }
        
        try {
            Integer.parseInt(str.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Kiểm tra xem một chuỗi có phải là số thực hợp lệ không
     */
    public static boolean isValidDouble(String str) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        }
        
        try {
            Double.parseDouble(str.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validate ID có tồn tại trong hệ thống không (dùng để kiểm tra trùng lặp)
     */
    public static void validateIdUniqueness(String id, java.util.List<Patient> existingPatients) 
            throws InvalidDataException {
        if (existingPatients == null) {
            return;
        }
        
        boolean exists = existingPatients.stream()
                .anyMatch(patient -> patient.getId().equalsIgnoreCase(id));
        
        if (exists) {
            throw new InvalidDataException("ID bệnh nhân đã tồn tại trong hệ thống: " + id);
        }
    }

    /**
     * Chuẩn hóa chuỗi đầu vào (loại bỏ khoảng trắng thừa)
     */
    public static String normalizeString(String input) {
        if (input == null) {
            return "";
        }
        return input.trim().replaceAll("\\s+", " ");
    }

    /**
     * Kiểm tra tính hợp lệ của email (nếu có trong tương lai)
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.matches(emailPattern, email.trim());
    }

    /**
     * Kiểm tra tính hợp lệ của số điện thoại (nếu có trong tương lai)
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }
        
        // Pattern cho số điện thoại Việt Nam
        String phonePattern = "^(\\+84|84|0)[1-9][0-9]{8,9}$";
        return Pattern.matches(phonePattern, phoneNumber.trim().replaceAll("\\s+", ""));
    }
}
