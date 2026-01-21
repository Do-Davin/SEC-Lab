package main.java.lab07.lesson;

public class FibonacciNoThread {
    private long result;
    private int n;
    
    public FibonacciNoThread(int n) {
        this.n = n;
    }
    public long getResult() {
        return result;
    }
    public void setResult(long result) {
        this.result = result;
    }
    public int getN() {
        return n;
    }
    public void setN(int n) {
        this.n = n;
    }
    public void calculate() {
        if (n <= 0)
            result = 0;
        else if (n == 1)
            result = 1;
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
