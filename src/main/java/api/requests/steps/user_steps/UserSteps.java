package api.requests.steps.user_steps;

import api.config.AccountData;
import api.config.Operations;
import api.config.TransactionStatus;
import api.dao.jdbc.TransactionsDao;
import api.models.*;
import api.models.comparison.ModelAssertions;
import api.requests.skelethon.EndpointRequests;
import api.requests.skelethon.requesters.CrudRequester;
import api.requests.skelethon.requesters.ValidatableCrudRequester;
import api.requests.steps.db_steps.DBSteps;
import api.specs.RequestSpecs;
import api.specs.ResponseSpecs;
import common.SessionStorage;
import io.qameta.allure.Step;
import io.restassured.common.mapper.TypeRef;
import io.restassured.specification.ResponseSpecification;
import lombok.Getter;
import lombok.Setter;
import org.assertj.core.api.SoftAssertions;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static api.config.ResponseMessages.TRANSFER_COMPLETE;

@Setter
@Getter
public class UserSteps {
    public static SoftAssertions softly;
    public static final int DEFAULT_ZERO_ACCOUNT_ID = 0;
    public static final int PLUS_MINUS_SECONDS = 30;
    public static final double DEFAULT_ZERO_BALANCE = 0.0;

    public static void SoftAssertions(SoftAssertions softAssertions) {
        softly = softAssertions;
    }

    @Step("Создаём пользователю аккаунт")
    public static int createUserAccount(String userToken) {
        final CreateUserAccountResponse userAccountResponse =
                new ValidatableCrudRequester<CreateUserAccountResponse>(RequestSpecs.withToken(userToken),
                        EndpointRequests.CREATE_USER_ACCOUNT,
                        ResponseSpecs.entityWasCreated()).POST(null);

        final List<UserAccountResponse> userAccounts = getUserAccounts(userToken);
        SessionStorage.replaceUserInfoInStorage(userToken, null, userAccounts);
        return userAccountResponse.getId();


    }

    @Step("Получаем все аккаунты пользователя")
    public static List<UserAccountResponse> getUserAccounts(String userToken) {
        return new CrudRequester(RequestSpecs.withToken(userToken), EndpointRequests.GET_USER_ACCOUNTS,
                ResponseSpecs.requestReturnsOk())
                .GET().assertThat().extract().as(new TypeRef<List<UserAccountResponse>>() {
                });
    }

    public static List<Integer> getUserAccountIds(String userToken) {
        final List<UserAccountResponse> userAccountResponse = new CrudRequester(RequestSpecs.withToken(userToken),
                EndpointRequests.GET_USER_ACCOUNTS,
                ResponseSpecs.requestReturnsOk()).GET().assertThat().extract()
                .as(new TypeRef<List<UserAccountResponse>>() {
                });
        return null;

    }

