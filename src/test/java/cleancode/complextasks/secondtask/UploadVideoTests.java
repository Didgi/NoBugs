package cleancode.complextasks.secondtask;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UploadVideoTests extends BaseTest {

    public static Stream<Arguments> uploadAndConvertDiffVideoFormatsParam() {
        return Stream.of(
                Arguments.of(
                        "test/test.avi", VideoFormat.MP4),
                Arguments.of("test/test.mov", VideoFormat.MP4),
                Arguments.of("test/test.wmv", VideoFormat.MP4),
                Arguments.of("test/test.AVI", VideoFormat.MP4));
    }

    @ParameterizedTest
    @MethodSource("uploadAndConvertDiffVideoFormatsParam")
    @DisplayName("Позитивные тесты: загрузка поддерживаемого формата видео и конвертация в mp4")
    public void uploadAndConvertValidVideoFormatToMp4IsSuccessWhenVideoFormatIs(String videoUrl,
                                                                                VideoFormat expectedVideoformat) {
        Video video = videoService.uploadVideo(videoUrl);
        assertEquals(videoUrl.toLowerCase(), video.getUrl());
        assertEquals(expectedVideoformat, video.getVideoFormat());

    }

    @Test
    @DisplayName("Негативный тест: повторная загрузка ранее загруженного видео")
    public void uploadAndConvertSameVideoAgainManual() {
        String videoUrl = "test/test.avi";
        Video video = videoService.uploadVideo(videoUrl);
        assertEquals(videoUrl, video.getUrl());
        assertEquals(VideoFormat.MP4, video.getVideoFormat());
        assertEquals(1, video.getId());

        video = videoService.uploadVideo(videoUrl);
        assertEquals(videoUrl, video.getUrl());
        assertEquals(VideoFormat.MP4, video.getVideoFormat());
        assertEquals(1, video.getId());
    }

    @Test
    @DisplayName("Негативный тест: загрузка не поддерживаемого формата видео в mp4")
    public void uploadIsFailedWhenVideoIsOfUnsupportedFormat() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->{
            String videoUrl = "test/test.MKV";
            videoService.uploadVideo(videoUrl);
        });
    }
}
