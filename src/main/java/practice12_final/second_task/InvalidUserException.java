package practice12_final.second_task;

import java.io.IOException;

public class InvalidUserException extends IOException {
    public InvalidUserException(String message) {
        super(message);
    }
}
