package api_tests.iteraion2_senior;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import requests.steps.user_steps.UserSteps;

import static requests.steps.admin_steps.AdminSteps.createUserAndGetToken;
import static requests.steps.admin_steps.AdminSteps.deleteUsersById;
import static requests.steps.user_steps.UserSteps.createUserAccount;
import static requests.steps.user_steps.UserSteps.deleteUserAccounts;

public class BaseTestSenior {
    protected static String authUserToken;
    protected static int userAccount;
    public static SoftAssertions softly;

    @BeforeEach
    public void setUp() {
        softly = new SoftAssertions();
        UserSteps.SoftAssertions(softly);
        authUserToken = createUserAndGetToken();
        userAccount = createUserAccount(authUserToken);
    }

    @AfterEach
    public void cleanUp() {
        deleteUserAccounts(authUserToken);
        deleteUsersById();
        softly.assertAll();
    }
}
