package cleancode.complextasks.firsttask;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

public class MakeShortenUrlByMD5Tests extends BaseTest {

    @Test
    @DisplayName("Позитивный тест: смена стратегии с Base62 на MD5")
    public void changeStrategyFromBase62ToMD5EncodingUrl() throws NoSuchAlgorithmException {
        String originalUrl = "https://nobugs.me/members/courses/want-an-offer-in-qa-auto/domasnee-zadanie-kompleksnye-zadaci-780471161126";
        String expectedShortenUrlBase62 = "9YZoKyqA7eK2UzB2jUAbMGZnwtWrAUYOv19yVWDh9IIwOLGXNEkgVFdRRNWBeqnRTsbA4OW0NouwGAJzDTZ9lB1oLd2m9tRL8Sxo1Kd9VzokCLRE9y3q7zNianrfYI1xYDosGmz9kEqw7j4Y";
        final String madeActualShortenUrlBase62 = urlShortenerService.makeShortenUrl(originalUrl);
        Assertions.assertEquals(expectedShortenUrlBase62, madeActualShortenUrlBase62);
        final String receivedOriginalUrlFromMemoryBase62 = urlShortenerService.getOriginalUrl(madeActualShortenUrlBase62);
        Assertions.assertEquals(receivedOriginalUrlFromMemoryBase62, originalUrl);
        Assertions.assertEquals(1, urlStorageMap.size());

        shorteningStrategy = ShortenerFactory.chooseAlgorithmStrategy(EnumAlgorithms.MD5);
        urlShortenerService.setShorteningStrategy(shorteningStrategy);
        String expectedShortenUrlMD5 = "5aa3c8ee1cdb51c93d8c1235d935a303";
        final String madeActualShortenUrlMD5 = urlShortenerService.makeShortenUrl(originalUrl);
        Assertions.assertEquals(expectedShortenUrlMD5, madeActualShortenUrlMD5);
        final String receivedOriginalUrlFromMemoryMD5 = urlShortenerService.getOriginalUrl(madeActualShortenUrlMD5);
        Assertions.assertEquals(receivedOriginalUrlFromMemoryMD5, originalUrl);
        Assertions.assertEquals(1, urlStorageMap.size());
    }

}
