package ui_tests;

import api.config.AccountData;
import api.config.Operations;
import api.dao.jdbc.AccountsDao;
import api.models.ChangeUserRequest;
import api.models.CreateUserResponse;
import api.models.UserAccountResponse;
import api.models.UserProfileResponse;
import api.requests.skelethon.EndpointRequests;
import api.requests.skelethon.requesters.ValidatableCrudRequester;
import api.requests.steps.admin_steps.AdminSteps;
import api.requests.steps.db_steps.DBSteps;
import api.requests.steps.user_steps.UserSteps;
import api.specs.RequestSpecs;
import api.specs.ResponseSpecs;
import api.utils.RandomData;
import api.utils.RandomModelGenerator;
import com.codeborne.selenide.Selenide;
import common.SessionStorage;
import common.annotations.AdminSession;
import common.annotations.ApiVersion;
import common.annotations.Bug;
import common.annotations.UserSession;
import common.retry.RetryUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ui.elements.UserTransactionHistory;
import ui.pages.DepositPage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;

import static api.config.ResponseMessages.CANNOT_TRANSFER_MONEY_TO_THE_SAME_ACCOUNT;
import static api.config.ResponseMessages.INVALID_TRANSFER_INSUFFICIENT_FUNDS_OR_INVALID_ACCOUNTS;
import static org.assertj.core.api.Assertions.assertThat;
import static ui.pages.AlertMessages.*;
import static ui.pages.BasePage.getAccountInfoList;
import static ui.pages.BasePage.getAccountInfoListOld;

public class TransferTests extends UIBaseTestSenior {


    @AdminSession(amountUsers = 2)
    @UserSession
    @Test
    @ApiVersion(version = "with_deletion")
    @DisplayName("Позитивный тест: пользователь может переводить деньги на аккаунт другого пользователя")
    public void userCanTransferMoneyToSomeoneElseExistedAccountOld() {

        double expectedRandomMoney = RandomData.getMoney();
        int expectedListSize = 2;

        final String firstUserToken = SessionStorage.getUserTokenFromStorage(1);
        final String secondUserToken = SessionStorage.getUserTokenFromStorage(2);
        final int firstUserAccount = SessionStorage.getUserAccountByUserToken(firstUserToken, 1);
        final int secondUserAccount = SessionStorage.getUserAccountByUserToken(secondUserToken, 1);

        final ChangeUserRequest changeUserRequest = RandomModelGenerator.generate(ChangeUserRequest.class);

        new ValidatableCrudRequester<CreateUserResponse>(RequestSpecs.withToken(secondUserToken),
                EndpointRequests.UPDATE_USER, ResponseSpecs.requestReturnsOk()).PUT(changeUserRequest);

        UserSteps.depositMoney(firstUserToken, firstUserAccount, expectedRandomMoney);

        transferPage
                .open()
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(firstUserAccount)
                .checkSelectedAccountInListOld(firstUserToken, firstUserAccount)
                .inputRecipientName(changeUserRequest.getName())
                .inputRecipientAccount(secondUserAccount)
                .inputAmountValue(expectedRandomMoney)
                .checkConfirmCheckboxUnchecked()
                .clickConfirmCheckboxToChecked()
                .clickTransferButton();


        final String expectedAlertText = transferPage.expectedSuccessfulTransferModalMessageOld(expectedRandomMoney, secondUserAccount);
        transferPage.checkMessageFromModalPageAndAccept(expectedAlertText);

        transferPage
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkRecipientNameDefaultValue().checkRecipientAccountDefaultValue()
                .checkAmountDefaultValue()
                .checkConfirmCheckboxUnchecked();

        final double actualSecondUserBalance = UserSteps.getUserBalance(secondUserToken, secondUserAccount);
        assertThat(actualSecondUserBalance).isEqualTo(expectedRandomMoney);
    }

    @AdminSession(amountUsers = 2)
    @UserSession
    @Test
    @ApiVersion(version = "preprod_version")
    @DisplayName("Позитивный тест: пользователь может переводить деньги на аккаунт другого пользователя")
    public void userCanTransferMoneyToSomeoneElseExistedAccount() throws SQLException {

        double expectedRandomMoney = RandomData.getMoney();
        int expectedListSize = 2;

        final String firstUserToken = SessionStorage.getUserTokenFromStorage(1);
        final String secondUserToken = SessionStorage.getUserTokenFromStorage(2);
        final UserAccountResponse firstUserAccountInfo = SessionStorage.getUserAccountByUserNumber(1, 1);
        final int firstUserAccount = firstUserAccountInfo.getId();
        final UserAccountResponse secondUserAccountInfo = SessionStorage.getUserAccountByUserNumber(2, 1);
        final int secondUserAccount = secondUserAccountInfo.getId();

        final ChangeUserRequest changeUserRequest = RandomModelGenerator.generate(ChangeUserRequest.class);

        new ValidatableCrudRequester<CreateUserResponse>(RequestSpecs.withToken(secondUserToken),
                EndpointRequests.UPDATE_USER, ResponseSpecs.requestReturnsOk()).PUT(changeUserRequest);

        UserSteps.depositMoneyWOCheckResponse(firstUserToken, firstUserAccount, expectedRandomMoney);

        transferPage
                .open()
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(firstUserAccount)
                .checkSelectedAccountInList(firstUserToken, firstUserAccountInfo.getAccountNumber())
                .inputRecipientName(changeUserRequest.getName())
                .inputRecipientAccount(secondUserAccountInfo.getAccountNumber())
                .inputAmountValue(expectedRandomMoney)
                .checkConfirmCheckboxUnchecked()
                .clickConfirmCheckboxToChecked()
                .clickTransferButton();


        final String expectedAlertText = transferPage.expectedSuccessfulTransferModalMessage(expectedRandomMoney, secondUserAccountInfo.getAccountNumber());
        transferPage.checkMessageFromModalPageAndAccept(expectedAlertText);

        transferPage
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkRecipientNameDefaultValue().checkRecipientAccountDefaultValue()
                .checkAmountDefaultValue()
                .checkConfirmCheckboxUnchecked();

        final double actualSecondUserBalance = UserSteps.getUserBalance(secondUserToken, secondUserAccount);
        assertThat(actualSecondUserBalance).isEqualTo(expectedRandomMoney);

        final AccountsDao accountByAccountIdJDBC = DBSteps.getAccountByAccountIdJDBC(secondUserAccount);
        assertThat(accountByAccountIdJDBC.getBalance()).isEqualTo(expectedRandomMoney);
    }


    @AdminSession
    @UserSession(amountAccounts = 2)
    @Test
    @ApiVersion(version = "with_deletion")
    @DisplayName("Позитивный тест: пользователь может переводить деньги между своими же аккаунтами")
    public void userCanTransferMoneyBetweenHisAccountsOld() {


        final String firstUserToken = SessionStorage.getUserTokenFromStorage(1);
        final int userAccount = SessionStorage.getUserAccountByUserToken(firstUserToken, 1);
        final int userAccountSecond = SessionStorage.getUserAccountByUserToken(firstUserToken, 2);

        int expectedListSize = 3;

        final double expectedRandomMoney = RandomData.getMoney();
        UserSteps.depositMoney(firstUserToken, userAccount, expectedRandomMoney);

        transferPage
                .open()
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(userAccount)
                .checkSelectedAccountInListOld(firstUserToken, userAccount)
                .inputRecipientName(RandomData.randomName(3))
                .inputRecipientAccount(userAccountSecond)
                .inputAmountValue(expectedRandomMoney)
                .clickConfirmCheckboxToChecked()
                .clickTransferButton();


        final String expectedAlertText = transferPage.expectedSuccessfulTransferModalMessageOld(expectedRandomMoney, userAccountSecond);
        transferPage.checkMessageFromModalPageAndAccept(expectedAlertText);

        transferPage
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkRecipientNameDefaultValue()
                .checkRecipientAccountDefaultValue()
                .checkAmountDefaultValue()
                .checkConfirmCheckboxUnchecked();

        final double actualFirstUserBalance = UserSteps.getUserBalance(firstUserToken, userAccountSecond);
        assertThat(actualFirstUserBalance).isEqualTo(expectedRandomMoney);
    }

    @AdminSession
    @UserSession(amountAccounts = 2)
    @Test
    @ApiVersion(version = "preprod_version")
    @DisplayName("Позитивный тест: пользователь может переводить деньги между своими же аккаунтами")
    public void userCanTransferMoneyBetweenHisAccounts() throws SQLException {


        final String firstUserToken = SessionStorage.getUserTokenFromStorage(1);
        final UserAccountResponse userFirstAccountInfo = SessionStorage.getUserAccountByUserNumber(1, 1);
        final int userAccount = userFirstAccountInfo.getId();
        final UserAccountResponse userSecondAccountInfo = SessionStorage.getUserAccountByUserNumber(1, 2);
        final int userAccountSecond = userSecondAccountInfo.getId();
        final String userAccountNumberSecond = DBSteps.getAccountByAccountIdJDBC(userAccountSecond).getAccountNumber();

        int expectedListSize = 3;

        final double expectedRandomMoney = RandomData.getMoney();
        UserSteps.depositMoney(firstUserToken, userAccount, expectedRandomMoney);

        transferPage
                .open()
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(userAccount)
                .checkSelectedAccountInList(firstUserToken, userFirstAccountInfo.getAccountNumber())
                .inputRecipientName(RandomData.randomName(3))
                .inputRecipientAccount(userAccountNumberSecond)
                .inputAmountValue(expectedRandomMoney)
                .clickConfirmCheckboxToChecked()
                .clickTransferButton();


        final String expectedAlertText = transferPage.expectedSuccessfulTransferModalMessage(expectedRandomMoney, userSecondAccountInfo.getAccountNumber());
        transferPage.checkMessageFromModalPageAndAccept(expectedAlertText);

        transferPage
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkRecipientNameDefaultValue()
                .checkRecipientAccountDefaultValue()
                .checkAmountDefaultValue()
                .checkConfirmCheckboxUnchecked();

        final double actualFirstUserBalance = UserSteps.getUserBalance(firstUserToken, userAccountSecond);
        assertThat(actualFirstUserBalance).isEqualTo(expectedRandomMoney);

        final AccountsDao accountByAccountIdJDBC = DBSteps.getAccountByAccountIdJDBC(userAccountSecond);
        assertThat(accountByAccountIdJDBC.getBalance()).isEqualTo(expectedRandomMoney);
    }

    @AdminSession
    @UserSession
    @Test
    @ApiVersion(version = "with_deletion")
    @DisplayName("Позитивный тест: проверка возможности перевода денег на тот же аккаунт с которого происходит перевод")
    public void userCanTransferMoneyToSameAccountOld() {

        int expectedListSize = 2;

        final String firstUserToken = SessionStorage.getUserTokenFromStorage(1);
        final int userAccount = SessionStorage.getUserAccountByUserToken(firstUserToken, 1);

        final double expectedRandomMoney = RandomData.getMoney();
        UserSteps.depositMoney(firstUserToken, userAccount, expectedRandomMoney);

        transferPage
                .open()
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(userAccount)
                .checkSelectedAccountInListOld(firstUserToken, userAccount)
                .inputRecipientName(RandomData.randomName(3))
                .inputRecipientAccount(userAccount)
                .inputAmountValue(expectedRandomMoney)
                .clickConfirmCheckboxToChecked()
                .clickTransferButton();

        final String expectedAlertText = transferPage.expectedSuccessfulTransferModalMessageOld(expectedRandomMoney, userAccount);
        transferPage.checkMessageFromModalPageAndAccept(expectedAlertText);

        transferPage
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkRecipientNameDefaultValue()
                .checkRecipientAccountDefaultValue()
                .checkAmountDefaultValue()
                .checkConfirmCheckboxUnchecked();

        final double actualSecondUserBalance = UserSteps.getUserBalance(firstUserToken, userAccount);
        assertThat(actualSecondUserBalance).isEqualTo(expectedRandomMoney);

    }

