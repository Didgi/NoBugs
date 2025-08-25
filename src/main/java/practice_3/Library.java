package practice_3;

public class Library {

    private String bookTitle;
    protected String author;
    int year;
    public String category;

    String getBookTitle(){
        return this.bookTitle;
    }

    void setBookTitle(String newTitle){
        this.bookTitle = newTitle;
    }

    String getAuthor(){
        return this.author;
    }

    void setAuthor(String newAuthor){
        this.author = newAuthor;
    }

    int getYear(){
        return this.year;
    }

    void setYear(int newYear){
        this.year = newYear;
    }

    String getCategory(){
        return this.category;
    }

    void setCategory(String newCategory){
        this.category = newCategory;
    }
}
