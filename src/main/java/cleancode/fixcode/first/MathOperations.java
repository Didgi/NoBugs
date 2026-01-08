package cleancode.fixcode.first;

import java.util.Arrays;

public class MathOperations {
    /**
     * 1. Нарушение DRY (Don't Repeat Yourself) – дублирование кода
     * Задача: Устраните дублирование кода, применив перегрузку методов или
     * использование массива аргументов.
     */

    //Массив аргументов
    public int add(int... numbers) {
        return Arrays.stream(numbers).sum();
    }
}
