package cleancode.complextasks.secondtask;

public class MovVideoAdapter implements VideoAdapter {
    @Override
    public void convert(Video video) {
        if (!(video.getVideoFormat().equals(VideoFormat.MOV))) {
            System.out.println("Конвертации не произошло. Поддерживается только mov формат");
            return;
        }
        System.out.println("Сконвертировано видео из формата: " + video.getVideoFormat() + " в формат " + VideoFormat.MP4);
        video.setVideoFormat(VideoFormat.MP4);
    }
}
