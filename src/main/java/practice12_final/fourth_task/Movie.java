package practice12_final.fourth_task;

import java.util.Objects;

public class Movie {

    private String name;
    private String director;

    public Movie(String name, String director) {
        this.name = name;
        this.director = director;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;
        Movie movie = (Movie) o;
        return Objects.equals(name, movie.name) && Objects.equals(director, movie.director);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, director);
    }
}
