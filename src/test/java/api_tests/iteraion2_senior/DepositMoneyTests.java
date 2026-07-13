package api_tests.iteraion2_senior;

import api.config.AccountData;
import api.config.Operations;
import api.config.ResponseMessages;
import api.dao.comparison_db.ModelAssertionsDb;
import api.dao.jdbc.AccountsDao;
import api.dao.jdbc.TransactionsDao;
import api.models.DepositRequest;
import api.models.UserAccountResponse;
import api.models.UserTransactionsResponse;
import api.models.comparison.ModelAssertions;
import api.requests.skelethon.EndpointRequests;
import api.requests.skelethon.requesters.CrudRequester;
import api.requests.skelethon.requesters.ValidatableCrudRequester;
import api.requests.steps.admin_steps.AdminSteps;
import api.requests.steps.db_steps.DBSteps;
import api.specs.RequestSpecs;
import api.specs.ResponseSpecs;
import api.utils.RandomData;
import common.annotations.ApiVersion;
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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static api.requests.steps.admin_steps.AdminSteps.createUserAndGetToken;
import static api.requests.steps.user_steps.UserSteps.*;

public class DepositMoneyTests extends BaseTestSenior {

    private ZonedDateTime nowTime;
    private final int minimumDefaultTransactionId = 0;

    private static Stream<Arguments> diffPositiveValue() {
        return Stream.of(
                Arguments.of(0.01, 0.01),
                Arguments.of(2500, 2500.0),
                Arguments.of(4999.99, 4999.99),
                Arguments.of(5000.00, 5000.00));
    }

    @MethodSource("diffPositiveValue")
    @ParameterizedTest
    @ApiVersion(version = "with_deletion")
    @DisplayName("Позитивный тест: пользователь пополняет свой аккаунт валидной суммой")
    public void userCanDepositHisAccountOld(double incomingMoney, Number expectedBalance) {
        final DepositRequest depositRequest = new DepositRequest(userAccount, incomingMoney);

        nowTime = ZonedDateTime.now(ZoneOffset.UTC);

        UserAccountResponse userAccountResponse =
                new ValidatableCrudRequester<UserAccountResponse>(RequestSpecs.withToken(authUserToken),
                        EndpointRequests.DEPOSIT_MONEY, ResponseSpecs.requestReturnsOk())
                        .POST(depositRequest);

        ModelAssertions.assertThatModels(depositRequest, userAccountResponse).match();

        softly.assertThat(userAccountResponse.getId()).isEqualTo(depositRequest.getId());
        softly.assertThat(userAccountResponse.getAccountNumber())
                .isEqualTo(AccountData.ACCOUNT_NUMBER_PREFIX.getValue() + depositRequest.getId());
        softly.assertThat(userAccountResponse.getBalance()).isEqualTo(depositRequest.getBalance());
        softly.assertThat(userAccountResponse.getTransactions()).isNotEmpty();

        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(expectedBalance);

        final List<UserTransactionsResponse> userTransactions = getUserTransactions(authUserToken, userAccount);

        userTransactions.forEach(transactions -> {
            softly.assertThat(transactions.getId()).isGreaterThan(minimumDefaultTransactionId);
            softly.assertThat(transactions.getAmount()).isEqualTo(depositRequest.getBalance());
            softly.assertThat(transactions.getType()).isEqualTo(Operations.DEPOSIT);
            softly.assertThat(transactions.getTimestamp()).isBetween(nowTime.minusSeconds(PLUS_MINUS_SECONDS).toLocalDateTime(),
                    nowTime.plusSeconds(PLUS_MINUS_SECONDS).toLocalDateTime());
            softly.assertThat(transactions.getRelatedAccountId()).isEqualTo(depositRequest.getId());
        });

    }

