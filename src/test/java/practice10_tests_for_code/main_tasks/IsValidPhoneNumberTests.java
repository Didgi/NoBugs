package practice10_tests_for_code.main_tasks;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import practice10_tests_for_code.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

public class IsValidPhoneNumberTests extends BaseTest {
    /**
     * Позитивные кейсы:
     * Корректные номера ("+1 1234567890") -> true
     * Негативные кейсы:
     * Некорректные номера
     * "+1 987654321" -> false
     * "+1 98765432121" -> false
     * "12345" -> false
     * "invalid" -> false
     * Корнер кейсы:
     * Пустая строка -> false
     * null -> npe?
     */

    //Позитивные кейсы: корректный номер
    @Test
    @DisplayName("Проверка, что корректный номер является валидным")
    public void checkIfValidPhoneNumberIsTrueWhenPhoneNumberIsCorrect() {
        String initialPhoneNumber = "+1 1234567890";
        assertTrue(mainTasks.isValidPhoneNumber(initialPhoneNumber));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            //негативные кейсы:
            "+1 987654321",
            "+1 98765432121",
            "12345",
            "invalid",
            ""
    })
    @DisplayName("Проверка, что не верный номер является не валидным")
    public void checkIfValidPhoneNumberIsFalseWhenPhoneNumberIsNotCorrect(String phoneNumber) {
        boolean actualResult = mainTasks.isValidPhoneNumber(phoneNumber);
        assertFalse(actualResult);
    }

    //Корнер кейс: null строка
    @Test
    @DisplayName("Проверка, что выбрасывается исключение NullPointerException, когда вместо номера передано null")
    public void checkIfValidPhoneNumberIsFailedWhenPhoneNumberIsNull() {
        assertThrows(NullPointerException.class, () -> {
            String initialPhoneNumber = null;
            mainTasks.isValidPhoneNumber(initialPhoneNumber);
        });
    }


}
