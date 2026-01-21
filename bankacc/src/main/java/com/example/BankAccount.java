package com.example;

import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {

    private double balance = 1000.0;
    private final ReentrantLock lock = new ReentrantLock();

    public synchronized void withdraw(double amount) {
        lock.lock();
        try {
            if (balance >= amount) {       
                balance = balance - amount;          
                System.out.println("Withdrawn: " + amount);
            } else {
                System.out.println("Insufficient funds");
            }
        } finally {
            lock.unlock();
        }
    }

    public synchronized void deposit(double amount) {
        if (amount <= 0) {
            System.err.println("Amount must be positive");
        } else {
            balance += amount;
            System.out.println("Depositted" + amount);
        }
    }

    public double getBalance() {
        return balance;
    }

}
