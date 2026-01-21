package com.example;

import com.example.SharedVariableExample.SharedCounter;

public class Main {

    // public static void main(String[] args) throws InterruptedException {
    //     BankAccount acc = new BankAccount();
    //     Thread t1 = new Thread(()->acc.withdraw(600));
    //     Thread t2 = new Thread(()->acc.withdraw(500));
    //     t1.start();
    //     t2.start();
    //     t1.join();
    //     t2.join();
    //     System.out.println("Balances: "+acc.getBalance());
    // }
    // public static void main(String[] args) {
    //     DbConnectionPool pool = new DbConnectionPool();
    //     for (int i = 0; i < 10; i++) {
    //         final int threadId = i;
    //         new Thread(() -> pool.dbConnection(threadId)).start();
    //     }
    // }
    // public static void main(String[] args) throws InterruptedException{
    //     ProCon pc = new ProCon();
    //     for (int i = 0; i < 10; i++) {
    //         pc.produce(i);
    //         new Thread(() -> {
    //             try {
    //                 pc.consume();
    //             } catch (InterruptedException e) {
    //                 e.printStackTrace();
    //             }
    //         }).start();
    //     }
    // }
    // public static void main(String[] args) {
    //     Counter counter = new Counter();
    //     Thread th1 = new Thread(counter::Odd);
    //     Thread th2 = new Thread(counter::Even);
    //     th1.start();
    //     th2.start();
    // }
    // public static void main(String[] args) throws InterruptedException {
    //     Thread t1 = new Thread(new fiboThreading(100));
    //     t1.start();
    //     t1.join();
    // }
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

