package minimumcost_prj.ProjectCode;

public class Queue<Vertex> {
    private final Vertex[] array;
    private int front;
    private int rear;
    private int size;
    private final int capacity;

    public Queue(int capacity) {
        this.capacity = capacity;
        this.array = (Vertex[]) new Object[capacity];
        this.front = 0;
        this.rear = -1;
        this.size = 0;
    }

    public void enqueue(Vertex value) {
        if (size == capacity) {
            System.out.println("❌ Queue is full!");
            return;
        }
        rear = (rear + 1) % capacity;
        array[rear] = value;
        size++;
    }

    public Vertex dequeue() {
        if (isEmpty()) return null;
        Vertex value = array[front];
        front = (front + 1) % capacity;
        size--;
        return value;
    }

    public Vertex peek() {
        if (isEmpty()) return null;
        return array[front];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }
}

