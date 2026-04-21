package ui_tests.junior_ui_tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import config.UiPath;
import models.UserTransactionsResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import requests.steps.user_steps.UserSteps;
import utils.RandomData;

import java.util.List;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.visible;
import static pages.DepositPage.*;
import static requests.steps.user_steps.UserSteps.createUserAccount;

public class DepositTests extends BaseTestJunior {

    private double expectedRandomMoney = RandomData.getMoney();

    @Test
    @DisplayName("Позитивный тест: пользователь пополняет свой аккаунт валидной суммой")
    public void userCanDepositHisAccount() {

        int expectedListSize = 2;

        //Открываем страницу выполнения депозита
        Selenide.open(UiPath.DEPOSIT);

        // Проверяем лого страницы Deposit Money
        depositPage.getDepositTitle().shouldBe(visible);

        // Проверяем значение по-умолчанию в выпадающем списке Select Account:
        depositPage.getAccountSelector().options().findBy(Condition.exactText(DEFAULT_TEXT_IN_ACCOUNT_LIST_SELECTOR)).shouldBe(visible);

        // Проверяем размер списка
        depositPage.getAccountSelector().options().shouldHave(size(expectedListSize));

        // Открываем выпадающий список
        depositPage.getAccountSelector().click();

        // Выбираем созданный аккаунт
        depositPage.getAccountSelector().selectOptionByValue(String.valueOf(userAccount));

        // Проверяем отображение ID аккаунта и баланса аккаунта пользователя по-умолчанию
        final String actualAccountInfoInList = depositPage.getAccountSelector().getSelectedOptionText();
        final String expectedAccountInfoInList = getAccountInfoList(authUserToken, userAccount);
        Assertions.assertEquals(expectedAccountInfoInList, actualAccountInfoInList);

        // Проверяем отображение placeholder в поле Enter amount
        depositPage.getAccountAmount().shouldBe(visible);

        // Вводим рандомное значение денег для пополнения аккаунта
        depositPage.getAccountAmount().click();
        depositPage.getAccountAmount().setValue(String.valueOf(expectedRandomMoney));

        // Нажимаем кнопку Deposit для пополнения аккаунта
        depositPage.getDepositButton().click();

        // Проверяем сообщение в модальном окне об успешности выполнения Deposit
        final String actualAlertText = getActualTextFromModalPage();
        final String expectedAlertText = depositPage.expectedSuccessfulDepositModalMessage(expectedRandomMoney, userAccount);
        Assertions.assertEquals(expectedAlertText, actualAlertText);

        // Проверяем, что произошёл переход на главную страницу после выполнения Deposit
        mainPage.getMainTitle().shouldBe(visible);

        // Проверяем через api, что операция Deposit выполнена успешно
        final double actualUserBalance = UserSteps.getUserBalance(authUserToken, userAccount);
        Assertions.assertEquals(expectedRandomMoney, actualUserBalance);

    }

