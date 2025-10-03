package practice7_exception_generics.additionalTasks;

public class ContainerClass<T> implements Container<T> {

    private T item;

    @Override
    public void addT(T item) {
        this.item = item;
    }

    @Override
    public T get() {
        return this.item;
    }
}
