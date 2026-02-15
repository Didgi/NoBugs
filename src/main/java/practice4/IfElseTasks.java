package practice4;

import javax.sound.midi.Soundbank;
import java.util.Scanner;

public class IfElseTasks {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        //Определение знака числа
//                defineSignNumber();
        //Поиск максимального числа из двух
//        maxNumber();
        //Получение описания оценки
//        describeGrade();
        //Проверка числа на чётность
//        checkParity();
        //Печать скидки по возрасту
//        checkAgeDiscount();
        //Получение оценки по баллам
//        checkTestResult();
    }

    public static void defineSignNumber() {
        int number;
        String signDescription = "Число равно нулю";
        System.out.print("Введите число: ");
        number = scanner.nextInt();
        if (number > 0) {
            signDescription = "Число положительное";
        } else if (number < 0) {
            signDescription = "Число отрицательное";
        }
        System.out.println(signDescription);
    }

    public static void maxNumber() {
        int firstNumber;
        int secondNumber;
        System.out.print("Введите первое число: ");
        firstNumber = scanner.nextInt();
        System.out.print("Введите второе число: ");
        secondNumber = scanner.nextInt();
        System.out.println("Наибольшее число: " + Math.max(firstNumber, secondNumber));
    }

    public static void describeGrade() {
        int grade;
        String gradeDescription = "Введена неизвестная оценка";
        System.out.print("Введите оценку: ");
        grade = scanner.nextInt();
        if (grade >= 1 && grade <= 5) {
            switch (grade) {
                case 5:
                    gradeDescription = "Отлично";
                    break;
                case 4:
                    gradeDescription = "Хорошо";
                    break;
                case 3:
                    gradeDescription = "Удовлетворительно";
                    break;
                case 2:
                case 1:
                    gradeDescription = "Неудовлетворительно";
                    break;
            }
        }
        System.out.println(gradeDescription);
    }

    public static void checkParity() {
        int number;
        String numberDescription = "Нечётное";
        System.out.print("Введите число: ");
        number = scanner.nextInt();
        if (number % 2 == 0) {
            numberDescription = "Чётное";
        }
        System.out.println(numberDescription);
    }

    public static void checkAgeDiscount() {
        int age;
        String discountDescription = "Скидка отсутствует";
        System.out.print("Введите возраст: ");
        age = scanner.nextInt();
        if (age <= 0 || age >= 120) {
            discountDescription = "Введён некорректный возраст";
        } else if (age < 18) {
            discountDescription = "Размер скидки - 25%";
        } else if (age >= 65) {
            discountDescription = "Размер скидки - 30%";
        }
        System.out.println(discountDescription);
    }

    public static void checkTestResult() {
        int grade;
        String gradeDescription = "Неудовлетворительно";
        System.out.print("Введите количество баллов за экзамен: ");
        grade = scanner.nextInt();
        if (grade > 100 || grade < 0) {
            gradeDescription = "Введено некорректное количество баллов";
        } else if (grade >= 90) {
            gradeDescription = "Отлично";
        } else if (grade >= 75) {
            gradeDescription = "Хорошо";
        } else if (grade >= 60) {
            gradeDescription = "Удовлетворительно";
        }
        System.out.println(gradeDescription);

    }
}
