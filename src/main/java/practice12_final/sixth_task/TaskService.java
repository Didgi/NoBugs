package practice12_final.sixth_task;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class TaskService<T> {
    private List<Task<T>> taskList = new CopyOnWriteArrayList<>();

    public List<Task<T>> getTaskList() {
        return taskList;
    }

    public void addTask(Task<T> task) {
        if (!taskList.contains(task)) {
            taskList.add(task);
            System.out.println("Добавлена задача: " + task);

        } else {
            System.out.println("Такая задача уже добавлена");
        }
    }

    public synchronized void removeTask(T id) {
        if (id == null){
            System.out.println("Упc, передан невалидный id");
        }
        final Optional<Task<T>> taskOptional = taskList.stream().filter(task -> task.getID().equals(id)).findFirst();
        if (taskOptional.isPresent()) {
            taskList.remove(taskOptional.get());
            System.out.println("Удалена задача: " + taskOptional.get());
        } else {
            System.out.println("Задача с id: " + id + " отсутствует");
        }
    }

    public List<Task<T>> getTasksByStatus(StatusTask statusTask) {
        return taskList.stream().filter(task -> task.getStatusTask().equals(statusTask))
                .sorted((t1, t2) -> t2.getDate().compareTo(t1.getDate())).collect(Collectors.toList());
    }

    public List<Task<T>> getTasksByPriority(PriorityTask priorityTask) {
        return taskList.stream().filter(task -> task.getPriorityTask().equals(priorityTask))
                .sorted((t1, t2) -> t2.getDate().compareTo(t1.getDate())).collect(Collectors.toList());
    }

    public List<Task<T>> getSortedTasksByDate() {
        return taskList.stream().sorted((t1, t2) -> t2.getDate().compareTo(t1.getDate())).collect(Collectors.toList());
    }
}
