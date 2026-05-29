package HelperClasses;

import Model.Doctor;

public class LLNode {
    public Object object;
    public LLNode next;

    public LLNode(Object objet) {
        this.object = objet;
        next = null;
    }
}
