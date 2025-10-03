package practice6_collections;

import practice_2.Teacher;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class TreeMapTasks {

    static TreeMap<String, Integer> employeeData = new TreeMap<>();

    public static void addEmployee(String fullName, Integer id) {
        employeeData.put(fullName, id);
        System.out.println("Сотрудник " + fullName + " c id " + id + " добавлен");
    }

    public static Integer findMoreId(String name) {
        final Map.Entry<String, Integer> nameEntry = employeeData.higherEntry(name);
        return nameEntry != null ? nameEntry.getValue() : employeeData.get(name);
    }


    public static void main(String[] args) {

        //firstTask
        TreeMap<String, Integer> firstExample = new TreeMap<>();
        firstExample.put("Лёша", 99);
        firstExample.put("Саша", 100);
        firstExample.put("Маша", -1);
        firstExample.put("Даша", 30);
        firstExample.put("Игорь", 30);
        System.out.println(firstExample);

        //secondTask
        System.out.println(firstExample.firstKey());
        System.out.println(firstExample.lastKey());

        //thirdTask
        addEmployee("ПервыйБро", 99);
        addEmployee("ВторойБро", 33);
        addEmployee("ТретийБро", 98);
//        addEmployee("ТретийБроооо", 100);
        System.out.println(findMoreId("ТретийБро"));
    }
}
