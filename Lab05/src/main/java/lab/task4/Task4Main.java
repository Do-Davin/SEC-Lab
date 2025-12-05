package main.java.lab.task4;

import java.util.ArrayList;
import java.util.List;

public class Task4Main {

    private static final int INITIAL_BALANCE = 1000;
    private static final int THREAD_COUNT = 10;
    private static final int OPERATIONS_PER_THREAD = 100;

    public static void main(String[] args) throws InterruptedException {

        BankAccountUnsafe unsafeAccount = new BankAccountUnsafe(INITIAL_BALANCE);
        List<String> unsafeHistory = new ArrayList<>();
        List<Thread> unsafeThreads = new ArrayList<>();

        for (int i = 0; i < THREAD_COUNT; i++) {
            Thread t = new Thread(
                    new TransactionWorker("T" + (i + 1),
                            unsafeAccount, null, unsafeHistory,
                            false, OPERATIONS_PER_THREAD),
                    "Unsafe-T" + (i + 1)
            );
            unsafeThreads.add(t);
            t.start();
        }

        for (Thread t : unsafeThreads) {
            t.join();
        }

        int unsafeFinalBalance = unsafeAccount.getBalance();

        BankAccountSafe safeAccount = new BankAccountSafe(INITIAL_BALANCE);
        List<String> safeHistory = new ArrayList<>();
        List<Thread> safeThreads = new ArrayList<>();

        for (int i = 0; i < THREAD_COUNT; i++) {
            Thread t = new Thread(
                    new TransactionWorker("T" + (i + 1),
                            null, safeAccount, safeHistory,
                            true, OPERATIONS_PER_THREAD),
                    "Safe-T" + (i + 1)
            );
            safeThreads.add(t);
            t.start();
        }

        for (Thread t : safeThreads) {
            t.join();
        }

        int safeFinalBalance = safeAccount.getBalance();

        System.out.println("===== UNSAFE TRANSACTION HISTORY (first 20) =====");
        unsafeHistory.stream().limit(20).forEach(System.out::println);
        System.out.println("... (total " + unsafeHistory.size() + " operations)");
        System.out.println("UNSAFE final balance: $" + unsafeFinalBalance);

        System.out.println("\n===== SAFE TRANSACTION HISTORY (first 20) =====");
        safeHistory.stream().limit(20).forEach(System.out::println);
        System.out.println("... (total " + safeHistory.size() + " operations)");
        System.out.println("SAFE final balance: $" + safeFinalBalance);

        System.out.println("\n===== EXPLANATION =====");
        System.out.println("In the UNSAFE version, deposit/withdraw are not synchronized.");
        System.out.println("Multiple threads can read and write 'balance' at the same time,");
        System.out.println("which causes race conditions and an incorrect final balance.");
        System.out.println("In the SAFE version, methods are synchronized, so only one thread");
        System.out.println("can modify 'balance' at a time, leading to a consistent final balance.");
    }
}

