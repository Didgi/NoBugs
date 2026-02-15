package practice12_final.sixth_task;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeParseException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class AddTask extends BaseTest<Integer> {
    /**
     * Позитивные:
     * Добавление двух различных задач с разными приоритетами и статусами -> success
     * <p>
     * Негативные:
     * Добавление двух одинаковых задач -> failed
     */

    //Позитивный тест
    @Test
    @DisplayName("Добавление двух различных задач с разными приоритетами и статусами")
    public void addTwoDiffTasksIsSuccessWhenTasksIfValid() {
        int id = new Random().nextInt(100) + 1;
        Task<Integer> taskFirst = new Task<>(id, PriorityTask.BLOCKER, StatusTask.TO_DO, "2025-01-12");
        Task<Integer> taskSecond = new Task<>(id + 1, PriorityTask.MIDDLE, StatusTask.IN_PROGRESS, "2025-11-30");

        final int expectedSize = taskService.getTaskList().size();
        assertEquals(expectedSize, 0);

        taskService.addTask(taskFirst);
        taskService.addTask(taskSecond);

        final int expectedSizeUpdated = taskService.getTaskList().size();
        assertEquals(expectedSizeUpdated, 2);

        assertTrue(taskService.getTaskList().contains(taskFirst));
        assertTrue(taskService.getTaskList().contains(taskSecond));

    }

    //Негативный тест
    @Test
    @DisplayName("Добавление двух одинаковых задач")
    public void addTwoEqualTasksIsFailedWhenTasksIsValid() {
        int id = new Random().nextInt(100) + 1;
        Task<Integer> taskFirst = new Task<>(id, PriorityTask.BLOCKER, StatusTask.TO_DO, "2025-01-12");

        final int expectedSize = taskService.getTaskList().size();
        assertEquals(expectedSize, 0);

        taskService.addTask(taskFirst);
        taskService.addTask(taskFirst);

        final int expectedSizeUpdated = taskService.getTaskList().size();
        assertEquals(expectedSizeUpdated, 1);

        assertTrue(taskService.getTaskList().contains(taskFirst));
    }
}
