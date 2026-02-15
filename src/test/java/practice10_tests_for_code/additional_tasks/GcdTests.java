package practice10_tests_for_code.additional_tasks;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import practice10_tests_for_code.BaseTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GcdTests extends BaseTest {
    /**
     * Позитивный кейс:
     * 24, 36 → 12
     * 101, 103 → 1
     * Негативный кейс:
     * 0, 10 → 10
     * 15, 0 → 15
     */

    @ParameterizedTest
    @CsvFileSource(resources = "/18.csv", numLinesToSkip = 1)
    @DisplayName("Получение наибольшего общего делителя с различной комбинацией входных чисел")
    public void getGcdIsSuccessWhenNumbersAreDiff(int firstNumber, int secondNumber, int expectedGcd) {
        int actualResult = additionalTasks.gcd(firstNumber, secondNumber);
        assertEquals(expectedGcd, actualResult);
    }
}
