package cleancode.complextasks.secondtask;

public enum VideoFormat {
    AVI("avi"),
    MOV("mov"),
    WMV("wmv"),
    MP4("mp4"),
    MKV("mkv");//добавлено исключительно для тестов

    private String value;

    VideoFormat(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