    @AdminSession
    @UserSession
    @Test
    @ApiVersion(version = "preprod_version")
    @DisplayName("Позитивный тест: проверка возможности перевода денег на тот же аккаунт с которого происходит перевод")
    public void userCanTransferMoneyToSameAccount() throws SQLException {

        int expectedListSize = 2;

        final String firstUserToken = SessionStorage.getUserTokenFromStorage(1);
        final UserAccountResponse userFirstAccountInfo = SessionStorage.getUserAccountByUserNumber(1, 1);
        final int userAccount = userFirstAccountInfo.getId();

        final String randomName = RandomData.randomName(3);

        final double expectedRandomMoney = RandomData.getMoney();
        UserSteps.depositMoney(firstUserToken, userAccount, expectedRandomMoney);

        transferPage
                .open()
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(userAccount)
                .checkSelectedAccountInList(firstUserToken, userFirstAccountInfo.getAccountNumber())
                .inputRecipientName(randomName)
                .inputRecipientAccount(userFirstAccountInfo.getAccountNumber())
                .inputAmountValue(expectedRandomMoney)
                .clickConfirmCheckboxToChecked()
                .clickTransferButton();

        transferPage.checkMessageFromModalPageAndAccept(CANNOT_TRANSFER_MONEY_TO_THE_SAME_ACCOUNT.getValue());
        final String expectedAccountInfoInList = getAccountInfoList(firstUserToken, userFirstAccountInfo.getAccountNumber());

        transferPage
                .checkTransferPageOpened()
                .checkSelectedAccountDoesntChange(expectedAccountInfoInList)
                .checkRecipientNameDoesntChange(randomName)
                .checkRecipientAccountDoesntChange(userFirstAccountInfo.getAccountNumber())
                .checkAmountValueDoesntChange(expectedRandomMoney)
                .checkConfirmCheckboxChecked();

        final double actualUserBalance = UserSteps.getUserBalance(firstUserToken, userAccount);
        assertThat(actualUserBalance).isEqualTo(expectedRandomMoney);

        final AccountsDao accountByAccountIdJDBC = DBSteps.getAccountByAccountIdJDBC(userAccount);
        assertThat(accountByAccountIdJDBC.getBalance()).isEqualTo(expectedRandomMoney);

    }

    @AdminSession(amountUsers = 2)
    @UserSession
    @Test
    @ApiVersion(version = "with_deletion")
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке перевода отрицательной суммы")
    public void userSeesErrorMessageWhenTransferMoneyLessThanMiniumLimitValueOld() {

        double expectedRandomMoney = RandomData.getMoney();
        double negativeMoney = -0.01;
        double expectedZeroBalance = 0.00;
        int expectedListSize = 2;

        final String firstUserToken = SessionStorage.getUserTokenFromStorage(1);
        final String secondUserToken = SessionStorage.getUserTokenFromStorage(2);
        final int firstUserAccount = SessionStorage.getUserAccountByUserToken(firstUserToken, 1);
        final int secondUserAccount = SessionStorage.getUserAccountByUserToken(secondUserToken, 1);

        final ChangeUserRequest changeUserRequest = RandomModelGenerator.generate(ChangeUserRequest.class);

        new ValidatableCrudRequester<CreateUserResponse>(RequestSpecs.withToken(secondUserToken),
                EndpointRequests.UPDATE_USER, ResponseSpecs.requestReturnsOk()).PUT(changeUserRequest);

        UserSteps.depositMoney(firstUserToken, firstUserAccount, expectedRandomMoney);

        transferPage
                .open()
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(firstUserAccount)
                .checkSelectedAccountInListOld(firstUserToken, firstUserAccount)
                .inputRecipientName(changeUserRequest.getName())
                .inputRecipientAccount(secondUserAccount)
                .inputAmountValue(negativeMoney)
                .clickConfirmCheckboxToChecked()
                .clickTransferButton();

        final String expectedAccountInfoInList = getAccountInfoListOld(firstUserToken, firstUserAccount);

        transferPage
                .checkMessageFromModalPageAndAccept(TRANSFER_ERROR_NEGATIVE_VALUE_OLD.getValue())
                .checkTransferPageOpened()
                .checkSelectedAccountDoesntChange(expectedAccountInfoInList)
                .checkRecipientNameDoesntChange(changeUserRequest.getName())
                .checkRecipientAccountDoesntChange(secondUserAccount)
                .checkAmountValueDoesntChange(negativeMoney)
                .checkConfirmCheckboxChecked();


        final double actualUserBalance = UserSteps.getUserBalance(firstUserToken, firstUserAccount);
        assertThat(actualUserBalance).isEqualTo(expectedRandomMoney);

        final double actualSecondUserBalance = UserSteps.getUserBalance(secondUserToken, secondUserAccount);
        assertThat(actualSecondUserBalance).isEqualTo(expectedZeroBalance);
    }

    @AdminSession(amountUsers = 2)
    @UserSession
    @Test
    @ApiVersion(version = "preprod_version")
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке перевода отрицательной суммы")
    public void userSeesErrorMessageWhenTransferMoneyLessThanMiniumLimitValue() throws SQLException {

        double expectedRandomMoney = RandomData.getMoney();
        double negativeMoney = -0.01;
        double expectedZeroBalance = 0.00;
        int expectedListSize = 2;

        final String firstUserToken = SessionStorage.getUserTokenFromStorage(1);
        final String secondUserToken = SessionStorage.getUserTokenFromStorage(2);
        final UserAccountResponse firstUserAccountInfo = SessionStorage.getUserAccountByUserNumber(1, 1);
        final int firstUserAccount = firstUserAccountInfo.getId();
        final UserAccountResponse secondUserAccountInfo = SessionStorage.getUserAccountByUserNumber(2, 1);
        final int secondUserAccount = secondUserAccountInfo.getId();

        final ChangeUserRequest changeUserRequest = RandomModelGenerator.generate(ChangeUserRequest.class);

        new ValidatableCrudRequester<CreateUserResponse>(RequestSpecs.withToken(secondUserToken),
                EndpointRequests.UPDATE_USER, ResponseSpecs.requestReturnsOk()).PUT(changeUserRequest);

        UserSteps.depositMoney(firstUserToken, firstUserAccount, expectedRandomMoney);

        transferPage
                .open()
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(firstUserAccountInfo.getAccountNumber())
                .checkSelectedAccountInList(firstUserToken, firstUserAccountInfo.getAccountNumber())
                .inputRecipientName(changeUserRequest.getName())
                .inputRecipientAccount(secondUserAccountInfo.getAccountNumber())
                .inputAmountValue(negativeMoney)
                .clickConfirmCheckboxToChecked()
                .clickTransferButton();

        final String expectedAccountInfoInList = getAccountInfoList(firstUserToken, firstUserAccountInfo.getAccountNumber());

        transferPage
                .checkMessageFromModalPageAndAccept(TRANSFER_ERROR_NEGATIVE_VALUE.getValue())
                .checkTransferPageOpened()
                .checkSelectedAccountDoesntChange(expectedAccountInfoInList)
                .checkRecipientNameDoesntChange(changeUserRequest.getName())
                .checkRecipientAccountDoesntChange(secondUserAccountInfo.getAccountNumber())
                .checkAmountValueDoesntChange(negativeMoney)
                .checkConfirmCheckboxChecked();


        final double actualUserBalance = UserSteps.getUserBalance(firstUserToken, firstUserAccount);
        assertThat(actualUserBalance).isEqualTo(expectedRandomMoney);

        final AccountsDao firstUserAccountByAccountIdJDBC = DBSteps.getAccountByAccountIdJDBC(firstUserAccount);
        assertThat(firstUserAccountByAccountIdJDBC.getBalance()).isEqualTo(expectedRandomMoney);

        final double actualSecondUserBalance = UserSteps.getUserBalance(secondUserToken, secondUserAccount);
        assertThat(actualSecondUserBalance).isEqualTo(expectedZeroBalance);

        final AccountsDao secondUserAccountByAccountIdJDBC = DBSteps.getAccountByAccountIdJDBC(secondUserAccount);
        assertThat(secondUserAccountByAccountIdJDBC.getBalance()).isEqualTo(expectedZeroBalance);
    }

    @AdminSession(amountUsers = 2)
    @UserSession
    @Test
    @ApiVersion(version = "with_deletion")
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке перевода суммы больше допустимой 10000")
    public void userSeesErrorMessageWhenTransferMoneyMoreThanMaximumLimitValueOld() {

        double expectedRandomMoney = RandomData.getMoney();
        double moreMaximumLimitValueMoney = 10000.01;
        double expectedZeroBalance = 0.00;
        int expectedListSize = 2;

        final String firstUserToken = SessionStorage.getUserTokenFromStorage(1);
        final String secondUserToken = SessionStorage.getUserTokenFromStorage(2);
        final int firstUserAccount = SessionStorage.getUserAccountByUserToken(firstUserToken, 1);
        final int secondUserAccount = SessionStorage.getUserAccountByUserToken(secondUserToken, 1);
        final ChangeUserRequest changeUserRequest = RandomModelGenerator.generate(ChangeUserRequest.class);

        new ValidatableCrudRequester<CreateUserResponse>(RequestSpecs.withToken(secondUserToken),
                EndpointRequests.UPDATE_USER, ResponseSpecs.requestReturnsOk()).PUT(changeUserRequest);

        UserSteps.depositMoney(firstUserToken, firstUserAccount, expectedRandomMoney);

        transferPage
                .open()
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(firstUserAccount)
                .checkSelectedAccountInListOld(firstUserToken, firstUserAccount)
                .inputRecipientName(changeUserRequest.getName())
                .inputRecipientAccount(secondUserAccount)
                .inputAmountValue(moreMaximumLimitValueMoney)
                .clickConfirmCheckboxToChecked()
                .clickTransferButton();

        final String expectedAccountInfoInList = getAccountInfoListOld(firstUserToken, firstUserAccount);

        transferPage
                .checkMessageFromModalPageAndAccept(TRANSFER_ERROR_EXCEEDED_MAXIMUM_VALUE.getValue())
                .checkTransferPageOpened()
                .checkSelectedAccountDoesntChange(expectedAccountInfoInList)
                .checkRecipientNameDoesntChange(changeUserRequest.getName())
                .checkRecipientAccountDoesntChange(secondUserAccount)
                .checkAmountValueDoesntChange(moreMaximumLimitValueMoney)
                .checkConfirmCheckboxChecked();

        final double actualUserBalance = UserSteps.getUserBalance(firstUserToken, firstUserAccount);
        assertThat(actualUserBalance).isEqualTo(expectedRandomMoney);

        final double actualSecondUserBalance = UserSteps.getUserBalance(secondUserToken, secondUserAccount);
        assertThat(actualSecondUserBalance).isEqualTo(expectedZeroBalance);
    }

    @AdminSession(amountUsers = 2)
    @UserSession
    @Test
    @ApiVersion(version = "preprod_version")
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке перевода суммы больше допустимой 10000")
    public void userSeesErrorMessageWhenTransferMoneyMoreThanMaximumLimitValue() throws SQLException {

        double expectedRandomMoney = RandomData.getMoney();
        double moreMaximumLimitValueMoney = 10000.01;
        double expectedZeroBalance = 0.00;
        int expectedListSize = 2;

        final String firstUserToken = SessionStorage.getUserTokenFromStorage(1);
        final String secondUserToken = SessionStorage.getUserTokenFromStorage(2);
        final UserAccountResponse firstUserAccountInfo = SessionStorage.getUserAccountByUserNumber(1, 1);
        final int firstUserAccount = firstUserAccountInfo.getId();
        final UserAccountResponse secondUserAccountInfo = SessionStorage.getUserAccountByUserNumber(2, 1);
        final int secondUserAccount = secondUserAccountInfo.getId();

        final ChangeUserRequest changeUserRequest = RandomModelGenerator.generate(ChangeUserRequest.class);

        new ValidatableCrudRequester<CreateUserResponse>(RequestSpecs.withToken(secondUserToken),
                EndpointRequests.UPDATE_USER, ResponseSpecs.requestReturnsOk()).PUT(changeUserRequest);

        UserSteps.depositMoney(firstUserToken, firstUserAccount, expectedRandomMoney);

        transferPage
                .open()
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(firstUserAccount)
                .checkSelectedAccountInList(firstUserToken, firstUserAccountInfo.getAccountNumber())
                .inputRecipientName(changeUserRequest.getName())
                .inputRecipientAccount(secondUserAccountInfo.getAccountNumber())
                .inputAmountValue(moreMaximumLimitValueMoney)
                .clickConfirmCheckboxToChecked()
                .clickTransferButton();

        final String expectedAccountInfoInList = getAccountInfoList(firstUserToken, firstUserAccountInfo.getAccountNumber());

        transferPage
                .checkMessageFromModalPageAndAccept(TRANSFER_ERROR_EXCEEDED_MAXIMUM_VALUE.getValue())
                .checkTransferPageOpened()
                .checkSelectedAccountDoesntChange(expectedAccountInfoInList)
                .checkRecipientNameDoesntChange(changeUserRequest.getName())
                .checkRecipientAccountDoesntChange(secondUserAccountInfo.getAccountNumber())
                .checkAmountValueDoesntChange(moreMaximumLimitValueMoney)
                .checkConfirmCheckboxChecked();

        final double actualUserBalance = UserSteps.getUserBalance(firstUserToken, firstUserAccount);
        assertThat(actualUserBalance).isEqualTo(expectedRandomMoney);

        final AccountsDao firstUserAccountByAccountIdJDBC = DBSteps.getAccountByAccountIdJDBC(firstUserAccount);
        assertThat(firstUserAccountByAccountIdJDBC.getBalance()).isEqualTo(expectedRandomMoney);

        final double actualSecondUserBalance = UserSteps.getUserBalance(secondUserToken, secondUserAccount);
        assertThat(actualSecondUserBalance).isEqualTo(expectedZeroBalance);

        final AccountsDao secondUserAccountByAccountIdJDBC = DBSteps.getAccountByAccountIdJDBC(secondUserAccount);
        assertThat(secondUserAccountByAccountIdJDBC.getBalance()).isEqualTo(expectedZeroBalance);
    }

