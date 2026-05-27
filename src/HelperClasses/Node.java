package HelperClasses;
import Model.Patient;

public class Node {
    public Patient patient;
    public Node left, right, parent;

    public Node(Patient patient) {
        this.patient = patient;
        left = right = parent = null;
    }
}
