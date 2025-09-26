package practice6_collections.additionalTasks.second;

public class Main {
    public static void main(String[] args) {
        QueueTask queueTask = new QueueTask();
        queueTask.getQueueTasks();
        queueTask.addTask("First task");
        queueTask.addTask("Second task");
        queueTask.addTask("Third task");
        queueTask.getQueueTasks();
        queueTask.processTask();
        queueTask.processTask();
        queueTask.processTask();
    }
}
