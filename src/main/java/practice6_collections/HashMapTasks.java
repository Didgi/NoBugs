package practice6_collections;

import java.util.HashMap;

public class HashMapTasks {

    public static void checkContainsName(HashMap<String, Integer> map, String name) {
        if (map.containsKey(name)) {
            System.out.println("Имя " + name + " содержится в словаре");
        } else {
            System.out.println("Имя " + name + " не содержится в словаре");
        }
    }

    public static void printYoungUsers(HashMap<String, Integer> map) {
        map.forEach((key, value) -> {
            if (value < 18) {
                System.out.println(key + " младше 18 лет");
            }
        });

    }

    public static void main(String[] args) {
        //firstTask
        HashMap<String, Integer> firstExample = new HashMap<>();
        firstExample.put("Лёша", 33);
        firstExample.put("Антуан", 18);
        firstExample.put("Маша", 99);
        firstExample.put("Антон", 1);
        firstExample.put("Ника", 35);
        firstExample.put("Лёша", 17);
        System.out.println(firstExample);

        //secondTask
        checkContainsName(firstExample, "Лёша");
        checkContainsName(firstExample, "Катя");

        //thirdTask
        printYoungUsers(firstExample);
    }
}
