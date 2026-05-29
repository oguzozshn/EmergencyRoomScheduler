package DataStructures;
import Model.Doctor;

public class HashMap {
    private Doctor[] doctors = new Doctor[5];
    HashMap(){}

    public void put(Doctor doctor){
        int index = HashFunction(doctor);
        if (doctors[index] == null){
            doctors[index] = doctor;
        } else{

        }
    }

    public void get(){}

    public void remove(){}

    public int HashFunction(Doctor doctor){
        return doctor.id % 5;
    }
}
