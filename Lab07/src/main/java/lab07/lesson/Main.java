package main.java.lab07.lesson;

import java.time.Clock;
import java.time.Instant;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        final Semaphore connectionPool = new Semaphore(8);
        Clock clock = Clock.systemDefaultZone();
        Instant time = clock.instant();

        try (var sc = new Scanner(System.in)) {
            System.out.print("Input number of Fibonacci (n): ");
            int n = Integer.parseInt(sc.nextLine());
            Fibonacci[] fns = new Fibonacci[n+1];
            long startTime = System.currentTimeMillis();
            for(int i=0;i<fns.length;i++){
                fns[i] = new Fibonacci(i);
                fns[i].start();
            }
            // System.out.println("Early Result = " + fn.getResult());
            // for(var fn : fns) fn.join();
            for(var fn : fns) connectionPool.acquire();
            // System.out.printf("Result = %,d\n", fn.getResult());
            long endTime = System.currentTimeMillis();
            long sum = 0;
            for(var fn : fns) sum += fn.getResult();
            System.out.printf("Sum = %,d\n",sum);
            System.out.printf("Time spent: %d ns\n", endTime - startTime);
            startTime = time.getNano();
            long sumWithoutThreads = 0;
            for (int i = 0; i <= n; i++) {
                var fnt = new FibonacciNoThread(i);
                fnt.calculate();
                sumWithoutThreads += fnt.getResult();
            }
            endTime = time.getNano();
            System.out.printf("Sum (Without Thread) = %,d\n",sumWithoutThreads);
            System.out.printf("Time spent (Without Thread): %d ns\n", endTime - startTime);
        }
    }
}
