package practice9_multithreading.thirdTask;

public class InfinityIncrement implements Runnable {
    private int count = 0;
    private volatile boolean stop = false;

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    @Override
    public void run() {
            System.out.println("Поток выполнения: " + Thread.currentThread().getName());
        while (!this.stop) {
            this.count++;
        }
    }
}