    @MethodSource("diffPositiveValue")
    @ParameterizedTest
    @ApiVersion(version = "with_database_with_fix")
    @DisplayName("Позитивный тест: пользователь пополняет свой аккаунт валидной суммой")
    public void userCanDepositHisAccount(double incomingMoney, Number expectedBalance) throws SQLException {

        final DepositRequest depositRequest = new DepositRequest(userAccount, incomingMoney);

        nowTime = ZonedDateTime.now(ZoneOffset.UTC);

        UserAccountResponse userAccountResponse =
                new ValidatableCrudRequester<UserAccountResponse>(RequestSpecs.withToken(authUserToken),
                        EndpointRequests.DEPOSIT_MONEY, ResponseSpecs.requestReturnsOk())
                        .POST(depositRequest);

        ModelAssertions.assertThatModels(depositRequest, userAccountResponse).match();

        final AccountsDao accountsDao = DBSteps.getAccountByAccountNumberJDBC(userAccountResponse.getAccountNumber());
        ModelAssertionsDb.assertThatModels(userAccountResponse, accountsDao).match();

        softly.assertThat(userAccountResponse.getId()).isEqualTo(depositRequest.getId());
        softly.assertThat(userAccountResponse.getAccountNumber())
                .startsWith(AccountData.ACCOUNT_NUMBER_PREFIX.getValue());
        softly.assertThat(userAccountResponse.getBalance()).isEqualTo(depositRequest.getBalance());
        softly.assertThat(userAccountResponse.getTransactions()).isNotEmpty();

        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(expectedBalance);

        final List<UserTransactionsResponse> userTransactions = getUserTransactions(authUserToken, userAccount);

        userTransactions.forEach(transactions -> {
            softly.assertThat(transactions.getId()).isGreaterThan(minimumDefaultTransactionId);
            softly.assertThat(transactions.getAmount()).isEqualTo(depositRequest.getBalance());
            softly.assertThat(transactions.getType()).isEqualTo(Operations.DEPOSIT);
            softly.assertThat(transactions.getTimestamp())
                    .isBetween(nowTime.minusSeconds(PLUS_MINUS_SECONDS).toLocalDateTime(),
                            nowTime.plusSeconds(PLUS_MINUS_SECONDS).toLocalDateTime());
            softly.assertThat(transactions.getRelatedAccountId()).isEqualTo(depositRequest.getId());
        });

        final TransactionsDao transactionDao = DBSteps.getTransactionInfoByAccountIdJDBC(userAccountResponse.getId());

        softly.assertThat(transactionDao.getId()).isGreaterThan(minimumDefaultTransactionId);
        softly.assertThat(transactionDao.getAmount()).isEqualTo(depositRequest.getBalance());
        softly.assertThat(transactionDao.getType()).isEqualTo(Operations.DEPOSIT.name());
        softly.assertThat(transactionDao.getTimestamp().toLocalDateTime()).isBetween(nowTime.minusSeconds(PLUS_MINUS_SECONDS).toLocalDateTime(),
                nowTime.plusSeconds(PLUS_MINUS_SECONDS).toLocalDateTime());
        softly.assertThat(transactionDao.getRelatedAccountId()).isEqualTo(depositRequest.getId());

    }

