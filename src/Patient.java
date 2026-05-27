public class Patient {
    String patientId;
    String name;
    int age;
    int severity;
    int arrivalTime;

    Patient(String patientId, String name, int age, int severity, int arrivalTime) {
        this.patientId = patientId;
        this.name = name;
        this.age = age;
        this.severity = severity;
        this.arrivalTime = arrivalTime;
    }
}
