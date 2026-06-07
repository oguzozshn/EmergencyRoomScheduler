package Model;

public class Doctor {
    public int id;
    public String name;
    public String status;
    public int treatedCount = 0;

    public Doctor(int id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;

    }
}
