package DataStructures;
import HelperClasses.LLNode;
import Model.Doctor;

public class LinkedList {
    private LLNode head;

    public LinkedList() {
        head = null;
    }
    public LinkedList add(LinkedList list, Object data) {
        LLNode newNode = new LLNode(data);

        if (list.head == null) {
            list.head = newNode;
        }
        else {
            LLNode last = list.head;
            while (last.next != null) {
                last = last.next;
            }
            last.next = newNode;
        }
        return list;
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