    @Test
    @DisplayName("Позитивный тест: пользователь может положить деньги на свои любые аккаунты")
    public void userCanDepositMoneyIntoHisDiffAccounts() {

        int expectedListSize = 3;

        //Открываем страницу выполнения депозита
        Selenide.open(UiPath.DEPOSIT);

        //Создаём второй аккаунт для пользователя
        final int secondUserAccount = createUserAccount(authUserToken);

        // Проверяем лого страницы Deposit Money
        depositPage.getDepositTitle().shouldBe(visible);

        // Проверяем значение по-умолчанию в выпадающем списке Select Account:
        depositPage.getAccountSelector().options().findBy(Condition.exactText(DEFAULT_TEXT_IN_ACCOUNT_LIST_SELECTOR)).shouldBe(visible);

        // Проверяем размер списка
        depositPage.getAccountSelector().options().shouldHave(size(expectedListSize));

        // Открываем выпадающий список
        depositPage.getAccountSelector().click();

        // Выбираем первый аккаунт
        depositPage.getAccountSelector().selectOptionByValue(String.valueOf(userAccount));

        // Проверяем отображение ID аккаунта и баланса аккаунта пользователя по-умолчанию
        final String actualAccountInfoInList = depositPage.getAccountSelector().getSelectedOptionText();
        final String expectedAccountInfoInList = getAccountInfoList(authUserToken, userAccount);
        Assertions.assertEquals(expectedAccountInfoInList, actualAccountInfoInList);

        // Проверяем отображение placeholder в поле Enter amount
        depositPage.getAccountAmount().shouldBe(visible);

        // Вводим рандомное значение денег для пополнения аккаунта
        depositPage.getAccountAmount().click();
        depositPage.getAccountAmount().setValue(String.valueOf(expectedRandomMoney));

        // Нажимаем кнопку Deposit для пополнения аккаунта
        depositPage.getDepositButton().click();

        // Проверяем сообщение в модальном окне об успешности выполнения Deposit
        final String actualAlertText = getActualTextFromModalPage();
        final String expectedAlertText = depositPage.expectedSuccessfulDepositModalMessage(expectedRandomMoney, userAccount);
        Assertions.assertEquals(expectedAlertText, actualAlertText);

        // Проверяем, что произошёл переход на главную страницу после выполнения Deposit
        mainPage.getMainTitle().shouldBe(visible);

        Selenide.open(UiPath.DEPOSIT);

        // Проверяем лого страницы Deposit Money
        depositPage.getDepositTitle().shouldBe(visible);

        // Открываем выпадающий список
        depositPage.getAccountSelector().click();

        // Выбираем второй аккаунт
        depositPage.getAccountSelector().selectOptionByValue(String.valueOf(secondUserAccount));

        // Проверяем отображение ID аккаунта и баланса аккаунта пользователя по-умолчанию
        final String actualSecondAccountInfoInList = depositPage.getAccountSelector().getSelectedOptionText();
        final String expectedSecondAccountInfoInList = getAccountInfoList(authUserToken, secondUserAccount);
        Assertions.assertEquals(expectedSecondAccountInfoInList, actualSecondAccountInfoInList);

        // Вводим рандомное значение денег для пополнения аккаунта
        depositPage.getAccountAmount().click();
        depositPage.getAccountAmount().setValue(String.valueOf(expectedRandomMoney));

        // Нажимаем кнопку Deposit для пополнения аккаунта
        depositPage.getDepositButton().click();

        // Проверяем сообщение в модальном окне об успешности выполнения Deposit
        final String actualSecondAlertText = getActualTextFromModalPage();
        final String expectedSecondAlertText = depositPage.expectedSuccessfulDepositModalMessage(expectedRandomMoney, secondUserAccount);
        Assertions.assertEquals(expectedSecondAlertText, actualSecondAlertText);

        // Проверяем, что произошёл переход на главную страницу после выполнения Deposit
        mainPage.getMainTitle().shouldBe(visible);

        // Проверяем через api, что операция Deposit для первого аккаунта выполнена успешно
        final double actualFirstAccountBalance = UserSteps.getUserBalance(authUserToken, userAccount);
        Assertions.assertEquals(expectedRandomMoney, actualFirstAccountBalance);

        // Проверяем через api, что операция Deposit для второго аккаунта выполнена успешно
        final double actualSecondAccountBalance = UserSteps.getUserBalance(authUserToken, secondUserAccount);
        Assertions.assertEquals(expectedRandomMoney, actualSecondAccountBalance);

    }

    @Test
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке пополнить свой аккаунт суммой меньше 0.01")
    public void userSeesErrorMessageWhenDepositHisAccountWithLessThanMiniumLimitValue() {

        double negativeMoneyValue = -0.01;
        double zeroBalance = 0.00;
        int expectedListSize = 2;

        //Открываем страницу выполнения депозита
        Selenide.open(UiPath.DEPOSIT);

        // Проверяем лого страницы Deposit Money
        depositPage.getDepositTitle().shouldBe(visible);

        // Проверяем значение по-умолчанию в выпадающем списке Select Account:
        depositPage.getAccountSelector().options().findBy(Condition.exactText(DEFAULT_TEXT_IN_ACCOUNT_LIST_SELECTOR)).shouldBe(visible);

        // Проверяем размер списка
        depositPage.getAccountSelector().options().shouldHave(size(expectedListSize));

        // Открываем выпадающий список
        depositPage.getAccountSelector().click();

        // Выбираем созданный аккаунт
        depositPage.getAccountSelector().selectOptionByValue(String.valueOf(userAccount));

        // Проверяем отображение ID аккаунта и баланса аккаунта пользователя по-умолчанию
        final String actualAccountInfoInList = depositPage.getAccountSelector().getSelectedOptionText();
        final String expectedAccountInfoInList = getAccountInfoList(authUserToken, userAccount);
        Assertions.assertEquals(expectedAccountInfoInList, actualAccountInfoInList);

        // Проверяем отображение placeholder в поле Enter amount
        depositPage.getAccountAmount().shouldBe(visible);

        // Вводим отрицательное -0.01 значение денег для пополнения аккаунта
        depositPage.getAccountAmount().click();
        depositPage.getAccountAmount().setValue(String.valueOf(negativeMoneyValue));

        // Нажимаем кнопку Deposit для пополнения аккаунта
        depositPage.getDepositButton().click();

        // Проверяем сообщение в модальном окне об ошибке выполнения Deposit
        final String actualAlertText = getActualTextFromModalPage();
        Assertions.assertEquals(DEPOSIT_ERROR_NEGATIVE_VALUE, actualAlertText);

        // Проверяем, что остались на той же странице
        depositPage.getDepositTitle().shouldBe(visible);

        // Проверяем через api, что операция Deposit не выполнена
        final double actualUserBalance = UserSteps.getUserBalance(authUserToken, userAccount);
        Assertions.assertEquals(zeroBalance, actualUserBalance);

    }

