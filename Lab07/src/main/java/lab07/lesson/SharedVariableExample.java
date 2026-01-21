package main.java.lab07.lesson;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SharedVariableExample {

    static class SharedCounter {

        private int value = 0;
        private final Lock lock = new ReentrantLock();

        public void increment() {
            lock.lock();
            try {
                int temp = value;
                Thread.sleep(100);  // Simulate work
                value = temp + 1;
                System.out.println("Thread " + Thread.currentThread().getId() + ": " + value);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }

        public int getValue() {
            lock.lock();
            try {
                return value;
            } finally {
                lock.unlock();
            }
        }
    }
}
