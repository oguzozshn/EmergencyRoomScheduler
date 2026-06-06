package DataStructures;
import HelperClasses.HashMapNode;
import HelperClasses.LLNode;

public class Graph {
    private HashMap map = new HashMap();
    private LinkedList list = new LinkedList();

    public Graph(){}

    public void addVertex(String roomId) {
        if (map.get(roomId) == null) {
            map.put(roomId, new LinkedList());
        }
    }
    public void addEdge(int source, int destination, boolean bidirectional) {
        if (map.get(source) == null) addVertex(source);
        if (map.get(destination) == null) addVertex(destination);

        LinkedList sourceNeighbors = (LinkedList) map.get(source);
        sourceNeighbors.add(destination);

        if (bidirectional) {
            LinkedList targetNeighbors = (LinkedList) map.get(destination);
            targetNeighbors.add(source);
        }
    }


    public void BFS(int startRoomId) {
        if (map.get(startRoomId) == null) return;

        LinkedList visited = new LinkedList(); // Ziyaret edilenleri tutan listemiz
        MiniQueue queue = new MiniQueue();     // Sadece bu metot için yazdığımız kuyruk

        // Başlangıç düğümünü kuyruğa al ve ziyaret edildi olarak işaretle
        queue.enqueue(startRoomId);
        visited.add(startRoomId);

        System.out.print("BFS (Genişlik Öncelikli) Navigasyon Sırası: ");

        while (!queue.isEmpty()) {
            int current = queue.dequeue();
            System.out.print(current + " -> ");

            // Mevcut odanın komşularını al
            LinkedList neighbors = (LinkedList) map.get(current);
            if (neighbors != null) {
                // HATA DÜZELTME: getHead() kullanarak zincire eriştik
                LLNode neighborNode = neighbors.getHead();
                while (neighborNode != null) {
                    int neighborId = (int) neighborNode.data;

                    // Eğer komşu daha önce ziyaret edilmediyse kuyruğa ekle
                    if (!visited.contains(neighborId)) {
                        visited.add(neighborId);
                        queue.enqueue(neighborId);
                    }
                    neighborNode = neighborNode.next;
                }
            }
        }
        System.out.println("BİTTİ");
    }

    // =========================================================
    // 🌟 DFS (DEPTH-FIRST SEARCH) - DERİNLEMESİNE GEZİNTİ
    // =========================================================
    public void DFS(int startRoomId) {
        if (map.get(startRoomId) == null) return;

        LinkedList visited = new LinkedList(); // Ziyaret edilenleri tutacak liste
        System.out.print("DFS (Derinlik Öncelikli) Navigasyon Sırası: ");

        // Özyinelemeli (Recursive) yardımcı fonksiyonu tetikliyoruz
        dfsHelper(startRoomId, visited);
        System.out.println("BİTTİ");
    }

    private void dfsHelper(int current, LinkedList visited) {
        // Mevcut odayı ziyaret et ve ekrana bas
        visited.add(current);
        System.out.print(current + " -> ");

        // Komşularını al ve derinlemesine ilerle
        LinkedList neighbors = (LinkedList) map.get(current);
        if (neighbors != null) {
            LLNode neighborNode = neighbors.getHead();
            while (neighborNode != null) {
                int neighborId = (int) neighborNode.data;

                // Ziyaret edilmemiş bir komşu bulduğun an doğrudan onun içine dal!
                if (!visited.contains(neighborId)) {
                    dfsHelper(neighborId, visited);
                }
                neighborNode = neighborNode.next;
            }
        }
    }

    // =========================================================
    // 🚨 BFS İÇİN GEREKLİ GİZLİ MİNİ KUYRUK (QUEUE) SINIFI
    // =========================================================
    private class MiniQueue {
        private LLNode front, rear;

        public void enqueue(int data) {
            LLNode newNode = new LLNode(data);
            if (rear == null) {
                front = rear = newNode;
                return;
            }
            rear.next = newNode;
            rear = newNode;
        }

        public int dequeue() {
            if (front == null) return -1;
            int data = (int) front.data;
            front = front.next;
            if (front == null) rear = null;
            return data;
        }

        public boolean isEmpty() {
            return front == null;
        }
    }
}
