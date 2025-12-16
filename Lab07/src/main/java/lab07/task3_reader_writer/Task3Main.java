package main.java.lab07.task3_reader_writer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Task3Main {
    public static void main(String[] args) throws InterruptedException {
        SharedDataStore store = new SharedDataStore();
        AtomicBoolean running = new AtomicBoolean(true);
        Random rnd = new Random();

        List<Thread> threads = new ArrayList<>();

        // 5 readers
        for (int i = 0; i < 5; i++) {
            int id = i + 1;
            Thread r = new Thread(() -> {
                String name = "Reader-" + id;
                while (running.get()) {
                    System.out.println("\u001B[32m" + store.read(name) + "\u001B[0m");
                    sleep(30 + rnd.nextInt(70));
                }
            }, "Reader-" + id);
            threads.add(r);
        }

        // 2 writers
        for (int i = 0; i < 2; i++) {
            int id = i + 1;
            Thread w = new Thread(() -> {
                String name = "Writer-" + id;
                int count = 1;
                while (running.get()) {
                    String val = "Data(" + name + ":" + (count++) + ")";
                    System.out.println("\u001B[31m" + store.write(name, val) + "\u001B[0m");
                    sleep(120 + rnd.nextInt(200));
                }
            }, "Writer-" + id);
            threads.add(w);
        }

        threads.forEach(Thread::start);

        // Run demo for ~7 seconds
        Thread.sleep(7000);
        running.set(false);

        for (Thread t : threads) t.join();
        System.out.println("\nTask 3 done.");
    }

    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) { Thread.currentThread().interrupt(); }
    }
}
