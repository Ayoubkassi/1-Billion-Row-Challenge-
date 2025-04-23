import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class FindCheapest {

    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        Map<String, Double> cityPrices = new ConcurrentHashMap<>();
        Map<String, Double> productPrices = new ConcurrentHashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader("data.csv"))) {
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }

            ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
            List<Future<?>> futures = new ArrayList<>();

            int chunkSize = (int) Math.ceil((double) lines.size() / NUM_THREADS);
            for (int i = 0; i < lines.size(); i += chunkSize) {
                int end = Math.min(i + chunkSize, lines.size());
                List<String> chunk = lines.subList(i, end);
                futures.add(executor.submit(() -> processChunk(chunk, cityPrices, productPrices)));
            }

            for (Future<?> future : futures) {
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            executor.shutdown();
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

    private static void processChunk(List<String> lines, Map<String, Double> cityPrices, Map<String, Double> productPrices) {
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length != 3) continue;

            String city = parts[0];
            String product = parts[1];
            double price = Double.parseDouble(parts[2]);

            cityPrices.merge(city, price, Double::sum);

            productPrices.merge(product, price, Double::min);
        }
    }
}
