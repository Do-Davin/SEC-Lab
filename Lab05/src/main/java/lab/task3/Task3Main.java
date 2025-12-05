package main.java.lab.task3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Task3Main {

    private static final int URL_COUNT = 20;
    private static final int MAX_DOWNLOAD_SECONDS = 5;
    private static final int PAGE_TIMEOUT_SECONDS = 10;

    public static void main(String[] args) throws InterruptedException {
        List<String> urls = new ArrayList<>();
        for (int i = 1; i <= URL_COUNT; i++) {
            urls.add("http://example.com/page" + i);
        }

        runWithExecutor("FixedThreadPool(5)",
                Executors.newFixedThreadPool(5), urls);

        runWithExecutor("CachedThreadPool",
                Executors.newCachedThreadPool(), urls);

        runWithExecutor("SingleThreadExecutor",
                Executors.newSingleThreadExecutor(), urls);
    }

    private static void runWithExecutor(String poolName,
                                        ExecutorService executor,
                                        List<String> urls) throws InterruptedException {

        System.out.println("\n===== Running with " + poolName + " =====");

        List<Future<String>> futures = new ArrayList<>();
        long startTime = System.currentTimeMillis();

        for (String url : urls) {
            futures.add(executor.submit(new DownloadTask(url, poolName)));
        }

        for (Future<String> future : futures) {
            try {
                String result = future.get(PAGE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                System.out.println(result);
            } catch (TimeoutException e) {
                System.out.println("Timeout: Page took more than " + PAGE_TIMEOUT_SECONDS + " seconds, cancelling...");
                future.cancel(true);
            } catch (ExecutionException e) {
                System.out.println("Error downloading page: " + e.getCause());
            }
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        long endTime = System.currentTimeMillis();
        System.out.println(poolName + " total execution time: " + (endTime - startTime) + "ms");
    }

    private static class DownloadTask implements Callable<String> {
        private final String url;
        private final String poolName;
        private final Random random = new Random();

        public DownloadTask(String url, String poolName) {
            this.url = url;
            this.poolName = poolName;
        }

        @Override
        public String call() throws Exception {
            String threadName = Thread.currentThread().getName();
            int sleepSeconds = random.nextInt(MAX_DOWNLOAD_SECONDS) + 1; // 1–5s
            long start = System.currentTimeMillis();
            try {
                Thread.sleep(sleepSeconds * 1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return poolName + " | " + threadName + " | " + url + " interrupted.";
            }
            long end = System.currentTimeMillis();
            long duration = end - start;
            return poolName + " | " + threadName + " downloaded " + url +
                    " in " + duration + "ms (simulated " + sleepSeconds + "s)";
        }
    }
}

