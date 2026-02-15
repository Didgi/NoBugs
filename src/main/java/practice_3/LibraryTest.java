package practice_3;

public class LibraryTest {

    public static void main(String[] args) {
    Library library1 = new Library();
        System.out.println(library1.getBookTitle()); //напрямую не обратиться, т.к. модификатор private
        library1.setBookTitle("Новое имя книги через сеттер");
        System.out.println(library1.author);
        System.out.println(library1.getAuthor());
        library1.author = "Новый автор напрямую";
        library1.setAuthor("Новый автор через сеттер");
        System.out.println(library1.year);
        System.out.println(library1.getYear());
        library1.year = 2025;
        library1.setYear(2026);
        System.out.println(library1.category);
        System.out.println(library1.getCategory());
        library1.category = "Новая категория напрямую";
        library1.setCategory("Новая категория через сеттер");

    }
}
