package ui_tests.junior_ui_tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import config.Config;
import config.Operations;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.Alert;
import pages.DepositPage;
import pages.MainPage;
import pages.TransferPage;
import pages.UserProfilePage;
import requests.steps.user_steps.UserSteps;

import java.util.Locale;
import java.util.Map;

import static com.codeborne.selenide.Selenide.switchTo;
import static config.AccountData.ACCOUNT_NUMBER_PREFIX;
import static requests.steps.admin_steps.AdminSteps.createUserAndGetToken;
import static requests.steps.admin_steps.AdminSteps.deleteUsersById;
import static requests.steps.system_steps.SystemSteps.putTokenIntoStorage;
import static requests.steps.user_steps.UserSteps.*;

public class BaseTestJunior {
    @BeforeAll
    public static void basic(){
        Configuration.remote = Config.getProperty("remote_host");
        Configuration.baseUrl = Config.getProperty("ui_baseurl");
        Configuration.browser = Config.getProperty("browser");
        Configuration.browserSize = Config.getProperty("resolution");
        Configuration.browserCapabilities.setCapability("selenoid:options",
                Map.of("enableVNC", true, "enableLog", true)
        );
    }

    protected static String authUserToken;
    protected static int userAccount;
    public static SoftAssertions softly;
    protected MainPage mainPage;
    protected UserProfilePage userProfilePage;
    protected TransferPage transferPage;
    protected DepositPage depositPage;

    @BeforeEach
    public void setUp() {
        softly = new SoftAssertions();
        UserSteps.SoftAssertions(softly);
        // 1. Создаём нового пользователя и получаем его токен
        authUserToken = createUserAndGetToken();
        // 2. Создаём аккаунт для пользователя
        userAccount = createUserAccount(authUserToken);
        // 3. Сохраняем токен пользователя в localStorage
        putTokenIntoStorage(authUserToken);

        mainPage = new MainPage();
        userProfilePage = new UserProfilePage();
        transferPage = new TransferPage();
        depositPage = new DepositPage();
    }

    @AfterEach
    public void cleanUp() {
        // 4. Удаляем все аккаунты пользователя
        deleteUserAccounts(authUserToken);
        // 5. Удаляем всех пользователей
        deleteUsersById();
        softly.assertAll();
    }

    public String getAccountInfoList(String userToken, int userAccount){
        final double userBalance = getUserBalance(userToken, userAccount);
        return ACCOUNT_NUMBER_PREFIX.getValue() + userAccount +
                " (Balance: $" + String.format(Locale.US,"%.2f", userBalance) + ")";
    }

    public String getActualTextFromModalPage() {
        Alert alert = switchTo().alert();
        final String actualAlertText = alert.getText();
        alert.accept();
        return actualAlertText;
    }
}
