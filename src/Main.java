import DataStructures.*;
import Model.*;
import java.io.*;
import java.util.Scanner;
import HelperClasses.BSTNode;


public class Main {
    static Room Reception = new Room("RO", "Reception");
    static Room TreatmentRoomA = new Room("R1", "Treatment Room A");
    static Room TreatmentRoomB = new Room("R2", "Treatment Room B");
    static Room ICU = new Room("R3", "ICU");
    static Room WaitingRoom = new Room("R4", "Waiting Room");
    static Room Discharge = new Room("R5", "Discharge");
    static int currentTime = 10;
    static Doctor d1 = new Doctor(101, "Dr. Alice Carter",  "AVAILABLE");
    static Doctor d2 = new Doctor(202, "Dr. Ben Nguyen",    "AVAILABLE");
    static Doctor d3 = new Doctor(303, "Dr. Clara Hassan",  "AVAILABLE");
    static Doctor d4 = new Doctor(404, "Dr. David Reyes",   "AVAILABLE");

    public static void main(String[] args) {
        Graph erGraph = initializeGraph();
        HashMap doctorMap = initializeDoctors();
        BinarySearchTree patientTree = new BinarySearchTree();
        Stack actionStack = new Stack(100);

        Scanner scanner = new Scanner(System.in);
        int totalAdmitted = loadPatientsFromFile(patientTree, scanner, actionStack, currentTime);

        Queue dischargeQueue = new Queue(10);

        while (patientTree.getRoot() != null) {
            Patient nextPatient = findHighestPriorityPatient(patientTree.getRoot());
            if (nextPatient == null) break;

            Doctor availableDoctor = findFirstAvailableDoctor(doctorMap);

            if (availableDoctor == null) {
                // Müsait doktor kalmadı, undo ile available yap
                undoLastAction(actionStack, doctorMap);

                // Tekrar kontrol et, hala yoksa limit dolmuş, çık
                if (findFirstAvailableDoctor(doctorMap) == null) {
                    System.out.println("\n[UYARI]: Günlük doktor limiti doldu, discharge'a geçiliyor...");
                    break;
                }
                continue;
            }

            assignDoctorAndRoom(nextPatient, doctorMap, erGraph, actionStack);
            dischargeQueue.enqueue(nextPatient);
            patientTree.delete(nextPatient);
        }

        // Gün sonu discharge
        int totalDischarged = 0;
        while (!dischargeQueue.isEmpty()) {
            dischargePatient(dischargeQueue, patientTree, erGraph);
            totalDischarged++;
        }

        searchAndPrintPatient(patientTree, scanner);

        printSystemSummary(patientTree, doctorMap, totalAdmitted, totalDischarged);
        scanner.close();
    }

    private static void printPatientRecord(Patient patient) {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║        HASTA KAYDı DETAYLARI           ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("📋 Hasta ID        : " + patient.patientId);
        System.out.println("👤 Adı Soyadı      : " + patient.name);
        System.out.println("📅 Yaş             : " + patient.age);
        System.out.println("🚨 Aciliyet Derecesi : " + patient.severity + "/5");
        System.out.println("🕐 Geliş Zamanı    : " + patient.arrivalTime);
        System.out.println("⭐ Öncelik Skoru   : " + patient.priorityScore);
        System.out.println("🏥 Atanan Oda     : " + (patient.assignedRoom != -1 ? "R" + patient.assignedRoom : "Belirlenmedi"));
        System.out.println("👨‍⚕️ Atanan Doktor   : " + (patient.assignedDocId != -1 ? "Dr. ID: " + patient.assignedDocId : "Belirlenmedi"));
        System.out.println("════════════════════════════════════════\n");
    }

    // === EN YÜKSEK PRİYORİTE HASTASINI BULAN HELPER METODU ===
    private static Patient findHighestPriorityPatient(BSTNode current) {
        if (current == null) {
            return null;
        }

        Patient maxPatient = current.patient;

        if (current.left != null) {
            Patient leftMax = findHighestPriorityPatient(current.left);
            if (leftMax != null && (leftMax.priorityScore > maxPatient.priorityScore ||
                    (leftMax.priorityScore == maxPatient.priorityScore &&
                            leftMax.arrivalTime < maxPatient.arrivalTime))) {
                maxPatient = leftMax;
            }
        }

        if (current.right != null) {
            Patient rightMax = findHighestPriorityPatient(current.right);
            if (rightMax != null && (rightMax.priorityScore > maxPatient.priorityScore ||
                    (rightMax.priorityScore == maxPatient.priorityScore &&
                            rightMax.arrivalTime < maxPatient.arrivalTime))) {
                maxPatient = rightMax;
            }
        }

        return maxPatient;
    }

