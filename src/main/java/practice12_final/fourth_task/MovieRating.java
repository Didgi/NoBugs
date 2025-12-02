package practice12_final.fourth_task;

public class MovieRating<T extends Number> extends Rating<T>{
    public MovieRating(T value) throws InvalidRateException {
        super(value);
    }
}
