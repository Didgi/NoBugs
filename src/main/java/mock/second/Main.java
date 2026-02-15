package mock.second;

public class Main {
    public static void main(String[] args) {
        Library library = new Library();
        SimpleBook bulgakov = new SimpleBook("Булгаков", "Мастер и Маргарита", 500);
        SimpleBook tolstoy = new SimpleBook("Толстой", "Война и мир", 2000);
        library.readBook(tolstoy);
        library.nextPage();
        library.printPage();
        library.readBook(bulgakov);
        library.toPage(499);
        library.printPage();
        library.nextPage();
    }
}
