package DataStructures;

// 🚨 EN ÖNEMLİ EKSİK: LLNode'u buraya tanıtıyoruz
import HelperClasses.LLNode;

public class LinkedList {
    private LLNode head;

    public LinkedList() {
        this.head = null;
    }

    // 🌟 Graph sınıfının BFS/DFS yaparken listeyi tarayabilmesi için bu şart:
    public LLNode getHead() {
        return this.head;
    }

    public void add(Object data) {
        LLNode newNode = new LLNode(data);
        if (this.head == null) {
            this.head = newNode;
        } else {
            LLNode last = this.head;
            while (last.next != null) {
                last = last.next;
            }
            last.next = newNode;
        }
    }

    public boolean contains(Object target) {
        LLNode current = this.head;
        while (current != null) {
            if (current.data.equals(target)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public int size() {
        int count = 0;
        LLNode current = this.head;
        while (current != null) {
            count++;
            current = current.next;
        }
        return count;
    }

    public void display() {
        LLNode current = this.head;
        while (current != null) {
            System.out.print(current.data + " ");
            current = current.next;
        }
        System.out.println();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        LLNode current = this.head;
        while (current != null) {
            sb.append(current.data).append(" ");
            current = current.next;
        }
        return sb.toString().trim();
    }

    public void addFirst(Object data) {
        LLNode newNode = new LLNode(data);
        newNode.next = this.head;
        this.head = newNode;
    }
}