package com.example;

import java.util.concurrent.Semaphore;

public class DbConnectionPool {
    private final Semaphore connectionPool = new Semaphore(3);
    public void dbConnection(int ThreadId){
        try {
            long startWaiting = System.currentTimeMillis();
            connectionPool.acquire();
            long endWaiting = System.currentTimeMillis();
            System.out.println("Thread "+ThreadId+": Connected to DB("+(endWaiting-startWaiting)+" ms");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally{
            System.out.println("Thread"+ThreadId+": Disconnecting");
            connectionPool.release();
        }
    }
}
