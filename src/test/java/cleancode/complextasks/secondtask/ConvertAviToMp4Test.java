package cleancode.complextasks.secondtask;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConvertAviToMp4Test extends BaseTest{
    @Test
    @DisplayName("Позитивный тест: Конвертация avi формата в mp4")
    public void convertAviToMp4IsSuccessWhenVideoIsAvi(){
        Video video = new Video(VideoFormat.AVI, "test/test.avi");
        assertEquals(VideoFormat.AVI, video.getVideoFormat());
        aviVideoAdapter.convert(video);
        assertEquals(VideoFormat.MP4, video.getVideoFormat());
    }
    @Test
    @DisplayName("Негативный тест: Конвертация не поддерживаемого формата в mp4")
    public void convertToMp4IsFailedWhenVideoIsUnsupported(){
        Video mkv = new Video(VideoFormat.MKV, "test/test.mkv");
        assertEquals(VideoFormat.MKV, mkv.getVideoFormat());
        aviVideoAdapter.convert(mkv);
        assertEquals(VideoFormat.MKV, mkv.getVideoFormat());
    }
}
