package practice6_collections;

import java.util.ArrayList;
import java.util.Arrays;

public class ArrayListTasks {

    public static void printParityNumbers(ArrayList<Integer> array) {
        array.forEach(number -> {
            if (number % 2 == 0) {
                System.out.println(number);
            }
        });
    }

    public static void printMaxString(ArrayList<String> array) {
        String maxString = "";
        for (String s : array) {
            if (maxString.length() < s.length()) {
                maxString = s;
            }
        }
        System.out.println(maxString);
    }

    public static void sumNumbersInArray(ArrayList<Integer> array) {
        int sum = 0;
        for (Integer i : array) {
            sum += i;
        }
        System.out.println("Сумма чисел " + sum);
    }

    public static void printMaxNumber(ArrayList<Integer> array) {
        int maxNumber = 0;
        for (Integer i : array) {
            if (maxNumber < i) {
                maxNumber = i;
            }
        }
        System.out.println("Максимальное число в массиве " + maxNumber);
    }

    public static void main(String[] args) {
        //firstTask
        ArrayList<Integer> numbersInArrayListFirst = new ArrayList<>(Arrays.asList(1,-1,-4, 5,99,100));
        System.out.println("Оригинальный массив " + numbersInArrayListFirst);
        numbersInArrayListFirst.add(99);
        System.out.println("Изменённый массив " + numbersInArrayListFirst);

        //secondTask
        ArrayList<Integer> numbersInArrayListSecond = new ArrayList<>(Arrays.asList(1,-1,-4, 5,99,100));
        printParityNumbers(numbersInArrayListSecond);

        //thirdTask
        ArrayList<String> stringsInArrayList = new ArrayList<>();
        stringsInArrayList.add("Привет");
        stringsInArrayList.add("Как");
        stringsInArrayList.add("Дела");
        stringsInArrayList.add("Привет");
        stringsInArrayList.add("Приветики");
        printMaxString(stringsInArrayList);

        //fourthTask
        ArrayList<Integer> numbersInArrayListFourth = new ArrayList<>(Arrays.asList(1, -1, -4, 5, 99, 100));
        numbersInArrayListFourth.remove(numbersInArrayListFourth.size() - 1);
        sumNumbersInArray(numbersInArrayListFourth);

        //fifthTask
        ArrayList<Integer> numbersInArrayListFifth = new ArrayList<>(Arrays.asList(1, 101, 1,-400, 5,99,100));
        printMaxNumber(numbersInArrayListFifth);
    }
}
