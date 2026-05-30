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

    public Object get(Doctor doctor){
        int hash = HashFunction(doctor.id);
        if (table[hash] == null) {
            return null;
        }else {
            HashMapNode temp = table[hash];
            while (temp != null){
                if ((int)temp.key == doctor.id){
                    return temp.value;
                }
                temp = temp.next;
            }
            return null;
        }
    }

    public boolean remove(Doctor doctor){
        int hash = HashFunction(doctor.id);
        if (table[hash] == null) {
            return false;
        }else {
            HashMapNode previous = null;
            HashMapNode current = table[hash];

            while (current != null){
                if ((int)current.key == doctor.id){
                    if (previous == null){
                        table[hash] = current.next;
                        return true;
                    } else {
                        previous.next = current.next;
                        return true;
                    }
                }
                previous = current;
                current = current.next;
            }
            return false;
        }
    }

    public int HashFunction(int id) {
        return Math.abs(id) % capacity;
    }
}