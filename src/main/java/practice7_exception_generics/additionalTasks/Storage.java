package practice7_exception_generics.additionalTasks;

import java.util.List;

public class Storage<T> {

    private T value;

    public Storage(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public <V> void printList(List<V> list) {
        for (V v : list
        ) {
            System.out.println(v);
        }
    }
}
