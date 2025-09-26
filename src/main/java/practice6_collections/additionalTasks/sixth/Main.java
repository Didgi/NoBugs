package practice6_collections.additionalTasks.sixth;

public class Main {

    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        phoneBook.addContact("Лёша", 123);
        phoneBook.addContact("Маша", 456);
        phoneBook.addContact("Саша", 789);
        phoneBook.findContact("Маша");
        phoneBook.findContact("Лёшка");
    }
}
