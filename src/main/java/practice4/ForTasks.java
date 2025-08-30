package practice4;

import java.util.Scanner;

public class ForTasks {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Печать чисел от 1 до 100
        //        printNumberFrom1To100WhichDivideBy3();
        //Подсчёт суммы всех чисел от 1 до введённого
//        sumNumberFrom1ToInputNumber();
        //Таблица умножения для числа от до 1 до 10
//        multiplyTableForNumber();
        //проверка, что число простое
//        printIsNumberPrime();
        //печать чисел от 1 до 10 включительно
//        printNumberFrom1To10();
    }

    public static void printNumberFrom1To100WhichDivideBy3() {
        for (int i = 1; i < 100; i++) {
            if (i % 3 == 0) {
                System.out.println(i);
            }
        }
    }

    public static void sumNumberFrom1ToInputNumber() {
        int number;
        int sum = 0;

        System.out.print("Введите число: ");
        number = scanner.nextInt();
        if (number >= 1) {
            for (int i = 1; i < number; i++) {
                sum += i;
            }
            System.out.println(sum);
        } else {
            System.out.println("Введено некорректное число: ожидается любое от 1 и больше");
        }

    }

    public static void multiplyTableForNumber() {
        int number;
        System.out.print("Введите число: ");
        number = scanner.nextInt();

        for (int i = 1; i <= 10; i++) {
            System.out.println(number + " * " + i + " = " + number * i);
        }
    }

    public static void printIsNumberPrime() {
        int number;
        boolean isPrime = true;
        System.out.print("Введите число: ");
        number = scanner.nextInt();

        if (number > 1) {
            for (int i = 2; i < number - 1; i++) {
                if (number % i == 0) {
                    isPrime = false;
                    break;
                }
            }
            System.out.println(number + " число простое? " + isPrime);
        } else {
            System.out.println("Введено некорретное число: ожидается любое число больше 1");
        }
    }

    public static void printNumberFrom1To10() {
        for (int i = 1; i <= 10; i++) {
            System.out.print(i + " ");
        }
    }
}
