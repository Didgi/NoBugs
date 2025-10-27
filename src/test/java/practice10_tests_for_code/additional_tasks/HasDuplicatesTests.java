package practice10_tests_for_code.additional_tasks;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import practice10_tests_for_code.BaseTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HasDuplicatesTests extends BaseTest {
    /**
     * Позитивный кейс:
     * [1, 2, 3, 4, 5] → false
     * Негативный кейс:
     * [1, 2, 2, 3] → true
     * Корнер кейс:
     * Пустой массив → false
     */

    //Позитивный кейс
    @Test
    @DisplayName("Успешная проверка, что массив не содержит дубликаты, когда передаётся массив без дубликатов")
    public void getHasDuplicatesIsFalseWhenArrayDoesntHaveDuplicates(){
        int[] initialArray = {1,2,3,4,5};
        boolean actualResult = additionalTasks.hasDuplicates(initialArray);
        assertFalse(actualResult);
    }
    //Негативный кейс
    @Test
    @DisplayName("Успешная проверка, что массив содержит дубликаты, когда передаётся массив c дубликатами")
    public void getHasDuplicatesIsTrueWhenArrayHasDuplicates(){
        int[] initialArray = {1,2,2,4,5};
        boolean actualResult = additionalTasks.hasDuplicates(initialArray);
        assertTrue(actualResult);
    }
    //Корнер кейс
    @Test
    @DisplayName("Успешная проверка, что массив не содержит дубликаты, когда передаётся пустой массив")
    public void getHasDuplicatesIsFalseWhenArrayIsEmpty(){
        int[] initialArray = {};
        boolean actualResult = additionalTasks.hasDuplicates(initialArray);
        assertFalse(actualResult);
    }
}
