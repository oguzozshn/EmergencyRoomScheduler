package HelperClasses;
import Model.Patient;

public class BSTNode {
    public Patient patient;
    public BSTNode left;
    public BSTNode right;
    public BSTNode parent;

    public BSTNode(Patient patient) {
        this.patient = patient;
        left = right = parent = null;
    }
}
