import HelperClasses.LLNode;
import Model.Doctor;
import DataStructures.*;

void main() {
    Doctor doctor = new Doctor(1, "Oguz", "Available");
    Doctor doctor1 = new Doctor(2, "Dilara", "Unavailable");
    Doctor doctor2 = new Doctor(3, "Dilem", "Available");
    Doctor doctor3 = new Doctor(4, "Bengü", "Unavailable");
    Doctor doctor4 = new Doctor(5, "Hamdi", "Available");
    Doctor doctor5 = new Doctor(6, "Beren", "Unavailable");
    Doctor doctor6 = new Doctor(7, "Eren", "Available");
    Doctor doctor7 = new Doctor(8, "Yağız", "Unavailable");
    Doctor doctor8 = new Doctor(9, "Mert", "Available");

    HashMap hashMap = new HashMap();
    hashMap.put(doctor);
    hashMap.put(doctor1);
    hashMap.put(doctor2);
    hashMap.put(doctor3);
    hashMap.put(doctor4);
    hashMap.put(doctor5);
    hashMap.put(doctor6);
    hashMap.put(doctor7);
    hashMap.put(doctor8);

    System.out.println("=== HASH MAP ZİNCİRLEME GÖRSELLEŞTİRME ===");

    int bucketIndex = 0;
    for (LLNode node : hashMap.table) {
        System.out.print("Index [" + bucketIndex + "]: ");
        LLNode current = node;

        while (current != null) {
            Doctor doc = (Doctor) current.object;

            System.out.print("[" + doc.id + " - " + doc.name + " (" + doc.status + ")] -> ");

            current = current.next;
        }

        System.out.println("null (Zincir Bitti)");
        bucketIndex++;
        }
}
