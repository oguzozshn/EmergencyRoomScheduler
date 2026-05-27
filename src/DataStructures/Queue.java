package DataStructures;

import Model.Patient;

public class Queue {
    private Patient[] queue;
    private int front;
    private int rear;
    private int size;
    private int capacity;

    public Queue(int capacity) {
        this.capacity = capacity;
        this.queue = new Patient[capacity];
        this.front = 0;
        this.rear = -1;
        this.size = 0;
    }

    public void enqueue(Patient patient) {
        if (isFull()) {
            return;
        }

        rear = (rear + 1) % capacity;
        queue[rear] = patient;
        size++;
    }

    public Patient dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Kuyruk boş! Çıkarılacak hasta yok.");
        }

        Patient tempPatient = queue[front];
        queue[front] = null;

        front = (front + 1) % capacity;
        size--;

        return tempPatient;
    }

    public Patient peek() {
        if (isEmpty()) {
            throw new RuntimeException("Kuyruk boş!");
        }
        return queue[front];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == capacity;
    }
}