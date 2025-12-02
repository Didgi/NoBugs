package practice12_final.sixth_task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class Task<T> {
    private T ID;
    private PriorityTask priorityTask;
    private StatusTask statusTask;
    private LocalDate date;

    public Task(T ID, PriorityTask priorityTask, StatusTask statusTask, String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.ID = ID;
        this.priorityTask = priorityTask;
        this.statusTask = statusTask;
        this.date = LocalDate.parse(date, formatter);
    }

    public T getID() {
        return ID;
    }

    public PriorityTask getPriorityTask() {
        return priorityTask;
    }

    public StatusTask getStatusTask() {
        return statusTask;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task<?> task = (Task<?>) o;
        return Objects.equals(ID, task.ID) && priorityTask == task.priorityTask && statusTask == task.statusTask && Objects.equals(date, task.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, priorityTask, statusTask, date);
    }

    @Override
    public String toString() {
        return "Task{" +
                "ID=" + ID +
                ", priorityTask=" + priorityTask +
                ", statusTask=" + statusTask +
                ", date=" + date +
                '}';
    }
}
