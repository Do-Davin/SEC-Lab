package main.java.lab.task1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileProcessingUtils {
    
    public static FileStatistics processFile(Path filePath) {
        String fileName = filePath.getFileName().toString();
        String threadName = Thread.currentThread().getName();
        long startTime = System.currentTimeMillis();

        long lineCount = 0;
        long wordCount = 0;
        long charCount = 0;

        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                lineCount++;
                charCount += line.length();
                String trimmed = line.trim();
                if (!trimmed.isEmpty()) {
                    String[] words = trimmed.split("\\s+");
                    wordCount += words.length;
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("[" + threadName + "] File not found: " + filePath);
        } catch (IOException e) {
            System.err.println("[" + threadName + "] Error reading file: " + filePath + " -> " + e.getMessage());
        }

        long endTime = System.currentTimeMillis();
        return new FileStatistics(
            fileName,
            threadName,
            wordCount,
            lineCount,
            charCount,
            endTime - startTime
        );
    }
}
