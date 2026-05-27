package Model;

public class Patient {
    public String patientId;
    public String name;
    public int age;
    public int severity;
    public int arrivalTime;

    public Patient(String patientId, String name, int age, int severity, int arrivalTime) {
        this.patientId = patientId;
        this.name = name;
        this.age = age;
        this.severity = severity;
        this.arrivalTime = arrivalTime;
}
