package DataStructures;

import HelperClasses.HashMapNode;
import java.util.Objects;

/**
 * Implementation of a HashMap data structure using separate chaining for collision resolution.
 */
public class HashMap {
    private HashMapNode[] table;
    private int capacity = 11;

    /**
     * Constructor for HashMap, initializes the table with default capacity.
     */
    public HashMap() {
        table = new HashMapNode[capacity];
    }

    /**
     * Inserts a key-value pair into the HashMap.
     * If the key already exists, updates the associated value.
     * @param key The key to be inserted or updated.
     * @param value The value associated with the key.
     */
    public void put(String key, Object value){
        String hash = HashFunction(key);
        int hashInt = Integer.parseInt(hash);

        HashMapNode node = new HashMapNode(key, value, null);

        if (table[hashInt] == null) {
            table[hashInt] = node;
        }else {
            HashMapNode previous = null;
            HashMapNode current = table[hashInt];

            while (current != null){
                if (Objects.equals(current.key, key)){
                    if (previous == null){
                        node.next = current.next;
                        table[hashInt] = node;
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

    /**
     * Inserts an integer key-value pair into the HashMap.
     * If the key already exists, updates the associated value.
     * @param key The integer key to be inserted or updated.
     * @param value The value associated with the key.
     */
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

    /**
     * Retrieves the value associated with the given key.
     * @param key The key for which to retrieve the value.
     * @return The value associated with the key, or null if the key is not found.
     */
    public Object get(String key){
        String hash = HashFunction(key);
        int hashInt = Integer.parseInt(hash);

        if (table[hashInt] == null) {
            return null;
        }else {
            HashMapNode temp = table[hashInt];
            while (temp != null){
                if (Objects.equals((String) temp.key, key)){
                    return temp.value;
                }
                temp = temp.next;
            }
            return null;
        }
    }

    /**
     * Retrieves the value associated with the given integer key.
     * @param key The integer key for which to retrieve the value.
     * @return The value associated with the key, or null if the key is not found.
     */
    public Object get(int key){
        int hash = HashFunction(key);

        if (table[hash] == null) {
            return null;
        }else {
            HashMapNode temp = table[hash];
            while (temp != null){
                if ((int) temp.key == key){
                    return temp.value;
                }
                temp = temp.next;
            }
            return null;
        }
    }

    /**
     * Removes the key-value pair associated with the given string key.
     * @param key The string key to remove.
     * @return true if the key was found and removed, false otherwise.
     */
    public boolean remove(String key){
        String hash = HashFunction(key);
        int hashInt = Integer.parseInt(hash);
        if (table[hashInt] == null) {
            return false;
        }else {
            HashMapNode previous = null;
            HashMapNode current = table[hashInt];

            while (current != null){
                if (Objects.equals((String) current.key, key)){
                    if (previous == null){
                        table[hashInt] = current.next;
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

    /**
     * Removes the key-value pair associated with the given integer key.
     * @param key The integer key to remove.
     * @return true if the key was found and removed, false otherwise.
     */
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

    /**
     * Displays the contents of the hash map.
     */
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

    /**
     * Hash function for string keys.
     */
    public String HashFunction(String id) {
        int hash = 0;

        for (int i = 0; i < id.length(); i++) {
            char c = id.charAt(i);
            int charValue = c - 'A';

            if (c >= '0' && c <= '9') {
                charValue = c - '0';
            }

            hash = hash + (charValue * (i + 1));
        }
        if (hash < 0) {
            hash = hash * -1;
        }
        return Integer.toString(hash % capacity);
    }

    /**
     * Hash function for integer keys.
     */
    public int HashFunction(int id) {
        return Math.abs(id) % capacity;
    }

    /**
     * Getter for the hash map table.
     */
    public HashMapNode[] getTable() {
        return this.table;
    }
}