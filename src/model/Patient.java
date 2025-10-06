package model;

public class Patient {
    private String id;
    private String name;
    private int age;
    private String gender;
    private String diagnosis;

    public Patient(String id, String name, int age, String gender, String diagnosis) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.diagnosis = diagnosis;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getDiagnosis() { return diagnosis; }

    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setGender(String gender) { this.gender = gender; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    @Override
    public String toString() {
        return id + "," + name + "," + age + "," + gender + "," + diagnosis;
    }

    public static Patient fromString(String line) {
        String[] parts = line.split(",");
        if (parts.length != 5) return null;
        return new Patient(parts[0], parts[1], Integer.parseInt(parts[2]), parts[3], parts[4]);
    }
}
