package ui_tests.senior_ui_tests;

import api.config.Config;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import common.extensions.AdminSessionExtension;
import common.extensions.BrowserAnnotationExtension;
import common.extensions.BugExtension;
import common.extensions.UserSessionExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import ui.pages.DepositPage;
import ui.pages.MainPage;
import ui.pages.TransferPage;
import ui.pages.UserProfilePage;

import java.util.Map;

@ExtendWith(AdminSessionExtension.class)
@ExtendWith(UserSessionExtension.class)
@ExtendWith(BrowserAnnotationExtension.class)
@ExtendWith(BugExtension.class)

public class BaseTestSenior extends api_tests.iteraion2_senior.BaseTestSenior {
    @BeforeAll
    public static void setupSelenoid() {
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
        mainPage = new MainPage();
        userProfilePage = new UserProfilePage();
        transferPage = new TransferPage();
        depositPage = new DepositPage();
    }

    @AfterEach
    public void tearDownUiTests() {
        Selenide.closeWebDriver();
    }
}
