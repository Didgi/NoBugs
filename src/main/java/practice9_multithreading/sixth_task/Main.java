package practice9_multithreading.sixth_task;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        List<Integer> listExample = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        ListElementsExecutor listElementsExecutor = new ListElementsExecutor(listExample);
        listElementsExecutor.processList();
        listElementsExecutor.shutdown();
    }
}
