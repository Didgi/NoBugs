package practice4;

import java.util.Scanner;

public class SwitchTasks {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        //Расшифрока дня недели
        //printDaysOfWeek();
        //Печать стоимости билета в кино
//        printCinemaTicketPrice();
        //Перевод числовой оценки в буквенную
//        transferDigitalGradesToLetters();
        //Обработка текстовых команд
//        processTextCommand();
        //Базовый калькулятор двух чисел
//        calculateNumbers();
    }

    public static void printDaysOfWeek() {
        int day;
        String dayDescription;

        System.out.print("Введите порядковый номер дня недели: ");
        day = scanner.nextInt();

        switch (day) {
            case 1:
                dayDescription = "Понедельник";
                break;
            case 2:
                dayDescription = "Вторик";
                break;
            case 3:
                dayDescription = "Среда";
                break;
            case 4:
                dayDescription = "Четверг";
                break;
            case 5:
                dayDescription = "Пятница";
                break;
            case 6:
                dayDescription = "Суббота";
                break;
            case 7:
                dayDescription = "Воскресенье";
                break;
            default:
                dayDescription = "Введён некорректный день недели. Ожидается значение от 1 до 7";
        }
        if (day >= 1 && day <= 7) {
            System.out.println(day + " - " + dayDescription);
        } else {
            System.out.println(dayDescription);
        }
    }

    public static void printCinemaTicketPrice() {
        int day;
        int tickerPrice;

        System.out.print("Введите порядковый номер дня недели: ");
        day = scanner.nextInt();

        switch (day) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                tickerPrice = 300;
                break;
            case 6:
            case 7:
                tickerPrice = 450;
                break;
            default:
                tickerPrice = 0;
        }
        if (tickerPrice != 0) {
            System.out.println("Стоимость билета " + tickerPrice + " рублей");
        } else {
            System.out.println("Введён некорректный день недели. Ожидается значение от 1 до 7");
        }
    }

    public static void transferDigitalGradesToLetters() {
        int grade;
        char gradeDescription;

        System.out.print("Введите оценку: ");
        grade = scanner.nextInt();

        if (grade >= 0 && grade <= 100) {
            if (grade >= 90) {
                gradeDescription = 'A';
            } else if (grade >= 80) {
                gradeDescription = 'B';
            } else if (grade >= 70) {
                gradeDescription = 'C';

            } else if (grade >= 60) {
                gradeDescription = 'D';

            } else {
                gradeDescription = 'F';
            }
            System.out.println(gradeDescription);
        } else {
            System.out.println("Введена некорректная оценка. Ожидется от 0 до 100");
        }
    }

    public static void processTextCommand() {
        String command;
        String commandDescription;
        System.out.print("Введите команду: ");
        command = scanner.nextLine();

        switch (command) {
            case "start":
                commandDescription = "Система запущена";
                break;
            case "stop":
                commandDescription = "Система остановлена";
                break;
            case "restart":
                commandDescription = "Система перезапущена";
                break;
            case "status":
                commandDescription = "Запрос статуса системы";
                break;
            default:
                commandDescription = "Введена неизвестная команда. " +
                        "Ожидается одно из значений: start, stop, restart, status";
        }
        System.out.println(commandDescription);

    }

    public static void calculateNumbers() {
        int firstNumber;
        int secondNumber;
        double result = 0;
        String arithmeticOperator;

        System.out.print("Введите первое число: ");
        firstNumber = scanner.nextInt();
        System.out.print("Введите второе число: ");
        secondNumber = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Введите арифметический оператор: ");
        arithmeticOperator = scanner.nextLine();

        if (arithmeticOperator.equals("+") || arithmeticOperator.equals("-")
                || arithmeticOperator.equals("/") || arithmeticOperator.equals("*")) {
            switch (arithmeticOperator) {
                case "+":
                    result = firstNumber + secondNumber;
                    System.out.println(result);
                    break;
                case "-":
                    result = firstNumber - secondNumber;
                    System.out.println(result);
                    break;
                case "/":
                    if (secondNumber == 0) {
                        System.out.println("Деление на ноль запрещено");
                    } else {
                        result = (double) firstNumber / secondNumber;
                        System.out.println(result);
                    }
                    break;
                case "*":
                    result = firstNumber * secondNumber;
                    System.out.println(result);
                    break;
            }
        } else {
            System.out.println("Введен некорректный арифметический оператор. Ожидается одно из следующих значения: " +
                    "'+', '-', '/', '*'");
        }

    }

}
