package cleancode.complextasks.firsttask;

import org.junit.jupiter.api.BeforeEach;

import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ConcurrentHashMap;

public class BaseTest {
    ShorteningStrategy shorteningStrategy;
    UrlShortenerService urlShortenerService;
    ConcurrentHashMap<String, String> urlStorageMap;

    @BeforeEach
    public void setUp() throws NoSuchAlgorithmException {
        shorteningStrategy = ShortenerFactory.chooseAlgorithmStrategy(EnumAlgorithms.BASE_62);
        urlShortenerService = UrlShortenerService.getInstance(shorteningStrategy);
        urlShortenerService.setShorteningStrategy(shorteningStrategy);
        urlStorageMap = MemoryStorage.getUrlStorageMap();
        MemoryStorage.getUrlStorageMap().clear();
    }

}
