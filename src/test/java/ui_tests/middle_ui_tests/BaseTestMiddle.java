package ui_tests.middle_ui_tests;

import api.config.Config;
import api_tests.iteraion2_senior.BaseTestSenior;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.Alert;
import ui.pages.DepositPage;
import ui.pages.MainPage;
import ui.pages.TransferPage;
import ui.pages.UserProfilePage;

import java.util.Locale;
import java.util.Map;

import static api.config.AccountData.ACCOUNT_NUMBER_PREFIX;
import static api.requests.steps.user_steps.UserSteps.getUserBalance;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.switchTo;
import static ui.pages.BasePage.putTokenIntoStorage;

public class BaseTestMiddle extends BaseTestSenior {
    @BeforeAll
    public static void setupSelenoid(){
        Configuration.remote = Config.getProperty("remote_host");
        Configuration.baseUrl = Config.getProperty("ui_baseurl");
        Configuration.browser = Config.getProperty("browser");
        Configuration.browserSize = Config.getProperty("resolution");
        Configuration.browserCapabilities.setCapability("selenoid:options",
                Map.of("enableVNC", true, "enableLog", true)
        );
    }

    protected MainPage mainPage;
    protected UserProfilePage userProfilePage;
    protected TransferPage transferPage;
    protected DepositPage depositPage;

    @BeforeEach
    public void setUpUiTests() {
        // Сохраняем токен пользователя в localStorage
        putTokenIntoStorage(authUserToken);

        mainPage = new MainPage();
        userProfilePage = new UserProfilePage();
        transferPage = new TransferPage();
        depositPage = new DepositPage();
    }

//    public String getAccountInfoList(String userToken, int userAccount){
//        final double userBalance = getUserBalance(userToken, userAccount);
//        return ACCOUNT_NUMBER_PREFIX.getValue() + userAccount +
//                " (Balance: $" + String.format(Locale.US,"%.2f", userBalance) + ")";
//    }
}
