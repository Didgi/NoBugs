package api_tests.iteraion2_middle;

import config.Roles;
import io.restassured.common.mapper.TypeRef;
import io.restassured.specification.ResponseSpecification;
import models.*;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import requests.*;
import specs.RequestSpecs;
import specs.ResponseSpecs;
import utils.RandomData;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static config.ApiPath.AUTH_HEADER;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseTestMiddle {
    protected static String authUserToken;
    protected static int userAccount;
    protected static SoftAssertions softly;

    @BeforeEach
    public void setUp() {
        softly = new SoftAssertions();
        authUserToken = createUserAndGetToken();
        userAccount = createUserAccount(authUserToken);
        getUsersId();
    }

    @AfterEach
    public void cleanUp() {
        deleteUserAccounts(authUserToken);
        deleteUsersById();
        softly.assertAll();
    }

    protected static String createUserAndGetToken() {
        String username = RandomData.getUsername();
        String password = RandomData.getPassword();
        CreateUserRequest user = CreateUserRequest.builder()
                .username(username)
                .password(password)
                .role(Roles.USER.toString())
                .build();

        new CreateUserRequester(RequestSpecs.adminTokenSpec(), ResponseSpecs.entityWasCreated())
                .post(user);

        LoginRequest loginRequest = LoginRequest.builder()
                .username(username)
                .password(password)
                .build();

        return new LoginRequester(RequestSpecs.adminTokenSpec(), ResponseSpecs.requestReturnsOk())
                .post(loginRequest).extract().header(AUTH_HEADER);

    }

    protected static int createUserAccount(String userToken) {
        return new CreateUserAccountRequester(RequestSpecs.withTokenSpec(userToken), ResponseSpecs.entityWasCreated())
                .post(null).extract().as(UserAccountResponse.class).getId();
    }

    protected static List<Integer> getUsersId() {
        final List<UsersResponse> usersResponses = new GetUsersRequester(RequestSpecs.adminTokenSpec(), ResponseSpecs.requestReturnsOk())
                .get().assertThat().extract().as(new TypeRef<List<UsersResponse>>() {
                });
        return usersResponses.stream().map(UsersResponse::getId).toList();
    }

    protected static void deleteUsersById() {
        final List<Integer> usersId = getUsersId();
        usersId.forEach(id -> {
            new DeleteUsersRequester(RequestSpecs.adminTokenSpec(), ResponseSpecs.requestReturnsOk())
                    .delete(id);
        });

    }

    protected static List<UserAccountResponse> getUserAccounts(String userToken) {
        return new GetUserAccountsRequester(RequestSpecs.withTokenSpec(userToken), ResponseSpecs.requestReturnsOk())
                .get().assertThat().extract().as(new TypeRef<List<UserAccountResponse>>() {
                });

    }

    protected static List<Integer> getUserAccountIds(String userToken) {
        final List<UserAccountResponse> userAccountResponse = new GetUserAccountsRequester(RequestSpecs.withTokenSpec(userToken), ResponseSpecs.requestReturnsOk())
                .get().assertThat().extract().as(new TypeRef<List<UserAccountResponse>>() {
                });
        return userAccountResponse.stream().map(UserAccountResponse::getId).toList();

    }

    protected static void deleteUserAccounts(String userToken) {
        getUserAccountIds(userToken).forEach(accountId -> {
            new DeleteUserAccountsRequester(RequestSpecs.withTokenSpec(userToken), ResponseSpecs.requestReturnsOk())
                    .delete(accountId);
        });

    }

    protected static double getUserBalance(String userToken, int accountId) {
        final Optional<UserAccountResponse> foundAccount = getUserAccounts(userToken).stream().filter(accounts ->
                accounts.getId() == accountId).findFirst();

        final Double raw = foundAccount.map(UserAccountResponse::getBalance).orElse(0.0);
        return BigDecimal.valueOf(raw).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    protected static List<UserTransactionsResponse> getUserTransactions(String userToken, int accountId) {
        final Optional<UserAccountResponse> foundAccount = getUserAccounts(userToken).stream()
                .filter(accounts -> accounts.getId() == accountId).findFirst();

        return foundAccount.map(UserAccountResponse::getTransactions).orElse(null);
    }

    protected static void depositMoney(String userToken, int accountId, double money) {
        DepositRequest depositRequest = new DepositRequest(accountId, money);

        UserAccountResponse userAccountResponse = new DepositRequester(RequestSpecs.withTokenSpec(userToken), ResponseSpecs.requestReturnsOk())
                .post(depositRequest).extract().as(UserAccountResponse.class);

        softly.assertThat(userAccountResponse.getId()).isEqualTo(depositRequest.getId());
        softly.assertThat(userAccountResponse.getAccountNumber()).isEqualTo("ACC" + depositRequest.getId());
        softly.assertThat(userAccountResponse.getBalance()).isEqualTo(depositRequest.getBalance());
        softly.assertThat(userAccountResponse.getTransactions()).isNotEmpty();
    }

    protected static void depositMoneyWOCheckResponse(String userToken, int accountId, double money) {
        DepositRequest depositRequest = new DepositRequest(accountId, money);

        new DepositRequester(RequestSpecs.withTokenSpec(userToken), ResponseSpecs.requestReturnsOk())
                .post(depositRequest).extract().as(UserAccountResponse.class);
    }

    protected static TransferResponse successfulTransferMoneyBetweenAccounts(String userToken, int senderAccountId, int receiverAccountId, double money) {
        final TransferRequest transferRequest = TransferRequest.builder().senderAccountId(senderAccountId)
                .receiverAccountId(receiverAccountId)
                .amount(money).build();

        return new TransferRequester(RequestSpecs.withTokenSpec(userToken),
                ResponseSpecs.requestReturnsOk())
                .post(transferRequest).extract().as(TransferResponse.class);
    }

    protected static String failedTransferMoneyBetweenAccounts(String userToken, int senderAccountId,
                                                               int receiverAccountId, double money, ResponseSpecification responseSpec) {
        final TransferRequest transferRequest = TransferRequest.builder().senderAccountId(senderAccountId)
                .receiverAccountId(receiverAccountId)
                .amount(money).build();

        return new TransferRequester(RequestSpecs.withTokenSpec(userToken), responseSpec)
                .post(transferRequest).extract().response().asString();
    }

    protected int getMaxExistedAccountId() {
        final List<UsersResponse> usersResponses = new GetUsersRequester(RequestSpecs.adminTokenSpec(),
                ResponseSpecs.requestReturnsOk())
                .get().assertThat().extract().as(new TypeRef<List<UsersResponse>>() {
                });

        final Optional<UserAccountResponse> maxExistedAccountIdOptional = usersResponses
                .stream().flatMap(usersResponse -> usersResponse.getAccounts().stream())
                .max(Comparator.comparingInt(UserAccountResponse::getId));

        assertTrue(maxExistedAccountIdOptional.isPresent());
        System.out.println("Получен максимальный id: " + maxExistedAccountIdOptional.get().getId());
        return maxExistedAccountIdOptional.get().getId();
    }

    protected UsersResponse getUserInfo() {
        return new GetUsersRequester(RequestSpecs.withTokenSpec(authUserToken), ResponseSpecs.requestReturnsOk())
                .getUserInfo().extract().as(UsersResponse.class);
    }

    protected static ChangeUserResponse successfulChangeUserName(String updatedUserName, String userToken ){
        final ChangeUserRequest changeUserRequest = ChangeUserRequest.builder().name(updatedUserName).build();
        return new ChangeUserRequester(RequestSpecs.withTokenSpec(userToken), ResponseSpecs.requestReturnsOk())
                        .put(changeUserRequest).extract().as(ChangeUserResponse.class);
    }

    protected static String failedChangeUserName(String updatedUserName, String userToken, ResponseSpecification responseSpecs){
        final ChangeUserRequest changeUserRequest = ChangeUserRequest.builder().name(updatedUserName).build();
//        return new ChangeUserRequester(RequestSpecs.withTokenSpec(userToken), ResponseSpecs.requestReturnsBadRequest())
        return new ChangeUserRequester(RequestSpecs.withTokenSpec(userToken), responseSpecs)
                .put(changeUserRequest).extract().body().asString();
    }
}
