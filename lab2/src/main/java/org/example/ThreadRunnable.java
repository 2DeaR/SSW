package org.example;

import java.util.concurrent.CountDownLatch;

public class ThreadRunnable implements Runnable {
    private final int threadId;
    private final int length;
    private final int delay;
    private final CountDownLatch latch;
    private static final Object lock = new Object();

    public ThreadRunnable(int threadId, int length, int delay, CountDownLatch latch) {
        this.threadId = threadId;
        this.length = length;
        this.delay = delay;
        this.latch = latch;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        StringBuilder progress = new StringBuilder(" ".repeat(length));

        for (int i = 0; i < length; i++) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            progress.setCharAt(i, 'o');

            synchronized (lock) {
                System.out.print("\033[" + threadId + ";0H");
                System.out.printf("Thread %d: [%s]", threadId, progress);
            }
        }
        long endTime = System.currentTimeMillis();
        synchronized (lock) {
            System.out.print("\033[" + threadId + ";0H");
            System.out.printf("Thread %d completed in %d ms", threadId, (endTime - startTime));
        }
        latch.countDown();
    }
}