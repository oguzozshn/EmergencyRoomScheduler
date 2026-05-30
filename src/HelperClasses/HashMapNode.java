package HelperClasses;

import DataStructures.HashMap;

public class HashMapNode {
    public Object key;
    public Object value;
    public HashMapNode next;

    public HashMapNode(Object key, Object value, HashMapNode next){
        this.key = key;
        this.value = value;
        this.next = null;
    }

}
