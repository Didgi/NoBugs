package ui_tests;

import api.config.Config;
import api_tests.BaseTestSenior;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import common.extensions.*;
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
@ExtendWith(TimingExtension.class)

public class UIBaseTestSenior extends BaseTestSenior {

    @BeforeAll
    public static void setupSelenoid() {
        Configuration.remote = Config.getProperty("remote_host");
        Configuration.baseUrl = Config.getProperty("ui_baseurl");
        Configuration.browser = Config.getProperty("browser");
        Configuration.browserSize = Config.getProperty("resolution");
        Configuration.browserCapabilities.setCapability("selenoid:options",
                Map.of("enableVNC", Boolean.parseBoolean(Config.getProperty("enable_vnc")),
                        "enableLog", Boolean.parseBoolean(Config.getProperty("enable_log")),
                        "enableVideo", Boolean.parseBoolean(Config.getProperty("enable_recording_video")))
        );

        Configuration.headless = Boolean.parseBoolean(Config.getProperty("headless_mode"));
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
