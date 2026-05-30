import DataStructures.Graph;

public class Main {
    public static void main(String[] args) {
        // Kendi yazdığın özel harita tabanlı graf nesnesi
        Graph hospitalNav = new Graph();

        System.out.println("=== TEST 1: ACİL SERVİS ODALARI VE YOLLARI EKLEME ===");
        // Çift yönlü yolları kuruyoruz
        hospitalNav.addEdge(1, 2, true); // Giriş <-> Triaj
        hospitalNav.addEdge(1, 5, true); // Giriş <-> Eczane
        hospitalNav.addEdge(2, 3, true); // Triaj <-> Röntgen
        hospitalNav.addEdge(2, 4, true); // Triaj <-> Ameliyathane
        hospitalNav.addEdge(3, 4, true); // Röntgen <-> Ameliyathane

        System.out.println("Odalar ve bağlantılar başarıyla eklendi.\n");

        System.out.println("=== TEST 2: BFS (ENLEMESİNE) NAVİGASYON ===");
        // 1 numaralı odadan (Giriş) aramayı başlatıyoruz
        // Önce yakın komşuları (2 ve 5), sonra onların komşularını gezecek
        hospitalNav.BFS(1);
        System.out.println();

        System.out.println("=== TEST 3: DFS (DERİNLEMESİNE) NAVİGASYON ===");
        // 1 numaralı odadan (Giriş) aramayı başlatıyoruz
        // Bir yoldan girdimi geri dönemeyene kadar en derine kadar ilerleyecek
        hospitalNav.DFS(1);
    }
}