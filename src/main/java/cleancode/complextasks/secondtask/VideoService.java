package cleancode.complextasks.secondtask;

import java.io.IOException;

public class VideoService implements Uploading, Streaming {

    private final AviVideoAdapter aviVideoAdapter;
    private final MovVideoAdapter movVideoAdapter;
    private final WmvVideoAdapter wmvVideoAdapter;
    private Video video;
    private VideoStorage videoStorage;

    public VideoService(AviVideoAdapter aviVideoAdapter, MovVideoAdapter movVideoAdapter, WmvVideoAdapter wmvVideoAdapter) {
        this.aviVideoAdapter = aviVideoAdapter;
        this.movVideoAdapter = movVideoAdapter;
        this.wmvVideoAdapter = wmvVideoAdapter;
        videoStorage = new VideoStorage();
    }

    @Override
    public Video uploadVideo(String url) {
        Video checkVideo = videoStorage.checkExistVideo(url);
        if (checkVideo != null) {
            return checkVideo;
        }

        String formatVideo = url.toLowerCase().replaceAll(".*\\.", "");
        System.out.println("Загружено видео в формате: " + formatVideo);
        if (formatVideo.equals(VideoFormat.AVI.getValue())) {
            video = new Video(VideoFormat.AVI, url);
            aviVideoAdapter.convert(video);
        } else if (formatVideo.equals(VideoFormat.MOV.getValue())) {
            video = new Video(VideoFormat.MOV, url);
            movVideoAdapter.convert(video);
        } else if (formatVideo.equals(VideoFormat.WMV.getValue())) {
            video = new Video(VideoFormat.WMV, url);
            wmvVideoAdapter.convert(video);
        } else {
            throw new IllegalArgumentException("Загружено видео в неподдержимаемом формате: " + formatVideo + ". Поддерживаются " +
                    "следующие форматы: avi, mov, wmv");
        }
        final int videoId = videoStorage.addVideoToStorage(video);
        video.setId(videoId);
        return video;
    }

    @Override
    public void streamVideo(int id) throws IOException {
        final Video checkVideo = videoStorage.getVideo(id);
        if (checkVideo == null) {
            throw new IOException("По текущему id видео отсутствует");
        }
        System.out.println("Запущено видео в формате: " + video.getVideoFormat() + " с videoId: " + id);
    }
}