    @Step("Получаем баланс пользователя по аккаунту: {accountId}")
    public static double getUserBalance(String userToken, int accountId) {
        final Optional<UserAccountResponse> foundAccount = getUserAccounts(userToken)
                .stream()
                .filter(accounts ->
                        accounts.getId() == accountId).findFirst();
        final Double raw = foundAccount.map(UserAccountResponse::getBalance).orElse(DEFAULT_ZERO_BALANCE);
        return BigDecimal.valueOf(raw).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public static List<UserTransactionsResponse> getUserTransactionsOld(String userToken, int accountId) {
        final Optional<UserAccountResponse> foundAccount = getUserAccounts(userToken).stream()
                .filter(accounts -> accounts.getId() == accountId).findFirst();
        return null;
    }

    public static UserTransactionsResponse getUserTransactions(String userToken, int accountId, Operations operation) {
        final List<UserTransactionsResponse> userTransactionsResponses =
                new CrudRequester(RequestSpecs.withToken(userToken), EndpointRequests.GET_USER_TRANSACTIONS,
                        ResponseSpecs.requestReturnsOk())
                        .GET(accountId).assertThat().extract().as(new TypeRef<List<UserTransactionsResponse>>() {
                        });

        return userTransactionsResponses.stream().filter(userTransactionsResponse ->
                userTransactionsResponse.getType().equals(operation)).findFirst().orElse(null);
    }

    @Step("Получаем список транзакций аккаунта: {accountId}")
    public static List<UserTransactionsResponse> getUserTransactions(String userToken, int accountId) {
        return new CrudRequester(RequestSpecs.withToken(userToken),
                EndpointRequests.GET_USER_TRANSACTIONS,
                ResponseSpecs.requestReturnsOk())
                .GET(accountId).assertThat().extract().as(new TypeRef<List<UserTransactionsResponse>>() {
                });
    }

    @Step("Проверяем детали выполненной транзакции с аккаунта {fromAccountId} на аккаунт " +
            " {toAccountId} с типом операции {operation} через api")
    public static void checkUserTransactions(String userToken, int fromAccountId, int toAccountId,
                                             ZonedDateTime nowTime, Operations operation, double moneyToTransfer) {
        final List<UserTransactionsResponse> userFirstTransactions = getUserTransactions(userToken, fromAccountId);

        final UserTransactionsResponse userFirstTransactionsResponse = userFirstTransactions
                .stream().max(Comparator.comparingInt(UserTransactionsResponse::getId)).orElseThrow();

        softly.assertThat(userFirstTransactionsResponse.getId()).isGreaterThan(DEFAULT_ZERO_ACCOUNT_ID);
        softly.assertThat(userFirstTransactionsResponse.getAmount()).isEqualTo(moneyToTransfer);
        softly.assertThat(userFirstTransactionsResponse.getType()).isEqualTo(operation);
        softly.assertThat(userFirstTransactionsResponse.getTimestamp())
                .isBetween(nowTime.minusSeconds(PLUS_MINUS_SECONDS).toLocalDateTime(),
                        nowTime.plusSeconds(PLUS_MINUS_SECONDS).toLocalDateTime());
        softly.assertThat(userFirstTransactionsResponse.getRelatedAccountId()).isEqualTo(toAccountId);
    }

    @Step("Проверяем детали выполненной транзакции с аккаунта {fromAccountId} на аккаунт " +
            " {toAccountId} с типом операции {operation} с проверкой на мошенничество через api")
    public static void checkUserTransactions(String userToken, int fromAccountId, int toAccountId,
                                             ZonedDateTime nowTime, Operations operation, double moneyToTransfer,
                                             TransactionStatus status, boolean fraudCheckRequired) {
        final List<UserTransactionsResponse> userFirstTransactions = getUserTransactions(userToken, fromAccountId);

        final UserTransactionsResponse userFirstTransactionsResponse = userFirstTransactions
                .stream().max(Comparator.comparingInt(UserTransactionsResponse::getId)).orElseThrow();

        softly.assertThat(userFirstTransactionsResponse.getId()).isGreaterThan(DEFAULT_ZERO_ACCOUNT_ID);
        softly.assertThat(userFirstTransactionsResponse.getAmount()).isEqualTo(moneyToTransfer);
        softly.assertThat(userFirstTransactionsResponse.getType()).isEqualTo(operation);
        softly.assertThat(userFirstTransactionsResponse.getTimestamp())
                .isBetween(nowTime.minusSeconds(PLUS_MINUS_SECONDS).toLocalDateTime(),
                        nowTime.plusSeconds(PLUS_MINUS_SECONDS).toLocalDateTime());
        softly.assertThat(userFirstTransactionsResponse.getRelatedAccountId()).isEqualTo(toAccountId);
        softly.assertThat(userFirstTransactionsResponse.getStatus()).isEqualTo(status);
        softly.assertThat(userFirstTransactionsResponse.isFraudCheckRequired()).isEqualTo(fraudCheckRequired);
    }


    @Step("Проверяем детали выполненной транзакции с аккаунта {fromAccountId} на аккаунт " +
            " {toAccountId} с типом операции {operation} через БД")
    public static void checkUserTransactionsDb(int fromAccountId, int toAccountId,
                                               ZonedDateTime nowTime, String operation, double moneyToTransfer) throws SQLException {

        final TransactionsDao transactionsDao = DBSteps.getTransactionInfoListByAccountIdJDBC(fromAccountId)
                .stream().max(Comparator.comparing(TransactionsDao::getId)).orElseThrow();

        softly.assertThat(transactionsDao.getId()).isGreaterThan(DEFAULT_ZERO_ACCOUNT_ID);
        softly.assertThat(transactionsDao.getAmount()).isEqualTo(moneyToTransfer);
        softly.assertThat(transactionsDao.getType()).isEqualTo(operation);
        softly.assertThat(transactionsDao.getTimestamp().toLocalDateTime())
                .isBetween(nowTime.minusSeconds(PLUS_MINUS_SECONDS).toLocalDateTime(),
                        nowTime.plusSeconds(PLUS_MINUS_SECONDS).toLocalDateTime());
        softly.assertThat(transactionsDao.getRelatedAccountId()).isEqualTo(toAccountId);
    }

    @Step("Проверяем детали выполненной транзакции с аккаунта {fromAccountId} на аккаунт " +
            " {toAccountId} с типом операции {operation} с проверкой на мошенничество через БД")
    public static void checkUserTransactionsDb(int fromAccountId, int toAccountId,
                                               ZonedDateTime nowTime, String operation, double moneyToTransfer,
                                               TransactionStatus status, boolean fraudCheckRequired) throws SQLException {

        final TransactionsDao transactionsDao = DBSteps.getTransactionInfoListByAccountIdJDBC(fromAccountId)
                .stream().max(Comparator.comparing(TransactionsDao::getId)).orElseThrow();

        softly.assertThat(transactionsDao.getId()).isGreaterThan(DEFAULT_ZERO_ACCOUNT_ID);
        softly.assertThat(transactionsDao.getAmount()).isEqualTo(moneyToTransfer);
        softly.assertThat(transactionsDao.getType()).isEqualTo(operation);
        softly.assertThat(transactionsDao.getTimestamp().toLocalDateTime())
                .isBetween(nowTime.minusSeconds(PLUS_MINUS_SECONDS).toLocalDateTime(),
                        nowTime.plusSeconds(PLUS_MINUS_SECONDS).toLocalDateTime());
        softly.assertThat(transactionsDao.getRelatedAccountId()).isEqualTo(toAccountId);
        softly.assertThat(transactionsDao.getStatus()).isEqualTo(status);
        softly.assertThat(transactionsDao.isFraudCheckRequired()).isEqualTo(fraudCheckRequired);
    }

    @Step("Проверяем через api, что по аккаунту: {accountId} отсутствует транзакция с типом {operation}")
    public static void checkNegativeUserTransactions(String userToken, int accountId, Operations operation) {
        final List<UserTransactionsResponse> userTransactions = getUserTransactions(userToken, accountId);
        userTransactions.stream().max(Comparator.comparingInt(UserTransactionsResponse::getId))
                .ifPresent(userTransactionsResponse -> {
                    softly.assertThat(userTransactionsResponse.getType()).isNotEqualTo(operation);
                });
    }

    @Step("Проверяем через БД, что по аккаунту: {accountId} отсутствует транзакция с типом {operation}")
    public static void checkNegativeUserTransactionsDb(int accountId, Operations operation) throws SQLException {
        DBSteps.getTransactionInfoListByAccountIdJDBC(accountId)
                .stream()
                .max(Comparator.comparing(TransactionsDao::getId))
                .ifPresent(transactionsDao -> {
                    softly.assertThat(transactionsDao.getType()).isNotEqualTo(operation);
                });
    }

    @Step("Пополняем аккаунт {accountId} на сумму {money} с проверкой деталей ответа")
    public static void depositMoney(String userToken, int accountId, double money) {
        DepositRequest depositRequest = new DepositRequest(accountId, money);

        DepositResponse userAccountResponse =
                new ValidatableCrudRequester<DepositResponse>(RequestSpecs.withToken(userToken),
                        EndpointRequests.DEPOSIT_MONEY, ResponseSpecs.requestReturnsOk())
                        .POST(depositRequest);

        softly.assertThat(userAccountResponse.getId()).isEqualTo(depositRequest.getAccountId());
        softly.assertThat(userAccountResponse.getAccountNumber())
                .startsWith(AccountData.ACCOUNT_NUMBER_PREFIX.getValue());
        softly.assertThat(userAccountResponse.getDepositAmount()).isEqualTo(depositRequest.getAmount());
    }

    @Step("Пополняем аккаунт {accountId} на сумму {money} без проверки деталей ответа")
    public static void depositMoneyWOCheckResponse(String userToken, int accountId, double money) {
        DepositRequest depositRequest = new DepositRequest(accountId, money);
        new ValidatableCrudRequester<DepositResponse>(RequestSpecs.withToken(userToken), EndpointRequests.DEPOSIT_MONEY,
                ResponseSpecs.requestReturnsOk()).POST(depositRequest);
    }

    @Step("Выполняем перевод денег с аккаунта {senderAccountId} на аккаунт {receiverAccountId} на сумму {money}")
    public static TransferResponse successfulTransferMoneyBetweenAccounts(String userToken, int senderAccountId,
                                                                          int receiverAccountId, double money) {
        final TransferRequest transferRequest = TransferRequest.builder().senderAccountId(senderAccountId)
                .receiverAccountId(receiverAccountId)
                .amount(money).build();

        final TransferResponse transferResponse =
                new ValidatableCrudRequester<TransferResponse>(RequestSpecs.withToken(userToken)
                        , EndpointRequests.TRANSFER_MONEY, ResponseSpecs.requestReturnsOk())
                        .POST(transferRequest);

        ModelAssertions.assertThatModels(transferRequest, transferResponse).match();
        return transferResponse;
    }

    @Step("Выполняем перевод денег с аккаунта {senderAccountId} на аккаунт {receiverAccountId} на сумму {money} " +
            "с проверкой на мошенничество")
    public static TransferFraudCheckResponse successfulTransferMoneyBetweenAccountsWithFraudCheck(String userToken, int senderAccountId,
                                                                                                  int receiverAccountId, double money) {
        final TransferRequest transferRequest = TransferRequest.builder().senderAccountId(senderAccountId)
                .receiverAccountId(receiverAccountId)
                .amount(money).build();

        final TransferFraudCheckResponse transferResponse =
                new ValidatableCrudRequester<TransferFraudCheckResponse>(RequestSpecs.withToken(userToken)
                        , EndpointRequests.TRANSFER_MONEY_FRAUD_CHECK, ResponseSpecs.requestReturnsOk())
                        .POST(transferRequest);

        ModelAssertions.assertThatModels(transferRequest, transferResponse).match();

        return transferResponse;
    }

    @Step("Проверяем ошибки при невалидном переводе с аккаунта {senderAccountId} на аккаунт " +
            "{receiverAccountId} на сумму {money}")
    public static String failedTransferMoneyBetweenAccounts(String userToken, int senderAccountId,
                                                            int receiverAccountId, double money,
                                                            ResponseSpecification responseSpec) {
        final TransferRequest transferRequest = TransferRequest.builder().senderAccountId(senderAccountId)
                .receiverAccountId(receiverAccountId)
                .amount(money).build();

        return new ValidatableCrudRequester<TransferErrorResponse>(RequestSpecs.withToken(userToken), EndpointRequests.TRANSFER_MONEY_ERROR
                , responseSpec).POST(transferRequest).getMessage();
    }

    @Step("Получаем информацию о пользователе")
    public static UserProfileResponse getUserInfo(String userToken) {
        return new ValidatableCrudRequester<UserProfileResponse>(RequestSpecs.withToken(userToken),
                EndpointRequests.GET_USER_INFO, ResponseSpecs.requestReturnsOk()).GET();
    }

    @Step("Изменяем имя пользователя {changeUserRequest}")
    public static void successfulChangeUserName(ChangeUserRequest changeUserRequest, String userToken) {
        final ChangeUserResponse changeUserResponse =
                new ValidatableCrudRequester<ChangeUserResponse>(RequestSpecs.withToken(userToken),
                        EndpointRequests.UPDATE_USER, ResponseSpecs.requestReturnsOk())
                        .PUT(changeUserRequest);

        softly.assertThat(changeUserResponse.getName()).isEqualTo(changeUserRequest.getName());
    }

    @Step("Подтверждаем операцию перевода для транзакции {transactionId} при проверке на мошенничество")
    public static void transferComplete(String userToken, int transactionId) {
        TransferCompleteResponse transferCompleteResponse = new CrudRequester(RequestSpecs.withToken(userToken), EndpointRequests.TRANSFER_MONEY_COMPLETE,
                ResponseSpecs.requestReturnsOk())
                .POST(transactionId).assertThat().extract().as(TransferCompleteResponse.class);

        softly.assertThat(transferCompleteResponse.getMessage()).isEqualTo(TRANSFER_COMPLETE.getValue());
        softly.assertThat(transferCompleteResponse.getTransactionId()).isEqualTo(transactionId);
    }

    public static String failedChangeUserName(String updatedUserName, String userToken,
                                              ResponseSpecification responseSpecs) {
        final ChangeUserRequest changeUserRequest = ChangeUserRequest.builder().name(updatedUserName).build();
        return new CrudRequester(RequestSpecs.withToken(userToken), EndpointRequests.UPDATE_USER, responseSpecs)
                .PUT(changeUserRequest).extract().response().asString();
    }

    public static void repeatAction(int times, Runnable runnable) {
        for (int i = 0; i < times; i++) {
            runnable.run();
        }
    }
}
