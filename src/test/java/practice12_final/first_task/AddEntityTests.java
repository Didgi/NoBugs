package practice12_final.first_task;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class AddEntityTests extends BaseTest {

    /**
     * Позитивные:
     * Добавление 1 элемента -> success
     * Добавление 2 одинаковых элементов > success
     * Негативные:
     * Пустой -> exception
     * Корнер:
     * null -> exception
     * Потокобезопасность:
     * Добавление элементов в 2х разных потоках -> success
     */

    //Позитивный тест: добавление сущности в коллекцию
    @Test
    @DisplayName("Проверка на то, что успешно добавляется 1 сущность в коллекцию")
    public void checkAddOneEntityIsSuccess() {

        Users user = new Users("Петька " + randomAge, randomAge);

        int expectedListSize = entityManager.getEntityList().size();
        assertEquals(0, expectedListSize);

        entityManager.addEntity(user);

        int actualListSize = entityManager.getEntityList().size();
        assertEquals(expectedListSize + 1, actualListSize);

        Users actualUserInList = entityManager.getEntityList().get(0);
        assertEquals(user, actualUserInList);

    }

    //Позитивный тест: добавление 2 одинаковые сущности в коллекцию
    @Test
    @DisplayName("Проверка на то, что успешно добавляются 2 одинаковые сущности в коллекцию")
    public void checkAddTwoSameEntitiesIsSuccess() {

        Users user = new Users("Редиска " + randomAge, randomAge, EntityStatus.ACTIVE);
        Users user2 = new Users("Редиска " + randomAge, randomAge, EntityStatus.ACTIVE);

        int expectedListSize = entityManager.getEntityList().size();
        assertEquals(0, expectedListSize);

        entityManager.addEntity(user);
        entityManager.addEntity(user2);

        int actualListSize = entityManager.getEntityList().size();
        assertEquals(expectedListSize + 2, actualListSize);

        List<Users> actualUserInList = entityManager.getEntityList();

        assertEquals(user, actualUserInList.get(actualUserInList.indexOf(user)));
        assertEquals(user2, actualUserInList.get(actualUserInList.indexOf(user2)));

        for (Users userInList : actualUserInList) {
            assertEquals(userInList, user);
            assertEquals(userInList, user2);
        }
    }

    //Негативный тест: добавление null сущности в коллекцию
    @Test
    @DisplayName("Проверка на то, что невозможно добавить null сущность в коллекцию")
    public void checkAddNullEntityThrowsException() {

        Users user = null;

        int expectedListSize = entityManager.getEntityList().size();
        assertEquals(0, expectedListSize);

        assertThrows(NoSuchElementException.class, () -> {
            entityManager.addEntity(user);
        });

        int actualListSize = entityManager.getEntityList().size();
        assertEquals(expectedListSize, actualListSize);

    }

    //Негативный тест: добавление пустой сущности в коллекцию
    @Test
    @DisplayName("Проверка на то, что запрещено добавлять сущность без имени в коллекцию")
    public void checkAddEmptyEntityThrowsException() {

        Users user = new Users("", randomAge);

        int expectedListSize = entityManager.getEntityList().size();
        assertEquals(0, expectedListSize);

        assertThrows(NoSuchElementException.class, () -> {
            entityManager.addEntity(user);
        });

        int actualListSize = entityManager.getEntityList().size();
        assertEquals(expectedListSize, actualListSize);

    }

    //Тест на потокобезопасность
    @RepeatedTest(10)
    @DisplayName("Проверка на то, что сущности успешно добавляются в коллекцию при многопоточных вызовах")
    public void checkAddEntitiesInDiffThreadsIsSuccess() throws InterruptedException {

        Runnable task = () -> {
            for (int i = 0; i < 500; i++) {
                Users user = new Users("Имя", i);
                entityManager.addEntity(user);
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

        assertEquals(1000, actualListSize);
    }
}
