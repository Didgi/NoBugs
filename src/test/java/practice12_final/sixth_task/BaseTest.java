package practice12_final.sixth_task;

import org.junit.jupiter.api.BeforeEach;

public class BaseTest <T>{
    TaskService<T> taskService;

    @BeforeEach
    public void setUp(){
        taskService = new TaskService<>();
    }
}
