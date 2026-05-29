import HelperClasses.LLNode;
import Model.Doctor;
import DataStructures.*;

void main() {
    Doctor doctor1 = new Doctor(1, "Oguz1", "Available");
    Doctor doctor2 = new Doctor(2, "Oguz2", "Available");
    Doctor doctor3 = new Doctor(3, "Oguz3", "Available");
    Doctor doctor4 = new Doctor(4, "Oguz4", "Available");
    Doctor doctor5 = new Doctor(5, "Oguz5", "Available");
    Doctor doctor6 = new Doctor(6, "Oguz6", "Available");
    Doctor doctor7 = new Doctor(7, "Oguz7", "Available");
    Doctor doctor8 = new Doctor(8, "Oguz8", "Available");

    LinkedList list = new LinkedList();
    list = list.add(list, doctor1);
    list = list.add(list, doctor2);
    list = list.add(list, doctor3);
    list = list.add(list, doctor4);
    list = list.add(list, doctor5);
    list = list.add(list, doctor6);
    list = list.add(list, doctor7);
    list = list.add(list, doctor8);

    list.printList(list);

}
