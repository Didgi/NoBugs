package practice10_tests_for_code.additional_tasks;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import practice10_tests_for_code.BaseTest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilterEvenNumberTests extends BaseTest {
    /**
     * Позитивный кейс:
     * Обычный список ([1, 2, 3, 4, 5, 6] → [2, 4, 6])
     * Негативный кейс:
     * Список без чётных чисел ([1,3,5]) -> []
     * Корнер кейс:
     * Пустой список -> []
     * null -> NPE
     */

    //Позитивный кейс
    @Test
    @DisplayName("Проверка, что получен список с  чётными числами из списка включающего чётные и не чётные числа")
    public void getFilterEvenNumberIsSuccessWhenListContainsEvenOddNumbers() {
        List<Integer> intialList = Arrays.asList(1, 2, 3, 4, 5, 6);
        List<Integer> expectedList = Arrays.asList(2, 4, 6);
        assertEquals(expectedList, additionalTasks.filterEvenNumbers(intialList));
    }

    public static Stream<Arguments> negativeCases() {
        return Stream.of(
                //Негативный кейс: список без чётных чисел
                Arguments.of(Arrays.asList(1, 3, 5), List.of()),
                //Негативный кейс: пустой список
                Arguments.of(List.of(), List.of())
        );
    }

    @ParameterizedTest
    @MethodSource("negativeCases")
    @DisplayName("Проверка, что получен пустой список с чётными числами из списка включающего только не чётные числа")
    public void getFilterEvenNumberIsNotSuccessWhenListContainsOnlyOddNumbersOrEmpty(List<Integer> initialList, List<Integer> expectedList) {
        List<Integer> actualResult = additionalTasks.filterEvenNumbers(initialList);
        assertEquals(expectedList, actualResult);
    }

    //Корнер кейс: null строка
    @Test
    @DisplayName("Проверка, что выбрасывается исключение NullPointerException при попытке получить список " +
            "чётных чисел из отсутствующего списка")
    public void getFilterEvenNumberIsFailedWhenListIsNull() {
        assertThrows(NullPointerException.class, () -> {
            additionalTasks.filterEvenNumbers(null);
        });
    }
}
