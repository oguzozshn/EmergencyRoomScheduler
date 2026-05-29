package DataStructures;
import Model.Doctor;
import HelperClasses.LLNode;

public class HashMap {
    private Doctor[] doctors;
    private LinkedList linkedList;
    HashMap(){
        doctors = new Doctor[5];
        linkedList = new LinkedList();
    }

    public void put(Doctor doctor){
        int index = HashFunction(doctor);
        if (doctors[index] == null){
            doctors[index] = doctor;
        } else{
            doctors[index] = linkedList.add(doctor);
        }
    }

    public void get(){}

    public void remove(){}

    public int HashFunction(Doctor doctor){
        return doctor.id % 5;
    }
}
