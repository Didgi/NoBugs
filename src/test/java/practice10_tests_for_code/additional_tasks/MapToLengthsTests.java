package practice10_tests_for_code.additional_tasks;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import practice10_tests_for_code.BaseTest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class MapToLengthsTests extends BaseTest {
    /**
     * Позитивный кейс:
     * [ "Java", "C++", "Go" ] → [ 4, 3, 2 ]
     * [ "A" ] → [ 1 ]
     * Негативный корнер кейс:
     * Пустой список -> 0
     */

    public static Stream<Arguments> diffLengthLists() {
        return Stream.of(
                //Позитивный кейсы
                Arguments.of(List.of("Java", "C++", "Go"), List.of(4, 3, 2)),
                Arguments.of(List.of("A"), List.of(1)),
                //Корнер кейс
                Arguments.of(List.of(""), List.of(0))
        );
    }

    @ParameterizedTest
    @MethodSource("diffLengthLists")
    @DisplayName("Проверка, что возвращается список состоящий из цифр по длине слов")
    public void getMapToLengthsIsSuccessWhenListIsCorrect(List<String> initialList, List<Integer> expectedList) {
        List<Integer> actualResult = additionalTasks.mapToLengths(initialList);
        assertEquals(expectedList, actualResult);
    }
}
