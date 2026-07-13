package api_tests;

import api.requests.steps.user_steps.UserSteps;
import common.extensions.ApiVersionExtension;
import common.extensions.BugExtension;
import common.extensions.FraudCheckExtension;
import common.extensions.TimingExtension;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import static api.requests.steps.admin_steps.AdminSteps.createUserAndGetToken;
import static api.requests.steps.admin_steps.AdminSteps.deleteUsersById;
import static api.requests.steps.user_steps.UserSteps.createUserAccount;

@ExtendWith(BugExtension.class)
@ExtendWith(TimingExtension.class)
@ExtendWith(ApiVersionExtension.class)
@ExtendWith(FraudCheckExtension.class)
public class BaseTestSenior {
    protected String authUserToken;
    protected int userAccount;
    public SoftAssertions softly;

    @BeforeEach
    public void setUp() {
        softly = new SoftAssertions();
        UserSteps.SoftAssertions(softly);
        authUserToken = createUserAndGetToken();
        userAccount = createUserAccount(authUserToken);
    }

    @AfterEach
    public void cleanUp() {
        deleteUsersById();
        softly.assertAll();
    }
}
