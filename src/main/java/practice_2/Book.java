package practice_2;

public class Book {

    String title;
    String author;

    Book(String title, String author){
        this.title = title;
        this.author = author;
    }

    public String getTitle(){
        return this.title;
    }

    public String getAuthor(){
        return this.author;
    }

    public void setTitle(String newTitle){
        this.title = newTitle;
    }

    public void setAuthor(String newAuthor){
        this.author = newAuthor;
    }

    public void printInfo(){
        System.out.println("Книга " + this.title + " от автора " + this.author);
    }
}
