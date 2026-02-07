package cleancode.complextasks.firsttask;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

public class MakeShortenUrlByBase62Tests extends BaseTest {

    @Test
    @DisplayName("Позитивный тест: создание короткого урла в Base62 по не пустому урлу")
    public void makeShortenUrlBase62IsSuccessWhenIncomingUrlIsNotNull() throws NoSuchAlgorithmException {
        String originalUrl = "https://nobugs.me/members/courses/want-an-offer-in-qa-auto/domasnee-zadanie-kompleksnye-zadaci-780471161126";
        String expectedShortenUrl = "9YZoKyqA7eK2UzB2jUAbMGZnwtWrAUYOv19yVWDh9IIwOLGXNEkgVFdRRNWBeqnRTsbA4OW0NouwGAJzDTZ9lB1oLd2m9tRL8Sxo1Kd9VzokCLRE9y3q7zNianrfYI1xYDosGmz9kEqw7j4Y";
        final String madeActualShortenUrl = urlShortenerService.makeShortenUrl(originalUrl);
        Assertions.assertEquals(expectedShortenUrl, madeActualShortenUrl);
        final String receivedOriginalUrlFromMemory = urlShortenerService.getOriginalUrl(madeActualShortenUrl);
        Assertions.assertEquals(receivedOriginalUrlFromMemory, originalUrl);
    }

    @Test
    @DisplayName("Позитивный тест: повторное создание короткого урла по тому же ранее обработанному исходному урлу")
    public void makeShortenUrlBase62AgainIsSuccessWhenIncomingUrlIsAlreadyProcessedBefore() {
        String originalUrl = "https://nobugs.me/members/courses/want-an-offer-in-qa-auto/domasnee-zadanie-kompleksnye-zadaci-780471161126";
        String expectedShortenUrl = "9YZoKyqA7eK2UzB2jUAbMGZnwtWrAUYOv19yVWDh9IIwOLGXNEkgVFdRRNWBeqnRTsbA4OW0NouwGAJzDTZ9lB1oLd2m9tRL8Sxo1Kd9VzokCLRE9y3q7zNianrfYI1xYDosGmz9kEqw7j4Y";
        final String madeActualShortenUrl = urlShortenerService.makeShortenUrl(originalUrl);
        Assertions.assertEquals(expectedShortenUrl, madeActualShortenUrl);
        Assertions.assertEquals(1, urlStorageMap.size());
        final String madeActualShortenUrlAgain = urlShortenerService.makeShortenUrl(originalUrl);
        Assertions.assertEquals(madeActualShortenUrl, madeActualShortenUrlAgain);
        Assertions.assertEquals(1, urlStorageMap.size());
    }

}
