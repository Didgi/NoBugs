package cleancode.complextasks.thirdtask;

import lombok.Getter;

import java.util.HashMap;

@Getter
public class BookStorage {
    private static HashMap<String, Book> booksStorage = new HashMap<>();

    public synchronized void addBook(Book book) {
        booksStorage.put(book.getTitle().toLowerCase(), book);
    }

    public synchronized Book getBook(Book book) {
        return booksStorage.get(book.getTitle().toLowerCase());
    }

    public static HashMap<String, Book> getBooksStorage() {
        return booksStorage;
    }
}
