package practice12_final.first_task;

import org.junit.jupiter.api.BeforeEach;
import practice12_final.first_task.EntityManager;
import practice12_final.first_task.Users;

import java.util.Random;

public class BaseTest {
    public EntityManager<Users> entityManager;
    public int randomAge;

    @BeforeEach
    public void setUp() {
        entityManager = new EntityManager<>();
        randomAge = new Random().nextInt(99);
    }


}
