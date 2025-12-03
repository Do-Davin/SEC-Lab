package lab05;

import java.io.File;
import java.io.IOException;

public class SingleThreadReadFile extends Thread {
    private File file;
    public int countWords = 0;
    public int countChars = 0;
    public int countLines = 0;

    public SingleThreadReadFile(File file) {
        this.file = file;
    }

    @Override
    public void run() {
        try {
            var lines = java.nio.file.Files.readAllLines(file.toPath());
            countChars = 0;
            countWords = 0;

            for (String line : lines) {
                countWords += line.split("[\\s|,|\\.]+").length;
                countChars += line.length();
            }
            countLines = lines.size();
            System.out.println("Words: " + countWords);
            System.out.println("Lines: " + countLines);
            System.out.println("Chars: " + countChars);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    
}
