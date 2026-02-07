package cleancode.complextasks.secondtask;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class VideoStorage {
    private static int count = 1;
    private int id;
    private HashMap<Integer, Video> videoStorage;
    private final ReentrantLock lock = new ReentrantLock(true);

    public VideoStorage() {
        this.videoStorage = new HashMap<>();
    }

    public int addVideoToStorage(Video video) {
        lock.lock();
        try {
            this.id = count++;
            videoStorage.put(id, video);
            return id;
        } finally {
            lock.unlock();
        }
    }

    public Video checkExistVideo(String url) {
        final Optional<Video> result = videoStorage.values().stream().filter(v -> v.getUrl().equals(url.toLowerCase())).findFirst();
        if (result.isEmpty()) {
            return null;
        }
        System.out.println("Видео " + url + " уже было загружено ранее и сконвертировано в MP4");
        return result.get();
    }

    public Video getVideo(int id) {
        return videoStorage.get(id);
    }

    public int getSize() {
        return videoStorage.size();
    }

    public static void setCount(int count) {
        VideoStorage.count = count;
        //добавил исключительно для тестов, т.к. не придумал иного выхода,
        // чтобы тест uploadAndConvertSameVideoAgainManual не падал,
        //когда происходит запуск всех тестов внутри пакета
    }
}
