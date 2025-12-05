package main.java.lab.task2;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Producer extends Thread {

    private final PriorityBuffer buffer;
    private final AtomicBoolean running;
    private final Random random = new Random();
    private int producedCount = 0;

    public Producer(String name, PriorityBuffer buffer, AtomicBoolean running) {
        super(name);
        this.buffer = buffer;
        this.running = running;
    }

    @Override
    public void run() {
        try {
            while (running.get()) {
                int value = random.nextInt(100) + 1;      // 1–100
                int priority = random.nextInt(10) + 1;    // 1–10
                PriorityItem item = new PriorityItem(value, priority);
                buffer.put(item, getName());
                producedCount++;
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {}
    }

    public int getProducedCount() {
        return producedCount;
    }
}
