package practice9_multithreading;

public class FirstTask {
    public static void main(String[] args) {

        Thread firstTask = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("Привет из потока!");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        firstTask.start();

    }
}
