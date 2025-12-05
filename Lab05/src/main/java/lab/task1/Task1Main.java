package main.java.lab.task1;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Task1Main {
    
    private static final String FOLDER_NAME = "bunchOfFiles";
    private static final int MAX_CONCURRENT_THREADS = 3;

    public static void main(String[] args) throws IOException, InterruptedException {
        Path folder = Paths.get(FOLDER_NAME);

        if (!Files.exists(folder) || !Files.isDirectory(folder)) {
            System.err.println("Folder '" + FOLDER_NAME + "' not found. Please create it in the project root.");
            return;
        }

        List<Path> files = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder)) {
            for (Path path : stream) {
                if (Files.isRegularFile(path)) {
                    files.add(path);
                }
            }
        }

        if (files.isEmpty()) {
            System.err.println("No files found in '" + FOLDER_NAME + "'.");
            return;
        }

        System.out.println("\n\u001B[32m" + "Scanning files in folder: " + FOLDER_NAME + "..." + "\u001B[0m\n");
        List<FileStatistics> results = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();
        Semaphore semaphore = new Semaphore(MAX_CONCURRENT_THREADS);

        long globalStart = System.currentTimeMillis();

        for (int i = 0; i < files.size(); i++) {
            Path file = files.get(i);
            Thread t;
            if (i % 2 == 0) {
                t = new FileProcessorThread(file, results, semaphore);
            } else {
                t = new Thread(new FileProcessorRunnable(file, results, semaphore));
            }
            t.setName("Thread-" + (i + 1));
            threads.add(t);
            t.start();
        }

        for (Thread t : threads) {
            t.join();
        }

        long globalEnd = System.currentTimeMillis();
        long totalTime = globalEnd - globalStart;

        // Aggregate
        long totalWords = 0;
        long totalLines = 0;
        long totalChars = 0;
        long totalProcessingTime = 0;

        System.out.println("--------------------------------- RESULTS -------------------------------------");
        for (FileStatistics stats : results) {
            totalWords += stats.getWordCount();
            totalLines += stats.getLineCount();
            totalChars += stats.getCharCount();
            totalProcessingTime += stats.getProcessingTimeMs();

            System.out.printf(
                    "%s processed %s: %d words, %d lines, %d characters in %dms%n",
                    stats.getThreadName(),
                    stats.getFileName(),
                    stats.getWordCount(),
                    stats.getLineCount(),
                    stats.getCharCount(),
                    stats.getProcessingTimeMs()
            );
        }

        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("Total files processed: " + results.size());
        System.out.println("Total word(s): " + totalWords);
        System.out.println("Total line(s): " + totalLines);
        System.out.println("Total character(s): " + totalChars);
        System.out.println("Total processing time: " + totalProcessingTime + "ms");
        System.out.println("Wall-clock time: " + totalTime + "ms");
    }
}