    @Test
    @DisplayName("Позитивный тест: пользователь может положить деньги на свой аккаунт несколько раз с общей суммой больше 5000")
    public void userCanDepositMoneyIntoHisAccountSeveralTimesWithCommonAmountMore5000() throws SQLException {

        final Double firstDepositValue = RandomData.getMoneyFromTo(4000, 5000);
        final Double secondDepositValue = RandomData.getMoneyFromTo(1001, 1002);
        BigDecimal totalExpectedBalance = BigDecimal.valueOf(firstDepositValue + secondDepositValue)
                .setScale(2, RoundingMode.HALF_UP);

        depositMoney(authUserToken, userAccount, firstDepositValue);

        final DepositRequest depositRequestSecond = new DepositRequest(userAccount, secondDepositValue);

        nowTime = ZonedDateTime.now(ZoneOffset.UTC);

        final UserAccountResponse userAccountResponse = new ValidatableCrudRequester<UserAccountResponse>
                (RequestSpecs.withToken(authUserToken), EndpointRequests.DEPOSIT_MONEY,
                        ResponseSpecs.requestReturnsOk()).POST(depositRequestSecond);

        ModelAssertions.assertThatModels(depositRequestSecond, userAccountResponse).match();

        final AccountsDao accountsDao = DBSteps.getAccountByAccountNumberJDBC(userAccountResponse.getAccountNumber());
        ModelAssertionsDb.assertThatModels(userAccountResponse, accountsDao).match();

        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(totalExpectedBalance.doubleValue());

        final List<UserTransactionsResponse> userTransactions = getUserTransactions(authUserToken, userAccount);

        userTransactions.forEach(transactions -> {
            softly.assertThat(transactions.getId()).isGreaterThan(minimumDefaultTransactionId);
            softly.assertThat(transactions.getType()).isEqualTo(Operations.DEPOSIT);
            softly.assertThat(transactions.getTimestamp())
                    .isBetween(nowTime.minusSeconds(PLUS_MINUS_SECONDS).toLocalDateTime(),
                            nowTime.plusSeconds(PLUS_MINUS_SECONDS).toLocalDateTime());
            softly.assertThat(transactions.getRelatedAccountId()).isEqualTo(depositRequestSecond.getId());
        });

        final List<TransactionsDao> transactionDao = DBSteps.getTransactionInfoListByAccountIdJDBC(userAccountResponse.getId());

        transactionDao.forEach(transactionsDao -> {
            softly.assertThat(transactionsDao.getId()).isGreaterThan(minimumDefaultTransactionId);
            softly.assertThat(transactionsDao.getType()).isEqualTo(Operations.DEPOSIT.name());
            softly.assertThat(transactionsDao.getTimestamp().toLocalDateTime())
                    .isBetween(nowTime.minusSeconds(PLUS_MINUS_SECONDS).toLocalDateTime(),
                            nowTime.plusSeconds(PLUS_MINUS_SECONDS).toLocalDateTime());
            softly.assertThat(transactionsDao.getRelatedAccountId()).isEqualTo(depositRequestSecond.getId());
        });

        final UserTransactionsResponse userTransactionsResponseFirst = userTransactions
                .stream().min(Comparator.comparingInt(UserTransactionsResponse::getId)).orElseThrow();

        softly.assertThat(userTransactionsResponseFirst.getAmount()).isEqualTo(firstDepositValue);


        final UserTransactionsResponse userTransactionsResponseSecond = userTransactions
                .stream().max(Comparator.comparingInt(UserTransactionsResponse::getId)).orElseThrow();

        softly.assertThat(userTransactionsResponseSecond.getAmount()).isEqualTo(secondDepositValue);

        final TransactionsDao transactionsDaoMinId = transactionDao
                .stream().min(Comparator.comparing(TransactionsDao::getId)).orElseThrow();

        softly.assertThat(transactionsDaoMinId.getAmount()).isEqualTo(firstDepositValue);

        final TransactionsDao transactionsDaoMaxId = transactionDao
                .stream().max(Comparator.comparing(TransactionsDao::getId)).orElseThrow();

        softly.assertThat(transactionsDaoMaxId.getAmount()).isEqualTo(secondDepositValue);

    }

    @Test
    @DisplayName("Позитивный тест: пользователь может положить деньги на свои любые аккаунты")
    public void userCanDepositMoneyIntoHisAccounts() throws SQLException {
        final Double firstDepositValue = RandomData.getMoney();
        final Double secondDepositValue = RandomData.getMoney();

        depositMoney(authUserToken, userAccount, firstDepositValue);

        final int userAccountSecond = createUserAccount(authUserToken);

        depositMoney(authUserToken, userAccountSecond, secondDepositValue);

        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(firstDepositValue);
        softly.assertThat(getUserBalance(authUserToken, userAccountSecond)).isEqualTo(secondDepositValue);

        final AccountsDao firstAccountByAccountIdJDBC = DBSteps.getAccountByAccountIdJDBC(userAccount);

        softly.assertThat(firstAccountByAccountIdJDBC.getBalance()).isEqualTo(firstDepositValue);

        final AccountsDao secondAccountByAccountIdJDBC = DBSteps.getAccountByAccountIdJDBC(userAccountSecond);

        softly.assertThat(secondAccountByAccountIdJDBC.getBalance()).isEqualTo(secondDepositValue);

    }

    private static Stream<Arguments> diffNegativeValueOld() {
        return Stream.of(
                Arguments.of(-0.01, 0.0, ResponseMessages.DEPOSIT_AMOUNT_MUST_BE_AT_LEAST_01_OLD.getValue()),
                Arguments.of(0.0, 0.0, ResponseMessages.DEPOSIT_AMOUNT_MUST_BE_AT_LEAST_01_OLD.getValue()));
    }

