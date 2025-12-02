package practice12_final.sixth_task;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetTasksByPriority extends BaseTest<Integer>{
    /**
     * Позитивные:
     * Получение списка задач с существующим приоритетом -> success
     * Негативные:
     * Получение списка задач с отсутствующим приоритетом -> success
     *
     */

    //Позитивный тест
    @Test
    @DisplayName("Получение списка задач с существующим приоритетом")
    public void getTasksByPriorityIsSuccessWhenPriorityIsExist() {
        int id = new Random().nextInt(100) + 1;
        Task<Integer> taskFirst = new Task<>(id, PriorityTask.BLOCKER, StatusTask.TO_DO, "2025-01-12");
        Task<Integer> taskSecond = new Task<>(id + 1, PriorityTask.BLOCKER, StatusTask.IN_PROGRESS, "2025-11-30");
        taskService.addTask(taskFirst);
        taskService.addTask(taskSecond);

        final int expectedSize = taskService.getTaskList().size();
        assertEquals(expectedSize, 2);

        final List<Task<Integer>> tasksByPriority = taskService.getTasksByPriority(PriorityTask.BLOCKER);

        final int expectedSizeUpdated = tasksByPriority.size();
        assertEquals(expectedSizeUpdated, 2);

        assertTrue(tasksByPriority.contains(taskFirst));
        assertTrue(tasksByPriority.contains(taskSecond));
    }

    //Негативный тест
    @Test
    @DisplayName("Получение списка задач с отсутствующим приоритетом")
    public void getTasksByPriorityIsSuccessWhenPriorityIsNotExist() {
        int id = new Random().nextInt(100) + 1;
        Task<Integer> taskFirst = new Task<>(id, PriorityTask.BLOCKER, StatusTask.TO_DO, "2025-01-12");
        Task<Integer> taskSecond = new Task<>(id + 1, PriorityTask.BLOCKER, StatusTask.IN_PROGRESS, "2025-11-30");
        taskService.addTask(taskFirst);
        taskService.addTask(taskSecond);

        final int expectedSize = taskService.getTaskList().size();
        assertEquals(expectedSize, 2);

        final List<Task<Integer>> tasksByPriority = taskService.getTasksByPriority(PriorityTask.MIDDLE);

        final int expectedSizeUpdated = tasksByPriority.size();
        assertEquals(expectedSizeUpdated, 0);
    }
}
