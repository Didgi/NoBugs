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

public class CountWordsTests extends BaseTest {
    /**
     * Позитивные кейсы:
     * "Привет, как твои дела?" -> 4
     * "Привет" -> 1
     * Корнер кейсы:
     * Пустую строку -> 0
     * null -> exception?
     */

    public static Stream<Arguments> positiveCases() {
        return Stream.of(
                //позитивные кейсы
                Arguments.of("Привет, как твои дела?", 4),
                Arguments.of("Привет", 1)
        );
    }

    @ParameterizedTest
    @MethodSource("positiveCases")
    @DisplayName("Проверка получения количества слов в предложении для предложений с пробелами и без")
    public void getCountWordsIsSuccessWhenSentenceHasWordsWithSeveralSpaces(String sentence, int countWords) {
        int actualResult = mainTasks.countWords(sentence);
        assertEquals(countWords, actualResult);
    }

    //Корнер кейс: пустая строка
    @Test
    @DisplayName("Проверка получения количества слов в пустом предложении")
    public void getCountWordsIsFailedWhenSentenceIsEmpty() {
        int actualResult = mainTasks.countWords("");
        assertEquals(0, actualResult);
    }

    //Корнер кейс: строка null
    @Test
    @DisplayName("Проверка, что метод выбрасывает исключение NullPointerException, если передано null вместо строки")
    public void getCountWordsThrowsExceptionWhenSentenceIsNull() {
        assertThrows(NullPointerException.class, () -> {
            mainTasks.countWords(null);
        });
    }
}