    // === İLK AVAILABLE (MÜSAIT) DOKTORU BULAN HELPER METODU ===
    private static Doctor findFirstAvailableDoctor(HashMap doctorMap) {
        // HashMap'in internal table'ına eriş
        HelperClasses.HashMapNode[] table = doctorMap.getTable();

        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                HelperClasses.HashMapNode node = table[i];

                while (node != null) {
                    Doctor doctor = (Doctor) node.value;

                    // Eğer doktor müsaitse onu döndür
                    if (doctor.status.equals("AVAILABLE")) {
                        return doctor;
                    }
                    node = node.next;
                }
            }
        }

        return null; // Müsait doktor yok
    }

    // === TOPLAM HASTA SAYISINI BULAN HELPER METODU ===
    private static int countTotalPatients(BSTNode current) {
        if (current == null) {
            return 0;
        }
        return 1 + countTotalPatients(current.left) + countTotalPatients(current.right);
    }

    // === MEŞGUL DOKTOR SAYISINI BULAN HELPER METODU ===
    private static int countBusyDoctors(HashMap doctorMap) {
        HelperClasses.HashMapNode[] table = doctorMap.getTable();
        int busyCount = 0;

        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                HelperClasses.HashMapNode node = table[i];

                while (node != null) {
                    Doctor doctor = (Doctor) node.value;
                    if (doctor.status.equals("BUSY")) {
                        busyCount++;
                    }
                    node = node.next;
                }
            }
        }

        return busyCount;
    }

    // === MEŞGUL DOKTORLARI LISTELEYEN HELPER METODU ===
    private static void listBusyDoctors(HashMap doctorMap) {
        HelperClasses.HashMapNode[] table = doctorMap.getTable();

        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                HelperClasses.HashMapNode node = table[i];

                while (node != null) {
                    Doctor doctor = (Doctor) node.value;
                    if (doctor.status.equals("BUSY")) {
                        System.out.println("      └─ [" + doctor.id + "] " + doctor.name + " - Status: " + doctor.status);
                    }
                    node = node.next;
                }
            }
        }
    }

    private static Graph initializeGraph() {
        Graph erGraph = new Graph();

        erGraph.addVertex(Reception.id);
        erGraph.addVertex(TreatmentRoomA.id);
        erGraph.addVertex(TreatmentRoomB.id);
        erGraph.addVertex(ICU.id);
        erGraph.addVertex(WaitingRoom.id);
        erGraph.addVertex(Discharge.id);

        erGraph.addEdge(Reception.id,     TreatmentRoomA.id, false);
        erGraph.addEdge(Reception.id,     WaitingRoom.id,    false);
        erGraph.addEdge(TreatmentRoomA.id, TreatmentRoomB.id, false);
        erGraph.addEdge(TreatmentRoomA.id, ICU.id,           false);
        erGraph.addEdge(WaitingRoom.id,   TreatmentRoomB.id, false);
        erGraph.addEdge(TreatmentRoomB.id, Discharge.id,     false);

        return erGraph;
    }

    private static HashMap initializeDoctors() {
        HashMap doctorMap = new HashMap();

        doctorMap.put(d1.id, d1);
        doctorMap.put(d2.id, d2);
        doctorMap.put(d3.id, d3);
        doctorMap.put(d4.id, d4);

        return doctorMap;
    }
    private static int loadPatientsFromFile(BinarySearchTree patientTree, Scanner scanner, Stack actionStack, int currentTime) {
        int count = 0;
        System.out.println("=== ACİL SERVİS SİSTEMİ VERİ YÜKLEME ===");
        System.out.print("Lütfen hasta listesi dosyasının (patient.txt) tam yolunu giriniz: ");

        String filePath = scanner.nextLine();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");

                if (tokens.length >= 5) {
                    String patientId  = tokens[0].trim();
                    String name       = tokens[1].trim();
                    int age           = Integer.parseInt(tokens[2].trim());
                    int severity      = Integer.parseInt(tokens[3].trim());
                    int arrivalTime   = Integer.parseInt(tokens[4].trim());

                    Patient newPatient = new Patient(patientId, name, age, severity, arrivalTime, currentTime);
                    actionStack.push("INTAKE:" + patientId);
                    patientTree.insert(newPatient);
                    count++;

                    System.out.println("[KABUL BAŞARILI] " + patientId + " kodlu " + name + " ağaca eklendi.");
                }
            }

            System.out.println("\n[SİSTEM]: Tüm hastalar başarıyla okundu ve BST'ye kaydedildi.");
            System.out.println("\n=== BST GÜNCEL DURUM (InOrder Sıralı) ===");
            patientTree.inOrder();

        } catch (IOException e) {
            System.out.println("\n[HATA]: Dosya okunamadı! Girdiğiniz dizin yolunu kontrol edin.");
            System.out.println("Girilen Yol: " + filePath);
        } catch (NumberFormatException e) {
            System.out.println("\n[HATA]: Sayısal alanlarda hatalı format var!");
        }

        return count;
    }

    private static void assignDoctorAndRoom(Patient patient, HashMap doctorMap, Graph erGraph, Stack actionStack) {
        Doctor availableDoctor = findFirstAvailableDoctor(doctorMap);

        if (availableDoctor != null) {
            System.out.println("\n=== DOKTOR ATAMASI ===");
            System.out.println("Müsait Doktor Bulundu: [" + availableDoctor.id + "] " + availableDoctor.name);

            patient.assignedDocId = availableDoctor.id;
            patient.assignedRoom  = 1;
            availableDoctor.treatedCount++;

            if (availableDoctor.treatedCount >= 2) {
                availableDoctor.status = "BUSY"; // 2 hasta tedavi etti, gün sonu busy
            }

            actionStack.push("ASSIGN:" + patient.patientId + ":" + availableDoctor.id);

            System.out.println("[✓] " + patient.name + " -> " + availableDoctor.name + " (Oda: R1)");
            erGraph.BFSPath(Reception.id, "R1");
        } else {
            System.out.println("\n[UYARI]: Müsait doktor bulunamadı!");
        }
    }

    private static void dischargePatient(Queue dischargeQueue, BinarySearchTree patientTree, Graph erGraph) {
        if (!dischargeQueue.isEmpty()) {
            System.out.println("\n=== HASTANIN TABURCU PROSESİ ===");

            Patient dischargedPatient = dischargeQueue.dequeue();
            System.out.println("[→] Kuyruktan çıkarılan hasta: " + dischargedPatient.name);

            if (dischargedPatient.assignedRoom != -1) {
                String treatmentRoom = "R1";
                System.out.println("\n[📍] Taburcu yolu: " + treatmentRoom + " → R5");
                erGraph.BFSPath(treatmentRoom, "R5");
            }

            System.out.println("\n[🗑️] Hasta BST'den siliniyor...");
            patientTree.delete(dischargedPatient);
            System.out.println("[✓] " + dischargedPatient.name + " başarıyla taburcu edildi!");
            System.out.println("[✓] Hasta veri tabanından silindi.");
        } else {
            System.out.println("\n[UYARI]: Taburcu edilecek hasta yok!");
        }
    }
    private static void printSystemSummary(BinarySearchTree patientTree, HashMap doctorMap, int totalAdmitted, int totalDischarged) {
        System.out.println("\n╔═══════════════════════════════════════════════════════╗");
        System.out.println("║          ACİL SERVİS SİSTEMİ - SISTEM ÖZETİ           ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");

        int stillInSystem = totalAdmitted - totalDischarged;
        System.out.println("\n📊 HASTA SAYILARI:");
        System.out.println("   ├─ Toplam Kabul Edilen  : " + totalAdmitted);
        System.out.println("   ├─ Taburcu Edilen       : " + totalDischarged );
        System.out.println("   └─ Halen Sistemde       : " + stillInSystem);

        System.out.println("\n👥 HELENKİ HASTALAR (BST InOrder):");
        if (stillInSystem > 0) {
            System.out.println("   ├─ Hasta Listesi:");
            patientTree.inOrder();
        } else {
            System.out.println("   └─ Hasta yok!");
        }

        int busyDoctors      = countBusyDoctors(doctorMap);
        int totalDoctors     = 4;
        int availableDoctors = totalDoctors - busyDoctors;

        System.out.println("\n👨‍⚕️ DOKTOR DURUMU:");
        System.out.println("   ├─ Toplam Doktor        : " + totalDoctors);
        System.out.println("   ├─ Meşgul Doktor        : " + busyDoctors);
        System.out.println("   └─ Müsait Doktor        : " + availableDoctors);

        if (busyDoctors > 0) {
            System.out.println("\n   Meşgul Doktorlar:");
            listBusyDoctors(doctorMap);
        }

        System.out.println("\n╚═══════════════════════════════════════════════════════╝\n");
    }

    private static void undoLastAction(Stack actionStack, HashMap doctorMap) {
        if (actionStack.isEmpty()) {
            System.out.println("\n[UYARI]: Geri alınacak işlem yok!");
            return;
        }

        String lastAction = (String) actionStack.pop();
        String[] parts = lastAction.split(":");

        System.out.println("\n=== UNDO ===");

        if (parts[0].equals("ASSIGN")) {
            int doctorId = Integer.parseInt(parts[2]);
            Doctor doctor = (Doctor) doctorMap.get(doctorId);
            if (doctor != null) {
                doctor.status = "AVAILABLE";
                System.out.println("[✓] Geri alındı: " + doctor.name + " tekrar müsait.");
            }
        } else if (parts[0].equals("INTAKE")) {
            System.out.println("[✓] Geri alındı: " + parts[1] + " intake işlemi iptal edildi.");
        }
    }

    private static void searchAndPrintPatient(BinarySearchTree patientTree, Scanner scanner) {
        System.out.print("\nAranacak hasta ID'sini giriniz (örn: P001): ");
        String searchId = scanner.nextLine();
        Patient foundPatient = patientTree.searchById(searchId);

        if (foundPatient != null) {
            System.out.println("\n[✓] HASTA BULUNDU!");
            printPatientRecord(foundPatient);
        } else {
            System.out.println("\n[✗] Hasta bulunamadı! ID: " + searchId);
        }
    }
}
