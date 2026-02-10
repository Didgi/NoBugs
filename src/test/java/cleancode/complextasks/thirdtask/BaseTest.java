package cleancode.complextasks.thirdtask;

import org.junit.jupiter.api.BeforeEach;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class BaseTest {
    BookBuilder bookBuilder;

    @BeforeEach
    public void setUp() throws NoSuchAlgorithmException {
        bookBuilder = new BookBuilder();
        final HashMap<String, Book> booksStorage = BookStorage.getBooksStorage();
        booksStorage.clear();
    }

}
