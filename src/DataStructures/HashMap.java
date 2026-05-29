package DataStructures;

import Model.Doctor;
import HelperClasses.LLNode;

import java.io.Console;

public class HashMap {
    public LLNode[] table;
    private int capacity;

    public HashMap() {
        this.capacity = 5;
        this.table = new LLNode[capacity];
    }

    public void put(Object object, int id) {
        int index = HashFunction(id);
        LLNode newNode = new LLNode(object);

        if (table[index] == null) {
            table[index] = newNode;
        }
        else {
            newNode.next = table[index];
            table[index] = newNode;
        }
    }

    public Doctor getAvailableDoctor() {
        for (int i = 0; i < capacity; i++) {
            LLNode current = table[i];

            while (current != null) {
                Doctor doc = (Doctor) current.object;
                if (doc != null && doc.status.equals("available")) {
                }
                current = current.next;
            }
        }
        return null;
    }

    public int HashFunction(int id) {
        return Math.abs(id) % capacity;
    }
}