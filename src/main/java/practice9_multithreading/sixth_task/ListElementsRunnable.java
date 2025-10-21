package practice9_multithreading.sixth_task;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ListElementsRunnable implements Runnable {
    private final List<Integer> listElements;

    private final AtomicInteger sumElements = new AtomicInteger(0);
    private final AtomicInteger countProcessedElements = new AtomicInteger(0);

    public int getSumElements() {
        return sumElements.get();
    }

    public int getCountProcessedElements() {
        return countProcessedElements.get();
    }

    public ListElementsRunnable(List<Integer> listElements) {
        this.listElements = listElements;
    }

    @Override
    public void run() {
        for (int i = 0; i < listElements.size(); i++) {
            sumElements.getAndAdd(listElements.get(i));
            countProcessedElements.incrementAndGet();
            System.out.println("Поток: " + Thread.currentThread().getName());
        }

    }
}
