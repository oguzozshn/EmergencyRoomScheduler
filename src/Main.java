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

        Scanner scanner = new Scanner(System.in);
        loadPatientsFromFile(patientTree, scanner);

        // === EN YÜKSEK PRİYORİTE HASTASINI BULMAK ===
        Patient highestPriorityPatient = findHighestPriorityPatient(patientTree.getRoot());

        if (highestPriorityPatient != null) {
            System.out.println("\n=== EN YÜKSEK PRİYORİTE HASTASI ===");
            System.out.println("[" + highestPriorityPatient.patientId + "] " +
                               highestPriorityPatient.name +
                               " - Priority Score: " + highestPriorityPatient.priorityScore +
                               " (Severity: " + highestPriorityPatient.severity + ")");

            // === İLK AVAILABLE DOKTORU BULMAK ===
            Doctor availableDoctor = findFirstAvailableDoctor(doctorMap);

            if (availableDoctor != null) {
                System.out.println("\n=== DOKTOR ATAMASI ===");
                System.out.println("Müsait Doktor Bulundu: [" + availableDoctor.id + "] " + availableDoctor.name);

                // Hastaya doktor ata
                highestPriorityPatient.assignedDocId = availableDoctor.id;

                // Hastaya oda ata (Treatment Room A'ya)
                highestPriorityPatient.assignedRoom = 1; // R1

                // Doktorun durumunu güncelle
                availableDoctor.status = "BUSY";

                System.out.println("[✓] " + highestPriorityPatient.name + " -> Dr. " +
                                   availableDoctor.name + " (Oda: R1)");

                // === BFS PATH YAZDIRMA ===
                erGraph.BFSPath(Reception.id, "R1");
            } else {
                System.out.println("\n[UYARI]: Müsait doktor bulunamadı!");
            }
        } else {
            System.out.println("\n[UYARI]: Ağaçta hasta yok!");
        }

        // === TEDAVI EDILEN HASTALARI QUEUE'YA EKLE ===
        Queue dischargeQueue = new Queue(10);

        // Örnek: En yüksek priority hastasını tedavi edilmiş olarak queue'ya ekle
        if (highestPriorityPatient != null) {
            highestPriorityPatient.actionStack.push("Treated");
            dischargeQueue.enqueue(highestPriorityPatient);
            System.out.println("\n=== QUEUE İŞLEMLERİ ===");
            System.out.println("[✓] Hasta kuyruğa eklendi: " + highestPriorityPatient.name);
        }

        // === QUEUE'DAN HASTAYI ÇIKAR, BFS YOLU GÖSTER VE BST'DEN SİL ===
        if (!dischargeQueue.isEmpty()) {
            System.out.println("\n=== HASTANIN TABURCU PROSESİ ===");

            Patient dischargedPatient = dischargeQueue.dequeue();
            System.out.println("[→] Kuyruktan çıkarılan hasta: " + dischargedPatient.name);

            // BFS yolunu göster: Treatment Room -> Discharge (R5)
            if (dischargedPatient.assignedRoom != -1) {
                String treatmentRoom = "R1"; // assignedRoom 1 = R1
                System.out.println("\n[📍] Taburcu yolu: " + treatmentRoom + " → R5");
                erGraph.BFSPath(treatmentRoom, "R5");
            }

            // Hastayı BST'den sil
            System.out.println("\n[🗑️] Hasta BST'den siliniyor...");
            patientTree.delete(dischargedPatient);
            System.out.println("[✓] " + dischargedPatient.name + " başarıyla taburcu edildi!");
            System.out.println("[✓] Hasta veri tabanından silindi.");
        } else {
            System.out.println("\n[UYARI]: Taburcu edilecek hasta yok!");
        }

        // === PATIENTID'YE GÖRE HASTA KAYDI ARAMA ===
        System.out.println("\n=== HASTA KAYDI ARAMA ===");
        System.out.print("Lütfen aranacak hasta ID'sini giriniz (örn: P001): ");
        String searchPatientId = scanner.nextLine();

        Patient foundPatient = patientTree.searchById(searchPatientId);

        if (foundPatient != null) {
            System.out.println("\n[✓] HASTA BULUNDU!");
            printPatientRecord(foundPatient);
        } else {
            System.out.println("\n[✗] Hasta bulunamadı! ID: " + searchPatientId);
        }

        // === SİSTEM İSTATİSTİKLERİ VE ÖZETİ ===
        System.out.println("\n╔═══════════════════════════════════════════════════════╗");
        System.out.println("║          ACİL SERVİS SİSTEMİ - SISTEM ÖZETİ           ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");

        // Hastaların toplam sayılarını hesapla
        int totalAdmitted = countTotalPatients(patientTree.getRoot());
        int totalDischargedEstimate = 1; // Discharged hasta sayısı (1 hasta taburcu edildi)
        int stillInSystem = totalAdmitted - totalDischargedEstimate;

        System.out.println("\n📊 HASTA SAYILARI:");
        System.out.println("   ├─ Toplam Kabul Edilen  : " + totalAdmitted);
        System.out.println("   ├─ Taburcu Edilen       : " + totalDischargedEstimate);
        System.out.println("   └─ Halen Sistemde       : " + stillInSystem);

        // Sistemde kalan hastaları listele
        System.out.println("\n👥 HELENKİ HASTALAR (BST InOrder):");
        if (stillInSystem > 0) {
            System.out.println("   ├─ Hasta Listesi:");
            patientTree.inOrder();
        } else {
            System.out.println("   └─ Hasta yok!");
        }

        // Doktor durumlarını kontrol et
        int busyDoctors = countBusyDoctors(doctorMap);
        int totalDoctors = 4; // d1, d2, d3, d4
        int availableDoctors = totalDoctors - busyDoctors;

        System.out.println("\n👨‍⚕️ DOKTOR DURUMU:");
        System.out.println("   ├─ Toplam Doktor        : " + totalDoctors);
        System.out.println("   ├─ Meşgul Doktor        : " + busyDoctors);
        System.out.println("   └─ Müsait Doktor        : " + availableDoctors);

        // Meşgul doktorları listele
        if (busyDoctors > 0) {
            System.out.println("\n   Meşgul Doktorlar:");
            listBusyDoctors(doctorMap);
        }

        System.out.println("\n╚═══════════════════════════════════════════════════════╝\n");

        scanner.close();
    }

    // === HASTA KAYDINI YAZDIRAN HELPER METODU ===
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

        // Sol alt ağaçta kontrol et
        if (current.left != null) {
            Patient leftMax = findHighestPriorityPatient(current.left);
            if (leftMax != null && leftMax.priorityScore > maxPatient.priorityScore) {
                maxPatient = leftMax;
            }
        }

        // Sağ alt ağaçta kontrol et
        if (current.right != null) {
            Patient rightMax = findHighestPriorityPatient(current.right);
            if (rightMax != null && rightMax.priorityScore > maxPatient.priorityScore) {
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
    private static void loadPatientsFromFile(BinarySearchTree patientTree, Scanner scanner) {
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

                    Patient newPatient = new Patient(patientId, name, age, severity, arrivalTime);
                    newPatient.actionStack.push("Intake");
                    patientTree.insert(newPatient);

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
    }
}
