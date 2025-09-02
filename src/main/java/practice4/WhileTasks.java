package practice4;

import java.util.Scanner;

public class WhileTasks {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
//        Печать факториала числа
//        printFactorial();
//        printFactorialRemake();
//        Печать чётных чисел
        printParityNumbers();
        printParityNumbersRemakeOne();
        printParityNumbersRemakeTwo();
//        decrementNumber();


    }

    public static void printFactorial() {
        int userNumber;
        int result = 1;
        int i = 1;

        System.out.print("Введите число: ");
        userNumber = scanner.nextInt();
        if (userNumber > 0) {
            while (i < userNumber) {
                result = result * userNumber;
                userNumber--;
            }
        } else {
            System.out.println("Введено некорректное число: ожидается от 1 и больше");
        }
        System.out.println(result);
    }

    public static void printFactorialRemake() {
        int userNumber;
        int result = 1;
        int i = 1;

        System.out.print("Введите число: ");
        userNumber = scanner.nextInt();
        if (userNumber > 0) {
            while (i <= userNumber) {
                result = result * i;
                i++;
            }
        } else {
            System.out.println("Введено некорректное число: ожидается от 1 и больше");
        }
        System.out.println(result);
    }


    public static void printParityNumbers() {
        int userNumber;
        int i = 1;
        System.out.print("Введите число: ");
        userNumber = scanner.nextInt();
        if (userNumber > 0) {
            while (i < userNumber) {
                if ((userNumber - 1) % 2 == 0) {
                    System.out.println(userNumber - 1);
                    userNumber--;
                } else {
                    userNumber--;
                }
            }

        } else {
            System.out.println("Введено некорретное значение: ожидается больше 0");
        }
    }

    public static void printParityNumbersRemakeOne() {
        int userNumber;
        int i = 1;
        System.out.print("Введите число: ");
        userNumber = scanner.nextInt();
        if (userNumber > 0) {
            while (i < userNumber) {
                if ((userNumber - i) % 2 == 0) {
                    System.out.println(userNumber - i);
                    i++;
                } else {
                    i++;
                }
            }

        } else {
            System.out.println("Введено некорретное значение: ожидается больше 0");
        }
    }

    public static void printParityNumbersRemakeTwo() {
        int userNumber;
        System.out.print("Введите число: ");
        userNumber = scanner.nextInt();
        if (userNumber > 0) {
            while (1 < userNumber) {
                if ((userNumber - 1) % 2 == 0) {
                    System.out.println(userNumber - 1);
                    userNumber--;
                } else {
                    userNumber--;
                }
            }

        } else {
            System.out.println("Введено некорретное значение: ожидается больше 0");
        }
    }

    public static void decrementNumber() {
        int i = 1;
        int userNumber;
        System.out.print("Введите число: ");
        userNumber = scanner.nextInt();

        if (userNumber > 0) {
            while (i <= userNumber) {
                System.out.println(userNumber);
                userNumber--;
            }
        } else {
            System.out.println("Введенено некорректное число: ожидается больше 0");
        }

    }
}
