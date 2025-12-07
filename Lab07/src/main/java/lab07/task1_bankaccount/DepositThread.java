package main.java.lab07.task1_bankaccount;

public class DepositThread extends Thread {

    private final BankAccount bankAccount;
    private final int amount;

    public DepositThread(BankAccount bankAccount, int amount) {
        this.bankAccount = bankAccount;
        this.amount = amount;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            bankAccount.deposit(amount);
            try {
                Thread.sleep(100); // simulate processing time
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}