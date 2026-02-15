package practice12_final.second_task;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SetAgeTests extends BaseTest {

    /**
     * Позитивные:
     * Изменение возраста пользователя с валидным возрастом: 18 -> success
     * <p>
     * Негативные:
     * Изменение возраста пользователя с невалидным возрастом: 17 -> InvalidUserException
     * Изменение возраста пользователя с невалидным возрастом: 100 -> InvalidUserException*
     * Изменение возраста пользователя с невалидным возрастом при отключенной валидации -> success
     */

    //Позитивные тесты
    @ParameterizedTest
    @ValueSource(ints = {
            18,
            99
    })
    @DisplayName("Проверка на то, что возможно изменить возраст пользователя с валидным возрастом")
    public void checkSetUserAgeIsSuccessWhenAgeIsValid(int age) throws InvalidUserException {
        user.setAge(age);
        assertEquals(age, user.getAge());
    }

    //Негативные тесты
    @ParameterizedTest
    @ValueSource(ints = {
            17,
            100
    })
    @DisplayName("Проверка на то, что невозможно изменить возраст пользователя с невалидным возрастом")
    public void checkSetUserAgeThrowsExceptionWhenAgeIsInvalid(int age) throws InvalidUserException {
        assertThrows(InvalidUserException.class, () -> {
            user.setAge(age);
        });
    }

    //Негативные тесты
    @ParameterizedTest
    @ValueSource(ints = {
            17,
            100
    })
    @DisplayName("Проверка на то, что возможно изменить возраст пользователя с невалидным возрастом при отключенной валидации")
    public void checkSetUserAgeIsSuccessWhenAgeIsInvalidButValidationEnabledTurnOff(int age) throws InvalidUserException {
        UserValidator.setIsValidationEnabled(false);
        user.setAge(age);
        assertEquals(age, user.getAge());
    }

    @AfterEach
    public void end() {
        UserValidator.setIsValidationEnabled(true);
    }
}
