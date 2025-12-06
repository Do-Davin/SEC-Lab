package lesson04;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

// Why the main thread end first (Solving Problem)

public class RuntimeManager2 {
    public static void main(String[] args) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        Process p = runtime.exec(new String[]{"/bin/zsh", "-c", "curl https://itc.edu.kh/wp-content/uploads/2021/02/cropped-Logo-ITC.png"});
        Path path = Files.createFile(Path.of("logo.png"));
        try (BufferedInputStream reader = new BufferedInputStream(p.getInputStream())) {
            byte[] bytes = reader.readAllBytes();
            Files.write(path, bytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            System.out.println("Total bytes: " + bytes.length);
        } catch (Exception e) {
            
        }
    }
}
