package cleancode.complextasks.secondtask;

public class AviVideoAdapter implements VideoAdapter {
    @Override
    public void convert(Video video) {
        if (!(video.getVideoFormat().equals(VideoFormat.AVI))) {
            System.out.println("Конвертации не произошло. Поддерживается только avi формат");
            return;
        }
        System.out.println("Сконвертировано видео из формата: " + video.getVideoFormat() + " в формат " + VideoFormat.MP4);
        video.setVideoFormat(VideoFormat.MP4);
    }
}
