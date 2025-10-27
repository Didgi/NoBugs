package practice10_tests_for_code.main_tasks;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import practice10_tests_for_code.BaseTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IsLeapYearTests extends BaseTest {
    /**
     * //Позитивные кейсы:
     * Високосные (2020, 2000, 1600) -> true
     * //Негативные кейсы:
     * Обычные годы (2025) -> false
     * Года, которые делятся на 100, но не на 400 (1900, 2100) -> false
     */

    @ParameterizedTest
    @ValueSource(ints = {
            //позитивные кейсы: високосные года
            2020,
            2000,
            1600
    })
    @DisplayName("Проверка, что метод возвращает true, если передаётся год, который делится без остатка " +
            "либо на 4, либо на 400")
    public void checkIfYearIsLeapTrueWhenYearIsDividedBy4Or400(int year) {
        boolean actualResult = mainTasks.isLeapYear(year);
        assertTrue(actualResult);

    }

    @ParameterizedTest
    @ValueSource(ints = {
            //негативные кейсы: обычные года или года, которые делятся на 100, но не на 400
            2025,
            1900,
            2100
    })
    @DisplayName("Проверка, что метод возвращает false, если передаётся год, который не делится без остатка " +
            "либо на 4, либо на 400")
    public void checkIfYearIsLeapFalseWhenYearIsNotDividedBy4Or400(int year) {
        boolean actualResult = mainTasks.isLeapYear(year);
        assertFalse(actualResult);

    }
}
