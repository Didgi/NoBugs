package practice9_multithreading.thirdTask;

public class ThirdTaskMain {
    public static void main(String[] args) throws InterruptedException {
        InfinityIncrement infinityIncrement = new InfinityIncrement();
        Thread t1 = new Thread(infinityIncrement);
        long startTime = System.currentTimeMillis();
        t1.start();
        Thread.sleep(2000);
        System.out.println("Поток выполнения 2: " + Thread.currentThread().getName());
        infinityIncrement.setStop(true);
        long stopTime = System.currentTimeMillis();
        System.out.println("Затраченное время: " + (stopTime - startTime));
        System.out.println("Значение счётчика: " + infinityIncrement.getCount());
        System.out.println("Значение флага stop: " + infinityIncrement.isStop());
    }


}
