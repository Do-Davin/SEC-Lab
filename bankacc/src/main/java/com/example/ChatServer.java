package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import main.java.com.example.MessageUtil;
import main.java.com.example.messageUtil;

public class ChatServer {
    public static void main(String[] args) {
        int port = 5000;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("==========================================");
            System.out.println("| Server started. Waiting for client...   |");
            Socket socket = serverSocket.accept();
            System.out.println("| Client connected!                       |");
            System.out.println("==========================================");

            // Streams for communication
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Thread to read messages from client
            new Thread(() -> {
                try {
                    String msg;
                    while ((msg = in.readLine()) != null) {
                        System.out.println("[Client]: " + msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Main thread sends messages from server console
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




