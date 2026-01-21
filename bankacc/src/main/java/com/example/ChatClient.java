package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    public static void main(String[] args) {
        String host = "localhost"; // or server IP
        int port = 5000;

        try (Socket socket = new Socket(host, port)) {
            System.out.println("==========================================");
            System.out.println("| Connected to server!                    |");
            System.out.println("==========================================");
            System.out.println();

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Thread to read messages from server
            new Thread(() -> {
                try {
                    String msg;
                    while ((msg = in.readLine()) != null) {
                        System.out.println("[Server]: " + msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Main thread sends messages from client console
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("[Enter message]: ");
                String message = scanner.nextLine();
                out.println(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
