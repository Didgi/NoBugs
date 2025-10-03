package practice7_exception_generics.exceptions;

import java.io.IOException;

public class InvalidAgeException extends IOException {
    public InvalidAgeException(String message) {
        super(message);
    }
}
