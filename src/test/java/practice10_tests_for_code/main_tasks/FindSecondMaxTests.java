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

public class FindSecondMaxTests extends BaseTest {
    /**
     * Позитивные кейсы:
     * Обычный массив ([3, 5, 7, 2]) -> 5
     * Обычный массив c отрицательными числами ([-5, 7, 2]) -> 2
     * Негативные кейсы: массив с одинаковыми числами и не только
     * ([2, 7, 7]) -> 2
     * Корнер:
     * Пустой массив -> NoSuchElementException
     * Все элементы массива являются дублями ([7, 7, 7]) -> NoSuchElementException
     * Один элемент в массиве ([0]) -> NoSuchElementException
     */


    public static Stream<Arguments> arraysWithCorrectNumbers() {
        return Stream.of(
                //Позитивные кейсы с положительными числами
                Arguments.of(new int[]{3, 5, 7, 2}, 5),
                Arguments.of(new int[]{-5, 7, 2}, 2)
        );
    }

    @ParameterizedTest
    @MethodSource("arraysWithCorrectNumbers")
    @DisplayName("Проверка, что метод возвращает второе максимальное число в массиве, " +
            "если массив содержит корректные числа")
    public void getFindSecondMaxIntegerIsSuccessWhenArrayHasCorrectNumbers(int[] arrayNumbers, int secondMaxValue) {
        int actualResult = mainTasks.findSecondMax(arrayNumbers);
        assertEquals(actualResult, secondMaxValue);
    }

    //Негативные кейсы: кейс с разными и дублирующимися числами
    @Test
    @DisplayName("Проверка, что метод возвращает второе максимальное число в массиве, " +
            "если массив содержит дублирующиеся числа")
    public void getFindSecondMaxIntegerIsSuccessWhenArrayHasDuplicateNumbersAndOther() {
        int[] initialArray = new int[]{3, 5, 2, 7, 7};
        int expectedResult = 5;
        int actualResult = mainTasks.findSecondMax(initialArray);
        assertEquals(expectedResult, actualResult);
    }

//    корнер кейс: один элемент в массиве
    @Test
    @DisplayName("Проверка, что метод выбрасывает исключение NoSuchElementException, если в массиве " +
            "присутствует 1 элемент")
    public void getFindSecondMaxIntegerThrowsExceptionWhenArrayHasOneNumber() {
        int[] initialArray = new int[]{0};
        assertThrows(NoSuchElementException.class, () -> {
            mainTasks.findSecondMax(initialArray);
        });
    }
    //    корнер кейс: все одинаковые элементы в массиве
    @Test
    @DisplayName("Проверка, что метод выбрасывает исключение NoSuchElementException, если в массиве " +
            "присутствуют только дублирующеся элементы")
    public void getFindSecondMaxIntegerThrowsExceptionWhenArrayHasOnlySeveralDuplicateNumbers() {
        int[] initialArray = new int[]{7, 7, 7};
        assertThrows(NoSuchElementException.class, () -> {
            mainTasks.findSecondMax(initialArray);
        });
    }
    //    корнер кейс: все одинаковые элементы в массиве
    @Test
    @DisplayName("Проверка, что метод выбрасывает исключение NoSuchElementException, если в массиве " +
            "присутствуют только дублирующеся элементы")
    public void getFindSecondMaxIntegerThrowsExceptionWhenArrayIsEmpty() {
        int[] initialArray = new int[]{};
        assertThrows(NoSuchElementException.class, () -> {
            mainTasks.findSecondMax(initialArray);
        });
    }
}
