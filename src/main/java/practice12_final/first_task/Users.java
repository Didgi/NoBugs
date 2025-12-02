package practice12_final.first_task;

import java.util.Random;

public class Users extends Entity{

    private final String groupNumber;
    public Users(String name, int age, EntityStatus entityStatus) {
        super(name, age, entityStatus);
        this.groupNumber = "Группа №: " + new Random().nextInt(50);
    }

    public Users(String name, int age) {
        super(name, age);
        this.groupNumber = "Группа №: " + new Random().nextInt(50);
    }
}
