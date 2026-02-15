package practice9_multithreading.fourth_task.fourth_task_executorservice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(() -> {
            System.out.println("Поток в первом вызове: " + Thread.currentThread().getName());
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        });
        executorService.execute(() -> {
            System.out.println("Поток в втором вызове: " + Thread.currentThread().getName());
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        });
        System.out.println("Основной поток: " + Thread.currentThread().getName());
        try {
            executorService.shutdown();
            executorService.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.out.println("Прерывание потока: " + e.getMessage());
            executorService.shutdownNow();
        }
        System.out.println("Значение счётчика: " + counter.getCount());
    }
}
