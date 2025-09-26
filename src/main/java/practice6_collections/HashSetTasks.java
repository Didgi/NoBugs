package practice6_collections;

import java.util.*;

public class HashSetTasks {

    public static Set<String> returnUniqStrings(List<String> list) {
        return new HashSet<>(list);
    }

    public static void checkContainValue(HashSet<String> set, String name) {
        if (set.contains(name)) {
            System.out.println("Имя " + name + " содержится в списке");
        } else {
            System.out.println("Имя " + name + " добавлено в список");
            System.out.println(set);
        }
    }

    public static void main(String[] args) {
        //firstTask
        HashSet<Integer> firstTask = new HashSet<>(Arrays.asList(1, 2, 3, 4, 2));
        System.out.println(firstTask);

        //secondTask
        HashSet<Integer> secondTask = new HashSet<>();
        for (int i = 1; i < 11; i++) {
            secondTask.add(i);
        }
        System.out.println(secondTask);

        //thirdTask
        List<String> thirdTask = new ArrayList<>(Arrays.asList("Йоу", "КакДела", "Йоу", "Бро"));
        System.out.println(returnUniqStrings(thirdTask));

        //fourthTask
        HashSet<String> setNames = new HashSet<>(Arrays.asList(null, "Лёша", "Петя", "Саша"));
        checkContainValue(setNames, null);
        checkContainValue(setNames, "Лёша");
        checkContainValue(setNames, "Алёша");

    }
}
