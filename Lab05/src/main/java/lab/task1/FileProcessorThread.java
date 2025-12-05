package main.java.lab.task1;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Semaphore;

public class FileProcessorThread extends Thread {
    
    private final Path filePath;
    private final List<FileStatistics> results;
    private final Semaphore semaphore;

    public FileProcessorThread(Path filePath, List<FileStatistics> results, Semaphore semaphore) {
        this.filePath = filePath;
        this.results = results;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            semaphore.acquire(); // limit parallelism
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
