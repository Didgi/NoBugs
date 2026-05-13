package ui_tests.middle_ui_tests;

import api.models.UserTransactionsResponse;
import api.requests.steps.user_steps.UserSteps;
import api.utils.RandomData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ui.pages.MainPage;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ui.pages.AlertMessages.*;

public class DepositTests extends BaseTestMiddle {

    private double expectedRandomMoney = RandomData.getMoney();

    @Test
    @DisplayName("Позитивный тест: пользователь пополняет свой аккаунт валидной суммой")
    public void userCanDepositHisAccount() {

        int expectedListSize = 2;

        //Открываем страницу выполнения депозита
        // Проверяем лого страницы Deposit Money
        // Проверяем значение по-умолчанию в выпадающем списке Select Account:
        // Проверяем размер списка
        // Открываем выпадающий список
        // Выбираем созданный аккаунт
        // Проверяем отображение выбранного ID аккаунта и баланса аккаунта пользователя
        // Проверяем отображение placeholder в поле Enter amount
        // Вводим рандомное значение денег для пополнения аккаунта
        // Нажимаем кнопку Deposit для пополнения аккаунта
        depositPage.open()
                .checkDepositPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(userAccount)
                .checkSelectedAccountInList(authUserToken, userAccount)
                .inputAmountValue(expectedRandomMoney)
                .clickDepositButton();

        // Проверяем сообщение в модальном окне об успешности выполнения Deposit
        final String expectedAlertText = depositPage.expectedSuccessfullyDepositModalMessage(expectedRandomMoney, userAccount);
        // Проверяем, что произошёл переход на главную страницу после выполнения Deposit
        depositPage.checkMessageFromModalPageAndAccept(expectedAlertText)
                .getPage(MainPage.class)
                .checkMainPageOpened();

        // Проверяем через api, что операция Deposit выполнена успешно
        final double actualUserBalance = UserSteps.getUserBalance(authUserToken, userAccount);
        assertThat(actualUserBalance).isEqualTo(expectedRandomMoney);

    }

    @Test
    @DisplayName("Позитивный тест: пользователь может положить деньги на свои любые аккаунты")
    public void userCanDepositMoneyIntoHisDiffAccounts() {

        int expectedListSize = 3;

        //Создаём второй аккаунт для пользователя
        final int secondUserAccount = UserSteps.createUserAccount(authUserToken);

        //Открываем страницу выполнения депозита
        // Проверяем лого страницы Deposit Money
        // Проверяем значение по-умолчанию в выпадающем списке Select Account:
        // Проверяем размер списка
        // Открываем выпадающий список
        // Проверяем отображение выбранного ID аккаунта и баланса аккаунта пользователя
        // Проверяем отображение placeholder в поле Enter amount
        // Вводим рандомное значение денег для пополнения аккаунта
        // Нажимаем кнопку Deposit для пополнения аккаунта
        depositPage.open()
                .checkDepositPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(userAccount)
                .checkSelectedAccountInList(authUserToken, userAccount).
                inputAmountValue(expectedRandomMoney).clickDepositButton();

        // Проверяем сообщение в модальном окне об успешности выполнения Deposit
        final String expectedAlertText = depositPage.expectedSuccessfullyDepositModalMessage(expectedRandomMoney, userAccount);
        // Проверяем, что произошёл переход на главную страницу после выполнения Deposit
        depositPage.checkMessageFromModalPageAndAccept(expectedAlertText)
                .getPage(MainPage.class)
                .checkMainPageOpened();

        // Проверяем лого страницы Deposit Money
        // Открываем выпадающий список
        // Выбираем второй аккаунт
        // Проверяем отображение выбранного ID аккаунта и баланса аккаунта пользователя
        // Вводим рандомное значение денег для пополнения аккаунта
        // Нажимаем кнопку Deposit для пополнения аккаунта
        depositPage
                .goToDepositPage()
                .checkDepositPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(secondUserAccount)
                .checkSelectedAccountInList(authUserToken, secondUserAccount)
                .inputAmountValue(expectedRandomMoney).clickDepositButton();

        final String expectedSecondAlertText = depositPage
                .expectedSuccessfullyDepositModalMessage(expectedRandomMoney, secondUserAccount);

        // Проверяем сообщение в модальном окне об успешности выполнения Deposit
        // Проверяем, что произошёл переход на главную страницу после выполнения Deposit
        depositPage.checkMessageFromModalPageAndAccept(expectedSecondAlertText)
                .getPage(MainPage.class)
                .checkMainPageOpened();

        // Проверяем через api, что операция Deposit для первого аккаунта выполнена успешно
        final double actualFirstAccountBalance = UserSteps.getUserBalance(authUserToken, userAccount);
        assertThat(actualFirstAccountBalance).isEqualTo(expectedRandomMoney);

        // Проверяем через api, что операция Deposit для второго аккаунта выполнена успешно
        final double actualSecondAccountBalance = UserSteps.getUserBalance(authUserToken, secondUserAccount);
        assertThat(actualSecondAccountBalance).isEqualTo(expectedRandomMoney);

    }

