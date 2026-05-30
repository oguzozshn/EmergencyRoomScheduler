package DataStructures;
import HelperClasses.GraphNode;

public class Graph {
    private HashMap map = new HashMap();
    private LinkedList list = new LinkedList();

    public Graph(){}

    public void addVertex(int roomId) {
        if (map.get(roomId) == null) {
            map.put(roomId, new LinkedList());
        }
    }
    public void addEdge(int source, int destination, boolean bidirectional) {
        // Odalar haritada yoksa otomatik oluştur
        if (map.get(source) == null) addVertex(source);
        if (map.get(destination) == null) addVertex(destination);

        // Kaynak odanın komşuluk listesini al ve hedef odayı ekle
        LinkedList sourceNeighbors = (LinkedList) map.get(source);
        // NOT: Kendi LinkedList sınıfının ekleme metodunu çağırıyoruz (Örn: add veya insert)
        sourceNeighbors.add(destination);

        // Eğer yol çift yönlüyse, hedef odanın komşularına da kaynak odayı ekle
        if (bidirectional) {
            LinkedList targetNeighbors = (LinkedList) map.get(destination);
            targetNeighbors.add(source);
        }
    }


    public void BFS(){}

    public void DFS(){}
}
