package practice12_final.first_task;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class GetEntitiesByStatusTests extends BaseTest {
    /**
     * Позитивный:
     * Поиск элемента по статусу ACTIVE -> true
     * Поиск элемента по статусу INACTIVE -> true
     * <p>
     * Негативный корнер:
     * Поиск элементов по null -> exception
     */
    private final Users user1 = new Users("Иван", 14);
    private final Users user2 = new Users("Василий", 15, EntityStatus.INACTIVE);
    private final Users user3 = new Users("Ивпатий", 55, EntityStatus.INACTIVE);
    private final Users user4 = new Users("Иван", 56, EntityStatus.ACTIVE);

    //Позитивный тест: поиск 2х элементов по статусу ACTIVE
    @Test
    @DisplayName("Поиск 2х элементов по статусу ACTIVE")
    public void checkFindElementsInListByStatusIsSuccessWhenStatusIsActive() {

        int expectedListSize = entityManager.getEntityList().size();
        assertEquals(0, expectedListSize);

        entityManager.addEntity(user1);
        entityManager.addEntity(user2);
        entityManager.addEntity(user3);
        entityManager.addEntity(user4);

        int actualListSize = entityManager.getEntityList().size();
        assertEquals(expectedListSize + 4, actualListSize);

        List<Users> filteredUsersByStatus = entityManager.getEntitiesByStatus(EntityStatus.ACTIVE);
        assertEquals(2, filteredUsersByStatus.size());
        assertTrue(filteredUsersByStatus.contains(user1));
        assertFalse(filteredUsersByStatus.contains(user2));
        assertFalse(filteredUsersByStatus.contains(user3));
        assertTrue(filteredUsersByStatus.contains(user4));
    }

    //Позитивный тест: поиск 2х элементов по статусу INACTIVE
    @Test
    @DisplayName("Поиск 2х элементов по статусу INACTIVE")
    public void checkFindElementsInListByStatusIsSuccessWhenStatusIsInActive() {

        int expectedListSize = entityManager.getEntityList().size();
        assertEquals(0, expectedListSize);

        entityManager.addEntity(user1);
        entityManager.addEntity(user2);
        entityManager.addEntity(user3);
        entityManager.addEntity(user4);

        int actualListSize = entityManager.getEntityList().size();
        assertEquals(expectedListSize + 4, actualListSize);

        List<Users> filteredUsersByStatus = entityManager.getEntitiesByStatus(EntityStatus.INACTIVE);
        assertEquals(2, filteredUsersByStatus.size());
        assertFalse(filteredUsersByStatus.contains(user1));
        assertTrue(filteredUsersByStatus.contains(user2));
        assertTrue(filteredUsersByStatus.contains(user3));
        assertFalse(filteredUsersByStatus.contains(user4));
    }

    //Негативный тест: поиск элементов по null
    @Test
    @DisplayName("Поиск элементов по null выбрасывает исключение")
    public void checkFindElementsInListByStatusThrowsExceptionWhenStatusIsNull() {

        int expectedListSize = entityManager.getEntityList().size();
        assertEquals(0, expectedListSize);

        entityManager.addEntity(user1);
        entityManager.addEntity(user2);
        entityManager.addEntity(user3);
        entityManager.addEntity(user4);

        int actualListSize = entityManager.getEntityList().size();
        assertEquals(expectedListSize + 4, actualListSize);

        assertThrows(NoSuchElementException.class, () -> {
            List<Users> filteredUsersByStatus = entityManager.getEntitiesByStatus(null);
            assertEquals(0, filteredUsersByStatus.size());
        });
    }
}
