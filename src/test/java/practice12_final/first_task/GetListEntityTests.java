package practice12_final.first_task;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GetListEntityTests extends BaseTest {
    /**
     * Негативный тест:
     * изменение исходной коллекции -> exception
     */

    @Test
    @DisplayName("Проверка на то, что выбрасывается исключение при попытке изменения исходной коллекции")
    public void checkAddEntityInOriginalCollectionThrowsException() {

        Users user = new Users("Тест1", randomAge);

        int expectedListSize = entityManager.getEntityList().size();
        assertEquals(0, expectedListSize);

        assertThrows(UnsupportedOperationException.class, () -> {
            entityManager.getEntityList().add(user);
        });

        int actualListSize = entityManager.getEntityList().size();
        assertEquals(expectedListSize, actualListSize);

    }
}
