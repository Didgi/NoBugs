package practice6_collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class ArrayListTasks {

    public static void printParityNumbers(ArrayList<Integer> array) {
        array.forEach(number -> {
            if (number % 2 == 0) {
                System.out.println(number);
            }
        });
    }

    public static void printMaxString(ArrayList<String> array) {
        AtomicReference<String> maxString = new AtomicReference<>(array.get(0));
        array.forEach(string -> {
            if (maxString.get().length() < string.length()) {
                maxString.set(string);
            }
        });
        System.out.println(maxString);
    }

    public static void sumNumbersInArray(ArrayList<Integer> array) {
        AtomicReference<Integer> sum = new AtomicReference<>(0);
        array.forEach(number -> {
            sum.updateAndGet(v -> v + number);
        });
        System.out.println("Сумма чисел " + sum);
    }

    public static void printMaxNumber(ArrayList<Integer> array) {
        AtomicReference<Integer> maxNumber = new AtomicReference<>(array.get(0));
        array.forEach(number -> {
            if (maxNumber.get() < number) {
                maxNumber.set(number);
            }
        });
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
        ArrayList<Integer> numbersInArrayListFourth = new ArrayList<>(Arrays.asList(1,-1,-4, 5,99,100));
        numbersInArrayListFourth.remove(numbersInArrayListFourth.size()-1);
        sumNumbersInArray(numbersInArrayListFourth);

        //fifthTask
        ArrayList<Integer> numbersInArrayListFifth = new ArrayList<>(Arrays.asList(1, 101, 1,-400, 5,99,100));
        printMaxNumber(numbersInArrayListFifth);
    }
}
