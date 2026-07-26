package ui_tests;

import api.dao.jdbc.AccountsDao;
import api.dao.jdbc.TransactionsDao;
import api.models.UserAccountResponse;
import api.models.UserTransactionsResponse;
import api.requests.steps.db_steps.DBSteps;
import api.requests.steps.user_steps.UserSteps;
import api.utils.RandomData;
import common.SessionStorage;
import common.annotations.AdminSession;
import common.annotations.ApiVersion;
import common.annotations.UserSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ui.pages.MainPage;

import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ui.pages.AlertMessages.*;

public class DepositTests extends UIBaseTestSenior {

    private double expectedRandomMoney = RandomData.getMoney();

    @AdminSession()
    @UserSession()
    @Test
    @ApiVersion(version = "with_deletion")
    @DisplayName("Позитивный тест: пользователь пополняет свой аккаунт валидной суммой")
    public void userCanDepositHisAccountOld() {

        int expectedListSize = 2;

        final String authUserToken = SessionStorage.getUserTokenFromStorage();
        final UserAccountResponse userAccountInfo = SessionStorage.getUserAccountByUserNumber();
        final int userAccount = userAccountInfo.getId();

        depositPage.open()
                .checkDepositPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(userAccount)
                .checkSelectedAccountInListOld(authUserToken, userAccount)
                .inputAmountValue(expectedRandomMoney)
                .clickDepositButton();

        final String expectedAlertText = depositPage.expectedSuccessfullyDepositModalMessageOld(expectedRandomMoney, userAccount);
        depositPage.checkMessageFromModalPageAndAccept(expectedAlertText)
                .getPage(MainPage.class)
                .checkMainPageOpened();

        final double actualUserBalance = UserSteps.getUserBalance(authUserToken, userAccount);
        assertThat(actualUserBalance).isEqualTo(expectedRandomMoney);

    }

    @AdminSession()
    @UserSession()
    @Test
    @DisplayName("Позитивный тест: пользователь пополняет свой аккаунт валидной суммой")
    public void userCanDepositHisAccount() throws SQLException {

        int expectedListSize = 2;

        final String authUserToken = SessionStorage.getUserTokenFromStorage();
        final UserAccountResponse userAccountInfo = SessionStorage.getUserAccountByUserNumber();
        final int userAccount = userAccountInfo.getId();

        depositPage.open()
                .checkDepositPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(userAccountInfo.getAccountNumber())
                .checkSelectedAccountInList(authUserToken, userAccountInfo.getAccountNumber())
                .inputAmountValue(expectedRandomMoney)
                .clickDepositButton();

        final String expectedAlertText = depositPage.expectedSuccessfullyDepositModalMessage(expectedRandomMoney, userAccount);
        depositPage.checkMessageFromModalPageAndAccept(expectedAlertText)
                .getPage(MainPage.class)
                .checkMainPageOpened();

        final double actualUserBalance = UserSteps.getUserBalance(authUserToken, userAccount);
        assertThat(actualUserBalance).isEqualTo(expectedRandomMoney);

        final AccountsDao accountByAccountIdJDBC = DBSteps.getAccountByAccountIdJDBC(userAccount);

        assertThat(accountByAccountIdJDBC.getBalance()).isEqualTo(expectedRandomMoney);

    }

