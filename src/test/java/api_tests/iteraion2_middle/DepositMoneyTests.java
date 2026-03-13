package api_tests.iteraion2_middle;

import config.AccountData;
import config.Operations;
import config.ResponseMessages;
import models.DepositRequest;
import models.UserAccountResponse;
import models.UserTransactionsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import requests.DepositRequester;
import specs.RequestSpecs;
import specs.ResponseSpecs;
import utils.RandomData;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class DepositMoneyTests extends BaseTestMiddle {

    private static Stream<Arguments> diffPositiveValue() {
        return Stream.of(
                Arguments.of(0.01, 0.01),
                Arguments.of(2500, 2500.0),
                Arguments.of(4999.99, 4999.99),
                Arguments.of(5000.00, 5000.00));
    }

    @MethodSource("diffPositiveValue")
    @ParameterizedTest
    @DisplayName("Позитивный тест: пользователь пополняет свой аккаунт валидной суммой")
    public void userCanDepositHisAccount(double incomingMoney, Number expectedBalance) {

        final DepositRequest depositRequest = new DepositRequest(userAccount, incomingMoney);

        //сохраняем время для проверки времени пополнения
        ZonedDateTime nowTime = ZonedDateTime.now(ZoneOffset.UTC);

        //выполняем пополнение аккаунта
        final UserAccountResponse userAccountResponse =
                new DepositRequester(RequestSpecs.withTokenSpec(authUserToken), ResponseSpecs.requestReturnsOk())
                        .post(depositRequest).extract().as(UserAccountResponse.class);


        //проверяем ответ на запрос пополнения
        softly.assertThat(userAccountResponse.getId()).isEqualTo(depositRequest.getId());
        softly.assertThat(userAccountResponse.getAccountNumber())
                .isEqualTo(AccountData.ACCOUNT_NUMBER_PREFIX.getValue() + depositRequest.getId());
        softly.assertThat(userAccountResponse.getBalance()).isEqualTo(depositRequest.getBalance());
        softly.assertThat(userAccountResponse.getTransactions()).isNotEmpty();

        //проверяем баланс аккаунта пользователя
        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(expectedBalance);

        //проверяем транзакции пользователя
        final List<UserTransactionsResponse> userTransactions = getUserTransactions(authUserToken, userAccount);

        userTransactions.forEach(transactions -> {
            softly.assertThat(transactions.getId()).isGreaterThan(0);
            softly.assertThat(transactions.getAmount()).isEqualTo(depositRequest.getBalance());
            softly.assertThat(transactions.getType()).isEqualTo(Operations.DEPOSIT);
            softly.assertThat(transactions.getTimestamp()).isBetween(nowTime.minusSeconds(30), nowTime.plusSeconds(30));
            softly.assertThat(transactions.getRelatedAccountId()).isEqualTo(depositRequest.getId());
        });

    }

    @Test
    @DisplayName("Позитивный тест: пользователь может положить деньги на свой аккаунт несколько раз с общей суммой больше 5000")
    public void userCanDepositMoneyIntoHisAccountSeveralTimesWithCommonAmountMore5000() {

        final Double firstDepositValue = RandomData.getMoneyFromTo(4000, 5000);
        final Double secondDepositValue = RandomData.getMoneyFromTo(1001, 1002);
        BigDecimal totalExpectedBalance = BigDecimal.valueOf(firstDepositValue + secondDepositValue)
                .setScale(2, RoundingMode.HALF_UP);

        //пополняем аккаунт пользователя
        depositMoney(authUserToken, userAccount, firstDepositValue);

        final DepositRequest depositRequestSecond = new DepositRequest(userAccount, secondDepositValue);

        //сохраняем время для проверки времени пополнения
        ZonedDateTime nowTime = ZonedDateTime.now(ZoneOffset.UTC);

        //выполняем пополнение аккаунта
        new DepositRequester(RequestSpecs.withTokenSpec(authUserToken), ResponseSpecs.requestReturnsOk())
                .post(depositRequestSecond).extract().as(UserAccountResponse.class);


        //проверяем баланс аккаунта пользователя
        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(totalExpectedBalance.doubleValue());

        //проверяем транзакции пользователя
        final List<UserTransactionsResponse> userTransactions = getUserTransactions(authUserToken, userAccount);

        userTransactions.forEach(transactions -> {
            softly.assertThat(transactions.getId()).isGreaterThan(0);
            softly.assertThat(transactions.getType()).isEqualTo(Operations.DEPOSIT);
            softly.assertThat(transactions.getTimestamp()).isBetween(nowTime.minusSeconds(30), nowTime.plusSeconds(30));
            softly.assertThat(transactions.getRelatedAccountId()).isEqualTo(depositRequestSecond.getId());
        });

        final UserTransactionsResponse userTransactionsResponseFirst = userTransactions
                .stream().min(Comparator.comparingInt(UserTransactionsResponse::getId)).orElseThrow();

        softly.assertThat(userTransactionsResponseFirst.getAmount()).isEqualTo(firstDepositValue);

        final UserTransactionsResponse userTransactionsResponseSecond = userTransactions
                .stream().max(Comparator.comparingInt(UserTransactionsResponse::getId)).orElseThrow();

        softly.assertThat(userTransactionsResponseSecond.getAmount()).isEqualTo(secondDepositValue);

    }

    @Test
    @DisplayName("Позитивный тест: пользователь может положить деньги на свои любые аккаунты")
    public void userCanDepositMoneyIntoHisAccounts() {
        final Double firstDepositValue = RandomData.getMoney();
        final Double secondDepositValue = RandomData.getMoney();

        //пополняем первый аккаунт пользователя
        depositMoney(authUserToken, userAccount, firstDepositValue);

        //пользователя второй аккаунт у пользователя
        final int userAccountSecond = createUserAccount(authUserToken);

        //пополняем второй аккаунт пользователя
        depositMoney(authUserToken, userAccountSecond, secondDepositValue);

        //проверяем баланс обоих аккаунтов пользователя
        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(firstDepositValue);
        softly.assertThat(getUserBalance(authUserToken, userAccountSecond)).isEqualTo(secondDepositValue);

    }

    private static Stream<Arguments> diffNegativeValue() {
        return Stream.of(
                Arguments.of(-0.01, 0.0, ResponseMessages.DEPOSIT_AMOUNT_MUST_BE_AT_LEAST_01.getValue()),
                Arguments.of(0.0, 0.0, ResponseMessages.DEPOSIT_AMOUNT_MUST_BE_AT_LEAST_01.getValue()));
    }

    @MethodSource("diffNegativeValue")
    @ParameterizedTest
    @DisplayName("Негативный тест: пользователь не может пополнить свой аккаунт суммой меньше 0.01")
    public void userCannotDepositHisAccountMoneyLessThanMiniumLimit(Number incomingMoney, Number expectedBalance, String errorMessage) {

        final DepositRequest depositRequest = DepositRequest
                .builder().id(userAccount).balance(incomingMoney.doubleValue()).build();

        //выполняем пополнение аккаунта невалидной суммой и сохраняем сообщение об ошибке
        final String actualErrorMessage =
                new DepositRequester(RequestSpecs.withTokenSpec(authUserToken), ResponseSpecs.requestReturnsBadRequest())
                        .post(depositRequest).extract().response().asString();

        //проверяем полученную ошибку с ожидаемой
        softly.assertThat(actualErrorMessage).isEqualTo(errorMessage);

        //проверяем баланс аккаунта пользователя
        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(expectedBalance);

        //проверяем транзакции пользователя
        final List<UserTransactionsResponse> userTransactions = getUserTransactions(authUserToken, userAccount);

        softly.assertThat(userTransactions).isEmpty();
    }

    @Test
    @DisplayName("Негативный тест: пользователь не может пополнить свой аккаунт суммой больше 5000")
    public void userCannotDepositHisAccountMoneyMoreThanMaximumValue5000() {

        double depositMoney = 5000.01;
        double expectedBalance = 0.0;

        final DepositRequest depositRequest = DepositRequest
                .builder().id(userAccount).balance(depositMoney).build();

        //выполняем пополнение аккаунта невалидной суммой и сохраняем сообщение об ошибке
        final String actualErrorMessage =
                new DepositRequester(RequestSpecs.withTokenSpec(authUserToken), ResponseSpecs.requestReturnsBadRequest())
                        .post(depositRequest).extract().response().asString();

        //проверяем полученную ошибку с ожидаемой
        softly.assertThat(actualErrorMessage).isEqualTo(ResponseMessages.DEPOSIT_AMOUNT_CANNOT_EXCEED_5000.getValue());

        //проверяем баланс аккаунта пользователя
        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(expectedBalance);

        //проверяем транзакции пользователя
        final List<UserTransactionsResponse> userTransactions = getUserTransactions(authUserToken, userAccount);

        softly.assertThat(userTransactions).isEmpty();

    }

    @Test
    @DisplayName("Негативный тест: пользователь не может положить деньги на чужой аккаунт")
    public void userCannotDepositMoneyIntoSomeElseAccount() {

        double expectedBalance = 0.0;
        final Double depositMoney = RandomData.getMoney();
        final String authTokenUserSecond = createUserAndGetToken();
        final int secondUserAccount = createUserAccount(authTokenUserSecond);

        final DepositRequest depositRequest = DepositRequest.builder().id(secondUserAccount).balance(depositMoney).build();

        //выполняем пополнение чужого аккаунта и сохраняем сообщение об ошибке
        final String actualErrorMessage = new DepositRequester(RequestSpecs.withTokenSpec(authUserToken),
                ResponseSpecs.requestReturnsForbidden())
                .post(depositRequest).extract().response().asString();

        //проверяем полученную ошибку с ожидаемой
        softly.assertThat(actualErrorMessage).isEqualTo(ResponseMessages.UNAUTHORIZED_ACCESS_TO_ACCOUNT.getValue());

        //проверяем баланс аккаунта пользователя
        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(expectedBalance);

        //проверяем транзакции пользователя
        final List<UserTransactionsResponse> userTransactions = getUserTransactions(authUserToken, userAccount);

        softly.assertThat(userTransactions).isEmpty();

    }

    @Test
    @DisplayName("Негативный тест: пользователь при попытке положить деньги на несуществующий аккаунт не пополняет свой счёт")
    public void userCannotDepositIntoNonExistedAccount() {

        double expectedBalance = 0.0;
        final Double depositMoney = RandomData.getMoney();

        final DepositRequest depositRequest = DepositRequest
                .builder().id(getMaxExistedAccountId() + 1).balance(depositMoney).build();

        //выполняем пополнение чужого аккаунта и сохраняем сообщение об ошибке
        final String actualErrorMessage =
                new DepositRequester(RequestSpecs.withTokenSpec(authUserToken), ResponseSpecs.requestReturnsForbidden())
                        .post(depositRequest).extract().response().asString();

        //проверяем полученную ошибку с ожидаемой
        softly.assertThat(actualErrorMessage).isEqualTo(ResponseMessages.UNAUTHORIZED_ACCESS_TO_ACCOUNT.getValue());

        //проверяем баланс аккаунта пользователя
        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(expectedBalance);

        //проверяем транзакции пользователя
        final List<UserTransactionsResponse> userTransactions = getUserTransactions(authUserToken, userAccount);

        softly.assertThat(userTransactions).isEmpty();

    }
}
