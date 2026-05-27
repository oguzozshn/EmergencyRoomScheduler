package DataStructures;

import java.util.ArrayList;
import Model.Patient;

public class Stack {
    private ArrayList<Patient> stack;
    private int top;

    public Stack() {
        stack = new ArrayList<>();
        top = -1;
    };

    public void push(Patient patient){
        stack.add(top, patient);
        top++;
    };

    public void pop(){
        stack.remove(top);
        top--;
    };

    public Patient peek(){
        return stack.get(top);
    };

    public boolean isEmpty(){
        return top == -1;
    };
}