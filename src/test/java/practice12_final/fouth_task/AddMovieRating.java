package practice12_final.fouth_task;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import practice12_final.fourth_task.InvalidRateException;
import practice12_final.fourth_task.Movie;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AddMovieRating extends BaseTest {
    /**
     * Позитивные:
     * Добавление валидного рейтинга Integer -> success
     * Добавление валидного рейтинга Double -> success
     * <p>
     * Негативные:
     * Добавление невалидного рейтинга < 1 (integer) -> exception
     * Добавление невалидного рейтинга > 10 (double) -> exception
     * <p>
     * Потокобезопасность. Добавление оценки в нескольких потоках -> success
     **/

    public static Stream<Arguments> diffValidRates() {
        return Stream.of(Arguments.of(1),
                Arguments.of(10.0));
    }

    //Позитивный тест
    @ParameterizedTest
    @MethodSource("diffValidRates")
    @DisplayName("Добавление валидной оценки по фильму от 1 до 10")
    public <T extends Number> void addValidRateToMovieIsSuccessWhenRateIsValid(T rate) throws InvalidRateException {
        Movie movie1 = new Movie("Бесславные Ублюдки", "Тарантино");
        movieService.addMovieRating(movie1, rate);
        movieService.getRatingMap().forEach((movie, ratings) -> {
            assertEquals(movie.getName(), movie1.getName());
            ratings.forEach(rating -> {
                assertEquals(rate, rating.getValue());
            });
        });
    }

    public static Stream<Arguments> diffInValidRates() {
        return Stream.of(Arguments.of(0),
                Arguments.of(10.1));
    }

    //Негативный тест
    @ParameterizedTest
    @MethodSource("diffInValidRates")
    @DisplayName("Добавление невалидной оценки по фильму меньше 1 или больше 10")
    public <T extends Number> void addInValidRateToMovieThrowExceptionWhenRateIsInValid(T rate) {
        Movie movie1 = new Movie("Бесславные Ублюдки", "Тарантино");
        assertThrows(InvalidRateException.class, () -> {
            movieService.addMovieRating(movie1, rate);
        });
    }

    //Потокобезопасность
    @RepeatedTest(10)
    @Test
    @DisplayName("Добавление валидной оценки по фильму в нескольких потоках")
    public void addValidRateToMovieInDiffThreadsIsSuccess() throws InterruptedException {
        AtomicInteger rateSummary = new AtomicInteger();
        AtomicInteger rateCount = new AtomicInteger();
        Movie movie1 = new Movie("Бесславные Ублюдки", "Тарантино");
        Runnable taskOne = () -> {
            for (int i = 0; i < 500; i++) {
                int rate = new Random().nextInt(9) + 1;
                rateSummary.addAndGet(rate);
                rateCount.incrementAndGet();
                try {
                    movieService.addMovieRating(movie1, rate);
                    Thread.sleep(5);
                } catch (InvalidRateException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Thread t1 = new Thread(taskOne);
        Thread t2 = new Thread(taskOne);

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        final double actualAvgRateByMovie = movieService.getAvgRateByMovie(movie1);
        double expectedAvgRateByMovie = (double) rateSummary.get() / rateCount.get();
        assertEquals(actualAvgRateByMovie, expectedAvgRateByMovie);
    }


}
