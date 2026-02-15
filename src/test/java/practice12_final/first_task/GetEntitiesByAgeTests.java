package practice12_final.first_task;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GetEntitiesByAgeTests extends BaseTest {
    /**
     * Позитивный:
     * Поиск 2х элементов входящих в диапазон -> true
     * <p>
     * Негативный:
     * Поиск элементов не входящих в диапазон -> true
     */
    private final Users user1 = new Users("Иван", 14);
    private final Users user2 = new Users("Василий", 15);
    private final Users user3 = new Users("Ивпатий", 55);
    private final Users user4 = new Users("Иван", 56);

    //Позитивный тест: поиск 2х элементов входящих в диапазон
    @Test
    @DisplayName("Поиск 2х элементов входящих в диапазон по возрасту")
    public void checkFindElementsInListByAgeIsSuccessWhenAgeIsCorrect() {

        int expectedListSize = entityManager.getEntityList().size();
        assertEquals(0, expectedListSize);

        entityManager.addEntity(user1);
        entityManager.addEntity(user2);
        entityManager.addEntity(user3);
        entityManager.addEntity(user4);

        int actualListSize = entityManager.getEntityList().size();
        assertEquals(expectedListSize + 4, actualListSize);

        List<Users> filteredUsersByAge = entityManager.getEntitiesByAge(15, 55);
        assertEquals(2, filteredUsersByAge.size());
        assertFalse(filteredUsersByAge.contains(user1));
        assertTrue(filteredUsersByAge.contains(user2));
        assertTrue(filteredUsersByAge.contains(user3));
        assertFalse(filteredUsersByAge.contains(user4));
    }

    //Негативный тест: поиск элементов не входящих в диапазон
    @Test
    @DisplayName("Поиск элементов не входящих в диапазон по возрасту")
    public void checkFindElementsInListByAgeIsEmptyWhenAgeIsUnCorrect() {

        int expectedListSize = entityManager.getEntityList().size();
        assertEquals(0, expectedListSize);

        entityManager.addEntity(user1);
        entityManager.addEntity(user4);

        int actualListSize = entityManager.getEntityList().size();
        assertEquals(expectedListSize + 2, actualListSize);

        List<Users> filteredUsersByAge = entityManager.getEntitiesByAge(15, 55);
        assertEquals(0, filteredUsersByAge.size());
    }
}
