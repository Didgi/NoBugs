package practice6_collections;

import java.util.Arrays;
import java.util.LinkedHashSet;

public class LinkedHashSetTasks {

    public static void addElementInLinkedHashSet(LinkedHashSet<String> set, String value){
        if (!set.contains(value)){
            set.add(value);
            System.out.println("Элемент " + value + " добавлен в сет");
        } else {
            System.out.println("Элемент " + value + " уже существует");
        }
    }

    public static void main(String[] args) {

        //firstTask
        LinkedHashSet<String> firstTask = new LinkedHashSet<>();
        firstTask.add("First");
        firstTask.add("Second");
        firstTask.add("Third");
        firstTask.add("Four");
        firstTask.add("Five");
        System.out.println(firstTask);

        //secondTask
        LinkedHashSet<String> secondTask = new LinkedHashSet<>(Arrays.asList("Проверка дублей", "Чек", "Саунд"));
        addElementInLinkedHashSet(secondTask, "Проверка дублей");
        addElementInLinkedHashSet(secondTask, "Не дубль");
    }
}
