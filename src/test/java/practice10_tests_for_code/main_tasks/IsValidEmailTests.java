package practice10_tests_for_code.main_tasks;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import practice10_tests_for_code.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

public class IsValidEmailTests extends BaseTest {
    /**
     * //Позитивные кейсы:
     * "t@e.ru" -> true
     * "test@example.com" -> true
     * //Негативные кейсы:
     * "@example.com" -> false
     * "bad@.com", -> false
     * "test@email.r" -> false
     * "no-at-symbol", -> false
     * //Корнер кейсы:
     * null -> false -> false
     * "" -> false -> false
     */

    @ParameterizedTest
    @ValueSource(strings = {
            //позитивные кейсы
            "t@e.ru",
            "test@example.com"
    })
    @DisplayName("Проверка, что метод возвращает true, если передаётся корректный email")
    public void checkIfValidEmailIsTrueWhenEmailIsCorrect(String email) {
        boolean actualResult = mainTasks.isValidEmail(email);
        assertTrue(actualResult);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            //негативные кейсы
            "@example.com",
            "bad@.com",
            "test@email.r",
            "no-at-symbol",
            //корнер кейсы
            ""
    })
    @DisplayName("Проверка, что метод возвращает false, если передаётся не корректный email")
    public void checkIfValidEmailIsFalseWhenEmailIsNotCorrect(String email) {
        boolean actualResult = mainTasks.isValidEmail(email);
        assertFalse(actualResult);
    }

    //Корнер кейс: null
    @Test
    @DisplayName("Проверка, что метод возвращает false, если передаётся null вместо email")
    public void checkIfValidEmailIsFalseWhenEmailIsNull() {
        boolean actualResult = mainTasks.isValidEmail(null);
        assertFalse(actualResult);
    }
}
