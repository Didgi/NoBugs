package practice7_exception_generics.additionalTasks;

import java.util.List;

public class NumberBox<T extends Number> {

    public double sumNums(T value1, T value2){
        return value1.doubleValue() + value2.doubleValue();
    }

    public <V extends Number> void sumNums2(V value1, V value2){
        System.out.println(value1.intValue() + value2.intValue());
    }

    public void addNumToList(List<? super Integer> list){
        list.add(100);
        list.add(105);
        list.add(1000);
        System.out.println(list);
    }

}
