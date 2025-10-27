package practice10_tests_for_code.main_tasks;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import practice10_tests_for_code.BaseTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IsEvenTests extends BaseTest {
    /**
     * public boolean isEven(int number) {
     * return number % 2 == 0;
     * }
     * <p>
     * Тесты должны проверять:
     * Чётные и нечётные числа
     * Нулевое значение
     * Отрицательные числа
     * <p>
     * //Позитивные кейсы:
     * чётное положительное число
     * 2 -> true
     * 100 -> true
     * чётное отрицательное число
     * -2 -> true
     * -50 -> true
     * //Негативные кейсы:
     * не чётное положительное число
     * 1 -> false
     * 101 -> false
     * не чётное отрицательное число
     * -1 -> false
     * -1001 -> false
     * //Корнер кейс: нулевое число
     * 0 -> true
     */


    @ParameterizedTest
    @ValueSource(ints = {
            //чётное положительное число
            2,
            100,
            //чётное отрицательное число
            -2,
            -50,
            //нулевое значение
            0
    })
    @DisplayName("Проверка, что метод возвращает true, если передаётся чётное число")
    public void checkIfEvenIsTrueWhenValueIsEven(int value) {
        boolean actualResult = mainTasks.isEven(value);
        assertTrue(actualResult);
    }

    @ParameterizedTest
    @ValueSource(ints = {
            //не чётное положительное число
            1,
            101,
            // не чётное отрицательное число
            -1,
            -1001
    })
    @DisplayName("Проверка, что метод возвращает false, если передаётся не чётное число")
    public void checkIfEvenIsFalseWhenValueIsNotEven(int value) {
        boolean actualResult = mainTasks.isEven(value);
        assertFalse(actualResult);
    }


}
