package practice10_tests_for_code.main_tasks;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import practice10_tests_for_code.BaseTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CountVowelsTests extends BaseTest {
    /**
     * Тесты должны проверять:
     * Разные строки ("hello", "java", "AEIOU", "")
     * null (должно выбрасываться исключение)
     * Строки без гласных
     * //Позитивные кейсы:
     * "hello" -> 2
     * "java" -> 2
     * "AEIOU" -> 3
     * //Негативные кейсы:
     * "HwH" -> 0;
     * //Корнер кейсы:
     * null - IllegalArgumentException
     * "" -> 0;
     */

    public static Stream<Arguments> checkCountVowelsPositive() {
        return Stream.of(
                //позитивные кейсы
                Arguments.of("hello", 2),
                Arguments.of("java", 2),
                Arguments.of("AEIOU", 5)
        );
    }

    @ParameterizedTest
    @MethodSource("checkCountVowelsPositive")
    @DisplayName("Проверка, что метод возвращает правильное число гласных буквы в строке, если строка содержит такие буквы")
    public void getCountVowelsIsSuccessWhenStringContainsVowelLetter(String inputValue, int expectedCountVowels) {
        int actualCountVowels = mainTasks.countVowels(inputValue);
        assertEquals(actualCountVowels, expectedCountVowels);

    }

    public static Stream<Arguments> checkCountVowelsNegative() {
        return Stream.of(
                //негативные кейсы
                Arguments.of("HwH", 0),
                Arguments.of("", 0)
        );
    }

    @ParameterizedTest
    @MethodSource("checkCountVowelsNegative")
    @DisplayName("Проверка, что метод возвращает правильное число гласных буквы в строке, если строка не содержит такие буквы")
    public void getCountVowelsIsSuccessWhenStringDoesntContainVowelLetter(String inputValue, int expectedCountVowels) {
        int actualCountVowels = mainTasks.countVowels(inputValue);
        assertEquals(actualCountVowels, expectedCountVowels);

    }

    //корнер кейс: строка null
    @Test
    @DisplayName("Проверка, что метод выбрасывает исключение IllegalArgumentException, если передано null вместо строки")
    public void getCountVowelsThrowsExceptionWhenStringIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            mainTasks.countVowels(null);
        });
    }
}
