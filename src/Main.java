import DataStructures.*;
import Model.*;
import java.io.*;
import java.util.Scanner;
import HelperClasses.*;

/**
 * The Main class represents a hospital management system with core functionalities
 * for managing doctors, patients, rooms, and the hospital's operational processes. It
 * provides methods for initializing the system, assigning resources, and handling
 * patient-related operations through various data structures such as a binary search tree,
 * hashmaps, and graphs.
 */
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

    /**
     * The main method serves as the entry point to the application, initializing key data structures,
     * loading patient data, assigning doctors and treatment rooms to patients, handling patient discharge,
     * performing patient searches, and printing the final system summary.
     *
     * @param args Command-line arguments; not used in this implementation.
     */
    public static void main(String[] args) {
        Graph erGraph = initializeGraph();
        HashMap doctorMap = initializeDoctors();
        BinarySearchTree patientTree = new BinarySearchTree();
        Stack actionStack = new Stack(100);

        Scanner scanner = new Scanner(System.in);
        int totalAdmitted = loadPatientsFromFile(patientTree, scanner, actionStack, currentTime);

        Queue dischargeQueue = new Queue(10);

        // patientTree filled in loadPatientsFromFile()
        while (patientTree.getRoot() != null) {
            Patient nextPatient = findHighestPriorityPatient(patientTree.getRoot());
            if (nextPatient == null) break;

            boolean assigned = assignDoctorAndRoom(nextPatient, doctorMap, erGraph, actionStack);
            if (assigned) {
                dischargeQueue.enqueue(nextPatient);
                patientTree.delete(nextPatient);
            }else {
                break;
            }
        }

        int totalDischarged = 0;
        while (!dischargeQueue.isEmpty()) {
            dischargePatient(dischargeQueue, patientTree, erGraph);
            totalDischarged++;
        }

        searchAndPrintPatient(patientTree, scanner);

        printSystemSummary(patientTree, doctorMap, totalAdmitted, totalDischarged);
        scanner.close();
    }

    /**
     * Recursively traverses a binary search tree (BST) to find the patient with the highest priority based on their priority score.
     * In cases where priority scores are equal, the patient with the earlier arrival time is considered to have higher priority.
     *
     * @param current The current node of the BST being evaluated in the traversal process.
     * @return The patient with the highest priority in the subtree rooted at the specified node,
     * or null if the subtree is empty (i.e., the current node is null).
     */
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

    /**
     * Searches through the given HashMap of doctors and finds the first doctor
     * with a status of "AVAILABLE".
     *
     * @param doctorMap The HashMap containing the doctors, where each entry
     *                  represents a doctor object mapped to a specific key.
     * @return The first doctor with a status of "AVAILABLE", or null if no
     *         available doctor is found.
     */
    private static Doctor findFirstAvailableDoctor(HashMap doctorMap) {
        HashMapNode[] table = doctorMap.getTable();

        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                HashMapNode node = table[i];

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

    /**
     * Counts the number of doctors with a status of "BUSY" in the given HashMap of doctors.
     *
     * @param doctorMap A HashMap containing doctors, where each entry represents
     *                  a doctor object mapped to a specific key.
     * @return The total number of doctors in the HashMap whose status is "BUSY".
     */
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

    /**
     * Initializes and constructs a graph structure representing the relationships and connections
     * between different areas of a hospital emergency room (ER). The graph includes vertices
     * representing specific locations such as Reception, Treatment Rooms, ICU, Waiting Room,
     * and Discharge, as well as edges representing the pathways or connections between them.
     *
     * @return A Graph object representing the hospital's ER layout with predefined vertices
     * and edges.
     */
    private static Graph initializeGraph() {
        Graph erGraph = new Graph();

        erGraph.addVertex(Reception.id);
        erGraph.addVertex(TreatmentRoomA.id);
        erGraph.addVertex(TreatmentRoomB.id);
        erGraph.addVertex(ICU.id);
        erGraph.addVertex(WaitingRoom.id);
        erGraph.addVertex(Discharge.id);

        erGraph.addEdge(Reception.id, TreatmentRoomA.id, true);
        erGraph.addEdge(Reception.id, WaitingRoom.id,true);
        erGraph.addEdge(TreatmentRoomA.id, TreatmentRoomB.id,true);
        erGraph.addEdge(TreatmentRoomA.id, ICU.id,true);
        erGraph.addEdge(WaitingRoom.id, TreatmentRoomB.id,true);
        erGraph.addEdge(TreatmentRoomB.id, Discharge.id, true);

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

    /**
     * Loads patient data from a file, parses the information, and inserts the patient records into a binary search tree.
     * Each successfully loaded patient is also recorded as an "INTAKE" operation in the provided action stack.
     *
     * @param patientTree The binary search tree where the patient records will be inserted.
     * @param scanner The scanner used for user input to specify the file path.
     * @param actionStack The stack used for tracking actions, such as patient intake.
     * @param currentTime The current system time, used to calculate patient attributes.
     * @return The number of patients successfully loaded into the binary search tree.
     */
    private static int loadPatientsFromFile(BinarySearchTree patientTree, Scanner scanner, Stack actionStack, int currentTime) {
        int count = 0;
        System.out.println("=== Emergency Room (ER) ===");
        System.out.print("Please enter the full path of the patient list file: ");

        String filePath = scanner.nextLine();

        try {
            Scanner fileScanner = new Scanner(new File(filePath));

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                //tokens is an array of strings, each representing a comma-separated token in the line
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

                    System.out.println("[Intake Successful] " + patientId + " " + name + " added to patient tree.");
                }
            }
            fileScanner.close();

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        return count;
    }

    /**
     * Assigns a doctor and a treatment room to a patient based on their severity level,
     * and updates the doctor's status and patient details accordingly. If no available
     * doctor is found, a warning is printed.
     *
     * @param patient The patient who needs to be assigned a doctor and a treatment room.
     * @param doctorMap A HashMap containing the available doctors, where each entry
     *                  represents a doctor object mapped to a specific key.
     * @param erGraph The hospital's graph structure representing rooms and their
     *                connections, used for determining the path for the patient.
     * @param actionStack A stack recording actions performed during the assignment process,
     *                    such as doctor and room assignments.
     */
    private static boolean assignDoctorAndRoom(Patient patient, HashMap doctorMap, Graph erGraph, Stack actionStack) {
        Doctor availableDoctor = findFirstAvailableDoctor(doctorMap);

        if (availableDoctor != null) {

            patient.assignedDocId = availableDoctor.id;
            availableDoctor.treatedCount++;

            //This suggests a doctor can treat 2 patients in a day
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
            return true;
        } else {
            return false;
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
            System.out.println(dischargedPatient.name + " successfully discharged.");
        } else {
            System.out.println("\n[Warning]: No patient to discharge!");
        }
    }
    private static void printSystemSummary(BinarySearchTree patientTree, HashMap doctorMap, int totalAdmitted, int totalDischarged) {
        System.out.println("=== ER END OF THE DAY ===");

        int stillInSystem = totalAdmitted - totalDischarged;
        System.out.println("\n📊 Number of Patients:");
        System.out.println("Total admitted patient: " + totalAdmitted);
        System.out.println("Patients discharged: " + totalDischarged );
        System.out.println("Patients still in the hospital: " + stillInSystem);

        System.out.println("\n👥 Patients still in the hospital (BST InOrder):");
        if (stillInSystem > 0) {
            System.out.println("Patients");
            patientTree.inOrder();
        } else {
            System.out.println("No patients");
        }

        int busyDoctors      = countBusyDoctors(doctorMap);
        int totalDoctors     = 4;
        int availableDoctors = totalDoctors - busyDoctors;

        System.out.println("\n Doctor Status:");
        System.out.println("Total Doctors: " + totalDoctors);
        System.out.println("Busy Doctors: " + busyDoctors);
        System.out.println("Available Doctors: " + availableDoctors);
    }

    /**
     * Searches for a patient in the binary search tree (BST) using a provided patient ID
     * and prints the patient's details if found. If the patient is not found, an
     * appropriate message is displayed.
     *
     * @param patientTree The binary search tree containing patient records.
     * @param scanner The scanner used to retrieve user input for the patient ID to search.
     */
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
