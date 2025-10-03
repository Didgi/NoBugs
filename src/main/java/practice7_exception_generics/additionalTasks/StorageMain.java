package practice7_exception_generics.additionalTasks;

import java.util.Arrays;

public class StorageMain {

    public static void main(String[] args) {
        //firstTask
        Storage<Integer> storageInt = new Storage<>(123);
        System.out.println(storageInt.getValue());

        Storage<String> storageStr = new Storage<>("Example");
        System.out.println(storageStr.getValue());
        //secondTask
        storageStr.printList(Arrays.asList("First", "Second"));
        storageStr.printList(Arrays.asList(1, 2));
    }

}
