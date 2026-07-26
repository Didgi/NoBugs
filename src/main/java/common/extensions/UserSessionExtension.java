package common.extensions;

import api.models.UserAccountResponse;
import api.models.UserProfileResponse;
import api.requests.steps.user_steps.UserSteps;
import common.SessionStorage;
import common.annotations.UserSession;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.List;

public class UserSessionExtension implements BeforeEachCallback {
    @Override
    public void beforeEach(ExtensionContext context) {
        UserSession userSession = context.getRequiredTestMethod().getAnnotation(UserSession.class);
        if (userSession != null) {
            final int accountAmount = userSession.amountAccounts();
            for (int i = 0; i < accountAmount; i++) {
                SessionStorage.getAllUserTokensFromStorage().forEach(token -> {
                    UserSteps.createUserAccount(token);
                    UserProfileResponse userProfileResponse = UserSteps.getUserInfo(token);
                    final List<UserAccountResponse> userAccountsResponse = UserSteps.getUserAccounts(token);
                    SessionStorage.replaceUserInfoInStorage(token, userProfileResponse, userAccountsResponse);
                });
            }
        }
    }
}
