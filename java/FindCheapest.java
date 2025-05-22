import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class FindCheapest {

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        Map<String, Double> cityPrices = new HashMap<>();
        Map<String, Double> productPrices = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader("data.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 3) continue;

                String city = parts[0];
                String product = parts[1];
                double price = Double.parseDouble(parts[2]);

                cityPrices.put(city, cityPrices.getOrDefault(city, 0.0) + price);

                productPrices.put(product, Math.min(productPrices.getOrDefault(product, Double.MAX_VALUE), price));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String cheapestCity = null;
        double cheapestCityPrice = Double.MAX_VALUE;
        for (Map.Entry<String, Double> entry : cityPrices.entrySet()) {
            if (entry.getValue() < cheapestCityPrice) {
                cheapestCity = entry.getKey();
                cheapestCityPrice = entry.getValue();
            }
        }

        List<Map.Entry<String, Double>> productList = new ArrayList<>(productPrices.entrySet());
        productList.sort(Map.Entry.comparingByValue());
        List<Map.Entry<String, Double>> cheapestProducts = productList.subList(0, Math.min(5, productList.size()));

        System.out.printf("Ville la moins chère: %s %.2f%n", cheapestCity, cheapestCityPrice);
        System.out.println("5 produits les moins chers:");
        for (Map.Entry<String, Double> entry : cheapestProducts) {
            System.out.printf("%s %.2f%n", entry.getKey(), entry.getValue());
        }

        long endTime = System.nanoTime();
        double duration = (endTime - startTime) / 1_000_000_000.0;
        System.out.printf("Temps écoulé: %.2f secondes%n", duration);
    }
}
