package practice10_tests_for_code.main_tasks;

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

public class FindMaxTests extends BaseTest {
    /**
     * Позитивные кейсы:
     * Обычный массив ([3, 5, 7, 2]) -> 7
     * Один элемент в массиве ([0]) -> 0
     * Негативные кейсы:
     * Обычный массив с отрицательными числами ([3, -5, 1, 2]) -> 3
     * Один отрицательный элемент в массиве ([-5]) -> -5
     * Корнер:
     * Пустой массив -> (должно выбрасываться исключение)
     */


    public static Stream<Arguments> arraysWithCorrectNumbers() {
        return Stream.of(
                //Позитивные кейсы с положительными числами
                Arguments.of(new int[]{3, 5, 7, 2}, 7),
                Arguments.of(new int[]{0}, 0),
                //Негативные кейсы с отрицательными числа
                Arguments.of(new int[]{3, -5, 1, 2}, 3),
                Arguments.of(new int[]{-5}, -5)
        );
    }

    @ParameterizedTest
    @MethodSource("arraysWithCorrectNumbers")
    @DisplayName("Проверка, что метод возвращает максимальное число в массиве, если массив содержит корректные числа")
    public void getFindMaxIntegerIsSuccessWhenArrayHasCorrectNumbers(int[] arrayNumbers, int maxValue) {
        int actualResult = mainTasks.findMax(arrayNumbers);
        assertEquals(actualResult, maxValue);
    }

    //корнер кейс: пустой массив
    @Test
    @DisplayName("Проверка, что метод выбрасывает исключение NoSuchElementException, если в массиве отсутствуют числа")
    public void getFindMaxIntegerThrowsExceptionWhenArrayIsEmpty() {
        int[] initialArray = new int[0];
        assertThrows(NoSuchElementException.class, () -> {
            mainTasks.findMax(initialArray);
        });
    }
}
