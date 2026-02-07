package cleancode.complextasks.firsttask;

public class UrlShortenerService {
    private static volatile UrlShortenerService instance;
    private ShorteningStrategy shorteningStrategy;
    private UrlStorage urlStorage;

    private UrlShortenerService(ShorteningStrategy shorteningStrategy) {
        this.shorteningStrategy = shorteningStrategy;
        this.urlStorage = MemoryStorage.getInstance();
    }

    public static UrlShortenerService getInstance(ShorteningStrategy shorteningStrategy) {
        if (instance == null) {
            synchronized (UrlShortenerService.class) {
                if (instance == null) {
                    instance = new UrlShortenerService(shorteningStrategy);
                }
            }
        }
        return instance;
    }

    public String makeShortenUrl(String originalUrl) {
        if (originalUrl == null) {
            throw new IllegalArgumentException("Передано null значение");
        }
        String shortenUrl = shorteningStrategy.makeShortenUrl(originalUrl.toLowerCase());
        urlStorage.save(originalUrl, shortenUrl);
        return shortenUrl;
    }

    public String getOriginalUrl(String encodedUrl) {
        if (encodedUrl == null) {
            throw new IllegalArgumentException("Передано null значение");
        }
        return urlStorage.getOriginalUrl(encodedUrl);
    }

    public void setShorteningStrategy(ShorteningStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("Передано null значение");
        }
        this.shorteningStrategy = strategy;
    }
}
