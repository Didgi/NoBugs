package cleancode.complextasks.firsttask;

public class UrlShortenerService {
    private ShorteningStrategy shorteningStrategy;
    private UrlStorage urlStorage;

    public UrlShortenerService(ShorteningStrategy shorteningStrategy) {
        this.shorteningStrategy = shorteningStrategy;
        this.urlStorage = MemoryStorage.getInstance();
    }

    public String makeShortenUrl(String originalUrl) {
        if (originalUrl == null) {
            throw new IllegalArgumentException("Передано null значение");
        }
        String lowerOriginalUrl = originalUrl.toLowerCase();
        String shortenUrl = shorteningStrategy.makeShortenUrl(lowerOriginalUrl);
        urlStorage.save(lowerOriginalUrl, shortenUrl);
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
