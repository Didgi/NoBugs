package practice7_exception_generics.additionalTasks;

public interface Container<T> {
    void addT(T item);

    T get();
}
