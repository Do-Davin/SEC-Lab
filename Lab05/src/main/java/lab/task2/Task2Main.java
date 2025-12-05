package main.java.lab.task2;

import java.util.concurrent.atomic.AtomicBoolean;

public class Task2Main {

    private static final int BUFFER_CAPACITY = 10;
    private static final int RUN_DURATION_MS = 30_000; // 30 seconds

    public static void main(String[] args) throws InterruptedException {
        PriorityBuffer buffer = new PriorityBuffer(BUFFER_CAPACITY);
        AtomicBoolean running = new AtomicBoolean(true);

        Producer p1 = new Producer("Producer-1", buffer, running);
        Producer p2 = new Producer("Producer-2", buffer, running);

        Consumer c1 = new Consumer("Consumer-1", buffer, running);
        Consumer c2 = new Consumer("Consumer-2", buffer, running);
        Consumer c3 = new Consumer("Consumer-3", buffer, running);

        p1.start();
        p2.start();
        c1.start();
        c2.start();
        c3.start();

        Thread.sleep(RUN_DURATION_MS);
        running.set(false);

        p1.join();
        p2.join();
        c1.join();
        c2.join();
        c3.join();

        System.out.println("\n========= STATISTICS =========");
        System.out.println(p1.getName() + " produced: " + p1.getProducedCount());
        System.out.println(p2.getName() + " produced: " + p2.getProducedCount());

        System.out.println(c1.getName() + " consumed: " + c1.getConsumedCount());
        System.out.println(c2.getName() + " consumed: " + c2.getConsumedCount());
        System.out.println(c3.getName() + " consumed: " + c3.getConsumedCount());

        int totalProduced = p1.getProducedCount() + p2.getProducedCount();
        int totalConsumed = c1.getConsumedCount() + c2.getConsumedCount() + c3.getConsumedCount();

        System.out.println("Total produced: " + totalProduced);
        System.out.println("Total consumed: " + totalConsumed);
        System.out.println("================================");
    }
}

