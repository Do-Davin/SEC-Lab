package main.java.lab.task1;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Semaphore;

public class FileProcessorRunnable implements Runnable {
    
    private final Path filePath;
    private final List<FileStatistics> results;
    private final Semaphore semaphore;

    public FileProcessorRunnable (Path filePath, List<FileStatistics> results, Semaphore semaphore) {
        this.filePath = filePath;
        this.results = results;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            semaphore.acquire();
            FileStatistics stats = FileProcessingUtils.processFile(filePath);
            synchronized (results) {
                results.add(stats);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaphore.release();
        }
    }
}
