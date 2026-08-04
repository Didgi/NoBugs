package api_tests;

import api.config.Operations;
import api.config.ResponseMessages;
import api.config.TransactionFraudCheckDecision;
import api.config.TransactionStatus;
import api.dao.jdbc.AccountsDao;
import api.models.TransferFraudCheckResponse;
import api.requests.steps.db_steps.DBSteps;
import api.requests.steps.user_steps.UserSteps;
import api.utils.RandomData;
import common.annotations.Bug;
import common.annotations.FraudCheckMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static api.requests.steps.admin_steps.AdminSteps.createUserAndGetToken;
import static api.requests.steps.user_steps.UserSteps.*;

@Execution(ExecutionMode.SAME_THREAD)
public class TransferMoneyWithFraudChecksTests extends BaseTestSenior {

    private ZonedDateTime nowTime;
    private boolean fraudCheckRequiredFalse = false;
    private boolean fraudCheckRequiredTrue = true;


    @Test
    @Bug(value = true)
    @FraudCheckMock(
            decision = TransactionFraudCheckDecision.APPROVED
    )
    @DisplayName("Позитивный тест: при переводе денег проверка транзакции на мошенничество выполнена с автоматическим " +
            "подтверждением транзакции")
    public void userCanTransferMoneyWithApprovedFraudCheckTransaction() throws SQLException {

        double moneyToDeposit = RandomData.getMoneyFromTo(1000, 2000);
        double moneyToTransfer = RandomData.getMoneyFromTo(1, 1000);

        int repeatDepositTimes = 2;
        UserSteps.repeatAction(repeatDepositTimes, () -> depositMoneyWOCheckResponse(authUserToken, userAccount, moneyToDeposit));

        final String authTokenUserSecond = createUserAndGetToken();

        final int secondUserAccount = createUserAccount(authTokenUserSecond);

        nowTime = ZonedDateTime.now(ZoneOffset.UTC);

        final TransferFraudCheckResponse transferResponse =
                successfulTransferMoneyBetweenAccountsWithFraudCheck(authUserToken, userAccount, secondUserAccount, moneyToTransfer);

        softly.assertThat(transferResponse.getMessage()).isEqualTo(ResponseMessages.TRANSFER_APPROVED.getValue());

        final double expectedBalanceForFirstUserRawValue =
                Math.round((moneyToDeposit * repeatDepositTimes - moneyToTransfer) * 100D) / 100D;
        BigDecimal expectedBalanceForFirstUser =
                BigDecimal.valueOf(expectedBalanceForFirstUserRawValue).setScale(2, RoundingMode.HALF_UP);

        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(expectedBalanceForFirstUser.doubleValue());

        final AccountsDao firstUserAccountJDBC = DBSteps.getAccountByAccountIdJDBC(userAccount);

        softly.assertThat(firstUserAccountJDBC.getBalance()).isEqualTo(expectedBalanceForFirstUser.doubleValue());

        UserSteps.checkUserTransactions(authUserToken, userAccount, secondUserAccount, nowTime, Operations.TRANSFER_OUT,
                moneyToTransfer, TransactionStatus.COMPLETED, fraudCheckRequiredFalse);

        UserSteps.checkUserTransactionsDb(userAccount, secondUserAccount, nowTime, Operations.TRANSFER_OUT.name(),
                moneyToTransfer, TransactionStatus.COMPLETED, fraudCheckRequiredFalse);

        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccount)).isEqualTo(moneyToTransfer);

        final AccountsDao secondUserAccountJDBC = DBSteps.getAccountByAccountIdJDBC(secondUserAccount);

        softly.assertThat(secondUserAccountJDBC.getBalance()).isEqualTo(moneyToTransfer);

    }

    @Test
    @Bug(value = true)
    @FraudCheckMock(
            decision = TransactionFraudCheckDecision.BLOCKED
    )
    @DisplayName("Негативный тест: при переводе денег проверка транзакции на мошенничество выполнена с блокировкой транзакции")
    public void userCannotTransferMoneyWhenTransactionIsBlocked() throws SQLException {

        double moneyToDeposit = RandomData.getMoneyFromTo(1000, 2000);
        double moneyToTransfer = RandomData.getMoneyFromTo(1, 1000);

        depositMoneyWOCheckResponse(authUserToken, userAccount, moneyToDeposit);

        final String authTokenUserSecond = createUserAndGetToken();

        final int secondUserAccount = createUserAccount(authTokenUserSecond);

        nowTime = ZonedDateTime.now(ZoneOffset.UTC);

        final TransferFraudCheckResponse transferResponse =
                successfulTransferMoneyBetweenAccountsWithFraudCheck(authUserToken, userAccount, secondUserAccount, moneyToTransfer);

        softly.assertThat(transferResponse.getMessage()).isEqualTo(ResponseMessages.TRANSFER_BLOCKED.getValue());

        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(moneyToDeposit);

        final AccountsDao firstUserAccountJDBC = DBSteps.getAccountByAccountIdJDBC(userAccount);

        softly.assertThat(firstUserAccountJDBC.getBalance()).isEqualTo(moneyToDeposit);

        UserSteps.checkUserTransactions(authUserToken, userAccount, secondUserAccount, nowTime,
                Operations.TRANSFER_OUT, moneyToTransfer, TransactionStatus.BLOCKED, fraudCheckRequiredFalse);

        UserSteps.checkUserTransactionsDb(userAccount, secondUserAccount, nowTime, Operations.TRANSFER_OUT.name(),
                moneyToTransfer, TransactionStatus.BLOCKED, fraudCheckRequiredFalse);

        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccount)).isEqualTo(DEFAULT_ZERO_BALANCE);

        final AccountsDao secondUserAccountJDBC = DBSteps.getAccountByAccountIdJDBC(secondUserAccount);

        softly.assertThat(secondUserAccountJDBC.getBalance()).isEqualTo(DEFAULT_ZERO_BALANCE);

        checkNegativeUserTransactions(authTokenUserSecond, secondUserAccount, Operations.TRANSFER_IN);

    }

    @Test
    @Bug(value = true)
    @FraudCheckMock(
            decision = TransactionFraudCheckDecision.REVIEW_REQUIRED
    )
    @DisplayName("Позитивный тест: при переводе денег во время проверки на мошенничество транзакция " +
            " подтверждена после требования об обязательном ревью")
    public void userCanTransferMoneyWithApproveReviewRequired() throws SQLException {

        double moneyToDeposit = RandomData.getMoneyFromTo(1000, 2000);
        double moneyToTransfer = RandomData.getMoneyFromTo(1, 1000);

        depositMoneyWOCheckResponse(authUserToken, userAccount, moneyToDeposit);

        final String authTokenUserSecond = createUserAndGetToken();

        final int secondUserAccount = createUserAccount(authTokenUserSecond);

        nowTime = ZonedDateTime.now(ZoneOffset.UTC);

        final TransferFraudCheckResponse transferResponse =
                successfulTransferMoneyBetweenAccountsWithFraudCheck(authUserToken, userAccount, secondUserAccount, moneyToTransfer);

        softly.assertThat(transferResponse.getMessage()).isEqualTo(ResponseMessages.TRANSFER_REQUIRES_MANUAL_REVIEW.getValue());

        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(moneyToDeposit);

        final AccountsDao firstUserAccountJDBC = DBSteps.getAccountByAccountIdJDBC(userAccount);

        softly.assertThat(firstUserAccountJDBC.getBalance()).isEqualTo(moneyToDeposit);

        UserSteps.checkUserTransactions(authUserToken, userAccount, secondUserAccount, nowTime,
                Operations.TRANSFER_OUT, moneyToTransfer, TransactionStatus.PENDING, fraudCheckRequiredTrue);

        UserSteps.checkUserTransactionsDb(userAccount, secondUserAccount, nowTime, Operations.TRANSFER_OUT.name(),
                moneyToTransfer, TransactionStatus.PENDING, fraudCheckRequiredTrue);

        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccount)).isEqualTo(DEFAULT_ZERO_BALANCE);

        AccountsDao secondUserAccountJDBC = DBSteps.getAccountByAccountIdJDBC(secondUserAccount);

        softly.assertThat(secondUserAccountJDBC.getBalance()).isEqualTo(DEFAULT_ZERO_BALANCE);

        checkNegativeUserTransactions(authTokenUserSecond, secondUserAccount, Operations.TRANSFER_IN);

        transferComplete(authUserToken, transferResponse.getTransactionId());

        UserSteps.checkUserTransactions(authUserToken, userAccount, secondUserAccount, nowTime,
                Operations.TRANSFER_OUT, moneyToTransfer, TransactionStatus.COMPLETED, fraudCheckRequiredFalse);

        UserSteps.checkUserTransactionsDb(userAccount, secondUserAccount, nowTime, Operations.TRANSFER_OUT.name(),
                moneyToTransfer, TransactionStatus.COMPLETED, fraudCheckRequiredFalse);

        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccount)).isEqualTo(moneyToTransfer);

        secondUserAccountJDBC = DBSteps.getAccountByAccountIdJDBC(secondUserAccount);

        softly.assertThat(secondUserAccountJDBC.getBalance()).isEqualTo(moneyToTransfer);
    }

    @Test
    @Bug(value = true)
    @FraudCheckMock(
            requiresManualReview = true
    )
    @DisplayName("Позитивный тест: при переводе денег во время проверки на мошенничество транзакция " +
            " подтверждена после требования об обязательном ручном ревью")
    public void userCanTransferMoneyWithApproveManualReviewRequired() throws SQLException {

        double moneyToDeposit = RandomData.getMoneyFromTo(1000, 2000);
        double moneyToTransfer = RandomData.getMoneyFromTo(1, 1000);

        depositMoneyWOCheckResponse(authUserToken, userAccount, moneyToDeposit);

        final String authTokenUserSecond = createUserAndGetToken();

        final int secondUserAccount = createUserAccount(authTokenUserSecond);

        nowTime = ZonedDateTime.now(ZoneOffset.UTC);

        final TransferFraudCheckResponse transferResponse =
                successfulTransferMoneyBetweenAccountsWithFraudCheck(authUserToken, userAccount, secondUserAccount, moneyToTransfer);

        softly.assertThat(transferResponse.getMessage()).isEqualTo(ResponseMessages.TRANSFER_REQUIRES_MANUAL_REVIEW.getValue());

        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(moneyToDeposit);

        final AccountsDao firstUserAccountJDBC = DBSteps.getAccountByAccountIdJDBC(userAccount);

        softly.assertThat(firstUserAccountJDBC.getBalance()).isEqualTo(moneyToDeposit);

        UserSteps.checkUserTransactions(authUserToken, userAccount, secondUserAccount, nowTime,
                Operations.TRANSFER_OUT, moneyToTransfer, TransactionStatus.PENDING, fraudCheckRequiredTrue);

        UserSteps.checkUserTransactionsDb(userAccount, secondUserAccount, nowTime, Operations.TRANSFER_OUT.name(),
                moneyToTransfer, TransactionStatus.PENDING, fraudCheckRequiredTrue);

        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccount)).isEqualTo(DEFAULT_ZERO_BALANCE);

        AccountsDao secondUserAccountJDBC = DBSteps.getAccountByAccountIdJDBC(secondUserAccount);

        softly.assertThat(secondUserAccountJDBC.getBalance()).isEqualTo(DEFAULT_ZERO_BALANCE);

        checkNegativeUserTransactions(authTokenUserSecond, secondUserAccount, Operations.TRANSFER_IN);

        transferComplete(authUserToken, transferResponse.getTransactionId());

        UserSteps.checkUserTransactions(authUserToken, userAccount, secondUserAccount, nowTime,
                Operations.TRANSFER_OUT, moneyToTransfer, TransactionStatus.COMPLETED, fraudCheckRequiredFalse);

        UserSteps.checkUserTransactionsDb(userAccount, secondUserAccount, nowTime, Operations.TRANSFER_OUT.name(),
                moneyToTransfer, TransactionStatus.COMPLETED, fraudCheckRequiredFalse);

        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccount)).isEqualTo(moneyToTransfer);

        secondUserAccountJDBC = DBSteps.getAccountByAccountIdJDBC(secondUserAccount);

        softly.assertThat(secondUserAccountJDBC.getBalance()).isEqualTo(moneyToTransfer);
    }

    @Test
    @Bug(value = true)
    @FraudCheckMock(
            decision = TransactionFraudCheckDecision.VERIFICATION_REQUIRED
    )
    @DisplayName("Позитивный тест: при переводе денег во время проверки на мошенничество транзакция " +
            " подтверждена после требования об обязательной верификации")
    public void userCanTransferMoneyWithApproveVerificationRequired() throws SQLException {

        double moneyToDeposit = RandomData.getMoneyFromTo(1000, 2000);
        double moneyToTransfer = RandomData.getMoneyFromTo(1, 1000);

        depositMoneyWOCheckResponse(authUserToken, userAccount, moneyToDeposit);

        final String authTokenUserSecond = createUserAndGetToken();

        final int secondUserAccount = createUserAccount(authTokenUserSecond);

        nowTime = ZonedDateTime.now(ZoneOffset.UTC);

        final TransferFraudCheckResponse transferResponse =
                successfulTransferMoneyBetweenAccountsWithFraudCheck(authUserToken, userAccount, secondUserAccount, moneyToTransfer);

        softly.assertThat(transferResponse.getMessage()).isEqualTo(ResponseMessages.TRANSFER_ADDITIONAL_VERIFICATION_REQUIRED.getValue());

        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(moneyToDeposit);

        final AccountsDao firstUserAccountJDBC = DBSteps.getAccountByAccountIdJDBC(userAccount);

        softly.assertThat(firstUserAccountJDBC.getBalance()).isEqualTo(moneyToDeposit);

        UserSteps.checkUserTransactions(authUserToken, userAccount, secondUserAccount, nowTime,
                Operations.TRANSFER_OUT, moneyToTransfer, TransactionStatus.PENDING, fraudCheckRequiredTrue);

        UserSteps.checkUserTransactionsDb(userAccount, secondUserAccount, nowTime, Operations.TRANSFER_OUT.name(),
                moneyToTransfer, TransactionStatus.PENDING, fraudCheckRequiredTrue);

        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccount)).isEqualTo(DEFAULT_ZERO_BALANCE);

        AccountsDao secondUserAccountJDBC = DBSteps.getAccountByAccountIdJDBC(secondUserAccount);

        softly.assertThat(secondUserAccountJDBC.getBalance()).isEqualTo(DEFAULT_ZERO_BALANCE);

        checkNegativeUserTransactions(authTokenUserSecond, secondUserAccount, Operations.TRANSFER_IN);

        transferComplete(authUserToken, transferResponse.getTransactionId());

        UserSteps.checkUserTransactions(authUserToken, userAccount, secondUserAccount, nowTime,
                Operations.TRANSFER_OUT, moneyToTransfer, TransactionStatus.COMPLETED, fraudCheckRequiredFalse);

        UserSteps.checkUserTransactionsDb(userAccount, secondUserAccount, nowTime, Operations.TRANSFER_OUT.name(),
                moneyToTransfer, TransactionStatus.COMPLETED, fraudCheckRequiredFalse);

        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccount)).isEqualTo(moneyToTransfer);

        secondUserAccountJDBC = DBSteps.getAccountByAccountIdJDBC(secondUserAccount);

        softly.assertThat(secondUserAccountJDBC.getBalance()).isEqualTo(moneyToTransfer);
    }

    @Test
    @Bug(value = true)
    @FraudCheckMock(
            additionalVerificationRequired = true
    )
    @DisplayName("Позитивный тест: при переводе денег во время проверки на мошенничество транзакция " +
            " подтверждена после требования об дополнительной обязательной верификации")
    public void userCanTransferMoneyWithApproveAdditionalVerificationRequired() throws SQLException {

        double moneyToDeposit = RandomData.getMoneyFromTo(1000, 2000);
        double moneyToTransfer = RandomData.getMoneyFromTo(1, 1000);

        depositMoneyWOCheckResponse(authUserToken, userAccount, moneyToDeposit);

        final String authTokenUserSecond = createUserAndGetToken();

        final int secondUserAccount = createUserAccount(authTokenUserSecond);

        nowTime = ZonedDateTime.now(ZoneOffset.UTC);

        final TransferFraudCheckResponse transferResponse =
                successfulTransferMoneyBetweenAccountsWithFraudCheck(authUserToken, userAccount, secondUserAccount, moneyToTransfer);

        softly.assertThat(transferResponse.getMessage()).isEqualTo(ResponseMessages.TRANSFER_ADDITIONAL_VERIFICATION_REQUIRED.getValue());

        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(moneyToDeposit);

        final AccountsDao firstUserAccountJDBC = DBSteps.getAccountByAccountIdJDBC(userAccount);

        softly.assertThat(firstUserAccountJDBC.getBalance()).isEqualTo(moneyToDeposit);

        UserSteps.checkUserTransactions(authUserToken, userAccount, secondUserAccount, nowTime,
                Operations.TRANSFER_OUT, moneyToTransfer, TransactionStatus.PENDING, fraudCheckRequiredTrue);

        UserSteps.checkUserTransactionsDb(userAccount, secondUserAccount, nowTime, Operations.TRANSFER_OUT.name(),
                moneyToTransfer, TransactionStatus.PENDING, fraudCheckRequiredTrue);

        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccount)).isEqualTo(DEFAULT_ZERO_BALANCE);

        AccountsDao secondUserAccountJDBC = DBSteps.getAccountByAccountIdJDBC(secondUserAccount);

        softly.assertThat(secondUserAccountJDBC.getBalance()).isEqualTo(DEFAULT_ZERO_BALANCE);

        checkNegativeUserTransactions(authTokenUserSecond, secondUserAccount, Operations.TRANSFER_IN);

        transferComplete(authUserToken, transferResponse.getTransactionId());

        UserSteps.checkUserTransactions(authUserToken, userAccount, secondUserAccount, nowTime,
                Operations.TRANSFER_OUT, moneyToTransfer, TransactionStatus.COMPLETED, fraudCheckRequiredFalse);

        UserSteps.checkUserTransactionsDb(userAccount, secondUserAccount, nowTime, Operations.TRANSFER_OUT.name(),
                moneyToTransfer, TransactionStatus.COMPLETED, fraudCheckRequiredFalse);

        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccount)).isEqualTo(moneyToTransfer);

        secondUserAccountJDBC = DBSteps.getAccountByAccountIdJDBC(secondUserAccount);

        softly.assertThat(secondUserAccountJDBC.getBalance()).isEqualTo(moneyToTransfer);

    }

    @Test
    @Bug(value = true)
    @FraudCheckMock(
            badRequest = true
    )
    @DisplayName("Негативный тест: при переводе денег во время проверки на мошенничество запрос упал с 400 ошибкой")
    public void userCanTransferMoneyAfterApproveTransferWhenBadRequestDuringFraudCheck() throws SQLException {

        double moneyToDeposit = RandomData.getMoneyFromTo(1000, 2000);
        double moneyToTransfer = RandomData.getMoneyFromTo(1, 1000);

        depositMoneyWOCheckResponse(authUserToken, userAccount, moneyToDeposit);

        final String authTokenUserSecond = createUserAndGetToken();

        final int secondUserAccount = createUserAccount(authTokenUserSecond);

        nowTime = ZonedDateTime.now(ZoneOffset.UTC);

        final TransferFraudCheckResponse transferResponse =
                successfulTransferMoneyBetweenAccountsWithFraudCheck(authUserToken, userAccount, secondUserAccount, moneyToTransfer);

        softly.assertThat(transferResponse.getMessage()).isEqualTo(ResponseMessages.TRANSFER_REQUIRES_MANUAL_REVIEW.getValue());

        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(moneyToDeposit);

        final AccountsDao firstUserAccountJDBC = DBSteps.getAccountByAccountIdJDBC(userAccount);

        softly.assertThat(firstUserAccountJDBC.getBalance()).isEqualTo(moneyToDeposit);

        UserSteps.checkUserTransactions(authUserToken, userAccount, secondUserAccount, nowTime,
                Operations.TRANSFER_OUT, moneyToTransfer, TransactionStatus.PENDING, fraudCheckRequiredTrue);

        UserSteps.checkUserTransactionsDb(userAccount, secondUserAccount, nowTime, Operations.TRANSFER_OUT.name(),
                moneyToTransfer, TransactionStatus.PENDING, fraudCheckRequiredTrue);

        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccount)).isEqualTo(DEFAULT_ZERO_BALANCE);

        AccountsDao secondUserAccountJDBC = DBSteps.getAccountByAccountIdJDBC(secondUserAccount);

        softly.assertThat(secondUserAccountJDBC.getBalance()).isEqualTo(DEFAULT_ZERO_BALANCE);

        checkNegativeUserTransactions(authTokenUserSecond, secondUserAccount, Operations.TRANSFER_IN);

        transferComplete(authUserToken, transferResponse.getTransactionId());

        UserSteps.checkUserTransactions(authUserToken, userAccount, secondUserAccount, nowTime,
                Operations.TRANSFER_OUT, moneyToTransfer, TransactionStatus.COMPLETED, fraudCheckRequiredFalse);

        UserSteps.checkUserTransactionsDb(userAccount, secondUserAccount, nowTime, Operations.TRANSFER_OUT.name(),
                moneyToTransfer, TransactionStatus.COMPLETED, fraudCheckRequiredFalse);

        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccount)).isEqualTo(moneyToTransfer);

        secondUserAccountJDBC = DBSteps.getAccountByAccountIdJDBC(secondUserAccount);

        softly.assertThat(secondUserAccountJDBC.getBalance()).isEqualTo(moneyToTransfer);
    }

    @Test
    @Bug(value = true)
    @FraudCheckMock(
            internalServerError = true
    )
    @DisplayName("Негативный тест: при переводе денег во время проверки на мошенничество запрос упал с 500 ошибкой")
    public void userCanTransferMoneyAfterApproveTransferWhenInternalServerErrorDuringFraudCheck() throws SQLException {

        double moneyToDeposit = RandomData.getMoneyFromTo(1000, 2000);
        double moneyToTransfer = RandomData.getMoneyFromTo(1, 1000);

        depositMoneyWOCheckResponse(authUserToken, userAccount, moneyToDeposit);

        final String authTokenUserSecond = createUserAndGetToken();

        final int secondUserAccount = createUserAccount(authTokenUserSecond);

        nowTime = ZonedDateTime.now(ZoneOffset.UTC);

        final TransferFraudCheckResponse transferResponse =
                successfulTransferMoneyBetweenAccountsWithFraudCheck(authUserToken, userAccount, secondUserAccount, moneyToTransfer);

        softly.assertThat(transferResponse.getMessage()).isEqualTo(ResponseMessages.TRANSFER_REQUIRES_MANUAL_REVIEW.getValue());

        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(moneyToDeposit);

        final AccountsDao firstUserAccountJDBC = DBSteps.getAccountByAccountIdJDBC(userAccount);

        softly.assertThat(firstUserAccountJDBC.getBalance()).isEqualTo(moneyToDeposit);

        UserSteps.checkUserTransactions(authUserToken, userAccount, secondUserAccount, nowTime,
                Operations.TRANSFER_OUT, moneyToTransfer, TransactionStatus.PENDING, fraudCheckRequiredTrue);

        UserSteps.checkUserTransactionsDb(userAccount, secondUserAccount, nowTime, Operations.TRANSFER_OUT.name(),
                moneyToTransfer, TransactionStatus.PENDING, fraudCheckRequiredTrue);

        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccount)).isEqualTo(DEFAULT_ZERO_BALANCE);

        AccountsDao secondUserAccountJDBC = DBSteps.getAccountByAccountIdJDBC(secondUserAccount);

        softly.assertThat(secondUserAccountJDBC.getBalance()).isEqualTo(DEFAULT_ZERO_BALANCE);

        checkNegativeUserTransactions(authTokenUserSecond, secondUserAccount, Operations.TRANSFER_IN);

        transferComplete(authUserToken, transferResponse.getTransactionId());

        UserSteps.checkUserTransactions(authUserToken, userAccount, secondUserAccount, nowTime,
                Operations.TRANSFER_OUT, moneyToTransfer, TransactionStatus.COMPLETED, fraudCheckRequiredFalse);

        UserSteps.checkUserTransactionsDb(userAccount, secondUserAccount, nowTime, Operations.TRANSFER_OUT.name(),
                moneyToTransfer, TransactionStatus.COMPLETED, fraudCheckRequiredFalse);

        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccount)).isEqualTo(moneyToTransfer);

        secondUserAccountJDBC = DBSteps.getAccountByAccountIdJDBC(secondUserAccount);

        softly.assertThat(secondUserAccountJDBC.getBalance()).isEqualTo(moneyToTransfer);
    }

    @Test
    @Bug(value = true)
    @FraudCheckMock(
            timeout = true,
            decision = TransactionFraudCheckDecision.APPROVED
    )
    @DisplayName("Негативный тест: при переводе денег во время проверки на мошенничество запрос упал по timeout")
    public void userCanTransferMoneyAfterApproveTransferWhenTimeoutDuringFraudCheck() throws SQLException {

        double moneyToDeposit = RandomData.getMoneyFromTo(1000, 2000);
        double moneyToTransfer = RandomData.getMoneyFromTo(1, 1000);

        depositMoneyWOCheckResponse(authUserToken, userAccount, moneyToDeposit);

        final String authTokenUserSecond = createUserAndGetToken();

        final int secondUserAccount = createUserAccount(authTokenUserSecond);

        nowTime = ZonedDateTime.now(ZoneOffset.UTC);

        final TransferFraudCheckResponse transferResponse =
                successfulTransferMoneyBetweenAccountsWithFraudCheck(authUserToken, userAccount, secondUserAccount, moneyToTransfer);

        softly.assertThat(transferResponse.getMessage()).isEqualTo(ResponseMessages.TRANSFER_REQUIRES_MANUAL_REVIEW.getValue());

        softly.assertThat(getUserBalance(authUserToken, userAccount)).isEqualTo(moneyToDeposit);

        final AccountsDao firstUserAccountJDBC = DBSteps.getAccountByAccountIdJDBC(userAccount);

        softly.assertThat(firstUserAccountJDBC.getBalance()).isEqualTo(moneyToDeposit);

        UserSteps.checkUserTransactions(authUserToken, userAccount, secondUserAccount, nowTime,
                Operations.TRANSFER_OUT, moneyToTransfer, TransactionStatus.PENDING, fraudCheckRequiredTrue);

        UserSteps.checkUserTransactionsDb(userAccount, secondUserAccount, nowTime, Operations.TRANSFER_OUT.name(),
                moneyToTransfer, TransactionStatus.PENDING, fraudCheckRequiredTrue);

        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccount)).isEqualTo(DEFAULT_ZERO_BALANCE);

        AccountsDao secondUserAccountJDBC = DBSteps.getAccountByAccountIdJDBC(secondUserAccount);

        softly.assertThat(secondUserAccountJDBC.getBalance()).isEqualTo(DEFAULT_ZERO_BALANCE);

        checkNegativeUserTransactions(authTokenUserSecond, secondUserAccount, Operations.TRANSFER_IN);

        transferComplete(authUserToken, transferResponse.getTransactionId());

        UserSteps.checkUserTransactions(authUserToken, userAccount, secondUserAccount, nowTime,
                Operations.TRANSFER_OUT, moneyToTransfer, TransactionStatus.COMPLETED, fraudCheckRequiredFalse);

        UserSteps.checkUserTransactionsDb(userAccount, secondUserAccount, nowTime, Operations.TRANSFER_OUT.name(),
                moneyToTransfer, TransactionStatus.COMPLETED, fraudCheckRequiredFalse);

        softly.assertThat(getUserBalance(authTokenUserSecond, secondUserAccount)).isEqualTo(moneyToTransfer);

        secondUserAccountJDBC = DBSteps.getAccountByAccountIdJDBC(secondUserAccount);

        softly.assertThat(secondUserAccountJDBC.getBalance()).isEqualTo(moneyToTransfer);
    }
}
