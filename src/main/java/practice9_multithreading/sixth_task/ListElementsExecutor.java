package practice9_multithreading.sixth_task;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ListElementsExecutor {
    private final List<Integer> listValue;

    public ListElementsExecutor(List<Integer> list) {
        this.listValue = list;
    }

    public void processList() {
        List<Integer> firstList = listValue.subList(0, 3);
        List<Integer> secondList = listValue.subList(3, 6);
        List<Integer> thirdList = listValue.subList(6, 9);

        ListElementsRunnable first = new ListElementsRunnable(firstList);
        ListElementsRunnable second = new ListElementsRunnable(secondList);
        ListElementsRunnable third = new ListElementsRunnable(thirdList);

        ExecutorService executorService = Executors.newFixedThreadPool(3);

        executorService.execute(first);
        executorService.execute(second);
        executorService.execute(third);

        try {
            executorService.shutdown();
            executorService.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
        int totalProcessedElementsInList = first.getCountProcessedElements() + second.getCountProcessedElements() + third.getCountProcessedElements();
        int sumElementsInList = first.getSumElements() + second.getSumElements() + third.getSumElements();
        System.out.println("Количество обработанных элементов в списке: " + totalProcessedElementsInList);
        System.out.println("Сумма числе по обработанным элементам списка: " + sumElementsInList);
    }
}
