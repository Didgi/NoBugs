package api_tests.iteraion2_middle;

import config.Operations;
import config.ResponseMessages;
import models.TransferResponse;
import models.UserTransactionsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import specs.ResponseSpecs;
import utils.RandomData;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static config.ResponseMessages.*;

public class TransferMoneyTests extends BaseTestMiddle {

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
    public void userCanTransferMoneyToSomeoneElseExistedAccount(double moneyToDeposit, double moneyToTransfer) {

        //дважды пополняем аккаунт основного пользователя
        depositMoneyWOCheckResponse(authUserToken, userAccount, moneyToDeposit);
        depositMoneyWOCheckResponse(authUserToken, userAccount, moneyToDeposit);

        //создаём второго пользователя и получаем его токен
        final String authTokenUserSecond = createUserAndGetToken();
        //создаём аккаунт для второго пользователя
        final int secondUserAccount = createUserAccount(authTokenUserSecond);

        //сохраняем текущее время
        ZonedDateTime nowTime = ZonedDateTime.now(ZoneOffset.UTC);

        //выполняем перевод с аккаунта основного пользователя на аккаунт второго пользователя
        final TransferResponse transferResponse =
                successfulTransferMoneyBetweenAccounts(authUserToken, userAccount, secondUserAccount, moneyToTransfer);

        //проверяем значения параметров ответов на post запрос перевода
        softly.assertThat(transferResponse.getSenderAccountId()).isEqualTo(userAccount);
        softly.assertThat(transferResponse.getReceiverAccountId()).isEqualTo(secondUserAccount);
        softly.assertThat(transferResponse.getAmount()).isEqualTo(moneyToTransfer);
        softly.assertThat(transferResponse.getMessage()).isEqualTo(ResponseMessages.TRANSFER_SUCCESSFUL.getValue());

        //проверяем общий баланс аккаунта основного пользователя после перевода
        final double expectedBalanceForFirstUserRawValue = Math.round((moneyToDeposit * 2 - moneyToTransfer) * 100D) / 100D;
        BigDecimal expectedBalanceForFirstUser =
                BigDecimal.valueOf(expectedBalanceForFirstUserRawValue).setScale(2, RoundingMode.HALF_UP);

        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(expectedBalanceForFirstUser.doubleValue());

        //проверяем транзакции основного пользователя
        final List<UserTransactionsResponse> userFirstTransactions = getUserTransactions(authUserToken, userAccount);

        userFirstTransactions.forEach(transactions -> {
            softly.assertThat(transactions.getId()).isGreaterThan(0);
            softly.assertThat(transactions.getTimestamp()).isBetween(nowTime.minusSeconds(30), nowTime.plusSeconds(30));
        });

        //проверяем транзакцию основного пользователя на перевод
        final UserTransactionsResponse userFirstTransactionsResponse = userFirstTransactions
                .stream().max(Comparator.comparingInt(UserTransactionsResponse::getId)).orElseThrow();

        softly.assertThat(userFirstTransactionsResponse.getAmount()).isEqualTo(moneyToTransfer);
        softly.assertThat(userFirstTransactionsResponse.getType()).isEqualTo(Operations.TRANSFER_OUT);
        softly.assertThat(userFirstTransactionsResponse.getRelatedAccountId()).isEqualTo(secondUserAccount);

        //проверяем общий баланс аккаунта второго пользователя после перевода
        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccount)).isEqualTo(moneyToTransfer);

        //проверяем транзакции второго пользователя
        final List<UserTransactionsResponse> userSecondTransactions = getUserTransactions(authTokenUserSecond,
                secondUserAccount);

        userSecondTransactions.forEach(transactions -> {
            softly.assertThat(transactions.getId()).isGreaterThan(0);
            softly.assertThat(transactions.getTimestamp()).isBetween(nowTime.minusSeconds(30), nowTime.plusSeconds(30));
        });

        //проверяем транзакцию второго пользователя на полученный перевод
        final UserTransactionsResponse userSecondTransactionsResponse = userSecondTransactions
                .stream().max(Comparator.comparingInt(UserTransactionsResponse::getId)).orElseThrow();

        softly.assertThat(userSecondTransactionsResponse.getAmount()).isEqualTo(moneyToTransfer);
        softly.assertThat(userSecondTransactionsResponse.getType()).isEqualTo(Operations.TRANSFER_IN);
        softly.assertThat(userSecondTransactionsResponse.getRelatedAccountId()).isEqualTo(userAccount);

    }

    private static Stream<Arguments> diffNegativeValue() {
        return Stream.of(
                Arguments.of(0.1, -0.01, TRANSFER_AMOUNT_MUST_BE_AT_LEAST_01.getValue()),
                Arguments.of(0.1, 0.0, TRANSFER_AMOUNT_MUST_BE_AT_LEAST_01.getValue()));
    }


    @MethodSource("diffNegativeValue")
    @ParameterizedTest
    @DisplayName("Негативный тест: пользователь не может переводить сумму меньше 0.01")
    public void userCannotTransferMoneyToSomeoneElseExistedAccountLessThanMinimumLimit(double moneyToDeposit,
                                                                                       double moneyToTransfer,
                                                                                       String errorMessage) {
        //пополняем аккаунт основного пользователя
        depositMoneyWOCheckResponse(authUserToken, userAccount, moneyToDeposit);

        //создаём второго пользователя и получаем его токен
        final String authTokenUserSecond = createUserAndGetToken();

        //создаём аккаунт для второго пользователя
        final int secondUserAccount = createUserAccount(authTokenUserSecond);

        //выполняем запрос по переводу и сохраняем сообщение об ошибке
        final String actualErrorMessage =
                failedTransferMoneyBetweenAccounts(authUserToken, userAccount, secondUserAccount, moneyToTransfer,
                        ResponseSpecs.requestReturnsBadRequest());

        //проверяем сообщение об ошибке
        softly.assertThat(actualErrorMessage).isEqualTo(errorMessage);

        //проверяем баланс аккаунта основного пользователя
        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(moneyToDeposit);

        //проверяем транзакции основного пользователя
        final List<UserTransactionsResponse> userFirstTransactions = getUserTransactions(authUserToken, userAccount);

        //проверяем транзакцию основного пользователя на перевод
        final UserTransactionsResponse userFirstTransactionsResponse = userFirstTransactions
                .stream().max(Comparator.comparingInt(UserTransactionsResponse::getId)).orElseThrow();

        softly.assertThat(userFirstTransactionsResponse.getType()).isNotEqualTo(Operations.TRANSFER_OUT);

        //проверяем баланс аккаунта второго пользователя
        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccount)).isEqualTo(0.0);

        //проверяем транзакцию второго пользователя на получение перевода
        final List<UserTransactionsResponse> userSecondUserTransactions = getUserTransactions(authTokenUserSecond, secondUserAccount);

        softly.assertThat(userSecondUserTransactions.isEmpty());
    }


    @Test
    @DisplayName("Негативный тест: пользователь не может переводить сумму больше 10000")
    public void userCannotTransferMoneyToSomeoneElseExistedAccountMoreThanMaximumLimit() {

        int moneyToDeposit = 5000;
        double moneyToTransfer = 10000.01;

        //трижды пополняем аккаунт основного пользователя
        depositMoneyWOCheckResponse(authUserToken, userAccount, moneyToDeposit);
        depositMoneyWOCheckResponse(authUserToken, userAccount, moneyToDeposit);
        depositMoneyWOCheckResponse(authUserToken, userAccount, moneyToDeposit);

        //создаём второго пользователя и получаем его токен
        final String authTokenUserSecond = createUserAndGetToken();
        //создаём аккаунт для второго пользователя
        final int secondUserAccount = createUserAccount(authTokenUserSecond);

        //выполняем запрос по переводу и сохраняем сообщение об ошибке
        final String actualErrorMessage =
                failedTransferMoneyBetweenAccounts(authUserToken, userAccount, secondUserAccount, moneyToTransfer,
                        ResponseSpecs.requestReturnsBadRequest());

        //проверяем сообщение об ошибке
        softly.assertThat(actualErrorMessage).isEqualTo(TRANSFER_AMOUNT_CANNOT_EXCEED_10000.getValue());

        //проверяем баланс аккаунта основного пользователя
        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(moneyToDeposit * 3);

        //проверяем транзакции основного пользователя
        final List<UserTransactionsResponse> userFirstTransactions = getUserTransactions(authUserToken, userAccount);

        //проверяем транзакцию основного пользователя на перевод
        final UserTransactionsResponse userFirstTransactionsResponse = userFirstTransactions
                .stream().max(Comparator.comparingInt(UserTransactionsResponse::getId)).orElseThrow();

        softly.assertThat(userFirstTransactionsResponse.getType()).isNotEqualTo(Operations.TRANSFER_OUT);

        //проверяем баланс аккаунта второго пользователя
        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccount)).isEqualTo(0.0);

        //проверяем транзакции второго пользователя
        final List<UserTransactionsResponse> userSecondUserTransactions = getUserTransactions(authTokenUserSecond, secondUserAccount);

        softly.assertThat(userSecondUserTransactions.isEmpty());

    }

    @Test
    @DisplayName("Позитивный тест: пользователь может переводить деньги между своими же аккаунтами")
    public void userCanTransferMoneyBetweenHisAccounts() {

        final double depositTransferMoney = RandomData.getMoney();

        //пополняем первый аккаунт пользователя
        depositMoneyWOCheckResponse(authUserToken, userAccount, depositTransferMoney);

        //создаём второй аккаунт для пользователя
        final int userAccountSecond = createUserAccount(authUserToken);

        //сохраняем текущее время
        ZonedDateTime nowTime = ZonedDateTime.now(ZoneOffset.UTC);

        //выполняем перевод с первого аккаунта пользователя на его второй аккаунт
        final TransferResponse transferResponse =
                successfulTransferMoneyBetweenAccounts(authUserToken, userAccount, userAccountSecond, depositTransferMoney);

        //проверяем значения параметров ответа на post запрос перевода
        softly.assertThat(transferResponse.getSenderAccountId()).isEqualTo(userAccount);
        softly.assertThat(transferResponse.getReceiverAccountId()).isEqualTo(userAccountSecond);
        softly.assertThat(transferResponse.getAmount()).isEqualTo(depositTransferMoney);
        softly.assertThat(transferResponse.getMessage()).isEqualTo(ResponseMessages.TRANSFER_SUCCESSFUL.getValue());

        //проверяем общий баланс первого аккаунта пользователя после перевода
        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(0.0);

        //проверяем транзакции пользователя по первому аккаунту
        final List<UserTransactionsResponse> userFirstTransactions = getUserTransactions(authUserToken, userAccount);

        userFirstTransactions.forEach(transactions -> {
            softly.assertThat(transactions.getId()).isGreaterThan(0);
            softly.assertThat(transactions.getTimestamp()).isBetween(nowTime.minusSeconds(30), nowTime.plusSeconds(30));
        });

        //проверяем транзакцию пользователя на перевод с первого аккаунта на второй
        final UserTransactionsResponse userFirstTransactionsResponse = userFirstTransactions
                .stream().max(Comparator.comparingInt(UserTransactionsResponse::getId)).orElseThrow();

        softly.assertThat(userFirstTransactionsResponse.getAmount()).isEqualTo(depositTransferMoney);
        softly.assertThat(userFirstTransactionsResponse.getType()).isEqualTo(Operations.TRANSFER_OUT);
        softly.assertThat(userFirstTransactionsResponse.getRelatedAccountId()).isEqualTo(userAccountSecond);

        //проверяем общий баланс второго аккаунта пользователя после перевода
        softly.assertThat(getUserBalance(authUserToken, userAccountSecond)).isEqualTo(depositTransferMoney);

        //проверяем транзакции пользователя по второму аккаунту
        final List<UserTransactionsResponse> userSecondTransactions = getUserTransactions(authUserToken,
                userAccountSecond);

        userSecondTransactions.forEach(transactions -> {
            softly.assertThat(transactions.getId()).isGreaterThan(0);
            softly.assertThat(transactions.getTimestamp()).isBetween(nowTime.minusSeconds(30), nowTime.plusSeconds(30));
        });

        //проверяем транзакцию пользователя на полученный перевод с первого аккаунта
        final UserTransactionsResponse userSecondTransactionsResponse = userSecondTransactions
                .stream().max(Comparator.comparingInt(UserTransactionsResponse::getId)).orElseThrow();

        softly.assertThat(userSecondTransactionsResponse.getAmount()).isEqualTo(depositTransferMoney);
        softly.assertThat(userSecondTransactionsResponse.getType()).isEqualTo(Operations.TRANSFER_IN);
        softly.assertThat(userSecondTransactionsResponse.getRelatedAccountId()).isEqualTo(userAccount);

    }

    @Test
    @DisplayName("Позитивный тест: при переводе пользователем денег пользователю на один из двух аккаунтов, " +
            "другой аккаунт не пополняется")
    public void UserCanTransferMoneyToSomeoneElseUserWithTwoExistedAccounts() {

        final Double depositTransferMoney = RandomData.getMoney();

        //пополняем аккаунт основного пользователя
        depositMoneyWOCheckResponse(authUserToken, userAccount, depositTransferMoney);

        //создаём второго пользователя и получаем его токен
        final String authTokenUserSecond = createUserAndGetToken();
        //создаём первый аккаунт второго пользователя
        final int secondUserAccountFirst = createUserAccount(authTokenUserSecond);
        //создаём второй аккаунт второго пользователя
        final int secondUserAccountSecond = createUserAccount(authTokenUserSecond);

        //сохраняем текущее время
        ZonedDateTime nowTime = ZonedDateTime.now(ZoneOffset.UTC);

        //выполняем перевод с аккаунта основного пользователя на аккаунт второго пользователя
        final TransferResponse transferResponse = successfulTransferMoneyBetweenAccounts(authUserToken, userAccount,
                secondUserAccountSecond, depositTransferMoney);

        //проверяем значения параметров ответов на post запрос перевода
        softly.assertThat(transferResponse.getSenderAccountId()).isEqualTo(userAccount);
        softly.assertThat(transferResponse.getReceiverAccountId()).isEqualTo(secondUserAccountSecond);
        softly.assertThat(transferResponse.getAmount()).isEqualTo(depositTransferMoney);
        softly.assertThat(transferResponse.getMessage()).isEqualTo(ResponseMessages.TRANSFER_SUCCESSFUL.getValue());

        //проверяем общий баланс аккаунта основного пользователя после перевода
        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(0.0);

        //проверяем транзакции основного пользователя
        final List<UserTransactionsResponse> userFirstTransactions = getUserTransactions(authUserToken, userAccount);

        userFirstTransactions.forEach(transactions -> {
            softly.assertThat(transactions.getId()).isGreaterThan(0);
            softly.assertThat(transactions.getTimestamp()).isBetween(nowTime.minusSeconds(30), nowTime.plusSeconds(30));
        });

        //проверяем транзакцию основного пользователя на перевод
        final UserTransactionsResponse userFirstTransactionsResponse = userFirstTransactions
                .stream().max(Comparator.comparingInt(UserTransactionsResponse::getId)).orElseThrow();

        softly.assertThat(userFirstTransactionsResponse.getAmount()).isEqualTo(depositTransferMoney);
        softly.assertThat(userFirstTransactionsResponse.getType()).isEqualTo(Operations.TRANSFER_OUT);
        softly.assertThat(userFirstTransactionsResponse.getRelatedAccountId()).isEqualTo(secondUserAccountSecond);

        //проверяем общий баланс второго аккаунта второго пользователя после перевода
        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccountSecond)).isEqualTo(depositTransferMoney);

        //проверяем транзакции второго пользователя
        final List<UserTransactionsResponse> secondUserSecondAccountTransactions = getUserTransactions(authTokenUserSecond,
                secondUserAccountSecond);

        secondUserSecondAccountTransactions.forEach(transactions -> {
            softly.assertThat(transactions.getId()).isGreaterThan(0);
            softly.assertThat(transactions.getTimestamp()).isBetween(nowTime.minusSeconds(30), nowTime.plusSeconds(30));
        });

        //проверяем транзакцию второго пользователя на полученный перевод
        final UserTransactionsResponse secondUserSecondAccountTransactionsResponse = secondUserSecondAccountTransactions
                .stream().max(Comparator.comparingInt(UserTransactionsResponse::getId)).orElseThrow();

        softly.assertThat(secondUserSecondAccountTransactionsResponse.getAmount()).isEqualTo(depositTransferMoney);
        softly.assertThat(secondUserSecondAccountTransactionsResponse.getType()).isEqualTo(Operations.TRANSFER_IN);
        softly.assertThat(secondUserSecondAccountTransactionsResponse.getRelatedAccountId()).isEqualTo(userAccount);

        //проверяем общий баланс первого аккаунта второго пользователя после перевода
        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccountFirst)).isEqualTo(0.0);

    }

    @Test
    @DisplayName("Негативный тест: пользователь не может переводить деньги при нулевом балансе")
    public void userCannotTransferMoneyToSomeoneElseExistedAccountWhenHisAccountBalanceIsZero() {

        final Double transferMoney = RandomData.getMoney();

        //создаём второго пользователя и получаем его токен
        final String authTokenUserSecond = createUserAndGetToken();

        //создаём аккаунт для второго пользователя
        final int secondUserAccount = createUserAccount(authTokenUserSecond);

        //выполняем запрос по переводу и сохраняем сообщение об ошибке
        final String errorMessage = failedTransferMoneyBetweenAccounts(authUserToken, userAccount, secondUserAccount,
                transferMoney, ResponseSpecs.requestReturnsBadRequest());

        //проверяем сообщение об ошибке
        softly.assertThat(errorMessage).isEqualTo(INVALID_TRANSFER_INSUFFICIENT_FUNDS_OR_INVALID_ACCOUNTS.getValue());

        //Проверяем баланс основного пользователя
        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(0.0);

        //Проверяем транзакции основного пользователя
        final List<UserTransactionsResponse> userFirstTransactions = getUserTransactions(authUserToken, userAccount);
        softly.assertThat(userFirstTransactions.isEmpty());

        //Проверяем баланс второго пользователя
        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccount)).isEqualTo(0.0);

        //Проверяем транзакции второго пользователя
        final List<UserTransactionsResponse> userSecondTransactions = getUserTransactions(authTokenUserSecond, secondUserAccount);
        softly.assertThat(userSecondTransactions.isEmpty());

    }


    @Test
    @DisplayName("Негативный тест: пользователь не может переводить деньги с чужого аккаунта на свой")
    public void userCannotTransferMoneyFromSomeoneElseExistedAccountToHisOwn() {

        double moneyToDepositTransfer = RandomData.getMoney();

        //создаём второго пользователя и получаем его токен
        final String authTokenUserSecond = createUserAndGetToken();

        //создаём аккаунт для второго пользователя
        final int secondUserAccount = createUserAccount(authTokenUserSecond);

        //Пополняем аккаунт второго пользователя
        depositMoney(authTokenUserSecond, secondUserAccount, moneyToDepositTransfer);

        //выполняем запрос по переводу и сохраняем сообщение об ошибке
        final String errorMessage = failedTransferMoneyBetweenAccounts(authUserToken, secondUserAccount,
                userAccount, moneyToDepositTransfer, ResponseSpecs.requestReturnsForbidden());

        //проверяем сообщение об ошибке
        softly.assertThat(errorMessage).isEqualTo(UNAUTHORIZED_ACCESS_TO_ACCOUNT.getValue());

        //Проверяем баланс основного пользователя
        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(0.0);

        //Проверяем баланс второго пользователя
        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccount)).isEqualTo(moneyToDepositTransfer);

    }

    @Test
    @DisplayName("Негативный тест: пользователь не может переводить деньги с/на один и тот же свой аккаунт")
    public void userCannotTransferMoneyFromToSameHisAccount() {

        double moneyToDepositTransfer = RandomData.getMoney();
        //Пополняем аккаунт основного пользователя
        depositMoney(authUserToken, userAccount, moneyToDepositTransfer);

        //выполняем запрос по переводу и сохраняем сообщение об ошибке
        //тест падает, т.к. ожидается 400, а отдаётся 200. Писал об этом ранее
        final String actualErrorMessage = failedTransferMoneyBetweenAccounts(authUserToken, userAccount, userAccount, moneyToDepositTransfer,
                ResponseSpecs.requestReturnsBadRequest());

        //проверяем сообщение об ошибке
        softly.assertThat(actualErrorMessage).isEqualTo(OPERATION_IS_FORBIDDEN);

        //Проверяем баланс пользователя
        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(moneyToDepositTransfer);

    }

    @Test
    @DisplayName("Негативный тест: пользователь не может переводить деньги на несуществующий аккаунт")
    public void userCannotTransferMoneyFromToNonExistedAccount() {

        double moneyToTransfer = RandomData.getMoney();
        int nonExistingAccount = getMaxExistedAccountId() + 1;

        //выполняем запрос по переводу и сохраняем сообщение об ошибке
        final String actualErrorMessage = failedTransferMoneyBetweenAccounts(authUserToken, userAccount, nonExistingAccount,
                moneyToTransfer, ResponseSpecs.requestReturnsBadRequest());

        //проверяем сообщение об ошибке
        softly.assertThat(actualErrorMessage).isEqualTo(INVALID_TRANSFER_INSUFFICIENT_FUNDS_OR_INVALID_ACCOUNTS.getValue());

        //Проверяем баланс пользователя
        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(0.0);

        //Проверяем транзакции аккаунта пользователя
        final List<UserTransactionsResponse> userTransactions = getUserTransactions(authUserToken, userAccount);

        userTransactions.forEach(transactions -> {
            softly.assertThat(transactions.getType()).isNotEqualTo(Operations.TRANSFER_IN);
        });

    }

    @Test
    @DisplayName("Негативный тест: пользователь не может переводить деньги с несуществующего аккаунта")
    public void userCannotTransferMoneyFromNonExistedAccount() {

        double moneyToTransfer = RandomData.getMoney();
        int nonExistingAccount = getMaxExistedAccountId() + 1;

        //выполняем запрос по переводу и сохраняем сообщение об ошибке
        final String actualErrorMessage = failedTransferMoneyBetweenAccounts(authUserToken, nonExistingAccount,
                userAccount, moneyToTransfer, ResponseSpecs.requestReturnsForbidden());

        //проверяем сообщение об ошибке
        softly.assertThat(actualErrorMessage).isEqualTo(UNAUTHORIZED_ACCESS_TO_ACCOUNT.getValue());

        //Проверяем баланс пользователя
        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(0.0);

        //Проверяем транзакции аккаунта пользователя
        final List<UserTransactionsResponse> userTransactions = getUserTransactions(authUserToken, userAccount);

        userTransactions.forEach(transactions -> {
            softly.assertThat(transactions.getType()).isNotEqualTo(Operations.TRANSFER_IN);
        });

    }

}
