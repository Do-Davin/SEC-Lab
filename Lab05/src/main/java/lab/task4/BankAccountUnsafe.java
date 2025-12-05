package main.java.lab.task4;

public class BankAccountUnsafe {

    private int balance;

    public BankAccountUnsafe(int initialBalance) {
        this.balance = initialBalance;
    }

    public void deposit(int amount) {
        int newBalance = balance + amount;
        Thread.yield();
        balance = newBalance;
    }

    public void withdraw(int amount) {
        if (balance >= amount) {
            int newBalance = balance - amount;
            Thread.yield();
            balance = newBalance;
        }
    }

    public int getBalance() {
        return balance;
    }
}
