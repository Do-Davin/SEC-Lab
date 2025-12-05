package main.java.lab.task4;

public class BankAccountSafe {

    private int balance;

    public BankAccountSafe(int initialBalance) {
        this.balance = initialBalance;
    }

    public synchronized void deposit(int amount) {
        int newBalance = balance + amount;
        balance = newBalance;
    }

    public synchronized void withdraw(int amount) {
        if (balance >= amount) {
            int newBalance = balance - amount;
            balance = newBalance;
        }
    }

    public synchronized int getBalance() {
        return balance;
    }
}
