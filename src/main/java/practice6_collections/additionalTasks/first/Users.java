package practice6_collections.additionalTasks.first;

import java.util.HashMap;

public class Users {

    public HashMap<Integer, String> getUsers() {
        return users;
    }

    private HashMap<Integer, String> users;

    public Users() {
        users = new HashMap<>();
    }

    public void addUser(Integer id, String userName) {
        if (checkContainUser(id)) {
            System.out.println("Пользователь с id: " + id + " и именем " + userName + " добавлен");
            users.put(id, userName);
        }
    }

    public boolean checkContainUser(Integer id) {
        if (users.containsKey(id)) {
            System.out.println("Пользователь с " + id + " уже существует");
            return false;
        } else {
            System.out.println("Пользователь с " + id + " отсутствует");
            return true;
        }

    }
}
