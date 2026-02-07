package cleancode.complextasks.firsttask;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MakeShortenUrlGetOriginalUrlExceptionsTests extends BaseTest {

    @Test
    @DisplayName("Негативный тест: создание короткого урла по пустому урлу выбрасывает исключение")
    public void makeShortenUrlBase62ThrowsExceptionWhenIncomingUrlIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            urlShortenerService.makeShortenUrl(null);
        });
    }

    @Test
    @DisplayName("Негативный тест: получение оригинального урла по пустому урлу выбрасывает исключения")
    public void getOriginalUrlThrowsExceptionWhenIncomingEncodedUrlIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            urlShortenerService.getOriginalUrl(null);
        });
    }

}