    @Test
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке пополнить свой аккаунт суммой меньше 0.01")
    public void userSeesErrorMessageWhenDepositHisAccountWithLessThanMiniumLimitValue() {

        double negativeMoneyValue = -0.01;
        double zeroBalance = 0.00;
        int expectedListSize = 2;

        //Открываем страницу выполнения депозита
        // Проверяем лого страницы Deposit Money
        // Проверяем значение по-умолчанию в выпадающем списке Select Account:
        // Проверяем размер списка
        // Открываем выпадающий список
        // Выбираем первый аккаунт
        // Проверяем отображение выбранного ID аккаунта и баланса аккаунта пользователя
        // Проверяем отображение placeholder в поле Enter amount
        // Вводим отрицательное -0.01 значение денег для пополнения аккаунта
        // Проверяем сообщение в модальном окне об ошибке выполнения Deposit и закрываем его
        // Проверяем, что остались на той же странице
        depositPage.open()
                .checkDepositPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(userAccount)
                .checkSelectedAccountInList(authUserToken, userAccount)
                .inputAmountValue(negativeMoneyValue).clickDepositButton()
                .checkMessageFromModalPageAndAccept(DEPOSIT_ERROR_NEGATIVE_VALUE.getValue())
                .checkDepositPageOpened();

        // Проверяем через api, что операция Deposit не выполнена
        final double actualUserBalance = UserSteps.getUserBalance(authUserToken, userAccount);
        assertThat(zeroBalance).isEqualTo(actualUserBalance);

    }

    @Test
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке пополнить свой аккаунт суммой больше 5000")
    public void userSeesErrorMessageWhenDepositHisAccountWithValueMoreThanMaximum5000() {

        double maximumValue = 5000.01;
        double zeroBalance = 0.00;
        int expectedListSize = 2;

        //Открываем страницу выполнения депозита
        // Проверяем лого страницы Deposit Money
        // Проверяем значение по-умолчанию в выпадающем списке Select Account:
        // Проверяем размер списка
        // Открываем выпадающий список
        // Выбираем первый аккаунт
        // Проверяем отображение выбранного ID аккаунта и баланса аккаунта пользователя
        // Проверяем отображение placeholder в поле Enter amount
        // Вводим значение больше максимального допустимого значение денег для пополнения аккаунта
        // Проверяем сообщение в модальном окне об ошибке выполнения Deposit
        // Проверяем, что остались на той же странице
        depositPage.open()
                .checkDepositPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(userAccount)
                .checkSelectedAccountInList(authUserToken, userAccount)
                .inputAmountValue(maximumValue).clickDepositButton()
                .checkMessageFromModalPageAndAccept(DEPOSIT_ERROR_EXCEEDED_MAXIMUM_VALUE.getValue())
                .checkDepositPageOpened();

        // Проверяем через api, что операция Deposit не выполнена
        final double actualUserBalance = UserSteps.getUserBalance(authUserToken, userAccount);
        assertThat(zeroBalance).isEqualTo(actualUserBalance);
    }

