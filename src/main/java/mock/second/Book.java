package mock.second;

public abstract class Book {
    private String author;

    private String name;
    private int allPages;
    private int currentPage;
    public Book(String author, String name, int pages) {
        this.author = author;
        this.name = name;
        this.allPages = pages;
    }

    public String getAuthor() {
        return author;
    }

    public String getName() {
        return name;
    }

    public int getAllPages() {
        return allPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
