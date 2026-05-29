package HelperClasses;

import Model.Doctor;

public class LLNode {
    public Object objet;
    public LLNode next;

    public LLNode(Object objet) {
        this.objet = objet;
        next = null;
    }
}
