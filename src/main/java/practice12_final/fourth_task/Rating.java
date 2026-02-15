package practice12_final.fourth_task;

public abstract class Rating<T extends Number> {

    protected T value;
    public Rating(T value) throws InvalidRateException {
        double valueDouble = value.doubleValue();
        if (valueDouble < 1 || valueDouble > 10){
            throw new InvalidRateException("Числовое значение должно быть в диапазоне 1-10");
        }
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}
