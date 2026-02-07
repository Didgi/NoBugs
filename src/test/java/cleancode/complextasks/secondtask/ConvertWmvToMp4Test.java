package cleancode.complextasks.secondtask;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConvertWmvToMp4Test extends BaseTest {
    @Test
    @DisplayName("Позитивный тест: Конвертация wmv формата в mp4")
    public void convertWmvToMp4IsSuccessWhenVideoIsWmv() {
        Video video = new Video(VideoFormat.WMV, "test/test.avi");
        assertEquals(VideoFormat.WMV, video.getVideoFormat());
        wmvVideoAdapter.convert(video);
        assertEquals(VideoFormat.MP4, video.getVideoFormat());
    }

    @Test
    @DisplayName("Негативный тест: Конвертация не поддерживаемого формата в mp4")
    public void convertToMp4IsFailedWhenVideoIsUnsupported() {
        Video mkv = new Video(VideoFormat.MKV, "test/test.mkv");
        assertEquals(VideoFormat.MKV, mkv.getVideoFormat());
        wmvVideoAdapter.convert(mkv);
        assertEquals(VideoFormat.MKV, mkv.getVideoFormat());
    }
}
