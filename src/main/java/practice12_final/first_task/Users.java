package practice12_final.first_task;

import java.util.Random;

public class Users extends Entity{

    private String groupNumber;
    public Users(String name, int age, EntityStatus entityStatus) {
        super(name, age, entityStatus);
    }

    public Users(String name, int age) {
        super(name, age);
    }
}
