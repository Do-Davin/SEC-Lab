package lesson04;

import java.io.IOException;

public class ProcessManager1 {
    public static void main(String[] args) {
        try {
            ProcessBuilder pb = new ProcessBuilder("open", "-e", "test-file.txt");
            Process process = pb.start();
            // process.getOutputStream().write("Hello, ");

            // Wait for the process to complete
            int exitCode = process.waitFor();
            System.out.println("DEBUG: Process exited with code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
