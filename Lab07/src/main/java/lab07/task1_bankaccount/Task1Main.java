package main.java.lab07.task1_bankaccount;

public class Task1Main {
    public static void main(String[] args) {
        
        BankAccount bankAccount = new BankAccount(1000);

        // Create multiple deposit & withdraw threads
        Thread t1 = new DepositThread(bankAccount, 200);
        Thread t2 = new WithdrawThread(bankAccount, 150);
        Thread t3 = new WithdrawThread(bankAccount, 300);
        Thread t4 = new DepositThread(bankAccount, 100);

        // Start threads
        t1.start();
        t2.start();
        t3.start();
        t4.start();

        // Wait for all threads to finish
        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Final balance
        System.out.println("\nFinal Balance: " + bankAccount.getBalance());
    }
}
