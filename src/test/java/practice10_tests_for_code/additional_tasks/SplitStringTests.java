package practice10_tests_for_code.additional_tasks;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import practice10_tests_for_code.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SplitStringTests extends BaseTest {
    /**
     * Позитивный кейс:
     * "Java,Python,C++", "," → ["Java", "Python", "C++"]
     * Негативный кейс:
     * "word", "," → ["word"]
     * Корнер кейсы
     * "", "," → [""]
     * ",", "," → ["", ""]
     * "Java  String", "" → ["Java", "String"]
     */
    public final String delimeter = ",";

    //Позитивный кейс
    @Test
    @DisplayName("Успешная проверка разделения строки на массив при не пустом массиве содержащем делитель")
    public void getSplitStringIsSuccessWhenStringHasSeveralWordsWithDelimeter() {
        String initialString = "Java,Python,C++";
        String[] actualResult = additionalTasks.splitString(initialString, delimeter);
        String[] expectedResult = {"Java", "Python", "C++"};
        for (int i = 0; i < expectedResult.length - 1; i++) {
            for (int j = 0; j < actualResult.length - 1; j++) {
                assertEquals(expectedResult[i], actualResult[i]);
            }
        }
    }

    //Негативный кейс
    @Test
    @DisplayName("Успешная проверка разделения строки на массив при не пустом массиве без делителя")
    public void getSplitStringIsSuccessWhenStringHasSeveralWordsWithoutDelimeter() {
        String initialString = "Word";
        String[] actualResult = additionalTasks.splitString(initialString, delimeter);
        String[] expectedResult = {"Word"};
        for (String strExpected : expectedResult) {
            for (String strActual : actualResult) {
                assertEquals(strExpected, strActual);
            }

        }
    }

    //Корнер кейс: пустая строка без делителя
    @Test
    @DisplayName("Успешная проверка разделения строки на массив при пустой строке без делителя")
    public void getSplitStringIsSuccessWhenStringIsEmptyWithoutDelimeter() {
        String initialString = "";
        String[] actualResult = additionalTasks.splitString(initialString, delimeter);
        String[] expectedResult = {""};
        for (String strExpected : expectedResult) {
            for (String strActual : actualResult) {
                assertEquals(strExpected, strActual);
            }

        }
    }

    //Корнер кейс: пустая строка с делителем
    @Test
    @DisplayName("Успешная проверка разделения строки на массив при пустой строке с делителем")
    public void getSplitStringIsSuccessWhenStringIsEmptyWithDelimeter() {
        String initialString = ",";
        String[] actualResult = additionalTasks.splitString(initialString, delimeter);
        String[] expectedResult = {"", ""};
        for (String strExpected : expectedResult) {
            for (String strActual : actualResult) {
                assertEquals(strExpected, strActual);
            }

        }
    }

    //Корнер кейс: строка с делителем в виде пробела
    @Test
    @DisplayName("Успешная проверка разделения строки на массив при строке с двумя пробелами с делителем в виде пробела")
    public void getSplitStringIsSuccessWhenStringIsEmptyWithSpaceDelimeter() {
        String initialString = "Java  String";
        String[] actualResult = additionalTasks.splitString(initialString, " ");
        String[] expectedResult = {"Java", "", "String"};
        for (int i = 0; i < expectedResult.length - 1; i++) {
            for (int j = 0; j < actualResult.length - 1; j++) {
                assertEquals(expectedResult[i], actualResult[i]);
            }
        }
    }
}
