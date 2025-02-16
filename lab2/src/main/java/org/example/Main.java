package org.example;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static final int THREAD_COUNT = 3;
    public static final int LENGTH_CALCULATOR = 10;
    public static final int DELAY = 500;

    public static void main(String[] args) {
        System.out.print("\033[2J\033[H");
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        for (int i = 1; i <= THREAD_COUNT; i++) {
            executor.execute(new ThreadRunnable(i, LENGTH_CALCULATOR, DELAY, latch));
        }
        executor.shutdown();

        try {
            latch.await();
            System.out.print("\033[" + (THREAD_COUNT + 1) + ";0H");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}