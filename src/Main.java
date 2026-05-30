import DataStructures.Graph;

public class Main {
    public static void main(String[] args) {
        // Kendi yazdığın özel harita tabanlı graf nesnesi
        Graph hospitalNav = new Graph();

        System.out.println("=== TEST 1: ODA VE BAĞLANTI (EDGE) EKLEME ===");
        // Çift yönlü yolları tanımlıyoruz (Girişten triaja gidilebiliyorsa, triajdan da girişe dönülebilir)
        hospitalNav.addEdge(1, 2, true); // Giriş <-> Triaj
        hospitalNav.addEdge(1, 5, true); // Giriş <-> Eczane
        hospitalNav.addEdge(2, 3, true); // Triaj <-> Röntgen
        hospitalNav.addEdge(2, 4, true); // Triaj <-> Ameliyathane
        hospitalNav.addEdge(3, 4, true); // Röntgen <-> Ameliyathane

        // Grafın genel yapısını ekrana basıyoruz (Komşuluk Listesi Görünümü)
        System.out.println("\n--- ACİL SERVİS NAVİGASYON HARİTASI ---");
        System.out.println(hospitalNav.toString());
    }
}