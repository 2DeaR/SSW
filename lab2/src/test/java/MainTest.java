import org.example.ThreadRunnable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class MainTest {
    private static final int THREAD_COUNT = 3;
    private static final int LENGTH_CALCULATOR = 5;
    private static final int DELAY = 100;

    @Test
    void testMainExecution() {
        assertTimeoutPreemptively(Duration.ofMillis(LENGTH_CALCULATOR * DELAY + 500), () -> {
            ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
            CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

            for (int i = 1; i <= THREAD_COUNT; i++) {
                executor.execute(new ThreadRunnable(i, LENGTH_CALCULATOR, DELAY, latch));
            }
            executor.shutdown();
            latch.await();
        });
    }
}