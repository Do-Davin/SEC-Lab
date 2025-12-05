package main.java.lab.task1;

public class FileStatistics {
    private final String fileName;
    private final String threadName;
    private final long wordCount;
    private final long lineCount;
    private final long charCount;
    private final long processingTimeMs;

    public FileStatistics(String fileName, String threadName, long wordCount, long lineCount, long charCount, long processingTimeMs) {
        this.fileName = fileName;
        this.threadName = threadName;
        this.wordCount = wordCount;
        this.lineCount = lineCount;
        this.charCount = charCount;
        this.processingTimeMs = processingTimeMs;
    }

    public String getFileName() {
        return fileName;
    }

    public String getThreadName() {
        return threadName;
    }

    public long getWordCount() {
        return wordCount;
    }

    public long getLineCount() {
        return lineCount;
    }

    public long getCharCount() {
        return charCount;
    }

    public long getProcessingTimeMs() {
        return processingTimeMs;
    }
}
