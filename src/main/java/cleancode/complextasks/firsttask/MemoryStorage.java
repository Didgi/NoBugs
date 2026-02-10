package cleancode.complextasks.firsttask;

import java.util.concurrent.ConcurrentHashMap;

public class MemoryStorage implements UrlStorage {
    private ConcurrentHashMap<String, String> urlStorageMap;
    private ConcurrentHashMap<String, String> urlStorageMapShortenOriginal;

    private MemoryStorage() {
        urlStorageMap = new ConcurrentHashMap<>();
        urlStorageMapShortenOriginal = new ConcurrentHashMap<>();
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
        urlStorageMapShortenOriginal.put(shortenUrl, originalUrl);
    }

    @Override
    public String getShortenUrl(String originalUrl) {
        return urlStorageMap.get(originalUrl);
    }

    @Override
    public String getOriginalUrl(String shortenUrl) {
        return urlStorageMapShortenOriginal.get(shortenUrl);
    }

    public ConcurrentHashMap<String, String> getUrlStorageMap() {
        return urlStorageMap;
    }

}
