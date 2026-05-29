package DataStructures;
import HelperClasses.LLNode;
import Model.Doctor;

public class LinkedList {
    private LLNode head;

    public LinkedList() {
        head = null;
    }
    public Doctor add(Object obj) {
        LLNode newNode = new LLNode(obj);

        newNode.next = head;
        head = newNode;
        return null;
    }
}
