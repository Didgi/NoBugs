package practice6_collections.additionalTasks.second;

import java.util.ArrayDeque;
import java.util.Queue;

public class QueueTask {

    private ArrayDeque<String> queueTasks;

    public ArrayDeque<String> getQueueTasks() {
        System.out.println("Список задач на обработку " + queueTasks);
        return queueTasks;
    }

    public QueueTask() {
        queueTasks = new ArrayDeque<>();
    }

    public void addTask(String task) {
        queueTasks.offer(task);
        System.out.println("Добавлена задача " + task);
    }

    public void processTask() {
        System.out.println("Обработана задача " + queueTasks.poll());
    }

}
