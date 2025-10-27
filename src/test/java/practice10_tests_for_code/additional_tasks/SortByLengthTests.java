package practice10_tests_for_code.additional_tasks;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import practice10_tests_for_code.BaseTest;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SortByLengthTests extends BaseTest {
    /**
     * Позитивные кейсы:
     * [ "Java", "C", "Python" ] → [ "C", "Java", "Python" ]
     * Негативные кейсы:
     * Одинаковые длины [ "aa", "BB", "cc" ] -> [ "aa", "BB", "cc" ]
     * Пустой список
     * [] -> []
     */

    public static Stream<Arguments> sortByLength() {
        return Stream.of(
                //Позитивный кейс: список с элементами разной длины
                Arguments.of(List.of("Java", "C", "Python"), List.of("C", "Java", "Python")),
                //Негативнвый кейс: список с элементами одинаковой длины
                Arguments.of(List.of("cc", "BB", "aa"), List.of("cc", "BB", "aa")),
                //Корнер кейс: пустой список
                Arguments.of(List.of(), List.of())
        );
    }

    @ParameterizedTest
    @MethodSource("sortByLength")
    @DisplayName("Проверка получения отсортированного списка по длине слов")
    public void getSortByLengthIsSuccessWhenListHasValues(List<String> initialList, List<String> expectedSortList) {
        List<String> actualResult = additionalTasks.sortByLength(initialList);
        assertEquals(expectedSortList, actualResult);
    }
}
