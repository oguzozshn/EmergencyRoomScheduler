package DataStructures;

import Model.Patient;
import HelperClasses.BSTNode;

public class BinarySearchTree {
    private BSTNode root;

    public BinarySearchTree() {
        root = null;
    }

    public void insert(Patient patient) {
        root = insertRecursive(root, patient, null);
    }

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

    public Patient search(Patient patient) {
        BSTNode result = searchRecursive(root, patient);
        return result != null ? result.patient : null;
    };

    private BSTNode searchRecursive(BSTNode current, Patient patient) {
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

    private BSTNode findNode(BSTNode current, String patientId) {
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
        BSTNode targetBSTNode = findNode(root, patient.patientId);

        if (targetBSTNode == null) {
            System.out.println("Hasta bulunamadı.");
            return;
        }

        // Düğümü ağaçtan recursive olarak silmesi için yardımcı metodu çağırıyoruz
        deleteNodeRecursive(targetBSTNode);
    }

    private void deleteNodeRecursive(BSTNode target) {
        // Durum 3: İki çocuğu da varsa (Successor ile yer değiştirme)
        if (target.left != null && target.right != null) {
            BSTNode successor = findMin(target.right);
            target.patient = successor.patient; // Veriyi kopyala
            deleteNodeRecursive(successor);    // Alttaki successor düğümünü silmek için tekrar çağır
        }
        // Durum 1 & 2: En fazla 1 çocuğu varsa veya yaprak düğümse
        else {
            BSTNode child = (target.left != null) ? target.left : target.right;

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

    private BSTNode findMin(BSTNode BSTNode) {
        while (BSTNode.left != null) {
            BSTNode = BSTNode.left;
        }
        return BSTNode;
    }

    public void inOrder() {
        inOrderRecursive(root);
        System.out.println();
    }

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

    // Helper method: Search by patientId (String)
    public Patient searchById(String patientId) {
        BSTNode node = findNode(root, patientId);
        return node != null ? node.patient : null;
    }

    // Root düğümünü geri döndür
    public BSTNode getRoot() {
        return root;
    }
}
