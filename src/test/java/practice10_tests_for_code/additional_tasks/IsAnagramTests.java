package practice10_tests_for_code.additional_tasks;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import practice10_tests_for_code.BaseTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IsAnagramTests extends BaseTest {
    /**
     * Позитивный кейс: "listen", "silent" → true
     * Негативный кейс: "java", "python" → false
     * Корнер кейс:
     * null → false
     * "" - > true
     */

    //Позитивный кейс
    @Test
    @DisplayName("Успешная проверка, что слова являются анаграмой друг друга, когда символы в каждой строке равны ")
    public void checkIfAnagramIsTrueWhenCharsInStringAreEquals() {
        String initialFirstString = "lisTen";
        String initialSecondString = "silent";
        boolean actualResult = additionalTasks.isAnagram(initialFirstString, initialSecondString);
        assertTrue(actualResult);
    }

    //Негативный кейс
    @Test
    @DisplayName("Не успешная проверка, что слова являются анаграмой друг друга, когда символы в строках различаются")
    public void checkIfAnagramIsFalseWhenCharsInStringAreNotEquals() {
        String initialFirstString = "java";
        String initialSecondString = "python";
        boolean actualResult = additionalTasks.isAnagram(initialFirstString, initialSecondString);
        assertFalse(actualResult);
    }

    //Корнер кейс: пустая строка
    @Test
    @DisplayName("Успешная проверка, что слова являются анаграмой друг друга, когда строки пустые")
    public void checkIfAnagramIsTrueWhenCharsInStringAreEmpty() {
        String initialFirstString = "";
        String initialSecondString = "";
        boolean actualResult = additionalTasks.isAnagram(initialFirstString, initialSecondString);
        assertTrue(actualResult);
    }

    //Корнер кейс: null строка
    @Test
    @DisplayName("Не успешная проверка, что слова являются анаграмой друг друга, когда строки null")
    public void checkIfAnagramIsFalseWhenCharsInStringAreNull() {
        String initialFirstString = null;
        String initialSecondString = null;
        boolean actualResult = additionalTasks.isAnagram(initialFirstString, initialSecondString);
        assertFalse(actualResult);
    }
}
