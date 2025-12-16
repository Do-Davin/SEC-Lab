package main.java.lab07.task3_reader_writer;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SharedDataStore {
    private final ReentrantReadWriteLock rw = new ReentrantReadWriteLock(true);

    private String data = "Initial";

    public String read(String readerName) {
        rw.readLock().lock();
        try {
            // simulate read time
            sleepQuiet(40);
            return String.format("[%s] READ  -> %s", readerName, data);
        } finally {
            rw.readLock().unlock();
        }
    }

    public String write(String writerName, String newValue) {
        rw.writeLock().lock();
        try {
            sleepQuiet(80);
            data = newValue;
            return String.format("[%s] WRITE -> %s", writerName, data);
        } finally {
            rw.writeLock().unlock();
        }
    }

    private static void sleepQuiet(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) { Thread.currentThread().interrupt(); }
    }
}
