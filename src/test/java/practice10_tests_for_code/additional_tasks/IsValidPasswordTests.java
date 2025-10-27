package practice10_tests_for_code.additional_tasks;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import practice10_tests_for_code.BaseTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IsValidPasswordTests extends BaseTest {
    /**
     * Позитивный кейс:
     * "Passwor1" → true
     * Негативный кейс:
     * "Password" → false
     * "passwor" → false
     * Корнер кейс:
     * "" -> false
     * null → false
     */

    //Позитивные кейсы
    @Test
    @DisplayName("Проверка, что пароль является валидным, если его длина 8 и более символов и он состоит из " +
            "символов верхнего и нижнего регистров, а также включает в себя числа")
    public void checkIfValidPasswordIsTrueWhenPasswordIsCorrect() {
        String initialPassword = "Passwor1";
        assertTrue(additionalTasks.isValidPassword(initialPassword));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            //негативные кейс:
            "Password",
            "passwor",
            //корнер кейсы
            ""
    })
    @DisplayName("Проверка, что пароль является не валидным, если его длина менее 8 символов и он не содержит" +
            " символы верхнего и нижнего регистров, а также не включает в себя числа")
    public void checkIfValidPasswordIsFalseWhenPasswordIsNotCorrect(String password) {
        boolean actualResult = additionalTasks.isValidPassword(password);
        assertFalse(actualResult);
    }

    //Корнер кейс: пустой пароль
    @Test
    @DisplayName("Проверка, что пароль является валидным, если его длина 8 и более символов и он состоит из " +
            "символов верхнего и нижнего регистров, а также включает в себя числа")
    public void checkIfValidPasswordIsFalseWhenPasswordIsNull() {
        String initialPassword = null;
        assertFalse(additionalTasks.isValidPassword(initialPassword));
    }
}
