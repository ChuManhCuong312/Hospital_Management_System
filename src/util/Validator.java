package util;

import exception.InvalidDataException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Validator {

    public static void checkNotEmpty(String value, String message) throws InvalidDataException {
        if (value == null || value.trim().isEmpty()) throw new InvalidDataException(message);
    }

    public static void checkPhone(String phone) throws InvalidDataException {
        if (!phone.matches("^0\\d{9}$")) throw new InvalidDataException("Số điện thoại không hợp lệ (bắt đầu 0, 10 chữ số)");
    }

    public static void checkEmail(String email) throws InvalidDataException {
        if (!email.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) throw new InvalidDataException("Email không hợp lệ");
    }

    public static void checkDate(String date) throws InvalidDataException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(date);
        } catch (ParseException e) {
            throw new InvalidDataException("Định dạng ngày không hợp lệ (dd/MM/yyyy)");
        }
    }

    public static void checkTime(String time) throws InvalidDataException {
        if (!time.matches("^([01]?\\d|2[0-3]):[0-5]\\d$")) throw new InvalidDataException("Định dạng giờ không hợp lệ (HH:mm)");
    }

    public static void checkCCCD(String cccd) throws InvalidDataException {
        if (!cccd.matches("^\\d{12}$")) throw new InvalidDataException("CCCD không hợp lệ (12 chữ số)");
    }

    public static void checkBloodGroup(String group) throws InvalidDataException {
        if (!group.matches("^(A|B|AB|O|A\\+|A-|B\\+|B-|AB\\+|AB-|O\\+|O-)$")) throw new InvalidDataException("Nhóm máu không hợp lệ (A/B/AB/O, có thể +/ -)");
    }

    public static void checkGender(String gender) throws InvalidDataException {
        if (gender == null) throw new InvalidDataException("Giới tính không được để trống");
        if (!(gender.equalsIgnoreCase("Nam") || gender.equalsIgnoreCase("Nữ"))) throw new InvalidDataException("Giới tính phải là 'Nam' hoặc 'Nữ'");
    }

    public static void checkPositive(int value, String message) throws InvalidDataException {
        if (value <= 0) throw new InvalidDataException(message);
    }
}
