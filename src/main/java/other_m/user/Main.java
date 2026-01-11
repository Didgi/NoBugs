package other_m.user;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    private static String json = """
            [
            {"id": 1, "name": "Alice", "email": "email@mail.ru", "age": 5},
            {"id": 2, "name": "Mike", "email": "email@mail.com", "age": 19},
            {"id": 3, "name": "", "email": "email@mail.com", "age": 100},
            {"id": 4, "name": "Jake", "email": "", "age": 100},
            ]
            """;
    static Gson gson = new Gson();
    static Type userListType = new TypeToken<List<User>>() {
    }.getType();
    static List<User> users = gson.fromJson(json, userListType);

    public static void main(String[] args) {
        String regExpFormat = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        users.stream().filter(x -> x.getName().isBlank()
                || x.getEmail().matches(regExpFormat)
                || x.getAge() < 18).collect(Collectors.toList()).forEach(System.out::println);

    }



}