    @Test
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке нажатия 'Deposit' без выбора аккаунта")
    public void userSeesErrorMessageWhenClickDepositButtonWithoutAccount() {

        //Открываем страницу выполнения депозита
        // Проверяем лого страницы Deposit Money
        // Не выбираем никакой аккаунт и не вводим сумму для пополнения.
        // Нажимаем кнопку Deposit для пополнения аккаунта
        // Проверяем сообщение в модальном окне об ошибке выполнения Deposit
        // Проверяем, что остались на той же странице
        depositPage
                .open()
                .checkDepositPageOpened()
                .clickDepositButton()
                .checkMessageFromModalPageAndAccept(DEPOSIT_ERROR_WITHOUT_REQUIRED_FIELDS.getValue())
                .checkDepositPageOpened();
    }

    @Test
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке нажатия 'Deposit' без указания суммы")
    public void userSeesErrorMessageWhenClickDepositButtonWithoutAmount() {

        //Открываем страницу выполнения депозита
        // Проверяем лого страницы Deposit Money
        // Проверяем значение по-умолчанию в выпадающем списке Select Account:
        // Проверяем размер списка
        // Открываем выпадающий список
        // Выбираем первый аккаунт
        // Не вводим сумму для пополнения
        // Нажимаем кнопку Deposit для пополнения аккаунта
        // Проверяем сообщение в модальном окне об ошибке выполнения Deposit
        // Проверяем, что остались на той же странице
        depositPage.open()
                .checkDepositPageOpened()
                .checkDefaultValueInAccountList()
                .selectAccount(userAccount)
                .checkSelectedAccountInList(authUserToken, userAccount)
                .clickDepositButton()
                .checkMessageFromModalPageAndAccept(DEPOSIT_ERROR_WITHOUT_AMOUNT.getValue())
                .checkDepositPageOpened();

        // Проверяем через api, что операция Deposit не выполнена
        final List<UserTransactionsResponse> userTransactions = UserSteps.getUserTransactions(authUserToken, userAccount);
        assertThat(userTransactions).isEmpty();

    }

    @Test
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке нажатия 'Deposit' с пустым аккаунтом, " +
            "хотя ранее он был выбран")
    public void userSeesErrorMessageWhenClickDepositButtonWithoutAccountWhenAccountWasChooseBefore() {

        //Открываем страницу выполнения депозита
        // Проверяем лого страницы Deposit Money
        // Проверяем значение по-умолчанию в выпадающем списке Select Account:
        // Проверяем размер списка
        // Открываем выпадающий список
        // Выбираем первый аккаунт
        // Не вводим сумму для пополнения
        // Нажимаем кнопку Deposit для пополнения аккаунта
        // Проверяем сообщение в модальном окне об ошибке выполнения Deposit
        // Проверяем, что остались на той же странице
        depositPage.open()
                .checkDepositPageOpened()
                .checkDefaultValueInAccountList()
                .selectAccount(userAccount)
                .selectDefaultValueInAccountList()
                .checkDefaultValueInAccountList()
                .inputAmountValue(expectedRandomMoney)
                .clickDepositButton()
                .checkMessageFromModalPageAndAccept(DEPOSIT_ERROR_WITHOUT_REQUIRED_FIELDS.getValue())
                .checkDepositPageOpened();

        // Проверяем через api, что операция Deposit не выполнена
        final List<UserTransactionsResponse> userTransactions = UserSteps.getUserTransactions(authUserToken, userAccount);
        assertThat(userTransactions).isEmpty();
    }
}
