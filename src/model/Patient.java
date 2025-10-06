package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Class đại diện cho thông tin bệnh nhân
 */
public class Patient {
    private String patientId;        // Mã BN
    private String fullName;         // Họ tên
    private LocalDate dateOfBirth;   // Ngày sinh
    private String gender;           // Giới tính
    private String idCard;           // CCCD
    private String phoneNumber;      // SĐT
    private String address;          // Địa chỉ
    private String bloodType;        // Nhóm máu
    private String medicalHistory;   // Tiền sử bệnh
    private String status;           // Trạng thái

    // Constructor đầy đủ
    public Patient(String patientId, String fullName, LocalDate dateOfBirth, String gender,
                   String idCard, String phoneNumber, String address, String bloodType,
                   String medicalHistory, String status) {
        this.patientId = patientId;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.idCard = idCard;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.bloodType = bloodType;
        this.medicalHistory = medicalHistory;
        this.status = status;
    }

    // Constructor mặc định
    public Patient() {
        this.status = "Hoạt động";
    }

    // Constructor tương thích với code cũ
    public Patient(String id, String name, int age, String gender, String diagnosis) {
        this.patientId = id;
        this.fullName = name;
        this.dateOfBirth = LocalDate.now().minusYears(age);
        this.gender = gender;
        this.idCard = "";
        this.phoneNumber = "";
        this.address = "";
        this.bloodType = "";
        this.medicalHistory = diagnosis;
        this.status = "Hoạt động";
    }

    // Getters và Setters
    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Methods tương thích với code cũ
    public String getId() { 
        return patientId; 
    }
    
    public String getName() { 
        return fullName; 
    }
    
    public int getAge() { 
        return LocalDate.now().getYear() - dateOfBirth.getYear(); 
    }
    
    public String getDiagnosis() { 
        return medicalHistory; 
    }

    public void setName(String name) { 
        this.fullName = name; 
    }
    
    public void setAge(int age) { 
        this.dateOfBirth = LocalDate.now().minusYears(age); 
    }
    
    public void setDiagnosis(String diagnosis) { 
        this.medicalHistory = diagnosis; 
    }

    /**
     * Chuyển đổi thông tin bệnh nhân thành chuỗi để lưu vào file
     * Format: patientId|fullName|dateOfBirth|gender|idCard|phoneNumber|address|bloodType|medicalHistory|status
     */
    public String toFileString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.join("|",
                patientId,
                fullName,
                dateOfBirth.format(formatter),
                gender,
                idCard,
                phoneNumber,
                address,
                bloodType,
                medicalHistory,
                status
        );
    }

    /**
     * Tạo đối tượng Patient từ chuỗi đọc từ file
     */
    public static Patient fromFileString(String fileString) {
        String[] parts = fileString.split("\\|");
        if (parts.length != 10) {
            throw new IllegalArgumentException("Định dạng dữ liệu không hợp lệ");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return new Patient(
                parts[0],                                    // patientId
                parts[1],                                    // fullName
                LocalDate.parse(parts[2], formatter),        // dateOfBirth
                parts[3],                                    // gender
                parts[4],                                    // idCard
                parts[5],                                    // phoneNumber
                parts[6],                                    // address
                parts[7],                                    // bloodType
                parts[8],                                    // medicalHistory
                parts[9]                                     // status
        );
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format(
                "╔════════════════════════════════════════════════════════════════╗\n" +
                "║ THÔNG TIN BỆNH NHÂN                                            ║\n" +
                "╠════════════════════════════════════════════════════════════════╣\n" +
                "║ Mã BN          : %-45s ║\n" +
                "║ Họ tên         : %-45s ║\n" +
                "║ Ngày sinh      : %-45s ║\n" +
                "║ Giới tính      : %-45s ║\n" +
                "║ CCCD           : %-45s ║\n" +
                "║ SĐT            : %-45s ║\n" +
                "║ Địa chỉ        : %-45s ║\n" +
                "║ Nhóm máu       : %-45s ║\n" +
                "║ Tiền sử bệnh   : %-45s ║\n" +
                "║ Trạng thái     : %-45s ║\n" +
                "╚════════════════════════════════════════════════════════════════╝",
                patientId, fullName, dateOfBirth.format(formatter), gender,
                idCard, phoneNumber, address, bloodType, medicalHistory, status
        );
    }

    // Method tương thích với code cũ
    public static Patient fromString(String line) {
        // Thử parse theo format mới trước
        if (line.contains("|")) {
            try {
                return fromFileString(line);
            } catch (Exception e) {
                // Fallback về format cũ
            }
        }
        
        // Parse theo format cũ
        String[] parts = line.split(",");
        if (parts.length != 5) return null;
        return new Patient(parts[0], parts[1], Integer.parseInt(parts[2]), parts[3], parts[4]);
    }
}
