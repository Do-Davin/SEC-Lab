package com.example;

public class Counter {
    private int num = 1;
    private int max = 10;
    private boolean thread1 = true;

    public synchronized void Odd(){
        while(num <= max){
            while(!thread1){
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            if(num<=max){
                System.out.println("Thread1: "+num++);
            }
            thread1 = false;
            notifyAll();
        }
    }

    public synchronized void Even(){
        while(num<=max){
            while(thread1){
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            if(num<=max){
                System.out.println("Thread2: "+num++);
            }
            thread1 = true;
            notifyAll();
        }
    }
}
