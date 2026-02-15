package practice12_final.second_task;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SetEmailTests extends BaseTest {

    /**
     * Позитивные:
     * Изменение email пользователя с валидным email: test-bugs@mail.ru -> success
     * <p>
     * Негативные:
     * Изменение email пользователя с невалидным email:
     * * test-bugs@mail.r -> InvalidUserException
     * * test-bugs@.ru -> InvalidUserException
     * * test-bugsmail.ru -> InvalidUserException
     * * @mail.ru -> InvalidUserException
     * Изменение email пользователя с невалидным email при отключенной валидации -> success
     * <p>
     * Корнер:
     * null -> NPE
     */

    //Позитивные тесты
    @ParameterizedTest
    @ValueSource(strings = {
            "test-bugs@mail.ru",
            "test-Email-WithDiffSymbols@google.com"
    })
    @DisplayName("Проверка на то, что возможно изменить email пользователя с валидным email")
    public void checkSetUserEmailIsSuccessWhenEmailIsValid(String email) throws InvalidUserException {
        user.setEmail(email);
        assertEquals(email, user.getEmail());
    }

    public static Stream<Arguments> diffInvalidUserEmail() {
        return Stream.of(
                //Невалидный email
                Arguments.of("test-bugs@mail.r"),
                Arguments.of("test-bugs@.ru"),
                Arguments.of("test-bugsmail.ru"),
                Arguments.of("@mail.ru"));
    }

    //Негативные тесты
    @ParameterizedTest
    @MethodSource("diffInvalidUserEmail")
    @DisplayName("Проверка на то, что невозможно изменить email пользователя с невалидным email")
    public void checkSetUserEmailThrowsExceptionWhenEmailIsInvalid(String email) throws InvalidUserException {
        assertThrows(InvalidUserException.class, () -> {
            user.setEmail(email);
        });
    }

    //Негативные тесты
    @ParameterizedTest
    @MethodSource("diffInvalidUserEmail")
    @DisplayName("Проверка на то, что возможно изменить email пользователя с невалидным email при отключенной валидации")
    public void checkSetUserEmailIsSuccessWhenEmailIsInvalidButValidationEnabledTurnOff(String email) throws InvalidUserException {
        UserValidator.setIsValidationEnabled(false);
        user.setEmail(email);
        assertEquals(email, user.getEmail());
    }

    //Негативные тесты
    @Test
    @DisplayName("Проверка на то, что невозможно изменить email пользователя на null")
    public void checkSetUserEmailThrowsNPEWhenEmailIsInvalidWithNull() throws InvalidUserException {
        assertThrows(NullPointerException.class, () -> {
            user.setEmail(null);
        });
    }

    @AfterEach
    public void end() {
        UserValidator.setIsValidationEnabled(true);
    }
}
