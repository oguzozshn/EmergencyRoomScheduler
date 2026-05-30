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

    public void put(int key, Object value){
        int hash = HashFunction(key);
        HashMapNode node = new HashMapNode(key, value, null);

        if (table[hash] == null) {
            table[hash] = node;
        }else {
            HashMapNode previous = null;
            HashMapNode current = table[hash];

            while (current != null){
                if ((int)current.key == key){
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
            previous.next = node;
        }
    }

    public Object get(int key){
        int hash = HashFunction(key);
        if (table[hash] == null) {
            return null;
        }else {
            HashMapNode temp = table[hash];
            while (temp != null){
                if ((int)temp.key == key){
                    return temp.value;
                }
                temp = temp.next;
            }
            return null;
        }
    }

    public boolean remove(int key){
        int hash = HashFunction(key);
        if (table[hash] == null) {
            return false;
        }else {
            HashMapNode previous = null;
            HashMapNode current = table[hash];

            while (current != null){
                if ((int)current.key == key){
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

    public void display(){
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                HashMapNode node = table[i];
                while (node != null) {
                    System.out.println(node.key + " : " + node.value);
                    node = node.next;
                }
            }
        }
    }

    public int HashFunction(int id) {
        return Math.abs(id) % capacity;
    }

    public HashMapNode[] getTable() {
        return this.table;
    }
}