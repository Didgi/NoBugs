package practice6_collections;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.ListIterator;

public class LinkedListTasks {

    public static void printFirstLastElementInList(LinkedList<String> list) {
        System.out.println("Первый элемент: " + list.getFirst());
        System.out.println("Последний элемент: " + list.getLast());
    }

    public static void printSumNumbersInList(LinkedList<Integer> list) {
        Integer sum = 0;
        for (Integer integer : list) {
            sum += integer;
        }
        System.out.println(sum);
    }

    public static void printElementsByIterator(ListIterator<String> stringListIterator) {
        while (stringListIterator.hasNext()) {
            System.out.println(stringListIterator.next());
        }
        while (stringListIterator.hasPrevious()) {
            System.out.println(stringListIterator.previous());
        }
    }

    public static void main(String[] args) {
        //firstTask
        LinkedList<String> firstTask = new LinkedList<>();
        firstTask.add("Привет");
        firstTask.add("Мир");
        firstTask.add("Привет");
        firstTask.add("Мир");
        firstTask.add("Как дела, бро?");
        System.out.println(firstTask);

        //secondTask
        LinkedList<String> queueList = new LinkedList<>();
        queueList.offer("First");
        queueList.offer("Second");
        queueList.offer("Third");
        System.out.println("Первый элемент: " + queueList.poll());
        System.out.println("Второй элемент: " + queueList.poll());
        System.out.println("Третий элемент: " + queueList.poll());

        //thirdTask
        LinkedList<String> thirdTask = new LinkedList<>(Arrays.asList("First", "Middle", "Last"));
        printFirstLastElementInList(thirdTask);

        //fourthTask
        LinkedList<Integer> fourthTask = new LinkedList<>(Arrays.asList(-1, 1, 5));
        printSumNumbersInList(fourthTask);

        //fifthTask
        LinkedList<String> fifthTask = new LinkedList<>(Arrays.asList("First", "Middle", "Last"));
        printElementsByIterator(fifthTask.listIterator());


    }
}
