package practice9_multithreading.fifthTask;

import java.util.concurrent.*;

public class FifthMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        long startTime = System.currentTimeMillis();
        Callable<?> task = (() -> {
            System.out.println("Выполнение простого теста");
            System.out.println("Поток в callable: " + Thread.currentThread().getName());
            return null;
        });
        Future<?> resultFuture = null;
        for (int i = 0; i < 10; i++) {
            System.out.println("Поток в main: " + Thread.currentThread().getName());
            resultFuture = executorService.submit(task);
            Thread.sleep(2000);

        }
        resultFuture.get();
        executorService.shutdown();
        long stopTime = System.currentTimeMillis();
        System.out.println("Программа выполнена успешно");
        System.out.println("Время выполнения: " + (stopTime - startTime));


    }
}
