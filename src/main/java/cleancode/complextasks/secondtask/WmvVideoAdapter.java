package cleancode.complextasks.secondtask;

public class WmvVideoAdapter implements VideoAdapter {
    @Override
    public void convert(Video video) {
        if (!(video.getVideoFormat().equals(VideoFormat.WMV))) {
            System.out.println("Конвертации не произошло. Поддерживается только wmv формат");
            return;
        }
        System.out.println("Сконвертировано видео из формата: " + video.getVideoFormat() + " в формат " + VideoFormat.MP4);
        video.setVideoFormat(VideoFormat.MP4);
    }
}
