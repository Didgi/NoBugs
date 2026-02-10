package cleancode.complextasks.firsttask;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class MemoryStorageGetInstanceDiffThreadsTest extends BaseTest{

    @RepeatedTest(5)
    @DisplayName("Создание и получение инстансов в нескольких потоках для memory storage")
    public void getInstanceInDiffThreadsIsSuccessMemoryStorage() throws InterruptedException {
        AtomicReference<MemoryStorage> memoryStorageFirst = new AtomicReference<>();
        AtomicReference<MemoryStorage> memoryStorageSecond = new AtomicReference<>();
        CountDownLatch count = new CountDownLatch(2);
        Runnable task = () -> {
            for (int i = 0; i < 2000; i++) {
                memoryStorageFirst.set(MemoryStorage.getInstance());
                memoryStorageFirst.get().save(String.valueOf(i), String.valueOf(i));
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            count.countDown();

        };

        Runnable taskSecond = () -> {
            for (int i = 0; i < 2000; i++) {
                memoryStorageSecond.set(MemoryStorage.getInstance());
                memoryStorageSecond.get().save(String.valueOf(i), String.valueOf(i));
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            count.countDown();

        };

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(task);
        executorService.execute(taskSecond);

        if (!(count.await(30, TimeUnit.SECONDS))) {
            Thread.currentThread().interrupt();
            System.out.println("Принудительно завершены не завершённые потоки");
        }

        Assertions.assertNotNull(memoryStorageFirst);
        Assertions.assertNotNull(memoryStorageSecond);

        Assertions.assertSame(memoryStorageFirst.get(), memoryStorageSecond.get());
        Assertions.assertEquals(2000, memoryStorageFirst.get().getUrlStorageMap().size());
        Assertions.assertEquals(2000, memoryStorageSecond.get().getUrlStorageMap().size());
    }

}
