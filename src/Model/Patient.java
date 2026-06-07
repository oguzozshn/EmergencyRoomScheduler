package Model;
import DataStructures.Stack;

public class Patient {
    public String patientId;       // P001, P002 gibi String değerler için
    public String name;
    public int age;         // Dosyadan gelen yaş bilgisi
    public int severity;
    public int arrivalTime;
    public int assignedRoom = -1;
    public int assignedDocId = -1;
    public int priorityScore = -1;

    public Patient(String id, String name, int age, int severity, int arrivalTime, int currentTime) {
        this.patientId = id;
        this.name = name;
        this.age = age;
        this.severity = severity;
        this.arrivalTime = arrivalTime;
        this.priorityScore = calculatePriority(currentTime);
    }

    public int calculatePriority(int currentTime) {
        int waitTime = currentTime - this.arrivalTime;
        return (6 - this.severity) * 100 + waitTime;
    }
}