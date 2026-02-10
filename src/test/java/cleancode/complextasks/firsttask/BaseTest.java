package cleancode.complextasks.firsttask;

import org.junit.jupiter.api.BeforeEach;

import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ConcurrentHashMap;

public class BaseTest {
    ShorteningStrategy shorteningStrategy;
    UrlShortenerService urlShortenerService;
    ConcurrentHashMap<String, String> urlStorageMap;
    MemoryStorage memoryStorage;

    @BeforeEach
    public void setUp() throws NoSuchAlgorithmException {
        shorteningStrategy = ShortenerFactory.chooseAlgorithmStrategy(EnumAlgorithms.BASE_62);
        urlShortenerService = new UrlShortenerService(shorteningStrategy);
        memoryStorage = MemoryStorage.getInstance();
        urlStorageMap = memoryStorage.getUrlStorageMap();
        urlStorageMap.clear();
    }

}
