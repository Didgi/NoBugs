package mock.second;

public class Library {

    private Book activeBook;

    public void readBook(Book book) {
        activeBook = book;
        System.out.println("Происходит чтение книги: " + activeBook.getName());
    }

    public void nextPage() {
        System.out.println("Смена страницы");
        final int readPages = activeBook.getCurrentPage();
        final int allPages = activeBook.getAllPages();
        if (readPages == allPages) {
            System.out.println("Книга уже прочитана");
        }
        if (readPages + 1 == allPages) {
            activeBook.setCurrentPage(readPages + 1);
            System.out.println("Книга прочитана");
        } else {
            activeBook.setCurrentPage(readPages + 1);
        }
    }

    public void toPage(int value) {
        System.out.println("Открытие определённой страницы: " + value);
        activeBook.setCurrentPage(value);
        final int allPages = activeBook.getAllPages();
        if (value > allPages) {
            System.out.println("Книга уже прочитана");
        }
    }

    public void printPage() {
        System.out.println("Взята книга: " + activeBook.getName() + " с прочитанными страницами: " + activeBook.getCurrentPage());
    }
}
