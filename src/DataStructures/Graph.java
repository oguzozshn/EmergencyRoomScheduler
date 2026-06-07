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
    // 🌟 STRING ID İLE BFS - ODALAR ARASI NAVIGASYON
    // =========================================================
    public void BFSPath(String startRoomId, String endRoomId) {
        if (map.get(startRoomId) == null || map.get(endRoomId) == null) {
            System.out.println("[HATA]: Başlangıç veya hedef oda bulunamadı!");
            return;
        }

        LinkedList visited = new LinkedList();
        MiniQueueString queue = new MiniQueueString();
        HashMap parentMap = new HashMap(); // her node'un parent'ını tutar

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
                        parentMap.put(neighborId, current); // parent'ı kaydet
                    }
                    neighborNode = neighborNode.next;
                }
            }
        }

        if (found) {
            // Yolu geriye doğru takip et
            LinkedList path = new LinkedList();
            String step = endRoomId;
            while (!step.equals("NULL")) {
                path.addFirst(step);
                step = (String) parentMap.get(step);
            }

            // Yazdır
            System.out.println("\n=== BFS EN KISA YOLU ===");
            System.out.print("Rota: ");
            LLNode node = path.getHead();
            while (node != null) {
                System.out.print("[" + node.data + "]");
                if (node.next != null) System.out.print(" -> ");
                node = node.next;
            }
            System.out.println();
        } else {
            System.out.println("[UYARI]: Hedef odaya ulaşılamadı!");
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

    // =========================================================
    // 🚨 STRING İÇİN MINI KUYRUK SINIFI
    // =========================================================
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
