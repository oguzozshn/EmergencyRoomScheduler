import DataStructures.*;
import Model.*;
import java.io.*;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {

        //ER Room Graph
        Graph erRoomGraph = new Graph();
        erRoomGraph.addVertex(1);

        HashMap doctorMap = new HashMap();
        Stack stack = new Stack(10);


        Doctor d1 = new Doctor(101, "Dr. Alice Carter",  "AVAILABLE");
        Doctor d2 = new Doctor(202, "Dr. Ben Nguyen",    "AVAILABLE");
        Doctor d3 = new Doctor(303, "Dr. Clara Hassan",  "AVAILABLE");
        Doctor d4 = new Doctor(404, "Dr. David Reyes",   "AVAILABLE");

        // Store using doctorId as the key
        doctorMap.put(d1.id, d1);
        doctorMap.put(d2.id, d2);
        doctorMap.put(d3.id, d3);
        doctorMap.put(d4.id, d4);

        BinarySearchTree patientTree = new BinarySearchTree();
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== ACİL SERVİS SİSTEMİ VERİ YÜKLEME ===");
        System.out.print("Lütfen hasta listesi dosyasının (patient.txt) tam yolunu giriniz: ");

        String filePath = scanner.nextLine();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {

                // Satırı virgüllere göre parçala
                String[] tokens = line.split(",");

                if (tokens.length >= 5) {
                    String patientId = tokens[0].trim();
                    String name = tokens[1].trim();
                    int age = Integer.parseInt(tokens[2].trim());
                    int severity = Integer.parseInt(tokens[3].trim());
                    int arrivalTime = Integer.parseInt(tokens[4].trim());

                    // Yeni hasta modelini oluştur
                    Patient newPatient = new Patient(patientId, name, age, severity, arrivalTime);

                    // Kendi yazdığın BST yapısına ekle
                    patientTree.insert(newPatient);
                    System.out.println("[KABUL BAŞARILI] " + patientId + " kodlu " + name + " ağaca eklendi.");
                }
            }

            System.out.println("\n[SİSTEM]: Dosyadaki tüm hastalar başarıyla okundu ve BST'ye kaydedildi.");

            // Kontrol Etmek İçin: BST'deki hastaları sıralı (InOrder) yazdıralım
            System.out.println("\n=== BST GÜNCEL DURUM (InOrder Sıralı) ===");
            patientTree.inOrder();

        } catch (IOException e) {
            System.out.println("\n[HATA]: Dosya okunamadı! Girdiğiniz dizin yolunu kontrol edin.");
            System.out.println("Girilen Yol: " + filePath);
        } catch (NumberFormatException e) {
            System.out.println("\n[HATA]: Dosyadaki sayısal alanlarda (yaş, aciliyet, geliş zamanı) hatalı format var!");
        }
    }
}