package ui.pages;

import api.config.Config;
import com.codeborne.selenide.*;
import common.retry.RetryUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.Alert;
import ui.elements.BaseElement;

import java.util.List;
import java.util.Locale;
import java.util.function.Function;

import static api.config.AccountData.ACCOUNT_NUMBER_PREFIX;
import static api.requests.steps.user_steps.UserSteps.getUserBalance;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Selenide.*;
import static org.assertj.core.api.Assertions.assertThat;

@Getter
@NoArgsConstructor
public abstract class BasePage<T extends BasePage> {

    private final SelenideElement userInfo = $(".user-info .user-name");

    private final SelenideElement username = $(".user-username");

    private final SelenideElement homeButton = $(Selectors.byText("\uD83C\uDFE0 Home"));

    private final SelenideElement accountSelector = $(Selectors.byClassName("account-selector"));

    private final SelenideElement amountField = $(Selectors.byPlaceholder("Enter amount"));

    public static final String DEFAULT_TEXT_IN_ACCOUNT_LIST_SELECTOR = "-- Choose an account --";

    public abstract String url();

    public T open() {
        return Selenide.open(url(), (Class<T>) this.getClass());
    }

    public <T extends BasePage> T getPage(Class<T> classPage) {
        return Selenide.page(classPage);
    }

    public static void putTokenIntoStorage(String authToken) {
        Selenide.open("/");
        executeJavaScript(
                "window.localStorage.setItem(arguments[0], arguments[1]);",
                "authToken",
                authToken
        );
    }

    public T inputAmountValue(double value) {
        amountField.shouldBe(Condition.visible).click();
        amountField.setValue(String.valueOf(value));
        return (T) this;
    }

    public T checkAmountDefaultValue() {
        amountField.shouldBe(Condition.visible);
        amountField.shouldHave(Condition.exactValue(""));
        return (T) this;
    }

    public T checkAmountValueDoesntChange(double value) {
        amountField.shouldBe(Condition.visible);
        amountField.shouldHave(Condition.exactValue(String.valueOf(value)));
        return (T) this;
    }

    public T checkMessageFromModalPageAndAccept(String messageText) {
        Alert alert = RetryUtils.retry(
                () -> switchTo().alert(),
                result -> result != null,
                Integer.parseInt(Config.getProperty("max_retry_amounts")),
                Long.parseLong(Config.getProperty("timeout_mills"))
        );
        final String actualAlertText = alert.getText();
        assertThat(actualAlertText).isEqualTo(messageText);
        alert.accept();
        return (T) this;
    }

    public String expectedSuccessfullyDepositModalMessage(double money, int userAccount) {
        return "✅ Successfully deposited $" + money + " to account " + ACCOUNT_NUMBER_PREFIX.getValue() + userAccount + "!";
    }

    public static String getAccountInfoList(String userToken, int userAccount) {
        final double userBalance = getUserBalance(userToken, userAccount);
        return ACCOUNT_NUMBER_PREFIX.getValue() + userAccount +
                " (Balance: $" + String.format(Locale.US, "%.2f", userBalance) + ")";
    }

    public T checkSelectedAccountInList(String userToken, int userAccount) {
        final String actualAccountInfoInList = getAccountSelector().getSelectedOptionText();
        final String expectedAccountInfoInList = getAccountInfoList(userToken, userAccount);
        assertThat(actualAccountInfoInList).isEqualTo(expectedAccountInfoInList);
        return (T) this;
    }

    public T checkDefaultValueInAccountList() {
        getAccountSelector().options().findBy(Condition.exactText(DEFAULT_TEXT_IN_ACCOUNT_LIST_SELECTOR))
                .shouldBe(Condition.visible);
        return (T) this;
    }

    public T checkAccountSize(int expectedAmount) {
        getAccountSelector().options().shouldHave(size(expectedAmount));
        return (T) this;
    }

    public T selectAccount(int account) {
        getAccountSelector().click();
        getAccountSelector().selectOptionByValue(String.valueOf(account));
        return (T) this;
    }

    public T selectDefaultValueInAccountList() {
        getAccountSelector().selectOption(DEFAULT_TEXT_IN_ACCOUNT_LIST_SELECTOR);
        return (T) this;
    }

    public T checkSelectedAccountDoesntChange(String expectedAccountInfoInList) {
        final String actualAccountInfoInListAfterTransfer = getAccountSelector().getSelectedOptionText();
        Assertions.assertEquals(expectedAccountInfoInList, actualAccountInfoInListAfterTransfer);
        return (T) this;
    }

    public T checkUsernameMainPageTopRight(String expectedName) {
        assertThat(userInfo.getText().toLowerCase()).isEqualTo(expectedName);
        return (T) this;
    }

    public T clickHomeButton() {
        homeButton.click();
        return (T) this;
    }

    public <T extends BaseElement> List<T> generateElementList(ElementsCollection elements,
                                                               Function<SelenideElement, T> constructor) {
        return elements.stream().map(constructor).toList();
    }
}
