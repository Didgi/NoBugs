package cleancode.complextasks.thirdtask;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProxyGetContentTests {

    @Test
    @DisplayName("Позитивный тест: загрузка книги происходит только при первичном обращении к ней")
    public void uploadBookIsDoneWhenBookCallsForTheFirstTime() {
        BookBuilder bookBuilder = new BookBuilder();
        Book book = bookBuilder
                .setTitle("Война и мир")
                .setAuthor("А. Толстой")
                .setDescription("Большая книга о войне")
                .build();

        BookProxy bookProxy = new BookProxy(book);

        final int originalStorageSize = bookProxy.getBookService().getBookStorage().getBooksStorage().size();

        assertEquals(0, originalStorageSize);

        bookProxy.getContent();

        final int updatedStorageSize = bookProxy.getBookService().getBookStorage().getBooksStorage().size();
        assertEquals(1, updatedStorageSize);
    }

    @Test
    @DisplayName("Позитивный тест: повторное обращение к книге не приводит к загрузке")
    public void dontUploadBookAgainWhenBookCallsForTheFirstTime() {
        BookBuilder bookBuilder = new BookBuilder();
        Book book = bookBuilder
                .setTitle("Война и мир")
                .setAuthor("А. Толстой")
                .setDescription("Большая книга о войне")
                .build();

        BookProxy bookProxy = new BookProxy(book);

        final int originalStorageSize = bookProxy.getBookService().getBookStorage().getBooksStorage().size();

        assertEquals(0, originalStorageSize);

        String expectedTextAfterUploading = "Книга добавлена в библиотеку";
        String expectedTextAfterGetting = "Книга получена из библиотеки";

        final String firstContent = bookProxy.getContent();

        assertTrue(firstContent.contains(expectedTextAfterUploading));
        assertFalse(firstContent.contains(expectedTextAfterGetting));

        final String secondContent = bookProxy.getContent();

        assertFalse(secondContent.contains(expectedTextAfterUploading));
        assertTrue(secondContent.contains(expectedTextAfterGetting));

        //немного не понимаю, как здесь вовсе можно проверить, что книга не загружена вновь, а получена из хранилища,
        //т.к. даже если бы книга попыталась добавиться в него, то по переопределённым hashcode/equals этого бы
        //не произошло. Пока сделал вот таким кустарным способом
    }
}
