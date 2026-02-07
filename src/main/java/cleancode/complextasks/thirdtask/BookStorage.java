package cleancode.complextasks.thirdtask;

import lombok.Getter;

import java.util.HashMap;

@Getter
public class BookStorage {
    private HashMap<String, Book> booksStorage;

    public BookStorage() {
        this.booksStorage = new HashMap<>();
    }

    public synchronized void addBook(Book book) {
        booksStorage.put(book.getTitle().toLowerCase(), book);
    }

    public Book getBook(Book book) {
        return booksStorage.get(book.getTitle().toLowerCase());
    }
}
