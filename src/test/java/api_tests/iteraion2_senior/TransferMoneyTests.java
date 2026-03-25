package api_tests.iteraion2_senior;

import config.Operations;
import config.ResponseMessages;
import models.TransferResponse;
import models.UserTransactionsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import requests.steps.user_steps.UserSteps;
import specs.ResponseSpecs;
import utils.RandomData;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Stream;

import static config.ResponseMessages.*;
import static requests.steps.admin_steps.AdminSteps.createUserAndGetToken;
import static requests.steps.admin_steps.AdminSteps.getMaxExistedAccountId;
import static requests.steps.user_steps.UserSteps.*;

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
    public void userCanTransferMoneyToSomeoneElseExistedAccount(double moneyToDeposit, double moneyToTransfer) {

        int repeatDepositTimes = 2;
        //дважды пополняем аккаунт основного пользователя
        UserSteps.repeatAction(repeatDepositTimes, () -> depositMoneyWOCheckResponse(authUserToken, userAccount, moneyToDeposit));

        //создаём второго пользователя и получаем его токен
        final String authTokenUserSecond = createUserAndGetToken();

        //создаём аккаунт для второго пользователя
        final int secondUserAccount = createUserAccount(authTokenUserSecond);

        //сохраняем текущее время
        nowTime = ZonedDateTime.now(ZoneOffset.UTC);

        //выполняем перевод с аккаунта основного пользователя на аккаунт второго пользователя и проверяем параметры ответа
        final TransferResponse transferResponse =
                successfulTransferMoneyBetweenAccounts(authUserToken, userAccount, secondUserAccount, moneyToTransfer);

        //проверяем сообщение в ответе об успешном переводе
        softly.assertThat(transferResponse.getMessage()).isEqualTo(ResponseMessages.TRANSFER_SUCCESSFUL.getValue());

        //проверяем общий баланс аккаунта основного пользователя после перевода
        final double expectedBalanceForFirstUserRawValue =
                Math.round((moneyToDeposit * repeatDepositTimes - moneyToTransfer) * 100D) / 100D;
        BigDecimal expectedBalanceForFirstUser =
                BigDecimal.valueOf(expectedBalanceForFirstUserRawValue).setScale(2, RoundingMode.HALF_UP);

        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(expectedBalanceForFirstUser.doubleValue());

        //проверяем транзакции основного пользователя на перевод
        UserSteps.checkPositiveUserTransactions(authUserToken, userAccount, secondUserAccount, nowTime, Operations.TRANSFER_OUT, moneyToTransfer);

        //проверяем общий баланс аккаунта второго пользователя после перевода
        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccount)).isEqualTo(moneyToTransfer);

        //проверяем транзакции второго пользователя на полученный перевод
        checkPositiveUserTransactions(authTokenUserSecond, secondUserAccount, userAccount, nowTime, Operations.TRANSFER_IN, moneyToTransfer);

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

        //проверяем транзакции основного пользователя на перевод
        UserSteps.checkNegativeUserTransactions(authUserToken, userAccount, Operations.TRANSFER_OUT);

        //проверяем баланс аккаунта второго пользователя
        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccount)).isEqualTo(DEFAULT_ZERO_BALANCE);

        //проверяем транзакцию второго пользователя на получение перевода
        checkNegativeUserTransactions(authTokenUserSecond, secondUserAccount, null);
    }


    @Test
    @DisplayName("Негативный тест: пользователь не может переводить сумму больше 10000")
    public void userCannotTransferMoneyToSomeoneElseExistedAccountMoreThanMaximumLimit() {

        double moneyToDeposit = RandomData.getMoneyFromTo(4000, 5000);
        double moneyToTransfer = 10000.01;

        int repeatDepositTimes = 3;
        //трижды пополняем аккаунт основного пользователя
        UserSteps.repeatAction(repeatDepositTimes, () -> depositMoneyWOCheckResponse(authUserToken, userAccount, moneyToDeposit));

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
        double expectedBalance =
                BigDecimal.valueOf(moneyToDeposit * repeatDepositTimes).setScale(2, RoundingMode.HALF_UP).doubleValue();
        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(expectedBalance);

        //проверяем транзакции основного пользователя на перевод
        checkNegativeUserTransactions(authUserToken, userAccount, Operations.TRANSFER_OUT);

        //проверяем баланс аккаунта второго пользователя
        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccount)).isEqualTo(0.0);

        //проверяем транзакции второго пользователя
        checkNegativeUserTransactions(authTokenUserSecond, secondUserAccount, null);

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
        nowTime = ZonedDateTime.now(ZoneOffset.UTC);

        //выполняем перевод с первого аккаунта пользователя на его второй аккаунт
        final TransferResponse transferResponse =
                successfulTransferMoneyBetweenAccounts(authUserToken, userAccount, userAccountSecond, depositTransferMoney);

        //проверяем значения параметров ответа на post запрос перевода
        softly.assertThat(transferResponse.getMessage()).isEqualTo(ResponseMessages.TRANSFER_SUCCESSFUL.getValue());

        //проверяем общий баланс первого аккаунта пользователя после перевода
        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(DEFAULT_ZERO_BALANCE);

        //проверяем транзакцию пользователя на перевод с первого аккаунта на второй
        checkPositiveUserTransactions(authUserToken, userAccount, userAccountSecond, nowTime, Operations.TRANSFER_OUT, depositTransferMoney);

        //проверяем общий баланс второго аккаунта пользователя после перевода
        softly.assertThat(getUserBalance(authUserToken, userAccountSecond)).isEqualTo(depositTransferMoney);

        //проверяем транзакции пользователя по второму аккаунту
        checkPositiveUserTransactions(authUserToken, userAccountSecond, userAccount, nowTime, Operations.TRANSFER_IN, depositTransferMoney);

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
        nowTime = ZonedDateTime.now(ZoneOffset.UTC);

        //выполняем перевод с аккаунта основного пользователя на аккаунт второго пользователя
        final TransferResponse transferResponse = successfulTransferMoneyBetweenAccounts(authUserToken, userAccount,
                secondUserAccountSecond, depositTransferMoney);

        //проверяем значения параметров ответов на post запрос перевода
        softly.assertThat(transferResponse.getMessage()).isEqualTo(ResponseMessages.TRANSFER_SUCCESSFUL.getValue());

        //проверяем общий баланс аккаунта основного пользователя после перевода
        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(DEFAULT_ZERO_BALANCE);

        //проверяем транзакцию основного пользователя на перевод
        checkPositiveUserTransactions(authUserToken, userAccount, secondUserAccountSecond, nowTime, Operations.TRANSFER_OUT, depositTransferMoney);

        //проверяем общий баланс второго аккаунта второго пользователя после перевода
        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccountSecond)).isEqualTo(depositTransferMoney);

        //проверяем транзакцию второго пользователя на полученный перевод
        checkPositiveUserTransactions(authTokenUserSecond, secondUserAccountSecond, userAccount, nowTime,
                Operations.TRANSFER_IN, depositTransferMoney);

        //проверяем общий баланс первого аккаунта второго пользователя после перевода
        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccountFirst)).isEqualTo(DEFAULT_ZERO_BALANCE);

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
        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(DEFAULT_ZERO_BALANCE);

        //Проверяем транзакции основного пользователя
        final List<UserTransactionsResponse> userFirstTransactions = getUserTransactions(authUserToken, userAccount);
        softly.assertThat(userFirstTransactions.isEmpty());

        //Проверяем баланс второго пользователя
        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccount)).isEqualTo(DEFAULT_ZERO_BALANCE);

        //Проверяем транзакции второго пользователя
        final List<UserTransactionsResponse> userSecondTransactions = getUserTransactions(authTokenUserSecond,
                secondUserAccount);
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
        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(DEFAULT_ZERO_BALANCE);

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
        final String actualErrorMessage = failedTransferMoneyBetweenAccounts(authUserToken, userAccount, userAccount,
                moneyToDepositTransfer, ResponseSpecs.requestReturnsBadRequest());

        //проверяем сообщение об ошибке
        softly.assertThat(actualErrorMessage).isEqualTo(OPERATION_IS_FORBIDDEN);

        //Проверяем баланс пользователя
        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(moneyToDepositTransfer);

    }

    @Test
    @DisplayName("Негативный тест: пользователь не может переводить деньги на несуществующий аккаунт")
    public void userCannotTransferMoneyFromToNonExistedAccount() {

        double moneyToTransfer = RandomData.getMoney();

        //получаем несуществующий аккаунт
        nonExistingAccount = getMaxExistedAccountId() + 1;

        //выполняем запрос по переводу и сохраняем сообщение об ошибке
        final String actualErrorMessage = failedTransferMoneyBetweenAccounts(authUserToken, userAccount, nonExistingAccount,
                moneyToTransfer, ResponseSpecs.requestReturnsBadRequest());

        //проверяем сообщение об ошибке
        softly.assertThat(actualErrorMessage).isEqualTo(INVALID_TRANSFER_INSUFFICIENT_FUNDS_OR_INVALID_ACCOUNTS.getValue());

        //Проверяем баланс пользователя
        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(DEFAULT_ZERO_BALANCE);

        //Проверяем транзакции аккаунта пользователя
        checkNegativeUserTransactions(authUserToken, userAccount, Operations.TRANSFER_IN);

    }

    @Test
    @DisplayName("Негативный тест: пользователь не может переводить деньги с несуществующего аккаунта")
    public void userCannotTransferMoneyFromNonExistedAccount() {

        double moneyToTransfer = RandomData.getMoney();

        //получаем несуществующий аккаунт
        nonExistingAccount = getMaxExistedAccountId() + 1;

        //выполняем запрос по переводу и сохраняем сообщение об ошибке
        final String actualErrorMessage = failedTransferMoneyBetweenAccounts(authUserToken, nonExistingAccount,
                userAccount, moneyToTransfer, ResponseSpecs.requestReturnsForbidden());

        //проверяем сообщение об ошибке
        softly.assertThat(actualErrorMessage).isEqualTo(UNAUTHORIZED_ACCESS_TO_ACCOUNT.getValue());

        //Проверяем баланс пользователя
        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(DEFAULT_ZERO_BALANCE);

        //Проверяем транзакции аккаунта пользователя
        checkNegativeUserTransactions(authUserToken, userAccount, Operations.TRANSFER_IN);
    }
}
