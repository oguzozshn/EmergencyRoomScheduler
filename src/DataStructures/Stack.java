package DataStructures;

/**
 * Stack implementation using an array.
 */
public class Stack {
    private Object[] stack;
    private int top;
    private int capacity;

    /**
     * Constructs a new stack with the specified capacity.
     * @param capacity the maximum number of elements the stack can hold
     */
    public Stack(int capacity) {
        this.capacity = capacity;
        this.stack = new Object[capacity];
        this.top = -1;
    }

    /**
     * Pushes an object onto the stack.
     * @param object the object to be pushed
     */
    public void push(Object object) {
        if (isFull()) {
            return;
        }
        top++;
        stack[top] = object;
    }

    /**
     * Pops an object from the stack.
     * @return the object popped from the stack
     */
    public Object pop() {
        if (isEmpty()) {
            return null;
        }

        Object tempPatient = stack[top];
        stack[top] = null;
        top--;

        return tempPatient;
    }

    /**
     * Peeks at the top object of the stack without removing it.
     * @return the object at the top of the stack
     */
    public Object peek() {
        if (isEmpty()) {
            return null;
        }
        return stack[top];
    }

    /**
     * Checks if the stack is empty.
     * @return true if the stack is empty, false otherwise
     */
    public boolean isEmpty() {
        return top == -1;
    }

    /**
     * Checks if the stack is full.
     * @return true if the stack is full, false otherwise
     */
    public boolean isFull() {
        return top == capacity - 1;
    }
}