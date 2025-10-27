package practice10_tests_for_code.main_tasks;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import practice10_tests_for_code.BaseTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReverseTests extends BaseTest {

    /**
     * Обычные строки
     * Позитивные кейсы:
     * a -> a;
     * AbC123 -> 321CbA;
     * Корнер кейсы:
     * Пустую строку -> ""
     * null -> null
     */

    public static Stream<Arguments> positiveTests() {
        return Stream.of(
                //позитивные кейсы
                Arguments.of("a", "a"),
                Arguments.of("AbC123", "321CbA")
        );
    }

    @ParameterizedTest
    @MethodSource("positiveTests")
    @DisplayName("Проверка, что метод возвращает обратную строку, если передаётся корректная строка")
    public void getReverseStringIsSuccessWhenStringIsNotNullAndLengthIsNotEmpty(String originString, String reversedString) {
        String expectedResult = reversedString;
        String actualResult = mainTasks.reverse(originString);
        assertEquals(actualResult, expectedResult);
    }

    //Корнер кейс: пустая строка
    @Test
    @DisplayName("Проверка, что метод возвращает пустую строку, если передаётся пустая строка")
    public void getReverseStringIsSuccessWhenStringIsNotNullAndLengthIsEmpty() {
        String expectedResult = "";
        String actualResult = mainTasks.reverse("");
        assertEquals(actualResult, expectedResult);
    }

    //Корнер кейс: строка null
    @Test
    @DisplayName("Проверка, что метод возвращает null, если передаётся null вместо строки")
    public void getReverseStringIsSuccessWhenStringIsNull() {
        String expectedResult = null;
        String actualResult = mainTasks.reverse(null);
        assertEquals(actualResult, expectedResult);
    }
}
