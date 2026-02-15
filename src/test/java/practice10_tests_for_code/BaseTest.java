package practice10_tests_for_code;

import org.junit.jupiter.api.BeforeEach;

public class BaseTest {
    protected MainTasks mainTasks;
    protected AdditionalTasks additionalTasks;

    @BeforeEach
    public void setUp() {
        mainTasks = new MainTasks();
        additionalTasks = new AdditionalTasks();
    }
}
