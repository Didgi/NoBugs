package practice9_multithreading.sixth_task;

import java.util.List;
import java.util.concurrent.*;

public class ListElementsExecutor {
    private final List<Integer> listValue;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(3);

    public ListElementsExecutor(List<Integer> list) {
        this.listValue = list;
    }

    public void processList() throws ExecutionException, InterruptedException {
        List<Integer> firstList = listValue.subList(0, 3);
        List<Integer> secondList = listValue.subList(3, 6);
        List<Integer> thirdList = listValue.subList(6, 9);

        ListElementsRunnable first = new ListElementsRunnable(firstList);
        ListElementsRunnable second = new ListElementsRunnable(secondList);
        ListElementsRunnable third = new ListElementsRunnable(thirdList);

        Future<?> firstTask = executorService.submit(first);
        Future<?> secondTask = executorService.submit(second);
        Future<?> thirdTask = executorService.submit(third);

        firstTask.get();
        secondTask.get();
        thirdTask.get();

        int totalProcessedElementsInList = first.getCountProcessedElements() + second.getCountProcessedElements() + third.getCountProcessedElements();
        int sumElementsInList = first.getSumElements() + second.getSumElements() + third.getSumElements();
        System.out.println("Количество обработанных элементов в списке: " + totalProcessedElementsInList);
        System.out.println("Сумма числе по обработанным элементам списка: " + sumElementsInList);
    }

    public void shutdown() {
        executorService.shutdown();
        try {
            executorService.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
