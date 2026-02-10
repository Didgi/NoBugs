package cleancode.complextasks.thirdtask;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProxyGetContentTests extends BaseTest {

    @Test
    @DisplayName("Позитивный тест: загрузка книги происходит только при первичном обращении к ней")
    public void uploadBookIsDoneWhenBookCallsForTheFirstTime() {
        Book book = bookBuilder
                .setTitle("Война и мир")
                .setAuthor("А. Толстой")
                .setDescription("Большая книга о войне")
                .build();

        BookProxy bookProxy = new BookProxy(book);

        final int originalStorageSize = BookStorage.getBooksStorage().size();
        assertEquals(0, originalStorageSize);

        bookProxy.getContent();

        final int updatedStorageSize = BookStorage.getBooksStorage().size();
        assertEquals(1, updatedStorageSize);
    }

    @Test
    @DisplayName("Позитивный тест: повторное обращение к книге не приводит к загрузке")
    public void dontUploadBookAgainWhenBookCallsNotForTheFirstTime() {
        Book book = bookBuilder
                .setTitle("Война и мир")
                .setAuthor("А. Толстой")
                .setDescription("Большая книга о войне")
                .build();

        BookProxy bookProxy = new BookProxy(book);

        final int originalStorageSize = BookStorage.getBooksStorage().size();

        assertEquals(0, originalStorageSize);

        bookProxy.getContent();

        final int updatedFirstStorageSize = BookStorage.getBooksStorage().size();
        assertEquals(1, updatedFirstStorageSize);

        bookProxy.getContent();

        final int updatedSecondStorageSize = BookStorage.getBooksStorage().size();
        assertEquals(1, updatedSecondStorageSize);
    }
}
