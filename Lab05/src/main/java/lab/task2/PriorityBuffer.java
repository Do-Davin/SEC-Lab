package main.java.lab.task2;

import java.util.PriorityQueue;

public class PriorityBuffer {

    private final PriorityQueue<PriorityItem> queue = new PriorityQueue<>();
    private final int capacity;

    public PriorityBuffer(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void put(PriorityItem item, String producerName) throws InterruptedException {
        while (queue.size() == capacity) {
            wait();
        }
        queue.offer(item);
        System.out.println(producerName + " produced " + item + " | Buffer: " + queue);
        notifyAll();
    }

    public synchronized PriorityItem take(String consumerName) throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        PriorityItem item = queue.poll();
        System.out.println(consumerName + " consumed " + item + " | Buffer: " + queue);
        notifyAll();
        return item;
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}

