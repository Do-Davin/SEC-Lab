package main.java.lab07.task2_producer_consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Task2Main {
    public static void main(String[] args) throws InterruptedException {
        BoundedBuffer<Integer> buffer = new BoundedBuffer<>(5);
        AtomicBoolean running = new AtomicBoolean(true);

        int producers = 2;
        int consumers = 3;

        List<Thread> threads = new ArrayList<>();
        Random rnd = new Random();

        for (int i = 0; i < producers; i++) {
            int id = i + 1;
            Thread p = new Thread(() -> {
                int item = 0;
                try {
                    while (running.get()) {
                        int val = (id * 100000) + item++;
                        buffer.put(val);
                        System.out.printf("\u001B[32m[P%d] produced %d | size=%d%n\u001B[0m", id, val, buffer.size());
                        Thread.sleep(50 + rnd.nextInt(80));
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "Producer-" + id);
            threads.add(p);
        }

        for (int i = 0; i < consumers; i++) {
            int id = i + 1;
            Thread c = new Thread(() -> {
                try {
                    while (running.get()) {
                        int val = buffer.take();
                        System.out.printf("\u001B[31m[C%d] consumed %d | size=%d%n\u001B[0m", id, val, buffer.size());
                        Thread.sleep(60 + rnd.nextInt(120));
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "Consumer-" + id);
            threads.add(c);
        }

        threads.forEach(Thread::start);

        // Run demo for ~6 seconds
        Thread.sleep(6000);
        running.set(false);

        // Interrupt all to stop waiting threads
        for (Thread t : threads) t.interrupt();
        for (Thread t : threads) t.join();

        System.out.println("\nTask 2 done.");
    }
}
