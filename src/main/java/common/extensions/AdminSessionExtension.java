package common.extensions;

import api.requests.steps.admin_steps.AdminSteps;
import common.SessionStorage;
import common.annotations.AdminSession;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import ui.pages.BasePage;

public class AdminSessionExtension implements BeforeEachCallback {
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AdminSession adminSession = context.getRequiredTestMethod().getAnnotation(AdminSession.class);
        if (adminSession != null) {
            SessionStorage.clearUsersInfoStorageMap();
            int userAmount = adminSession.amountUsers();
            for (int i = 0; i < userAmount; i++) {
                final String userToken = AdminSteps.createUserAndGetToken();
                SessionStorage.addUserInfoToStorage(userToken, null);
            }

            final int userMainNumber = adminSession.mainUserNumberToPutInStorage();
            BasePage.putTokenIntoStorage(SessionStorage.getUserTokenFromStorage(userMainNumber));
        }
    }
}
