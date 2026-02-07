package cleancode.complextasks.firsttask;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class ShortenServiceGetInstanceDiffThreadsTest {

    @RepeatedTest(5)
    @DisplayName("Создание и получение инстансов в нескольких потоках для urlShortenService")
    public void getInstanceInDiffThreadsIsSuccessUrlShortenService() throws NoSuchAlgorithmException, InterruptedException {
        ShorteningStrategy shorteningStrategy = ShortenerFactory.chooseAlgorithmStrategy(EnumAlgorithms.BASE_62);
        AtomicReference<UrlShortenerService> urlShortenerServiceFirst = new AtomicReference<>();
        AtomicReference<UrlShortenerService> urlShortenerServiceFirst2 = new AtomicReference<>();
        Runnable task = () -> {
            for (int i = 0; i < 500; i++) {
                urlShortenerServiceFirst.set(UrlShortenerService.getInstance(shorteningStrategy));
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        };

        Runnable taskSecond = () -> {
            for (int i = 0; i < 1000; i++) {
                urlShortenerServiceFirst2.set(UrlShortenerService.getInstance(shorteningStrategy));
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        };

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(task);
        executorService.execute(taskSecond);
        executorService.shutdown();

        if (!(executorService.awaitTermination(15, TimeUnit.SECONDS))) {
            Thread.currentThread().interrupt();
            System.out.println("Принудительно завершены не завершённые потоки");
        }

        Assertions.assertNotNull(urlShortenerServiceFirst);
        Assertions.assertNotNull(urlShortenerServiceFirst2);

        Assertions.assertSame(urlShortenerServiceFirst2.get(), urlShortenerServiceFirst.get());
    }

}
