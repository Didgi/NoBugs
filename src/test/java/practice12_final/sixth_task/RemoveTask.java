package practice12_final.sixth_task;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RemoveTask extends BaseTest<Integer> {
    /**
     * Позитивные:
     * Удаление существующей задачи из списка -> success
     * <p>
     * Негативные:
     * Удаление несуществующей задачи из списка -> success
     * <p>
     * Потокобезопасность: удаление задачи из списка в нескольких потоках;
     */

    //Позитивный тест
    @Test
    @DisplayName("Удаление существующей задачи из списка")
    public void removeTaskIsSuccessWhenIdIsExist() {
        int id = new Random().nextInt(100) + 1;
        Task<Integer> taskFirst = new Task<>(id, PriorityTask.BLOCKER, StatusTask.TO_DO, "2025-01-12");

        taskService.addTask(taskFirst);
        final int expectedSize = taskService.getTaskList().size();
        assertEquals(expectedSize, 1);

        taskService.removeTask(taskFirst.getID());

        final int expectedSizeUpdated = taskService.getTaskList().size();
        assertEquals(expectedSizeUpdated, 0);
    }

    //Негативный тест
    @Test
    @DisplayName("Удаление несуществующей задачи из списка")
    public void removeTaskIsFailedWhenIdIsNotExist() {
        int id = new Random().nextInt(100) + 1;
        Task<Integer> taskFirst = new Task<>(id, PriorityTask.BLOCKER, StatusTask.TO_DO, "2025-01-12");

        taskService.addTask(taskFirst);
        final int expectedSize = taskService.getTaskList().size();
        assertEquals(expectedSize, 1);

        taskService.removeTask(id + 1);

        final int expectedSizeUpdated = taskService.getTaskList().size();
        assertEquals(expectedSizeUpdated, 1);
    }

    //Потокобезопасность
    @RepeatedTest(10)
    @DisplayName("Удаление существующих задач из списка в нескольких потоках")
    public void removeTaskInDiffThreads() throws InterruptedException {
        Runnable task = () -> {
            for (int i = 1; i < 500; i++) {
                taskService.addTask(new Task<>(i, PriorityTask.BLOCKER, StatusTask.TO_DO, "2025-01-12"));
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                taskService.removeTask(i);
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        final int expectedSize = taskService.getTaskList().size();
        assertEquals(expectedSize, 0);
    }
}
