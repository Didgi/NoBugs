package practice12_final.second_task;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class CreateUserTests {

    /**
     * Позитивные:
     * Создание пользователя с валидными данными: Test, 18, test-bugs@mail.ru -> success
     * Создание пользователя с валидными данными: NAME, 99, test-Email-WithDiffSymbols@google.com -> success
     * <p>
     * Негативные:
     * Создание пользователя с невалидным именем: test, 18, test-bugs@mail.ru -> InvalidUserException
     * Создание пользователя с невалидным именем: "", 18, test-bugs@mail.ru -> InvalidUserException
     * Создание пользователя с невалидным возрастом: Test, 17, test-bugs@mail.ru -> InvalidUserException
     * Создание пользователя с невалидным возрастом: Test, 100, test-bugs@mail.ru -> InvalidUserException
     * Создание пользователя с невалидным email:
     * Test, 18, test-bugs@mail.r -> InvalidUserException
     * Test, 18, test-bugs@.ru -> InvalidUserException
     * Test, 18, test-bugsmail.ru -> InvalidUserException
     * Test, 18, @mail.ru -> InvalidUserException
     * Создание пользователя с невалидными данными при отключенной валидации -> success
     * <p>
     * Корнер:
     * null, 18, @mail.ru -> NPE
     * Test, 18, null -> NPE
     */

    public static Stream<Arguments> diffValidUserData() {
        return Stream.of(Arguments.of("Test", 18, "test-bugs@mail.ru"), Arguments.of("NAME", 99, "test-Email-WithDiffSymbols@google.com"));
    }

    //Позитивные тесты
    @ParameterizedTest
    @MethodSource("diffValidUserData")
    @DisplayName("Проверка на то, что возможно создать пользователя с валидными данными")
    public void checkUserCreationIsSuccessWhenDataIsValid(String name, int age, String email) throws InvalidUserException {
        User user = new User(name, age, email);
        assertTrue(!user.toString().isEmpty());
        assertEquals(name, user.getName());
        assertEquals(age, user.getAge());
        assertEquals(email, user.getEmail());
    }

    public static Stream<Arguments> diffInvalidUserData() {
        return Stream.of(
                //Имя пользователя с маленькой буквы
                Arguments.of("test", 18, "test-bugs@mail.ru"),
                //Пустое имя пользователя
                Arguments.of("", 18, "test-bugs@mail.ru"),
                //Возраст выходящий за границы
                Arguments.of("Test", 17, "test-Email-WithDiffSymbols@google.com"),
                Arguments.of("Test", 100, "test-Email-WithDiffSymbols@google.com"),
                //Невалидный email
                Arguments.of("Test", 99, "test-bugs@mail.r"),
                Arguments.of("Test", 99, "test-bugs@.ru"),
                Arguments.of("Test", 99, "test-bugsmail.ru"),
                Arguments.of("Test", 99, "@mail.ru"));
    }

    //Негативные тесты
    @ParameterizedTest
    @MethodSource("diffInvalidUserData")
    @DisplayName("Проверка на то, что невозможно создать пользователя с невалидными данными")
    public void checkUserCreationThrowsExceptionWhenDataIsInvalid(String name, int age, String email) throws InvalidUserException {

        assertThrows(InvalidUserException.class, () -> {
            new User(name, age, email);
        });
    }

    //Негативные тесты
    @ParameterizedTest
    @MethodSource("diffInvalidUserData")
    @DisplayName("Проверка на то, что возможно создать пользователя с невалидными данными при отключенной валидации")
    public void checkUserCreationIsSuccessWhenDataIsInvalidButValidationEnabledTurnOff(String name, int age, String email) throws InvalidUserException {
        UserValidator.setIsValidationEnabled(false);
        User user = new User(name, age, email);
        assertTrue(!user.toString().isEmpty());
        assertEquals(name, user.getName());
        assertEquals(age, user.getAge());
        assertEquals(email, user.getEmail());
    }

    public static Stream<Arguments> diffInvalidNullUserData() {
        return Stream.of(
                Arguments.of(null, 50, "test-Email-WithDiffSymbols@google.com"),
                Arguments.of("Test", 50, null));
    }

    //Негативные тесты
    @ParameterizedTest
    @MethodSource("diffInvalidNullUserData")
    @DisplayName("Проверка на то, что невозможно создать пользователя с невалидными данными, где имя/email - null")
    public void checkUserCreationThrowsNPExceptionWhenDataIsInvalidWithNull(String name, int age, String email) throws InvalidUserException {

        assertThrows(NullPointerException.class, () -> {
            new User(name, age, email);
        });
    }

    @AfterEach
    public void end() {
        UserValidator.setIsValidationEnabled(true);
    }
}
