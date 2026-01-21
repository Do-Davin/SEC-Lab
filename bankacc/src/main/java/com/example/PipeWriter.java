package com.example;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class PipeWriter {
    public static void main(String[] args) {
        String pipePath = "pipefile.txt";
        Scanner scanner = new Scanner(System.in);

        System.out.println("Writer started. Type messages:");

        try {
            while (true) {
                String message = scanner.nextLine() + "\n";
                try (FileOutputStream fos = new FileOutputStream(pipePath, true)) {
                    fos.write(message.getBytes());
                    fos.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
