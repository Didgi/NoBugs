package cleancode.complextasks.secondtask;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Random;

public class StreamVideoTests extends BaseTest {

    @Test
    @DisplayName("Позитивный тест: просмотр существуюшего видео")
    public void streamVideoIsSuccessWhenVideoIsExist() throws IOException {
        Assertions.assertDoesNotThrow(() -> {
            String videoUrl = "test/test.avi";
            Video video = videoService.uploadVideo(videoUrl);
            videoService.streamVideo(video.getId());
        });
    }

    @Test
    @DisplayName("Негативный тест: просмотр не существуюшего видео")
    public void streamVideoThrowsExceptionWhenVideoIsNotExist() {
        Assertions.assertThrows(IOException.class, () -> {
            int randomId = new Random().nextInt(50);
            videoService.streamVideo(randomId);
        });
    }


}
