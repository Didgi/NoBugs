package api_tests;

import api.config.Operations;
import api.config.ResponseMessages;
import api.dao.jdbc.AccountsDao;
import api.dao.jdbc.TransactionsDao;
import api.models.TransferResponse;
import api.models.UserTransactionsResponse;
import api.requests.steps.db_steps.DBSteps;
import api.requests.steps.user_steps.UserSteps;
import api.specs.ResponseSpecs;
import api.utils.RandomData;
import common.annotations.ApiVersion;
import common.annotations.Bug;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Stream;

import static api.config.ResponseMessages.*;
import static api.requests.steps.admin_steps.AdminSteps.createUserAndGetToken;
import static api.requests.steps.admin_steps.AdminSteps.getMaxExistedAccountId;
import static api.requests.steps.user_steps.UserSteps.*;

public class TransferMoneyTests extends BaseTestSenior {

    private ZonedDateTime nowTime;
    private int nonExistingAccount = 0;

    private static Stream<Arguments> diffPositiveValue() {
        return Stream.of(
                Arguments.of(0.01, 0.01),
                Arguments.of(2500.00, 2500.00),
                Arguments.of(5000.00, 9999.99),
                Arguments.of(5000.00, 10000.00));
    }


    @MethodSource("diffPositiveValue")
    @ParameterizedTest
    @DisplayName("Позитивный тест: пользователь может переводить деньги на аккаунт другого пользователя")
    public void userCanTransferMoneyToSomeoneElseExistedAccount(double moneyToDeposit, double moneyToTransfer) throws SQLException {

        int repeatDepositTimes = 2;
        UserSteps.repeatAction(repeatDepositTimes, () -> depositMoneyWOCheckResponse(authUserToken, userAccount, moneyToDeposit));

        final String authTokenUserSecond = createUserAndGetToken();

        final int secondUserAccount = createUserAccount(authTokenUserSecond);

        nowTime = ZonedDateTime.now(ZoneOffset.UTC);

        final TransferResponse transferResponse =
                successfulTransferMoneyBetweenAccounts(authUserToken, userAccount, secondUserAccount, moneyToTransfer);

        softly.assertThat(transferResponse.getMessage()).isEqualTo(ResponseMessages.TRANSFER_SUCCESSFUL.getValue());

        final double expectedBalanceForFirstUserRawValue =
                Math.round((moneyToDeposit * repeatDepositTimes - moneyToTransfer) * 100D) / 100D;
        BigDecimal expectedBalanceForFirstUser =
                BigDecimal.valueOf(expectedBalanceForFirstUserRawValue).setScale(2, RoundingMode.HALF_UP);

        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(expectedBalanceForFirstUser.doubleValue());

        final AccountsDao firstUserAccountJDBC = DBSteps.getAccountByAccountIdJDBC(userAccount);

        softly.assertThat(firstUserAccountJDBC.getBalance()).isEqualTo(expectedBalanceForFirstUser.doubleValue());

        UserSteps.checkUserTransactions(authUserToken, userAccount, secondUserAccount, nowTime, Operations.TRANSFER_OUT, moneyToTransfer);

        UserSteps.checkUserTransactionsDb(userAccount, secondUserAccount, nowTime, Operations.TRANSFER_OUT.name(), moneyToTransfer);

        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccount)).isEqualTo(moneyToTransfer);

        final AccountsDao secondUserAccountJDBC = DBSteps.getAccountByAccountIdJDBC(secondUserAccount);

        softly.assertThat(secondUserAccountJDBC.getBalance()).isEqualTo(moneyToTransfer);

        checkUserTransactions(authTokenUserSecond, secondUserAccount, userAccount, nowTime, Operations.TRANSFER_IN, moneyToTransfer);

    }

    private static Stream<Arguments> diffNegativeValueOld() {
        return Stream.of(
                Arguments.of(0.1, -0.01, TRANSFER_AMOUNT_MUST_BE_AT_LEAST_01.getValue()),
                Arguments.of(0.1, 0.0, TRANSFER_AMOUNT_MUST_BE_AT_LEAST_01.getValue()));
    }


    @MethodSource("diffNegativeValueOld")
    @ParameterizedTest
    @ApiVersion(version = "with_deletion")
    @DisplayName("Негативный тест: пользователь не может переводить сумму меньше 0.01")
    public void userCannotTransferMoneyToSomeoneElseExistedAccountLessThanMinimumLimitOld(double moneyToDeposit,
                                                                                          double moneyToTransfer,
                                                                                          String errorMessage) {
        depositMoneyWOCheckResponse(authUserToken, userAccount, moneyToDeposit);

        final String authTokenUserSecond = createUserAndGetToken();

        final int secondUserAccount = createUserAccount(authTokenUserSecond);

        final String actualErrorMessage =
                failedTransferMoneyBetweenAccounts(authUserToken, userAccount, secondUserAccount, moneyToTransfer,
                        ResponseSpecs.requestReturnsBadRequest());

        softly.assertThat(actualErrorMessage).isEqualTo(errorMessage);

        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(moneyToDeposit);

        UserSteps.checkNegativeUserTransactions(authUserToken, userAccount, Operations.TRANSFER_OUT);

        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccount)).isEqualTo(DEFAULT_ZERO_BALANCE);

        checkNegativeUserTransactions(authTokenUserSecond, secondUserAccount, null);
    }

    private static Stream<Arguments> diffNegativeValue() {
        return Stream.of(
                Arguments.of(0.1, -0.01, INVALID_TRANSFER_INSUFFICIENT_FUNDS_OR_INVALID_ACCOUNTS.getValue()),
                Arguments.of(0.1, 0.0, INVALID_TRANSFER_INSUFFICIENT_FUNDS_OR_INVALID_ACCOUNTS.getValue()));
    }

    @MethodSource("diffNegativeValue")
    @ParameterizedTest
    @DisplayName("Негативный тест: пользователь не может переводить сумму меньше 0.01")
    public void userCannotTransferMoneyToSomeoneElseExistedAccountLessThanMinimumLimit(double moneyToDeposit,
                                                                                       double moneyToTransfer,
                                                                                       String errorMessage) throws SQLException {
        depositMoneyWOCheckResponse(authUserToken, userAccount, moneyToDeposit);

        final String authTokenUserSecond = createUserAndGetToken();

        final int secondUserAccount = createUserAccount(authTokenUserSecond);

        final String actualErrorMessage =
                failedTransferMoneyBetweenAccounts(authUserToken, userAccount, secondUserAccount, moneyToTransfer,
                        ResponseSpecs.requestReturnsBadRequest());

        softly.assertThat(actualErrorMessage).isEqualTo(errorMessage);

        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(moneyToDeposit);

        final AccountsDao firstUserAccountJDBC = DBSteps.getAccountByAccountIdJDBC(userAccount);

        softly.assertThat(firstUserAccountJDBC.getBalance()).isEqualTo(moneyToDeposit);

        UserSteps.checkNegativeUserTransactions(authUserToken, userAccount, Operations.TRANSFER_OUT);

        UserSteps.checkNegativeUserTransactionsDb(userAccount, Operations.TRANSFER_OUT);

        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccount)).isEqualTo(DEFAULT_ZERO_BALANCE);

        final AccountsDao secondUserAccountJDBC = DBSteps.getAccountByAccountIdJDBC(secondUserAccount);

        softly.assertThat(secondUserAccountJDBC.getBalance()).isEqualTo(DEFAULT_ZERO_BALANCE);

        checkNegativeUserTransactions(authTokenUserSecond, secondUserAccount, null);

        UserSteps.checkNegativeUserTransactionsDb(userAccount, null);
    }


    @Test
    @DisplayName("Негативный тест: пользователь не может переводить сумму больше 10000")
    public void userCannotTransferMoneyToSomeoneElseExistedAccountMoreThanMaximumLimit() throws SQLException {

        double moneyToDeposit = RandomData.getMoneyFromTo(4000, 5000);
        double moneyToTransfer = 10000.01;

        int repeatDepositTimes = 3;
        UserSteps.repeatAction(repeatDepositTimes, () -> depositMoneyWOCheckResponse(authUserToken, userAccount, moneyToDeposit));

        final String authTokenUserSecond = createUserAndGetToken();
        final int secondUserAccount = createUserAccount(authTokenUserSecond);

        final String actualErrorMessage =
                failedTransferMoneyBetweenAccounts(authUserToken, userAccount, secondUserAccount, moneyToTransfer,
                        ResponseSpecs.requestReturnsBadRequest());

        softly.assertThat(actualErrorMessage).isEqualTo(TRANSFER_AMOUNT_CANNOT_EXCEED_10000.getValue());

        double expectedBalance =
                BigDecimal.valueOf(moneyToDeposit * repeatDepositTimes).setScale(2, RoundingMode.HALF_UP).doubleValue();

        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(expectedBalance);

        final AccountsDao firstUserAccountJDBC = DBSteps.getAccountByAccountIdJDBC(userAccount);

        softly.assertThat(firstUserAccountJDBC.getBalance()).isEqualTo(expectedBalance);

        checkNegativeUserTransactions(authUserToken, userAccount, Operations.TRANSFER_OUT);

        UserSteps.checkNegativeUserTransactionsDb(userAccount, Operations.TRANSFER_OUT);

        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccount)).isEqualTo(DEFAULT_ZERO_BALANCE);

        final AccountsDao secondUserAccountJDBC = DBSteps.getAccountByAccountIdJDBC(secondUserAccount);

        softly.assertThat(secondUserAccountJDBC.getBalance()).isEqualTo(DEFAULT_ZERO_BALANCE);

        checkNegativeUserTransactions(authTokenUserSecond, secondUserAccount, null);

        UserSteps.checkNegativeUserTransactionsDb(userAccount, null);

    }

    @Test
    @DisplayName("Позитивный тест: пользователь может переводить деньги между своими же аккаунтами")
    public void userCanTransferMoneyBetweenHisAccounts() throws SQLException {

        final double depositTransferMoney = RandomData.getMoney();

        depositMoneyWOCheckResponse(authUserToken, userAccount, depositTransferMoney);

        final int userAccountSecond = createUserAccount(authUserToken);

        nowTime = ZonedDateTime.now(ZoneOffset.UTC);

        final TransferResponse transferResponse =
                successfulTransferMoneyBetweenAccounts(authUserToken, userAccount, userAccountSecond, depositTransferMoney);

        softly.assertThat(transferResponse.getMessage()).isEqualTo(ResponseMessages.TRANSFER_SUCCESSFUL.getValue());

        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(DEFAULT_ZERO_BALANCE);

        final AccountsDao UserFirstAccountJDBC = DBSteps.getAccountByAccountIdJDBC(userAccount);

        softly.assertThat(UserFirstAccountJDBC.getBalance()).isEqualTo(DEFAULT_ZERO_BALANCE);

        checkUserTransactions(authUserToken, userAccount, userAccountSecond, nowTime, Operations.TRANSFER_OUT, depositTransferMoney);

        checkUserTransactionsDb(userAccount, userAccountSecond, nowTime, Operations.TRANSFER_OUT.name(), depositTransferMoney);

        softly.assertThat(getUserBalance(authUserToken, userAccountSecond)).isEqualTo(depositTransferMoney);

        final AccountsDao UserAccountSecondJDBC = DBSteps.getAccountByAccountIdJDBC(userAccountSecond);

        softly.assertThat(UserAccountSecondJDBC.getBalance()).isEqualTo(depositTransferMoney);

        checkUserTransactions(authUserToken, userAccountSecond, userAccount, nowTime, Operations.TRANSFER_IN, depositTransferMoney);

        checkUserTransactionsDb(userAccountSecond, userAccount, nowTime, Operations.TRANSFER_IN.name(), depositTransferMoney);

    }

    @Test
    @DisplayName("Позитивный тест: при переводе пользователем денег пользователю на один из двух аккаунтов, " +
            "другой аккаунт не пополняется")
    public void UserCanTransferMoneyToSomeoneElseUserWithTwoExistedAccounts() throws SQLException {

        final Double depositTransferMoney = RandomData.getMoney();

        depositMoneyWOCheckResponse(authUserToken, userAccount, depositTransferMoney);

        final String authTokenUserSecond = createUserAndGetToken();
        final int secondUserAccountFirst = createUserAccount(authTokenUserSecond);
        final int secondUserAccountSecond = createUserAccount(authTokenUserSecond);

        nowTime = ZonedDateTime.now(ZoneOffset.UTC);

        final TransferResponse transferResponse = successfulTransferMoneyBetweenAccounts(authUserToken, userAccount,
                secondUserAccountSecond, depositTransferMoney);

        softly.assertThat(transferResponse.getMessage()).isEqualTo(ResponseMessages.TRANSFER_SUCCESSFUL.getValue());

        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(DEFAULT_ZERO_BALANCE);

        final AccountsDao firstUserAccountJDBC = DBSteps.getAccountByAccountIdJDBC(userAccount);

        softly.assertThat(firstUserAccountJDBC.getBalance()).isEqualTo(DEFAULT_ZERO_BALANCE);

        checkUserTransactions(authUserToken, userAccount, secondUserAccountSecond, nowTime, Operations.TRANSFER_OUT, depositTransferMoney);

        checkUserTransactionsDb(userAccount, secondUserAccountSecond, nowTime, Operations.TRANSFER_OUT.name(), depositTransferMoney);

        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccountSecond)).isEqualTo(depositTransferMoney);

        final AccountsDao secondUserSecondAccountJDBC = DBSteps.getAccountByAccountIdJDBC(secondUserAccountSecond);

        softly.assertThat(secondUserSecondAccountJDBC.getBalance()).isEqualTo(depositTransferMoney);

        checkUserTransactions(authTokenUserSecond, secondUserAccountSecond, userAccount, nowTime,
                Operations.TRANSFER_IN, depositTransferMoney);

        checkUserTransactionsDb(secondUserAccountSecond, userAccount, nowTime,
                Operations.TRANSFER_IN.name(), depositTransferMoney);

        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccountFirst)).isEqualTo(DEFAULT_ZERO_BALANCE);

        final AccountsDao secondUserFirstAccountJDBC = DBSteps.getAccountByAccountIdJDBC(secondUserAccountFirst);

        softly.assertThat(secondUserFirstAccountJDBC.getBalance()).isEqualTo(DEFAULT_ZERO_BALANCE);

    }

    @Test
    @DisplayName("Негативный тест: пользователь не может переводить деньги при нулевом балансе")
    public void userCannotTransferMoneyToSomeoneElseExistedAccountWhenHisAccountBalanceIsZero() throws SQLException {

        final Double transferMoney = RandomData.getMoney();

        final String authTokenUserSecond = createUserAndGetToken();

        final int secondUserAccount = createUserAccount(authTokenUserSecond);

        final String errorMessage = failedTransferMoneyBetweenAccounts(authUserToken, userAccount, secondUserAccount,
                transferMoney, ResponseSpecs.requestReturnsBadRequest());

        softly.assertThat(errorMessage).isEqualTo(INVALID_TRANSFER_INSUFFICIENT_FUNDS_OR_INVALID_ACCOUNTS.getValue());

        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(DEFAULT_ZERO_BALANCE);

        final AccountsDao UserAccountJDBC = DBSteps.getAccountByAccountIdJDBC(userAccount);

        softly.assertThat(UserAccountJDBC.getBalance()).isEqualTo(DEFAULT_ZERO_BALANCE);

        final List<UserTransactionsResponse> userFirstTransactions = getUserTransactions(authUserToken, userAccount);

        softly.assertThat(userFirstTransactions.isEmpty());

        final List<TransactionsDao> transactionInfoByFirstUserAccountJDBC = DBSteps.getTransactionInfoListByAccountIdJDBC(userAccount);

        softly.assertThat(transactionInfoByFirstUserAccountJDBC.isEmpty());


        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccount)).isEqualTo(DEFAULT_ZERO_BALANCE);

        final AccountsDao SecondUserAccountJDBC = DBSteps.getAccountByAccountIdJDBC(secondUserAccount);

        softly.assertThat(SecondUserAccountJDBC.getBalance()).isEqualTo(DEFAULT_ZERO_BALANCE);

        final List<UserTransactionsResponse> userSecondTransactions = getUserTransactions(authTokenUserSecond,
                secondUserAccount);

        softly.assertThat(userSecondTransactions.isEmpty());

        final List<TransactionsDao> transactionInfoBySecondUserAccountJDBC = DBSteps.getTransactionInfoListByAccountIdJDBC(secondUserAccount);

        softly.assertThat(transactionInfoBySecondUserAccountJDBC.isEmpty());

    }


    @Test
    @DisplayName("Негативный тест: пользователь не может переводить деньги с чужого аккаунта на свой")
    public void userCannotTransferMoneyFromSomeoneElseExistedAccountToHisOwn() throws SQLException {

        double moneyToDepositTransfer = RandomData.getMoney();

        final String authTokenUserSecond = createUserAndGetToken();

        final int secondUserAccount = createUserAccount(authTokenUserSecond);

        depositMoney(authTokenUserSecond, secondUserAccount, moneyToDepositTransfer);

        final String errorMessage = failedTransferMoneyBetweenAccounts(authUserToken, secondUserAccount,
                userAccount, moneyToDepositTransfer, ResponseSpecs.requestReturnsForbidden());

        softly.assertThat(errorMessage).isEqualTo(UNAUTHORIZED_ACCESS_TO_ACCOUNT.getValue());

        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(DEFAULT_ZERO_BALANCE);

        final AccountsDao firstUserAccountJDBC = DBSteps.getAccountByAccountIdJDBC(userAccount);

        softly.assertThat(firstUserAccountJDBC.getBalance()).isEqualTo(DEFAULT_ZERO_BALANCE);

        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccount)).isEqualTo(moneyToDepositTransfer);

        final AccountsDao secondUserAccountJDBC = DBSteps.getAccountByAccountIdJDBC(secondUserAccount);

        softly.assertThat(secondUserAccountJDBC.getBalance()).isEqualTo(moneyToDepositTransfer);

    }

    @Test
    @Bug(true)
    @DisplayName("Негативный тест: пользователь не может переводить деньги с/на один и тот же свой аккаунт")
    public void userCannotTransferMoneyFromToSameHisAccount() throws SQLException {

        double moneyToDepositTransfer = RandomData.getMoney();
        depositMoney(authUserToken, userAccount, moneyToDepositTransfer);

        final String actualErrorMessage = failedTransferMoneyBetweenAccounts(authUserToken, userAccount, userAccount,
                moneyToDepositTransfer, ResponseSpecs.requestReturnsBadRequest());

        softly.assertThat(actualErrorMessage).isEqualTo(OPERATION_IS_FORBIDDEN);

        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(moneyToDepositTransfer);

        final AccountsDao UserAccountJDBC = DBSteps.getAccountByAccountIdJDBC(userAccount);

        softly.assertThat(UserAccountJDBC.getBalance()).isEqualTo(moneyToDepositTransfer);

    }

    @Test
    @DisplayName("Негативный тест: пользователь не может переводить деньги на несуществующий аккаунт")
    public void userCannotTransferMoneyFromToNonExistedAccount() throws SQLException {

        double moneyToTransfer = RandomData.getMoney();

        nonExistingAccount = getMaxExistedAccountId() + 1;

        final String actualErrorMessage = failedTransferMoneyBetweenAccounts(authUserToken, userAccount, nonExistingAccount,
                moneyToTransfer, ResponseSpecs.requestReturnsBadRequest());

        softly.assertThat(actualErrorMessage).isEqualTo(INVALID_TRANSFER_INSUFFICIENT_FUNDS_OR_INVALID_ACCOUNTS.getValue());

        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(DEFAULT_ZERO_BALANCE);

        final AccountsDao UserAccountJDBC = DBSteps.getAccountByAccountIdJDBC(userAccount);

        softly.assertThat(UserAccountJDBC.getBalance()).isEqualTo(DEFAULT_ZERO_BALANCE);

        checkNegativeUserTransactions(authUserToken, userAccount, Operations.TRANSFER_IN);

        checkNegativeUserTransactionsDb(userAccount, Operations.TRANSFER_IN);

    }

    @Test
    @DisplayName("Негативный тест: пользователь не может переводить деньги с несуществующего аккаунта")
    public void userCannotTransferMoneyFromNonExistedAccount() throws SQLException {

        double moneyToTransfer = RandomData.getMoney();

        nonExistingAccount = getMaxExistedAccountId() + 1;

        final String actualErrorMessage = failedTransferMoneyBetweenAccounts(authUserToken, nonExistingAccount,
                userAccount, moneyToTransfer, ResponseSpecs.requestReturnsForbidden());

        softly.assertThat(actualErrorMessage).isEqualTo(UNAUTHORIZED_ACCESS_TO_ACCOUNT.getValue());

        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(DEFAULT_ZERO_BALANCE);

        final AccountsDao UserAccountJDBC = DBSteps.getAccountByAccountIdJDBC(userAccount);

        softly.assertThat(UserAccountJDBC.getBalance()).isEqualTo(DEFAULT_ZERO_BALANCE);

        checkNegativeUserTransactions(authUserToken, userAccount, Operations.TRANSFER_IN);

        checkNegativeUserTransactionsDb(userAccount, Operations.TRANSFER_IN);
    }
}
