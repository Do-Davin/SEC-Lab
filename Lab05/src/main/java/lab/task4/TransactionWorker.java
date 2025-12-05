package main.java.lab.task4;

import java.util.List;
import java.util.Random;

public class TransactionWorker implements Runnable {

    private final String name;
    private final BankAccountUnsafe unsafeAccount;
    private final BankAccountSafe safeAccount;
    private final List<String> history;
    private final boolean useSafeAccount;
    private final Random random = new Random();
    private final int operations;

    public TransactionWorker(String name,
                             BankAccountUnsafe unsafeAccount,
                             BankAccountSafe safeAccount,
                             List<String> history,
                             boolean useSafeAccount,
                             int operations) {
        this.name = name;
        this.unsafeAccount = unsafeAccount;
        this.safeAccount = safeAccount;
        this.history = history;
        this.useSafeAccount = useSafeAccount;
        this.operations = operations;
    }

    @Override
    public void run() {
        for (int i = 0; i < operations; i++) {
            boolean deposit = random.nextBoolean();
            int amount = random.nextInt(50) + 1; // 1–50

            if (useSafeAccount) {
                if (deposit) {
                    safeAccount.deposit(amount);
                    int balance = safeAccount.getBalance();
                    synchronized (history) {
                        history.add("[SAFE] " + name + " deposited $" + amount + ", balance=" + balance);
                    }
                } else {
                    safeAccount.withdraw(amount);
                    int balance = safeAccount.getBalance();
                    synchronized (history) {
                        history.add("[SAFE] " + name + " withdrew $" + amount + ", balance=" + balance);
                    }
                }
            } else {
                if (deposit) {
                    unsafeAccount.deposit(amount);
                    int balance = unsafeAccount.getBalance();
                    synchronized (history) {
                        history.add("[UNSAFE] " + name + " deposited $" + amount + ", balance=" + balance);
                    }
                } else {
                    unsafeAccount.withdraw(amount);
                    int balance = unsafeAccount.getBalance();
                    synchronized (history) {
                        history.add("[UNSAFE] " + name + " withdrew $" + amount + ", balance=" + balance);
                    }
                }
            }
        }
    }
}

