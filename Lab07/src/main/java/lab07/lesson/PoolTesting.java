package main.java.lab07.lesson;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PoolTesting {
    public static void main(String[] args) throws InterruptedException {
        // fibonacci Threadpool
        int n = 0;
        ExecutorService executor = Executors.newFixedThreadPool(16);
        FibonacciRunnable[] fnr = new FibonacciRunnable[n + 1];
        for (int i = 0; i <= n; i++) {
            fnr[i] = new FibonacciRunnable(i);
            executor.submit(fnr[i]);
        }
        long startTimePool = System.nanoTime();
        long sumPool = 0;

        executor.shutdown();
        executor.awaitTermination(60, TimeUnit.SECONDS);
        executor.shutdownNow();

        long endTimePool = System.nanoTime();
        for (FibonacciRunnable fibonacciRunnable : fnr) {
            sumPool += fibonacciRunnable.getResult();
        }
        System.out.printf("Sum(threadpool) = %,d\n", sumPool);
        System.out.printf("Time spent(threadpool): %,d ns\n", endTimePool - startTimePool);
    }
}