    @MethodSource("diffNegativeValueOld")
    @ParameterizedTest
    @ApiVersion(version = "with_deletion")
    @DisplayName("Негативный тест: пользователь не может пополнить свой аккаунт суммой меньше 0.01")
    public void userCannotDepositHisAccountMoneyLessThanMiniumLimitOld(Number incomingMoney, Number expectedBalance, String errorMessage) {

        final DepositRequest depositRequest = DepositRequest
                .builder().id(userAccount).balance(incomingMoney.doubleValue()).build();

        final String actualErrorMessage = new CrudRequester(RequestSpecs.withToken(authUserToken), EndpointRequests.DEPOSIT_MONEY
                , ResponseSpecs.requestReturnsBadRequest()).POST(depositRequest).extract().response().asString();

        softly.assertThat(actualErrorMessage).isEqualTo(errorMessage);

        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(expectedBalance);

        final List<UserTransactionsResponse> userTransactions = getUserTransactions(authUserToken, userAccount);

        softly.assertThat(userTransactions).isEmpty();
    }


    private static Stream<Arguments> diffNegativeValue() {
        return Stream.of(
                Arguments.of(-0.01, 0.0, ResponseMessages.INVALID_ACCOUNT_OR_AMOUNT.getValue()),
                Arguments.of(0.0, 0.0, ResponseMessages.INVALID_ACCOUNT_OR_AMOUNT.getValue()));
    }
    @MethodSource("diffNegativeValue")
    @ParameterizedTest
    @ApiVersion(version = "with_database_with_fix")
    @DisplayName("Негативный тест: пользователь не может пополнить свой аккаунт суммой меньше 0.01")
    public void userCannotDepositHisAccountMoneyLessThanMiniumLimit(Number incomingMoney, Number expectedBalance, String errorMessage) throws SQLException {

        final DepositRequest depositRequest = DepositRequest
                .builder().id(userAccount).balance(incomingMoney.doubleValue()).build();

        final String actualErrorMessage = new CrudRequester(RequestSpecs.withToken(authUserToken), EndpointRequests.DEPOSIT_MONEY
                , ResponseSpecs.requestReturnsBadRequest()).POST(depositRequest).extract().response().asString();

        softly.assertThat(actualErrorMessage).isEqualTo(errorMessage);

        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(expectedBalance);

        final AccountsDao accountByAccountIdJDBC = DBSteps.getAccountByAccountIdJDBC(userAccount);

        softly.assertThat(accountByAccountIdJDBC.getBalance()).isEqualTo(expectedBalance);

        final List<UserTransactionsResponse> userTransactions = getUserTransactions(authUserToken, userAccount);

        softly.assertThat(userTransactions).isEmpty();

        final List<TransactionsDao> transactionInfoListByAccountIdJDBC = DBSteps.getTransactionInfoListByAccountIdJDBC(userAccount);

        softly.assertThat(transactionInfoListByAccountIdJDBC).isEmpty();
    }

    @Test
    @ApiVersion(version = "with_deletion")
    @DisplayName("Негативный тест: пользователь не может пополнить свой аккаунт суммой больше 5000")
    public void userCannotDepositHisAccountMoneyMoreThanMaximumValue5000Old() {

        double depositMoney = 5000.01;

        final DepositRequest depositRequest = DepositRequest
                .builder().id(userAccount).balance(depositMoney).build();

        final String actualErrorMessage = new CrudRequester(RequestSpecs.withToken(authUserToken), EndpointRequests.DEPOSIT_MONEY
                , ResponseSpecs.requestReturnsBadRequest()).POST(depositRequest).extract().response().asString();

        softly.assertThat(actualErrorMessage).isEqualTo(ResponseMessages.DEPOSIT_AMOUNT_CANNOT_EXCEED_5000.getValue());

        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(DEFAULT_ZERO_BALANCE);

        final List<UserTransactionsResponse> userTransactions = getUserTransactions(authUserToken, userAccount);

        softly.assertThat(userTransactions).isEmpty();

    }

