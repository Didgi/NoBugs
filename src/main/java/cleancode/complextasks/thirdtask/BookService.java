package cleancode.complextasks.thirdtask;

import lombok.Getter;

@Getter
public class BookService implements Readable, Uploadable {

    private Book book;
    private BookStorage bookStorage;

    public BookService(Book book) {
        this.book = book;
        this.bookStorage = new BookStorage();
    }

    @Override
    public String getContent() {
        Book foundBook = bookStorage.getBook(book);
        if (foundBook == null) {
            return null;
        }
        return book.toString();
    }

    @Override
    public synchronized void uploadBook(Book book) {
        bookStorage.addBook(book);
    }
}
