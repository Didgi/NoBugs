package cleancode.complextasks.secondtask;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CheckExistVideoTests extends BaseTest {

    @Test
    @DisplayName("Позитивный тест: проверка добавленного видео в хранилище")
    public void checkExistVideoInStorageIsSuccessWhenVideoIsAdded() {
        String videoUrl = "test/test.avi";
        Video originalVideo = new Video(VideoFormat.AVI, videoUrl);
        videoStorage.addVideoToStorage(originalVideo);
        final Video actualVideo = videoStorage.checkExistVideo(videoUrl);
        assertEquals(originalVideo, actualVideo);
    }

    @Test
    @DisplayName("Негативный тест: проверка отсутствующего видео в хранилище")
    public void checkExistVideoInStorageReturnsNullWhenVideoIsNotAdded() {
        String videoUrl = "test/testFailed.avi";
        final Video actualVideo = videoStorage.checkExistVideo(videoUrl);
        assertEquals(null, actualVideo);
    }

}
