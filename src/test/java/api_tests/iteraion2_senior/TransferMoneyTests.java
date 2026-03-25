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
        // 1. Дважды пополняем аккаунт основного пользователя
        UserSteps.repeatAction(repeatDepositTimes, () -> depositMoneyWOCheckResponse(authUserToken, userAccount, moneyToDeposit));

        // 2. Создаём второго пользователя и получаем его токен
        final String authTokenUserSecond = createUserAndGetToken();

        // 3. Создаём аккаунт для второго пользователя
        final int secondUserAccount = createUserAccount(authTokenUserSecond);

        // 4. Сохраняем текущее время
        nowTime = ZonedDateTime.now(ZoneOffset.UTC);

        // 5. Выполняем перевод с аккаунта основного пользователя на аккаунт второго пользователя и проверяем параметры ответа
        final TransferResponse transferResponse =
                successfulTransferMoneyBetweenAccounts(authUserToken, userAccount, secondUserAccount, moneyToTransfer);

        // 6. Проверяем сообщение в ответе об успешном переводе
        softly.assertThat(transferResponse.getMessage()).isEqualTo(ResponseMessages.TRANSFER_SUCCESSFUL.getValue());

        // 7. Проверяем общий баланс аккаунта основного пользователя после перевода
        final double expectedBalanceForFirstUserRawValue =
                Math.round((moneyToDeposit * repeatDepositTimes - moneyToTransfer) * 100D) / 100D;
        BigDecimal expectedBalanceForFirstUser =
                BigDecimal.valueOf(expectedBalanceForFirstUserRawValue).setScale(2, RoundingMode.HALF_UP);

        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(expectedBalanceForFirstUser.doubleValue());

        // 8. Проверяем транзакции основного пользователя на перевод
        UserSteps.checkPositiveUserTransactions(authUserToken, userAccount, secondUserAccount, nowTime, Operations.TRANSFER_OUT, moneyToTransfer);

        // 9. Проверяем общий баланс аккаунта второго пользователя после перевода
        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccount)).isEqualTo(moneyToTransfer);

        // 10. Проверяем транзакции второго пользователя на полученный перевод
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

        // 1. Пополняем аккаунт основного пользователя
        depositMoneyWOCheckResponse(authUserToken, userAccount, moneyToDeposit);

        // 2. Создаём второго пользователя и получаем его токен
        final String authTokenUserSecond = createUserAndGetToken();

        // 3. Создаём аккаунт для второго пользователя
        final int secondUserAccount = createUserAccount(authTokenUserSecond);

        // 4. Выполняем запрос по переводу и сохраняем сообщение об ошибке
        final String actualErrorMessage =
                failedTransferMoneyBetweenAccounts(authUserToken, userAccount, secondUserAccount, moneyToTransfer,
                        ResponseSpecs.requestReturnsBadRequest());

        // 5. Проверяем сообщение об ошибке
        softly.assertThat(actualErrorMessage).isEqualTo(errorMessage);

        // 6. Проверяем баланс аккаунта основного пользователя
        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(moneyToDeposit);

        // 7. Проверяем транзакции основного пользователя на перевод
        UserSteps.checkNegativeUserTransactions(authUserToken, userAccount, Operations.TRANSFER_OUT);

        // 8. Проверяем баланс аккаунта второго пользователя
        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccount)).isEqualTo(DEFAULT_ZERO_BALANCE);

        // 9. Проверяем транзакцию второго пользователя на получение перевода
        checkNegativeUserTransactions(authTokenUserSecond, secondUserAccount, null);
    }


    @Test
    @DisplayName("Негативный тест: пользователь не может переводить сумму больше 10000")
    public void userCannotTransferMoneyToSomeoneElseExistedAccountMoreThanMaximumLimit() {

        double moneyToDeposit = RandomData.getMoneyFromTo(4000, 5000);
        double moneyToTransfer = 10000.01;

        int repeatDepositTimes = 3;
        // 1. Трижды пополняем аккаунт основного пользователя
        UserSteps.repeatAction(repeatDepositTimes, () -> depositMoneyWOCheckResponse(authUserToken, userAccount, moneyToDeposit));

        // 2. Создаём второго пользователя и получаем его токен
        final String authTokenUserSecond = createUserAndGetToken();
        // 3. Создаём аккаунт для второго пользователя
        final int secondUserAccount = createUserAccount(authTokenUserSecond);

        // 4. Выполняем запрос по переводу и сохраняем сообщение об ошибке
        final String actualErrorMessage =
                failedTransferMoneyBetweenAccounts(authUserToken, userAccount, secondUserAccount, moneyToTransfer,
                        ResponseSpecs.requestReturnsBadRequest());

        // 5. Проверяем сообщение об ошибке
        softly.assertThat(actualErrorMessage).isEqualTo(TRANSFER_AMOUNT_CANNOT_EXCEED_10000.getValue());

        // 6. Проверяем баланс аккаунта основного пользователя
        double expectedBalance =
                BigDecimal.valueOf(moneyToDeposit * repeatDepositTimes).setScale(2, RoundingMode.HALF_UP).doubleValue();
        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(expectedBalance);

        // 7. Проверяем транзакции основного пользователя на перевод
        checkNegativeUserTransactions(authUserToken, userAccount, Operations.TRANSFER_OUT);

        // 8. Проверяем баланс аккаунта второго пользователя
        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccount)).isEqualTo(0.0);

        // 9. Проверяем транзакции второго пользователя
        checkNegativeUserTransactions(authTokenUserSecond, secondUserAccount, null);

    }

    @Test
    @DisplayName("Позитивный тест: пользователь может переводить деньги между своими же аккаунтами")
    public void userCanTransferMoneyBetweenHisAccounts() {

        final double depositTransferMoney = RandomData.getMoney();

        // 1. Пополняем первый аккаунт пользователя
        depositMoneyWOCheckResponse(authUserToken, userAccount, depositTransferMoney);

        // 2. Создаём второй аккаунт для пользователя
        final int userAccountSecond = createUserAccount(authUserToken);

        // 3. Сохраняем текущее время
        nowTime = ZonedDateTime.now(ZoneOffset.UTC);

        // 4. Выполняем перевод с первого аккаунта пользователя на его второй аккаунт
        final TransferResponse transferResponse =
                successfulTransferMoneyBetweenAccounts(authUserToken, userAccount, userAccountSecond, depositTransferMoney);

        // 5. Проверяем значения параметров ответа на post запрос перевода
        softly.assertThat(transferResponse.getMessage()).isEqualTo(ResponseMessages.TRANSFER_SUCCESSFUL.getValue());

        // 6. Проверяем общий баланс первого аккаунта пользователя после перевода
        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(DEFAULT_ZERO_BALANCE);

        // 7. Проверяем транзакцию пользователя на перевод с первого аккаунта на второй
        checkPositiveUserTransactions(authUserToken, userAccount, userAccountSecond, nowTime, Operations.TRANSFER_OUT, depositTransferMoney);

        // 8. Проверяем общий баланс второго аккаунта пользователя после перевода
        softly.assertThat(getUserBalance(authUserToken, userAccountSecond)).isEqualTo(depositTransferMoney);

        // 9. Проверяем транзакции пользователя по второму аккаунту
        checkPositiveUserTransactions(authUserToken, userAccountSecond, userAccount, nowTime, Operations.TRANSFER_IN, depositTransferMoney);

    }

    @Test
    @DisplayName("Позитивный тест: при переводе пользователем денег пользователю на один из двух аккаунтов, " +
            "другой аккаунт не пополняется")
    public void UserCanTransferMoneyToSomeoneElseUserWithTwoExistedAccounts() {

        final Double depositTransferMoney = RandomData.getMoney();

        // 1. Пополняем аккаунт основного пользователя
        depositMoneyWOCheckResponse(authUserToken, userAccount, depositTransferMoney);

        // 2. Создаём второго пользователя и получаем его токен
        final String authTokenUserSecond = createUserAndGetToken();
        // 3. Создаём первый аккаунт второго пользователя
        final int secondUserAccountFirst = createUserAccount(authTokenUserSecond);
        // 4. Создаём второй аккаунт второго пользователя
        final int secondUserAccountSecond = createUserAccount(authTokenUserSecond);

        // 5. Сохраняем текущее время
        nowTime = ZonedDateTime.now(ZoneOffset.UTC);

        // 6. Выполняем перевод с аккаунта основного пользователя на аккаунт второго пользователя
        final TransferResponse transferResponse = successfulTransferMoneyBetweenAccounts(authUserToken, userAccount,
                secondUserAccountSecond, depositTransferMoney);

        // 7. Проверяем значения параметров ответов на post запрос перевода
        softly.assertThat(transferResponse.getMessage()).isEqualTo(ResponseMessages.TRANSFER_SUCCESSFUL.getValue());

        // 8. Проверяем общий баланс аккаунта основного пользователя после перевода
        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(DEFAULT_ZERO_BALANCE);

        // 9. Проверяем транзакцию основного пользователя на перевод
        checkPositiveUserTransactions(authUserToken, userAccount, secondUserAccountSecond, nowTime, Operations.TRANSFER_OUT, depositTransferMoney);

        // 10. Проверяем общий баланс второго аккаунта второго пользователя после перевода
        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccountSecond)).isEqualTo(depositTransferMoney);

        // 11. Проверяем транзакцию второго пользователя на полученный перевод
        checkPositiveUserTransactions(authTokenUserSecond, secondUserAccountSecond, userAccount, nowTime,
                Operations.TRANSFER_IN, depositTransferMoney);

        // 12. Проверяем общий баланс первого аккаунта второго пользователя после перевода
        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccountFirst)).isEqualTo(DEFAULT_ZERO_BALANCE);

    }

    @Test
    @DisplayName("Негативный тест: пользователь не может переводить деньги при нулевом балансе")
    public void userCannotTransferMoneyToSomeoneElseExistedAccountWhenHisAccountBalanceIsZero() {

        final Double transferMoney = RandomData.getMoney();

        // 1. Создаём второго пользователя и получаем его токен
        final String authTokenUserSecond = createUserAndGetToken();

        // 2. Создаём аккаунт для второго пользователя
        final int secondUserAccount = createUserAccount(authTokenUserSecond);

        // 3. Выполняем запрос по переводу и сохраняем сообщение об ошибке
        final String errorMessage = failedTransferMoneyBetweenAccounts(authUserToken, userAccount, secondUserAccount,
                transferMoney, ResponseSpecs.requestReturnsBadRequest());

        // 4. Проверяем сообщение об ошибке
        softly.assertThat(errorMessage).isEqualTo(INVALID_TRANSFER_INSUFFICIENT_FUNDS_OR_INVALID_ACCOUNTS.getValue());

        // 5. Проверяем баланс основного пользователя
        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(DEFAULT_ZERO_BALANCE);

        // 6. Проверяем транзакции основного пользователя
        final List<UserTransactionsResponse> userFirstTransactions = getUserTransactions(authUserToken, userAccount);
        softly.assertThat(userFirstTransactions.isEmpty());

        // 7. Проверяем баланс второго пользователя
        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccount)).isEqualTo(DEFAULT_ZERO_BALANCE);

        // 8. Проверяем транзакции второго пользователя
        final List<UserTransactionsResponse> userSecondTransactions = getUserTransactions(authTokenUserSecond,
                secondUserAccount);
        softly.assertThat(userSecondTransactions.isEmpty());

    }


    @Test
    @DisplayName("Негативный тест: пользователь не может переводить деньги с чужого аккаунта на свой")
    public void userCannotTransferMoneyFromSomeoneElseExistedAccountToHisOwn() {

        double moneyToDepositTransfer = RandomData.getMoney();

        // 1. Создаём второго пользователя и получаем его токен
        final String authTokenUserSecond = createUserAndGetToken();

        // 2. Создаём аккаунт для второго пользователя
        final int secondUserAccount = createUserAccount(authTokenUserSecond);

        // 3. Пополняем аккаунт второго пользователя
        depositMoney(authTokenUserSecond, secondUserAccount, moneyToDepositTransfer);

        // 4. выполняем запрос по переводу и сохраняем сообщение об ошибке
        final String errorMessage = failedTransferMoneyBetweenAccounts(authUserToken, secondUserAccount,
                userAccount, moneyToDepositTransfer, ResponseSpecs.requestReturnsForbidden());

        // 5. проверяем сообщение об ошибке
        softly.assertThat(errorMessage).isEqualTo(UNAUTHORIZED_ACCESS_TO_ACCOUNT.getValue());

        // 6. Проверяем баланс основного пользователя
        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(DEFAULT_ZERO_BALANCE);

        // 7. Проверяем баланс второго пользователя
        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccount)).isEqualTo(moneyToDepositTransfer);

    }

    @Test
    @DisplayName("Негативный тест: пользователь не может переводить деньги с/на один и тот же свой аккаунт")
    public void userCannotTransferMoneyFromToSameHisAccount() {

        double moneyToDepositTransfer = RandomData.getMoney();
        // 1. Пополняем аккаунт основного пользователя
        depositMoney(authUserToken, userAccount, moneyToDepositTransfer);

        // 2. Выполняем запрос по переводу и сохраняем сообщение об ошибке
        //тест падает, т.к. ожидается 400, а отдаётся 200. Писал об этом ранее
        final String actualErrorMessage = failedTransferMoneyBetweenAccounts(authUserToken, userAccount, userAccount,
                moneyToDepositTransfer, ResponseSpecs.requestReturnsBadRequest());

        // 3. Проверяем сообщение об ошибке
        softly.assertThat(actualErrorMessage).isEqualTo(OPERATION_IS_FORBIDDEN);

        // 4. Проверяем баланс пользователя
        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(moneyToDepositTransfer);

    }

    @Test
    @DisplayName("Негативный тест: пользователь не может переводить деньги на несуществующий аккаунт")
    public void userCannotTransferMoneyFromToNonExistedAccount() {

        double moneyToTransfer = RandomData.getMoney();

        // 1. Получаем несуществующий аккаунт
        nonExistingAccount = getMaxExistedAccountId() + 1;

        // 2. Выполняем запрос по переводу и сохраняем сообщение об ошибке
        final String actualErrorMessage = failedTransferMoneyBetweenAccounts(authUserToken, userAccount, nonExistingAccount,
                moneyToTransfer, ResponseSpecs.requestReturnsBadRequest());

        // 3. Проверяем сообщение об ошибке
        softly.assertThat(actualErrorMessage).isEqualTo(INVALID_TRANSFER_INSUFFICIENT_FUNDS_OR_INVALID_ACCOUNTS.getValue());

        // 4. Проверяем баланс пользователя
        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(DEFAULT_ZERO_BALANCE);

        // 5. Проверяем транзакции аккаунта пользователя
        checkNegativeUserTransactions(authUserToken, userAccount, Operations.TRANSFER_IN);

    }

    @Test
    @DisplayName("Негативный тест: пользователь не может переводить деньги с несуществующего аккаунта")
    public void userCannotTransferMoneyFromNonExistedAccount() {

        double moneyToTransfer = RandomData.getMoney();

        // 1. Получаем несуществующий аккаунт
        nonExistingAccount = getMaxExistedAccountId() + 1;

        // 2. Выполняем запрос по переводу и сохраняем сообщение об ошибке
        final String actualErrorMessage = failedTransferMoneyBetweenAccounts(authUserToken, nonExistingAccount,
                userAccount, moneyToTransfer, ResponseSpecs.requestReturnsForbidden());

        // 3. Проверяем сообщение об ошибке
        softly.assertThat(actualErrorMessage).isEqualTo(UNAUTHORIZED_ACCESS_TO_ACCOUNT.getValue());

        // 4. Проверяем баланс пользователя
        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(DEFAULT_ZERO_BALANCE);

        // 5. Проверяем транзакции аккаунта пользователя
        checkNegativeUserTransactions(authUserToken, userAccount, Operations.TRANSFER_IN);
    }
}
