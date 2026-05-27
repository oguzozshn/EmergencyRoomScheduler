package DataStructures;

import Model.Patient;

public class Stack {
    private Patient[] stack;
    private int top;
    private int capacity;

    public Stack(int capacity) {
        this.capacity = capacity;
        this.stack = new Patient[capacity];
        this.top = -1;
    }

    public void push(Patient patient) {
        if (isFull()) {
            return;
        }
        top++;
        stack[top] = patient;
    }

    public Patient pop() {
        if (isEmpty()) {
            return null;
        }

        Patient tempPatient = stack[top];
        stack[top] = null;
        top--;

        return tempPatient;
    }

    public Patient peek() {
        if (isEmpty()) {
            return null;
        }
        return stack[top];
    }

    public boolean isEmpty() {
        return top == -1;
    }

    public boolean isFull() {
        return top == capacity - 1;
    }
}