package lab05;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) throws InterruptedException {
        
        String currentFolderPath = System.getProperty("user.dir");
        System.out.println("Your current Folder Path: " + currentFolderPath + "\n");
        File dir = new File(currentFolderPath + "/bunchOfFiles");
        List<SingleThreadReadFile> threads = new ArrayList<>();
        for (File file : dir.listFiles(f -> f.getName().endsWith(".txt"))) {
            SingleThreadReadFile t1 = new SingleThreadReadFile(file);
            t1.start();
            threads.add(t1);
        }
        var startTime = System.currentTimeMillis();
        System.out.println("Waiting for calculation...");
        for (SingleThreadReadFile singleThreadReadFile : threads) {
            singleThreadReadFile.join();
        }
        int sumWords = 0;
        int sumLines = 0;
        int sumChars = 0;
        for (SingleThreadReadFile t : threads) {
            sumChars += t.countChars;
            sumLines += t.countLines;
            sumWords += t.countWords;
        }
        var endTime = System.currentTimeMillis();
        var duration = endTime - startTime;
        System.out.println("Done");
        System.out.println("TOTAL WORDS: " + sumWords);
        System.out.println("TOTAL LINES: " + sumLines);
        System.out.println("TOTAL CHARS: " + sumChars);
        System.out.println("Time spent: " + (duration) + " ms");
    }
}