package requests.steps.user_steps;

import config.Operations;
import config.ResponseMessages;
import io.restassured.common.mapper.TypeRef;
import io.restassured.specification.ResponseSpecification;
import lombok.Getter;
import lombok.Setter;
import models.*;
import models.comparison.ModelAssertions;
import org.assertj.core.api.SoftAssertions;
import requests.skelethon.EndpointRequests;
import requests.skelethon.requesters.CrudRequester;
import requests.skelethon.requesters.ValidatableCrudRequester;
import specs.RequestSpecs;
import specs.ResponseSpecs;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static config.AccountData.ACCOUNT_NUMBER_PREFIX;

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

    public static int createUserAccount(String userToken) {
        final UserAccountResponse userAccountResponse =
                new ValidatableCrudRequester<UserAccountResponse>(RequestSpecs.withToken(userToken),
                        EndpointRequests.CREATE_USER_ACCOUNT,
                        ResponseSpecs.entityWasCreated()).POST(null);
        return userAccountResponse.getId();

    }

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
        return userAccountResponse.stream().map(UserAccountResponse::getId).toList();

    }

    public static void deleteUserAccounts(String userToken) {
        getUserAccountIds(userToken).forEach(accountId -> {
            new CrudRequester(RequestSpecs.withToken(userToken),
                    EndpointRequests.DELETE_USER_ACCOUNT,
                    ResponseSpecs.requestReturnsOk()).DELETE(accountId);
        });
    }

    public static double getUserBalance(String userToken, int accountId) {
        final Optional<UserAccountResponse> foundAccount = getUserAccounts(userToken)
                .stream()
                .filter(accounts ->
                        accounts.getId() == accountId).findFirst();

        final Double raw = foundAccount.map(UserAccountResponse::getBalance).orElse(DEFAULT_ZERO_BALANCE);
        return BigDecimal.valueOf(raw).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public static List<UserTransactionsResponse> getUserTransactions(String userToken, int accountId) {
        final Optional<UserAccountResponse> foundAccount = getUserAccounts(userToken).stream()
                .filter(accounts -> accounts.getId() == accountId).findFirst();

        return foundAccount.map(UserAccountResponse::getTransactions).orElse(null);
    }

    public static void checkPositiveUserTransactions(String userToken, int fromAccountId, int toAccountId,
                                                     ZonedDateTime nowTime, Operations operation, double moneyToTransfer) {
        final List<UserTransactionsResponse> userFirstTransactions = getUserTransactions(userToken, fromAccountId);

        final UserTransactionsResponse userFirstTransactionsResponse = userFirstTransactions
                .stream().max(Comparator.comparingInt(UserTransactionsResponse::getId)).orElseThrow();

        softly.assertThat(userFirstTransactionsResponse.getId()).isGreaterThan(DEFAULT_ZERO_ACCOUNT_ID);
        softly.assertThat(userFirstTransactionsResponse.getAmount()).isEqualTo(moneyToTransfer);
        softly.assertThat(userFirstTransactionsResponse.getType()).isEqualTo(operation);
        softly.assertThat(userFirstTransactionsResponse.getTimestamp()).isBetween(nowTime.minusSeconds(PLUS_MINUS_SECONDS),
                nowTime.plusSeconds(PLUS_MINUS_SECONDS));
        softly.assertThat(userFirstTransactionsResponse.getRelatedAccountId()).isEqualTo(toAccountId);

    }

    public static void checkNegativeUserTransactions(String userToken, int accountId, Operations operation) {
        final List<UserTransactionsResponse> userTransactions = getUserTransactions(userToken, accountId);

        userTransactions.stream().max(Comparator.comparingInt(UserTransactionsResponse::getId))
                .ifPresent(userTransactionsResponse -> {
                    softly.assertThat(userTransactionsResponse.getType()).isNotEqualTo(operation);
                });
    }

    public static void depositMoney(String userToken, int accountId, double money) {
        DepositRequest depositRequest = new DepositRequest(accountId, money);

        final UserAccountResponse userAccountResponse =
                new ValidatableCrudRequester<UserAccountResponse>(RequestSpecs.withToken(userToken),
                        EndpointRequests.DEPOSIT_MONEY,
                        ResponseSpecs.requestReturnsOk()).POST(depositRequest);

        softly.assertThat(userAccountResponse.getId()).isEqualTo(depositRequest.getId());
        softly.assertThat(userAccountResponse.getAccountNumber())
                .isEqualTo(ACCOUNT_NUMBER_PREFIX.getValue() + depositRequest.getId());
        softly.assertThat(userAccountResponse.getBalance()).isEqualTo(depositRequest.getBalance());
        softly.assertThat(userAccountResponse.getTransactions()).isNotEmpty();
    }

    public static void depositMoneyWOCheckResponse(String userToken, int accountId, double money) {
        DepositRequest depositRequest = new DepositRequest(accountId, money);

        new ValidatableCrudRequester<UserAccountResponse>(RequestSpecs.withToken(userToken), EndpointRequests.DEPOSIT_MONEY,
                ResponseSpecs.requestReturnsOk()).POST(depositRequest);
    }

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


    public static String failedTransferMoneyBetweenAccounts(String userToken, int senderAccountId,
                                                            int receiverAccountId, double money,
                                                            ResponseSpecification responseSpec) {
        final TransferRequest transferRequest = TransferRequest.builder().senderAccountId(senderAccountId)
                .receiverAccountId(receiverAccountId)
                .amount(money).build();

        return new CrudRequester(RequestSpecs.withToken(userToken), EndpointRequests.TRANSFER_MONEY
                , responseSpec).POST(transferRequest).extract().response().asString();
    }

    public static UsersResponse getUserInfo(String userToken) {
        return new ValidatableCrudRequester<UsersResponse>(RequestSpecs.withToken(userToken),
                EndpointRequests.GET_USER_INFO, ResponseSpecs.requestReturnsOk()).GET();
    }

    public static void successfulChangeUserName(ChangeUserRequest changeUserRequest, String userToken) {
        final ChangeUserResponse changeUserResponse =
                new ValidatableCrudRequester<ChangeUserResponse>(RequestSpecs.withToken(userToken),
                        EndpointRequests.UPDATE_USER, ResponseSpecs.requestReturnsOk())
                        .PUT(changeUserRequest);

        softly.assertThat(changeUserResponse.getCustomer().getName()).isEqualTo(changeUserRequest.getName());
        softly.assertThat(changeUserResponse.getMessage())
                .isEqualTo(ResponseMessages.PROFILE_UPDATED_SUCCESSFULLY.getValue());
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
