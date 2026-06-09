package DataStructures;

import Model.Patient;

/**
 * Queue class implements a queue data structure for storing and managing patient records.
 */
public class Queue {
    private Patient[] queue;
    private int front;
    private int rear;
    private int size;
    private int capacity;

    /**
     * Constructor for Queue.
     * @param capacity
     */
    public Queue(int capacity) {
        this.capacity = capacity;
        this.queue = new Patient[capacity];
        this.front = 0;
        this.rear = -1;
        this.size = 0;
    }

    /**
     * Enqueues a patient into the queue.
     * @param patient
     */
    public void enqueue(Patient patient) {
        if (isFull()) {
            return;
        }

        rear = (rear + 1) % capacity;
        queue[rear] = patient;
        size++;
    }

    /**
     * Dequeues a patient from the queue.
     * @return Patient
     */
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

    /**
     * Peeks at the front patient in the queue.
     * @return Patient
     */
    public Patient peek() {
        if (isEmpty()) {
            throw new RuntimeException("Kuyruk boş!");
        }
        return queue[front];
    }

    /**
     * Checks if the queue is empty.
     * @return true if the queue is empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Checks if the queue is full.
     * @return true if the queue is full, false otherwise
     */
    public boolean isFull() {
        return size == capacity;
    }
}