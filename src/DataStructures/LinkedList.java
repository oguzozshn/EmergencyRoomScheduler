package DataStructures;

import HelperClasses.LLNode;

/**
 * Represents a singly linked list data structure.
 */
public class LinkedList {
    private LLNode head;

    /**
     * Constructs an empty linked list.
     */
    public LinkedList() {
        this.head = null;
    }

    /**
     * Returns the head node of the linked list.
     *
     * @return the head node
     */
    public LLNode getHead() {
        return this.head;
    }

    /**
     * Adds a new node with the given data to the end of the linked list.
     *
     * @param data the data to be added
     */
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

    /**
     * Checks if the linked list contains the given target.
     * @param target the target to search for
     * @return true if the target is found, false otherwise
     */
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

    /**
     * Returns the number of elements in the linked list.
     * @return the number of elements in the linked list
     */
    public int size() {
        int count = 0;
        LLNode current = this.head;
        while (current != null) {
            count++;
            current = current.next;
        }
        return count;
    }

    /**
     * Returns a string representation of the linked list.
     * @return a string representation of the linked list
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        LLNode current = this.head;
        while (current != null) {
            sb.append(current.data).append(" ");
            current = current.next;
        }
        return sb.toString().trim();
    }

    /**
     * Adds a new element to the beginning of the linked list.
     * @param data the data to add
     */
    public void addFirst(Object data) {
        LLNode newNode = new LLNode(data);
        newNode.next = this.head;
        this.head = newNode;
    }
}