    @AdminSession(amountUsers = 2)
    @UserSession
    @Test
    @ApiVersion(version = "with_deletion")
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке перевода денег, когда баланс равен 0")
    public void userSeesErrorMessageWhenHisBalanceIsZeroAndHeTransferMoneyOld() {

        double expectedZeroBalance = 0.00;
        int expectedListSize = 2;

        final String firstUserToken = SessionStorage.getUserTokenFromStorage(1);
        final String secondUserToken = SessionStorage.getUserTokenFromStorage(2);
        final int firstUserAccount = SessionStorage.getUserAccountByUserToken(firstUserToken, 1);
        final int secondUserAccount = SessionStorage.getUserAccountByUserToken(secondUserToken, 1);

        final ChangeUserRequest changeUserRequest = RandomModelGenerator.generate(ChangeUserRequest.class);

        new ValidatableCrudRequester<CreateUserResponse>(RequestSpecs.withToken(secondUserToken),
                EndpointRequests.UPDATE_USER, ResponseSpecs.requestReturnsOk()).PUT(changeUserRequest);

        transferPage
                .open()
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(firstUserAccount)
                .checkSelectedAccountInListOld(firstUserToken, firstUserAccount)
                .inputRecipientName(changeUserRequest.getName())
                .inputRecipientAccount(secondUserAccount)
                .inputAmountValue(expectedZeroBalance)
                .clickConfirmCheckboxToChecked()
                .clickTransferButton();

        final String expectedAccountInfoInList = getAccountInfoListOld(firstUserToken, firstUserAccount);

        transferPage
                .checkMessageFromModalPageAndAccept(TRANSFER_ERROR_NEGATIVE_VALUE_OLD.getValue())
                .checkTransferPageOpened()
                .checkSelectedAccountDoesntChange(expectedAccountInfoInList)
                .checkRecipientNameDoesntChange(changeUserRequest.getName())
                .checkRecipientAccountDoesntChange(secondUserAccount)
                .checkAmountValueDoesntChange(expectedZeroBalance)
                .checkConfirmCheckboxChecked();

        final double actualUserBalance = UserSteps.getUserBalance(firstUserToken, firstUserAccount);
        assertThat(actualUserBalance).isEqualTo(expectedZeroBalance);

        final double actualSecondUserBalance = UserSteps.getUserBalance(secondUserToken, secondUserAccount);
        assertThat(actualSecondUserBalance).isEqualTo(expectedZeroBalance);
    }

    @AdminSession(amountUsers = 2)
    @UserSession
    @Test
    @ApiVersion(version = "preprod_version")
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке перевода денег, когда баланс равен 0")
    public void userSeesErrorMessageWhenHisBalanceIsZeroAndHeTransferMoney() throws SQLException {

        double expectedZeroBalance = 0.00;
        int expectedListSize = 2;

        final String firstUserToken = SessionStorage.getUserTokenFromStorage(1);
        final String secondUserToken = SessionStorage.getUserTokenFromStorage(2);
        final UserAccountResponse firstUserAccountInfo = SessionStorage.getUserAccountByUserNumber(1, 1);
        final int firstUserAccount = firstUserAccountInfo.getId();
        final UserAccountResponse secondUserAccountInfo = SessionStorage.getUserAccountByUserNumber(2, 1);
        final int secondUserAccount = secondUserAccountInfo.getId();

        final ChangeUserRequest changeUserRequest = RandomModelGenerator.generate(ChangeUserRequest.class);

        new ValidatableCrudRequester<CreateUserResponse>(RequestSpecs.withToken(secondUserToken),
                EndpointRequests.UPDATE_USER, ResponseSpecs.requestReturnsOk()).PUT(changeUserRequest);

        transferPage
                .open()
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(firstUserAccount)
                .checkSelectedAccountInList(firstUserToken, firstUserAccountInfo.getAccountNumber())
                .inputRecipientName(changeUserRequest.getName())
                .inputRecipientAccount(secondUserAccountInfo.getAccountNumber())
                .inputAmountValue(expectedZeroBalance)
                .clickConfirmCheckboxToChecked()
                .clickTransferButton();

        final String expectedAccountInfoInList = getAccountInfoList(firstUserToken, firstUserAccountInfo.getAccountNumber());

        transferPage
                .checkMessageFromModalPageAndAccept(TRANSFER_ERROR_NEGATIVE_VALUE.getValue())
                .checkTransferPageOpened()
                .checkSelectedAccountDoesntChange(expectedAccountInfoInList)
                .checkRecipientNameDoesntChange(changeUserRequest.getName())
                .checkRecipientAccountDoesntChange(secondUserAccountInfo.getAccountNumber())
                .checkAmountValueDoesntChange(expectedZeroBalance)
                .checkConfirmCheckboxChecked();

        final double actualUserBalance = UserSteps.getUserBalance(firstUserToken, firstUserAccount);
        assertThat(actualUserBalance).isEqualTo(expectedZeroBalance);

        final AccountsDao firstUserAccountByAccountIdJDBC = DBSteps.getAccountByAccountIdJDBC(firstUserAccount);
        assertThat(firstUserAccountByAccountIdJDBC.getBalance()).isEqualTo(expectedZeroBalance);

        final double actualSecondUserBalance = UserSteps.getUserBalance(secondUserToken, secondUserAccount);
        assertThat(actualSecondUserBalance).isEqualTo(expectedZeroBalance);

        final AccountsDao secondUserAccountByAccountIdJDBC = DBSteps.getAccountByAccountIdJDBC(secondUserAccount);
        assertThat(secondUserAccountByAccountIdJDBC.getBalance()).isEqualTo(expectedZeroBalance);
    }

    @AdminSession(amountUsers = 2)
    @UserSession
    @Test
    @ApiVersion(version = "with_deletion")
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке перевода денег без заполнения обязательных полей")
    public void userSeesErrorMessageWhenTryTransferWithoutRequiredFieldsOld() {

        double expectedRandomMoney = RandomData.getMoney();
        int expectedListSize = 2;

        final String firstUserToken = SessionStorage.getUserTokenFromStorage(1);
        final String secondUserToken = SessionStorage.getUserTokenFromStorage(2);
        final int firstUserAccount = SessionStorage.getUserAccountByUserToken(firstUserToken, 1);
        final int secondUserAccount = SessionStorage.getUserAccountByUserToken(secondUserToken, 1);

        final ChangeUserRequest changeUserRequest = RandomModelGenerator.generate(ChangeUserRequest.class);

        new ValidatableCrudRequester<CreateUserResponse>(RequestSpecs.withToken(secondUserToken),
                EndpointRequests.UPDATE_USER, ResponseSpecs.requestReturnsOk()).PUT(changeUserRequest);

        UserSteps.depositMoney(firstUserToken, firstUserAccount, expectedRandomMoney);

        transferPage
                .open()
                .checkTransferPageOpened()
                .clickTransferButton()
                .checkMessageFromModalPageAndAccept(TRANSFER_ERROR_WITHOUT_REQUIRED_FIELDS.getValue())
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(firstUserAccount)
                .checkSelectedAccountInListOld(firstUserToken, firstUserAccount)
                .clickTransferButton();

        final String expectedAccountInfoInList = getAccountInfoListOld(firstUserToken, firstUserAccount);

        transferPage
                .checkMessageFromModalPageAndAccept(TRANSFER_ERROR_WITHOUT_REQUIRED_FIELDS.getValue())
                .checkTransferPageOpened()
                .checkSelectedAccountDoesntChange(expectedAccountInfoInList)
                .inputRecipientName(changeUserRequest.getName())
                .clickTransferButton()
                .checkMessageFromModalPageAndAccept(TRANSFER_ERROR_WITHOUT_REQUIRED_FIELDS.getValue())
                .checkTransferPageOpened()
                .checkRecipientNameDoesntChange(changeUserRequest.getName())
                .inputRecipientAccount(secondUserAccount)
                .clickTransferButton()
                .checkMessageFromModalPageAndAccept(TRANSFER_ERROR_WITHOUT_REQUIRED_FIELDS.getValue())
                .checkTransferPageOpened()
                .checkRecipientAccountDoesntChange(secondUserAccount)
                .inputAmountValue(expectedRandomMoney)
                .clickTransferButton()
                .checkMessageFromModalPageAndAccept(TRANSFER_ERROR_WITHOUT_REQUIRED_FIELDS.getValue())
                .checkTransferPageOpened()
                .checkAmountValueDoesntChange(expectedRandomMoney)
                .clickConfirmCheckboxToChecked()
                .clickTransferButton();

        final String expectedSuccessfulAlertText = transferPage.expectedSuccessfulTransferModalMessageOld(expectedRandomMoney, secondUserAccount);
        transferPage.checkMessageFromModalPageAndAccept(expectedSuccessfulAlertText);

        final double actualSecondUserBalance = UserSteps.getUserBalance(secondUserToken, secondUserAccount);
        assertThat(actualSecondUserBalance).isEqualTo(expectedRandomMoney);
    }

    @AdminSession(amountUsers = 2)
    @UserSession
    @Test
    @ApiVersion(version = "preprod_version")
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке перевода денег без заполнения обязательных полей")
    public void userSeesErrorMessageWhenTryTransferWithoutRequiredFields() throws SQLException {

        double expectedRandomMoney = RandomData.getMoney();
        int expectedListSize = 2;

        final String firstUserToken = SessionStorage.getUserTokenFromStorage(1);
        final String secondUserToken = SessionStorage.getUserTokenFromStorage(2);
        final UserAccountResponse firstUserAccountInfo = SessionStorage.getUserAccountByUserNumber(1, 1);
        final int firstUserAccount = firstUserAccountInfo.getId();
        final UserAccountResponse secondUserAccountInfo = SessionStorage.getUserAccountByUserNumber(2, 1);
        final int secondUserAccount = secondUserAccountInfo.getId();

        final ChangeUserRequest changeUserRequest = RandomModelGenerator.generate(ChangeUserRequest.class);

        new ValidatableCrudRequester<CreateUserResponse>(RequestSpecs.withToken(secondUserToken),
                EndpointRequests.UPDATE_USER, ResponseSpecs.requestReturnsOk()).PUT(changeUserRequest);

        UserSteps.depositMoney(firstUserToken, firstUserAccount, expectedRandomMoney);

        transferPage
                .open()
                .checkTransferPageOpened()
                .clickTransferButton()
                .checkMessageFromModalPageAndAccept(TRANSFER_ERROR_WITHOUT_REQUIRED_FIELDS.getValue())
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(firstUserAccount)
                .checkSelectedAccountInList(firstUserToken, firstUserAccountInfo.getAccountNumber())
                .clickTransferButton();

        final String expectedAccountInfoInList = getAccountInfoList(firstUserToken, firstUserAccountInfo.getAccountNumber());

        transferPage
                .checkMessageFromModalPageAndAccept(TRANSFER_ERROR_WITHOUT_REQUIRED_FIELDS.getValue())
                .checkTransferPageOpened()
                .checkSelectedAccountDoesntChange(expectedAccountInfoInList)
                .inputRecipientName(changeUserRequest.getName())
                .clickTransferButton()
                .checkMessageFromModalPageAndAccept(TRANSFER_ERROR_WITHOUT_REQUIRED_FIELDS.getValue())
                .checkTransferPageOpened()
                .checkRecipientNameDoesntChange(changeUserRequest.getName())
                .inputRecipientAccount(secondUserAccountInfo.getAccountNumber())
                .clickTransferButton()
                .checkMessageFromModalPageAndAccept(TRANSFER_ERROR_WITHOUT_REQUIRED_FIELDS.getValue())
                .checkTransferPageOpened()
                .checkRecipientAccountDoesntChange(secondUserAccountInfo.getAccountNumber())
                .inputAmountValue(expectedRandomMoney)
                .clickTransferButton()
                .checkMessageFromModalPageAndAccept(TRANSFER_ERROR_WITHOUT_REQUIRED_FIELDS.getValue())
                .checkTransferPageOpened()
                .checkAmountValueDoesntChange(expectedRandomMoney)
                .clickConfirmCheckboxToChecked()
                .clickTransferButton();

        final String expectedSuccessfulAlertText = transferPage.expectedSuccessfulTransferModalMessage(expectedRandomMoney, secondUserAccountInfo.getAccountNumber());
        transferPage.checkMessageFromModalPageAndAccept(expectedSuccessfulAlertText);

        final double actualSecondUserBalance = UserSteps.getUserBalance(secondUserToken, secondUserAccount);
        assertThat(actualSecondUserBalance).isEqualTo(expectedRandomMoney);

        final AccountsDao secondUserAccountByAccountIdJDBC = DBSteps.getAccountByAccountIdJDBC(secondUserAccount);
        assertThat(secondUserAccountByAccountIdJDBC.getBalance()).isEqualTo(expectedRandomMoney);
    }