    @Test
    @ApiVersion(version = "with_database_with_fix")
    @DisplayName("Негативный тест: пользователь не может пополнить свой аккаунт суммой больше 5000")
    public void userCannotDepositHisAccountMoneyMoreThanMaximumValue5000() throws SQLException {

        double depositMoney = 5000.01;

        final DepositRequest depositRequest = DepositRequest
                .builder().id(userAccount).balance(depositMoney).build();

        final String actualErrorMessage = new CrudRequester(RequestSpecs.withToken(authUserToken), EndpointRequests.DEPOSIT_MONEY
                , ResponseSpecs.requestReturnsBadRequest()).POST(depositRequest).extract().response().asString();

        softly.assertThat(actualErrorMessage).isEqualTo(ResponseMessages.DEPOSIT_AMOUNT_CANNOT_EXCEED_5000.getValue());

        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(DEFAULT_ZERO_BALANCE);

        final AccountsDao accountByAccountIdJDBC = DBSteps.getAccountByAccountIdJDBC(userAccount);

        softly.assertThat(accountByAccountIdJDBC.getBalance()).isEqualTo(DEFAULT_ZERO_BALANCE);

        final List<UserTransactionsResponse> userTransactions = getUserTransactions(authUserToken, userAccount);

        softly.assertThat(userTransactions).isEmpty();

        final List<TransactionsDao> transactionInfoListByAccountIdJDBC = DBSteps.getTransactionInfoListByAccountIdJDBC(userAccount);

        softly.assertThat(transactionInfoListByAccountIdJDBC).isEmpty();

    }

    @Test
    @DisplayName("Негативный тест: пользователь не может положить деньги на чужой аккаунт")
    public void userCannotDepositMoneyIntoSomeElseAccount() throws SQLException {

        final Double depositMoney = RandomData.getMoney();
        final String authTokenUserSecond = createUserAndGetToken();
        final int secondUserAccount = createUserAccount(authTokenUserSecond);

        final DepositRequest depositRequest = DepositRequest.builder().id(secondUserAccount).balance(depositMoney).build();

        final String actualErrorMessage = new CrudRequester(RequestSpecs.withToken(authUserToken), EndpointRequests.DEPOSIT_MONEY
                , ResponseSpecs.requestReturnsForbidden()).POST(depositRequest).extract().response().asString();

        softly.assertThat(actualErrorMessage).isEqualTo(ResponseMessages.UNAUTHORIZED_ACCESS_TO_ACCOUNT.getValue());

        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(DEFAULT_ZERO_BALANCE);

        final AccountsDao accountByAccountIdJDBC = DBSteps.getAccountByAccountIdJDBC(userAccount);

        softly.assertThat(accountByAccountIdJDBC.getBalance()).isEqualTo(DEFAULT_ZERO_BALANCE);

        final List<UserTransactionsResponse> userTransactions = getUserTransactions(authUserToken, userAccount);

        softly.assertThat(userTransactions).isEmpty();

        final List<TransactionsDao> transactionInfoListByAccountIdJDBC = DBSteps.getTransactionInfoListByAccountIdJDBC(userAccount);

        softly.assertThat(transactionInfoListByAccountIdJDBC).isEmpty();

    }

    @Test
    @DisplayName("Негативный тест: пользователь при попытке положить деньги на несуществующий аккаунт не пополняет свой счёт")
    public void userCannotDepositIntoNonExistedAccount() throws SQLException {

        final Double depositMoney = RandomData.getMoney();

        final DepositRequest depositRequest = DepositRequest
                .builder().id(AdminSteps.getMaxExistedAccountId() + 1).balance(depositMoney).build();

        final String actualErrorMessage = new CrudRequester(RequestSpecs.withToken(authUserToken), EndpointRequests.DEPOSIT_MONEY
                , ResponseSpecs.requestReturnsForbidden()).POST(depositRequest).extract().response().asString();

        softly.assertThat(actualErrorMessage).isEqualTo(ResponseMessages.UNAUTHORIZED_ACCESS_TO_ACCOUNT.getValue());

        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(DEFAULT_ZERO_BALANCE);

        final AccountsDao accountByAccountIdJDBC = DBSteps.getAccountByAccountIdJDBC(userAccount);

        softly.assertThat(accountByAccountIdJDBC.getBalance()).isEqualTo(DEFAULT_ZERO_BALANCE);

        final List<UserTransactionsResponse> userTransactions = getUserTransactions(authUserToken, userAccount);

        softly.assertThat(userTransactions).isEmpty();

        final List<TransactionsDao> transactionInfoListByAccountIdJDBC = DBSteps.getTransactionInfoListByAccountIdJDBC(userAccount);

        softly.assertThat(transactionInfoListByAccountIdJDBC).isEmpty();

    }
}
