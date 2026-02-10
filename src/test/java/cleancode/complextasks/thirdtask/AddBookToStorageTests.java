package cleancode.complextasks.thirdtask;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddBookToStorageTests extends BaseTest{

    @Test
    @DisplayName("Позитивный тест: добавление книги в хранилище")
    public void addBookToStorageIsSuccess() {
        Book bookFirst = bookBuilder
                .setTitle("Убийство в восточном экспрессе")
                .setAuthor("Агата Кристи")
                .setDescription("Классный детектив")
                .build();

        BookStorage bookStorage = new BookStorage();

        final int originalStorageSize = BookStorage.getBooksStorage().size();
        assertEquals(0, originalStorageSize);

        bookStorage.addBook(bookFirst);

        int updatedStorageSize = BookStorage.getBooksStorage().size();
        assertEquals(1, updatedStorageSize);

        Book bookSecond = bookBuilder
                .setTitle("Внутри убийцы")
                .setAuthor("Марк Омер")
                .setDescription("Классный детектив")
                .build();

        bookStorage.addBook(bookSecond);

        updatedStorageSize = BookStorage.getBooksStorage().size();
        assertEquals(2, updatedStorageSize);

    }

    @Test
    @DisplayName("Негативный тест: добавление двух одинаковых книг в хранилище")
    public void addSameBookToStorageTwiceIsFailed() {
        Book bookFirst = bookBuilder
                .setTitle("Убийство в восточном экспрессе")
                .setAuthor("Агата Кристи")
                .setDescription("Классный детектив")
                .build();

        BookStorage bookStorage = new BookStorage();

        final int originalStorageSize = BookStorage.getBooksStorage().size();
        assertEquals(0, originalStorageSize);

        bookStorage.addBook(bookFirst);

        int updatedStorageSize = BookStorage.getBooksStorage().size();
        assertEquals(1, updatedStorageSize);

        bookStorage.addBook(bookFirst);

        updatedStorageSize = BookStorage.getBooksStorage().size();
        assertEquals(1, updatedStorageSize);

    }
}
