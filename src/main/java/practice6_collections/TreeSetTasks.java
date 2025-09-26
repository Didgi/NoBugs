package practice6_collections;

import java.util.Arrays;
import java.util.TreeSet;

public class TreeSetTasks {

    public static void addNumberInSet(TreeSet<Integer> set, Integer number) {
        if (!set.contains(number)) {
            set.add(number);
            System.out.println("Число " + number + " добавлено в сет");
        } else {
            System.out.println("Число " + number + " уже существует");
        }
    }

    public static void main(String[] args) {

        //firstTask
        TreeSet<Integer> firstTask = new TreeSet<>(Arrays.asList(3, 2, 1, 6, 5));
        System.out.println(firstTask);

        //secondTask
        addNumberInSet(firstTask, 1);
        addNumberInSet(firstTask, 100);

        //thirdTask
        System.out.println(firstTask.higher(5));
        System.out.println(firstTask.lower(100));
        System.out.println(firstTask.lower(101));
    }
}
