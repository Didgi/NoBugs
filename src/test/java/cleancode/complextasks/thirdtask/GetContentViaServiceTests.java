package cleancode.complextasks.thirdtask;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetContentViaServiceTests extends BaseTest {

    @Test
    @DisplayName("Позитивный тест: получение существующей книги из хранилища")
    public void getExistedBookViaServiceIsSuccess() {
        Book bookFirst = bookBuilder
                .setTitle("УБИЙСТВО В ВОСТОЧНОМ ЭКСПРЕССЕ")
                .setAuthor("Агата Кристи")
                .setDescription("Классный детектив")
                .build();

        BookService bookService = new BookService(bookFirst);

        final int originalStorageSize = BookStorage.getBooksStorage().size();
        assertEquals(0, originalStorageSize);

        bookService.uploadBook(bookFirst);

        final int updatedStorageSize = BookStorage.getBooksStorage().size();
        assertEquals(1, updatedStorageSize);

        final String bookContent = bookService.getContent();

        Assertions.assertEquals(bookFirst.toString(), bookContent);
    }

    @Test
    @DisplayName("Негативный тест: получение не существующей книги из хранилища")
    public void getNotExistedBookViaServiceIsFailed() {
        Book bookFirst = bookBuilder
                .setTitle("УБИЙСТВО В ВОСТОЧНОМ ЭКСПРЕССЕ")
                .setAuthor("Агата Кристи")
                .setDescription("Классный детектив")
                .build();

        BookService bookService = new BookService(bookFirst);

        final String bookContent = bookService.getContent();

        Assertions.assertNull(bookContent);
    }
}
