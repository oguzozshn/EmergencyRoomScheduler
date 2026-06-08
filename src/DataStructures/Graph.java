package DataStructures;
import HelperClasses.LLNode;

/**
 * Graph implementation using adjacency list representation.
 */
public class Graph {
    private HashMap map = new HashMap();

    /**
     * Constructor for Graph.
     */
    public Graph(){}

    /**
     * Adds a vertex to the graph.
     * @param roomId
     */
    public void addVertex(String roomId) {
        if (map.get(roomId) == null) {
            map.put(roomId, new LinkedList());
        }
    }

    /**
     * Adds an edge between two vertices in the graph.
     * @param source
     * @param destination
     * @param bidirectional
     */
    public void addEdge(String source, String destination, boolean bidirectional) {
        if (map.get(source) == null) addVertex(source);
        if (map.get(destination) == null) addVertex(destination);

        LinkedList sourceNeighbors = (LinkedList) map.get(source);
        sourceNeighbors.add(destination);

        if (bidirectional) {
            LinkedList targetNeighbors = (LinkedList) map.get(destination);
            targetNeighbors.add(source);
        }
    }

    /**
     * Depth-first search traversal starting from a given room.
     * @param startRoomId
     */
    public void DFS(int startRoomId) {
        if (map.get(startRoomId) == null) return;

        LinkedList visited = new LinkedList();

        dfsHelper(startRoomId, visited);
    }

    /**
     * Depth-first search traversal helper method.
     * @param current
     * @param visited
     */
    private void dfsHelper(int current, LinkedList visited) {
        visited.add(current);
        System.out.print(current + " -> ");

        LinkedList neighbors = (LinkedList) map.get(current);
        if (neighbors != null) {
            LLNode neighborNode = neighbors.getHead();
            while (neighborNode != null) {
                int neighborId = (int) neighborNode.data;

                if (!visited.contains(neighborId)) {
                    dfsHelper(neighborId, visited);
                }
                neighborNode = neighborNode.next;
            }
        }
    }

    /**
     * Performs a Breadth-First Search (BFS) traversal starting from the given room
     * and finds the shortest path to the target room if it exists.
     *
     * The method utilizes a queue and a visited list to explore the graph level by level,
     * and a parent map to reconstruct the shortest path if the destination is reachable.
     * If the destination room is found, the traversal path is printed.
     * Otherwise, a warning message is printed indicating the target room is inaccessible.
     *
     * @param startRoomId the ID of the starting room from which the BFS traversal begins
     * @param endRoomId the ID of the target room that the BFS traversal attempts to reach
     */
    public void BFS(String startRoomId, String endRoomId) {

        LinkedList visited = new LinkedList();
        MiniQueueString queue = new MiniQueueString();
        HashMap parentMap = new HashMap();

        queue.enqueue(startRoomId);
        visited.add(startRoomId);
        parentMap.put(startRoomId, "NULL");

        boolean found = false;

        while (!queue.isEmpty()) {
            String current = queue.dequeue();

            if (current.equals(endRoomId)) {
                found = true;
                break;
            }

            LinkedList neighbors = (LinkedList) map.get(current);
            if (neighbors != null) {
                LLNode neighborNode = neighbors.getHead();
                while (neighborNode != null) {
                    String neighborId = (String) neighborNode.data;
                    if (!visited.contains(neighborId)) {
                        visited.add(neighborId);
                        queue.enqueue(neighborId);
                        parentMap.put(neighborId, current);
                    }
                    neighborNode = neighborNode.next;
                }
            }
        }

        if (found) {
            LinkedList path = new LinkedList();
            String step = endRoomId;
            while (!step.equals("NULL")) {
                path.addFirst(step);
                step = (String) parentMap.get(step);
            }

            // Yazdır
            System.out.println("\n=== BFS SHORTEST PATH===");
            System.out.print("Routh: ");
            LLNode node = path.getHead();
            while (node != null) {
                System.out.print("[" + node.data + "]");
                if (node.next != null) System.out.print(" -> ");
                node = node.next;
            }
            System.out.println();
        }
    }

    /**
     * Represents a simple queue structure for storing and managing String data
     * using a linked list implementation.
     *
     * This class provides basic queue operations such as adding an element
     * to the end of the queue (enqueue), removing an element from the front
     * of the queue (dequeue), and checking if the queue is empty.
     */
    private class MiniQueueString {
        private LLNode front, rear;

        public void enqueue(String data) {
            LLNode newNode = new LLNode(data);
            if (rear == null) {
                front = rear = newNode;
                return;
            }
            rear.next = newNode;
            rear = newNode;
        }

        public String dequeue() {
            if (front == null) return null;
            String data = (String) front.data;
            front = front.next;
            if (front == null) rear = null;
            return data;
        }

        public boolean isEmpty() {
            return front == null;
        }
    }
}
