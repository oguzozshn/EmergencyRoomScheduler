package DataStructures;
import HelperClasses.LLNode;
import Model.Doctor;

public class LinkedList {
    private LLNode head;

    public LinkedList() {
        this.head = null;
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

    public void display() {
        LLNode current = this.head;
        while (current != null) {
            System.out.print(current.data + " ");
            current = current.next;
        }
        System.out.println();
    }

    // 🌟 Grafın toString() metodunda komşuları metin olarak birleştirebilmesi için:
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

    public void printList(LinkedList list)
    {
        LLNode currNode = list.head;

        System.out.print("LinkedList: ");

        while (currNode != null) {
            System.out.print(currNode.data + " ");

            currNode = currNode.next;
        }
    }
}
