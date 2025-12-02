package practice12_final.fouth_task;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import practice12_final.fourth_task.InvalidRateException;
import practice12_final.fourth_task.Movie;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetAvgRateByMovie extends BaseTest{

    /**
     * Позитивные:
     * Расчёт среднего значения рейтинга по существующему фильму -> success
     * <p>
     * Негативные:
     * Расчёт среднего значения рейтинга по отсутствующему фильму -> 0.0
     */

    //Позитивный тест
    @Test
    @DisplayName("Получение среднего значения рейтинга по существующему фильму")
    public void getAvgRateByMovieIsSuccessWhenMovieIsExist() throws InvalidRateException {
        Movie movie1 = new Movie("Бесславные Ублюдки", "Тарантино");
        movieService.addMovieRating(movie1, 10);
        movieService.addMovieRating(movie1, 5);
        double actualAvgRateByMovie = movieService.getAvgRateByMovie(movie1);
        double expectedAvgRageByMovie = 7.5;
        assertEquals(actualAvgRateByMovie, expectedAvgRageByMovie);
    }

    //Негативный тест
    @Test
    @DisplayName("Получение среднего значения рейтинга по несуществующему фильму")
    public void getAvgRateByMovieIsSuccessWhenMovieIsNotExist() {
        Movie movie1 = new Movie("Бесславные Ублюдки", "Тарантино");
        double actualAvgRateByMovie = movieService.getAvgRateByMovie(movie1);
        double expectedAvgRageByMovie = 0.0;
        assertEquals(actualAvgRateByMovie, expectedAvgRageByMovie);
    }
}
