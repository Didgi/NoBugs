package cleancode.complextasks.secondtask;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddVideoToStorageTests extends BaseTest {

    @Test
    @DisplayName("Позитивный тест: добавление видео в хранилище")
    public void addVideoToStorageIsSuccess() {
        String videoUrl = "test/test.avi";
        Video originalVideo = new Video(VideoFormat.AVI, videoUrl);
        int expectedStorageSize = 0;
        int actualStorageSize = videoStorage.getSize();
        assertEquals(expectedStorageSize, actualStorageSize);
        final int videoId = videoStorage.addVideoToStorage(originalVideo);
        expectedStorageSize = 1;
        actualStorageSize = videoStorage.getSize();
        assertEquals(expectedStorageSize, actualStorageSize);
        final Video actualVideoFromStorage = videoStorage.getVideo(videoId);
        assertEquals(originalVideo, actualVideoFromStorage);
    }

}
