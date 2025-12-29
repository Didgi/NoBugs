package cleancode.fixcode.first;

import java.util.Arrays;

public class MathOperations {
    /**
     * 1. Нарушение DRY (Don't Repeat Yourself) – дублирование кода
     * Задача: Устраните дублирование кода, применив перегрузку методов или
     * использование массива аргументов.
     */
    //Перегрузка
    public int add(int a, int b) {
        return a + b;
    }

    public int add(int a, int b, int c) {
        return a + b + c;
    }

    public int add(int a, int b, int c, int d) {
        return a + b + c + d;
    }

    //Массив аргументов
    public int add(int... numbers) {
        return Arrays.stream(numbers).sum();
    }
}
