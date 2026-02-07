package cleancode.complextasks.thirdtask;


import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class Book {
    private String title;
    private String author;
    private String description;

    public Book(String title, String author, String description) {
        this.title = title;
        this.author = author;
        this.description = description;
    }

    public Book(BookBuilder bookBuilder) {
        this.title = bookBuilder.getTitle();
        this.author = bookBuilder.getAuthor();
        this.description = bookBuilder.getDescription();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Book)) return false;
        Book book = (Book) object;
        return Objects.equals(title, book.title) && Objects.equals(author, book.author) && Objects.equals(description, book.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, description);
    }

    @Override
    public String toString() {
        return "Наименование='" + title + '\'' +
                ". Автор='" + author + '\'' +
                ". Описание='" + description;
    }
}
