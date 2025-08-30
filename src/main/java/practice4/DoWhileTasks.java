package practice4;

import java.util.Scanner;

public class DoWhileTasks {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        //Печать положительного числа;
//        printPositiveNumbers();
        //Проверка ввода пароля
//        checkPassword();
//        Печать чисел от 1 до 10
//        printNumberFrom1To10();
//        Завершение работы
//        exitProgram();
//        Подсчёт количество цифр в числе
//        System.out.println(countDigits());
    }

    public static void printPositiveNumbers() {
        int number;
        do {
            System.out.print("Введите число: ");
            number = scanner.nextInt();
        } while (number <= 0);
        System.out.println(number);
    }

    public static void checkPassword() {
        String userPassword;
        String expectedPassword = "hardCode";
        do {
            System.out.print("Введите пароль: ");
            userPassword = scanner.nextLine();
        } while (!userPassword.equals(expectedPassword));
        System.out.println("Пароль введён корректно");
    }

    public static void printNumberFrom1To10() {
        int i = 0;
        do {
            i++;
            System.out.println(i);
        } while (i < 10);
    }

    public static void exitProgram() {
        String userCommand;
        do {
            System.out.print("Введите команду: ");
            userCommand = scanner.nextLine();
        } while (!userCommand.equals("exit"));
        System.out.println("Завершение работы");
    }

    public static int countDigits() {
        int userNumber;
        int count = 0;

        System.out.print("Введите число: ");
        userNumber = scanner.nextInt();
        do {
            userNumber = userNumber / 10;
            count++;
        } while (userNumber != 0);
        return count;
    }

}
