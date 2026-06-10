package ui_tests.senior_ui_tests;

import api.models.UserTransactionsResponse;
import api.requests.steps.user_steps.UserSteps;
import api.utils.RandomData;
import common.SessionStorage;
import common.annotations.AdminSession;
import common.annotations.UserSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ui.pages.MainPage;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ui.pages.AlertMessages.*;

public class DepositTests extends UIBaseTestSenior {

    private double expectedRandomMoney = RandomData.getMoney();

    @AdminSession()
    @UserSession()
    @Test
    @DisplayName("Позитивный тест: пользователь пополняет свой аккаунт валидной суммой")
    public void userCanDepositHisAccount() {

        int expectedListSize = 2;

        final String authUserToken = SessionStorage.getUserTokenFromStorage();
        final int userAccount = SessionStorage.getUserAccountByUserNumber();

        depositPage.open()
                .checkDepositPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(userAccount)
                .checkSelectedAccountInList(authUserToken, userAccount)
                .inputAmountValue(expectedRandomMoney)
                .clickDepositButton();

        final String expectedAlertText = depositPage.expectedSuccessfullyDepositModalMessage(expectedRandomMoney, userAccount);
        depositPage.checkMessageFromModalPageAndAccept(expectedAlertText)
                .getPage(MainPage.class)
                .checkMainPageOpened();

        final double actualUserBalance = UserSteps.getUserBalance(authUserToken, userAccount);
        assertThat(actualUserBalance).isEqualTo(expectedRandomMoney);

    }

    @AdminSession
    @UserSession(amountAccounts = 2)
    @Test
    @DisplayName("Позитивный тест: пользователь может положить деньги на свои любые аккаунты")
    public void userCanDepositMoneyIntoHisDiffAccounts() {

        int expectedListSize = 3;

        final String authUserToken = SessionStorage.getUserTokenFromStorage();
        final int userFirstAccount = SessionStorage.getUserAccountByUserNumber();
        final int userSecondAccount = SessionStorage.getUserAccountByUserToken(authUserToken, 2);

        depositPage.open()
                .checkDepositPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(userFirstAccount)
                .checkSelectedAccountInList(authUserToken, userFirstAccount).
                inputAmountValue(expectedRandomMoney).clickDepositButton();

        final String expectedAlertText = depositPage.expectedSuccessfullyDepositModalMessage(expectedRandomMoney, userFirstAccount);
        depositPage.checkMessageFromModalPageAndAccept(expectedAlertText)
                .getPage(MainPage.class)
                .checkMainPageOpened();

        depositPage
                .goToDepositPage()
                .checkDepositPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(userSecondAccount)
                .checkSelectedAccountInList(authUserToken, userSecondAccount)
                .inputAmountValue(expectedRandomMoney).clickDepositButton();

        final String expectedSecondAlertText = depositPage
                .expectedSuccessfullyDepositModalMessage(expectedRandomMoney, userSecondAccount);

        depositPage.checkMessageFromModalPageAndAccept(expectedSecondAlertText)
                .getPage(MainPage.class)
                .checkMainPageOpened();

        final double actualFirstAccountBalance = UserSteps.getUserBalance(authUserToken, userFirstAccount);
        assertThat(actualFirstAccountBalance).isEqualTo(expectedRandomMoney);

        final double actualSecondAccountBalance = UserSteps.getUserBalance(authUserToken, userSecondAccount);
        assertThat(actualSecondAccountBalance).isEqualTo(expectedRandomMoney);

    }

    @AdminSession
    @UserSession
    @Test
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке пополнить свой аккаунт суммой меньше 0.01")
    public void userSeesErrorMessageWhenDepositHisAccountWithLessThanMiniumLimitValue() {

        double negativeMoneyValue = -0.01;
        double zeroBalance = 0.00;
        int expectedListSize = 2;

        final String authUserToken = SessionStorage.getUserTokenFromStorage();
        final int userAccount = SessionStorage.getUserAccountByUserNumber();

        depositPage.open()
                .checkDepositPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(userAccount)
                .checkSelectedAccountInList(authUserToken, userAccount)
                .inputAmountValue(negativeMoneyValue).clickDepositButton()
                .checkMessageFromModalPageAndAccept(DEPOSIT_ERROR_NEGATIVE_VALUE.getValue())
                .checkDepositPageOpened();

        final double actualUserBalance = UserSteps.getUserBalance(authUserToken, userAccount);
        assertThat(zeroBalance).isEqualTo(actualUserBalance);

    }

    @AdminSession
    @UserSession
    @Test
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке пополнить свой аккаунт суммой больше 5000")
    public void userSeesErrorMessageWhenDepositHisAccountWithValueMoreThanMaximum5000() {

        double maximumValue = 5000.01;
        double zeroBalance = 0.00;
        int expectedListSize = 2;

        final String authUserToken = SessionStorage.getUserTokenFromStorage();
        final int userAccount = SessionStorage.getUserAccountByUserNumber();

        depositPage.open()
                .checkDepositPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(userAccount)
                .checkSelectedAccountInList(authUserToken, userAccount)
                .inputAmountValue(maximumValue).clickDepositButton()
                .checkMessageFromModalPageAndAccept(DEPOSIT_ERROR_EXCEEDED_MAXIMUM_VALUE.getValue())
                .checkDepositPageOpened();

        final double actualUserBalance = UserSteps.getUserBalance(authUserToken, userAccount);
        assertThat(zeroBalance).isEqualTo(actualUserBalance);
    }

    @AdminSession
    @Test
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке нажатия 'Deposit' без выбора аккаунта")
    public void userSeesErrorMessageWhenClickDepositButtonWithoutAccount() {

        depositPage
                .open()
                .checkDepositPageOpened()
                .clickDepositButton()
                .checkMessageFromModalPageAndAccept(DEPOSIT_ERROR_WITHOUT_REQUIRED_FIELDS.getValue())
                .checkDepositPageOpened();
    }

    @AdminSession
    @UserSession
    @Test
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке нажатия 'Deposit' без указания суммы")
    public void userSeesErrorMessageWhenClickDepositButtonWithoutAmount() {

        final String authUserToken = SessionStorage.getUserTokenFromStorage();
        final int userAccount = SessionStorage.getUserAccountByUserNumber();

        depositPage.open()
                .checkDepositPageOpened()
                .checkDefaultValueInAccountList()
                .selectAccount(userAccount)
                .checkSelectedAccountInList(authUserToken, userAccount)
                .clickDepositButton()
                .checkMessageFromModalPageAndAccept(DEPOSIT_ERROR_WITHOUT_AMOUNT.getValue())
                .checkDepositPageOpened();

        final List<UserTransactionsResponse> userTransactions = UserSteps.getUserTransactions(authUserToken, userAccount);
        assertThat(userTransactions).isEmpty();

    }

    @AdminSession
    @UserSession
    @Test
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке нажатия 'Deposit' с пустым аккаунтом, " +
            "хотя ранее он был выбран")
    public void userSeesErrorMessageWhenClickDepositButtonWithoutAccountWhenAccountWasChooseBefore() {

        final String authUserToken = SessionStorage.getUserTokenFromStorage();
        final int userAccount = SessionStorage.getUserAccountByUserNumber();

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

        final List<UserTransactionsResponse> userTransactions = UserSteps.getUserTransactions(authUserToken, userAccount);
        assertThat(userTransactions).isEmpty();
    }
}
