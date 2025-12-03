package lab05;

class SingleThreadTest {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new SingleThread();
        t1.setDaemon(true);
        t1.start();
        for (int i = 0; i < 100; i++) {
            System.out.print("\u001B[37m" + i + "\u001B[0m");
        }
        System.out.println();
        t1.join();
    }
}

public class SingleThread extends Thread {

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.print("\u001B[31m" + i + "\u001B[0m");
        }
        System.out.println();
    }
    
}