    @AdminSession
    @UserSession(amountAccounts = 2)
    @Test
    @ApiVersion(version = "with_deletion")
    @DisplayName("Позитивный тест: пользователь может положить деньги на свои любые аккаунты")
    public void userCanDepositMoneyIntoHisDiffAccountsOld() throws SQLException {

        int expectedListSize = 3;

        final String authUserToken = SessionStorage.getUserTokenFromStorage();
        final UserAccountResponse userAccountInfo = SessionStorage.getUserAccountByUserNumber();
        final int userFirstAccount = userAccountInfo.getId();
        final UserAccountResponse userSecondAccountInfo = SessionStorage.getUserAccountByUserNumber(1, 2);
        final int userSecondAccount = userSecondAccountInfo.getId();


        depositPage.open()
                .checkDepositPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(userFirstAccount)
                .checkSelectedAccountInList(authUserToken, userAccountInfo.getAccountNumber()).
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
                .checkSelectedAccountInList(authUserToken, userSecondAccountInfo.getAccountNumber())
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
    @UserSession(amountAccounts = 2)
    @Test
    @DisplayName("Позитивный тест: пользователь может положить деньги на свои любые аккаунты")
    public void userCanDepositMoneyIntoHisDiffAccounts() throws SQLException {

        int expectedListSize = 3;

        final String authUserToken = SessionStorage.getUserTokenFromStorage();
        final List<UserAccountResponse> userAccounts = SessionStorage.getUserAccountsByToken(authUserToken);
        final UserAccountResponse userFirstAccountInfo = userAccounts.get(0);
        final int userFirstAccount = userFirstAccountInfo.getId();
        final UserAccountResponse userSecondAccountInfo = userAccounts.get(1);
        final int userSecondAccount = userSecondAccountInfo.getId();

        depositPage.open()
                .checkDepositPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(userFirstAccount)
                .checkSelectedAccountInList(authUserToken, userFirstAccountInfo.getAccountNumber()).
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
                .checkSelectedAccountInList(authUserToken, userSecondAccountInfo.getAccountNumber())
                .inputAmountValue(expectedRandomMoney).clickDepositButton();

        final String expectedSecondAlertText = depositPage
                .expectedSuccessfullyDepositModalMessage(expectedRandomMoney, userSecondAccount);

        depositPage.checkMessageFromModalPageAndAccept(expectedSecondAlertText)
                .getPage(MainPage.class)
                .checkMainPageOpened();

        final double actualFirstAccountBalance = UserSteps.getUserBalance(authUserToken, userFirstAccount);
        assertThat(actualFirstAccountBalance).isEqualTo(expectedRandomMoney);

        final AccountsDao userFirstAccountIdJDBC = DBSteps.getAccountByAccountIdJDBC(userFirstAccount);
        assertThat(userFirstAccountIdJDBC.getBalance()).isEqualTo(expectedRandomMoney);

        final double actualSecondAccountBalance = UserSteps.getUserBalance(authUserToken, userSecondAccount);
        assertThat(actualSecondAccountBalance).isEqualTo(expectedRandomMoney);

        final AccountsDao userSecondAccountIdJDBC = DBSteps.getAccountByAccountIdJDBC(userSecondAccount);
        assertThat(userSecondAccountIdJDBC.getBalance()).isEqualTo(expectedRandomMoney);

    }

    @AdminSession
    @UserSession
    @Test
    @ApiVersion(version = "with_deletion")
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке пополнить свой аккаунт суммой меньше 0.01")
    public void userSeesErrorMessageWhenDepositHisAccountWithLessThanMiniumLimitValueOld() {

        double negativeMoneyValue = -0.01;
        double zeroBalance = 0.00;
        int expectedListSize = 2;

        final String authUserToken = SessionStorage.getUserTokenFromStorage();
        final UserAccountResponse userAccountInfo = SessionStorage.getUserAccountByUserNumber();
        final int userAccount = userAccountInfo.getId();

        depositPage.open()
                .checkDepositPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(userAccount)
                .checkSelectedAccountInListOld(authUserToken, userAccount)
                .inputAmountValue(negativeMoneyValue).clickDepositButton()
                .checkMessageFromModalPageAndAccept(DEPOSIT_ERROR_NEGATIVE_VALUE.getValue())
                .checkDepositPageOpened();

        final double actualUserBalance = UserSteps.getUserBalance(authUserToken, userAccount);
        assertThat(zeroBalance).isEqualTo(actualUserBalance);

    }

    @AdminSession
    @UserSession
    @Test
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке пополнить свой аккаунт суммой меньше 0.01")
    public void userSeesErrorMessageWhenDepositHisAccountWithLessThanMiniumLimitValue() throws SQLException {

        double negativeMoneyValue = -0.01;
        double zeroBalance = 0.00;
        int expectedListSize = 2;

        final String authUserToken = SessionStorage.getUserTokenFromStorage();
        final UserAccountResponse userAccountInfo = SessionStorage.getUserAccountByUserNumber();
        final int userAccount = userAccountInfo.getId();

        depositPage.open()
                .checkDepositPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(userAccount)
                .checkSelectedAccountInList(authUserToken, userAccountInfo.getAccountNumber())
                .inputAmountValue(negativeMoneyValue).clickDepositButton()
                .checkMessageFromModalPageAndAccept(DEPOSIT_ERROR_NEGATIVE_VALUE.getValue())
                .checkDepositPageOpened();

        final double actualUserBalance = UserSteps.getUserBalance(authUserToken, userAccount);
        assertThat(actualUserBalance).isEqualTo(zeroBalance);

        final AccountsDao userAccountIdJDBC = DBSteps.getAccountByAccountIdJDBC(userAccount);
        assertThat(userAccountIdJDBC.getBalance()).isEqualTo(zeroBalance);

    }

    @AdminSession
    @UserSession
    @Test
    @ApiVersion(version = "with_deletion")
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке пополнить свой аккаунт суммой больше 5000")
    public void userSeesErrorMessageWhenDepositHisAccountWithValueMoreThanMaximum5000Old() {

        double maximumValue = 5000.01;
        double zeroBalance = 0.00;
        int expectedListSize = 2;

        final String authUserToken = SessionStorage.getUserTokenFromStorage();
        final UserAccountResponse userAccountInfo = SessionStorage.getUserAccountByUserNumber();
        final int userAccount = userAccountInfo.getId();

        depositPage.open()
                .checkDepositPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(userAccount)
                .checkSelectedAccountInListOld(authUserToken, userAccount)
                .inputAmountValue(maximumValue).clickDepositButton()
                .checkMessageFromModalPageAndAccept(DEPOSIT_ERROR_EXCEEDED_MAXIMUM_VALUE.getValue())
                .checkDepositPageOpened();

        final double actualUserBalance = UserSteps.getUserBalance(authUserToken, userAccount);
        assertThat(actualUserBalance).isEqualTo(zeroBalance);
    }

    @AdminSession
    @UserSession
    @Test
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке пополнить свой аккаунт суммой больше 5000")
    public void userSeesErrorMessageWhenDepositHisAccountWithValueMoreThanMaximum5000() throws SQLException {

        double maximumValue = 5000.01;
        double zeroBalance = 0.00;
        int expectedListSize = 2;

        final String authUserToken = SessionStorage.getUserTokenFromStorage();
        final UserAccountResponse userAccountInfo = SessionStorage.getUserAccountByUserNumber();
        final int userAccount = userAccountInfo.getId();

        depositPage.open()
                .checkDepositPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(userAccount)
                .checkSelectedAccountInList(authUserToken, userAccountInfo.getAccountNumber())
                .inputAmountValue(maximumValue).clickDepositButton()
                .checkMessageFromModalPageAndAccept(DEPOSIT_ERROR_EXCEEDED_MAXIMUM_VALUE.getValue())
                .checkDepositPageOpened();

        final double actualUserBalance = UserSteps.getUserBalance(authUserToken, userAccount);
        assertThat(actualUserBalance).isEqualTo(zeroBalance);

        final AccountsDao userAccountIdJDBC = DBSteps.getAccountByAccountIdJDBC(userAccount);
        assertThat(userAccountIdJDBC.getBalance()).isEqualTo(zeroBalance);
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
    @ApiVersion(version = "with_deletion")
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке нажатия 'Deposit' без указания суммы")
    public void userSeesErrorMessageWhenClickDepositButtonWithoutAmountOld() {

        final String authUserToken = SessionStorage.getUserTokenFromStorage();
        final UserAccountResponse userAccountInfo = SessionStorage.getUserAccountByUserNumber();
        final int userAccount = userAccountInfo.getId();

        depositPage.open()
                .checkDepositPageOpened()
                .checkDefaultValueInAccountList()
                .selectAccount(userAccount)
                .checkSelectedAccountInListOld(authUserToken, userAccount)
                .clickDepositButton()
                .checkMessageFromModalPageAndAccept(DEPOSIT_ERROR_WITHOUT_AMOUNT.getValue())
                .checkDepositPageOpened();

        final List<UserTransactionsResponse> userTransactions = UserSteps.getUserTransactionsOld(authUserToken, userAccount);
        assertThat(userTransactions).isEmpty();

    }

    @AdminSession
    @UserSession
    @Test
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке нажатия 'Deposit' без указания суммы")
    public void userSeesErrorMessageWhenClickDepositButtonWithoutAmount() throws SQLException {

        final String authUserToken = SessionStorage.getUserTokenFromStorage();
        final UserAccountResponse userAccountInfo = SessionStorage.getUserAccountByUserNumber();
        final int userAccount = userAccountInfo.getId();

        depositPage.open()
                .checkDepositPageOpened()
                .checkDefaultValueInAccountList()
                .selectAccount(userAccount)
                .checkSelectedAccountInList(authUserToken, userAccountInfo.getAccountNumber())
                .clickDepositButton()
                .checkMessageFromModalPageAndAccept(DEPOSIT_ERROR_WITHOUT_AMOUNT.getValue())
                .checkDepositPageOpened();

        final List<UserTransactionsResponse> userTransactions = UserSteps.getUserTransactions(authUserToken, userAccount);
        assertThat(userTransactions).isEmpty();

        final List<TransactionsDao> transactionInfoListByAccountIdJDBC = DBSteps.getTransactionInfoListByAccountIdJDBC(userAccount);
        assertThat(transactionInfoListByAccountIdJDBC).isEmpty();

    }

    @AdminSession
    @UserSession
    @Test
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке нажатия 'Deposit' с пустым аккаунтом, " +
            "хотя ранее он был выбран")
    public void userSeesErrorMessageWhenClickDepositButtonWithoutAccountWhenAccountWasChooseBefore() throws SQLException {

        final String authUserToken = SessionStorage.getUserTokenFromStorage();
        final UserAccountResponse userAccountInfo = SessionStorage.getUserAccountByUserNumber();
        final int userAccount = userAccountInfo.getId();

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

        final List<TransactionsDao> transactionInfoListByAccountIdJDBC = DBSteps.getTransactionInfoListByAccountIdJDBC(userAccount);
        assertThat(transactionInfoListByAccountIdJDBC).isEmpty();
    }

}
