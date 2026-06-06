package DataStructures;

import Model.Patient;

public class Stack {
    private Object[] stack;
    private int top;
    private int capacity;

    public Stack(int capacity) {
        this.capacity = capacity;
        this.stack = new Object[capacity];
        this.top = -1;
    }

    public void push(Object object) {
        if (isFull()) {
            return;
        }
        top++;
        stack[top] = object;
    }

    public Object pop() {
        if (isEmpty()) {
            return null;
        }

        Object tempPatient = stack[top];
        stack[top] = null;
        top--;

        return tempPatient;
    }

    public Object peek() {
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