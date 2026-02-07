package cleancode.complextasks.secondtask;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConvertMovToMp4Test extends BaseTest {
    @Test
    @DisplayName("Позитивный тест: Конвертация mov формата в mp4")
    public void convertMovToMp4IsSuccessWhenVideoIsMov() {
        Video video = new Video(VideoFormat.MOV, "test/test.avi");
        assertEquals(VideoFormat.MOV, video.getVideoFormat());
        movVideoAdapter.convert(video);
        assertEquals(VideoFormat.MP4, video.getVideoFormat());
    }

    @Test
    @DisplayName("Негативный тест: Конвертация не поддерживаемого формата в mp4")
    public void convertToMp4IsFailedWhenVideoIsUnsupported() {
        Video mkv = new Video(VideoFormat.MKV, "test/test.mkv");
        assertEquals(VideoFormat.MKV, mkv.getVideoFormat());
        movVideoAdapter.convert(mkv);
        assertEquals(VideoFormat.MKV, mkv.getVideoFormat());
    }
}
