package practice_1;

import javax.sound.midi.Soundbank;

public class Main {

    public static void main(String[] args) {
        int add1 = MathOperations.add(5, 2);
        System.out.println("Сложение двух чисел: " + add1);

        int subtract1 = MathOperations.subtract(5, 10);
        System.out.println("Разность двух чисел: " + subtract1);

        int multiply1 = MathOperations.multiply(1, 3);
        System.out.println("Умножение двух чисел: " + multiply1);

        double divide1 = MathOperations.divide(10, 3);
        System.out.println("Деление двух чисел: " + divide1);

        int findMax1 = MathOperations.findMax(10, 2);
        System.out.println("Максимальное число: " + findMax1);

        int findMax2 = MathOperations.findMax(10, 20);
        System.out.println("Максимальное число: " + findMax2);

        int difference1 = MathOperations.difference(10, 20);
        System.out.println("Разность по модулю: " + difference1);

        int squareArea1 = MathOperations.squareArea(10);
        System.out.println("Площадь квадрата: " + squareArea1);

        int squarePerimeter1 = MathOperations.squarePerimeter(3);
        System.out.println("Периметр квадрата: " + squarePerimeter1);

        float convertSecondsToMinutes1 = MathOperations.convertSecondsToMinutes(60);
        System.out.println("Количество минут в секундах: " + convertSecondsToMinutes1);

        float convertSecondsToMinutes2 = MathOperations.convertSecondsToMinutes(1000);
        System.out.println("Количество минут в секундах: " + convertSecondsToMinutes2);

        double averageSpeed1 = MathOperations.averageSpeed(100, 1);
        System.out.println("Средняя скорость: " + averageSpeed1);

        double averageSpeed2 = MathOperations.averageSpeed(10, 24);
        System.out.println("Средняя скорость: " + averageSpeed2);

        double findHypotenuse1 = MathOperations.findHypotenuse(10, 53);
        System.out.println("Значение гипотенузы: " + findHypotenuse1);

        double circleCircumference1 = MathOperations.circleCircumference(123);
        System.out.println("Длина окружности: " + circleCircumference1);

        double circleCircumference2 = MathOperations.circleCircumference(1);
        System.out.println("Длина окружности: " + circleCircumference2);

        double total = 25;
        double part = 100;
        double calculatePercentage1 = MathOperations.calculatePercentage(total, part);
        System.out.println("Процент " + total + " от " + part + ": " + calculatePercentage1);

        double celsiusToFahrenheit1 = MathOperations.celsiusToFahrenheit(18);
        System.out.println("Градусы по цельсию в градусах по фаренгейту: " + celsiusToFahrenheit1);

        double fahrenheitToCelsius1 = MathOperations.fahrenheitToCelsius(273);
        System.out.println("Градусы по фаренгейту в градусы по цельсию: " + fahrenheitToCelsius1);
    }
}
