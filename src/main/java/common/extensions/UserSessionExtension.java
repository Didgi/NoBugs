package common.extensions;

import api.models.UsersResponse;
import api.requests.steps.user_steps.UserSteps;
import common.SessionStorage;
import common.annotations.UserSession;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class UserSessionExtension implements BeforeEachCallback {
    @Override
    public void beforeEach(ExtensionContext context) {
        UserSession userSession = context.getRequiredTestMethod().getAnnotation(UserSession.class);
        if (userSession != null) {
            //получаем полученное количество создаваемых аккаунтов для каждого пользователя
            final int accountAmount = userSession.amountAccounts();
            //создаём полученное количество аккаунтов для каждого пользователя по его токену
            for (int i = 0; i < accountAmount; i++) {
                SessionStorage.getAllUserTokensFromStorage().forEach(token -> {
                    UserSteps.createUserAccount(token);
                    final UsersResponse userInfo = UserSteps.getUserInfo(token);
                    SessionStorage.replaceUserInfoInStorage(token, userInfo);
                });
            }
        }
    }
}