    @Test
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке пополнить свой аккаунт суммой больше 5000")
    public void userSeesErrorMessageWhenDepositHisAccountWithValueMoreThanMaximum5000() {

        double maximumValue = 5000.01;
        double zeroBalance = 0.00;
        int expectedListSize = 2;

        //Открываем страницу выполнения депозита
        Selenide.open(UiPath.DEPOSIT);

        // Проверяем лого страницы Deposit Money
        depositPage.getDepositTitle().shouldBe(visible);

        // Проверяем значение по-умолчанию в выпадающем списке Select Account:
        depositPage.getAccountSelector().options().findBy(Condition.exactText(DEFAULT_TEXT_IN_ACCOUNT_LIST_SELECTOR)).shouldBe(visible);

        // Проверяем размер списка
        depositPage.getAccountSelector().options().shouldHave(size(expectedListSize));

        // Открываем выпадающий список
        depositPage.getAccountSelector().click();

        // Выбираем созданный аккаунт
        depositPage.getAccountSelector().selectOptionByValue(String.valueOf(userAccount));

        // Проверяем отображение ID аккаунта и баланса аккаунта пользователя по-умолчанию
        final String actualAccountInfoInList = depositPage.getAccountSelector().getSelectedOptionText();
        final String expectedAccountInfoInList = getAccountInfoList(authUserToken, userAccount);
        Assertions.assertEquals(expectedAccountInfoInList, actualAccountInfoInList);

        // Проверяем отображение placeholder в поле Enter amount
        depositPage.getAccountAmount().shouldBe(visible);

        // Вводим значение больше максимального допустимого значение денег для пополнения аккаунта
        depositPage.getAccountAmount().click();
        depositPage.getAccountAmount().setValue(String.valueOf(maximumValue));

        // Нажимаем кнопку Deposit для пополнения аккаунта
        depositPage.getDepositButton().click();

        // Проверяем сообщение в модальном окне об ошибке выполнения Deposit
        final String actualAlertText = getActualTextFromModalPage();
        Assertions.assertEquals(DEPOSIT_ERROR_EXCEEDED_MAXIMUM_VALUE, actualAlertText);

        // Проверяем, что остались на той же странице
        depositPage.getDepositTitle().shouldBe(visible);

        // Проверяем через api, что операция Deposit не выполнена
        final double actualUserBalance = UserSteps.getUserBalance(authUserToken, userAccount);
        Assertions.assertEquals(zeroBalance, actualUserBalance);

    }

    @Test
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке нажатия 'Deposit' без выбора аккаунта")
    public void userSeesErrorMessageWhenClickDepositButtonWithoutAccount() {

        int expectedListSize = 2;

        //Открываем страницу выполнения депозита
        Selenide.open(UiPath.DEPOSIT);

        // Проверяем лого страницы Deposit Money
        depositPage.getDepositTitle().shouldBe(visible);

        // Не выбираем никакой аккаунт и не вводим сумму для пополнения.
        // Нажимаем кнопку Deposit для пополнения аккаунта
        depositPage.getDepositButton().click();

        // Проверяем сообщение в модальном окне об ошибке выполнения Deposit
        final String actualAlertText = getActualTextFromModalPage();
        Assertions.assertEquals(DEPOSIT_ERROR_WITHOUT_REQUIRED_FIELDS, actualAlertText);

        // Проверяем, что остались на той же странице
        depositPage.getDepositTitle().shouldBe(visible);

    }