    @AdminSession
    @UserSession
    @Test
    @ApiVersion(version = "with_deletion")
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке перевода денег на несуществующий аккаунт")
    public void userSeesErrorMessageWhenTransferMoneyToUnexistedAccountOld() {

        double expectedRandomMoney = RandomData.getMoney();
        String randomRecipientName = RandomData.randomName(3);
        int expectedListSize = 2;
        int maxAccountIdDeltaForConcurrentRun = 100;

        final String firstUserToken = SessionStorage.getUserTokenFromStorage(1);
        final int firstUserAccount = SessionStorage.getUserAccountByUserToken(firstUserToken, 1);
        final int maxExistedAccountId = AdminSteps.getMaxExistedAccountId() + maxAccountIdDeltaForConcurrentRun;

        UserSteps.depositMoney(firstUserToken, firstUserAccount, expectedRandomMoney);

        transferPage
                .open()
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(firstUserAccount)
                .checkSelectedAccountInListOld(firstUserToken, firstUserAccount)
                .inputRecipientName(randomRecipientName)
                .inputRecipientAccount(maxExistedAccountId)
                .inputAmountValue(expectedRandomMoney)
                .clickConfirmCheckboxToChecked()
                .clickTransferButton();

        final String expectedAccountInfoInList = getAccountInfoListOld(firstUserToken, firstUserAccount);

        transferPage
                .checkMessageFromModalPageAndAccept(TRANSFER_ERROR_UNEXISTED_ACCOUNT.getValue())
                .checkTransferPageOpened()
                .checkSelectedAccountDoesntChange(expectedAccountInfoInList)
                .checkRecipientNameDoesntChange(randomRecipientName)
                .checkRecipientAccountDoesntChange(maxExistedAccountId)
                .checkAmountValueDoesntChange(expectedRandomMoney)
                .checkConfirmCheckboxChecked();

        final double actualSecondUserBalance = UserSteps.getUserBalance(firstUserToken, firstUserAccount);
        assertThat(actualSecondUserBalance).isEqualTo(expectedRandomMoney);
    }

    @AdminSession
    @UserSession
    @Test
    @ApiVersion(version = "preprod_version")
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке перевода денег на несуществующий аккаунт")
    public void userSeesErrorMessageWhenTransferMoneyToUnexistedAccount() throws SQLException {

        double expectedRandomMoney = RandomData.getMoney();
        String randomRecipientName = RandomData.randomName(3);
        int expectedListSize = 2;
        int maxAccountIdDeltaForConcurrentRun = 100;

        final String firstUserToken = SessionStorage.getUserTokenFromStorage(1);
        final UserAccountResponse firstUserAccountInfo = SessionStorage.getUserAccountByUserNumber(1, 1);
        final int firstUserAccount = firstUserAccountInfo.getId();
        final int maxExistedAccountId = AdminSteps.getMaxExistedAccountId() + maxAccountIdDeltaForConcurrentRun;

        UserSteps.depositMoney(firstUserToken, firstUserAccount, expectedRandomMoney);

        transferPage
                .open()
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(firstUserAccount)
                .checkSelectedAccountInList(firstUserToken, firstUserAccountInfo.getAccountNumber())
                .inputRecipientName(randomRecipientName)
                .inputRecipientAccount(maxExistedAccountId)
                .inputAmountValue(expectedRandomMoney)
                .clickConfirmCheckboxToChecked()
                .clickTransferButton();

        final String expectedAccountInfoInList = getAccountInfoList(firstUserToken, firstUserAccountInfo.getAccountNumber());

        transferPage
                .checkMessageFromModalPageAndAccept(TRANSFER_ERROR_UNEXISTED_ACCOUNT.getValue())
                .checkTransferPageOpened()
                .checkSelectedAccountDoesntChange(expectedAccountInfoInList)
                .checkRecipientNameDoesntChange(randomRecipientName)
                .checkRecipientAccountDoesntChange(maxExistedAccountId)
                .checkAmountValueDoesntChange(expectedRandomMoney)
                .checkConfirmCheckboxChecked();

        final double actualSecondUserBalance = UserSteps.getUserBalance(firstUserToken, firstUserAccount);
        assertThat(actualSecondUserBalance).isEqualTo(expectedRandomMoney);

        final AccountsDao secondUserAccountByAccountIdJDBC = DBSteps.getAccountByAccountIdJDBC(firstUserAccount);
        assertThat(secondUserAccountByAccountIdJDBC.getBalance()).isEqualTo(expectedRandomMoney);
    }

