package practice12_final.fourth_task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MovieService {
    private Map<Movie, List<Rating<?>>> ratingMap;
    private MovieRating<?> movieRating;

    public MovieService() {
        this.ratingMap = new ConcurrentHashMap<>();
    }

    public synchronized <T extends Number> void addMovieRating(Movie movie, T rateValue) throws InvalidRateException {
        try {
            movieRating = new MovieRating<>(rateValue);
        } catch (InvalidRateException e) {
            throw new InvalidRateException("Числовое значение должно быть в диапазоне 1-10");
        }
        ratingMap.computeIfAbsent(movie, rate -> new ArrayList<>()).add(movieRating);
        System.out.println("Добавлен рейтинг по фильму: " + movie.getName() + " - " + movieRating.getValue());

    }

    public double getAvgRateByMovie(Movie movie){
        return ratingMap.entrySet().stream()
                .filter(entry -> entry.getKey().getName().equals(movie.getName()))
                .flatMap(entry -> entry.getValue().stream())
                .mapToDouble(rate -> rate.getValue().doubleValue()).average().orElse(0.0);
    }

    public List<Map.Entry<String, Double>> getSortedMovieList(){
      return ratingMap.entrySet().stream()
                .map(entry -> Map.entry(
                        entry.getKey().getName(),
                        entry.getValue().stream().mapToDouble(rate -> rate.getValue().doubleValue())
                                .average().orElse(0.0)))
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed()).collect(Collectors.toList());
    }

    public Map<Movie, List<Rating<?>>> getRatingMap() {
        return ratingMap;
    }
}
