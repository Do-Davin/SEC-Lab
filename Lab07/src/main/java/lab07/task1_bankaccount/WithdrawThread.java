package main.java.lab07.task1_bankaccount;

public class WithdrawThread extends Thread {
    
    private final BankAccount bankAccount;
    private final int amount;

    public WithdrawThread(BankAccount bankAccount, int amount) {
        this.bankAccount = bankAccount;
        this.amount = amount;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            bankAccount.withdraw(amount);
            try {
                Thread.sleep(120); // simulate processing time
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