    @AdminSession(amountUsers = 2)
    @UserSession
    @Test
    @ApiVersion(version = "with_deletion")
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке перевода денег с указанием " +
            " имени пользователя аккаунта другим регистром, когда имя задано")
    public void userSeesErrorMessageWhenTransferMoneyWithIncorrectNameAccountWhenNameIsUpperCaseOld() {

        double expectedRandomMoney = RandomData.getMoney();
        double zeroBalance = 0.00;
        int expectedListSize = 2;

        final String firstUserToken = SessionStorage.getUserTokenFromStorage(1);
        final String secondUserToken = SessionStorage.getUserTokenFromStorage(2);
        final int firstUserAccount = SessionStorage.getUserAccountByUserToken(firstUserToken, 1);
        final int secondUserAccount = SessionStorage.getUserAccountByUserToken(secondUserToken, 1);
        final ChangeUserRequest changeUserRequest = RandomModelGenerator.generate(ChangeUserRequest.class);

        new ValidatableCrudRequester<CreateUserResponse>(RequestSpecs.withToken(secondUserToken),
                EndpointRequests.UPDATE_USER, ResponseSpecs.requestReturnsOk()).PUT(changeUserRequest);

        UserSteps.depositMoney(firstUserToken, firstUserAccount, expectedRandomMoney);

        transferPage
                .open()
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(firstUserAccount)
                .checkSelectedAccountInListOld(firstUserToken, firstUserAccount)
                .inputRecipientName(changeUserRequest.getName().toUpperCase())
                .inputRecipientAccount(secondUserAccount)
                .inputAmountValue(expectedRandomMoney)
                .checkConfirmCheckboxUnchecked()
                .clickConfirmCheckboxToChecked()
                .clickTransferButton();

        final String expectedAccountInfoInList = getAccountInfoListOld(firstUserToken, firstUserAccount);

        transferPage
                .checkMessageFromModalPageAndAccept(TRANSFER_ERROR_RECIPIENT_NAME_ANOTHER_CASE.getValue())
                .checkTransferPageOpened()
                .checkSelectedAccountDoesntChange(expectedAccountInfoInList)
                .checkRecipientNameDoesntChange(changeUserRequest.getName().toUpperCase())
                .checkRecipientAccountDoesntChange(secondUserAccount)
                .checkAmountValueDoesntChange(expectedRandomMoney)
                .checkConfirmCheckboxChecked();

        final double actualUserBalance = UserSteps.getUserBalance(firstUserToken, firstUserAccount);
        assertThat(actualUserBalance).isEqualTo(expectedRandomMoney);

        final double actualSecondUserBalance = UserSteps.getUserBalance(secondUserToken, secondUserAccount);
        assertThat(actualSecondUserBalance).isEqualTo(zeroBalance);
    }

    @AdminSession(amountUsers = 2)
    @UserSession
    @Test
    @ApiVersion(version = "preprod_version")
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке перевода денег с указанием " +
            " имени пользователя аккаунта другим регистром, когда имя задано")
    public void userSeesErrorMessageWhenTransferMoneyWithIncorrectNameAccountWhenNameIsUpperCase() throws SQLException {

        double expectedRandomMoney = RandomData.getMoney();
        double zeroBalance = 0.00;
        int expectedListSize = 2;

        final String firstUserToken = SessionStorage.getUserTokenFromStorage(1);
        final String secondUserToken = SessionStorage.getUserTokenFromStorage(2);
        final UserAccountResponse firstUserAccountInfo = SessionStorage.getUserAccountByUserNumber(1, 1);
        final int firstUserAccount = firstUserAccountInfo.getId();
        final UserAccountResponse secondUserAccountInfo = SessionStorage.getUserAccountByUserNumber(2, 1);
        final int secondUserAccount = secondUserAccountInfo.getId();

        final ChangeUserRequest changeUserRequest = RandomModelGenerator.generate(ChangeUserRequest.class);

        new ValidatableCrudRequester<CreateUserResponse>(RequestSpecs.withToken(secondUserToken),
                EndpointRequests.UPDATE_USER, ResponseSpecs.requestReturnsOk()).PUT(changeUserRequest);

        UserSteps.depositMoney(firstUserToken, firstUserAccount, expectedRandomMoney);

        transferPage
                .open()
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(firstUserAccount)
                .checkSelectedAccountInList(firstUserToken, firstUserAccountInfo.getAccountNumber())
                .inputRecipientName(changeUserRequest.getName().toUpperCase())
                .inputRecipientAccount(secondUserAccountInfo.getAccountNumber())
                .inputAmountValue(expectedRandomMoney)
                .checkConfirmCheckboxUnchecked()
                .clickConfirmCheckboxToChecked()
                .clickTransferButton();

        final String expectedAccountInfoInList = getAccountInfoList(firstUserToken, firstUserAccountInfo.getAccountNumber());

        transferPage
                .checkMessageFromModalPageAndAccept(TRANSFER_ERROR_RECIPIENT_NAME_ANOTHER_CASE.getValue())
                .checkTransferPageOpened()
                .checkSelectedAccountDoesntChange(expectedAccountInfoInList)
                .checkRecipientNameDoesntChange(changeUserRequest.getName().toUpperCase())
                .checkRecipientAccountDoesntChange(secondUserAccountInfo.getAccountNumber())
                .checkAmountValueDoesntChange(expectedRandomMoney)
                .checkConfirmCheckboxChecked();

        final double actualUserBalance = UserSteps.getUserBalance(firstUserToken, firstUserAccount);
        assertThat(actualUserBalance).isEqualTo(expectedRandomMoney);

        final AccountsDao firstUserAccountByAccountIdJDBC = DBSteps.getAccountByAccountIdJDBC(firstUserAccount);
        assertThat(firstUserAccountByAccountIdJDBC.getBalance()).isEqualTo(expectedRandomMoney);

        final double actualSecondUserBalance = UserSteps.getUserBalance(secondUserToken, secondUserAccount);
        assertThat(actualSecondUserBalance).isEqualTo(zeroBalance);

        final AccountsDao secondUserAccountByAccountIdJDBC = DBSteps.getAccountByAccountIdJDBC(secondUserAccount);
        assertThat(secondUserAccountByAccountIdJDBC.getBalance()).isEqualTo(zeroBalance);
    }

    @AdminSession(amountUsers = 2)
    @UserSession
    @Test
    @ApiVersion(version = "with_deletion")
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке перевода денег с указанием " +
            " аккаунта пользователя другим регистром")
    public void userSeesErrorMessageWhenTransferMoneyWithIncorrectAccountOld() {
        double expectedRandomMoney = RandomData.getMoney();
        double zeroBalance = 0.00;
        int expectedListSize = 2;

        final String firstUserToken = SessionStorage.getUserTokenFromStorage(1);
        final String secondUserToken = SessionStorage.getUserTokenFromStorage(2);
        final int firstUserAccount = SessionStorage.getUserAccountByUserToken(firstUserToken, 1);
        final int secondUserAccount = SessionStorage.getUserAccountByUserToken(secondUserToken, 1);
        final ChangeUserRequest changeUserRequest = RandomModelGenerator.generate(ChangeUserRequest.class);

        new ValidatableCrudRequester<CreateUserResponse>(RequestSpecs.withToken(secondUserToken),
                EndpointRequests.UPDATE_USER, ResponseSpecs.requestReturnsOk()).PUT(changeUserRequest);

        UserSteps.depositMoney(firstUserToken, firstUserAccount, expectedRandomMoney);

        transferPage
                .open()
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(firstUserAccount)
                .checkSelectedAccountInListOld(firstUserToken, firstUserAccount)
                .inputRecipientName(changeUserRequest.getName())
                .inputRecipientAccount(AccountData.ACCOUNT_NUMBER_PREFIX.getValue().toLowerCase() + secondUserAccount)
                .inputAmountValue(expectedRandomMoney)
                .checkConfirmCheckboxUnchecked()
                .clickConfirmCheckboxToChecked()
                .clickTransferButton();

        final String expectedAccountInfoInList = getAccountInfoListOld(firstUserToken, firstUserAccount);

        transferPage
                .checkMessageFromModalPageAndAccept(TRANSFER_ERROR_UNEXISTED_ACCOUNT.getValue())
                .checkTransferPageOpened();

        final String actualAccountInfoInListAfterTransfer = transferPage.getAccountSelector().getSelectedOptionText();
        assertThat(actualAccountInfoInListAfterTransfer).isEqualTo(expectedAccountInfoInList);

        transferPage
                .checkTransferPageOpened()
                .checkSelectedAccountDoesntChange(expectedAccountInfoInList)
                .checkRecipientNameDoesntChange(changeUserRequest.getName())
                .checkRecipientAccountDoesntChange(AccountData.ACCOUNT_NUMBER_PREFIX.getValue().toLowerCase() + secondUserAccount)
                .checkAmountValueDoesntChange(expectedRandomMoney)
                .checkConfirmCheckboxChecked();

        final double actualUserBalance = UserSteps.getUserBalance(firstUserToken, firstUserAccount);
        assertThat(actualUserBalance).isEqualTo(expectedRandomMoney);

        final double actualSecondUserBalance = UserSteps.getUserBalance(secondUserToken, secondUserAccount);
        assertThat(actualSecondUserBalance).isEqualTo(zeroBalance);
    }

    @AdminSession(amountUsers = 2)
    @UserSession
    @Test
    @ApiVersion(version = "preprod_version")
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке перевода денег с указанием " +
            " аккаунта пользователя другим регистром")
    public void userSeesErrorMessageWhenTransferMoneyWithIncorrectAccount() throws SQLException {
        double expectedRandomMoney = RandomData.getMoney();
        double zeroBalance = 0.00;
        int expectedListSize = 2;

        final String firstUserToken = SessionStorage.getUserTokenFromStorage(1);
        final String secondUserToken = SessionStorage.getUserTokenFromStorage(2);
        final UserAccountResponse firstUserAccountInfo = SessionStorage.getUserAccountByUserNumber(1, 1);
        final int firstUserAccount = firstUserAccountInfo.getId();
        final UserAccountResponse secondUserAccountInfo = SessionStorage.getUserAccountByUserNumber(2, 1);
        final int secondUserAccount = secondUserAccountInfo.getId();


        final ChangeUserRequest changeUserRequest = RandomModelGenerator.generate(ChangeUserRequest.class);

        new ValidatableCrudRequester<CreateUserResponse>(RequestSpecs.withToken(secondUserToken),
                EndpointRequests.UPDATE_USER, ResponseSpecs.requestReturnsOk()).PUT(changeUserRequest);

        UserSteps.depositMoney(firstUserToken, firstUserAccount, expectedRandomMoney);

        transferPage
                .open()
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(firstUserAccount)
                .checkSelectedAccountInList(firstUserToken, firstUserAccountInfo.getAccountNumber())
                .inputRecipientName(changeUserRequest.getName())
                .inputRecipientAccount(secondUserAccountInfo.getAccountNumber().toLowerCase())
                .inputAmountValue(expectedRandomMoney)
                .checkConfirmCheckboxUnchecked()
                .clickConfirmCheckboxToChecked()
                .clickTransferButton();

        final String expectedAccountInfoInList = getAccountInfoList(firstUserToken, firstUserAccountInfo.getAccountNumber());

        transferPage
                .checkMessageFromModalPageAndAccept(TRANSFER_ERROR_UNEXISTED_ACCOUNT.getValue())
                .checkTransferPageOpened();

        final String actualAccountInfoInListAfterTransfer = transferPage.getAccountSelector().getSelectedOptionText();
        assertThat(actualAccountInfoInListAfterTransfer).isEqualTo(expectedAccountInfoInList);

        transferPage
                .checkTransferPageOpened()
                .checkSelectedAccountDoesntChange(expectedAccountInfoInList)
                .checkRecipientNameDoesntChange(changeUserRequest.getName())
                .checkRecipientAccountDoesntChange(secondUserAccountInfo.getAccountNumber().toLowerCase())
                .checkAmountValueDoesntChange(expectedRandomMoney)
                .checkConfirmCheckboxChecked();

        final double actualUserBalance = UserSteps.getUserBalance(firstUserToken, firstUserAccount);
        assertThat(actualUserBalance).isEqualTo(expectedRandomMoney);

        final AccountsDao firstUserAccountByAccountIdJDBC = DBSteps.getAccountByAccountIdJDBC(firstUserAccount);
        assertThat(firstUserAccountByAccountIdJDBC.getBalance()).isEqualTo(expectedRandomMoney);

        final double actualSecondUserBalance = UserSteps.getUserBalance(secondUserToken, secondUserAccount);
        assertThat(actualSecondUserBalance).isEqualTo(zeroBalance);

        final AccountsDao secondUserAccountByAccountIdJDBC = DBSteps.getAccountByAccountIdJDBC(secondUserAccount);
        assertThat(secondUserAccountByAccountIdJDBC.getBalance()).isEqualTo(zeroBalance);
    }

    @AdminSession(amountUsers = 1)
    @UserSession(amountAccounts = 2)
    @Test
    @ApiVersion(version = "with_deletion")
    @DisplayName("Позитивный тест: проверка, что пользователь может просмотреть выполненные транзакции по своим аккаунтам")
    public void userCanSeeHisTransactionHistoryOld() {

        //У операций не отображается имя пользователя под которым выполнялись эти транзакции.
        //Ввиду этого, пока что у пользователя не задано name, невозможно найти его транзакции, если пробовать их искать
        //Не отображаются транзакции по переводу если не выполнить рефреш
        //Вопрос. Какая ожидается сортировка при просмотре списка транзакций? Из-за этого не стал писать проверки на порядок

        final String firstUserToken = SessionStorage.getUserTokenFromStorage(1);
        final int userFirstAccount = SessionStorage.getUserAccountByUserToken(firstUserToken, 1);
        final int userSecondAccount = SessionStorage.getUserAccountByUserToken(firstUserToken, 2);

        int expectedTransactions = 0;
        int expectedListSize = 3;
        double randomMoneyForFirstAccount = RandomData.getMoney();
        double randomMoneyForSecondAccount = RandomData.getMoney();

        transferPage
                .open()
                .checkTransferPageOpened()
                .openTransferAgainTab()
                .checkTransferAgainPageOpened()
                .checkTransactionsListSize(expectedTransactions);

        UserSteps.depositMoney(firstUserToken, userFirstAccount, randomMoneyForFirstAccount);
        UserSteps.depositMoney(firstUserToken, userSecondAccount, randomMoneyForSecondAccount);

        Selenide.refresh();

        expectedTransactions = 2;
        transferPage
                .checkTransferPageOpened()
                .openTransferAgainTab()
                .checkTransferAgainPageOpened()
                .checkTransactionsListSize(expectedTransactions);

        final List<UserTransactionHistory> transactionsHistoryList = transferPage.getTransactionsHistoryList();
        assertThat(transferPage.checkTransaction(transactionsHistoryList, randomMoneyForFirstAccount, Operations.DEPOSIT)).isTrue();
        assertThat(transferPage.checkTransaction(transactionsHistoryList, randomMoneyForSecondAccount, Operations.DEPOSIT)).isTrue();

        transferPage
                .openNewTransferTab()
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(userFirstAccount)
                .checkSelectedAccountInListOld(firstUserToken, userFirstAccount)
                .inputRecipientName(RandomData.randomName(3))
                .inputRecipientAccount(userSecondAccount)
                .inputAmountValue(randomMoneyForFirstAccount)
                .clickConfirmCheckboxToChecked()
                .clickTransferButton();

        final String expectedAlertText =
                transferPage.expectedSuccessfulTransferModalMessageOld(randomMoneyForFirstAccount, userSecondAccount);
        transferPage.checkMessageFromModalPageAndAccept(expectedAlertText);

        Selenide.refresh();

        expectedTransactions = 4;
        transferPage
                .openTransferAgainTab()
                .checkTransferAgainPageOpened()
                .checkTransactionsListSize(expectedTransactions);

        final List<UserTransactionHistory> transactionsTextTransfer = transferPage.getTransactionsHistoryList();
        assertThat(transferPage.checkTransaction(transactionsTextTransfer, randomMoneyForFirstAccount, Operations.DEPOSIT)).isTrue();
        assertThat(transferPage.checkTransaction(transactionsTextTransfer, randomMoneyForSecondAccount, Operations.DEPOSIT)).isTrue();
        assertThat(transferPage.checkTransaction(transactionsTextTransfer, randomMoneyForFirstAccount, Operations.TRANSFER_OUT)).isTrue();
        assertThat(transferPage.checkTransaction(transactionsTextTransfer, randomMoneyForFirstAccount, Operations.TRANSFER_IN)).isTrue();

    }

    @AdminSession(amountUsers = 1)
    @UserSession(amountAccounts = 2)
    @Test
    @ApiVersion(version = "preprod_version")
    @DisplayName("Позитивный тест: проверка, что пользователь может просмотреть выполненные транзакции по своим аккаунтам")
    public void userCanSeeHisTransactionHistory() {

        //Без рефреша не обновляются показатели баланса аккаунта после выполнения перевода
        //Вопрос. Какая ожидается сортировка при просмотре списка транзакций? Из-за этого не стал писать проверки на порядок

        final String firstUserToken = SessionStorage.getUserTokenFromStorage(1);
        final String secondUserToken = SessionStorage.getUserTokenFromStorage(2);
        final UserAccountResponse userFirstAccountInfo = SessionStorage.getUserAccountByUserNumber(1, 1);
        final UserAccountResponse userSecondAccountInfo = SessionStorage.getUserAccountByUserNumber(1, 2);
        final int userFirstAccount = userFirstAccountInfo.getId();
        final int userSecondAccount = userSecondAccountInfo.getId();

        int expectedTransactions = 0;
        int expectedListSize = 3;

        double randomMoneyForFirstAccount = RandomData.getMoney();
        double randomMoneyForSecondAccount = RandomData.getMoney();

        final ChangeUserRequest changeUserRequest = RandomModelGenerator.generate(ChangeUserRequest.class);

        new ValidatableCrudRequester<CreateUserResponse>(RequestSpecs.withToken(secondUserToken),
                EndpointRequests.UPDATE_USER, ResponseSpecs.requestReturnsOk()).PUT(changeUserRequest);

        UserSteps.depositMoneyWOCheckResponse(firstUserToken, userFirstAccount, randomMoneyForFirstAccount);
        UserSteps.depositMoneyWOCheckResponse(firstUserToken, userSecondAccount, randomMoneyForSecondAccount);

        transferPage
                .open()
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(userFirstAccount)
                .checkSelectedAccountInList(firstUserToken, userFirstAccountInfo.getAccountNumber())
                .inputRecipientName(changeUserRequest.getName())
                .inputRecipientAccount(userSecondAccountInfo.getAccountNumber())
                .inputAmountValue(randomMoneyForFirstAccount)
                .checkConfirmCheckboxUnchecked()
                .clickConfirmCheckboxToChecked()
                .clickTransferButton();

        final String expectedAlertText = transferPage.expectedSuccessfulTransferModalMessage(randomMoneyForFirstAccount, userSecondAccountInfo.getAccountNumber());
        transferPage.checkMessageFromModalPageAndAccept(expectedAlertText);

        expectedTransactions = 2;

        Selenide.refresh();

        transferPage
                .openNewTransferTab()
                .checkTransferPageOpened()
                .openTransferAgainTab()
                .checkTransferAgainPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(userFirstAccount)
                .checkSelectedAccountInList(firstUserToken, userFirstAccountInfo.getAccountNumber())
                .checkTransactionsListSize(expectedTransactions);

        final List<UserTransactionHistory> transactionsTextTransferFirstAccount = transferPage.getTransactionsHistoryList();
        assertThat(transferPage.checkTransaction(transactionsTextTransferFirstAccount, randomMoneyForFirstAccount, Operations.DEPOSIT)).isTrue();
        assertThat(transferPage.checkTransaction(transactionsTextTransferFirstAccount, randomMoneyForFirstAccount, Operations.TRANSFER_OUT)).isTrue();

        transferPage
                .selectAccount(userSecondAccount)
                .checkSelectedAccountInList(firstUserToken, userSecondAccountInfo.getAccountNumber())
                .checkTransactionsListSize(expectedTransactions);

        final List<UserTransactionHistory> transactionsTextTransferSecondAccount = transferPage.getTransactionsHistoryList();
        assertThat(transferPage.checkTransaction(transactionsTextTransferSecondAccount, randomMoneyForSecondAccount, Operations.DEPOSIT)).isTrue();
        assertThat(transferPage.checkTransaction(transactionsTextTransferSecondAccount, randomMoneyForFirstAccount, Operations.TRANSFER_IN)).isTrue();
    }

    @AdminSession
    @UserSession
    @Test
    @ApiVersion(version = "with_deletion")
    @Bug(true)
    //из-за того, что в многопоточном режиме при поиске транзакций по вхождению находятся транзакции от других потоков
    @DisplayName("Позитивный тест: проверка, что пользователь может находить свои транзакции по username/name")
    public void userCanFindHisTransactionHistoryByUsernameNameOld() {

        int expectedTransactions = 0;
        int expectedListSize = 3;
        double randomMoney = RandomData.getMoney();

        final String firstUserToken = SessionStorage.getUserTokenFromStorage(1);
        final int userFirstAccount = SessionStorage.getUserAccountByUserToken(firstUserToken, 1);
        final UserProfileResponse userInfo = UserSteps.getUserInfo(firstUserToken);

        UserSteps.depositMoney(firstUserToken, userFirstAccount, randomMoney);

        final int userAccountSecond = UserSteps.createUserAccount(firstUserToken);

        transferPage
                .open()
                .checkTransferPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(userFirstAccount)
                .checkSelectedAccountInListOld(firstUserToken, userFirstAccount)
                .inputRecipientName(RandomData.randomName(3))
                .inputRecipientAccount(userAccountSecond)
                .inputAmountValue(randomMoney)
                .clickConfirmCheckboxToChecked()
                .clickTransferButton();


        final String expectedAlertText =
                transferPage.expectedSuccessfulTransferModalMessageOld(randomMoney, userAccountSecond);
        transferPage.checkMessageFromModalPageAndAccept(expectedAlertText);

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


        final List<UserTransactionHistory> transactionsTextTransfer = transferPage.getTransactionsHistoryList();

        RetryUtils.retry(
                () -> transferPage.checkTransactionOld(transactionsTextTransfer, randomMoney, Operations.DEPOSIT, null),
                Boolean.TRUE::equals
        );

        assertThat(transferPage.checkTransactionOld(transactionsTextTransfer, randomMoney, Operations.TRANSFER_OUT, userInfo.getUsername())).isTrue();
        assertThat(transferPage.checkTransactionOld(transactionsTextTransfer, randomMoney, Operations.TRANSFER_IN, userInfo.getUsername())).isTrue();

        final ChangeUserRequest changeUserRequest = RandomModelGenerator.generate(ChangeUserRequest.class);

        new ValidatableCrudRequester<CreateUserResponse>(RequestSpecs.withToken(firstUserToken), EndpointRequests.UPDATE_USER, ResponseSpecs.requestReturnsOk())
                .PUT(changeUserRequest);

        Selenide.refresh();
        transferPage
                .checkTransferPageOpened()
                .openTransferAgainTab()
                .inputValueInSearchField(changeUserRequest.getName())
                .clickSearchTransactionsButton()
                .checkTransactionsListSize(expectedTransactions);

        final List<UserTransactionHistory> transactionsTextName = transferPage.getTransactionsHistoryList();

        RetryUtils.retry(
                () -> transferPage.checkTransactionOld(transactionsTextName, randomMoney, Operations.DEPOSIT, changeUserRequest.getName()),
                Boolean.TRUE::equals

        );
        assertThat(transferPage.checkTransactionOld(transactionsTextName, randomMoney, Operations.TRANSFER_OUT, changeUserRequest.getName())).isTrue();
        assertThat(transferPage.checkTransactionOld(transactionsTextName, randomMoney, Operations.TRANSFER_IN, changeUserRequest.getName())).isTrue();

        transferPage
                .inputValueInSearchField(userInfo.getUsername().toUpperCase())
                .clickSearchTransactionsButton()
                .checkTransactionsListSize(expectedTransactions);

        final List<UserTransactionHistory> transactionsTextUsernameUpperCase = transferPage.getTransactionsHistoryList();

        RetryUtils.retry(
                () -> transferPage.checkTransactionOld(transactionsTextUsernameUpperCase, randomMoney, Operations.DEPOSIT, changeUserRequest.getName()),
                Boolean.TRUE::equals

        );
        assertThat(transferPage.checkTransactionOld(transactionsTextUsernameUpperCase, randomMoney, Operations.TRANSFER_OUT, changeUserRequest.getName())).isTrue();
        assertThat(transferPage.checkTransactionOld(transactionsTextUsernameUpperCase, randomMoney, Operations.TRANSFER_IN, changeUserRequest.getName())).isTrue();

        transferPage
                .inputValueInSearchField(userInfo.getUsername().substring(0, 2))
                .clickSearchTransactionsButton()
                .checkTransactionsListSize(expectedTransactions);


        final List<UserTransactionHistory> transactionsTextUsernamePartially = transferPage.getTransactionsHistoryList();

        RetryUtils.retry(
                () -> transferPage.checkTransactionOld(transactionsTextUsernamePartially, randomMoney, Operations.DEPOSIT, changeUserRequest.getName()),
                Boolean.TRUE::equals

        );
        assertThat(transferPage.checkTransactionOld(transactionsTextUsernamePartially, randomMoney, Operations.TRANSFER_OUT, changeUserRequest.getName())).isTrue();
        assertThat(transferPage.checkTransactionOld(transactionsTextUsernamePartially, randomMoney, Operations.TRANSFER_IN, changeUserRequest.getName())).isTrue();

        transferPage
                .inputValueInSearchField(changeUserRequest.getName().split(" ")[0])
                .clickSearchTransactionsButton()
                .checkTransactionsListSize(expectedTransactions);

        final List<UserTransactionHistory> transactionsTextNamePartially = transferPage.getTransactionsHistoryList();

        RetryUtils.retry(
                () -> transferPage.checkTransactionOld(transactionsTextNamePartially, randomMoney, Operations.DEPOSIT, changeUserRequest.getName()),
                Boolean.TRUE::equals

        );
        assertThat(transferPage.checkTransactionOld(transactionsTextNamePartially, randomMoney, Operations.TRANSFER_OUT, changeUserRequest.getName())).isTrue();
        assertThat(transferPage.checkTransactionOld(transactionsTextNamePartially, randomMoney, Operations.TRANSFER_IN, changeUserRequest.getName())).isTrue();
    }


    @AdminSession(amountUsers = 2)
    @UserSession
    @Bug(true)
    @ApiVersion(version = "with_deletion")
    @Test
    @DisplayName("Негативный тест: проверка, что пользователь не может находить чужие транзакции по username/name")
    public void userCannotFindTransactionHistoryByOtherUsers() {

        //На последнем шаге баг - пользователь может просмотреть чужие транзакции

        final double randomMoney = RandomData.getMoney();

        final String firstUserToken = SessionStorage.getUserTokenFromStorage(1);
        final String secondUserToken = SessionStorage.getUserTokenFromStorage(2);
        final int firstUserAccount = SessionStorage.getUserAccountByUserToken(firstUserToken, 1);
        final int secondUserAccount = SessionStorage.getUserAccountByUserToken(secondUserToken, 1);

        UserSteps.depositMoney(firstUserToken, firstUserAccount, randomMoney);

        UserSteps.successfulTransferMoneyBetweenAccounts(firstUserToken, firstUserAccount, secondUserAccount, randomMoney);

        final UserProfileResponse secondUserInfo = UserSteps.getUserInfo(secondUserToken);

        transferPage
                .open()
                .checkTransferPageOpened()
                .openTransferAgainTab()
                .checkTransferAgainPageOpened()
                .inputValueInSearchField(secondUserInfo.getUsername())
                .clickSearchTransactionsButton();

        final List<UserTransactionHistory> transactionsTexts = transferPage.getTransactionsHistoryList();
        assertThat(transferPage.checkTransactionOld(transactionsTexts, randomMoney, Operations.TRANSFER_IN, secondUserInfo.getUsername())).isFalse();
    }

    @AdminSession(amountUsers = 2)
    @UserSession
    @Test
    @Bug(true)
    @ApiVersion(version = "with_deletion")
    //сделал дефектом, т.к. даже ожидания не помогают. Тут явно проблема уже в рендере фронта, а не в том, что тест флаки
    @DisplayName("Негативный тест: проверка отображения ошибки при попытке поиска транзакций с указанием " +
            "несуществующего username/name")
    public void userCannotFindTransactionHistoryByNotExistedUsernameOrName() {

        double randomMoney = RandomData.getMoney();
        int expectedTransactions = 0;

        final String firstUserToken = SessionStorage.getUserTokenFromStorage(1);
        final String secondUserToken = SessionStorage.getUserTokenFromStorage(2);
        final int firstUserAccount = SessionStorage.getUserAccountByUserToken(firstUserToken, 1);
        final int secondUserAccount = SessionStorage.getUserAccountByUserToken(secondUserToken, 1);

        UserSteps.depositMoney(firstUserToken, firstUserAccount, randomMoney);

        UserSteps.successfulTransferMoneyBetweenAccounts(firstUserToken, firstUserAccount, secondUserAccount, randomMoney);

        transferPage
                .open()
                .checkTransferPageOpened()
                .openTransferAgainTab()
                .inputValueInSearchField(RandomData.randomName(15))
                .clickSearchTransactionsButton()
                .checkMessageFromModalPageAndAccept(TRANSFER_ERROR_UNEXISTED_NAME.getValue())
                .checkTransactionsListSize(expectedTransactions);

        /* При ожиданиях уже такая ошибка ловится
        //[ERROR]   Run 14: TransferTests.userCannotFindTransactionHistoryByNotExistedUsernameOrName:821 »
        Runtime Ожидаемое условие: ui.pages.TransferPage$$Lambda$1427/0x00000006016f0220@366558a2
        при выполнении ui.pages.TransferPage$$Lambda$1426/0x00000006016f0000@bb2c986 не получено
         */
    }

    @AdminSession(amountUsers = 2)
    @UserSession(amountAccounts = 2)
    @Bug(true)
    @Test
    @ApiVersion(version = "with_deletion")
    @DisplayName("Позитивный тест: проверка, что пользователь может выполнить повторно ранее выполненные " +
            "транзакции с типом DEPOSIT")
    public void userCanRepeatHisTransactionForDepositFromTransactionHistoryOld() {

        //На последних двух шагах есть дефект: при повторении операции Deposit на самом деле
        //вызывается операция Transfer и это приводит к ошибке, т.к. используется значение Amount,
        //которое уже больше, чем баланс первого аккаунта

        final String firstUserToken = SessionStorage.getUserTokenFromStorage(1);
        final String secondUserToken = SessionStorage.getUserTokenFromStorage(2);
        final int firstUserFirstAccount = SessionStorage.getUserAccountByUserToken(firstUserToken, 1);
        final int firstUserSecondAccount = SessionStorage.getUserAccountByUserToken(firstUserToken, 2);
        final int secondUserAccount = SessionStorage.getUserAccountByUserToken(secondUserToken, 1);

        int expectedTransactions = 3;
        double randomMoneyDeposit = RandomData.getMoneyFromTo(3000, 5000);
        double randomMoneyTransfer = RandomData.getMoneyFromTo(2000, 2999);
        int expectedListSize = 3;

        final UserProfileResponse userInfo = UserSteps.getUserInfo(firstUserToken);

        UserSteps.depositMoney(firstUserToken, firstUserFirstAccount, randomMoneyDeposit);

        UserSteps.successfulTransferMoneyBetweenAccounts(firstUserToken, firstUserFirstAccount, firstUserSecondAccount, randomMoneyTransfer);

        transferPage
                .open()
                .checkTransferPageOpened()
                .openTransferAgainTab()
                .checkTransferAgainPageOpened()
                .checkTransactionsListSize(expectedTransactions)
                .inputValueInSearchField(userInfo.getUsername())
                .clickSearchTransactionsButton()
                .checkTransactionsListSize(expectedTransactions);

        final List<UserTransactionHistory> transactionsTextTransfer = transferPage.getTransactionsHistoryList();
        assertThat(transferPage.checkTransactionOld(transactionsTextTransfer, randomMoneyDeposit, Operations.DEPOSIT, userInfo.getUsername())).isTrue();
        assertThat(transferPage.checkTransactionOld(transactionsTextTransfer, randomMoneyTransfer, Operations.TRANSFER_OUT, userInfo.getUsername())).isTrue();
        assertThat(transferPage.checkTransactionOld(transactionsTextTransfer, randomMoneyTransfer, Operations.TRANSFER_IN, userInfo.getUsername())).isTrue();

        transferPage
                .clickRepeatButtonTransaction(Operations.DEPOSIT, randomMoneyDeposit)
                .checkTransferModalTitleRepeatVisible();

        final String actualTransactionMessage = transferPage.getTransactionInfoInRepeatModal().text();
        final String expectedTransactionMessage = TRANSACTION_MESSAGE_REPEAT_MODAL.getValue() + firstUserFirstAccount;
        assertThat(actualTransactionMessage).isEqualTo(expectedTransactionMessage);

        transferPage
                .checkDefaultValueInAccountListRepeatModal()
                .checkAccountSizeInRepeatModal(expectedListSize)
                .selectAccountInRepeatModal(firstUserFirstAccount);

        final String actualAccountInfoInList = transferPage.getAccountSelectorInRepeatModal().getSelectedOptionText();
        final String expectedAccountInfoInList = getAccountInfoListOld(firstUserToken, firstUserFirstAccount);
        assertThat(actualAccountInfoInList).isEqualTo(expectedAccountInfoInList);

        transferPage
                .checkAmountValueFieldRepeatModal(randomMoneyDeposit)
                .clickConfirmCheckboxToChecked()
                .clickTransferButton();

        final String expectedAlertText =
                transferPage.getPage(DepositPage.class).expectedSuccessfullyDepositModalMessageOld(randomMoneyDeposit, firstUserFirstAccount);
        transferPage.checkMessageFromModalPageAndAccept(expectedAlertText);

        final double expectedBalanceFirstAccount = randomMoneyDeposit - randomMoneyTransfer + randomMoneyDeposit;
        final double actualSecondUserBalance = UserSteps.getUserBalance(secondUserToken, secondUserAccount);
        assertThat(actualSecondUserBalance).isEqualTo(expectedBalanceFirstAccount);
    }

    @AdminSession()
    @UserSession(amountAccounts = 2)
    @Test
    @ApiVersion(version = "preprod_version")
    @DisplayName("Негативный тест: проверка, что пользователь не может выполнить повторно ранее выполненные " +
            "транзакции с типом DEPOSIT")
    public void userCannotRepeatHisTransactionForDepositFromTransactionHistory() {

        final String firstUserToken = SessionStorage.getUserTokenFromStorage(1);
        final UserAccountResponse userFirstAccountInfo = SessionStorage.getUserAccountByUserNumber(1, 1);
        final UserAccountResponse userSecondAccountInfo = SessionStorage.getUserAccountByUserNumber(1, 2);
        final int userFirstAccount = userFirstAccountInfo.getId();
        final int userSecondAccount = userSecondAccountInfo.getId();

        int expectedTransactions = 2;
        double randomMoneyDeposit = RandomData.getMoneyFromTo(3000, 5000);
        double randomMoneyTransfer = RandomData.getMoneyFromTo(2000, 2999);
        int expectedListSize = 3;

        UserSteps.depositMoneyWOCheckResponse(firstUserToken, userFirstAccount, randomMoneyDeposit);

        UserSteps.successfulTransferMoneyBetweenAccounts(firstUserToken, userFirstAccount, userSecondAccount, randomMoneyTransfer);

        transferPage
                .open()
                .openNewTransferTab()
                .checkTransferPageOpened()
                .openTransferAgainTab()
                .checkTransferAgainPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(userFirstAccount)
                .checkSelectedAccountInList(firstUserToken, userFirstAccountInfo.getAccountNumber())
                .checkTransactionsListSize(expectedTransactions);

        final List<UserTransactionHistory> transactionsTextTransfer = transferPage.getTransactionsHistoryList();
        transferPage.checkTransactionDetails(transactionsTextTransfer, randomMoneyDeposit, Operations.DEPOSIT, userFirstAccountInfo, firstUserToken);
        transferPage.checkTransactionDetails(transactionsTextTransfer, randomMoneyTransfer, Operations.TRANSFER_OUT, userFirstAccountInfo, firstUserToken);

        assertThat(transferPage
                .checkRepeatButtonNotAccessable(Operations.DEPOSIT)).isTrue();
        assertThat(transferPage
                .checkRepeatButtonNotAccessable(Operations.TRANSFER_IN)).isFalse();
        assertThat(transferPage
                .checkRepeatButtonNotAccessable(Operations.TRANSFER_OUT)).isFalse();
    }

    @AdminSession(amountUsers = 2)
    @UserSession
    @Bug(true)
    @Test
    @ApiVersion(version = "with_deletion")
    @DisplayName("Позитивный тест: проверка, что пользователь может выполнить повторно ранее выполненные " +
            "транзакции c типом TRANSFER_OUT")
    public void userCanRepeatHisTransactionsForTransferFromTransactionHistoryOld() {

        //На последних двух шагах баг: происходит перевод на тот же аккаунт с которого происходит перевод несмотря на то,
        // что в описании сказано, что перевод происходит на другой аккаунт

        //Какой ОР, если выбрать 'Repeat' для транзакции с типом TRANSFER_IN? Описывать пока не стал

        int expectedTransactions = 2;
        double randomMoneyDeposit = RandomData.getMoneyFromTo(4000, 5000);
        double randomMoneyTransfer = RandomData.getMoneyFromTo(1000, 2000);
        int expectedListSize = 2;

        final String firstUserToken = SessionStorage.getUserTokenFromStorage(1);
        final String secondUserToken = SessionStorage.getUserTokenFromStorage(2);
        final int firstUserFirstAccount = SessionStorage.getUserAccountByUserToken(firstUserToken, 1);
        final int secondUserAccount = SessionStorage.getUserAccountByUserToken(secondUserToken, 1);


        UserProfileResponse userInfo = UserSteps.getUserInfo(firstUserToken);

        UserSteps.depositMoney(firstUserToken, firstUserFirstAccount, randomMoneyDeposit);

        UserSteps.successfulTransferMoneyBetweenAccounts(firstUserToken, firstUserFirstAccount, secondUserAccount, randomMoneyTransfer);

        transferPage
                .open()
                .checkTransferPageOpened()
                .openTransferAgainTab()
                .checkTransferAgainPageOpened()
                .checkTransactionsListSize(expectedTransactions)
                .inputValueInSearchField(userInfo.getUsername())
                .clickSearchTransactionsButton()
                .checkTransactionsListSize(expectedTransactions);


        final List<UserTransactionHistory> transactionsTextTransfer = transferPage.getTransactionsHistoryList();

        assertThat(transferPage.checkTransactionOld(transactionsTextTransfer, randomMoneyDeposit, Operations.DEPOSIT, userInfo.getUsername())).isTrue();
        assertThat(transferPage.checkTransactionOld(transactionsTextTransfer, randomMoneyTransfer, Operations.TRANSFER_OUT, userInfo.getUsername())).isTrue();

        transferPage
                .clickRepeatButtonTransaction(Operations.TRANSFER_OUT, randomMoneyTransfer)
                .checkTransferModalTitleRepeatVisible();

        final String actualTransactionMessage = transferPage.getTransactionInfoInRepeatModal().text();
        final String expectedTransactionMessage = TRANSACTION_MESSAGE_REPEAT_MODAL.getValue() + secondUserAccount;
        assertThat(actualTransactionMessage).isEqualTo(expectedTransactionMessage);

        transferPage
                .checkDefaultValueInAccountListRepeatModal()
                .checkAccountSizeInRepeatModal(expectedListSize)
                .selectAccountInRepeatModal(firstUserFirstAccount);

        final String actualAccountInfoInList = transferPage.getAccountSelectorInRepeatModal().getSelectedOptionText();
        final String expectedAccountInfoInList = getAccountInfoListOld(firstUserToken, firstUserFirstAccount);
        assertThat(actualAccountInfoInList).isEqualTo(expectedAccountInfoInList);

        transferPage
                .checkAmountValueFieldRepeatModal(randomMoneyTransfer)
                .clickConfirmCheckboxToChecked()
                .clickTransferButton();

        final String expectedAlertText = transferPage.expectedSuccessfulTransferModalMessageOld(randomMoneyDeposit, secondUserAccount);
        transferPage.checkMessageFromModalPageAndAccept(expectedAlertText);

        final double expectedSecondUserBalance = randomMoneyTransfer + randomMoneyTransfer;
        final double actualSecondUserBalance = UserSteps.getUserBalance(secondUserToken, secondUserAccount);
        assertThat(actualSecondUserBalance).isEqualTo(expectedSecondUserBalance);
    }

    @AdminSession(amountUsers = 2)
    @UserSession
    @Test
    @ApiVersion(version = "preprod_version")
    @DisplayName("Позитивный тест: проверка, что пользователь может выполнить повторно ранее выполненные " +
            "транзакции c типом TRANSFER_OUT")
    public void userCanRepeatHisTransactionsForTransferFromTransactionHistory() throws SQLException {

        int expectedTransactions = 2;
        double randomMoneyDeposit = RandomData.getMoneyFromTo(4000, 5000);
        double randomMoneyTransfer = RandomData.getMoneyFromTo(1000, 2000);
        int expectedListSize = 2;

        final String firstUserToken = SessionStorage.getUserTokenFromStorage(1);
        final String secondUserToken = SessionStorage.getUserTokenFromStorage(2);
        final UserAccountResponse firstUserAccountInfo = SessionStorage.getUserAccountByUserNumber(1, 1);
        final UserAccountResponse secondUserAccountInfo = SessionStorage.getUserAccountByUserNumber(2, 1);
        final int firstUserAccount = firstUserAccountInfo.getId();
        final int secondUserAccount = secondUserAccountInfo.getId();

        UserSteps.depositMoneyWOCheckResponse(firstUserToken, firstUserAccount, randomMoneyDeposit);

        UserSteps.successfulTransferMoneyBetweenAccounts(firstUserToken, firstUserAccount, secondUserAccount, randomMoneyTransfer);

        transferPage
                .open()
                .openNewTransferTab()
                .checkTransferPageOpened()
                .openTransferAgainTab()
                .checkTransferAgainPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(firstUserAccount)
                .checkSelectedAccountInList(firstUserToken, firstUserAccountInfo.getAccountNumber())
                .checkTransactionsListSize(expectedTransactions)
                .clickRepeatButtonTransaction(Operations.TRANSFER_OUT, randomMoneyTransfer)
                .checkTransferModalTitleRepeatVisible();

        final String actualTransactionMessage = transferPage.getTransactionInfoInRepeatModal().text();
        final String expectedTransactionMessage = TRANSACTION_MESSAGE_REPEAT_MODAL.getValue() + secondUserAccount;
        assertThat(actualTransactionMessage).isEqualTo(expectedTransactionMessage);

        transferPage
                .checkDefaultValueInAccountListRepeatModal()
                .checkAccountSizeInRepeatModal(expectedListSize)
                .selectAccountInRepeatModal(firstUserAccount);

        final String actualAccountInfoInList = transferPage.getAccountSelectorInRepeatModal().getSelectedOptionText();
        final String expectedAccountInfoInList = getAccountInfoList(firstUserToken, firstUserAccountInfo.getAccountNumber());
        assertThat(actualAccountInfoInList).isEqualTo(expectedAccountInfoInList);

        transferPage
                .checkAmountValueFieldRepeatModal(randomMoneyTransfer)
                .clickConfirmCheckboxToChecked()
                .clickTransferButton();

        final String expectedAlertText = transferPage.expectedSuccessfulTransferModalMessageInRepeatModal(randomMoneyTransfer, firstUserAccountInfo.getId(), secondUserAccountInfo.getId());
        transferPage.checkMessageFromModalPageAndAccept(expectedAlertText);

        final double expectedSecondUserBalance = randomMoneyTransfer + randomMoneyTransfer;
        final double actualSecondUserBalance = UserSteps.getUserBalance(secondUserToken, secondUserAccount);
        assertThat(actualSecondUserBalance).isEqualTo(expectedSecondUserBalance);

        final AccountsDao secondUserAccountByAccountIdJDBC = DBSteps.getAccountByAccountIdJDBC(secondUserAccount);
        assertThat(secondUserAccountByAccountIdJDBC.getBalance()).isEqualTo(expectedSecondUserBalance);
    }

    @AdminSession(amountUsers = 2)
    @UserSession
    @Test
    @ApiVersion(version = "with_deletion")
    @DisplayName("Негативный тест: проверка, что пользователь не может выполнить повторно ранее выполненные " +
            "транзакции, если указано значение меньше 0.01")
    public void userCanRepeatHisTransactionsFromTransactionHistoryWhenAmountLessMinimumValueOld() {

        int expectedTransactions = 2;
        double randomMoneyDeposit = RandomData.getMoneyFromTo(4000, 5000);
        double randomMoneyTransfer = RandomData.getMoneyFromTo(2000, 3000);
        double zeroMoney = 0.00;
        int expectedListSize = 2;

        final String firstUserToken = SessionStorage.getUserTokenFromStorage(1);
        final String secondUserToken = SessionStorage.getUserTokenFromStorage(2);
        final int firstUserFirstAccount = SessionStorage.getUserAccountByUserToken(firstUserToken, 1);
        final int secondUserAccount = SessionStorage.getUserAccountByUserToken(secondUserToken, 1);

        UserProfileResponse userInfo = UserSteps.getUserInfo(firstUserToken);

        UserSteps.depositMoney(firstUserToken, firstUserFirstAccount, randomMoneyDeposit);

        UserSteps.successfulTransferMoneyBetweenAccounts(firstUserToken, firstUserFirstAccount, secondUserAccount, randomMoneyTransfer);

        transferPage
                .open()
                .checkTransferPageOpened()
                .openTransferAgainTab()
                .checkTransferAgainPageOpened()
                .checkTransactionsListSize(expectedTransactions)
                .inputValueInSearchField(userInfo.getUsername())
                .clickSearchTransactionsButton()
                .checkTransactionsListSize(expectedTransactions);

        final List<UserTransactionHistory> transactionsTextTransfer = transferPage.getTransactionsHistoryList();

        assertThat(transferPage.checkTransactionOld(transactionsTextTransfer, randomMoneyDeposit, Operations.DEPOSIT, userInfo.getUsername())).isTrue();
        assertThat(transferPage.checkTransactionOld(transactionsTextTransfer, randomMoneyTransfer, Operations.TRANSFER_OUT, userInfo.getUsername())).isTrue();

        transferPage.clickRepeatButtonTransaction(Operations.TRANSFER_OUT, randomMoneyTransfer);
        transferPage.checkTransferModalTitleRepeatVisible();

        final String actualTransactionMessage = transferPage.getTransactionInfoInRepeatModal().text();
        final String expectedTransactionMessage = TRANSACTION_MESSAGE_REPEAT_MODAL.getValue() + secondUserAccount;
        assertThat(actualTransactionMessage).isEqualTo(expectedTransactionMessage);

        transferPage
                .checkDefaultValueInAccountListRepeatModal()
                .checkAccountSizeInRepeatModal(expectedListSize)
                .selectAccountInRepeatModal(firstUserFirstAccount);

        final String actualAccountInfoInList = transferPage.getAccountSelectorInRepeatModal().getSelectedOptionText();
        final String expectedAccountInfoInList = getAccountInfoListOld(firstUserToken, firstUserFirstAccount);
        assertThat(actualAccountInfoInList).isEqualTo(expectedAccountInfoInList);

        transferPage
                .checkAmountValueFieldRepeatModal(randomMoneyTransfer)
                .inputAmountValueRepeatModal(zeroMoney)
                .clickConfirmCheckboxToChecked()
                .clickTransferButton()
                .checkMessageFromModalPageAndAccept(TRANSFER_ERROR_WITH_ZERO_AMOUNT.getValue());

        final double expectedUserBalanceRaw = randomMoneyDeposit - randomMoneyTransfer;
        final double expectedUserBalance = new BigDecimal(expectedUserBalanceRaw).setScale(2, RoundingMode.HALF_UP).doubleValue();
        final double actualUserBalance = UserSteps.getUserBalance(firstUserToken, firstUserFirstAccount);
        assertThat(actualUserBalance).isEqualTo(expectedUserBalance);
    }

    @AdminSession(amountUsers = 2)
    @UserSession
    @Test
    @ApiVersion(version = "preprod_version")
    @DisplayName("Негативный тест: проверка, что пользователь не может выполнить повторно ранее выполненные " +
            "транзакции, если указано значение меньше 0.01")
    public void userCanRepeatHisTransactionsFromTransactionHistoryWhenAmountLessMinimumValue() throws SQLException {

        int expectedTransactions = 2;
        double randomMoneyDeposit = RandomData.getMoneyFromTo(4000, 5000);
        double randomMoneyTransfer = RandomData.getMoneyFromTo(2000, 3000);
        double zeroMoney = 0.00;
        int expectedListSize = 2;

        final String firstUserToken = SessionStorage.getUserTokenFromStorage(1);
        final UserAccountResponse firstUserAccountInfo = SessionStorage.getUserAccountByUserNumber(1, 1);
        final UserAccountResponse secondUserAccountInfo = SessionStorage.getUserAccountByUserNumber(2, 1);
        final int firstUserAccount = firstUserAccountInfo.getId();
        final int secondUserAccount = secondUserAccountInfo.getId();

        UserSteps.depositMoney(firstUserToken, firstUserAccount, randomMoneyDeposit);

        UserSteps.successfulTransferMoneyBetweenAccounts(firstUserToken, firstUserAccount, secondUserAccount, randomMoneyTransfer);

        transferPage
                .open()
                .openNewTransferTab()
                .checkTransferPageOpened()
                .openTransferAgainTab()
                .checkTransferAgainPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(firstUserAccount)
                .checkSelectedAccountInList(firstUserToken, firstUserAccountInfo.getAccountNumber())
                .checkTransactionsListSize(expectedTransactions)
                .clickRepeatButtonTransaction(Operations.TRANSFER_OUT, randomMoneyTransfer)
                .checkTransferModalTitleRepeatVisible();

        final String actualTransactionMessage = transferPage.getTransactionInfoInRepeatModal().text();
        final String expectedTransactionMessage = TRANSACTION_MESSAGE_REPEAT_MODAL.getValue() + secondUserAccount;
        assertThat(actualTransactionMessage).isEqualTo(expectedTransactionMessage);

        transferPage
                .checkDefaultValueInAccountListRepeatModal()
                .checkAccountSizeInRepeatModal(expectedListSize)
                .selectAccountInRepeatModal(firstUserAccount);

        final String actualAccountInfoInList = transferPage.getAccountSelectorInRepeatModal().getSelectedOptionText();
        final String expectedAccountInfoInList = getAccountInfoList(firstUserToken, firstUserAccountInfo.getAccountNumber());
        assertThat(actualAccountInfoInList).isEqualTo(expectedAccountInfoInList);

        transferPage
                .checkAmountValueFieldRepeatModal(randomMoneyTransfer)
                .inputAmountValueRepeatModal(zeroMoney)
                .clickConfirmCheckboxToChecked()
                .clickTransferButton()
                .checkMessageFromModalPageAndAccept(INVALID_TRANSFER_INSUFFICIENT_FUNDS_OR_INVALID_ACCOUNTS.getValue());

        final double expectedUserBalanceRaw = randomMoneyDeposit - randomMoneyTransfer;
        final double expectedUserBalance = new BigDecimal(expectedUserBalanceRaw).setScale(2, RoundingMode.HALF_UP).doubleValue();
        final double actualUserBalance = UserSteps.getUserBalance(firstUserToken, firstUserAccount);
        assertThat(actualUserBalance).isEqualTo(expectedUserBalance);

        final AccountsDao userAccountByAccountIdJDBC = DBSteps.getAccountByAccountIdJDBC(firstUserAccount);
        assertThat(userAccountByAccountIdJDBC.getBalance()).isEqualTo(expectedUserBalance);
    }

    @AdminSession
    @UserSession
    @Bug(true)
    @Test
    @DisplayName("Негативный тест: проверка запрета выполнения транзакции повторно если не указано одно " +
            "из обязательных полей")
    public void userCannotRepeatHisTransactionsFromTransactionHistoryIfRequiredFieldsAreNotFilledIn() {

        //В шаге проверки недоступности кнопки Send Transfer при пустом Amount - баг. Кнопка доступна.

        int expectedTransactions = 2;
        double randomMoneyDeposit = RandomData.getMoneyFromTo(4000, 5000);
        double randomMoneyTransfer = RandomData.getMoneyFromTo(2000, 3000);
        int expectedListSize = 2;

        final String firstUserToken = SessionStorage.getUserTokenFromStorage(1);
        final UserAccountResponse firstUserAccountInfo = SessionStorage.getUserAccountByUserNumber(1, 1);
        final UserAccountResponse secondUserAccountInfo = SessionStorage.getUserAccountByUserNumber(2, 1);
        final int firstUserAccount = firstUserAccountInfo.getId();
        final int secondUserAccount = secondUserAccountInfo.getId();

        UserSteps.depositMoney(firstUserToken, firstUserAccount, randomMoneyDeposit);

        UserSteps.successfulTransferMoneyBetweenAccounts(firstUserToken, firstUserAccount, secondUserAccount, randomMoneyTransfer);

        transferPage
                .open()
                .openNewTransferTab()
                .checkTransferPageOpened()
                .openTransferAgainTab()
                .checkTransferAgainPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(firstUserAccount)
                .checkSelectedAccountInList(firstUserToken, firstUserAccountInfo.getAccountNumber())
                .checkTransactionsListSize(expectedTransactions)
                .clickRepeatButtonTransaction(Operations.TRANSFER_OUT, randomMoneyTransfer)
                .checkTransferModalTitleRepeatVisible();

        final String actualTransactionMessage = transferPage.getTransactionInfoInRepeatModal().text();
        final String expectedTransactionMessage = TRANSACTION_MESSAGE_REPEAT_MODAL.getValue() + secondUserAccount;
        assertThat(actualTransactionMessage).isEqualTo(expectedTransactionMessage);

        transferPage
                .checkDefaultValueInAccountListRepeatModal()
                .checkAccountSizeInRepeatModal(expectedListSize)
                .checkAmountValueFieldRepeatModal(randomMoneyTransfer)
                .clickConfirmCheckboxToChecked();


        //Проверяем, что кнопка Send Transfer недоступна для нажатия
        //Здесь баг. Ожидаем, что кнопка не должна быть доступна
        transferPage
                .checkTransferButtonNotClickable()
                .selectAccountInRepeatModal(firstUserAccount)
                .checkSelectedAccountInListRepeatModal(firstUserToken, firstUserAccountInfo.getAccountNumber())
                .clearValueAmountRepeatModal()
                .checkConfirmCheckboxChecked()
                .checkTransferButtonNotClickable()
                .inputAmountValueRepeatModal(randomMoneyDeposit)
                .clickConfirmCheckboxToUnchecked()
                .checkTransferButtonNotClickable();
    }

    @AdminSession
    @UserSession
    @Test
    @DisplayName("Позитивный тест: проверка закрытия модального окна Repeat Transfer различными способами")
    public void userCanCloseModalPageRepeatTransferInDiffWays() {

        int expectedTransactions = 2;
        double randomMoneyDeposit = RandomData.getMoneyFromTo(4000, 5000);
        double randomMoneyTransfer = RandomData.getMoneyFromTo(2000, 3000);
        int expectedListSize = 2;

        final String firstUserToken = SessionStorage.getUserTokenFromStorage(1);
        final UserAccountResponse firstUserAccountInfo = SessionStorage.getUserAccountByUserNumber(1, 1);
        final UserAccountResponse secondUserAccountInfo = SessionStorage.getUserAccountByUserNumber(2, 1);
        final int firstUserAccount = firstUserAccountInfo.getId();
        final int secondUserAccount = secondUserAccountInfo.getId();

        UserSteps.depositMoney(firstUserToken, firstUserAccount, randomMoneyDeposit);

        UserSteps.successfulTransferMoneyBetweenAccounts(firstUserToken, firstUserAccount, secondUserAccount, randomMoneyTransfer);

        transferPage
                .open()
                .openNewTransferTab()
                .checkTransferPageOpened()
                .openTransferAgainTab()
                .checkTransferAgainPageOpened()
                .checkDefaultValueInAccountList()
                .checkAccountSize(expectedListSize)
                .selectAccount(firstUserAccount)
                .checkSelectedAccountInList(firstUserToken, firstUserAccountInfo.getAccountNumber())
                .checkTransactionsListSize(expectedTransactions)
                .clickRepeatButtonTransaction(Operations.TRANSFER_OUT, randomMoneyTransfer)
                .checkTransferModalTitleRepeatVisible()
                .checkDefaultValueInAccountListRepeatModal()
                .checkAccountSizeInRepeatModal(expectedListSize)
                .selectAccountInRepeatModal(firstUserAccount)
                .checkSelectedAccountInListRepeatModal(firstUserToken, firstUserAccountInfo.getAccountNumber())
                .clickCancelButton()
                .checkTransferModalTitleRepeatNotVisible()
                .clickRepeatButtonTransaction(Operations.TRANSFER_OUT, randomMoneyTransfer)
                .checkTransferModalTitleRepeatVisible()
                .checkSelectedAccountInListRepeatModal(firstUserToken, firstUserAccountInfo.getAccountNumber())
                .clickCloseButton()
                .checkTransferModalTitleRepeatNotVisible();
    }
}

