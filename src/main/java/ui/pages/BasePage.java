package ui.pages;

import api.dao.jpa.entities.AccountsEntity;
import api.models.UserAccountResponse;
import api.requests.steps.db_steps.DBSteps;
import api.requests.steps.user_steps.UserSteps;
import com.codeborne.selenide.*;
import common.StepLogger;
import common.retry.RetryUtils;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
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
        return StepLogger.log("Открываем сайт", () -> Selenide.open(url(), (Class<T>) this.getClass()));
    }

    public <T extends BasePage> T getPage(Class<T> classPage) {

        return StepLogger.log("Переходим на страницу", () -> Selenide.page(classPage));
    }

    @Step("Сохраняем токен пользователя в storage")
    public static void putTokenIntoStorage(String authToken) {
        Selenide.open("/");
        executeJavaScript(
                "window.localStorage.setItem(arguments[0], arguments[1]);",
                "authToken",
                authToken
        );
    }

    @Step("Вводим значение количества денег")
    public T inputAmountValue(double value) {
        amountField.shouldBe(Condition.visible).click();
        amountField.setValue(String.valueOf(value));
        return (T) this;
    }

    @Step("Проверяем введённое значение количества денег")
    public T checkAmountDefaultValue() {
        amountField.shouldBe(Condition.visible);
        amountField.shouldHave(Condition.exactValue(""));
        return (T) this;
    }

    @Step("Проверяем, что введённое ранее значение количества денег не изменилось")
    public T checkAmountValueDoesntChange(double value) {
        amountField.shouldBe(Condition.visible);
        amountField.shouldHave(Condition.exactValue(String.valueOf(value)));
        return (T) this;
    }

    @Step("Проверяем сообщение об успешности операции в модальном окне: {messageText}")
    public T checkMessageFromModalPageAndAccept(String messageText) {
        Alert alert = RetryUtils.retry(
                () -> {
                    try {
                        return switchTo().alert();
                    } catch (NoAlertPresentException e) {
                        return null;
                    }
                },
                result -> result != null);
        final String actualAlertText = alert.getText();
        assertThat(actualAlertText).isEqualTo(messageText);
        alert.accept();
        return (T) this;
    }

    @Deprecated
    public String expectedSuccessfullyDepositModalMessageOld(double money, int userAccount) {
        return "✅ Successfully deposited $" + money + " to account " + ACCOUNT_NUMBER_PREFIX.getValue() + userAccount + "!";
    }

    @Step("Получаем сообщение об успешном выполненном депозите для дальнейшего сравнения с реальным результатом")
    public String expectedSuccessfullyDepositModalMessage(double money, int userAccount) {
        final AccountsEntity accountByAccountIdJPA = DBSteps.getAccountByAccountIdJPA(userAccount);
        return "✅ Successfully deposited $" + money + " to account " + accountByAccountIdJPA.getAccountNumber() + "!";
    }

    @Deprecated
    public static String getAccountInfoListOld(String userToken, int userAccount) {
        final double userBalance = getUserBalance(userToken, userAccount);
        return ACCOUNT_NUMBER_PREFIX.getValue() + userAccount +
                " (Balance: $" + String.format(Locale.US, "%.2f", userBalance) + ")";
    }

    @Step("Получаем информацию об аккаунте: {userAccountNumber}")
    public static String getAccountInfoList(String userToken, String userAccountNumber) {
        final List<UserAccountResponse> userAccounts = UserSteps.getUserAccounts(userToken);
        final UserAccountResponse userAccountResponse = userAccounts
                .stream()
                .filter(accInfo -> accInfo.getAccountNumber()
                        .equals(userAccountNumber))
                .findFirst()
                .orElseThrow();
        return userAccountResponse.getAccountNumber() +
                " (Balance: $" + String.format(Locale.US, "%.2f", userAccountResponse.getBalance()) + ")";
    }

    @Deprecated
    public T checkSelectedAccountInListOld(String userToken, int userAccount) {
        final String actualAccountInfoInList = getAccountSelector().getSelectedOptionText();
        final String expectedAccountInfoInList = getAccountInfoListOld(userToken, userAccount);
        assertThat(actualAccountInfoInList).isEqualTo(expectedAccountInfoInList);
        return (T) this;
    }

    @Step("Проверяем, что выбран {userAccountNumber} аккаунт в списке всех аккаунтов")
    public T checkSelectedAccountInList(String userToken, String userAccountNumber) {
        final String actualAccountInfoInList = getAccountSelector().getSelectedOptionText();
        final String expectedAccountInfoInList = getAccountInfoList(userToken, userAccountNumber);
        assertThat(actualAccountInfoInList).isEqualTo(expectedAccountInfoInList);
        return (T) this;
    }

    @Step("Проверяем дефолтное значение в списке всех аккаунтов")
    public T checkDefaultValueInAccountList() {
        getAccountSelector().options().findBy(Condition.exactText(DEFAULT_TEXT_IN_ACCOUNT_LIST_SELECTOR))
                .shouldBe(Condition.visible);
        return (T) this;
    }

    @Step("Проверяем количество аккаунтов в списке всех аккаунтов")
    public T checkAccountSize(int expectedAmount) {
        getAccountSelector().options().shouldHave(size(expectedAmount));
        return (T) this;
    }

    @Step("Выделяем аккаунт {account}")
    public T selectAccount(int account) {
        getAccountSelector().click();
        getAccountSelector().selectOptionByValue(String.valueOf(account));
        return (T) this;
    }

    @Step("Выделяем аккаунт {account}")
    public T selectAccount(String account) {
        getAccountSelector().click();
        getAccountSelector().selectOptionContainingText(account);
        return (T) this;
    }

    @Step("Получаем информацию о выбранном аккаунте")
    public String getActualAccountInfoInListTransfer(){
        return getAccountSelector().getSelectedOptionText();
    }

    @Step("Проверяем дефолтное значение в списке всех аккаунтов")
    public T selectDefaultValueInAccountList() {
        getAccountSelector().selectOption(DEFAULT_TEXT_IN_ACCOUNT_LIST_SELECTOR);
        return (T) this;
    }

    @Step("Проверяем, что ранее выбранный аккаунт {expectedAccountInfoInList} в списке не сбросился")
    public T checkSelectedAccountDoesntChange(String expectedAccountInfoInList) {
        final String actualAccountInfoInListAfterTransfer = getAccountSelector().getSelectedOptionText();
        Assertions.assertEquals(expectedAccountInfoInList, actualAccountInfoInListAfterTransfer);
        return (T) this;
    }

    @Step("Проверяем username пользователя {expectedName} на странице справа сверху")
    public T checkUsernameMainPageTopRight(String expectedName) {
        assertThat(userInfo.getText().toLowerCase()).isEqualTo(expectedName);
        return (T) this;
    }

    @Step("Нажимаем на кнопку Home")
    public T clickHomeButton() {
        homeButton.click();
        return (T) this;
    }

    public <T extends BaseElement> List<T> generateElementList(ElementsCollection elements,
                                                               Function<SelenideElement, T> constructor) {
        return elements.stream().map(constructor).toList();
    }

    @Step("Обновляем страницу")
    public T refreshPage() {
        Selenide.refresh();
        return (T) this;
    }
}
