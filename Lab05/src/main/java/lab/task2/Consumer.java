package main.java.lab.task2;

import java.util.concurrent.atomic.AtomicBoolean;

public class Consumer extends Thread {

    private final PriorityBuffer buffer;
    private final AtomicBoolean running;
    private int consumedCount = 0;

    public Consumer(String name, PriorityBuffer buffer, AtomicBoolean running) {
        super(name);
        this.buffer = buffer;
        this.running = running;
    }

    @Override
    public void run() {
        try {
            while (running.get() || !buffer.isEmpty()) {
                PriorityItem item = buffer.take(getName());
                if (item != null) {
                    consumedCount++;
                    Thread.sleep(150);
                }
            }
        } catch (InterruptedException e) {}
    }

    public int getConsumedCount() {
        return consumedCount;
    }
}

