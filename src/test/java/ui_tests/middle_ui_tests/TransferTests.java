package ui_tests.middle_ui_tests;

import api.config.AccountData;
import api.config.Operations;
import api.models.ChangeUserRequest;
import api.models.UsersResponse;
import api.requests.skelethon.EndpointRequests;
import api.requests.skelethon.requesters.ValidatableCrudRequester;
import api.requests.steps.admin_steps.AdminSteps;
import api.requests.steps.user_steps.UserSteps;
import api.specs.RequestSpecs;
import api.specs.ResponseSpecs;
import api.utils.RandomData;
import api.utils.RandomModelGenerator;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ui.pages.DepositPage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ui.pages.AlertMessages.*;
import static ui.pages.BasePage.getAccountInfoList;

public class TransferTests extends BaseTestMiddle {

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

        //Открываем страницу выполнения трансфера
        // Проверяем лого страницы Transfer Money
        // Проверяем значение по-умолчанию в выпадающем списке Select Account:
        // Проверяем размер списка
        // Открываем выпадающий список
        // Выбираем созданный аккаунт
        // Проверяем отображение выбранного ID аккаунта и баланса аккаунта пользователя
        // Проверяем отображение placeholder в поле Recipient Name
        //Указываем имя второго пользователя в поле Recipient Name
        // Проверяем отображение placeholder в поле Recipient Account Number
        //Указываем аккаунт второго пользователя в поле Recipient Account Number
        //Вводим сумму для перевода равную зачисленным деньгам в поле Amount
        // Проверяем, что чекбокс Confirm details are correct по-умолчанию не активен
        //Активируем чекбокс подтверждая данные
        //Нажимаем кнопку Transfer
        transferPage
                .open()
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(userAccount)
                .checkSelectedAccountInList(authUserToken, userAccount)
                .inputRecipientName(changeUserRequest.getName())
                .inputRecipientAccount(accountSecondUser)
                .inputAmountValue(expectedRandomMoney)
                .checkConfirmCheckboxUnchecked()
                .clickConfirmCheckboxToChecked()
                .clickTransferButton();


        // Проверяем сообщение в модальном окне и закрываем его
        final String expectedAlertText = transferPage.expectedSuccessfulTransferModalMessage(expectedRandomMoney, accountSecondUser);
        transferPage.checkMessageFromModalPageAndAccept(expectedAlertText);

        //Проверяем, что остались на той же странице
        //Проверяем, что в списке выбранный аккаунт не сброшен
        //Проверяем, что поле Recipient Name очистилось
        //Проверяем, что поле Recipient Account Number очистилось
        //Проверяем, что поле Amount очистилось
        //Проверяем, что чекбокс Confirm details are correct не активен
        transferPage
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkRecipientNameDefaultValue().checkRecipientAccountDefaultValue()
                .checkAmountDefaultValue()
                .checkConfirmCheckboxUnchecked();

        //Проверяем, что баланс аккаунта второго пользователя пополнился после перевода
        final double actualSecondUserBalance = UserSteps.getUserBalance(secondUserToken, accountSecondUser);
        assertThat(actualSecondUserBalance).isEqualTo(expectedRandomMoney);

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
        // Проверяем значение по-умолчанию в выпадающем списке Select Account
        //Проверяем размер списка
        //Выбираем первый аккаунт пользователя
        //Проверяем отображаемый аккаунт в списке
        //Указываем рандомное имя пользователя в поле Recipient Name
        //Указываем второй аккаунт пользователя в поле Recipient Account Number
        //Вводим сумму для перевода равную зачисленным деньгам в поле Amount
        //Активируем чекбокс подтверждая данные
        //Нажимаем кнопку Transfer
        transferPage
                .open()
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(userAccount)
                .checkSelectedAccountInList(authUserToken, userAccount)
                .inputRecipientName(RandomData.randomName(3))
                .inputRecipientAccount(userAccountSecond)
                .inputAmountValue(expectedRandomMoney)
                .clickConfirmCheckboxToChecked()
                .clickTransferButton();


        // Проверяем сообщение в модальном окне и закрываем его
        final String expectedAlertText = transferPage.expectedSuccessfulTransferModalMessage(expectedRandomMoney, userAccountSecond);
        transferPage.checkMessageFromModalPageAndAccept(expectedAlertText);

        //Проверяем, что остались на той же странице
        //Проверяем, что в списке выбранный аккаунт не сброшен
        //Проверяем, что поле Recipient Name очистилось
        //Проверяем, что поле Recipient Account Number очистилось
        //Проверяем, что поле Amount очистилось
        //Проверяем, что чекбокс Confirm details are correct не активен
        transferPage
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkRecipientNameDefaultValue()
                .checkRecipientAccountDefaultValue()
                .checkAmountDefaultValue()
                .checkConfirmCheckboxUnchecked();

