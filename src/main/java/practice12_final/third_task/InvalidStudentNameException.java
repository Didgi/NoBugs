package practice12_final.third_task;

import java.io.IOException;

public class InvalidStudentNameException extends IOException {
    public InvalidStudentNameException(String message) {
        super(message);
    }
}
