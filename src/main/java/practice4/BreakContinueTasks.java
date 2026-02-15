package practice4;

import java.util.Scanner;

public class BreakContinueTasks {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
//        Сумма положительных чисел
//        printPositiveSum();
        //Вывод чисел от 1 до 20 не делящихся на 3
//        printNumbersFrom1To20ExceptDividedBy3();
//        Печать положительных чисел
//        printPositiveNumbers();
        //Ожидание выхода из программы
//        waitStop();
    }

    public static void printPositiveSum() {
        int number;
        int sum = 0;

        while (true) {
            System.out.print("Введите число: ");
            number = scanner.nextInt();
            if (number > 0) {
                sum += number;
            } else {
                break;
            }
        }
        System.out.println(sum);

    }

    public static void printNumbersFrom1To20ExceptDividedBy3() {
        for (int i = 1; i <= 20; i++) {
            if (i % 3 == 0) {
                continue;
            } else {
                System.out.println(i);
            }
        }
    }

    public static void printPositiveNumbers() {
        int number;

        do {
            System.out.print("Введите число (0 для завершения): ");
            number = scanner.nextInt();

            if (number < 0) {
                continue;
            } else if (number > 0) {
                System.out.println(number);
            }
        } while (number != 0);
    }

    public static void waitStop() {
        String inputCommand;
        String expectedCommand = "stop";
        while (true) {
            System.out.print("Введите команду: ");
            inputCommand = scanner.nextLine();
            if (inputCommand.equals(expectedCommand)) {
                System.out.println("Программа завершена");
                break;
            }
        }
    }
}
