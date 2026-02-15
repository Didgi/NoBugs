package practice10_tests_for_code.additional_tasks;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import practice10_tests_for_code.BaseTest;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FindAverageTests extends BaseTest {
    /**
     * Позитивный кейс:
     * [1, 2, 3, 4, 5] → 3.0
     * [10] → 10.0
     * Корнер кейс:
     * Пустой массив -> (должно выбрасываться исключение)
     */

    public static Stream<Arguments> positiveCases() {
        return Stream.of(
                Arguments.of(new int[]{1, 2, 3, 4, 5}, 3.0d),
                Arguments.of(new int[]{10}, 10.0d)
        );
    }

    @ParameterizedTest
    @MethodSource("positiveCases")
    @DisplayName("Проверка получения среднего значения, когда массив чисел корректен")
    public void getFindAverageIsSuccessWhenArrayIsCorrect(int[] initialArray, double avgValue) {
        double actualResult = additionalTasks.findAverage(initialArray);
        assertEquals(avgValue, actualResult);
    }

    @Test
    @DisplayName("Проверка, что выбрасывается исключение NoSuchElementException, когда массив чисел пустой")
    public void getFindAverageThrowsExceptionWhenArrayIsEmpty() {
        assertThrows(NoSuchElementException.class, () -> {
            additionalTasks.findAverage(new int[]{});
        });
    }
}
