package practice6_collections;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class LinkedHashMapTasks {

    private static final int MAX_ENTRIES = 10;
    public static LinkedHashMap<String, Integer> phoneBook = new LinkedHashMap<>();


    private static LinkedHashMap<String, String> thirdExample = new LinkedHashMap<>(10) {
        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return size() > MAX_ENTRIES;
        }

    };

    public static void addContact(String fullName, Integer phoneNumber) {
        phoneBook.putIfAbsent(fullName, phoneNumber);
        System.out.println("Контакт " + fullName + " с номером " + phoneNumber + " добавлен в телефонную книгу");
    }

    public static void findContact(String fullName) {
        if (phoneBook.containsKey(fullName)) {
            System.out.println("Контакт " + fullName + " с номером " + phoneBook.get(fullName) + " найден в телефонной книге");
        } else {
            System.out.println("Контакт " + fullName + " отсутствует в телефонной книге");
        }
    }

    public static void findContact(Integer phoneNumber) {
        if (phoneBook.containsValue(phoneNumber)) {
            System.out.println("Контакт с номером " + phoneNumber + " найден в телефонной книге");
        } else {
            System.out.println("Контакт с номером " + phoneNumber + " отсутствует в телефонной книге");
        }
    }

    public static void main(String[] args) {

        //firstTask
        LinkedHashMap<String, String> firstExample = new LinkedHashMap<>();
        firstExample.put("FirstElement", "Fir");
        firstExample.put("Second", "Sec");
        firstExample.put("Third", "Th");
        firstExample.put("Fourth", "Fo");
        firstExample.put("Fifth", "Fi");
        System.out.println(firstExample);

        //secondTask
        addContact("Лёша", 123);
        addContact("Маша", 456);
        addContact("Саша", 789);
        findContact(456);
        findContact(4566);
        findContact("Маша");
        findContact("Лёшка");

        //thirdTask
        thirdExample.put("Юзер", "Открыл браузер");
        thirdExample.put("Юзер2", "Открыл новую вкладку");
        thirdExample.put("Юзер3", "Ввёл в поисков значение");
        thirdExample.put("Юзер4", "Удалил ввод значения");
        thirdExample.put("Юзер5", "Закрыл вкладку");
        thirdExample.put("Юзер6", "Открыл новую вкладку");
        thirdExample.put("Юзер7", "Ввёл в поиске гугла что-то");
        thirdExample.put("Юзер8", "Закрыл вкладку вновь");
        thirdExample.put("Юзер9", "Открыл ютуб");
        thirdExample.put("Юзер10", "Начал смотреть NoBugs");
        thirdExample.put("Юзер11", "Просмотрел все уроки NoBugs");
        thirdExample.put("Юзер12", "Поставил лайк Саше");
        System.out.println(thirdExample);


    }
}
