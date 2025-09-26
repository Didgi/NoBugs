package practice6_collections.additionalTasks.sixth;

import java.util.LinkedHashMap;

public class PhoneBook {
    private LinkedHashMap<String, Integer> phoneBookMap;

    public PhoneBook() {
        phoneBookMap = new LinkedHashMap<>();
    }

    public void addContact(String fullName, Integer phoneNumber) {
        phoneBookMap.putIfAbsent(fullName, phoneNumber);
        System.out.println("Контакт " + fullName + " с номером " + phoneNumber + " добавлен в телефонную книгу");
    }

    public void findContact(String fullName) {
        if (phoneBookMap.containsKey(fullName)) {
            System.out.println("Контакт " + fullName + " с номером " + phoneBookMap.get(fullName) + " найден в телефонной книге");
        } else {
            System.out.println("Контакт " + fullName + " отсутствует в телефонной книге");
        }
    }
}
