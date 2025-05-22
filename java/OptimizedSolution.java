import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.concurrent.*;

public class OptimizedSolution{

    private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();
    private static final String INPUT_FILE = "../one_million_data.csv";
    private static final Map<Long, Map<String, Double>> globalCityPricesByThreadId = new ConcurrentHashMap<>();
    private static final Map<Long, Map<String, Double>> globalProductPricesByThreadId = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        try {
            partitioning(executor);
        } catch (IOException e) {
            e.printStackTrace();
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Map<String, Double> mergedCityPrices = mergeMaps(globalCityPricesByThreadId);
        Map<String, Double> mergedProductPrices = mergeMaps(globalProductPricesByThreadId);

        String cheapestCity = findCheapestCity(mergedCityPrices);
        List<Map.Entry<String, Double>> cheapestProducts = findCheapestProducts(mergedProductPrices);

        System.out.printf("Ville la moins chère: %s %.2f%n", cheapestCity, mergedCityPrices.get(cheapestCity));
        System.out.println("5 produits les moins chers:");
        for (Map.Entry<String, Double> entry : cheapestProducts) {
            System.out.printf("%s %.2f%n", entry.getKey(), entry.getValue());
        }

        long endTime = System.nanoTime();
        double duration = (endTime - startTime) / 1_000_000_000.0;
        System.out.printf("Temps écoulé: %.2f secondes%n", duration);
    }

    private static void partitioning(ExecutorService executor) throws IOException {
        long fileSize;
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(INPUT_FILE, "r")) {
            fileSize = randomAccessFile.length();
        }
        long segmentSize = fileSize / THREAD_COUNT;

        for (int i = 0; i < THREAD_COUNT; i++) {
            long startOffset = i * segmentSize;
            long endOffset = (i + 1) * segmentSize;
            if (i == THREAD_COUNT - 1) {
                endOffset = fileSize; // Ensure the last thread covers the rest of the file
            }
            executor.execute(new FileProcessorThread(startOffset, endOffset, i == 0));
        }
    }

    private static class FileProcessorThread implements Runnable {

        private final long startOffset;
        private final long endOffset;
        private final boolean isFirstSegment;

        public FileProcessorThread(long startOffset, long endOffset, boolean isFirstSegment) {
            this.startOffset = startOffset;
            this.endOffset = endOffset;
            this.isFirstSegment = isFirstSegment;
        }

        @Override
        public void run() {
            process();
        }

        private void process() {
            try (RandomAccessFile fis = new RandomAccessFile(INPUT_FILE, "r");
                 FileChannel fileChannel = fis.getChannel()) {

                fileChannel.position(startOffset);
                if (!isFirstSegment) {
                    skipToNextLine(fileChannel);
                }

                processOneSegmentOfFile(fileChannel);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void processOneSegmentOfFile(FileChannel fileChannel) throws IOException {
            long threadId = Thread.currentThread().getId();
            Map<String, Double> cityPrices = getCityMap(threadId, globalCityPricesByThreadId);
            Map<String, Double> productPrices = getProductMap(threadId, globalProductPricesByThreadId);

            ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 1024); // 1MB buffer
            long currentPosition = fileChannel.position();
            StringBuilder lineBuilder = new StringBuilder();

            while (currentPosition < endOffset && fileChannel.read(buffer) != -1) {
                buffer.flip();
                while (buffer.hasRemaining() && currentPosition < endOffset) {
                    byte b = buffer.get();
                    currentPosition++;
                    if (b == '\n') {
                        processLine(lineBuilder.toString(), cityPrices, productPrices);
                        lineBuilder.setLength(0);
                    } else {
                        lineBuilder.append((char) b);
                    }
                }
                buffer.clear();
            }
        }

        private void skipToNextLine(FileChannel fileChannel) throws IOException {
            ByteBuffer buffer = ByteBuffer.allocateDirect(1);
            while (fileChannel.read(buffer) > 0) {
                buffer.flip();
                if (buffer.get() == '\n') {
                    break;
                }
                buffer.clear();
            }
        }
    }

    private static void processLine(String line, Map<String, Double> cityPrices, Map<String, Double> productPrices) {
        String[] parts = line.split(",");
        if (parts.length != 3) return;

        String city = parts[0];
        String product = parts[1];
        double price = Double.parseDouble(parts[2]);

        cityPrices.merge(city, price, Double::sum);
        productPrices.merge(product, price, Double::min);
    }

    private static Map<String, Double> getCityMap(long threadId, Map<Long, Map<String, Double>> globalMap) {
        return globalMap.computeIfAbsent(threadId, k -> new ConcurrentHashMap<>());
    }

    private static Map<String, Double> getProductMap(long threadId, Map<Long, Map<String, Double>> globalMap) {
        return globalMap.computeIfAbsent(threadId, k -> new ConcurrentHashMap<>());
    }

    private static Map<String, Double> mergeMaps(Map<Long, Map<String, Double>> globalMap) {
        Map<String, Double> mergedMap = new HashMap<>();
        for (Map<String, Double> map : globalMap.values()) {
            map.forEach((key, value) -> mergedMap.merge(key, value, Double::sum));
        }
        return mergedMap;
    }

    private static String findCheapestCity(Map<String, Double> cityPrices) {
        return cityPrices.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    private static List<Map.Entry<String, Double>> findCheapestProducts(Map<String, Double> productPrices) {
        List<Map.Entry<String, Double>> productList = new ArrayList<>(productPrices.entrySet());
        productList.sort(Map.Entry.comparingByValue());
        return productList.subList(0, Math.min(5, productList.size()));
    }
}
