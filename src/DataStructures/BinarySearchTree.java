package DataStructures;

import Model.Patient;
import HelperClasses.Node;

public class BinarySearchTree {
    private Node root;

    public BinarySearchTree() {
        root = null;
    }

    public void insert(Patient patient) {
        root = insertRecursive(root, patient);
    }

    private Node insertRecursive(Node current, Patient patient) {
        if (current == null) {
            return new Node(patient);
        }
        if (patient.patientId.compareTo(current.patient.patientId) < 0){
            current.left = insertRecursive(current.left, patient);
        }else if (patient.patientId.compareTo(current.patient.patientId) > 0) {
            current.right = insertRecursive(current.right, patient);
        }
        return current;
    }

    public Patient search(Patient patient) {
        Node result = searchRecursive(root, patient);
        return result != null ? result.patient : null;
    };

    private Node searchRecursive(Node current, Patient patient) {
        if (current == null) {
            return null;
        }
        if (patient.patientId.compareTo(current.patient.patientId) == 0) {
            return current;
        }
        if (patient.patientId.compareTo(current.patient.patientId) < 0) {
            return searchRecursive(current.left, patient);
        } else if (patient.patientId.compareTo(current.patient.patientId) > 0) {
            return searchRecursive(current.right, patient);
        }
        return null;
    }

    public void delete(Patient patient) {
        Patient tempPatient = search(patient);
        tempPatient = null;

    }

    public void inOrder() {}
}
