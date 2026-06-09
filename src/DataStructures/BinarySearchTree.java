package DataStructures;

import Model.Patient;
import HelperClasses.BSTNode;

/**
 * BinarySearchTree class implements a binary search tree data structure for storing and searching patient records.
 */
public class BinarySearchTree {
    private BSTNode root;

    /**
     * Constructor for BinarySearchTree, initializes an empty tree.
     */
    public BinarySearchTree() {
        root = null;
    }

    /**
     * Inserts a patient into the binary search tree.
     * @param patient The patient to be inserted.
     */
    public void insert(Patient patient) {
        root = insertRecursive(root, patient, null);
    }

    /**
     * Recursive helper method to insert a patient into the binary search tree.
     * @param current The current node being processed.
     * @param patient The patient to be inserted.
     * @param currentParent The parent of the current node.
     * @return The updated current node after insertion.
     */
    private BSTNode insertRecursive(BSTNode current, Patient patient, BSTNode currentParent) {
        if (current == null) {
            BSTNode newBSTNode = new BSTNode(patient);
            newBSTNode.parent = currentParent;
            return newBSTNode;
        }

        if (patient.patientId.compareTo(current.patient.patientId) < 0) {
            current.left = insertRecursive(current.left, patient, current);
        } else if (patient.patientId.compareTo(current.patient.patientId) > 0) {
            current.right = insertRecursive(current.right, patient, current);
        }

        return current;
    }

    /**
     * Finds a node in the binary search tree recursively.
     * @param current The current node being checked.
     * @param patientId The patient ID to search for.
     * @return The found node or null if not found.
     */
    private BSTNode searchRecursive(BSTNode current, String patientId) {
        if (current == null) {
            return null;
        }
        if (patientId.compareTo(current.patient.patientId) == 0) {
            return current;
        }
        if (patientId.compareTo(current.patient.patientId) < 0) {
            return searchRecursive(current.left, patientId);
        } else {
            return searchRecursive(current.right, patientId);
        }
    }

    /**
     * Deletes a patient from the binary search tree.
     * @param patient The patient to delete.
     */
    public void delete(Patient patient) {
        if (patient == null || root == null) return;

        BSTNode targetBSTNode = searchRecursive(root, patient.patientId);

        if (targetBSTNode == null) {
            System.out.println("Patient not found in the tree.");
            return;
        }
        deleteNodeRecursive(targetBSTNode);
    }

    /**
     * Recursively deletes a node from the binary search tree. Depending on the node's structure,
     * it either replaces it with its in-order successor, handles its child, or updates the root.
     *
     * @param target The node to be deleted from the binary search tree.
     */
    private void deleteNodeRecursive(BSTNode target) {

        if (target.left != null && target.right != null) {
            BSTNode successor = findMinSubTree(target.right);
            target.patient = successor.patient;
            deleteNodeRecursive(successor);
        }

        else {
            BSTNode child = (target.left != null) ? target.left : target.right;

            if (child != null) {
                child.parent = target.parent;
            }

            if (target.parent == null) {
                root = child;
            } else if (target == target.parent.left) {
                target.parent.left = child;
            } else {
                target.parent.right = child;
            }
        }
    }

    /**
     * Finds the minimum node in the subtree rooted at BSTNode.
     * @param BSTNode
     * @return
     */
    private BSTNode findMinSubTree(BSTNode BSTNode) {
        while (BSTNode.left != null) {
            BSTNode = BSTNode.left;
        }
        return BSTNode;
    }

    /**
     * Performs an in-order traversal of the binary search tree, printing the patient details.
     */
    public void inOrder() {
        inOrderRecursive(root);
        System.out.println();
    }

    /**
     * Helper method: Recursive in-order traversal to print patient details.
     */
    private void inOrderRecursive(BSTNode current) {
        if (current == null) {
            return;
        }
        
        inOrderRecursive(current.left);
        System.out.println("[" + current.patient.patientId + "] " + 
                         current.patient.name + " (Age: " + current.patient.age + 
                         ", Severity: " + current.patient.severity + ")");
        inOrderRecursive(current.right);
    }

    /**
     * Searches for a patient by their ID in the binary search tree.
     * @param patientId The ID of the patient to search for.
     * @return The patient if found, otherwise null.
     */
    public Patient search(String patientId) {
        BSTNode node = searchRecursive(root, patientId);
        return node != null ? node.patient : null;
    }

    /**
     * Retrieves the root node of the binary search tree.
     * @return The root node of the tree.
     */
    public BSTNode getRoot() {
        return root;
    }
}
