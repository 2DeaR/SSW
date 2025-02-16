import org.example.ThreadRunnable;
import org.junit.jupiter.api.Test;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ThreadRunnableTest {
    private static final int LENGTH_CALCULATOR = 5;
    private static final int DELAY = 100;

    @Test
    void testLatchCountDown() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Thread thread = new Thread(new ThreadRunnable(1, LENGTH_CALCULATOR, DELAY, latch));
        thread.start();
        thread.join();
        assertEquals(0, latch.getCount(), "Latch should reach zero after thread completion");
    }

    @Test
    void testMultipleThreads() throws InterruptedException {
        int threadCount = 3;
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 1; i <= threadCount; i++) {
            new Thread(new ThreadRunnable(i, LENGTH_CALCULATOR, DELAY, latch)).start();
        }

        latch.await();
        assertEquals(0, latch.getCount(), "Latch should be zero after all threads complete");
    }
}