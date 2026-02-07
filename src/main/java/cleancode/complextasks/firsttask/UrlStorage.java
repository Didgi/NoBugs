package cleancode.complextasks.firsttask;

public interface UrlStorage {
    void save(String originalUrl, String shortenUrl);
    String getShortenUrl(String originalUrl);
    String getOriginalUrl(String shortenUrl);
}
