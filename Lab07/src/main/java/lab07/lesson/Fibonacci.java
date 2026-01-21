package main.java.lab07.lesson;

public class Fibonacci extends Thread {
    private long result;
    private long n;
    public Fibonacci(int n) {
        this.n = n;
    }
    public long getResult() {
        return result;
    }
    public long getN() {
        return n;
    }
    @Override
    public void run() {
        if (n <= 0) result = 0;
        else if (n == 1) result = 1;
        else {
            long fn_2 = 0, fn_1 = 1;
            result = fn_2 + fn_1;
            for (int i = 3; i <= n; i++) {
                fn_2 = fn_1;
                fn_1 = result;
                result = fn_2 + fn_1;
            }
        }
    }
}
