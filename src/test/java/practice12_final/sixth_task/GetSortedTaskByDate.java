package practice12_final.sixth_task;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetSortedTaskByDate extends BaseTest<Integer>{
    /**
     * Позитивные:
     * Получение списка задач с сортировкой по дате -> success
     */

    //Позитивный тест
    @Test
    @DisplayName("Получение списка задач с сортировкой по дате")
    public void getSortedTasksByDateIsSuccess() {
        int id = new Random().nextInt(100) + 1;
        Task<Integer> taskFirst = new Task<>(id, PriorityTask.BLOCKER, StatusTask.TO_DO, "2025-12-01");
        Task<Integer> taskSecond = new Task<>(id + 1, PriorityTask.LOW, StatusTask.IN_PROGRESS, "2025-12-02");
        Task<Integer> taskThird = new Task<>(id + 2, PriorityTask.MIDDLE, StatusTask.DONE, "2025-11-30");
        taskService.addTask(taskFirst);
        taskService.addTask(taskSecond);
        taskService.addTask(taskThird);

        final int expectedSize = taskService.getTaskList().size();
        assertEquals(expectedSize, 3);

        final List<Task<Integer>> sortedTasksByDate = taskService.getSortedTasksByDate();

        final int expectedSizeUpdated = sortedTasksByDate.size();
        assertEquals(expectedSizeUpdated, 3);

        assertEquals(taskSecond.getDate(), sortedTasksByDate.get(0).getDate());
        assertEquals(taskFirst.getDate(), sortedTasksByDate.get(1).getDate());
        assertEquals(taskThird.getDate(), sortedTasksByDate.get(2).getDate());
    }
}
