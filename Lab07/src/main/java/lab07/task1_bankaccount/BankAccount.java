package main.java.lab07.task1_bankaccount;

public class BankAccount {
    
    private int balance;

    public BankAccount(int balance) {
        this.balance = balance;
    }

    // Deposit money safely (atomic operation)
    public synchronized void deposit(int amount) {
        balance += amount;
        System.out.println(Thread.currentThread().getName() +
        " deposited " + amount + " | New Balance: " + balance);
    }

    // Withdraw money safely (atomic operation)
    public synchronized void withdraw(int amount) {
        if (balance >= amount) {
            balance -= amount;
            System.out.println(Thread.currentThread().getName() +
            " withdraw " + amount + " | New Balance: " + balance);
        } else {
            System.out.println(Thread.currentThread().getName() +
            " attempted to withdraw " + amount + " | Insufficient funds!");
        }
    }

    public synchronized int getBalance() {
        return balance;
    }
}
