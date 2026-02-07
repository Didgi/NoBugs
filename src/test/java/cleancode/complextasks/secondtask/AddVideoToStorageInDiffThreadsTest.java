package cleancode.complextasks.secondtask;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddVideoToStorageInDiffThreadsTest extends BaseTest {

    @RepeatedTest(5)
    @DisplayName("Позитивные тесты: добавление видео в хранилище в несколько потоков")
    public void uploadAndConvertValidVideoFormatToMp4IsSuccessWhenVideoFormatIs() throws InterruptedException {
        AtomicInteger totalRequests = new AtomicInteger();
        Runnable taskOne = () -> {
            for (int i = 0; i < 500; i++) {
                totalRequests.getAndIncrement();
                String videoUrl = "test/test"+ i + ".avi";
                Video video= new Video(VideoFormat.AVI, videoUrl);
                videoStorage.addVideoToStorage(video);
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Runnable taskSecond = () -> {
            for (int i = 0; i < 500; i++) {
                totalRequests.getAndIncrement();
                String videoUrl = "test/test"+ i + ".mov";
                Video video= new Video(VideoFormat.AVI, videoUrl);
                videoStorage.addVideoToStorage(video);
                try {
                    Thread.sleep(3);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        Thread threadOne = new Thread(taskOne);
        Thread threadSecond = new Thread(taskSecond);
        threadOne.start();
        threadSecond.start();
        threadOne.join();
        threadSecond.join();
        assertEquals(totalRequests.get(), videoStorage.getSize());

    }

}
