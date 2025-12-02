package practice12_final.third_task;

import java.io.IOException;

public class InvalidGradeException extends IOException {
    public InvalidGradeException(String message) {
        super(message);
    }
}
