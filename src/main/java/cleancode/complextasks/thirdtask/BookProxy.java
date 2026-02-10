package cleancode.complextasks.thirdtask;

import lombok.Getter;

@Getter
public class BookProxy implements Readable {

    private Book book;
    private BookService bookService;

    public BookProxy(Book book) {
        this.book = book;
        this.bookService = new BookService(book);
    }

    //добавил исключительно для инъекции spy bookService
    public BookProxy(Book book, BookService bookService) {
        this.book = book;
        this.bookService = bookService;
    }

    @Override
    public String getContent() {
        String foundBook = bookService.getContent();
        if (foundBook == null) {
            bookService.uploadBook(book);
            return "Книга добавлена в библиотеку: " + book.toString();
        }
        return "Книга получена из библиотеки: " + book.toString();
    }
}
