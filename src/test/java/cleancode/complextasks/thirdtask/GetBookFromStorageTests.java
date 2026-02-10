package cleancode.complextasks.thirdtask;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetBookFromStorageTests extends BaseTest{

    @Test
    @DisplayName("Позитивный тест: получение существующей книги из хранилища")
    public void getExistedBookFromStorageIsSuccess() {
        Book bookFirst = bookBuilder
                .setTitle("УБИЙСТВО В ВОСТОЧНОМ ЭКСПРЕССЕ")
                .setAuthor("Агата Кристи")
                .setDescription("Классный детектив")
                .build();

        BookStorage bookStorage = new BookStorage();

        bookStorage.addBook(bookFirst);

        Book bookFirstFromStorage = bookStorage.getBook(bookFirst);

        Assertions.assertEquals(bookFirst.getTitle(), bookFirstFromStorage.getTitle());
    }

    @Test
    @DisplayName("Негативный тест: получение не существующей книги из хранилища")
    public void getNotExistedBookFromStorageIsFailed() {
        Book bookFirst = bookBuilder
                .setTitle("УБИЙСТВО В ВОСТОЧНОМ ЭКСПРЕССЕ")
                .setAuthor("Агата Кристи")
                .setDescription("Классный детектив")
                .build();

        Book bookSecond = bookBuilder
                .setTitle("Внутри убийцы")
                .setAuthor("Марк Омер")
                .setDescription("Классный детектив")
                .build();

        BookStorage bookStorage = new BookStorage();

        bookStorage.addBook(bookFirst);

        Book bookFirstFromStorage = bookStorage.getBook(bookSecond);

        Assertions.assertNull(bookFirstFromStorage);
    }
}
