import DataStructures.HashMap;
import Model.Doctor;

public class Main {
    public static void main(String[] args) {
        HashMap doctorMap = new HashMap();

        // 1. Doktor Nesnelerini Oluşturma (Bilinçli olarak çakışma yaratıyoruz)
        Doctor d1 = new Doctor(1, "Dr. Oguz", "Available");   // 1 % 5 = Index 1
        Doctor d2 = new Doctor(6, "Dr. Dilara", "Available"); // 6 % 5 = Index 1 (ÇAKIŞMA!)
        Doctor d3 = new Doctor(3, "Dr. Dilem", "Busy");       // 3 % 5 = Index 3

        System.out.println("=== TEST 1: ELEMAN EKLEME VE ÇAKIŞMA KONTROLÜ ===");
        doctorMap.put(d1, d1.name);
        doctorMap.put(d2, d2.name);
        doctorMap.put(d3, d3.name);
        doctorMap.display();
        // Beklenen: Index 1'de hem Oguz hem Dilara birbirine bağlı olmalı.

        System.out.println("\n=== TEST 2: GET (ARAMA) KONTROLÜ ===");
        String foundDoctorName = (String) doctorMap.get(d2); // Dr. Dilara'yı aratıyoruz
        System.out.println("ID'si 6 olan doktor arandı. Bulunan: " + foundDoctorName);

        System.out.println("\n=== TEST 3: PUT (GÜNCELLEME) KONTROLÜ ===");
        System.out.println("Dr. Oguz'un ismini 'Dr. Oguz Han' olarak güncelliyoruz...");
        doctorMap.put(d1, "Dr. Oguz Han"); // Aynı ID ile tekrar put ettik
        doctorMap.display();

        System.out.println("\n=== TEST 4: REMOVE (SİLME) KONTROLÜ ===");
        System.out.println("Dr. Dilara sistemden siliniyor...");
        boolean isDeleted = doctorMap.remove(d2);
        System.out.println("Silme işlemi başarılı mı?: " + isDeleted);
        doctorMap.display();
        // Beklenen: Index 1'de sadece Oguz Han kalmalı, Dilara uçmalı.
    }
}