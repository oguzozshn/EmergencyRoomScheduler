package DataStructures;

import Model.Patient;
import HelperClasses.Node;

public class BinarySearchTree {
    private Node root;

    public BinarySearchTree() {
        root = null;
    }

    public void insert(Patient patient) {
        root = insertRecursive(root, patient, null);
    }

    private Node insertRecursive(Node current, Patient patient, Node currentParent) {
        if (current == null) {
            Node newNode = new Node(patient);
            newNode.parent = currentParent;
            return newNode;
        }

        if (patient.patientId.compareTo(current.patient.patientId) < 0) {
            current.left = insertRecursive(current.left, patient, current);
        } else if (patient.patientId.compareTo(current.patient.patientId) > 0) {
            current.right = insertRecursive(current.right, patient, current);
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

    private Node findNode(Node current, String patientId) {
        if (current == null) {
            return null;
        }
        if (patientId.compareTo(current.patient.patientId) == 0) {
            return current;
        }
        if (patientId.compareTo(current.patient.patientId) < 0) {
            return findNode(current.left, patientId);
        } else {
            return findNode(current.right, patientId);
        }
    }

    // --- DELETE (Search fonksiyonunu kullanan recursive yapı) ---
    public void delete(Patient patient) {
        if (patient == null || root == null) return;

        // Yazdığımız yardımcı metotla doğrudan Node'u buluyoruz
        Node targetNode = findNode(root, patient.patientId);

        if (targetNode == null) {
            System.out.println("Hasta bulunamadı.");
            return;
        }

        // Düğümü ağaçtan recursive olarak silmesi için yardımcı metodu çağırıyoruz
        deleteNodeRecursive(targetNode);
    }

    private void deleteNodeRecursive(Node target) {
        // Durum 3: İki çocuğu da varsa (Successor ile yer değiştirme)
        if (target.left != null && target.right != null) {
            Node successor = findMin(target.right);
            target.patient = successor.patient; // Veriyi kopyala
            deleteNodeRecursive(successor);    // Alttaki successor düğümünü silmek için tekrar çağır
        }
        // Durum 1 & 2: En fazla 1 çocuğu varsa veya yaprak düğümse
        else {
            Node child = (target.left != null) ? target.left : target.right;

            if (child != null) {
                child.parent = target.parent; // Çocuğun ebeveyn bağını güncelle
            }

            if (target.parent == null) { // Kök düğüm siliniyorsa
                root = child;
            } else if (target == target.parent.left) { // Sol çocuk siliniyorsa
                target.parent.left = child;
            } else { // Sağ çocuk siliniyorsa
                target.parent.right = child;
            }
        }
    }

    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }


    public void inOrder() {}
}
