package cleancode.complextasks.thirdtask;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ProxyGetContentTestsWithSpy extends BaseTest {

    @Test
    @DisplayName("Негативный тест: проверка количества вызова метода uploadBook при повторном " +
            "обращении к ранее загруженной книге")
    public void checkCountCallingUploadBookWhenBookCallsNotForTheFirstTime() {
        Book book = bookBuilder
                .setTitle("Война и мир")
                .setAuthor("А. Толстой")
                .setDescription("Большая книга о войне")
                .build();

        BookService bookServiceSpy = spy(new BookService(book));
        BookProxy bookProxy = new BookProxy(book, bookServiceSpy);

        bookProxy.getContent();

        bookProxy.getContent();

        verify(bookServiceSpy, times(1)).uploadBook(book);

    }
}
