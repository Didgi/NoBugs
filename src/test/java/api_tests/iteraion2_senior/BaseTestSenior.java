package api_tests.iteraion2_senior;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import api.requests.steps.user_steps.UserSteps;

import static api.requests.steps.admin_steps.AdminSteps.createUserAndGetToken;
import static api.requests.steps.admin_steps.AdminSteps.deleteUsersById;
import static api.requests.steps.user_steps.UserSteps.createUserAccount;
import static api.requests.steps.user_steps.UserSteps.deleteUserAccounts;

public class BaseTestSenior {
    protected static String authUserToken;
    protected static int userAccount;
    public static SoftAssertions softly;

    @BeforeEach
    public void setUp() {
        softly = new SoftAssertions();
        UserSteps.SoftAssertions(softly);
        // 1. Создаём нового пользователя и получаем его токен
        authUserToken = createUserAndGetToken();
        // 2. Создаём аккаунт для пользователя
        userAccount = createUserAccount(authUserToken);
    }

    @AfterEach
    public void cleanUp() {
        // 3. Удаляем все аккаунты пользователя
        deleteUserAccounts(authUserToken);
        // 4. Удаляем всех пользователей
        deleteUsersById();
        softly.assertAll();
    }
}
