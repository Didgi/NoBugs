package cleancode.complextasks.thirdtask;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProxyGetContentInDiffThreadsTest {

    @RepeatedTest(5)
    @DisplayName("Позитивный тест: загрузка книг впервые и получение из памяти в несколько потоков")
    public void uploadBooksAndGetFromStorageIsDiffThreads() throws InterruptedException {


        List<BookBuilder> bookBuilders = new CopyOnWriteArrayList<>();
        List<BookProxy> bookProxies = new CopyOnWriteArrayList<>();
        List<Book> books = new CopyOnWriteArrayList<>();
        Runnable task = () -> {
            for (int i = 0; i < 2000; i++) {
                bookBuilders.add(i, new BookBuilder());
            }
            for (int i = 0; i < 2000; i++) {
                Book buildBook = bookBuilders.get(i)
                        .setTitle("Война и мир" + i)
                        .setAuthor("А. Толстой" + i)
                        .setDescription("Большая книга о войне" + i)
                        .build();
                books.add(i, buildBook);
                bookProxies.add(i, new BookProxy(books.get(i)));
                bookProxies.get(i).getContent();
                int updatedStorageSize = bookProxies.get(i).getBookService().getBookStorage().getBooksStorage().size();
                assertEquals(1, updatedStorageSize);
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Runnable taskSecond = () -> {
            for (int i = 0; i < 2000; i++) {
                bookBuilders.add(i, new BookBuilder());
            }
            for (int i = 0; i < 2000; i++) {
                Book buildBook = bookBuilders.get(i)
                        .setTitle("Война и мир" + i)
                        .setAuthor("А. Толстой" + i)
                        .setDescription("Большая книга о войне" + i)
                        .build();
                books.add(i, buildBook);
                bookProxies.add(i, new BookProxy(books.get(i)));
                bookProxies.get(i).getContent();
                int updatedStorageSize = bookProxies.get(i).getBookService().getBookStorage().getBooksStorage().size();
                assertEquals(1, updatedStorageSize);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Thread thread = new Thread(task);
        Thread threadSecond = new Thread(taskSecond);
        thread.start();
        threadSecond.start();
        thread.join();
        thread.join();
    }
}
