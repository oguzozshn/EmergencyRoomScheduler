package DataStructures;

import HelperClasses.HashMapNode;
import Model.Doctor;
import HelperClasses.LLNode;

import java.io.Console;

public class HashMap {
    private HashMapNode[] table; //entry
    private int capacity = 5;


    public HashMap() {
        table = new HashMapNode[capacity];
    }

    public void put(Doctor doctor, Object value){
        int hash = HashFunction(doctor.id);
        HashMapNode node = new HashMapNode(doctor, value, null);

        if (table[hash] == null) {
            table[hash] = node;
        }else {
            HashMapNode previous = null;
            HashMapNode current = table[hash];

            while (current != null){
                if ((int)current.key == doctor.id){
                    if (previous == null){
                        node.next = current.next;
                        table[hash] = node;
                        return;
                    }else {
                        node.next = current.next;
                        previous.next = node;
                        return;
                    }
                }
                previous = current;
                current = current.next;
            }
        }
    }



    //Get bucket index
    public int HashFunction(int id) {
        return Math.abs(id) % capacity;
    }
}