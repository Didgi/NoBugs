package practice9_multithreading;

public class SecondTask {
    public static void main(String[] args) throws InterruptedException {
        Thread secondTask1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("A");
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException("Операция ожидания потока завершилась с ошибкой: " + e.getMessage());
                }
            }
        });

        Thread secondTask2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("B");
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException("Операция ожидания потока завершилась с ошибкой: " + e.getMessage());
                }
            }
        });
        secondTask1.start();
        secondTask2.start();

        secondTask1.join();
        secondTask2.join();
    }
}
