package com.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PipeReader {
    public static void main(String[] args) {
        String pipePath = "./pipefile.txt";
        File file = new File(pipePath);

        System.out.println("Reader started. Waiting for messages...");

        try {
            long lastLength = 0;
            while (true) {
                if (file.exists() && file.length() > lastLength) {
                    try (FileInputStream fis = new FileInputStream(file)) {
                        fis.skip(lastLength);
                        int data;
                        while ((data = fis.read()) != -1) {
                            System.out.print((char) data);
                        }
                        lastLength = file.length();
                    }
                }
                Thread.sleep(1000);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
