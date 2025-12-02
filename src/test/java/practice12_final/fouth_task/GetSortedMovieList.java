package practice12_final.fouth_task;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import practice12_final.fourth_task.InvalidRateException;
import practice12_final.fourth_task.Movie;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetSortedMovieList extends BaseTest {

    /**
     * Позитивные:
     * Получение отсортированного списка фильмов по убыванию рейтинга -> success
     * <p>
     * Негативные:
     * Получение отсортированного списка по рейтингу при пустом списке -> empty
     */

    //Позитивный тест
    @Test
    @DisplayName("Получение отсортированного списка фильмов по убыванию рейтинга при не пустом списке")
    public void getSortedMovieListIsSuccessWhenListIsNotEmpty() throws InvalidRateException {
        Movie movie1 = new Movie("Бесславные Ублюдки", "Тарантино");
        Movie movie2 = new Movie("Джанго Освобождённый", "Тарантино");
        movieService.addMovieRating(movie1, 10);
        movieService.addMovieRating(movie1, 5);
        movieService.addMovieRating(movie2, 9);
        movieService.addMovieRating(movie2, 7);
        final List<Map.Entry<String, Double>> actualSortedMovieList = movieService.getSortedMovieList();
        final String actualFirstPosition = actualSortedMovieList.get(0).getKey();
        final Double actualAvgFirstPosition = actualSortedMovieList.get(0).getValue();
        final String actualSecondPosition = actualSortedMovieList.get(1).getKey();
        final Double actualAvgSecondPosition = actualSortedMovieList.get(1).getValue();
        double expectedAvgRageByMovie1 = 7.5;
        double expectedAvgRageByMovie2 = 8;
        assertEquals(actualFirstPosition, movie2.getName());
        assertEquals(actualAvgFirstPosition, expectedAvgRageByMovie2);
        assertEquals(actualSecondPosition, movie1.getName());
        assertEquals(actualAvgSecondPosition, expectedAvgRageByMovie1);
        assertTrue(actualAvgFirstPosition > actualAvgSecondPosition);
    }

    //Негативный тест
    @Test
    @DisplayName("Получение отсортированного списка фильмов по убыванию рейтинга при пустом списке")
    public void getSortedMovieListIsSuccessWhenListIsEmpty() {
        final List<Map.Entry<String, Double>> actualSortedMovieList = movieService.getSortedMovieList();
        assertTrue(actualSortedMovieList.isEmpty());
    }
}