    @Test
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке нажатия 'Deposit' без указания суммы")
    public void userSeesErrorMessageWhenClickDepositButtonWithoutAmount() {

        //Открываем страницу выполнения депозита
        Selenide.open(UiPath.DEPOSIT);

        // Проверяем лого страницы Deposit Money
        depositPage.getDepositTitle().shouldBe(visible);

        // Открываем выпадающий список
        depositPage.getAccountSelector().click();

        // Выбираем созданный аккаунт
        depositPage.getAccountSelector().selectOptionByValue(String.valueOf(userAccount));

        // Проверяем отображение ID аккаунта и баланса аккаунта пользователя по-умолчанию
        final String actualAccountInfoInList = depositPage.getAccountSelector().getSelectedOptionText();
        final String expectedAccountInfoInList = getAccountInfoList(authUserToken, userAccount);
        Assertions.assertEquals(expectedAccountInfoInList, actualAccountInfoInList);

        // Не вводим сумму для пополнения.
        // Нажимаем кнопку Deposit для пополнения аккаунта
        depositPage.getDepositButton().click();

        // Проверяем сообщение в модальном окне об ошибке выполнения Deposit
        final String actualAlertText = getActualTextFromModalPage();
        Assertions.assertEquals(DEPOSIT_ERROR_WITHOUT_AMOUNT, actualAlertText);

        // Проверяем, что остались на той же странице
        depositPage.getDepositTitle().shouldBe(visible);

        // Проверяем через api, что операция Deposit не выполнена
        final List<UserTransactionsResponse> userTransactions = UserSteps.getUserTransactions(authUserToken, userAccount);
        Assertions.assertTrue(userTransactions.isEmpty());

    }

    @Test
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке нажатия 'Deposit' с пустым аккаунтом, " +
            "хотя ранее он был выбран")
    public void userSeesErrorMessageWhenClickDepositButtonWithoutAccountWhenAccountWasChooseBefore() {

        //Открываем страницу выполнения депозита
        Selenide.open(UiPath.DEPOSIT);

        // Проверяем лого страницы Deposit Money
        depositPage.getDepositTitle().shouldBe(visible);

        // Открываем выпадающий список
        depositPage.getAccountSelector().click();

        // Выбираем созданный аккаунт
        depositPage.getAccountSelector().selectOptionByValue(String.valueOf(userAccount));

        // Вновь открываем выпадающий список
        depositPage.getAccountSelector().click();

        // Теперь выбираем дефолтное значение в списке
        depositPage.getAccountSelector().selectOption(DEFAULT_TEXT_IN_ACCOUNT_LIST_SELECTOR);

        // Проверяем отображение ID аккаунта и баланса аккаунта пользователя по-умолчанию
        final String actualAccountInfoInList = depositPage.getAccountSelector().getSelectedOptionText();
        Assertions.assertEquals(DEFAULT_TEXT_IN_ACCOUNT_LIST_SELECTOR, actualAccountInfoInList);

        // Проверяем отображение placeholder в поле Enter amount
        depositPage.getAccountAmount().shouldBe(visible);

        // Вводим рандомное значение денег для пополнения аккаунта
        depositPage.getAccountAmount().click();
        depositPage.getAccountAmount().setValue(String.valueOf(expectedRandomMoney));

        // Нажимаем кнопку Deposit для пополнения аккаунта
        depositPage.getDepositButton().click();

        // Проверяем сообщение в модальном окне об ошибке выполнения Deposit
        final String actualAlertText = getActualTextFromModalPage();
        Assertions.assertEquals(DEPOSIT_ERROR_WITHOUT_REQUIRED_FIELDS, actualAlertText);

        // Проверяем, что остались на той же странице
        depositPage.getDepositTitle().shouldBe(visible);

        // Проверяем через api, что операция Deposit не выполнена
        final List<UserTransactionsResponse> userTransactions = UserSteps.getUserTransactions(authUserToken, userAccount);
        Assertions.assertTrue(userTransactions.isEmpty());

    }
}
