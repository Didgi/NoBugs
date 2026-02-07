package cleancode.complextasks.secondtask;

import org.junit.jupiter.api.BeforeEach;

public class BaseTest {
    AviVideoAdapter aviVideoAdapter;
    MovVideoAdapter movVideoAdapter;
    WmvVideoAdapter wmvVideoAdapter;
    VideoService videoService;
    VideoStorage videoStorage;

    @BeforeEach
    public void setUp() {
        aviVideoAdapter = new AviVideoAdapter();
        movVideoAdapter = new MovVideoAdapter();
        wmvVideoAdapter = new WmvVideoAdapter();
        videoService = new VideoService(aviVideoAdapter, movVideoAdapter, wmvVideoAdapter);
        videoStorage = new VideoStorage();
        VideoStorage.setCount(1);
    }
}
