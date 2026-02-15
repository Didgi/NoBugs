package practice7_exception_generics.additionalTasks;

import java.util.Arrays;
import java.util.List;

public class ContainerClassMain {
    public static void main(String[] args) {
        ContainerClass<List<String>> cc = new ContainerClass<>();
        cc.addT(Arrays.asList("123","456"));
        System.out.println(cc.get());
    }
}
