package practice6_collections.additionalTasks.first;

public class Main {
    public static void main(String[] args) {
        Users users = new Users();
        System.out.println("Список пользователей: " + users.getUsers());
        users.addUser(123, "First User");
        System.out.println("Список пользователей: " + users.getUsers());
        users.addUser(456, "Second User");
        System.out.println("Список пользователей: " + users.getUsers());
        users.addUser(123, "First User");
        System.out.println("Список пользователей: " + users.getUsers());
        users.checkContainUser(123);
        users.checkContainUser(789);
    }
}