        //Проверяем, что баланс второго аккаунта основного пользователя пополнился после перевода
        final double actualFirstUserBalance = UserSteps.getUserBalance(authUserToken, userAccountSecond);
        assertThat(actualFirstUserBalance).isEqualTo(expectedRandomMoney);
    }

    @Test
    @DisplayName("Позитивный тест: проверка возможности перевода денег на тот же аккаунт с которого происходит перевод")
    public void userCanTransferMoneyToSameAccount() {

        int expectedListSize = 2;

        //Пополняем аккаунт основного пользователя
        final double expectedRandomMoney = RandomData.getMoney();
        UserSteps.depositMoney(authUserToken, userAccount, expectedRandomMoney);

        //Переходим на страницу трансфера и проверяем, что отображается название этой страницы
        //Проверяем значение по-умолчанию в выпадающем списке Select Account
        //Проверяем размер списка
        //Выбираем аккаунт пользователя
        //Проверяем отображаемый аккаунт в списке
        //Указываем рандомное имя пользователя в поле Recipient Name
        //Указываем тот аккаунт пользователя в поле Recipient Account Number
        //Вводим сумму для перевода равную зачисленным деньгам в поле Amount
        //Активируем чекбокс подтверждая данные
        //Нажимаем кнопку Transfer
        transferPage
                .open()
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(userAccount)
                .checkSelectedAccountInList(authUserToken, userAccount)
                .inputRecipientName(RandomData.randomName(3))
                .inputRecipientAccount(userAccount)
                .inputAmountValue(expectedRandomMoney)
                .clickConfirmCheckboxToChecked()
                .clickTransferButton();


        // Проверяем сообщение в модальном окне об успешности выполнения Deposit и закрываем его
        final String expectedAlertText = transferPage.expectedSuccessfulTransferModalMessage(expectedRandomMoney, userAccount);
        transferPage.checkMessageFromModalPageAndAccept(expectedAlertText);

        //Проверяем, что остались на той же странице
        //Проверяем, что в списке выбранный аккаунт не сброшен
        //Проверяем, что поле Recipient Name очистилось
        //Проверяем, что поле Recipient Account Number очистилось
        //Проверяем, что поле Amount очистилось
        //Проверяем, что чекбокс Confirm details are correct не активен
        transferPage
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkRecipientNameDefaultValue()
                .checkRecipientAccountDefaultValue()
                .checkAmountDefaultValue()
                .checkConfirmCheckboxUnchecked();

        //Проверяем, что баланс аккаунта пользователя не изменился после перевода самому себе
        final double actualSecondUserBalance = UserSteps.getUserBalance(authUserToken, userAccount);
        assertThat(actualSecondUserBalance).isEqualTo(expectedRandomMoney);

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
        //Проверяем значение по-умолчанию в выпадающем списке Select Account
        //Проверяем размер списка
        //Выбираем аккаунт пользователя
        //Проверяем отображаемый аккаунт в списке
        //Указываем рандомное имя пользователя в поле Recipient Name
        //Указываем тот аккаунт пользователя в поле Recipient Account Number
        //Вводим отрицательное количество денег для перевода равную зачисленным деньгам в поле Amount
        //Активируем чекбокс подтверждая данные
        //Нажимаем кнопку Transfer
        transferPage
                .open()
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(userAccount)
                .checkSelectedAccountInList(authUserToken, userAccount)
                .inputRecipientName(changeUserRequest.getName())
                .inputRecipientAccount(accountSecondUser)
                .inputAmountValue(negativeMoney)
                .clickConfirmCheckboxToChecked()
                .clickTransferButton();

        final String expectedAccountInfoInList = getAccountInfoList(authUserToken, userAccount);

        // Проверяем сообщение в модальном окне и закрываем его
        //Проверяем, что остались на той же странице
        //Проверяем, что в списке выбранный аккаунт не сброшен
        //Проверяем, что значение в поле Recipient Name не сброшено
        //Проверяем, что значение в поле Recipient Account Number не сброшено
        //Проверяем, что значение в поле Amount не сброшено
        //Проверяем, что чекбокс Confirm details are correct остался активен

        transferPage
                .checkMessageFromModalPageAndAccept(TRANSFER_ERROR_NEGATIVE_VALUE.getValue())
                .checkTransferPageOpened()
                .checkSelectedAccountDoesntChange(expectedAccountInfoInList)
                .checkRecipientNameDoesntChange(changeUserRequest.getName())
                .checkRecipientAccountDoesntChange(accountSecondUser)
                .checkAmountValueDoesntChange(negativeMoney)
                .checkConfirmCheckboxChecked();


        //Проверяем, что баланс аккаунта первого пользователя не изменился
        final double actualUserBalance = UserSteps.getUserBalance(authUserToken, userAccount);
        assertThat(actualUserBalance).isEqualTo(expectedRandomMoney);

        //Проверяем, что баланс аккаунта второго пользователя не изменился
        final double actualSecondUserBalance = UserSteps.getUserBalance(secondUserToken, accountSecondUser);
        assertThat(actualSecondUserBalance).isEqualTo(expectedZeroBalance);
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
        //Проверяем значение по-умолчанию в выпадающем списке Select Account
        //Проверяем размер списка
        //Выбираем аккаунт пользователя
        //Проверяем отображаемый аккаунт в списке
        //Указываем рандомное имя пользователя в поле Recipient Name
        //Указываем тот аккаунт пользователя в поле Recipient Account Number
        //Вводим количество денег превышающее максимальное значение для перевода
        //Активируем чекбокс подтверждая данные
        //Нажимаем кнопку Transfer
        transferPage
                .open()
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(userAccount)
                .checkSelectedAccountInList(authUserToken, userAccount)
                .inputRecipientName(changeUserRequest.getName())
                .inputRecipientAccount(accountSecondUser)
                .inputAmountValue(moreMaximumLimitValueMoney)
                .clickConfirmCheckboxToChecked()
                .clickTransferButton();

        final String expectedAccountInfoInList = getAccountInfoList(authUserToken, userAccount);

        // Проверяем сообщение в модальном окне и закрываем его
        //Проверяем, что остались на той же странице
        //Проверяем, что в списке выбранный аккаунт не сброшен
        //Проверяем, что значение в поле Recipient Name не сброшено
        //Проверяем, что значение в поле Recipient Account Number не сброшено
        //Проверяем, что значение в поле Amount не сброшено
        //Проверяем, что чекбокс Confirm details are correct остался активен
        transferPage
                .checkMessageFromModalPageAndAccept(TRANSFER_ERROR_EXCEEDED_MAXIMUM_VALUE.getValue())
                .checkTransferPageOpened()
                .checkSelectedAccountDoesntChange(expectedAccountInfoInList)
                .checkRecipientNameDoesntChange(changeUserRequest.getName())
                .checkRecipientAccountDoesntChange(accountSecondUser)
                .checkAmountValueDoesntChange(moreMaximumLimitValueMoney)
                .checkConfirmCheckboxChecked();

        //Проверяем, что баланс аккаунта первого пользователя не изменился
        final double actualUserBalance = UserSteps.getUserBalance(authUserToken, userAccount);
        assertThat(actualUserBalance).isEqualTo(expectedRandomMoney);

        //Проверяем, что баланс аккаунта второго пользователя не изменился
        final double actualSecondUserBalance = UserSteps.getUserBalance(secondUserToken, accountSecondUser);
        assertThat(actualSecondUserBalance).isEqualTo(expectedZeroBalance);
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
        //Проверяем значение по-умолчанию в выпадающем списке Select Account
        //Проверяем размер списка
        //Выбираем аккаунт пользователя
        //Проверяем отображаемый аккаунт в списке
        //Указываем рандомное имя пользователя в поле Recipient Name
        //Указываем тот аккаунт пользователя в поле Recipient Account Number
        //Вводим нулевое количество денег для перевода
        //Активируем чекбокс подтверждая данные
        //Нажимаем кнопку Transfer
        transferPage
                .open()
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(userAccount)
                .checkSelectedAccountInList(authUserToken, userAccount)
                .inputRecipientName(changeUserRequest.getName())
                .inputRecipientAccount(accountSecondUser)
                .inputAmountValue(expectedZeroBalance)
                .clickConfirmCheckboxToChecked()
                .clickTransferButton();

        final String expectedAccountInfoInList = getAccountInfoList(authUserToken, userAccount);

        // Проверяем сообщение в модальном окне и закрываем его
        //Проверяем, что остались на той же странице
        //Проверяем, что в списке выбранный аккаунт не сброшен
        //Проверяем, что значение в поле Recipient Name не сброшено
        //Проверяем, что значение в поле Recipient Account Number не сброшено
        //Проверяем, что значение в поле Amount не сброшено
        //Проверяем, что чекбокс Confirm details are correct остался активен

        transferPage
                .checkMessageFromModalPageAndAccept(TRANSFER_ERROR_NEGATIVE_VALUE.getValue())
                .checkTransferPageOpened()
                .checkSelectedAccountDoesntChange(expectedAccountInfoInList)
                .checkRecipientNameDoesntChange(changeUserRequest.getName())
                .checkRecipientAccountDoesntChange(accountSecondUser)
                .checkAmountValueDoesntChange(expectedZeroBalance)
                .checkConfirmCheckboxChecked();

        //Проверяем, что баланс аккаунта первого пользователя не изменился
        final double actualUserBalance = UserSteps.getUserBalance(authUserToken, userAccount);
        assertThat(actualUserBalance).isEqualTo(expectedZeroBalance);

        //Проверяем, что баланс аккаунта второго пользователя не изменился
        final double actualSecondUserBalance = UserSteps.getUserBalance(secondUserToken, accountSecondUser);
        assertThat(actualSecondUserBalance).isEqualTo(expectedZeroBalance);
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
        //Не заполняем ни одно из полей и сразу нажимаем кнопку Transfer
        // Проверяем сообщение в модальном окне и закрываем его
        //Проверяем, что остались на той же странице
        //Проверяем значение по-умолчанию в выпадающем списке Select Account
        //Проверяем размер списка
        //Выбираем аккаунт пользователя
        //Проверяем отображаемый аккаунт в списке
        //Остальные поля не заполняем и нажимаем кнопку Transfer
        transferPage
                .open()
                .checkTransferPageOpened()
                .clickTransferButton()
                .checkMessageFromModalPageAndAccept(TRANSFER_ERROR_WITHOUT_REQUIRED_FIELDS.getValue())
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(userAccount)
                .checkSelectedAccountInList(authUserToken, userAccount)
                .clickTransferButton();

        final String expectedAccountInfoInList = getAccountInfoList(authUserToken, userAccount);

        // Проверяем сообщение в модальном окне и закрываем его
        //Проверяем, что остались на той же странице
        //Проверяем, что в списке выбранный аккаунт не сброшен
        //Указываем имя второго пользователя в поле Recipient Name
        //Остальные поля не заполняем и нажимаем кнопку Transfer
        // Проверяем сообщение в модальном окне и закрываем его
        //Проверяем, что остались на той же странице
        //Проверяем, что значение в поле Recipient Name не сброшено
        //Указываем аккаунт второго пользователя в поле Recipient Account Number
        //Остальные поля не заполняем и нажимаем кнопку Transfer
        // Проверяем сообщение в модальном окне и закрываем его
        //Проверяем, что остались на той же странице
        //Проверяем, что значение в поле Recipient Account Number не сброшено
        //Вводим сумму для перевода равную зачисленным деньгам в поле Amount
        //Остальные поля не заполняем и нажимаем кнопку Transfer
        // Проверяем сообщение в модальном окне и закрываем его
        //Проверяем, что остались на той же странице
        //Проверяем, что значение в поле Amount не сброшено
        //Активируем чекбокс подтверждая данные
        transferPage
                .checkMessageFromModalPageAndAccept(TRANSFER_ERROR_WITHOUT_REQUIRED_FIELDS.getValue())
                .checkTransferPageOpened()
                .checkSelectedAccountDoesntChange(expectedAccountInfoInList)
                .inputRecipientName(changeUserRequest.getName())
                .clickTransferButton()
                .checkMessageFromModalPageAndAccept(TRANSFER_ERROR_WITHOUT_REQUIRED_FIELDS.getValue())
                .checkTransferPageOpened()
                .checkRecipientNameDoesntChange(changeUserRequest.getName())
                .inputRecipientAccount(accountSecondUser)
                .clickTransferButton()
                .checkMessageFromModalPageAndAccept(TRANSFER_ERROR_WITHOUT_REQUIRED_FIELDS.getValue())
                .checkTransferPageOpened()
                .checkRecipientAccountDoesntChange(accountSecondUser)
                .inputAmountValue(expectedRandomMoney)
                .clickTransferButton()
                .checkMessageFromModalPageAndAccept(TRANSFER_ERROR_WITHOUT_REQUIRED_FIELDS.getValue())
                .checkTransferPageOpened()
                .checkAmountValueDoesntChange(expectedRandomMoney)
                .clickConfirmCheckboxToChecked()
                .clickTransferButton();

        // Проверяем сообщение в модальном окне и закрываем его
        final String expectedSuccessfulAlertText = transferPage.expectedSuccessfulTransferModalMessage(expectedRandomMoney, accountSecondUser);
        transferPage.checkMessageFromModalPageAndAccept(expectedSuccessfulAlertText);

        //Проверяем, что баланс аккаунта второго пользователя пополнился после перевода
        final double actualSecondUserBalance = UserSteps.getUserBalance(secondUserToken, accountSecondUser);
        assertThat(actualSecondUserBalance).isEqualTo(expectedRandomMoney);
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
        //Проверяем значение по-умолчанию в выпадающем списке Select Account
        //Проверяем размер списка
        //Выбираем аккаунт пользователя
        //Проверяем отображаемый аккаунт в списке
        //Указываем рандомное имя пользователя в поле Recipient Name
        //Указываем несуществующий аккаунт ID пользователя в поле Recipient Account Number
        //Вводим рандомное количество денег для перевода
        //Активируем чекбокс подтверждая данные
        //Нажимаем кнопку Transfer
        transferPage
                .open()
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(userAccount)
                .checkSelectedAccountInList(authUserToken, userAccount)
                .inputRecipientName(randomRecipientName)
                .inputRecipientAccount(maxExistedAccountId + 1)
                .inputAmountValue(expectedRandomMoney)
                .clickConfirmCheckboxToChecked()
                .clickTransferButton();

        final String expectedAccountInfoInList = getAccountInfoList(authUserToken, userAccount);

        // Проверяем сообщение в модальном окне и закрываем его
        //Проверяем, что остались на той же странице
        //Проверяем, что в списке выбранный аккаунт не сброшен
        //Проверяем, что значение в поле Recipient Name не сброшено
        //Проверяем, что значение в поле Recipient Account Number не сброшено
        //Проверяем, что значение в поле Amount не сброшено
        //Проверяем, что чекбокс Confirm details are correct остался активен

        transferPage
                .checkMessageFromModalPageAndAccept(TRANSFER_ERROR_UNEXISTED_ACCOUNT.getValue())
                .checkTransferPageOpened()
                .checkSelectedAccountDoesntChange(expectedAccountInfoInList)
                .checkRecipientNameDoesntChange(randomRecipientName)
                .checkRecipientAccountDoesntChange(maxExistedAccountId + 1)
                .checkAmountValueDoesntChange(expectedRandomMoney)
                .checkConfirmCheckboxChecked();

        //Проверяем, что баланс аккаунта основного пользователя не изменился
        final double actualSecondUserBalance = UserSteps.getUserBalance(authUserToken, userAccount);
        assertThat(actualSecondUserBalance).isEqualTo(expectedRandomMoney);
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

        //Открываем страницу выполнения трансфера
        // Проверяем лого страницы Transfer Money
        // Проверяем значение по-умолчанию в выпадающем списке Select Account:
        // Проверяем размер списка
        // Открываем выпадающий список
        // Выбираем созданный аккаунт
        // Проверяем отображение выбранного ID аккаунта и баланса аккаунта пользователя
        // Проверяем отображение placeholder в поле Recipient Name
        // Указываем имя второго пользователя в поле Recipient Name верхним регистром
        // Проверяем отображение placeholder в поле Recipient Account Number
        // Указываем аккаунт второго пользователя в поле Recipient Account Number
        // Вводим сумму для перевода равную зачисленным деньгам в поле Amount
        // Проверяем, что чекбокс Confirm details are correct по-умолчанию не активен
        // Активируем чекбокс подтверждая данные
        // Нажимаем кнопку Transfer
        transferPage
                .open()
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(userAccount)
                .checkSelectedAccountInList(authUserToken, userAccount)
                .inputRecipientName(changeUserRequest.getName().toUpperCase())
                .inputRecipientAccount(accountSecondUser)
                .inputAmountValue(expectedRandomMoney)
                .checkConfirmCheckboxUnchecked()
                .clickConfirmCheckboxToChecked()
                .clickTransferButton();

        final String expectedAccountInfoInList = getAccountInfoList(authUserToken, userAccount);

        // Проверяем сообщение в модальном окне и закрываем его
        //Проверяем, что остались на той же странице
        //Проверяем, что в списке выбранный аккаунт не сброшен
        //Проверяем, что значение в поле Recipient Name не сброшено
        //Проверяем, что значение в поле Recipient Account Number не сброшено
        //Проверяем, что значение в поле Amount не сброшено
        //Проверяем, что чекбокс Confirm details are correct остался активен
        transferPage
                .checkMessageFromModalPageAndAccept(TRANSFER_ERROR_RECIPIENT_NAME_ANOTHER_CASE.getValue())
                .checkTransferPageOpened()
                .checkSelectedAccountDoesntChange(expectedAccountInfoInList)
                .checkRecipientNameDoesntChange(changeUserRequest.getName().toUpperCase())
                .checkRecipientAccountDoesntChange(accountSecondUser)
                .checkAmountValueDoesntChange(expectedRandomMoney)
                .checkConfirmCheckboxChecked();

        //Проверяем, что баланс аккаунта основного пользователя не изменился
        final double actualUserBalance = UserSteps.getUserBalance(authUserToken, userAccount);
        assertThat(actualUserBalance).isEqualTo(expectedRandomMoney);

        //Проверяем, что баланс аккаунта второго пользователя не изменился
        final double actualSecondUserBalance = UserSteps.getUserBalance(secondUserToken, accountSecondUser);
        assertThat(actualSecondUserBalance).isEqualTo(zeroBalance);
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

        //Открываем страницу выполнения трансфера
        // Проверяем лого страницы Transfer Money
        // Проверяем значение по-умолчанию в выпадающем списке Select Account:
        // Проверяем размер списка
        // Открываем выпадающий список
        // Выбираем созданный аккаунт
        // Проверяем отображение выбранного ID аккаунта и баланса аккаунта пользователя
        // Проверяем отображение placeholder в поле Recipient Name
        // Указываем имя второго пользователя в поле Recipient Name
        // Проверяем отображение placeholder в поле Recipient Account Number
        // Указываем аккаунт второго пользователя в поле Recipient Account Number нижним регистром
        // Вводим сумму для перевода равную зачисленным деньгам в поле Amount
        // Проверяем, что чекбокс Confirm details are correct по-умолчанию не активен
        // Активируем чекбокс подтверждая данные
        // Нажимаем кнопку Transfer
        transferPage
                .open()
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(userAccount)
                .checkSelectedAccountInList(authUserToken, userAccount)
                .inputRecipientName(changeUserRequest.getName())
                .inputRecipientAccount(AccountData.ACCOUNT_NUMBER_PREFIX.getValue().toLowerCase() + accountSecondUser)
                .inputAmountValue(expectedRandomMoney)
                .checkConfirmCheckboxUnchecked()
                .clickConfirmCheckboxToChecked()
                .clickTransferButton();

        final String expectedAccountInfoInList = getAccountInfoList(authUserToken, userAccount);

        // Проверяем сообщение в модальном окне и закрываем его
        //Проверяем, что остались на той же странице
        transferPage
                .checkMessageFromModalPageAndAccept(TRANSFER_ERROR_UNEXISTED_ACCOUNT.getValue())
                .checkTransferPageOpened();

        //Проверяем, что в списке выбранный аккаунт не сброшен
        final String actualAccountInfoInListAfterTransfer = transferPage.getAccountSelector().getSelectedOptionText();
        assertThat(actualAccountInfoInListAfterTransfer).isEqualTo(expectedAccountInfoInList);

        //Проверяем, что остались на той же странице
        //Проверяем, что в списке выбранный аккаунт не сброшен
        //Проверяем, что значение в поле Recipient Name не сброшено
        //Проверяем, что значение в поле Recipient Account Number не сброшено
        //Проверяем, что значение в поле Amount не сброшено
        //Проверяем, что чекбокс Confirm details are correct остался активен
        transferPage
                .checkTransferPageOpened()
                .checkSelectedAccountDoesntChange(expectedAccountInfoInList)
                .checkRecipientNameDoesntChange(changeUserRequest.getName())
                .checkRecipientAccountDoesntChange(AccountData.ACCOUNT_NUMBER_PREFIX.getValue().toLowerCase() + accountSecondUser)
                .checkAmountValueDoesntChange(expectedRandomMoney)
                .checkConfirmCheckboxChecked();

        //Проверяем, что баланс аккаунта основного пользователя не изменился
        final double actualUserBalance = UserSteps.getUserBalance(authUserToken, userAccount);
        assertThat(actualUserBalance).isEqualTo(expectedRandomMoney);

        //Проверяем, что баланс аккаунта второго пользователя не изменился
        final double actualSecondUserBalance = UserSteps.getUserBalance(secondUserToken, accountSecondUser);
        assertThat(actualSecondUserBalance).isEqualTo(zeroBalance);
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
        //Переходим на вкладку TransferAgain и проверяем, что отображается строка поиска
        //Проверяем, что список транзакций пуст
        transferPage
                .open()
                .checkTransferPageOpened()
                .openTransferAgainTab()
                .checkTransferAgainPageOpened()
                .checkTransactionsListSize(expectedTransactions);

        //Пополняем оба аккаунта пользователя разными суммами
        UserSteps.depositMoney(authUserToken, userAccount, randomMoneyForFirstAccount);
        UserSteps.depositMoney(authUserToken, userAccountSecond, randomMoneyForSecondAccount);

        //Выполняем рефреш
        Selenide.refresh();

        //Проверяем, что отображается название страницы переводов
        //Переходим на вкладку TransferAgain и проверяем, что отображается строка поиска
        //Проверяем, что список транзакций содержит 2 транзакции
        expectedTransactions = 2;
        transferPage
                .checkTransferPageOpened()
                .openTransferAgainTab()
                .checkTransferAgainPageOpened()
                .checkTransactionsListSize(expectedTransactions);

        //Проверяем, что каждая транзакция содержит тип DEPOSIT и сумма, которой пополняется каждый аккаунт
        final List<String> transactionsTextDeposit = transferPage.getTransactionsText();
        assertThat(transferPage.checkTransaction(transactionsTextDeposit, randomMoneyForFirstAccount, Operations.DEPOSIT)).isTrue();
        assertThat(transferPage.checkTransaction(transactionsTextDeposit, randomMoneyForSecondAccount, Operations.DEPOSIT)).isTrue();

        //Переходим обратно на вкладку New Transfer и проверяем, что отображается наименование вкладки
        // Проверяем значение по-умолчанию в выпадающем списке Select Account
        //Проверяем размер списка
        //Выбираем первый аккаунт пользователя
        //Проверяем отображаемый аккаунт в списке
        //Указываем рандомное пользователя в поле Recipient Name
        //Указываем второй аккаунт пользователя в поле Recipient Account Number
        //Вводим сумму для перевода равную зачисленным деньгам в поле Amount
        //Активируем чекбокс подтверждая данные
        //Нажимаем кнопку Transfer
        transferPage
                .openNewTransferTab()
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(userAccount)
                .checkSelectedAccountInList(authUserToken, userAccount)
                .inputRecipientName(RandomData.randomName(3))
                .inputRecipientAccount(userAccountSecond)
                .inputAmountValue(randomMoneyForFirstAccount)
                .clickConfirmCheckboxToChecked()
                .clickTransferButton();

        // Проверяем сообщение в модальном окне об успешности выполнения Transfer и закрываем его
        final String expectedAlertText =
                transferPage.expectedSuccessfulTransferModalMessage(randomMoneyForFirstAccount, userAccountSecond);
        transferPage.checkMessageFromModalPageAndAccept(expectedAlertText);

        //Выполняем рефреш, чтобы появились транзакции по переводу
        Selenide.refresh();

        //Переходим на вкладку Transfer Again и проверяем, что отображается строка поиска
        //Проверяем, что список транзакций содержит 4 транзакции
        expectedTransactions = 4;
        transferPage
                .openTransferAgainTab()
                .checkTransferAgainPageOpened()
                .checkTransactionsListSize(expectedTransactions);

        //Проверяем, что две транзакции содержат тип DEPOSIT и сумму, которой пополнялся каждый аккаунт
        //Проверяем, что остальные две транзакции содержат тип TRANSFER_IN и TRANSFER_OUT и сумму перевода
        final List<String> transactionsTextTransfer = transferPage.getTransactionsText();
        assertThat(transferPage.checkTransaction(transactionsTextTransfer, randomMoneyForFirstAccount, Operations.DEPOSIT)).isTrue();
        assertThat(transferPage.checkTransaction(transactionsTextTransfer, randomMoneyForSecondAccount, Operations.DEPOSIT)).isTrue();
        assertThat(transferPage.checkTransaction(transactionsTextTransfer, randomMoneyForFirstAccount, Operations.TRANSFER_OUT)).isTrue();
        assertThat(transferPage.checkTransaction(transactionsTextTransfer, randomMoneyForFirstAccount, Operations.TRANSFER_IN)).isTrue();
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
        //Проверяем значение по-умолчанию в выпадающем списке Select Account
        //Проверяем размер списка
        //Выбираем первый аккаунт пользователя
        //Проверяем отображаемый аккаунт в списке
        //Указываем рандомное имя пользователя в поле Recipient Name
        //Указываем второй аккаунт пользователя в поле Recipient Account Number
        //Вводим сумму для перевода равную зачисленным деньгам в поле Amount
        //Активируем чекбокс подтверждая данные
        //Нажимаем кнопку Transfer

        transferPage
                .open()
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(userAccount)
                .checkSelectedAccountInList(authUserToken, userAccount)
                .inputRecipientName(RandomData.randomName(3))
                .inputRecipientAccount(userAccountSecond)
                .inputAmountValue(randomMoney)
                .clickConfirmCheckboxToChecked()
                .clickTransferButton();


        // Проверяем сообщение в модальном окне и закрываем его
        final String expectedAlertText =
                transferPage.expectedSuccessfulTransferModalMessage(randomMoney, userAccountSecond);
        transferPage.checkMessageFromModalPageAndAccept(expectedAlertText);

        //Выполняем рефреш, чтобы появились транзакции по переводу
        //Переходим на вкладку Transfer Again и проверяем, что отображается строка поиска
        //Проверяем, что список транзакций содержит 3 транзакции
        //В поле 'Search by Username or Name' вводим username пользователя
        //Нажимаем кнопку 'Search Transactions'
        //Проверяем, что список транзакций содержит 3 транзакции
        expectedTransactions = 3;
        Selenide.refresh();
        transferPage
                .checkTransferPageOpened()
                .openTransferAgainTab()
                .checkTransferAgainPageOpened()
                .checkTransactionsListSize(expectedTransactions)
                .inputValueInSearchField(userInfo.getUsername())
                .clickSearchTransactionsButton()
                .checkTransactionsListSize(expectedTransactions);


        //Проверяем, что одна транзакция содержит тип DEPOSIT, сумму и username пользователя
        //Проверяем, что остальные две транзакции содержат тип TRANSFER_IN и TRANSFER_OUT, сумму перевода и username пользователя
        final List<String> transactionsTextTransfer = transferPage.getTransactionsText();
        assertThat(transferPage.checkTransaction(transactionsTextTransfer, randomMoney, Operations.DEPOSIT, userInfo.getUsername())).isTrue();
        assertThat(transferPage.checkTransaction(transactionsTextTransfer, randomMoney, Operations.TRANSFER_OUT, userInfo.getUsername())).isTrue();
        assertThat(transferPage.checkTransaction(transactionsTextTransfer, randomMoney, Operations.TRANSFER_IN, userInfo.getUsername())).isTrue();

        //Для пользователя задаём имя
        final ChangeUserRequest changeUserRequest = RandomModelGenerator.generate(ChangeUserRequest.class);

        new ValidatableCrudRequester<UsersResponse>(RequestSpecs.withToken(authUserToken), EndpointRequests.UPDATE_USER, ResponseSpecs.requestReturnsOk())
                .PUT(changeUserRequest);

        //Если сделать следующие шаги сразу после изменения имени без рефреша, то при попытке
        //поиска отобразится модальное окно с ошибкой. Ввиду отсутствия требования будет выполняться рефреш
        //Вновь переходим на вкладку Transfer Again
        //В поле 'Search by Username or Name' вводим name пользователя
        //Нажимаем кнопку 'Search Transactions'
        //Проверяем, что список транзакций содержит 3 транзакции
        Selenide.refresh();
        transferPage
                .checkTransferPageOpened()
                .openTransferAgainTab()
                .inputValueInSearchField(changeUserRequest.getName())
                .clickSearchTransactionsButton()
                .checkTransactionsListSize(expectedTransactions);

        //Проверяем, что одна транзакция содержит тип DEPOSIT, сумму и username пользователя
        //Проверяем, что остальные две транзакции содержат тип TRANSFER_IN и TRANSFER_OUT, сумму перевода и name пользователя
        final List<String> transactionsTextName = transferPage.getTransactionsText();

        assertThat(transferPage.checkTransaction(transactionsTextName, randomMoney, Operations.DEPOSIT, changeUserRequest.getName())).isTrue();
        assertThat(transferPage.checkTransaction(transactionsTextName, randomMoney, Operations.TRANSFER_OUT, changeUserRequest.getName())).isTrue();
        assertThat(transferPage.checkTransaction(transactionsTextName, randomMoney, Operations.TRANSFER_IN, changeUserRequest.getName())).isTrue();

        //В поле 'Search by Username or Name' вводим username пользователя верхним регистром
        //Нажимаем кнопку 'Search Transactions'
        //Проверяем, что список транзакций содержит 3 транзакции
        transferPage
                .inputValueInSearchField(userInfo.getUsername().toUpperCase())
                .clickSearchTransactionsButton()
                .checkTransactionsListSize(expectedTransactions);

        //Проверяем, что одна транзакция содержит тип DEPOSIT, сумму и username пользователя
        //Проверяем, что остальные две транзакции содержат тип TRANSFER_IN и TRANSFER_OUT, сумму перевода и name пользователя

        final List<String> transactionsTextUsernameUpperCase = transferPage.getTransactionsText();

        assertThat(transferPage.checkTransaction(transactionsTextUsernameUpperCase, randomMoney, Operations.DEPOSIT, changeUserRequest.getName())).isTrue();
        assertThat(transferPage.checkTransaction(transactionsTextUsernameUpperCase, randomMoney, Operations.TRANSFER_OUT, changeUserRequest.getName())).isTrue();
        assertThat(transferPage.checkTransaction(transactionsTextUsernameUpperCase, randomMoney, Operations.TRANSFER_IN, changeUserRequest.getName())).isTrue();

        //В поле 'Search by Username or Name' вводим username пользователя частично
        //Нажимаем кнопку 'Search Transactions'
        //Проверяем, что список транзакций содержит 3 транзакции
        transferPage
                .inputValueInSearchField(userInfo.getUsername().substring(0, 2))
                .clickSearchTransactionsButton()
                .checkTransactionsListSize(expectedTransactions);

        //Проверяем, что одна транзакция содержит тип DEPOSIT, сумму и username пользователя
        //Проверяем, что остальные две транзакции содержат тип TRANSFER_IN и TRANSFER_OUT, сумму перевода и name пользователя

        final List<String> transactionsTextUsernamePartially = transferPage.getTransactionsText();

        assertThat(transferPage.checkTransaction(transactionsTextUsernamePartially, randomMoney, Operations.DEPOSIT, changeUserRequest.getName())).isTrue();
        assertThat(transferPage.checkTransaction(transactionsTextUsernamePartially, randomMoney, Operations.TRANSFER_OUT, changeUserRequest.getName())).isTrue();
        assertThat(transferPage.checkTransaction(transactionsTextUsernamePartially, randomMoney, Operations.TRANSFER_IN, changeUserRequest.getName())).isTrue();

        //В поле 'Search by Username or Name' вводим name не полностью, а лишь одно слово
        //Нажимаем кнопку 'Search Transactions'
        //Проверяем, что список транзакций содержит 3 транзакции
        transferPage
                .inputValueInSearchField(changeUserRequest.getName().split(" ")[0])
                .clickSearchTransactionsButton()
                .checkTransactionsListSize(expectedTransactions);

        //Проверяем, что одна транзакция содержит тип DEPOSIT, сумму и username пользователя
        //Проверяем, что остальные две транзакции содержат тип TRANSFER_IN и TRANSFER_OUT, сумму перевода и name пользователя

        final List<String> transactionsTextNamePartially = transferPage.getTransactionsText();

        assertThat(transferPage.checkTransaction(transactionsTextNamePartially, randomMoney, Operations.DEPOSIT, changeUserRequest.getName())).isTrue();
        assertThat(transferPage.checkTransaction(transactionsTextNamePartially, randomMoney, Operations.TRANSFER_OUT, changeUserRequest.getName())).isTrue();
        assertThat(transferPage.checkTransaction(transactionsTextNamePartially, randomMoney, Operations.TRANSFER_IN, changeUserRequest.getName())).isTrue();
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
        //Переходим на вкладку 'Transfer Again' и проверяем отображение кнопки поиска транзакций
        //В строке поиска указываем username второго пользователя
        transferPage
                .open()
                .checkTransferPageOpened()
                .openTransferAgainTab()
                .checkTransferAgainPageOpened()
                .inputValueInSearchField(secondUserInfo.getUsername())
                .clickSearchTransactionsButton();

        final List<String> transactionsTexts = transferPage.getTransactionsText();
        assertThat(transferPage.checkTransaction(transactionsTexts, randomMoney, Operations.TRANSFER_IN, secondUserInfo.getUsername())).isFalse();
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
        //Переходим на вкладку 'Transfer Again' и проверяем отображение кнопки поиска транзакций
        //В строке поиска указываем любое рандомное не существующее значение
        //Проверяем сообщение в модальном окне и закрываем его
        //Проверяем количество отображаемых транзакций
        transferPage
                .open()
                .checkTransferPageOpened()
                .openTransferAgainTab()
                .inputValueInSearchField(RandomData.randomName(15))
                .clickSearchTransactionsButton()
                .checkMessageFromModalPageAndAccept(TRANSFER_ERROR_UNEXISTED_NAME.getValue())
                .checkTransactionsListSize(expectedTransactions);
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
        //Переходим на вкладку Transfer Again и проверяем, что отображается строка поиска
        //Проверяем, что список транзакций содержит 3 транзакции
        //В поле 'Search by Username or Name' вводим username пользователя
        //Нажимаем кнопку 'Search Transactions'
        //Проверяем, что список транзакций содержит 3 транзакции
        transferPage
                .open()
                .checkTransferPageOpened()
                .openTransferAgainTab()
                .checkTransferAgainPageOpened()
                .checkTransactionsListSize(expectedTransactions)
                .inputValueInSearchField(userInfo.getUsername())
                .clickSearchTransactionsButton()
                .checkTransactionsListSize(expectedTransactions);

        //Проверяем, что одна транзакция содержит тип DEPOSIT, сумму и username пользователя
        //Проверяем, что остальные две транзакции содержат тип TRANSFER_IN и TRANSFER_OUT, сумму перевода и username пользователя
        final List<String> transactionsTextTransfer = transferPage.getTransactionsText();
        assertThat(transferPage.checkTransaction(transactionsTextTransfer, randomMoneyDeposit, Operations.DEPOSIT, userInfo.getUsername())).isTrue();
        assertThat(transferPage.checkTransaction(transactionsTextTransfer, randomMoneyTransfer, Operations.TRANSFER_OUT, userInfo.getUsername())).isTrue();
        assertThat(transferPage.checkTransaction(transactionsTextTransfer, randomMoneyTransfer, Operations.TRANSFER_IN, userInfo.getUsername())).isTrue();

        //Нажимаем кнопку Repeat для транзакции Deposit и проверяем, что открылось модальное окно повтора транзакции
        transferPage
                .clickRepeatTransaction(Operations.DEPOSIT, randomMoneyDeposit)
                .checkTransferModalTitleRepeatVisible();

        //Проверяем текст с подтверждением транзакции и номером аккаунтом на который ранее производилось пополнение
        final String actualTransactionMessage = transferPage.getTransactionInfoInRepeatModal().text();
        final String expectedTransactionMessage = TRANSACTION_MESSAGE_REPEAT_MODAL.getValue() + userAccount;
        assertThat(actualTransactionMessage).isEqualTo(expectedTransactionMessage);

        //Проверяем значение по-умолчанию в выпадающем списке аккаунтов
        //Проверяем количество аккаунтов в выпадающем списке
        //Выбираем первый аккаунт пользователя
        transferPage
                .checkDefaultValueInAccountListRepeatModal()
                .checkAccountSizeInRepeatModal(expectedListSize)
                .selectAccountInRepeatModal(userAccount);

        //Проверяем отображаемый аккаунт в списке
        final String actualAccountInfoInList = transferPage.getAccountSelectorInRepeatModal().getSelectedOptionText();
        final String expectedAccountInfoInList = getAccountInfoList(authUserToken, userAccount);
        assertThat(actualAccountInfoInList).isEqualTo(expectedAccountInfoInList);

        //Проверяем, что поле Amount содержит значение указанное в транзакции
        //Активируем чекбокс подтверждая данные
        transferPage
                .checkAmountValueFieldRepeatModal(randomMoneyDeposit)
                .clickConfirmCheckboxToChecked()
                .clickTransferButton();

        // Проверяем сообщение в модальном окне об успешности выполнения Deposit и закрываем его
        final String expectedAlertText =
                transferPage.getPage(DepositPage.class).expectedSuccessfullyDepositModalMessage(randomMoneyDeposit, userAccount);
        transferPage.checkMessageFromModalPageAndAccept(expectedAlertText);

        //Проверяем, что баланс первого аккаунта второго пользователя пополнился после перевода
        final double expectedBalanceFirstAccount = randomMoneyDeposit - randomMoneyTransfer + randomMoneyDeposit;
        final double actualSecondUserBalance = UserSteps.getUserBalance(secondUserToken, accountSecondUser);
        assertThat(actualSecondUserBalance).isEqualTo(expectedBalanceFirstAccount);
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
        //Переходим на вкладку Transfer Again и проверяем, что отображается строка поиска
        //Проверяем список транзакций
        //В поле 'Search by Username or Name' вводим username пользователя
        //Нажимаем кнопку 'Search Transactions'
        //Проверяем список транзакций
        transferPage
                .open()
                .checkTransferPageOpened()
                .openTransferAgainTab()
                .checkTransferAgainPageOpened()
                .checkTransactionsListSize(expectedTransactions)
                .inputValueInSearchField(userInfo.getUsername())
                .clickSearchTransactionsButton()
                .checkTransactionsListSize(expectedTransactions);


        //Проверяем, что одна транзакция содержит тип DEPOSIT, сумму и username пользователя
        //Проверяем, что другая транзакция содержит тип TRANSFER_OUT, сумму перевода и username пользователя
        final List<String> transactionsTextTransfer = transferPage.getTransactionsText();
        assertThat(transferPage.checkTransaction(transactionsTextTransfer, randomMoneyDeposit, Operations.DEPOSIT, userInfo.getUsername())).isTrue();
        assertThat(transferPage.checkTransaction(transactionsTextTransfer, randomMoneyTransfer, Operations.TRANSFER_OUT, userInfo.getUsername())).isTrue();

        //Нажимаем кнопку Repeat для транзакции Transfer и проверяем, что открылось модальное окно повтора транзакции
        transferPage
                .clickRepeatTransaction(Operations.TRANSFER_OUT, randomMoneyTransfer)
                .checkTransferModalTitleRepeatVisible();

        //Проверяем текст с подтверждением транзакции и номером аккаунтом на который ранее производилось пополнение
        final String actualTransactionMessage = transferPage.getTransactionInfoInRepeatModal().text();
        final String expectedTransactionMessage = TRANSACTION_MESSAGE_REPEAT_MODAL.getValue() + accountSecondUser;
        assertThat(actualTransactionMessage).isEqualTo(expectedTransactionMessage);

        //Проверяем значение по-умолчанию в выпадающем списке аккаунтов
        //Проверяем количество аккаунтов в выпадающем списке
        //Выбираем аккаунт пользователя
        transferPage
                .checkDefaultValueInAccountListRepeatModal()
                .checkAccountSizeInRepeatModal(expectedListSize)
                .selectAccountInRepeatModal(userAccount);

        //Проверяем отображаемый аккаунт в списке
        final String actualAccountInfoInList = transferPage.getAccountSelectorInRepeatModal().getSelectedOptionText();
        final String expectedAccountInfoInList = getAccountInfoList(authUserToken, userAccount);
        assertThat(actualAccountInfoInList).isEqualTo(expectedAccountInfoInList);

        //Проверяем, что поле Amount содержит значение указанное в транзакции
        //Активируем чекбокс подтверждая данные
        transferPage
                .checkAmountValueFieldRepeatModal(randomMoneyTransfer)
                .clickConfirmCheckboxToChecked()
                .clickTransferButton();

        // Проверяем сообщение в модальном окне об успешности выполнения Deposit и закрываем его
        final String expectedAlertText = transferPage.expectedSuccessfulTransferModalMessage(randomMoneyDeposit, accountSecondUser);
        transferPage.checkMessageFromModalPageAndAccept(expectedAlertText);


        //Проверяем, что баланс аккаунта второго пользователя пополнился после перевода
        final double expectedSecondUserBalance = randomMoneyTransfer + randomMoneyTransfer;
        final double actualSecondUserBalance = UserSteps.getUserBalance(secondUserToken, accountSecondUser);
        assertThat(actualSecondUserBalance).isEqualTo(expectedSecondUserBalance);
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
        //Переходим на вкладку Transfer Again и проверяем, что отображается строка поиска
        //Проверяем список транзакций
        //В поле 'Search by Username or Name' вводим username пользователя
        //Нажимаем кнопку 'Search Transactions'
        //Проверяем список транзакций
        transferPage
                .open()
                .checkTransferPageOpened()
                .openTransferAgainTab()
                .checkTransferAgainPageOpened()
                .checkTransactionsListSize(expectedTransactions)
                .inputValueInSearchField(userInfo.getUsername())
                .clickSearchTransactionsButton()
                .checkTransactionsListSize(expectedTransactions);

        //Проверяем, что одна транзакция содержит тип DEPOSIT, сумму и username пользователя
        //Проверяем, что другая транзакция содержит тип TRANSFER_OUT, сумму перевода и username пользователя
        final List<String> transactionsTextTransfer = transferPage.getTransactionsText();
        assertThat(transferPage.checkTransaction(transactionsTextTransfer, randomMoneyDeposit, Operations.DEPOSIT, userInfo.getUsername())).isTrue();
        assertThat(transferPage.checkTransaction(transactionsTextTransfer, randomMoneyTransfer, Operations.TRANSFER_OUT, userInfo.getUsername())).isTrue();

        //Нажимаем кнопку Repeat для транзакции Transfer и проверяем, что открылось модальное окно повтора транзакции
        transferPage.clickRepeatTransaction(Operations.TRANSFER_OUT, randomMoneyTransfer);
        transferPage.checkTransferModalTitleRepeatVisible();

        //Проверяем текст с подтверждением транзакции и номером аккаунтом на который ранее производилось пополнение
        final String actualTransactionMessage = transferPage.getTransactionInfoInRepeatModal().text();
        final String expectedTransactionMessage = TRANSACTION_MESSAGE_REPEAT_MODAL.getValue() + accountSecondUser;
        assertThat(actualTransactionMessage).isEqualTo(expectedTransactionMessage);

        //Проверяем значение по-умолчанию в выпадающем списке аккаунтов
        //Проверяем количество аккаунтов в выпадающем списке
        //Выбираем аккаунт пользователя
        transferPage
                .checkDefaultValueInAccountListRepeatModal()
                .checkAccountSizeInRepeatModal(expectedListSize)
                .selectAccountInRepeatModal(userAccount);

        //Проверяем отображаемый аккаунт в списке
        final String actualAccountInfoInList = transferPage.getAccountSelectorInRepeatModal().getSelectedOptionText();
        final String expectedAccountInfoInList = getAccountInfoList(authUserToken, userAccount);
        assertThat(actualAccountInfoInList).isEqualTo(expectedAccountInfoInList);

        //Проверяем, что поле Amount содержит значение указанное в транзакции
        //Указываем новое значение в поле Amount
        //Активируем чекбокс подтверждая данные
        // Проверяем сообщение в модальном окне и закрываем его
        transferPage
                .checkAmountValueFieldRepeatModal(randomMoneyTransfer)
                .inputAmountValueRepeatModal(zeroMoney)
                .clickConfirmCheckboxToChecked()
                .clickTransferButton()
                .checkMessageFromModalPageAndAccept(TRANSFER_ERROR_WITH_ZERO_AMOUNT.getValue());

        //Проверяем, что баланс аккаунта первого пользователя не изменился
        final double expectedUserBalanceRaw = randomMoneyDeposit - randomMoneyTransfer;
        final double expectedUserBalance = new BigDecimal(expectedUserBalanceRaw).setScale(2, RoundingMode.HALF_UP).doubleValue();
        final double actualUserBalance = UserSteps.getUserBalance(authUserToken, userAccount);
        assertThat(actualUserBalance).isEqualTo(expectedUserBalance);
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
        //Переходим на вкладку Transfer Again и проверяем, что отображается строка поиска
        //Проверяем список транзакций
        //В поле 'Search by Username or Name' вводим username пользователя
        //Нажимаем кнопку 'Search Transactions'
        //Проверяем список транзакций
        transferPage
                .open()
                .checkTransferPageOpened()
                .openTransferAgainTab()
                .checkTransferAgainPageOpened()
                .checkTransactionsListSize(expectedTransactions)
                .inputValueInSearchField(userInfo.getUsername())
                .clickSearchTransactionsButton()
                .checkTransactionsListSize(expectedTransactions);

        //Проверяем, что одна транзакция содержит тип DEPOSIT, сумму и username пользователя
        final List<String> transactionsTextTransfer = transferPage.getTransactionsText();
        assertThat(transferPage.checkTransaction(transactionsTextTransfer, randomMoneyDeposit, Operations.DEPOSIT, userInfo.getUsername())).isTrue();

        //Нажимаем кнопку Repeat для транзакции Deposit и проверяем, что открылось модальное окно повтора транзакции
        transferPage
                .clickRepeatTransaction(Operations.DEPOSIT, randomMoneyDeposit)
                .checkTransferModalTitleRepeatVisible();

        //Проверяем текст с подтверждением транзакции и номером аккаунтом на который ранее производилось пополнение
        final String actualTransactionMessage = transferPage.getTransactionInfoInRepeatModal().text();
        final String expectedTransactionMessage = TRANSACTION_MESSAGE_REPEAT_MODAL.getValue() + userAccount;
        assertThat(actualTransactionMessage).isEqualTo(expectedTransactionMessage);

        //Проверяем значение по-умолчанию в выпадающем списке аккаунтов
        //Проверяем количество аккаунтов в выпадающем списке
        //Не выбираем никакой аккаунт пользователя и оставляем его пустым
        //Проверяем, что поле Amount содержит значение указанное в транзакции
        //Активируем чекбокс подтверждая данные
        transferPage
                .checkDefaultValueInAccountListRepeatModal()
                .checkAccountSizeInRepeatModal(expectedListSize)
                .checkAmountValueFieldRepeatModal(randomMoneyDeposit)
                .clickConfirmCheckboxToChecked();


        //Проверяем, что кнопка Send Transfer недоступна для нажатия
        //Выбираем аккаунт из выпадающего списка
        //Проверяем отображаемый аккаунт в списке
        //Удаляем значение в поле Amount
        //Проверяем, что чекбокс активен
        //Проверяем, что кнопка Send Transfer недоступна для нажатия
        //Здесь баг. Ожидаем, что кнопка не должна быть доступна
        //Указываем значение в поле Amount
        //Снимаем чекбокс
        //Проверяем, что кнопка Send Transfer недоступна для нажатия
        transferPage
                .checkTransferButtonNotClickable()
                .selectAccountInRepeatModal(userAccount)
                .checkSelectedAccountInListRepeatModal(authUserToken, userAccount)
                .clearValueAmountRepeatModal()
                .checkConfirmCheckboxChecked()
                .checkTransferButtonNotClickable()
                .inputAmountValueRepeatModal(randomMoneyDeposit)
                .clickConfirmCheckboxToUnchecked()
                .checkTransferButtonNotClickable();
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
        //Переходим на вкладку Transfer Again и проверяем, что отображается строка поиска
        //Проверяем список транзакций
        //В поле 'Search by Username or Name' вводим username пользователя
        //Нажимаем кнопку 'Search Transactions'
        //Проверяем список транзакций
        transferPage
                .open()
                .checkTransferPageOpened()
                .openTransferAgainTab()
                .checkTransferAgainPageOpened()
                .checkTransactionsListSize(expectedTransactions)
                .inputValueInSearchField(userInfo.getUsername())
                .clickSearchTransactionsButton()
                .checkTransactionsListSize(expectedTransactions);

        //Проверяем, что одна транзакция содержит тип DEPOSIT, сумму и username пользователя
        final List<String> transactionsTextTransfer = transferPage.getTransactionsText();
        assertThat(transferPage.checkTransaction(transactionsTextTransfer, randomMoneyDeposit, Operations.DEPOSIT, userInfo.getUsername())).isTrue();

        //Нажимаем кнопку Repeat для транзакции Deposit и проверяем, что открылось модальное окно повтора транзакции
        transferPage.clickRepeatTransaction(Operations.DEPOSIT, randomMoneyDeposit);
        transferPage.checkTransferModalTitleRepeatVisible();

        //Проверяем текст с подтверждением транзакции и номером аккаунтом на который ранее производилось пополнение
        final String actualTransactionMessage = transferPage.getTransactionInfoInRepeatModal().text();
        final String expectedTransactionMessage = TRANSACTION_MESSAGE_REPEAT_MODAL.getValue() + userAccount;
        assertThat(actualTransactionMessage).isEqualTo(expectedTransactionMessage);

        //Проверяем значение по-умолчанию в выпадающем списке аккаунтов
        //Проверяем количество аккаунтов в выпадающем списке
        //Выбираем аккаунт из выпадающего списка
        //Проверяем отображаемый аккаунт в списке
        //Нажимаем кнопку Cancel для закрытия Repeat окна
        //Проверяем, что окно закрылось
        //Нажимаем кнопку Repeat для транзакции Deposit и проверяем, что открылось модальное окно повтора транзакции
        //Проверяем отображаемый аккаунт в списке, что он не сброшен после нажатия Cancel
        //Нажимаем на иконку крестика для закрытия Repeat окна
        //Проверяем, что окно закрылось
        transferPage
                .checkDefaultValueInAccountListRepeatModal()
                .checkAccountSizeInRepeatModal(expectedListSize)
                .selectAccountInRepeatModal(userAccount)
                .checkSelectedAccountInListRepeatModal(authUserToken, userAccount)
                .clickCancelButton()
                .checkTransferModalTitleRepeatNotVisible()
                .clickRepeatTransaction(Operations.DEPOSIT, randomMoneyDeposit)
                .checkTransferModalTitleRepeatVisible()
                .checkSelectedAccountInListRepeatModal(authUserToken, userAccount)
                .clickCloseButton()
                .checkTransferModalTitleRepeatNotVisible();

    }
}
