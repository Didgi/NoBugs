package practice10_tests_for_code.main_tasks;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import practice10_tests_for_code.BaseTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class FactorialTests extends BaseTest {
    /**
     * Позитивные кейсы:
     * 1! -> 1
     * 5! -> 120
     * 0! -> 1
     * Негативные кейсы:
     * -1! -> исключение
     */

    public static Stream<Arguments> positiveCases() {
        return Stream.of(
                //позитивные кейсы
                Arguments.of(1, 1),
                Arguments.of(5, 120),
                Arguments.of(0, 1)
        );
    }

    @ParameterizedTest
    @MethodSource("positiveCases")
    @DisplayName("Получение факториала числа, когда число больше или равно нулю")
    public void getFactorialNumberIsSuccessWhenNumberIsMoreOrEqualZero(int initialNumber, int expectedNumber) {
        int actualResult = mainTasks.factorial(initialNumber);
        assertEquals(actualResult, expectedNumber);
    }

    //негативный кейс: отрицательное значение
    @Test
    @DisplayName("Проверка, что метод выбрасывает исключение IllegalArgumentException, когда число меньше нуля")
    public void getFactorialNumberThrowsExceptionWhenNumberIsLessThanZero() {
        assertThrows(IllegalArgumentException.class, () -> {
            mainTasks.factorial(-1);
        }, "Negative numbers not allowed");
    }
}
