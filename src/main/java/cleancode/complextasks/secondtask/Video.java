package cleancode.complextasks.secondtask;


public class Video {
    private int id;
    private VideoFormat videoFormat;
    private String url;

    public Video(VideoFormat videoFormat, String url) {
        this.videoFormat = videoFormat;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public VideoFormat getVideoFormat() {
        return videoFormat;
    }

    public String getUrl() {
        return url;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setVideoFormat(VideoFormat videoFormat) {
        this.videoFormat = videoFormat;
    }

}
