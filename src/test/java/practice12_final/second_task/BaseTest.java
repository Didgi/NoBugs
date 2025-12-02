package practice12_final.second_task;

import org.junit.jupiter.api.BeforeEach;

public class BaseTest {
    protected User user;

    @BeforeEach
    public void setUp() throws InvalidUserException {
        user = new User("TestValidName", 33, "testValidEmail@mail.com");
    }
}
