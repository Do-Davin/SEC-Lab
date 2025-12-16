package main.java.lab07.task4_dining_philosophers;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosophersSimulation {

    private enum State { THINKING, HUNGRY, EATING }

    private static final int N = 5;
    private static final long SIMULATION_MS = 2 * 60 * 1000; // 2 minutes
    private static final long FORK_TIMEOUT_MS = 120; // timeout mechanism
    private static final long REPORT_EVERY_MS = 5000;

    public static void main(String[] args) throws InterruptedException {
        ReentrantLock[] forks = new ReentrantLock[N];
        for (int i = 0; i < N; i++) forks[i] = new ReentrantLock(true); // fair fork locks

        Semaphore waiter = new Semaphore(N - 1, true); // allow only 4 philosophers to try at once
        AtomicBoolean running = new AtomicBoolean(true);

        State[] states = new State[N];
        Arrays.fill(states, State.THINKING);

        Stats[] stats = new Stats[N];
        for (int i = 0; i < N; i++) stats[i] = new Stats();

        Thread[] philosophers = new Thread[N];
        for (int i = 0; i < N; i++) {
            int id = i;
            philosophers[i] = new Thread(() ->
                    philosopherLoop(id, forks, waiter, running, states, stats),
                    "Philosopher-" + (id + 1)
            );
        }

        long start = System.currentTimeMillis();
        for (Thread p : philosophers) p.start();

        // Periodic reporter
        Thread reporter = new Thread(() -> {
            while (running.get()) {
                sleep(REPORT_EVERY_MS);
                printLive(states, stats, System.currentTimeMillis() - start);
            }
        }, "Reporter");
        reporter.setDaemon(true);
        reporter.start();

        // Run for 2 minutes
        Thread.sleep(SIMULATION_MS);
        running.set(false);

        // Stop all threads (in case someone is sleeping)
        for (Thread p : philosophers) p.interrupt();
        for (Thread p : philosophers) p.join();

        // Final stats
        System.out.println("\n================ FINAL STATISTICS ================");
        printLive(states, stats, System.currentTimeMillis() - start);

        boolean allAteAtLeast3 = true;
        for (int i = 0; i < N; i++) {
            if (stats[i].eatCount < 3) allAteAtLeast3 = false;
        }
        System.out.println("Requirement check: each philosopher ate at least 3 times -> " + (allAteAtLeast3 ? "YES ✅" : "NO ❌"));
        System.out.println("Task 4 done.");
    }

    private static void philosopherLoop(
            int id,
            ReentrantLock[] forks,
            Semaphore waiter,
            AtomicBoolean running,
            State[] states,
            Stats[] stats
    ) {
        Random rnd = new Random();
        int left = id;
        int right = (id + 1) % N;

        // Pick lower fork first to reduce circular waiting
        int first = Math.min(left, right);
        int second = Math.max(left, right);

        while (running.get() && !Thread.currentThread().isInterrupted()) {
            // THINK
            setState(states, id, State.THINKING);
            stats[id].thinkCount++;
            // If not yet ate 3 times, think less to ensure requirement
            sleep( (stats[id].eatCount < 3) ? (30 + rnd.nextInt(60)) : (80 + rnd.nextInt(200)) );

            // HUNGRY
            setState(states, id, State.HUNGRY);
            stats[id].hungryCount++;

            try {
                // Waiter prevents deadlock (only 4 can compete for forks)
                waiter.acquire();

                boolean gotFirst = false;
                boolean gotSecond = false;

                try {
                    gotFirst = forks[first].tryLock(FORK_TIMEOUT_MS, TimeUnit.MILLISECONDS);
                    if (!gotFirst) {
                        stats[id].timeouts++;
                        continue; // retry
                    }

                    gotSecond = forks[second].tryLock(FORK_TIMEOUT_MS, TimeUnit.MILLISECONDS);
                    if (!gotSecond) {
                        stats[id].timeouts++;
                        continue; // release first in finally
                    }

                    // EAT
                    setState(states, id, State.EATING);
                    stats[id].eatCount++;
                    sleep(40 + rnd.nextInt(120));

                } finally {
                    if (gotSecond) forks[second].unlock();
                    if (gotFirst) forks[first].unlock();
                    waiter.release();
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

            // small random backoff to reduce contention
            sleep(10 + rnd.nextInt(40));
        }
    }

    private static synchronized void setState(State[] states, int id, State s) {
        states[id] = s;
        System.out.printf("%s -> %s%n", Thread.currentThread().getName(), s);
    }

    private static void printLive(State[] states, Stats[] stats, long elapsedMs) {
        System.out.println("\n----- LIVE REPORT (" + (elapsedMs / 1000) + "s) -----");
        for (int i = 0; i < N; i++) {
            System.out.printf("P%d  state=%-8s | eat=%3d | hungry=%3d | think=%3d | timeouts=%3d%n",
                    (i + 1),
                    states[i],
                    stats[i].eatCount,
                    stats[i].hungryCount,
                    stats[i].thinkCount,
                    stats[i].timeouts
            );
        }
        System.out.println("-----------------------------------\n");
    }

    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    private static class Stats {
        volatile int eatCount = 0;
        volatile int hungryCount = 0;
        volatile int thinkCount = 0;
        volatile int timeouts = 0;
    }
}
