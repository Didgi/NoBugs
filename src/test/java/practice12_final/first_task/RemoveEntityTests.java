package practice12_final.first_task;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class RemoveEntityTests extends BaseTest {
    /**
     * Позитивный тест:
     * Удаление 1 элемента из коллекции -> true
     * Удаление нескольких одинаковых элементов из коллекции -> true
     * <p>
     * Негативный тест:
     * Удаление несуществующего (пустое имя) элемента -> false
     * <p>
     * Корнер тест:
     * Удаление null элемента -> exception
     * <p>
     * Потокобезопаность
     * Удаление элементов в 2 потоках -> true
     */

    //Позитивный тест: удаление 1 элемента из коллекции
    @Test
    @DisplayName("Проверка на то, что можно удалить элемент из коллекции")
    public void checkRemoveOneEntityFromListIsSuccess() {

        Users user = new Users("Кот-Матрос", randomAge);

        int expectedListSize = entityManager.getEntityList().size();
        assertEquals(0, expectedListSize);

        entityManager.addEntity(user);

        int actualListSizeAfterAddition = entityManager.getEntityList().size();
        assertEquals(expectedListSize + 1, actualListSizeAfterAddition);

        assertTrue(entityManager.removeEntity(user));
        int actualListSizeAfterRemoving = entityManager.getEntityList().size();
        assertEquals(expectedListSize, actualListSizeAfterRemoving);
    }

    //Позитивный тест: удаление нескольких элементов из коллекции
    @Test
    @DisplayName("Проверка на то, что можно удалить несколько элементов из коллекции")
    public void checkRemoveSeveralEntityFromListIsSuccess() {

        Users user = new Users("Кот-Матрос", randomAge);
        Users user2 = new Users("Кот-Матрос", randomAge);

        int expectedListSize = entityManager.getEntityList().size();
        assertEquals(0, expectedListSize);

        entityManager.addEntity(user);
        entityManager.addEntity(user2);

        int actualListSizeAfterAddition = entityManager.getEntityList().size();
        assertEquals(expectedListSize + 2, actualListSizeAfterAddition);

        assertTrue(entityManager.removeEntity(user));
        int actualListSizeAfterFirstRemoving = entityManager.getEntityList().size();
        assertEquals(expectedListSize + 1, actualListSizeAfterFirstRemoving);

        assertTrue(entityManager.removeEntity(user));
        int actualListSizeAfterSecondRemoving = entityManager.getEntityList().size();
        assertEquals(expectedListSize, actualListSizeAfterSecondRemoving);
    }

    //Негативный тест: удаление несуществующего элемента из коллекции
    @Test
    @DisplayName("Проверка на то, что не происходит удаление элементов из коллекции если передать несуществующий элемент")
    public void checkRemoveNotExistEntityFromListIsSuccess() {

        Users user = new Users("Собакен", randomAge);
        Users user2 = new Users("", randomAge);

        int expectedListSize = entityManager.getEntityList().size();
        assertEquals(0, expectedListSize);

        entityManager.addEntity(user);

        int actualListSizeAfterAddition = entityManager.getEntityList().size();
        assertEquals(expectedListSize + 1, actualListSizeAfterAddition);

        assertFalse(entityManager.removeEntity(user2));
        int actualListSizeAfterRemoving = entityManager.getEntityList().size();
        assertEquals(expectedListSize + 1, actualListSizeAfterRemoving);
    }

    //Корнер тест: удаление null элемента из коллекции
    @Test
    @DisplayName("Проверка на то, что выбрасывается исключение при попытке удаления null")
    public void checkRemoveNullEntityFromListThrowsException() {

        int expectedListSize = entityManager.getEntityList().size();
        assertEquals(0, expectedListSize);

        assertThrows(NoSuchElementException.class, () -> {
            assertFalse(entityManager.removeEntity(null));
        });

        int actualListSizeAfterRemoving = entityManager.getEntityList().size();
        assertEquals(expectedListSize, actualListSizeAfterRemoving);
    }

    //Тест на потокобезопасность
    @RepeatedTest(10)
    @DisplayName("Проверка на то, что сущности успешно удаляются из коллекции при многопоточных вызовах")
    public void checkRemoveEntitiesInDiffThreadsIsSuccess() throws InterruptedException {

        Runnable task = () -> {
            for (int i = 0; i < 500; i++) {
                Users user = new Users("Имя", i);
                entityManager.addEntity(user);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                entityManager.removeEntity(user);
                try {
                    Thread.sleep(1);
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

        int actualListSize = entityManager.getEntityList().size();

        assertEquals(0, actualListSize);
    }
}
