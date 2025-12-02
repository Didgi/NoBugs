package practice12_final.sixth_task;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetTasksByStatus extends BaseTest<Integer>{
    /**
     * Позитивные:
     * Получение списка задач с существующим статусом -> success
     * Негативные:
     * Получение списка задач с отсутствующим статусом -> success
     *
     */

    //Позитивный тест
    @Test
    @DisplayName("Получение списка задач с существующим статусом")
    public void getTasksByStatusIsSuccessWhenStatusIsExist() {
        int id = new Random().nextInt(100) + 1;
        Task<Integer> taskFirst = new Task<>(id, PriorityTask.BLOCKER, StatusTask.TO_DO, "2025-01-12");
        Task<Integer> taskSecond = new Task<>(id + 1, PriorityTask.MIDDLE, StatusTask.TO_DO, "2025-11-30");
        taskService.addTask(taskFirst);
        taskService.addTask(taskSecond);

        final int expectedSize = taskService.getTaskList().size();
        assertEquals(expectedSize, 2);

        final List<Task<Integer>> tasksByStatus = taskService.getTasksByStatus(StatusTask.TO_DO);

        final int expectedSizeUpdated = tasksByStatus.size();
        assertEquals(expectedSizeUpdated, 2);

        assertTrue(tasksByStatus.contains(taskFirst));
        assertTrue(tasksByStatus.contains(taskSecond));
    }

    //Негативный тест
    @Test
    @DisplayName("Получение списка задач с отсутствующим статусом")
    public void getTasksByStatusIsSuccessWhenStatusIsNotExist() {
        int id = new Random().nextInt(100) + 1;
        Task<Integer> taskFirst = new Task<>(id, PriorityTask.BLOCKER, StatusTask.TO_DO, "2025-01-12");
        Task<Integer> taskSecond = new Task<>(id + 1, PriorityTask.MIDDLE, StatusTask.TO_DO, "2025-11-30");
        taskService.addTask(taskFirst);
        taskService.addTask(taskSecond);

        final int expectedSize = taskService.getTaskList().size();
        assertEquals(expectedSize, 2);

        final List<Task<Integer>> tasksByStatus = taskService.getTasksByStatus(StatusTask.IN_PROGRESS);

        final int expectedSizeUpdated = tasksByStatus.size();
        assertEquals(expectedSizeUpdated, 0);
    }
}
