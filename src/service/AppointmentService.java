package service;

import model.Appointment;
import util.FileUtil;
import java.util.*;

public class AppointmentService {
    private static final String FILE_PATH = "data/appointments.txt";

    public void addAppointment(Appointment a) {
        FileUtil.appendToFile(FILE_PATH, a.toString());
        System.out.println("✅ Đã tạo lịch hẹn ID: " + a.getId());
    }

    public List<Appointment> getAllAppointments() {
        List<String> lines = FileUtil.readFile(FILE_PATH);
        List<Appointment> appointments = new ArrayList<>();
        for (String line : lines) {
            Appointment a = Appointment.fromString(line);
            if (a != null) appointments.add(a);
        }
        return appointments;
    }

    public void viewAll() {
        List<Appointment> list = getAllAppointments();
        if (list.isEmpty()) {
            System.out.println("Chưa có lịch hẹn nào.");
            return;
        }

        System.out.println("\n+------+----------+----------+------------+");
        System.out.println("|  ID  |  Mã BN   |  Mã BS   |   Ngày     |");
        System.out.println("+------+----------+----------+------------+");

        for (Appointment a : list) {
            System.out.printf("| %-4s | %-8s | %-8s | %-10s |%n",
                    a.getId(),
                    a.getPatientId(),
                    a.getDoctorId(),
                    a.getDate());
        }

        System.out.println("+------+----------+----------+------------+");
    }

    public void deleteAppointment(String id) {
        List<Appointment> list = getAllAppointments();
        boolean removed = list.removeIf(a -> a.getId().equalsIgnoreCase(id));
        if (removed) {
            saveAll(list);
            System.out.println("Đã hủy lịch hẹn ID: " + id);
        } else {
            System.out.println("Không tìm thấy lịch hẹn ID: " + id);
        }
    }

    private void saveAll(List<Appointment> list) {
        List<String> lines = new ArrayList<>();
        for (Appointment a : list) lines.add(a.toString());
        FileUtil.writeFile(FILE_PATH, lines);
    }
}
