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
    static Doctor d1 = new Doctor(101, "Dr. Alice Carter","AVAILABLE");
    static Doctor d2 = new Doctor(202, "Dr. Ben Nguyen","AVAILABLE");
    static Doctor d3 = new Doctor(303, "Dr. Clara Hassan","AVAILABLE");
    static Doctor d4 = new Doctor(404, "Dr. David Reyes","AVAILABLE");

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
                undoLastAction(actionStack, doctorMap);

                if (findFirstAvailableDoctor(doctorMap) == null) {
                    System.out.println("\nwarning: No available doctors. System halted.");
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
    private static Doctor findFirstAvailableDoctor(HashMap doctorMap) {
        HelperClasses.HashMapNode[] table = doctorMap.getTable();

        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                HelperClasses.HashMapNode node = table[i];

                while (node != null) {
                    Doctor doctor = (Doctor) node.value;

                    if (doctor.status.equals("AVAILABLE")) {
                        return doctor;
                    }
                    node = node.next;
                }
            }
        }

        return null;
    }

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

        erGraph.addEdge(Reception.id,     TreatmentRoomA.id, true);
        erGraph.addEdge(Reception.id,     WaitingRoom.id,    true);
        erGraph.addEdge(TreatmentRoomA.id, TreatmentRoomB.id, true);
        erGraph.addEdge(TreatmentRoomA.id, ICU.id,           true);
        erGraph.addEdge(WaitingRoom.id,   TreatmentRoomB.id, true);
        erGraph.addEdge(TreatmentRoomB.id, Discharge.id,     true);

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
        System.out.println("=== Emergency Room (ER) ===");
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

                    System.out.println("[Intake Successful] " + patientId + name + "added to patient tree.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return count;
    }

    private static void assignDoctorAndRoom(Patient patient, HashMap doctorMap, Graph erGraph, Stack actionStack) {
        Doctor availableDoctor = findFirstAvailableDoctor(doctorMap);

        if (availableDoctor != null) {

            patient.assignedDocId = availableDoctor.id;
            availableDoctor.treatedCount++;

            if (availableDoctor.treatedCount >= 2) {
                availableDoctor.status = "BUSY";
            }

            String targetRoom;
            if (patient.severity == 1) {
                targetRoom = ICU.id;
                patient.assignedRoom = 3;
            } else if (patient.severity <= 3) {
                targetRoom = TreatmentRoomA.id;
                patient.assignedRoom = 1;
            } else {
                targetRoom = TreatmentRoomB.id;
                patient.assignedRoom = 2;
            }

            actionStack.push("ASSIGN:" + patient.patientId + ":" + availableDoctor.id);
            erGraph.BFS(Reception.id, targetRoom);
        } else {
            System.out.println("\n[Warning]: No available doctor found!");
        }
    }

    private static void dischargePatient(Queue dischargeQueue, BinarySearchTree patientTree, Graph erGraph) {
        if (!dischargeQueue.isEmpty()) {
            System.out.println("\n=== Patient Discharge ===");

            Patient dischargedPatient = dischargeQueue.dequeue();
            System.out.println("Discharged patient: " + dischargedPatient.name);

            if (dischargedPatient.assignedRoom != -1) {
                String treatmentRoom = "R" + dischargedPatient.assignedRoom;
                System.out.println("\nDischarge path: " + treatmentRoom + " → R5");
                erGraph.BFS(treatmentRoom, "R5");
            }
            patientTree.delete(dischargedPatient);
            System.out.println("[✓] " + dischargedPatient.name + " successfully discharged.");
        } else {
            System.out.println("\n[Warning]: No patient to discharge!");
        }
    }
    private static void printSystemSummary(BinarySearchTree patientTree, HashMap doctorMap, int totalAdmitted, int totalDischarged) {
        System.out.println("=== ER END OF THE DAY ===");

        int stillInSystem = totalAdmitted - totalDischarged;
        System.out.println("\n📊 Number of Patients:");
        System.out.println("   ├─ Total admitted patient              : " + totalAdmitted);
        System.out.println("   ├─ Patients discharged                 : " + totalDischarged );
        System.out.println("   └─ Patients still in the hospital      : " + stillInSystem);

        System.out.println("\n👥 Patients still in the hospital (BST InOrder):");
        if (stillInSystem > 0) {
            System.out.println("   ├─ Patients");
            patientTree.inOrder();
        } else {
            System.out.println("   └─ No patients");
        }

        int busyDoctors      = countBusyDoctors(doctorMap);
        int totalDoctors     = 4;
        int availableDoctors = totalDoctors - busyDoctors;

        System.out.println("\n DOKTOR DURUMU:");
        System.out.println("   ├─ Total Doctors            : " + totalDoctors);
        System.out.println("   ├─ BUsy Doctors             : " + busyDoctors);
        System.out.println("   └─ Available Doctors        : " + availableDoctors);
    }

    private static void undoLastAction(Stack actionStack, HashMap doctorMap) {
        if (actionStack.isEmpty()) {
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
            }
        }
    }

    private static void searchAndPrintPatient(BinarySearchTree patientTree, Scanner scanner) {
        System.out.print("\nType the patient ID to search:");
        String searchId = scanner.nextLine();
        Patient foundPatient = patientTree.search(searchId);

        if (foundPatient != null) {
            System.out.println("\nPatient found: " + foundPatient.name + " (ID: " + foundPatient.patientId);
        } else {
            System.out.println("\nPatient could not be found.");
        }
    }
}
