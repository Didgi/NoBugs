package cleancode.complextasks.firsttask;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryStorage implements UrlStorage {
    private static ConcurrentHashMap<String, String> urlStorageMap;

    private MemoryStorage() {
        urlStorageMap = new ConcurrentHashMap<>();
    }

    public static class Helper {
        static final MemoryStorage instance = new MemoryStorage();
    }

    public static MemoryStorage getInstance() {
        return Helper.instance;
    }

    @Override
    public void save(String originalUrl, String shortenUrl) {
        urlStorageMap.put(originalUrl, shortenUrl);
    }

    @Override
    public String getShortenUrl(String originalUrl) {
        return urlStorageMap.get(originalUrl);
    }

    @Override
    public String getOriginalUrl(String shortenUrl) {
        final Optional<Map.Entry<String, String>> result = urlStorageMap.entrySet().stream()
                .filter(pair -> pair.getValue().equals(shortenUrl)).findFirst();
        return result.map(Map.Entry::getKey).orElse(null);
    }

    public static ConcurrentHashMap<String, String> getUrlStorageMap() {
        return urlStorageMap;
    }


}
