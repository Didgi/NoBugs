package other_m;

import java.util.Random;

public class AuthorizedUsers extends Users{

    private int id;
    public AuthorizedUsers(String name) {
        super(name);
        this.id = new Random().nextInt(100);;
    }

    @Override
    public String toString() {
        return "Авторизованный под " +
                "id=" + id + " по имени " + getName();
    }
}
