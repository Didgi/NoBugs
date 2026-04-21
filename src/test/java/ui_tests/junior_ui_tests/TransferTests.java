package ui_tests.junior_ui_tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import config.AccountData;
import config.Operations;
import config.UiPath;
import models.ChangeUserRequest;
import models.UsersResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import requests.skelethon.EndpointRequests;
import requests.skelethon.requesters.ValidatableCrudRequester;
import requests.steps.admin_steps.AdminSteps;
import requests.steps.user_steps.UserSteps;
import specs.RequestSpecs;
import specs.ResponseSpecs;
import utils.RandomData;
import utils.RandomModelGenerator;

import java.util.List;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static pages.TransferPage.*;

public class TransferTests extends BaseTestJunior {

    private String secondUserToken;
    private int accountSecondUser;

    @BeforeEach
    public void setUpTransfer() {
        secondUserToken = AdminSteps.createUserAndGetToken();
        accountSecondUser = UserSteps.createUserAccount(secondUserToken);
    }

    @Test
    @DisplayName("Позитивный тест: пользователь может переводить деньги на аккаунт другого пользователя")
    public void userCanTransferMoneyToSomeoneElseExistedAccount() {

        double expectedRandomMoney = RandomData.getMoney();
        int expectedListSize = 2;

        //Устанавливаем имя второго пользователю
        final ChangeUserRequest changeUserRequest = RandomModelGenerator.generate(ChangeUserRequest.class);

        new ValidatableCrudRequester<UsersResponse>(RequestSpecs.withToken(secondUserToken),
                EndpointRequests.UPDATE_USER, ResponseSpecs.requestReturnsOk()).PUT(changeUserRequest);

        //Пополняем аккаунт первого пользователя
        UserSteps.depositMoney(authUserToken, userAccount, expectedRandomMoney);

        //Переходим на страницу трансфера и проверяем, что отображается название этой страницы
        Selenide.open(UiPath.TRANSFER);
        transferPage.getTransferTitle().shouldBe(visible);
        transferPage.getAccountSelector().shouldBe(visible);

        // Проверяем значение по-умолчанию в выпадающем списке Select Account
        transferPage.getAccountSelector().options().findBy(exactText(DEFAULT_TEXT_IN_ACCOUNT_LIST_SELECTOR)).shouldBe(visible);

        //Проверяем размер списка
        transferPage.getAccountSelector().options().shouldHave(size(expectedListSize));

        //Выбираем единственный аккаунт пользователя
        transferPage.getAccountSelector().click();
        transferPage.getAccountSelector().selectOptionByValue(String.valueOf(userAccount));

        //Проверяем отображаемый аккаунт в списке
        final String actualAccountInfoInList = transferPage.getAccountSelector().getSelectedOptionText();
        final String expectedAccountInfoInList = getAccountInfoList(authUserToken, userAccount);
        Assertions.assertEquals(expectedAccountInfoInList, actualAccountInfoInList);

        // Проверяем отображение placeholder в поле Recipient Name
        transferPage.getRecipientName().shouldBe(visible);
        transferPage.getRecipientName().shouldHave(Condition.exactValue(""));

        //Указываем имя второго пользователя в поле Recipient Name
        transferPage.getRecipientName().setValue(changeUserRequest.getName());

        // Проверяем отображение placeholder в поле Recipient Account Number
        transferPage.getRecipientAccount().shouldBe(visible);
        transferPage.getRecipientAccount().shouldHave(Condition.exactValue(""));

        //Указываем аккаунт второго пользователя в поле Recipient Account Number
        transferPage.getRecipientAccount().setValue(AccountData.ACCOUNT_NUMBER_PREFIX.getValue() + accountSecondUser);

        // Проверяем отображение placeholder в поле Amount
        transferPage.getAmount().shouldBe(visible);
        transferPage.getAmount().shouldHave(Condition.exactValue(""));

        //Вводим сумму для перевода равную зачисленным деньгам в поле Amount
        transferPage.getAmount().setValue(String.valueOf(expectedRandomMoney));

        // Проверяем, что чекбокс Confirm details are correct по-умолчанию не активен
        transferPage.getConfirmDetailsCheckbox().shouldHave(visible);
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "false"));

        //Активируем чекбокс подтверждая данные
        transferPage.getConfirmDetailsCheckbox().click();
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "true"));

        //Нажимаем кнопку Transfer
        transferPage.getTransferButton().click();

        // Проверяем сообщение в модальном окне
        final String actualAlertText = getActualTextFromModalPage();
        final String expectedAlertText = transferPage.expectedSuccessfulTransferModalMessage(expectedRandomMoney, accountSecondUser);
        Assertions.assertEquals(expectedAlertText, actualAlertText);

        //Проверяем, что остались на той же странице
        transferPage.getTransferTitle().shouldBe(visible);

        //Проверяем, что в списке выбранный аккаунт не сброшен
        final String actualAccountInfoInListAfterTransfer = transferPage.getAccountSelector().getSelectedOptionText();
        Assertions.assertEquals(expectedAccountInfoInList, actualAccountInfoInListAfterTransfer);

        //Проверяем, что поле Recipient Name очистилось
        transferPage.getRecipientName().shouldHave(Condition.exactValue(""));

        //Проверяем, что поле Recipient Account Number очистилось
        transferPage.getRecipientAccount().shouldHave(Condition.exactValue(""));

        //Проверяем, что поле Amount очистилось
        transferPage.getAmount().shouldHave(Condition.exactValue(""));

        //Проверяем, что чекбокс Confirm details are correct не активен
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "false"));

        //Проверяем, что баланс аккаунта второго пользователя пополнился после перевода
        final double actualSecondUserBalance = UserSteps.getUserBalance(secondUserToken, accountSecondUser);
        Assertions.assertEquals(expectedRandomMoney, actualSecondUserBalance);

    }

    @Test
    @DisplayName("Позитивный тест: пользователь может переводить деньги между своими же аккаунтами")
    public void userCanTransferMoneyBetweenHisAccounts() {

        int expectedListSize = 3;

        //Создаём второй аккаунт для основного пользователя
        final int userAccountSecond = UserSteps.createUserAccount(authUserToken);

        //Пополняем первый аккаунт основного пользователя
        final double expectedRandomMoney = RandomData.getMoney();
        UserSteps.depositMoney(authUserToken, userAccount, expectedRandomMoney);

        //Переходим на страницу трансфера и проверяем, что отображается название этой страницы
        Selenide.open(UiPath.TRANSFER);

        transferPage.getTransferTitle().shouldBe(visible);
        transferPage.getAccountSelector().shouldBe(visible);

        // Проверяем значение по-умолчанию в выпадающем списке Select Account
        transferPage.getAccountSelector().options().findBy(exactText(DEFAULT_TEXT_IN_ACCOUNT_LIST_SELECTOR)).shouldBe(visible);

        //Проверяем размер списка
        transferPage.getAccountSelector().options().shouldHave(size(expectedListSize));

        //Выбираем первый аккаунт пользователя
        transferPage.getAccountSelector().click();
        transferPage.getAccountSelector().selectOptionByValue(String.valueOf(userAccount));

        //Проверяем отображаемый аккаунт в списке
        final String actualAccountInfoInList = transferPage.getAccountSelector().getSelectedOptionText();
        final String expectedAccountInfoInList = getAccountInfoList(authUserToken, userAccount);
        Assertions.assertEquals(expectedAccountInfoInList, actualAccountInfoInList);

        // Проверяем отображение placeholder в поле Recipient Name
        transferPage.getRecipientName().shouldBe(visible);
        transferPage.getRecipientName().shouldHave(Condition.exactValue(""));

        //Указываем рандомное имя пользователя в поле Recipient Name
        transferPage.getRecipientName().setValue(RandomData.randomName(3));

        // Проверяем отображение placeholder в поле Recipient Account Number
        transferPage.getRecipientAccount().shouldBe(visible);
        transferPage.getRecipientAccount().shouldHave(Condition.exactValue(""));

        //Указываем второй аккаунт пользователя в поле Recipient Account Number
        transferPage.getRecipientAccount().setValue(AccountData.ACCOUNT_NUMBER_PREFIX.getValue() + userAccountSecond);

        // Проверяем отображение placeholder в поле Amount
        transferPage.getAmount().shouldBe(visible);
        transferPage.getAmount().shouldHave(Condition.exactValue(""));

        //Вводим сумму для перевода равную зачисленным деньгам в поле Amount
        transferPage.getAmount().setValue(String.valueOf(expectedRandomMoney));

        // Проверяем, что чекбокс Confirm details are correct по-умолчанию не активен
        transferPage.getConfirmDetailsCheckbox().shouldHave(visible);
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "false"));

        //Активируем чекбокс подтверждая данные
        transferPage.getConfirmDetailsCheckbox().click();
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "true"));

        //Нажимаем кнопку Transfer
        transferPage.getTransferButton().click();

        // Проверяем сообщение в модальном окне
        final String actualAlertText = getActualTextFromModalPage();
        final String expectedAlertText = transferPage.expectedSuccessfulTransferModalMessage(expectedRandomMoney, userAccountSecond);
        Assertions.assertEquals(expectedAlertText, actualAlertText);

        //Проверяем, что остались на той же странице
        transferPage.getTransferTitle().shouldBe(visible);

        //Проверяем, что в списке выбранный аккаунт не сброшен
        final String actualAccountInfoInListAfterTransfer = transferPage.getAccountSelector().getSelectedOptionText();
        Assertions.assertEquals(expectedAccountInfoInList, actualAccountInfoInListAfterTransfer);

        //Проверяем, что поле Recipient Name очистилось
        transferPage.getRecipientName().shouldHave(Condition.exactValue(""));

        //Проверяем, что поле Recipient Account Number очистилось
        transferPage.getRecipientAccount().shouldHave(Condition.exactValue(""));

        //Проверяем, что поле Amount очистилось
        transferPage.getAmount().shouldHave(Condition.exactValue(""));

        //Проверяем, что чекбокс Confirm details are correct не активен
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "false"));

        //Проверяем, что баланс второго аккаунта пользователя пополнился после перевода
        final double actualSecondUserBalance = UserSteps.getUserBalance(authUserToken, userAccountSecond);
        Assertions.assertEquals(expectedRandomMoney, actualSecondUserBalance);
    }

    @Test
    @DisplayName("Позитивный тест: проверка возможности перевода денег на тот же аккаунт с которого происходит перевод")
    public void userCanTransferMoneyToSameAccount() {

        int expectedListSize = 2;

        //Пополняем аккаунт основного пользователя
        final double expectedRandomMoney = RandomData.getMoney();
        UserSteps.depositMoney(authUserToken, userAccount, expectedRandomMoney);

        //Переходим на страницу трансфера и проверяем, что отображается название этой страницы
        Selenide.open(UiPath.TRANSFER);

        transferPage.getTransferTitle().shouldBe(visible);
        transferPage.getAccountSelector().shouldBe(visible);

        // Проверяем значение по-умолчанию в выпадающем списке Select Account
        transferPage.getAccountSelector().options().findBy(exactText(DEFAULT_TEXT_IN_ACCOUNT_LIST_SELECTOR)).shouldBe(visible);

        //Проверяем размер списка
        transferPage.getAccountSelector().options().shouldHave(size(expectedListSize));

        //Выбираем аккаунт пользователя
        transferPage.getAccountSelector().click();
        transferPage.getAccountSelector().selectOptionByValue(String.valueOf(userAccount));

        //Проверяем отображаемый аккаунт в списке
        final String actualAccountInfoInList = transferPage.getAccountSelector().getSelectedOptionText();
        final String expectedAccountInfoInList = getAccountInfoList(authUserToken, userAccount);
        Assertions.assertEquals(expectedAccountInfoInList, actualAccountInfoInList);

        // Проверяем отображение placeholder в поле Recipient Name
        transferPage.getRecipientName().shouldBe(visible);
        transferPage.getRecipientName().shouldHave(Condition.exactValue(""));

        //Указываем рандомное имя пользователя в поле Recipient Name
        transferPage.getRecipientName().setValue(RandomData.randomName(3));

        // Проверяем отображение placeholder в поле Recipient Account Number
        transferPage.getRecipientAccount().shouldBe(visible);
        transferPage.getRecipientAccount().shouldHave(Condition.exactValue(""));

        //Указываем тот аккаунт пользователя в поле Recipient Account Number
        transferPage.getRecipientAccount().setValue(AccountData.ACCOUNT_NUMBER_PREFIX.getValue() + userAccount);

        // Проверяем отображение placeholder в поле Amount
        transferPage.getAmount().shouldBe(visible);
        transferPage.getAmount().shouldHave(Condition.exactValue(""));

        //Вводим сумму для перевода равную зачисленным деньгам в поле Amount
        transferPage.getAmount().setValue(String.valueOf(expectedRandomMoney));

        // Проверяем, что чекбокс Confirm details are correct по-умолчанию не активен
        transferPage.getConfirmDetailsCheckbox().shouldHave(visible);
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "false"));

        //Активируем чекбокс подтверждая данные
        transferPage.getConfirmDetailsCheckbox().click();
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "true"));

        //Нажимаем кнопку Transfer
        transferPage.getTransferButton().click();

        // Проверяем сообщение в модальном окне об успешности выполнения Deposit
        final String actualAlertText = getActualTextFromModalPage();
        final String expectedAlertText = transferPage.expectedSuccessfulTransferModalMessage(expectedRandomMoney, userAccount);
        Assertions.assertEquals(expectedAlertText, actualAlertText);

        //Проверяем, что остались на той же странице
        transferPage.getTransferTitle().shouldBe(visible);

        //Проверяем, что в списке выбранный аккаунт не сброшен
        final String actualAccountInfoInListAfterTransfer = transferPage.getAccountSelector().getSelectedOptionText();
        Assertions.assertEquals(expectedAccountInfoInList, actualAccountInfoInListAfterTransfer);

        //Проверяем, что поле Recipient Name очистилось
        transferPage.getRecipientName().shouldHave(Condition.exactValue(""));

        //Проверяем, что поле Recipient Account Number очистилось
        transferPage.getRecipientAccount().shouldHave(Condition.exactValue(""));

        //Проверяем, что поле Amount очистилось
        transferPage.getAmount().shouldHave(Condition.exactValue(""));

        //Проверяем, что чекбокс Confirm details are correct не активен
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "false"));

        //Проверяем, что баланс аккаунта пользователя не изменился после перевода самому себе
        final double actualSecondUserBalance = UserSteps.getUserBalance(authUserToken, userAccount);
        Assertions.assertEquals(expectedRandomMoney, actualSecondUserBalance);

    }

    @Test
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке перевода отрицательной суммы")
    public void userSeesErrorMessageWhenTransferMoneyLessThanMiniumLimitValue() {

        double expectedRandomMoney = RandomData.getMoney();
        double negativeMoney = -0.01;
        double expectedZeroBalance = 0.00;
        int expectedListSize = 2;

        //Устанавливаем имя второго пользователю
        final ChangeUserRequest changeUserRequest = RandomModelGenerator.generate(ChangeUserRequest.class);

        new ValidatableCrudRequester<UsersResponse>(RequestSpecs.withToken(secondUserToken),
                EndpointRequests.UPDATE_USER, ResponseSpecs.requestReturnsOk()).PUT(changeUserRequest);

        //Пополняем аккаунт первого пользователя
        UserSteps.depositMoney(authUserToken, userAccount, expectedRandomMoney);

        //Переходим на страницу трансфера и проверяем, что отображается название этой страницы
        Selenide.open(UiPath.TRANSFER);
        transferPage.getTransferTitle().shouldBe(visible);
        transferPage.getAccountSelector().shouldBe(visible);

        // Проверяем значение по-умолчанию в выпадающем списке Select Account
        transferPage.getAccountSelector().options().findBy(exactText(DEFAULT_TEXT_IN_ACCOUNT_LIST_SELECTOR)).shouldBe(visible);

        //Проверяем размер списка
        transferPage.getAccountSelector().options().shouldHave(size(expectedListSize));

        //Выбираем единственный аккаунт пользователя
        transferPage.getAccountSelector().click();
        transferPage.getAccountSelector().selectOptionByValue(String.valueOf(userAccount));

        //Проверяем отображаемый аккаунт в списке
        final String actualAccountInfoInList = transferPage.getAccountSelector().getSelectedOptionText();
        final String expectedAccountInfoInList = getAccountInfoList(authUserToken, userAccount);
        Assertions.assertEquals(expectedAccountInfoInList, actualAccountInfoInList);

        // Проверяем отображение placeholder в поле Recipient Name
        transferPage.getRecipientName().shouldBe(visible);
        transferPage.getRecipientName().shouldHave(Condition.exactValue(""));

        //Указываем имя второго пользователя в поле Recipient Name
        transferPage.getRecipientName().setValue(changeUserRequest.getName());

        // Проверяем отображение placeholder в поле Recipient Account Number
        transferPage.getRecipientAccount().shouldBe(visible);
        transferPage.getRecipientAccount().shouldHave(Condition.exactValue(""));

        //Указываем аккаунт второго пользователя в поле Recipient Account Number
        transferPage.getRecipientAccount().setValue(AccountData.ACCOUNT_NUMBER_PREFIX.getValue() + accountSecondUser);

        // Проверяем отображение placeholder в поле Amount
        transferPage.getAmount().shouldBe(visible);
        transferPage.getAmount().shouldHave(Condition.exactValue(""));

        //Вводим сумму для перевода равную зачисленным деньгам в поле Amount
        transferPage.getAmount().setValue(String.valueOf(negativeMoney));

        // Проверяем, что чекбокс Confirm details are correct по-умолчанию не активен
        transferPage.getConfirmDetailsCheckbox().shouldHave(visible);
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "false"));

        //Активируем чекбокс подтверждая данные
        transferPage.getConfirmDetailsCheckbox().click();
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "true"));

        //Нажимаем кнопку Transfer
        transferPage.getTransferButton().click();

        // Проверяем сообщение в модальном окне
        final String actualAlertText = getActualTextFromModalPage();
        Assertions.assertEquals(TRANSFER_ERROR_NEGATIVE_VALUE, actualAlertText);

        //Проверяем, что остались на той же странице
        transferPage.getTransferTitle().shouldBe(visible);

        //Проверяем, что в списке выбранный аккаунт не сброшен
        final String actualAccountInfoInListAfterTransfer = transferPage.getAccountSelector().getSelectedOptionText();
        Assertions.assertEquals(expectedAccountInfoInList, actualAccountInfoInListAfterTransfer);

        //Проверяем, что значение в поле Recipient Name не сброшено
        transferPage.getRecipientName().setValue(changeUserRequest.getName());

        //Проверяем, что значение в поле Recipient Account Number не сброшено
        transferPage.getRecipientAccount().setValue(AccountData.ACCOUNT_NUMBER_PREFIX.getValue() + accountSecondUser);

        //Проверяем, что значение в поле Amount не сброшено
        transferPage.getAmount().setValue(String.valueOf(negativeMoney));

        //Проверяем, что чекбокс Confirm details are correct остался активен
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "true"));

        //Проверяем, что баланс аккаунта первого пользователя не изменился
        final double actualUserBalance = UserSteps.getUserBalance(authUserToken, userAccount);
        Assertions.assertEquals(expectedRandomMoney, actualUserBalance);

        //Проверяем, что баланс аккаунта второго пользователя не изменился
        final double actualSecondUserBalance = UserSteps.getUserBalance(secondUserToken, accountSecondUser);
        Assertions.assertEquals(expectedZeroBalance, actualSecondUserBalance);
    }

    @Test
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке перевода суммы больше допустимой 10000")
    public void userSeesErrorMessageWhenTransferMoneyMoreThanMaximumLimitValue() {

        double expectedRandomMoney = RandomData.getMoney();
        double moreMaximumLimitValueMoney = 10000.01;
        double expectedZeroBalance = 0.00;
        int expectedListSize = 2;

        //Устанавливаем имя второго пользователю
        final ChangeUserRequest changeUserRequest = RandomModelGenerator.generate(ChangeUserRequest.class);

        new ValidatableCrudRequester<UsersResponse>(RequestSpecs.withToken(secondUserToken),
                EndpointRequests.UPDATE_USER, ResponseSpecs.requestReturnsOk()).PUT(changeUserRequest);

        //Пополняем аккаунт первого пользователя
        UserSteps.depositMoney(authUserToken, userAccount, expectedRandomMoney);

        //Переходим на страницу трансфера и проверяем, что отображается название этой страницы
        Selenide.open(UiPath.TRANSFER);
        transferPage.getTransferTitle().shouldBe(visible);
        transferPage.getAccountSelector().shouldBe(visible);

        // Проверяем значение по-умолчанию в выпадающем списке Select Account
        transferPage.getAccountSelector().options().findBy(exactText(DEFAULT_TEXT_IN_ACCOUNT_LIST_SELECTOR)).shouldBe(visible);

        //Проверяем размер списка
        transferPage.getAccountSelector().options().shouldHave(size(expectedListSize));

        //Выбираем единственный аккаунт пользователя
        transferPage.getAccountSelector().click();
        transferPage.getAccountSelector().selectOptionByValue(String.valueOf(userAccount));

        //Проверяем отображаемый аккаунт в списке
        final String actualAccountInfoInList = transferPage.getAccountSelector().getSelectedOptionText();
        final String expectedAccountInfoInList = getAccountInfoList(authUserToken, userAccount);
        Assertions.assertEquals(expectedAccountInfoInList, actualAccountInfoInList);

        // Проверяем отображение placeholder в поле Recipient Name
        transferPage.getRecipientName().shouldBe(visible);
        transferPage.getRecipientName().shouldHave(Condition.exactValue(""));

        //Указываем имя второго пользователя в поле Recipient Name
        transferPage.getRecipientName().setValue(changeUserRequest.getName());

        // Проверяем отображение placeholder в поле Recipient Account Number
        transferPage.getRecipientAccount().shouldBe(visible);
        transferPage.getRecipientAccount().shouldHave(Condition.exactValue(""));

        //Указываем аккаунт второго пользователя в поле Recipient Account Number
        transferPage.getRecipientAccount().setValue(AccountData.ACCOUNT_NUMBER_PREFIX.getValue() + accountSecondUser);

        // Проверяем отображение placeholder в поле Amount
        transferPage.getAmount().shouldBe(visible);
        transferPage.getAmount().shouldHave(Condition.exactValue(""));

        //Вводим сумму для перевода равную зачисленным деньгам в поле Amount
        transferPage.getAmount().setValue(String.valueOf(moreMaximumLimitValueMoney));

        // Проверяем, что чекбокс Confirm details are correct по-умолчанию не активен
        transferPage.getConfirmDetailsCheckbox().shouldHave(visible);
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "false"));

        //Активируем чекбокс подтверждая данные
        transferPage.getConfirmDetailsCheckbox().click();
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "true"));

        //Нажимаем кнопку Transfer
        transferPage.getTransferButton().click();

        // Проверяем сообщение в модальном окне об успешности выполнения Deposit
        final String actualAlertText = getActualTextFromModalPage();
        Assertions.assertEquals(TRANSFER_ERROR_EXCEEDED_MAXIMUM_VALUE, actualAlertText);

        //Проверяем, что остались на той же странице
        transferPage.getTransferTitle().shouldBe(visible);

        //Проверяем, что в списке выбранный аккаунт не сброшен
        final String actualAccountInfoInListAfterTransfer = transferPage.getAccountSelector().getSelectedOptionText();
        Assertions.assertEquals(expectedAccountInfoInList, actualAccountInfoInListAfterTransfer);

        //Проверяем, что значение в поле Recipient Name не сброшено
        transferPage.getRecipientName().setValue(changeUserRequest.getName());

        //Проверяем, что значение в поле Recipient Account Number не сброшено
        transferPage.getRecipientAccount().setValue(AccountData.ACCOUNT_NUMBER_PREFIX.getValue() + accountSecondUser);

        //Проверяем, что значение в поле Amount не сброшено
        transferPage.getAmount().setValue(String.valueOf(moreMaximumLimitValueMoney));

        //Проверяем, что чекбокс Confirm details are correct остался активен
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "true"));

        //Проверяем, что баланс аккаунта первого пользователя не изменился
        final double actualUserBalance = UserSteps.getUserBalance(authUserToken, userAccount);
        Assertions.assertEquals(expectedRandomMoney, actualUserBalance);

        //Проверяем, что баланс аккаунта второго пользователя не изменился
        final double actualSecondUserBalance = UserSteps.getUserBalance(secondUserToken, accountSecondUser);
        Assertions.assertEquals(expectedZeroBalance, actualSecondUserBalance);
    }

    @Test
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке перевода денег, когда баланс равен 0")
    public void userSeesErrorMessageWhenHisBalanceIsZeroAndHeTransferMoney() {

        double expectedZeroBalance = 0.00;
        int expectedListSize = 2;

        //Устанавливаем имя второго пользователю
        final ChangeUserRequest changeUserRequest = RandomModelGenerator.generate(ChangeUserRequest.class);

        new ValidatableCrudRequester<UsersResponse>(RequestSpecs.withToken(secondUserToken),
                EndpointRequests.UPDATE_USER, ResponseSpecs.requestReturnsOk()).PUT(changeUserRequest);

        //Переходим на страницу трансфера и проверяем, что отображается название этой страницы
        Selenide.open(UiPath.TRANSFER);
        transferPage.getTransferTitle().shouldBe(visible);
        transferPage.getAccountSelector().shouldBe(visible);

        // Проверяем значение по-умолчанию в выпадающем списке Select Account
        transferPage.getAccountSelector().options().findBy(exactText(DEFAULT_TEXT_IN_ACCOUNT_LIST_SELECTOR)).shouldBe(visible);

        //Проверяем размер списка
        transferPage.getAccountSelector().options().shouldHave(size(expectedListSize));

        //Выбираем единственный аккаунт пользователя
        transferPage.getAccountSelector().click();
        transferPage.getAccountSelector().selectOptionByValue(String.valueOf(userAccount));

        //Проверяем отображаемый аккаунт в списке
        final String actualAccountInfoInList = transferPage.getAccountSelector().getSelectedOptionText();
        final String expectedAccountInfoInList = getAccountInfoList(authUserToken, userAccount);
        Assertions.assertEquals(expectedAccountInfoInList, actualAccountInfoInList);

        // Проверяем отображение placeholder в поле Recipient Name
        transferPage.getRecipientName().shouldBe(visible);
        transferPage.getRecipientName().shouldHave(Condition.exactValue(""));

        //Указываем имя второго пользователя в поле Recipient Name
        transferPage.getRecipientName().setValue(changeUserRequest.getName());

        // Проверяем отображение placeholder в поле Recipient Account Number
        transferPage.getRecipientAccount().shouldBe(visible);
        transferPage.getRecipientAccount().shouldHave(Condition.exactValue(""));

        //Указываем аккаунт второго пользователя в поле Recipient Account Number
        transferPage.getRecipientAccount().setValue(AccountData.ACCOUNT_NUMBER_PREFIX.getValue() + accountSecondUser);

        // Проверяем отображение placeholder в поле Amount
        transferPage.getAmount().shouldBe(visible);
        transferPage.getAmount().shouldHave(Condition.exactValue(""));

        //Вводим сумму для перевода
        transferPage.getAmount().setValue(String.valueOf(expectedZeroBalance));

        // Проверяем, что чекбокс Confirm details are correct по-умолчанию не активен
        transferPage.getConfirmDetailsCheckbox().shouldHave(visible);
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "false"));

        //Активируем чекбокс подтверждая данные
        transferPage.getConfirmDetailsCheckbox().click();
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "true"));

        //Нажимаем кнопку Transfer
        transferPage.getTransferButton().click();

        // Проверяем сообщение в модальном окне
        final String actualAlertText = getActualTextFromModalPage();
        Assertions.assertEquals(TRANSFER_ERROR_NEGATIVE_VALUE, actualAlertText);

        //Проверяем, что остались на той же странице
        transferPage.getTransferTitle().shouldBe(visible);

        //Проверяем, что в списке выбранный аккаунт не сброшен
        final String actualAccountInfoInListAfterTransfer = transferPage.getAccountSelector().getSelectedOptionText();
        Assertions.assertEquals(expectedAccountInfoInList, actualAccountInfoInListAfterTransfer);

        //Проверяем, что значение в поле Recipient Name не сброшено
        transferPage.getRecipientName().setValue(changeUserRequest.getName());

        //Проверяем, что значение в поле Recipient Account Number не сброшено
        transferPage.getRecipientAccount().setValue(AccountData.ACCOUNT_NUMBER_PREFIX.getValue() + accountSecondUser);

        //Проверяем, что значение в поле Amount не сброшено
        transferPage.getAmount().setValue(String.valueOf(expectedZeroBalance));

        //Проверяем, что чекбокс Confirm details are correct остался активен
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "true"));

        //Проверяем, что баланс аккаунта первого пользователя не изменился
        final double actualUserBalance = UserSteps.getUserBalance(authUserToken, userAccount);
        Assertions.assertEquals(expectedZeroBalance, actualUserBalance);

        //Проверяем, что баланс аккаунта второго пользователя не изменился
        final double actualSecondUserBalance = UserSteps.getUserBalance(secondUserToken, accountSecondUser);
        Assertions.assertEquals(expectedZeroBalance, actualSecondUserBalance);
    }

    @Test
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке перевода денег без заполнения обязательных полей")
    public void userSeesErrorMessageWhenTryTransferWithoutRequiredFields() {

        double expectedRandomMoney = RandomData.getMoney();
        int expectedListSize = 2;

        //Устанавливаем имя второго пользователю
        final ChangeUserRequest changeUserRequest = RandomModelGenerator.generate(ChangeUserRequest.class);

        new ValidatableCrudRequester<UsersResponse>(RequestSpecs.withToken(secondUserToken),
                EndpointRequests.UPDATE_USER, ResponseSpecs.requestReturnsOk()).PUT(changeUserRequest);

        //Пополняем аккаунт первого пользователя
        UserSteps.depositMoney(authUserToken, userAccount, expectedRandomMoney);

        //Переходим на страницу трансфера и проверяем, что отображается название этой страницы
        Selenide.open(UiPath.TRANSFER);
        transferPage.getTransferTitle().shouldBe(visible);
        transferPage.getAccountSelector().shouldBe(visible);

        //Не заполняем ни одно из полей и сразу нажимаем кнопку Transfer
        transferPage.getTransferButton().click();

        // Проверяем сообщение в модальном окне
        final String actualAlertText = getActualTextFromModalPage();
        Assertions.assertEquals(TRANSFER_ERROR_WITHOUT_REQUIRED_FIELDS, actualAlertText);

        //Проверяем, что остались на той же странице
        transferPage.getTransferTitle().shouldBe(visible);

        // Проверяем значение по-умолчанию в выпадающем списке Select Account
        transferPage.getAccountSelector().options().findBy(exactText(DEFAULT_TEXT_IN_ACCOUNT_LIST_SELECTOR)).shouldBe(visible);

        //Проверяем размер списка
        transferPage.getAccountSelector().options().shouldHave(size(expectedListSize));

        //Выбираем единственный аккаунт пользователя
        transferPage.getAccountSelector().click();
        transferPage.getAccountSelector().selectOptionByValue(String.valueOf(userAccount));

        //Проверяем отображаемый аккаунт в списке
        final String actualAccountInfoInList = transferPage.getAccountSelector().getSelectedOptionText();
        final String expectedAccountInfoInList = getAccountInfoList(authUserToken, userAccount);
        Assertions.assertEquals(expectedAccountInfoInList, actualAccountInfoInList);

        //Остальные поля не заполняем и нажимаем кнопку Transfer
        transferPage.getTransferButton().click();

        // Проверяем сообщение в модальном окне
        final String actualAlertTextWithAccount = getActualTextFromModalPage();
        Assertions.assertEquals(TRANSFER_ERROR_WITHOUT_REQUIRED_FIELDS, actualAlertTextWithAccount);

        //Проверяем, что остались на той же странице
        transferPage.getTransferTitle().shouldBe(visible);

        //Проверяем, что в списке выбранный аккаунт не сброшен
        final String actualAccountInfoInListAfterTransfer = transferPage.getAccountSelector().getSelectedOptionText();
        Assertions.assertEquals(expectedAccountInfoInList, actualAccountInfoInListAfterTransfer);

        // Проверяем отображение placeholder в поле Recipient Name
        transferPage.getRecipientName().shouldBe(visible);
        transferPage.getRecipientName().shouldHave(Condition.exactValue(""));

        //Указываем имя второго пользователя в поле Recipient Name
        transferPage.getRecipientName().setValue(changeUserRequest.getName());

        //Остальные поля не заполняем и нажимаем кнопку Transfer
        transferPage.getTransferButton().click();

        // Проверяем сообщение в модальном окне
        final String actualAlertTextWithAccountRecipientName = getActualTextFromModalPage();
        Assertions.assertEquals(TRANSFER_ERROR_WITHOUT_REQUIRED_FIELDS, actualAlertTextWithAccountRecipientName);

        //Проверяем, что остались на той же странице
        transferPage.getTransferTitle().shouldBe(visible);

        //Проверяем, что значение в поле Recipient Name не сброшено
        transferPage.getRecipientName().setValue(changeUserRequest.getName());

        // Проверяем отображение placeholder в поле Recipient Account Number
        transferPage.getRecipientAccount().shouldBe(visible);
        transferPage.getRecipientAccount().shouldHave(Condition.exactValue(""));

        //Указываем аккаунт второго пользователя в поле Recipient Account Number
        transferPage.getRecipientAccount().setValue(AccountData.ACCOUNT_NUMBER_PREFIX.getValue() + accountSecondUser);

        //Остальные поля не заполняем и нажимаем кнопку Transfer
        transferPage.getTransferButton().click();

        // Проверяем сообщение в модальном окне
        final String actualAlertTextWithAccountRecipientNameAccount = getActualTextFromModalPage();
        Assertions.assertEquals(TRANSFER_ERROR_WITHOUT_REQUIRED_FIELDS, actualAlertTextWithAccountRecipientNameAccount);

        //Проверяем, что остались на той же странице
        transferPage.getTransferTitle().shouldBe(visible);

        //Проверяем, что значение в поле Recipient Account Number не сброшено
        transferPage.getRecipientAccount().setValue(AccountData.ACCOUNT_NUMBER_PREFIX.getValue() + accountSecondUser);

        // Проверяем отображение placeholder в поле Amount
        transferPage.getAmount().shouldBe(visible);
        transferPage.getAmount().shouldHave(Condition.exactValue(""));

        //Вводим сумму для перевода равную зачисленным деньгам в поле Amount
        transferPage.getAmount().setValue(String.valueOf(expectedRandomMoney));

        //Остальные поля не заполняем и нажимаем кнопку Transfer
        transferPage.getTransferButton().click();

        // Проверяем сообщение в модальном окне
        final String actualAlertTextWithAccountRecipientNameAccountAmount = getActualTextFromModalPage();
        Assertions.assertEquals(TRANSFER_ERROR_WITHOUT_REQUIRED_FIELDS, actualAlertTextWithAccountRecipientNameAccountAmount);

        //Проверяем, что остались на той же странице
        transferPage.getTransferTitle().shouldBe(visible);

        //Проверяем, что значение в поле Amount не сброшено
        transferPage.getAmount().setValue(String.valueOf(expectedRandomMoney));

        // Проверяем, что чекбокс Confirm details are correct по-умолчанию не активен
        transferPage.getConfirmDetailsCheckbox().shouldHave(visible);
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "false"));

        //Активируем чекбокс подтверждая данные
        transferPage.getConfirmDetailsCheckbox().click();
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "true"));

        //Нажимаем кнопку Transfer
        transferPage.getTransferButton().click();

        // Проверяем сообщение в модальном окне
        final String actualAlertTextWithAllFields = getActualTextFromModalPage();
        final String expectedSuccessfulAlertText = transferPage.expectedSuccessfulTransferModalMessage(expectedRandomMoney, accountSecondUser);
        Assertions.assertEquals(expectedSuccessfulAlertText, actualAlertTextWithAllFields);

        //Проверяем, что остались на той же странице
        transferPage.getTransferTitle().shouldBe(visible);

        //Проверяем, что баланс аккаунта второго пользователя пополнился после перевода
        final double actualSecondUserBalance = UserSteps.getUserBalance(secondUserToken, accountSecondUser);
        Assertions.assertEquals(expectedRandomMoney, actualSecondUserBalance);
    }

    @Test
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке перевода денег на несуществующий аккаунт")
    public void userSeesErrorMessageWhenTransferMoneyToUnexistedAccount() {

        double expectedRandomMoney = RandomData.getMoney();
        String randomRecipientName = RandomData.randomName(3);
        int expectedListSize = 2;

        //Находим максимальный ID аккаунт пользователя
        final int maxExistedAccountId = AdminSteps.getMaxExistedAccountId();

        //Пополняем аккаунт первого пользователя
        UserSteps.depositMoney(authUserToken, userAccount, expectedRandomMoney);

        //Переходим на страницу трансфера и проверяем, что отображается название этой страницы
        Selenide.open(UiPath.TRANSFER);
        transferPage.getTransferTitle().shouldBe(visible);
        transferPage.getAccountSelector().shouldBe(visible);

        // Проверяем значение по-умолчанию в выпадающем списке Select Account
        transferPage.getAccountSelector().options().findBy(exactText(DEFAULT_TEXT_IN_ACCOUNT_LIST_SELECTOR)).shouldBe(visible);

        //Проверяем размер списка
        transferPage.getAccountSelector().options().shouldHave(size(expectedListSize));

        //Выбираем единственный аккаунт пользователя
        transferPage.getAccountSelector().click();
        transferPage.getAccountSelector().selectOptionByValue(String.valueOf(userAccount));

        //Проверяем отображаемый аккаунт в списке
        final String actualAccountInfoInList = transferPage.getAccountSelector().getSelectedOptionText();
        final String expectedAccountInfoInList = getAccountInfoList(authUserToken, userAccount);
        Assertions.assertEquals(expectedAccountInfoInList, actualAccountInfoInList);

        // Проверяем отображение placeholder в поле Recipient Name
        transferPage.getRecipientName().shouldBe(visible);
        transferPage.getRecipientName().shouldHave(Condition.exactValue(""));

        //Указываем имя второго пользователя в поле Recipient Name
        transferPage.getRecipientName().setValue(randomRecipientName);

        // Проверяем отображение placeholder в поле Recipient Account Number
        transferPage.getRecipientAccount().shouldBe(visible);
        transferPage.getRecipientAccount().shouldHave(Condition.exactValue(""));

        //Указываем несуществующий аккаунт
        transferPage.getRecipientAccount().setValue(AccountData.ACCOUNT_NUMBER_PREFIX.getValue() + maxExistedAccountId + 1);

        // Проверяем отображение placeholder в поле Amount
        transferPage.getAmount().shouldBe(visible);
        transferPage.getAmount().shouldHave(Condition.exactValue(""));

        //Вводим сумму для перевода равную зачисленным деньгам в поле Amount
        transferPage.getAmount().setValue(String.valueOf(expectedRandomMoney));

        // Проверяем, что чекбокс Confirm details are correct по-умолчанию не активен
        transferPage.getConfirmDetailsCheckbox().shouldHave(visible);
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "false"));

        //Активируем чекбокс подтверждая данные
        transferPage.getConfirmDetailsCheckbox().click();
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "true"));

        //Нажимаем кнопку Transfer
        transferPage.getTransferButton().click();

        // Проверяем сообщение в модальном окне
        final String actualAlertText = getActualTextFromModalPage();
        Assertions.assertEquals(TRANSFER_ERROR_UNEXISTED_ACCOUNT, actualAlertText);

        //Проверяем, что остались на той же странице
        transferPage.getTransferTitle().shouldBe(visible);

        //Проверяем, что в списке выбранный аккаунт не сброшен
        final String actualAccountInfoInListAfterTransfer = transferPage.getAccountSelector().getSelectedOptionText();
        Assertions.assertEquals(expectedAccountInfoInList, actualAccountInfoInListAfterTransfer);

        //Проверяем, что значение в поле Recipient Name не сброшено
        transferPage.getRecipientName().setValue(randomRecipientName);

        //Проверяем, что значение в поле Recipient Account Number не сброшено
        transferPage.getRecipientAccount().setValue(AccountData.ACCOUNT_NUMBER_PREFIX.getValue() + accountSecondUser);

        //Проверяем, что значение в поле Amount не сброшено
        transferPage.getAmount().setValue(String.valueOf(expectedRandomMoney));

        //Проверяем, что чекбокс Confirm details are correct остался активен
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "true"));

        //Проверяем, что баланс аккаунта основного пользователя не изменился
        final double actualSecondUserBalance = UserSteps.getUserBalance(authUserToken, userAccount);
        Assertions.assertEquals(expectedRandomMoney, actualSecondUserBalance);
    }

    @Test
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке перевода денег с указанием " +
            " имени пользователя аккаунта другим регистром, когда имя задано")
    public void userSeesErrorMessageWhenTransferMoneyWithIncorrectNameAccountWhenNameIsUpperCase() {

        double expectedRandomMoney = RandomData.getMoney();
        double zeroBalance = 0.00;
        int expectedListSize = 2;

        //Устанавливаем имя второго пользователю
        final ChangeUserRequest changeUserRequest = RandomModelGenerator.generate(ChangeUserRequest.class);

        new ValidatableCrudRequester<UsersResponse>(RequestSpecs.withToken(secondUserToken),
                EndpointRequests.UPDATE_USER, ResponseSpecs.requestReturnsOk()).PUT(changeUserRequest);

        //Пополняем аккаунт первого пользователя
        UserSteps.depositMoney(authUserToken, userAccount, expectedRandomMoney);

        //Переходим на страницу трансфера и проверяем, что отображается название этой страницы
        Selenide.open(UiPath.TRANSFER);
        transferPage.getTransferTitle().shouldBe(visible);
        transferPage.getAccountSelector().shouldBe(visible);

        // Проверяем значение по-умолчанию в выпадающем списке Select Account
        transferPage.getAccountSelector().options().findBy(exactText(DEFAULT_TEXT_IN_ACCOUNT_LIST_SELECTOR)).shouldBe(visible);

        //Проверяем размер списка
        transferPage.getAccountSelector().options().shouldHave(size(expectedListSize));

        //Выбираем единственный аккаунт пользователя
        transferPage.getAccountSelector().click();
        transferPage.getAccountSelector().selectOptionByValue(String.valueOf(userAccount));

        //Проверяем отображаемый аккаунт в списке
        final String actualAccountInfoInList = transferPage.getAccountSelector().getSelectedOptionText();
        final String expectedAccountInfoInList = getAccountInfoList(authUserToken, userAccount);
        Assertions.assertEquals(expectedAccountInfoInList, actualAccountInfoInList);

        // Проверяем отображение placeholder в поле Recipient Name
        transferPage.getRecipientName().shouldBe(visible);
        transferPage.getRecipientName().shouldHave(Condition.exactValue(""));

        //Указываем имя второго пользователя в поле Recipient Name верхним регистром
        transferPage.getRecipientName().setValue(changeUserRequest.getName().toUpperCase());

        // Проверяем отображение placeholder в поле Recipient Account Number
        transferPage.getRecipientAccount().shouldBe(visible);
        transferPage.getRecipientAccount().shouldHave(Condition.exactValue(""));

        //Указываем аккаунт второго пользователя в поле Recipient Account Number
        transferPage.getRecipientAccount().setValue(AccountData.ACCOUNT_NUMBER_PREFIX.getValue() + accountSecondUser);

        // Проверяем отображение placeholder в поле Amount
        transferPage.getAmount().shouldBe(visible);
        transferPage.getAmount().shouldHave(Condition.exactValue(""));

        //Вводим сумму для перевода равную зачисленным деньгам в поле Amount
        transferPage.getAmount().setValue(String.valueOf(expectedRandomMoney));

        // Проверяем, что чекбокс Confirm details are correct по-умолчанию не активен
        transferPage.getConfirmDetailsCheckbox().shouldHave(visible);
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "false"));

        //Активируем чекбокс подтверждая данные
        transferPage.getConfirmDetailsCheckbox().click();
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "true"));

        //Нажимаем кнопку Transfer
        transferPage.getTransferButton().click();

        // Проверяем сообщение в модальном окне
        final String actualAlertText = getActualTextFromModalPage();
        Assertions.assertEquals(TRANSFER_ERROR_RECIPIENT_NAME_ANOTHER_CASE, actualAlertText);

        //Проверяем, что остались на той же странице
        transferPage.getTransferTitle().shouldBe(visible);

        //Проверяем, что в списке выбранный аккаунт не сброшен
        final String actualAccountInfoInListAfterTransfer = transferPage.getAccountSelector().getSelectedOptionText();
        Assertions.assertEquals(expectedAccountInfoInList, actualAccountInfoInListAfterTransfer);

        //Проверяем, что значение в поле Recipient Name не сброшено
        transferPage.getRecipientName().setValue(changeUserRequest.getName().toUpperCase());

        //Проверяем, что значение в поле Recipient Account Number не сброшено
        transferPage.getRecipientAccount().setValue(AccountData.ACCOUNT_NUMBER_PREFIX.getValue() + accountSecondUser);

        //Проверяем, что значение в поле Amount не сброшено
        transferPage.getAmount().setValue(String.valueOf(expectedRandomMoney));

        //Проверяем, что чекбокс Confirm details are correct остался активен
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "true"));

        //Проверяем, что баланс аккаунта основного пользователя не изменился
        final double actualUserBalance = UserSteps.getUserBalance(authUserToken, userAccount);
        Assertions.assertEquals(expectedRandomMoney, actualUserBalance);

        //Проверяем, что баланс аккаунта второго пользователя не изменился
        final double actualSecondUserBalance = UserSteps.getUserBalance(secondUserToken, accountSecondUser);
        Assertions.assertEquals(zeroBalance, actualSecondUserBalance);
    }

    @Test
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке перевода денег с указанием " +
            " аккаунта пользователя другим регистром")
    public void userSeesErrorMessageWhenTransferMoneyWithIncorrectAccount() {
        double expectedRandomMoney = RandomData.getMoney();
        double zeroBalance = 0.00;
        int expectedListSize = 2;

        //Устанавливаем имя второго пользователю
        final ChangeUserRequest changeUserRequest = RandomModelGenerator.generate(ChangeUserRequest.class);

        new ValidatableCrudRequester<UsersResponse>(RequestSpecs.withToken(secondUserToken),
                EndpointRequests.UPDATE_USER, ResponseSpecs.requestReturnsOk()).PUT(changeUserRequest);

        //Пополняем аккаунт первого пользователя
        UserSteps.depositMoney(authUserToken, userAccount, expectedRandomMoney);

        //Переходим на страницу трансфера и проверяем, что отображается название этой страницы
        Selenide.open(UiPath.TRANSFER);
        transferPage.getTransferTitle().shouldBe(visible);
        transferPage.getAccountSelector().shouldBe(visible);

        // Проверяем значение по-умолчанию в выпадающем списке Select Account
        transferPage.getAccountSelector().options().findBy(exactText(DEFAULT_TEXT_IN_ACCOUNT_LIST_SELECTOR)).shouldBe(visible);

        //Проверяем размер списка
        transferPage.getAccountSelector().options().shouldHave(size(expectedListSize));

        //Выбираем единственный аккаунт пользователя
        transferPage.getAccountSelector().click();
        transferPage.getAccountSelector().selectOptionByValue(String.valueOf(userAccount));

        //Проверяем отображаемый аккаунт в списке
        final String actualAccountInfoInList = transferPage.getAccountSelector().getSelectedOptionText();
        final String expectedAccountInfoInList = getAccountInfoList(authUserToken, userAccount);
        Assertions.assertEquals(expectedAccountInfoInList, actualAccountInfoInList);

        // Проверяем отображение placeholder в поле Recipient Name
        transferPage.getRecipientName().shouldBe(visible);
        transferPage.getRecipientName().shouldHave(Condition.exactValue(""));

        //Указываем имя второго пользователя в поле Recipient Name верхним регистром
        transferPage.getRecipientName().setValue(changeUserRequest.getName());

        // Проверяем отображение placeholder в поле Recipient Account Number
        transferPage.getRecipientAccount().shouldBe(visible);
        transferPage.getRecipientAccount().shouldHave(Condition.exactValue(""));

        //Указываем аккаунт второго пользователя в поле Recipient Account Number нижним регистром
        transferPage.getRecipientAccount().setValue(AccountData.ACCOUNT_NUMBER_PREFIX.getValue().toLowerCase() + accountSecondUser);

        // Проверяем отображение placeholder в поле Amount
        transferPage.getAmount().shouldBe(visible);
        transferPage.getAmount().shouldHave(Condition.exactValue(""));

        //Вводим сумму для перевода равную зачисленным деньгам в поле Amount
        transferPage.getAmount().setValue(String.valueOf(expectedRandomMoney));

        // Проверяем, что чекбокс Confirm details are correct по-умолчанию не активен
        transferPage.getConfirmDetailsCheckbox().shouldHave(visible);
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "false"));

        //Активируем чекбокс подтверждая данные
        transferPage.getConfirmDetailsCheckbox().click();
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "true"));

        //Нажимаем кнопку Transfer
        transferPage.getTransferButton().click();

        // Проверяем сообщение в модальном окне
        final String actualAlertText = getActualTextFromModalPage();
        Assertions.assertEquals(TRANSFER_ERROR_UNEXISTED_ACCOUNT, actualAlertText);

        //Проверяем, что остались на той же странице
        transferPage.getTransferTitle().shouldBe(visible);

        //Проверяем, что в списке выбранный аккаунт не сброшен
        final String actualAccountInfoInListAfterTransfer = transferPage.getAccountSelector().getSelectedOptionText();
        Assertions.assertEquals(expectedAccountInfoInList, actualAccountInfoInListAfterTransfer);

        //Проверяем, что значение в поле Recipient Name не сброшено
        transferPage.getRecipientName().setValue(changeUserRequest.getName().toUpperCase());

        //Проверяем, что значение в поле Recipient Account Number не сброшено
        transferPage.getRecipientAccount().setValue(AccountData.ACCOUNT_NUMBER_PREFIX.getValue() + accountSecondUser);

        //Проверяем, что значение в поле Amount не сброшено
        transferPage.getAmount().setValue(String.valueOf(expectedRandomMoney));

        //Проверяем, что чекбокс Confirm details are correct остался активен
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "true"));

        //Проверяем, что баланс аккаунта основного пользователя не изменился
        final double actualUserBalance = UserSteps.getUserBalance(authUserToken, userAccount);
        Assertions.assertEquals(expectedRandomMoney, actualUserBalance);

        //Проверяем, что баланс аккаунта второго пользователя не изменился
        final double actualSecondUserBalance = UserSteps.getUserBalance(secondUserToken, accountSecondUser);
        Assertions.assertEquals(zeroBalance, actualSecondUserBalance);
    }

    @Test
    @DisplayName("Позитивный тест: проверка, что пользователь может просмотреть выполненные транзакции по своим аккаунтам")
    public void userCanSeeHisTransactionHistory() {

        //У операций не отображается имя пользователя под которым выполнялись эти транзакции.
        //Ввиду этого, пока что у пользователя не задано name, невозможно найти его транзакции, если пробовать их искать
        //Не отображаются транзакции по переводу если не выполнить рефреш
        //Вопрос. Какая ожидается сортировка при просмотре списка транзакций? Из-за этого не стал писать проверки на порядок

        int expectedTransactions = 0;
        int expectedListSize = 3;
        double randomMoneyForFirstAccount = RandomData.getMoney();
        double randomMoneyForSecondAccount = RandomData.getMoney();

        //Создаём второй аккаунт для пользователя
        final int userAccountSecond = UserSteps.createUserAccount(authUserToken);

        //Переходим на страницу трансфера и проверяем, что отображается название этой страницы
        Selenide.open(UiPath.TRANSFER);
        transferPage.getTransferTitle().shouldBe(visible);

        //Переходим на вкладку TransferAgain и проверяем, что отображается строка поиска
        transferPage.getTransferAgainButton().click();
        transferPage.getSearchButton().shouldBe(visible);

        //Проверяем, что список транзакций пуст
        transferPage.getTransactionsList().shouldHave(size(expectedTransactions));

        //Пополняем оба аккаунта пользователя разными суммами
        UserSteps.depositMoney(authUserToken, userAccount, randomMoneyForFirstAccount);
        UserSteps.depositMoney(authUserToken, userAccountSecond, randomMoneyForSecondAccount);

        //Выполняем рефреш
        Selenide.refresh();

        //Проверяем, что отображается название страницы переводов
        transferPage.getTransferTitle().shouldBe(visible);

        //Переходим на вкладку TransferAgain и проверяем, что отображается строка поиска
        transferPage.getTransferAgainButton().click();
        transferPage.getSearchButton().shouldBe(visible);

        //Проверяем, что список транзакций содержит 2 транзакции
        expectedTransactions = 2;
        transferPage.getTransactionsList().shouldHave(size(expectedTransactions));

        //Проверяем, что каждая транзакция содержит тип DEPOSIT и сумма, которой пополняется каждый аккаунт
        final List<String> transactionsTextDeposit = transferPage.getTransactionsText();
        Assertions.assertTrue(transferPage.checkTransaction(transactionsTextDeposit, randomMoneyForFirstAccount, Operations.DEPOSIT));
        Assertions.assertTrue(transferPage.checkTransaction(transactionsTextDeposit, randomMoneyForSecondAccount, Operations.DEPOSIT));

        //Переходим обратно на вкладку New Transfer и проверяем, что отображается наименование вкладки
        transferPage.getNewTransferButton().click();
        transferPage.getTransferTitle().should(visible);

        // Проверяем значение по-умолчанию в выпадающем списке Select Account
        transferPage.getAccountSelector().options().findBy(exactText(DEFAULT_TEXT_IN_ACCOUNT_LIST_SELECTOR)).shouldBe(visible);

        //Проверяем размер списка
        transferPage.getAccountSelector().options().shouldHave(size(expectedListSize));

        //Выбираем первый аккаунт пользователя
        transferPage.getAccountSelector().click();
        transferPage.getAccountSelector().selectOptionByValue(String.valueOf(userAccount));

        //Проверяем отображаемый аккаунт в списке
        final String actualAccountInfoInList = transferPage.getAccountSelector().getSelectedOptionText();
        final String expectedAccountInfoInList = getAccountInfoList(authUserToken, userAccount);
        Assertions.assertEquals(expectedAccountInfoInList, actualAccountInfoInList);

        // Проверяем отображение placeholder в поле Recipient Name
        transferPage.getRecipientName().shouldBe(visible);
        transferPage.getRecipientName().shouldHave(Condition.exactValue(""));

        //Указываем рандомное пользователя в поле Recipient Name
        transferPage.getRecipientName().setValue(RandomData.randomName(3));

        // Проверяем отображение placeholder в поле Recipient Account Number
        transferPage.getRecipientAccount().shouldBe(visible);
        transferPage.getRecipientAccount().shouldHave(Condition.exactValue(""));

        //Указываем второй аккаунт пользователя в поле Recipient Account Number
        transferPage.getRecipientAccount().setValue(AccountData.ACCOUNT_NUMBER_PREFIX.getValue() + userAccountSecond);

        // Проверяем отображение placeholder в поле Amount
        transferPage.getAmount().shouldBe(visible);
        transferPage.getAmount().shouldHave(Condition.exactValue(""));

        //Вводим сумму для перевода равную зачисленным деньгам в поле Amount
        transferPage.getAmount().setValue(String.valueOf(randomMoneyForFirstAccount));

        // Проверяем, что чекбокс Confirm details are correct по-умолчанию не активен
        transferPage.getConfirmDetailsCheckbox().shouldHave(visible);
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "false"));

        //Активируем чекбокс подтверждая данные
        transferPage.getConfirmDetailsCheckbox().click();
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "true"));

        //Нажимаем кнопку Transfer
        transferPage.getTransferButton().click();

        // Проверяем сообщение в модальном окне об успешности выполнения Transfer
        final String actualAlertText = getActualTextFromModalPage();
        final String expectedAlertText =
                transferPage.expectedSuccessfulTransferModalMessage(randomMoneyForFirstAccount, userAccountSecond);
        Assertions.assertEquals(expectedAlertText, actualAlertText);

        //Выполняем рефреш, чтобы появились транзакции по переводу
        Selenide.refresh();

        //Переходим на вкладку Transfer Again и проверяем, что отображается строка поиска
        transferPage.getTransferAgainButton().click();
        transferPage.getSearchButton().should(visible);

        //Проверяем, что список транзакций содержит 4 транзакции
        expectedTransactions = 4;
        transferPage.getTransactionsList().shouldHave(size(expectedTransactions));

        //Проверяем, что две транзакции содержат тип DEPOSIT и сумму, которой пополнялся каждый аккаунт
        //Проверяем, что остальные две транзакции содержат тип TRANSFER_IN и TRANSFER_OUT и сумму перевода
        final List<String> transactionsTextTransfer = transferPage.getTransactionsText();
        Assertions.assertTrue(transferPage.checkTransaction(transactionsTextTransfer, randomMoneyForFirstAccount, Operations.DEPOSIT));
        Assertions.assertTrue(transferPage.checkTransaction(transactionsTextTransfer, randomMoneyForSecondAccount, Operations.DEPOSIT));
        Assertions.assertTrue(transferPage.checkTransaction(transactionsTextTransfer, randomMoneyForFirstAccount, Operations.TRANSFER_OUT));
        Assertions.assertTrue(transferPage.checkTransaction(transactionsTextTransfer, randomMoneyForFirstAccount, Operations.TRANSFER_IN));
    }

    @Test
    @DisplayName("Позитивный тест: проверка, что пользователь может находить свои транзакции по username/name")
    public void userCanFindHisTransactionHistoryByUsernameName() {

        int expectedTransactions = 0;
        int expectedListSize = 3;
        double randomMoney = RandomData.getMoney();

        //Получаем информацию о пользователе
        final UsersResponse userInfo = UserSteps.getUserInfo(authUserToken);

        //Пополняем первый аккаунт пользователя
        UserSteps.depositMoney(authUserToken, userAccount, randomMoney);

        //Создаём второй аккаунт для пользователя
        final int userAccountSecond = UserSteps.createUserAccount(authUserToken);

        //Переходим на страницу трансфера и проверяем, что отображается название этой страницы
        Selenide.open(UiPath.TRANSFER);
        transferPage.getTransferTitle().shouldBe(visible);

        // Проверяем значение по-умолчанию в выпадающем списке Select Account
        transferPage.getAccountSelector().options().findBy(exactText(DEFAULT_TEXT_IN_ACCOUNT_LIST_SELECTOR)).shouldBe(visible);

        //Проверяем размер списка
        transferPage.getAccountSelector().options().shouldHave(size(expectedListSize));

        //Выбираем первый аккаунт пользователя
        transferPage.getAccountSelector().click();
        transferPage.getAccountSelector().selectOptionByValue(String.valueOf(userAccount));

        //Проверяем отображаемый аккаунт в списке
        final String actualAccountInfoInList = transferPage.getAccountSelector().getSelectedOptionText();
        final String expectedAccountInfoInList = getAccountInfoList(authUserToken, userAccount);
        Assertions.assertEquals(expectedAccountInfoInList, actualAccountInfoInList);

        // Проверяем отображение placeholder в поле Recipient Name
        transferPage.getRecipientName().shouldBe(visible);
        transferPage.getRecipientName().shouldHave(Condition.exactValue(""));

        //Указываем рандомное имя пользователя в поле Recipient Name
        transferPage.getRecipientName().setValue(RandomData.randomName(3));

        // Проверяем отображение placeholder в поле Recipient Account Number
        transferPage.getRecipientAccount().shouldBe(visible);
        transferPage.getRecipientAccount().shouldHave(Condition.exactValue(""));

        //Указываем второй аккаунт пользователя в поле Recipient Account Number
        transferPage.getRecipientAccount().setValue(AccountData.ACCOUNT_NUMBER_PREFIX.getValue() + userAccountSecond);

        // Проверяем отображение placeholder в поле Amount
        transferPage.getAmount().shouldBe(visible);
        transferPage.getAmount().shouldHave(Condition.exactValue(""));

        //Вводим сумму для перевода равную зачисленным деньгам в поле Amount
        transferPage.getAmount().setValue(String.valueOf(randomMoney));

        // Проверяем, что чекбокс Confirm details are correct по-умолчанию не активен
        transferPage.getConfirmDetailsCheckbox().shouldHave(visible);
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "false"));

        //Активируем чекбокс подтверждая данные
        transferPage.getConfirmDetailsCheckbox().click();
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "true"));

        //Нажимаем кнопку Transfer
        transferPage.getTransferButton().click();

        // Проверяем сообщение в модальном окне
        final String actualAlertText = getActualTextFromModalPage();
        final String expectedAlertText =
                transferPage.expectedSuccessfulTransferModalMessage(randomMoney, userAccountSecond);
        Assertions.assertEquals(expectedAlertText, actualAlertText);

        //Выполняем рефреш, чтобы появились транзакции по переводу
        Selenide.refresh();

        //Переходим на вкладку Transfer Again и проверяем, что отображается строка поиска
        transferPage.getTransferAgainButton().click();
        transferPage.getSearchButton().should(visible);

        //Проверяем, что список транзакций содержит 3 транзакции
        expectedTransactions = 3;
        transferPage.getTransactionsList().shouldHave(size(expectedTransactions));

        // Проверяем отображение placeholder в поле 'Search by Username or Name'
        transferPage.getSearchField().shouldHave(Condition.exactValue(""));

        //В поле 'Search by Username or Name' вводим username пользователя
        transferPage.getSearchField().setValue(userInfo.getUsername());

        //Нажимаем кнопку 'Search Transactions'
        transferPage.getSearchButton().click();

        //Проверяем, что список транзакций содержит 3 транзакции
        transferPage.getTransactionsList().shouldHave(size(expectedTransactions));

        //Проверяем, что одна транзакция содержит тип DEPOSIT, сумму и username пользователя
        //Проверяем, что остальные две транзакции содержат тип TRANSFER_IN и TRANSFER_OUT, сумму перевода и username пользователя
        final List<String> transactionsTextTransfer = transferPage.getTransactionsText();
        Assertions.assertTrue(transferPage.checkTransaction(transactionsTextTransfer, randomMoney, Operations.DEPOSIT, userInfo.getUsername()));
        Assertions.assertTrue(transferPage.checkTransaction(transactionsTextTransfer, randomMoney, Operations.TRANSFER_OUT, userInfo.getUsername()));
        Assertions.assertTrue(transferPage.checkTransaction(transactionsTextTransfer, randomMoney, Operations.TRANSFER_IN, userInfo.getUsername()));

        //Для пользователя задаём имя
        final ChangeUserRequest changeUserRequest = RandomModelGenerator.generate(ChangeUserRequest.class);

        new ValidatableCrudRequester<UsersResponse>(RequestSpecs.withToken(authUserToken), EndpointRequests.UPDATE_USER, ResponseSpecs.requestReturnsOk())
                .PUT(changeUserRequest);

        //Если сделать следующие шаги сразу после изменения имени без рефреша, то при попытке
        //поиска отобразится модальное окно с ошибкой. Ввиду отсутствия требования будет выполняться рефреш

        Selenide.refresh();
        transferPage.getTransferAgainButton().click();
        transferPage.getSearchButton().shouldBe(visible);

        //В поле 'Search by Username or Name' вводим name пользователя
        transferPage.getSearchField().setValue(changeUserRequest.getName());

        //Нажимаем кнопку 'Search Transactions'
        transferPage.getSearchButton().click();

        //Проверяем, что список транзакций содержит 3 транзакции
        transferPage.getTransactionsList().shouldHave(size(expectedTransactions));

        final List<String> transactionsTextName = transferPage.getTransactionsText();
        Assertions.assertTrue(transferPage.checkTransaction(transactionsTextName, randomMoney, Operations.DEPOSIT, changeUserRequest.getName()));
        Assertions.assertTrue(transferPage.checkTransaction(transactionsTextName, randomMoney, Operations.TRANSFER_OUT, changeUserRequest.getName()));
        Assertions.assertTrue(transferPage.checkTransaction(transactionsTextName, randomMoney, Operations.TRANSFER_IN, changeUserRequest.getName()));

        //В поле 'Search by Username or Name' вводим username пользователя верхним регистром
        transferPage.getSearchField().setValue(userInfo.getUsername().toUpperCase());

        //Нажимаем кнопку 'Search Transactions'
        transferPage.getSearchButton().click();

        //Проверяем, что список транзакций содержит 3 транзакции
        transferPage.getTransactionsList().shouldHave(size(expectedTransactions));

        final List<String> transactionsTextUsernameUpperCase = transferPage.getTransactionsText();
        Assertions.assertTrue(transferPage.checkTransaction(transactionsTextUsernameUpperCase, randomMoney, Operations.DEPOSIT, changeUserRequest.getName()));
        Assertions.assertTrue(transferPage.checkTransaction(transactionsTextUsernameUpperCase, randomMoney, Operations.TRANSFER_OUT, changeUserRequest.getName()));
        Assertions.assertTrue(transferPage.checkTransaction(transactionsTextUsernameUpperCase, randomMoney, Operations.TRANSFER_IN, changeUserRequest.getName()));

        //В поле 'Search by Username or Name' вводим username пользователя частично
        transferPage.getSearchField().setValue(userInfo.getUsername().substring(0, 2));

        //Нажимаем кнопку 'Search Transactions'
        transferPage.getSearchButton().click();

        //Проверяем, что список транзакций содержит 3 транзакции
        transferPage.getTransactionsList().shouldHave(size(expectedTransactions));

        final List<String> transactionsTextUsernamePartially = transferPage.getTransactionsText();
        Assertions.assertTrue(transferPage.checkTransaction(transactionsTextUsernamePartially, randomMoney, Operations.DEPOSIT, changeUserRequest.getName()));
        Assertions.assertTrue(transferPage.checkTransaction(transactionsTextUsernamePartially, randomMoney, Operations.TRANSFER_OUT, changeUserRequest.getName()));
        Assertions.assertTrue(transferPage.checkTransaction(transactionsTextUsernamePartially, randomMoney, Operations.TRANSFER_IN, changeUserRequest.getName()));

        //В поле 'Search by Username or Name' вводим name не полностью, а лишь одно слово
        transferPage.getSearchField().setValue(changeUserRequest.getName().split(" ")[0]);

        //Нажимаем кнопку 'Search Transactions'
        transferPage.getSearchButton().click();

        //Проверяем, что список транзакций содержит 3 транзакции
        transferPage.getTransactionsList().shouldHave(size(expectedTransactions));

        final List<String> transactionsTextNamePartially = transferPage.getTransactionsText();
        Assertions.assertTrue(transferPage.checkTransaction(transactionsTextNamePartially, randomMoney, Operations.DEPOSIT, changeUserRequest.getName()));
        Assertions.assertTrue(transferPage.checkTransaction(transactionsTextNamePartially, randomMoney, Operations.TRANSFER_OUT, changeUserRequest.getName()));
        Assertions.assertTrue(transferPage.checkTransaction(transactionsTextNamePartially, randomMoney, Operations.TRANSFER_IN, changeUserRequest.getName()));
    }

    @Test
    @DisplayName("Негативный тест: проверка, что пользователь не может находить чужие транзакции по username/name")
    public void userCannotFindTransactionHistoryByOtherUsers() {
        //В последнем шаге баг: пользователь может просмотреть транзакции других пользователей

        final double randomMoney = RandomData.getMoney();

        //Пополняем баланс только аккаунта для первого пользователя через api
        UserSteps.depositMoney(authUserToken, userAccount, randomMoney);

        //Выполняем перевод с аккаунта первого пользователя на аккаунт второго пользователя через api
        UserSteps.successfulTransferMoneyBetweenAccounts(authUserToken, userAccount, accountSecondUser, randomMoney);

        //Получаем информацию о втором пользователе
        final UsersResponse secondUserInfo = UserSteps.getUserInfo(secondUserToken);

        //Авторизуемся под первым пользователем и переходим на вкладку 'New Transfer'
        // и проверяем, что отображается наименование страницы
        Selenide.open(UiPath.TRANSFER);
        transferPage.getTransferTitle().shouldBe(visible);

        //Переходим на вкладку 'Transfer Again' и проверяем отображение кнопки поиска транзакций
        transferPage.getTransferAgainButton().click();
        transferPage.getSearchButton().shouldBe(visible);

        //В строке поиска указываем username второго пользователя
        transferPage.getSearchField().setValue(secondUserInfo.getUsername());
        transferPage.getSearchButton().click();

        final List<String> transactionsTexts = transferPage.getTransactionsText();
        Assertions.assertFalse(transferPage.checkTransaction(transactionsTexts, randomMoney, Operations.TRANSFER_IN, secondUserInfo.getUsername()));
    }

    @Test
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке поиска транзакций с указанием " +
            "несуществующего username/name")
    public void userCannotFindTransactionHistoryByNotExistedUsernameOrName() {

        double randomMoney = RandomData.getMoney();
        int expectedTransactions = 0;

        //Пополняем баланс только аккаунта для первого пользователя через api
        UserSteps.depositMoney(authUserToken, userAccount, randomMoney);

        //Выполняем перевод с аккаунта первого пользователя на аккаунт второго пользователя через api
        UserSteps.successfulTransferMoneyBetweenAccounts(authUserToken, userAccount, accountSecondUser, randomMoney);

        //Авторизуемся под первым пользователем и переходим на вкладку 'New Transfer'
        // и проверяем, что отображается наименование страницы
        Selenide.open(UiPath.TRANSFER);
        transferPage.getTransferTitle().shouldBe(visible);

        //Переходим на вкладку 'Transfer Again' и проверяем отображение кнопки поиска транзакций
        transferPage.getTransferAgainButton().click();
        transferPage.getSearchButton().shouldBe(visible);

        //В строке поиска указываем любое рандомное не существующее значение
        transferPage.getSearchField().setValue(RandomData.randomName(15));
        transferPage.getSearchButton().click();

        final String actualTextFromModalPage = getActualTextFromModalPage();
        Assertions.assertEquals(TRANSFER_ERROR_UNEXISTED_NAME, actualTextFromModalPage);

        transferPage.getTransactionsList().shouldHave(size(expectedTransactions));

    }

    @Test
    @DisplayName("Позитивный тест: проверка, что пользователь может выполнить повторно ранее выполненные " +
            "транзакции с типом DEPOSIT")
    public void userCanRepeatHisTransactionForDepositFromTransactionHistory() {

        //На последних двух шагах есть дефект: при повторении операции Deposit на самом деле
        //вызывается операция Transfer и это приводит к ошибке, т.к. используется значение Amount,
        //которое уже больше, чем баланс первого аккаунта

        int expectedTransactions = 3;
        double randomMoneyDeposit = RandomData.getMoneyFromTo(3000, 5000);
        double randomMoneyTransfer = RandomData.getMoneyFromTo(2000, 2999);
        int expectedListSize = 3;

        //Получаем информацию о пользователе
        final UsersResponse userInfo = UserSteps.getUserInfo(authUserToken);

        //Создаём второй аккаунт для основного пользователя
        final int userAccountSecond = UserSteps.createUserAccount(authUserToken);

        //Пополняем первый аккаунт основного пользователя
        UserSteps.depositMoney(authUserToken, userAccount, randomMoneyDeposit);

        //Переводим деньги с первого на второй аккаунт пользователя
        UserSteps.successfulTransferMoneyBetweenAccounts(authUserToken, userAccount, userAccountSecond, randomMoneyTransfer);

        //Переходим на страницу трансфера и проверяем, что отображается название этой страницы
        Selenide.open(UiPath.TRANSFER);
        transferPage.getTransferTitle().shouldBe(visible);

        //Переходим на вкладку Transfer Again и проверяем, что отображается строка поиска
        transferPage.getTransferAgainButton().click();
        transferPage.getSearchButton().should(visible);

        //Проверяем, что список транзакций содержит 3 транзакции
        transferPage.getTransactionsList().shouldHave(size(expectedTransactions));

        // Проверяем отображение placeholder в поле 'Search by Username or Name'
        transferPage.getSearchField().shouldHave(Condition.exactValue(""));

        //В поле 'Search by Username or Name' вводим username пользователя
        transferPage.getSearchField().setValue(userInfo.getUsername());

        //Нажимаем кнопку 'Search Transactions'
        transferPage.getSearchButton().click();

        //Проверяем, что список транзакций содержит 3 транзакции
        transferPage.getTransactionsList().shouldHave(size(expectedTransactions));

        //Проверяем, что одна транзакция содержит тип DEPOSIT, сумму и username пользователя
        //Проверяем, что остальные две транзакции содержат тип TRANSFER_IN и TRANSFER_OUT, сумму перевода и username пользователя
        final List<String> transactionsTextTransfer = transferPage.getTransactionsText();
        Assertions.assertTrue(transferPage.checkTransaction(transactionsTextTransfer, randomMoneyDeposit, Operations.DEPOSIT, userInfo.getUsername()));
        Assertions.assertTrue(transferPage.checkTransaction(transactionsTextTransfer, randomMoneyTransfer, Operations.TRANSFER_OUT, userInfo.getUsername()));
        Assertions.assertTrue(transferPage.checkTransaction(transactionsTextTransfer, randomMoneyTransfer, Operations.TRANSFER_IN, userInfo.getUsername()));

        //Нажимаем кнопку Repeat для транзакции Deposit и проверяем, что открылось модальное окно повтора транзакции
        transferPage.clickRepeatTransaction(Operations.DEPOSIT, randomMoneyDeposit);
        transferPage.getTransferModalTitleInRepeatModal().shouldBe(visible);

        //Проверяем текст с подтверждением транзакции и номером аккаунтом на который ранее производилось пополнение
        final String actualTransactionMessage = transferPage.getTransactionInfoInRepeatModal().text();
        final String expectedTransactionMessage = TRANSACTION_MESSAGE_REPEAT_MODAL + userAccount;
        Assertions.assertEquals(expectedTransactionMessage, actualTransactionMessage);

        //Проверяем значение по-умолчанию в выпадающем списке аккаунтов
        transferPage.getAccountSelectorInRepeatModal().options().findBy(exactText(DEFAULT_TEXT_IN_ACCOUNT_LIST_SELECTOR)).shouldBe(visible);

        //Проверяем количество аккаунтов в выпадающем списке
        transferPage.getAccountSelectorInRepeatModal().options().shouldHave(size(expectedListSize));

        //Выбираем первый аккаунт пользователя
        transferPage.getAccountSelectorInRepeatModal().click();
        transferPage.getAccountSelectorInRepeatModal().selectOptionByValue(String.valueOf(userAccount));

        //Проверяем отображаемый аккаунт в списке
        final String actualAccountInfoInList = transferPage.getAccountSelectorInRepeatModal().getSelectedOptionText();
        final String expectedAccountInfoInList = getAccountInfoList(authUserToken, userAccount);
        Assertions.assertEquals(expectedAccountInfoInList, actualAccountInfoInList);

        //Проверяем, что поле Amount содержит значение указанное в транзакции
        transferPage.getAmountInRepeatModal().shouldHave(Condition.exactValue(String.valueOf(randomMoneyDeposit)));

        // Проверяем, что чекбокс Confirm details are correct по-умолчанию не активен
        transferPage.getConfirmDetailsCheckbox().shouldHave(visible);
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "false"));

        //Активируем чекбокс подтверждая данные
        transferPage.getConfirmDetailsCheckbox().click();
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "true"));

        transferPage.getTransferButton().click();

        // Проверяем сообщение в модальном окне об успешности выполнения Deposit
        final String actualAlertText = getActualTextFromModalPage();
        final String expectedAlertText = depositPage.expectedSuccessfulDepositModalMessage(randomMoneyDeposit, userAccount);
        Assertions.assertEquals(expectedAlertText, actualAlertText);

        //Проверяем, что баланс первого аккаунта второго пользователя пополнился после перевода
        final double expectedBalanceFirstAccount = randomMoneyDeposit - randomMoneyTransfer + randomMoneyDeposit;
        final double actualSecondUserBalance = UserSteps.getUserBalance(secondUserToken, accountSecondUser);
        Assertions.assertEquals(expectedBalanceFirstAccount, actualSecondUserBalance);

    }

    @Test
    @DisplayName("Позитивный тест: проверка, что пользователь может выполнить повторно ранее выполненные " +
            "транзакции c типом TRANSFER_OUT")
    public void userCanRepeatHisTransactionsForTransferFromTransactionHistory() {

        //На последних двух шагах баг: происходит перевод на тот же аккаунт с которого происходит перевод несмотря на то,
        // что в описании сказано, что перевод происходит на другой аккаунт

        //Какой ОР, если выбрать 'Repeat' для транзакции с типом TRANSFER_IN? Описывать пока не стал

        int expectedTransactions = 2;
        double randomMoneyDeposit = RandomData.getMoneyFromTo(4000, 5000);
        double randomMoneyTransfer = RandomData.getMoneyFromTo(1000, 2000);
        int expectedListSize = 2;
        UsersResponse userInfo = UserSteps.getUserInfo(authUserToken);

        //Пополняем аккаунт первого пользователя любой рандомной суммой
        UserSteps.depositMoney(authUserToken, userAccount, randomMoneyDeposit);

        //Выполняем перевод с аккаунта первого пользователя на аккаунт второго пользователя
        UserSteps.successfulTransferMoneyBetweenAccounts(authUserToken, userAccount, accountSecondUser, randomMoneyTransfer);

        //Переходим на страницу трансфера и проверяем, что отображается название этой страницы
        Selenide.open(UiPath.TRANSFER);
        transferPage.getTransferTitle().shouldBe(visible);

        //Переходим на вкладку Transfer Again и проверяем, что отображается строка поиска
        transferPage.getTransferAgainButton().click();
        transferPage.getSearchButton().should(visible);

        //Проверяем список транзакций
        transferPage.getTransactionsList().shouldHave(size(expectedTransactions));

        // Проверяем отображение placeholder в поле 'Search by Username or Name'
        transferPage.getSearchField().shouldHave(Condition.exactValue(""));

        //В поле 'Search by Username or Name' вводим username пользователя
        transferPage.getSearchField().setValue(userInfo.getUsername());

        //Нажимаем кнопку 'Search Transactions'
        transferPage.getSearchButton().click();

        //Проверяем список транзакций
        transferPage.getTransactionsList().shouldHave(size(expectedTransactions));

        //Проверяем, что одна транзакция содержит тип DEPOSIT, сумму и username пользователя
        //Проверяем, что другая транзакция содержит тип TRANSFER_OUT, сумму перевода и username пользователя
        final List<String> transactionsTextTransfer = transferPage.getTransactionsText();
        Assertions.assertTrue(transferPage.checkTransaction(transactionsTextTransfer, randomMoneyDeposit, Operations.DEPOSIT, userInfo.getUsername()));
        Assertions.assertTrue(transferPage.checkTransaction(transactionsTextTransfer, randomMoneyTransfer, Operations.TRANSFER_OUT, userInfo.getUsername()));

        //Нажимаем кнопку Repeat для транзакции Transfer и проверяем, что открылось модальное окно повтора транзакции
        transferPage.clickRepeatTransaction(Operations.TRANSFER_OUT, randomMoneyTransfer);
        transferPage.getTransferModalTitleInRepeatModal().shouldBe(visible);

        //Проверяем текст с подтверждением транзакции и номером аккаунтом на который ранее производилось пополнение
        final String actualTransactionMessage = transferPage.getTransactionInfoInRepeatModal().text();
        final String expectedTransactionMessage = TRANSACTION_MESSAGE_REPEAT_MODAL + accountSecondUser;
        Assertions.assertEquals(expectedTransactionMessage, actualTransactionMessage);

        //Проверяем значение по-умолчанию в выпадающем списке аккаунтов
        transferPage.getAccountSelectorInRepeatModal().options().findBy(exactText(DEFAULT_TEXT_IN_ACCOUNT_LIST_SELECTOR)).shouldBe(visible);

        //Проверяем количество аккаунтов в выпадающем списке
        transferPage.getAccountSelectorInRepeatModal().options().shouldHave(size(expectedListSize));

        //Выбираем аккаунт пользователя
        transferPage.getAccountSelectorInRepeatModal().click();
        transferPage.getAccountSelectorInRepeatModal().selectOptionByValue(String.valueOf(userAccount));

        //Проверяем отображаемый аккаунт в списке
        final String actualAccountInfoInList = transferPage.getAccountSelectorInRepeatModal().getSelectedOptionText();
        final String expectedAccountInfoInList = getAccountInfoList(authUserToken, userAccount);
        Assertions.assertEquals(expectedAccountInfoInList, actualAccountInfoInList);

        //Проверяем, что поле Amount содержит значение указанное в транзакции
        transferPage.getAmountInRepeatModal().shouldHave(Condition.exactValue(String.valueOf(randomMoneyTransfer)));

        // Проверяем, что чекбокс Confirm details are correct по-умолчанию не активен
        transferPage.getConfirmDetailsCheckbox().shouldHave(visible);
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "false"));

        //Активируем чекбокс подтверждая данные
        transferPage.getConfirmDetailsCheckbox().click();
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "true"));

        transferPage.getTransferButton().click();

        // Проверяем сообщение в модальном окне об успешности выполнения Deposit
        final String actualAlertText = getActualTextFromModalPage();
        final String expectedAlertText = transferPage.expectedSuccessfulTransferModalMessage(randomMoneyDeposit, accountSecondUser);
        Assertions.assertEquals(expectedAlertText, actualAlertText);

        //Проверяем, что баланс аккаунта второго пользователя пополнился после перевода
        final double expectedSecondUserBalance = randomMoneyTransfer + randomMoneyTransfer;
        final double actualSecondUserBalance = UserSteps.getUserBalance(secondUserToken, accountSecondUser);
        Assertions.assertEquals(expectedSecondUserBalance, actualSecondUserBalance);
    }

    @Test
    @DisplayName("Негативный тест: проверка, что пользователь не может выполнить повторно ранее выполненные " +
            "транзакции, если указано значение меньше 0.01")
    public void userCanRepeatHisTransactionsFromTransactionHistoryWhenAmountLessMinimumValue() {

        int expectedTransactions = 2;
        double randomMoneyDeposit = RandomData.getMoneyFromTo(4000, 5000);
        double randomMoneyTransfer = RandomData.getMoneyFromTo(2000, 3000);
        double zeroMoney = 0.00;
        int expectedListSize = 2;
        UsersResponse userInfo = UserSteps.getUserInfo(authUserToken);

        //Пополняем аккаунт первого пользователя любой рандомной суммой
        UserSteps.depositMoney(authUserToken, userAccount, randomMoneyDeposit);

        //Выполняем перевод с аккаунта первого пользователя на аккаунт второго пользователя
        UserSteps.successfulTransferMoneyBetweenAccounts(authUserToken, userAccount, accountSecondUser, randomMoneyTransfer);

        //Переходим на страницу трансфера и проверяем, что отображается название этой страницы
        Selenide.open(UiPath.TRANSFER);
        transferPage.getTransferTitle().shouldBe(visible);

        //Переходим на вкладку Transfer Again и проверяем, что отображается строка поиска
        transferPage.getTransferAgainButton().click();
        transferPage.getSearchButton().should(visible);

        //Проверяем список транзакций
        transferPage.getTransactionsList().shouldHave(size(expectedTransactions));

        // Проверяем отображение placeholder в поле 'Search by Username or Name'
        transferPage.getSearchField().shouldHave(Condition.exactValue(""));

        //В поле 'Search by Username or Name' вводим username пользователя
        transferPage.getSearchField().setValue(userInfo.getUsername());

        //Нажимаем кнопку 'Search Transactions'
        transferPage.getSearchButton().click();

        //Проверяем список транзакций
        transferPage.getTransactionsList().shouldHave(size(expectedTransactions));

        //Проверяем, что одна транзакция содержит тип DEPOSIT, сумму и username пользователя
        //Проверяем, что другая транзакция содержит тип TRANSFER_OUT, сумму перевода и username пользователя
        final List<String> transactionsTextTransfer = transferPage.getTransactionsText();
        Assertions.assertTrue(transferPage.checkTransaction(transactionsTextTransfer, randomMoneyDeposit, Operations.DEPOSIT, userInfo.getUsername()));
        Assertions.assertTrue(transferPage.checkTransaction(transactionsTextTransfer, randomMoneyTransfer, Operations.TRANSFER_OUT, userInfo.getUsername()));

        //Нажимаем кнопку Repeat для транзакции Transfer и проверяем, что открылось модальное окно повтора транзакции
        transferPage.clickRepeatTransaction(Operations.TRANSFER_OUT, randomMoneyTransfer);
        transferPage.getTransferModalTitleInRepeatModal().shouldBe(visible);

        //Проверяем текст с подтверждением транзакции и номером аккаунтом на который ранее производилось пополнение
        final String actualTransactionMessage = transferPage.getTransactionInfoInRepeatModal().text();
        final String expectedTransactionMessage = TRANSACTION_MESSAGE_REPEAT_MODAL + accountSecondUser;
        Assertions.assertEquals(expectedTransactionMessage, actualTransactionMessage);

        //Проверяем значение по-умолчанию в выпадающем списке аккаунтов
        transferPage.getAccountSelectorInRepeatModal().options().findBy(exactText(DEFAULT_TEXT_IN_ACCOUNT_LIST_SELECTOR)).shouldBe(visible);

        //Проверяем количество аккаунтов в выпадающем списке
        transferPage.getAccountSelectorInRepeatModal().options().shouldHave(size(expectedListSize));

        //Выбираем аккаунт пользователя
        transferPage.getAccountSelectorInRepeatModal().click();
        transferPage.getAccountSelectorInRepeatModal().selectOptionByValue(String.valueOf(userAccount));

        //Проверяем отображаемый аккаунт в списке
        final String actualAccountInfoInList = transferPage.getAccountSelectorInRepeatModal().getSelectedOptionText();
        final String expectedAccountInfoInList = getAccountInfoList(authUserToken, userAccount);
        Assertions.assertEquals(expectedAccountInfoInList, actualAccountInfoInList);

        //Проверяем, что поле Amount содержит значение указанное в транзакции
        transferPage.getAmountInRepeatModal().shouldHave(Condition.exactValue(String.valueOf(randomMoneyTransfer)));

        //Указываем новое значение в поле Amount
        transferPage.getAmountInRepeatModal().setValue(String.valueOf(zeroMoney));

        // Проверяем, что чекбокс Confirm details are correct по-умолчанию не активен
        transferPage.getConfirmDetailsCheckbox().shouldHave(visible);
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "false"));

        //Активируем чекбокс подтверждая данные
        transferPage.getConfirmDetailsCheckbox().click();
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "true"));

        transferPage.getTransferButton().click();

        // Проверяем сообщение в модальном окне
        final String actualAlertText = getActualTextFromModalPage();
        Assertions.assertEquals(TRANSFER_ERROR_WITH_ZERO_AMOUNT, actualAlertText);

        //Проверяем, что баланс аккаунта первого пользователя не изменился
        final double expectedUserBalance = randomMoneyDeposit - randomMoneyTransfer;
        final double actualUserBalance = UserSteps.getUserBalance(authUserToken, userAccount);
        Assertions.assertEquals(expectedUserBalance, actualUserBalance);
    }

    @Test
    @DisplayName("Негативный тест: проверка запрета выполнения транзакции повторно если не указано одно " +
            "из обязательных полей")
    public void userCannotRepeatHisTransactionsFromTransactionHistoryIfRequiredFieldsAreNotFilledIn() {

        //В шаге проверки недоступности кнопки Send Transfer при пустом Amount - баг. Кнопка доступна.

        int expectedTransactions = 1;
        int expectedListSize = 2;
        double randomMoneyDeposit = RandomData.getMoneyFromTo(4000, 5000);
        UsersResponse userInfo = UserSteps.getUserInfo(authUserToken);

        //Пополняем аккаунт первого пользователя любой рандомной суммой
        UserSteps.depositMoney(authUserToken, userAccount, randomMoneyDeposit);

        //Переходим на страницу трансфера и проверяем, что отображается название этой страницы
        Selenide.open(UiPath.TRANSFER);
        transferPage.getTransferTitle().shouldBe(visible);

        //Переходим на вкладку Transfer Again и проверяем, что отображается строка поиска
        transferPage.getTransferAgainButton().click();
        transferPage.getSearchButton().should(visible);

        //Проверяем список транзакций
        transferPage.getTransactionsList().shouldHave(size(expectedTransactions));

        // Проверяем отображение placeholder в поле 'Search by Username or Name'
        transferPage.getSearchField().shouldHave(Condition.exactValue(""));

        //В поле 'Search by Username or Name' вводим username пользователя
        transferPage.getSearchField().setValue(userInfo.getUsername());

        //Нажимаем кнопку 'Search Transactions'
        transferPage.getSearchButton().click();

        //Проверяем список транзакций
        transferPage.getTransactionsList().shouldHave(size(expectedTransactions));

        //Проверяем, что одна транзакция содержит тип DEPOSIT, сумму и username пользователя
        final List<String> transactionsTextTransfer = transferPage.getTransactionsText();
        Assertions.assertTrue(transferPage.checkTransaction(transactionsTextTransfer, randomMoneyDeposit, Operations.DEPOSIT, userInfo.getUsername()));

        //Нажимаем кнопку Repeat для транзакции Deposit и проверяем, что открылось модальное окно повтора транзакции
        transferPage.clickRepeatTransaction(Operations.DEPOSIT, randomMoneyDeposit);
        transferPage.getTransferModalTitleInRepeatModal().shouldBe(visible);

        //Проверяем текст с подтверждением транзакции и номером аккаунтом на который ранее производилось пополнение
        final String actualTransactionMessage = transferPage.getTransactionInfoInRepeatModal().text();
        final String expectedTransactionMessage = TRANSACTION_MESSAGE_REPEAT_MODAL + userAccount;
        Assertions.assertEquals(expectedTransactionMessage, actualTransactionMessage);

        //Проверяем значение по-умолчанию в выпадающем списке аккаунтов
        transferPage.getAccountSelectorInRepeatModal().options().findBy(exactText(DEFAULT_TEXT_IN_ACCOUNT_LIST_SELECTOR)).shouldBe(visible);

        //Проверяем количество аккаунтов в выпадающем списке
        transferPage.getAccountSelectorInRepeatModal().options().shouldHave(size(expectedListSize));

        //Не выбираем никакой аккаунт пользователя и оставляем его пустым

        //Проверяем, что поле Amount содержит значение указанное в транзакции
        transferPage.getAmountInRepeatModal().shouldHave(Condition.exactValue(String.valueOf(randomMoneyDeposit)));

        // Проверяем, что чекбокс Confirm details are correct по-умолчанию не активен
        transferPage.getConfirmDetailsCheckbox().shouldHave(visible);
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "false"));

        //Активируем чекбокс подтверждая данные
        transferPage.getConfirmDetailsCheckbox().click();
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "true"));

        //Проверяем, что кнопка Send Transfer недоступна для нажатия
        transferPage.getTransferButton().shouldBe(not(clickable));

        //Выбираем аккаунт из выпадающего списка
        transferPage.getAccountSelectorInRepeatModal().click();
        transferPage.getAccountSelectorInRepeatModal().selectOptionByValue(String.valueOf(userAccount));

        //Проверяем отображаемый аккаунт в списке
        final String actualAccountInfoInList = transferPage.getAccountSelectorInRepeatModal().getSelectedOptionText();
        final String expectedAccountInfoInList = getAccountInfoList(authUserToken, userAccount);
        Assertions.assertEquals(expectedAccountInfoInList, actualAccountInfoInList);

        //Удаляем значение в поле Amount
        transferPage.getAmountInRepeatModal().clear();

        //Проверяем, что чекбокс активен
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "true"));

        //Проверяем, что кнопка Send Transfer недоступна для нажатия
        //Здесь баг. Ожидаем, что кнопка не должна быть доступна
        transferPage.getTransferButton().shouldBe(not(clickable));

        //Указываем значение в поле Amount
        transferPage.getAmountInRepeatModal().setValue(String.valueOf(randomMoneyDeposit));

        //Снимаем чекбокс
        transferPage.getConfirmDetailsCheckbox().click();
        transferPage.getConfirmDetailsCheckbox().should(domProperty("checked", "false"));

        //Проверяем, что кнопка Send Transfer недоступна для нажатия
        transferPage.getTransferButton().shouldBe(not(clickable));
    }

    @Test
    @DisplayName("Позитивный тест: проверка закрытия модального окна Repeat Transfer различными способами")
    public void userCanCloseModalPageRepeatTransferInDiffWays() {

        int expectedTransactions = 1;
        int expectedListSize = 2;
        double randomMoneyDeposit = RandomData.getMoneyFromTo(4000, 5000);
        UsersResponse userInfo = UserSteps.getUserInfo(authUserToken);

        //Пополняем аккаунт первого пользователя любой рандомной суммой
        UserSteps.depositMoney(authUserToken, userAccount, randomMoneyDeposit);

        //Переходим на страницу трансфера и проверяем, что отображается название этой страницы
        Selenide.open(UiPath.TRANSFER);
        transferPage.getTransferTitle().shouldBe(visible);

        //Переходим на вкладку Transfer Again и проверяем, что отображается строка поиска
        transferPage.getTransferAgainButton().click();
        transferPage.getSearchButton().should(visible);

        //Проверяем список транзакций
        transferPage.getTransactionsList().shouldHave(size(expectedTransactions));

        // Проверяем отображение placeholder в поле 'Search by Username or Name'
        transferPage.getSearchField().shouldHave(Condition.exactValue(""));

        //В поле 'Search by Username or Name' вводим username пользователя
        transferPage.getSearchField().setValue(userInfo.getUsername());

        //Нажимаем кнопку 'Search Transactions'
        transferPage.getSearchButton().click();

        //Проверяем список транзакций
        transferPage.getTransactionsList().shouldHave(size(expectedTransactions));

        //Проверяем, что одна транзакция содержит тип DEPOSIT, сумму и username пользователя
        final List<String> transactionsTextTransfer = transferPage.getTransactionsText();
        Assertions.assertTrue(transferPage.checkTransaction(transactionsTextTransfer, randomMoneyDeposit, Operations.DEPOSIT, userInfo.getUsername()));

        //Нажимаем кнопку Repeat для транзакции Deposit и проверяем, что открылось модальное окно повтора транзакции
        transferPage.clickRepeatTransaction(Operations.DEPOSIT, randomMoneyDeposit);
        transferPage.getTransferModalTitleInRepeatModal().shouldBe(visible);

        //Проверяем текст с подтверждением транзакции и номером аккаунтом на который ранее производилось пополнение
        final String actualTransactionMessage = transferPage.getTransactionInfoInRepeatModal().text();
        final String expectedTransactionMessage = TRANSACTION_MESSAGE_REPEAT_MODAL + userAccount;
        Assertions.assertEquals(expectedTransactionMessage, actualTransactionMessage);

        //Проверяем значение по-умолчанию в выпадающем списке аккаунтов
        transferPage.getAccountSelectorInRepeatModal().options().findBy(exactText(DEFAULT_TEXT_IN_ACCOUNT_LIST_SELECTOR)).shouldBe(visible);

        //Проверяем количество аккаунтов в выпадающем списке
        transferPage.getAccountSelectorInRepeatModal().options().shouldHave(size(expectedListSize));

        //Выбираем аккаунт из выпадающего списка
        transferPage.getAccountSelectorInRepeatModal().click();
        transferPage.getAccountSelectorInRepeatModal().selectOptionByValue(String.valueOf(userAccount));

        //Проверяем отображаемый аккаунт в списке
        final String actualAccountInfoInList = transferPage.getAccountSelectorInRepeatModal().getSelectedOptionText();
        final String expectedAccountInfoInList = getAccountInfoList(authUserToken, userAccount);
        Assertions.assertEquals(expectedAccountInfoInList, actualAccountInfoInList);

        //Нажимаем кнопку Cancel для закрытия Repeat окна
        transferPage.getCancelTransferInRepeatModal().click();

        //Проверяем, что окно закрылось
        transferPage.getTransferModalTitleInRepeatModal().shouldBe(not(visible));

        //Нажимаем кнопку Repeat для транзакции Deposit и проверяем, что открылось модальное окно повтора транзакции
        transferPage.clickRepeatTransaction(Operations.DEPOSIT, randomMoneyDeposit);
        transferPage.getTransferModalTitleInRepeatModal().shouldBe(visible);

        //Проверяем отображаемый аккаунт в списке, что он не сброшен после нажатия Cancel
        final String actualAccountInfoInListAfterCancel = transferPage.getAccountSelectorInRepeatModal().getSelectedOptionText();
        Assertions.assertEquals(actualAccountInfoInListAfterCancel, actualAccountInfoInList);

        //Нажимаем на иконку крестика для закрытия Repeat окна
        transferPage.getCloseTransferInRepeatModal().click();

        //Проверяем, что окно закрылось
        transferPage.getTransferModalTitleInRepeatModal().shouldBe(not(visible));

    }
}
