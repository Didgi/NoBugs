package practice9_multithreading.fourth_task.fourth_task_executorservice;

public class Counter {
    private int count = 0;

    public int getCount() {
        return count;
    }

    public synchronized void increment(){
        this.count++;
    }
}
