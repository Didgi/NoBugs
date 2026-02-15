package practice12_final.first_task;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class GetEntitiesByNameTests extends BaseTest {
    /**
     * Позитивный:
     * Поиск 2х элементов совпадающих по имени -> true
     * <p>
     * Негативный:
     * Поиск элемента не совпадающего по имени -> false
     * <p>
     * Корнер:
     * Поиск элемента по пустой строке -> false
     * Поиск по null -> exception
     */

    private final Users user1 = new Users("Иван", 14);
    private final Users user2 = new Users("Василий", 15);
    private final Users user3 = new Users("Ивпатий", 55);
    private final Users user4 = new Users("Иван", 56);

    //Позитивный тест: поиск 2х элементов совпадающих по имени
    @Test
    @DisplayName("Поиск 2х элементов совпадающих по имени с разным регистром")
    public void checkFindElementsInListByNameIsSuccessWhenNameIsCorrect() {

        int expectedListSize = entityManager.getEntityList().size();
        assertEquals(0, expectedListSize);

        entityManager.addEntity(user1);
        entityManager.addEntity(user2);
        entityManager.addEntity(user3);
        entityManager.addEntity(user4);

        int actualListSize = entityManager.getEntityList().size();
        assertEquals(expectedListSize + 4, actualListSize);

        List<Users> filteredUsersByName = entityManager.getEntitiesByName("ИВАН");
        assertEquals(2, filteredUsersByName.size());
        assertTrue(filteredUsersByName.contains(user1));
        assertFalse(filteredUsersByName.contains(user2));
        assertFalse(filteredUsersByName.contains(user3));
        assertTrue(filteredUsersByName.contains(user4));
    }

    //Негативный тест: поиск элементов не совпадающих по имени
    @Test
    @DisplayName("Поиск элементов не совпадающих по имени")
    public void checkFindElementsInListByNameIsEmptyWhenListDoesntContainName() {

        int expectedListSize = entityManager.getEntityList().size();
        assertEquals(0, expectedListSize);

        entityManager.addEntity(user1);
        entityManager.addEntity(user4);

        int actualListSize = entityManager.getEntityList().size();
        assertEquals(expectedListSize + 2, actualListSize);

        List<Users> filteredUsersByName = entityManager.getEntitiesByName("Ива");
        assertEquals(0, filteredUsersByName.size());
    }

    //Корнер тест: поиск элементов с пустым именем
    @Test
    @DisplayName("Поиск элементов с пустым именем")
    public void checkFindElementsInListByNameIsEmptyWhenNameIsEmpty() {

        int expectedListSize = entityManager.getEntityList().size();
        assertEquals(0, expectedListSize);

        entityManager.addEntity(user1);
        entityManager.addEntity(user4);

        int actualListSize = entityManager.getEntityList().size();
        assertEquals(expectedListSize + 2, actualListSize);

        List<Users> filteredUsersByName = entityManager.getEntitiesByName("");
        assertEquals(0, filteredUsersByName.size());
    }

    //Корнер тест: поиск элементов по null вместо имени
    @Test
    @DisplayName("Поиск элементов с пустым именем выбрасывает исключение")
    public void checkFindElementsInListByNameThrowsExceptionWhenNameIsNull() {

        int expectedListSize = entityManager.getEntityList().size();
        assertEquals(0, expectedListSize);

        entityManager.addEntity(user1);
        entityManager.addEntity(user4);

        int actualListSize = entityManager.getEntityList().size();
        assertEquals(expectedListSize + 2, actualListSize);

        assertThrows(NoSuchElementException.class, () -> {
            List<Users> filteredUsersByName = entityManager.getEntitiesByName(null);
            assertEquals(0, filteredUsersByName.size());
        });
    }
}
