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

        for (int i = 0; i <= length; i++) {
            int procent = (i * 100) / length;
            if (i == length) {
                progress.setCharAt(length - 1, 'o');
            } else {
                progress.setCharAt(i, 'o');
            }

            synchronized (lock) {
                System.out.print("\033[" + threadId + ";0H");
                System.out.printf("Thread %d: [%s] %d%%", threadId, progress, procent);
            }

            if (i < length) {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
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