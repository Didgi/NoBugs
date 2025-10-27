package practice10_tests_for_code.additional_tasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import practice10_tests_for_code.BaseTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IsValidJsonTests extends BaseTest {
    /**
     * Позитивный кейс
     * "{"key":"value"}" → true
     * Негативные кейс:
     * "invalid json" → false
     * Корнер кейс
     * null → false
     */

    //Позитивный кейс
    @Test
    @DisplayName("Проверка, что JSON является валидным, когда на вход принимается строка с корректным JSON")
    public void checkIfValidJsonIsTrueWhenJsonIsCorrect() {
        String expectedJson = "{\"key\":\"value\"}";
        boolean actualResult = additionalTasks.isValidJson(expectedJson);
        assertTrue(actualResult);
    }

    //Негативный кейс
    @Test
    @DisplayName("Проверка, что JSON является не валидным, когда на вход принимается строка с не корректным JSON")
    public void checkIfValidJsonIsFalseWhenJsonIsNotCorrect() {
        String expectedJson = "invalid json";
        boolean actualResult = additionalTasks.isValidJson(expectedJson);
        assertFalse(actualResult);
    }

    //Корнер кейс
    @Test
    @DisplayName("Проверка, что JSON является не валидным, когда на вход принимается null строка")
    public void checkIfValidJsonIsFalseWhenJsonIsNull() {
        String expectedJson = null;
        boolean actualResult = additionalTasks.isValidJson(expectedJson);
        assertFalse(actualResult);
    }
}
