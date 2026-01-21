package main.java.lab07.lesson;

import main.java.lab07.lesson.SharedVariableExample.SharedCounter;

public class Main2 {
    public static void main(String[] args) throws InterruptedException {

        SharedCounter sharedVar = new SharedCounter();
        Thread[] threads = new Thread[3];
        for (int i = 0; i < 3; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 5; j++) {
                    sharedVar.increment();
                }
            });
            threads[i].start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        System.out.println("Final value: " + sharedVar.getValue());
    }
}
