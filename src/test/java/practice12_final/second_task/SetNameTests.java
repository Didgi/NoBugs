package practice12_final.second_task;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class SetNameTests extends BaseTest {

    /**
     * Позитивные:
     * Изменение имени пользователя с валидным именем: NAME -> success
     * <p>
     * Негативные:
     * Изменение имени пользователя с невалидным именем: test -> InvalidUserException
     * Изменение имени пользователя с невалидным именем: "" -> InvalidUserException
     * Изменение имени пользователя с невалидным именем при отключенной валидации -> success
     * <p>
     * Корнер:
     * null -> NPE
     */

    //Позитивные тесты
    @ParameterizedTest
    @ValueSource(strings = {
            "Test",
            "NAME"
    })
    @DisplayName("Проверка на то, что возможно изменить имя пользователя с валидным именем")
    public void checkSetUserNameIsSuccessWhenNameIsValid(String name) throws InvalidUserException {
        user.setName(name);
        assertEquals(name, user.getName());
    }

    //Негативные тесты
    @ParameterizedTest
    @ValueSource(strings = {
            "test",
            ""
    })
    @DisplayName("Проверка на то, что невозможно изменить имя пользователя с невалидным именем")
    public void checkSetUserNameThrowsExceptionWhenNameIsInvalid(String name) throws InvalidUserException {
        assertThrows(InvalidUserException.class, () -> {
            user.setName(name);
        });
    }

    //Негативные тесты
    @ParameterizedTest
    @ValueSource(strings = {
            "test",
            ""
    })
    @DisplayName("Проверка на то, что возможно изменить имя пользователя с невалидным именем при отключенной валидации")
    public void checkSetUserNameIsSuccessWhenNameIsInvalidButValidationEnabledTurnOff(String name) throws InvalidUserException {
        UserValidator.setIsValidationEnabled(false);
        user.setName(name);
        assertEquals(name, user.getName());
    }

    //Негативные тесты
    @Test
    @DisplayName("Проверка на то, что невозможно изменить имя пользователя на null")
    public void checkSetUserNameThrowsNPEWhenNameIsInvalidWithNull() throws InvalidUserException {
        assertThrows(NullPointerException.class, () -> {
            user.setName(null);
        });
    }

    @AfterEach
    public void end() {
        UserValidator.setIsValidationEnabled(true);
    }
}
