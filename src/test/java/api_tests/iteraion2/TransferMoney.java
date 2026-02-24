package api_tests.iteraion2;

import io.restassured.http.ContentType;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class TransferMoney extends BaseTest {

    private static Stream<Arguments> diffPositiveValue() {
        return Stream.of(
                Arguments.of(0.01, 0.01),
                Arguments.of(2500.0, 2500.0),
                Arguments.of(5000.0, 9999.99),
                Arguments.of(5000.0, 10000.00));
    }


    @MethodSource("diffPositiveValue")
    @ParameterizedTest
    @DisplayName("Позитивный тест: пользователь может переводить деньги на аккаунт другого пользователя")
    public void userCanTransferMoneyToSomeoneElseExistedAccount(Number moneyToDeposit, Number moneyToTransfer) {

        depositMoney(authUserToken, userAccount, moneyToDeposit);
        depositMoney(authUserToken, userAccount, moneyToDeposit);

        final String authTokenUserSecond = createUserAndGetToken();
        final int secondUserAccount = createUserAccount(authTokenUserSecond);

        JSONObject jsonObjectToTransfer = new JSONObject();
        jsonObjectToTransfer.put("senderAccountId", userAccount);
        jsonObjectToTransfer.put("receiverAccountId", secondUserAccount);
        jsonObjectToTransfer.put("amount", moneyToTransfer);


        given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .headers("Authorization", authUserToken)
                .body(jsonObjectToTransfer.toString())
                .post(ACCOUNTS_TRANSFER_PATH)
                .then()
                .statusCode(SC_OK)
                .body("message", equalTo("Transfer successful"));

        final double expectedBalanceForFirstUser = Math.round((moneyToDeposit.doubleValue() * 2 - moneyToTransfer.doubleValue()) * 100D) / 100D;
        Assertions.assertEquals(expectedBalanceForFirstUser, getUserBalance(authUserToken, userAccount));


        final List<Map<String, Object>> userFirstTransactions = getUserTransactions(authUserToken, userAccount);
        final Optional<Map<String, Object>> maxTransactionIdUserFirst = userFirstTransactions
                .stream()
                .max(Comparator.comparing(transactions -> (Integer) transactions
                        .get("id")));
        if (maxTransactionIdUserFirst.isPresent()) {
            Assertions.assertEquals(moneyToTransfer.toString(), maxTransactionIdUserFirst.get().get("amount").toString());
            Assertions.assertEquals("TRANSFER_OUT", maxTransactionIdUserFirst.get().get("type"));
        }

        Assertions.assertEquals(moneyToTransfer, getUserBalance(authTokenUserSecond, secondUserAccount));

        final List<Map<String, Object>> userSecondTransactions = getUserTransactions(authTokenUserSecond, secondUserAccount);
        final Optional<Map<String, Object>> maxTransactionIdUserSecond = userSecondTransactions
                .stream()
                .max(Comparator.comparing(transactions -> (Integer) transactions
                        .get("id")));
        if (maxTransactionIdUserSecond.isPresent()) {
            Assertions.assertEquals(moneyToTransfer.toString(), maxTransactionIdUserSecond.get().get("amount").toString());
            Assertions.assertEquals("TRANSFER_IN", maxTransactionIdUserSecond.get().get("type"));

        }
    }

    private static Stream<Arguments> diffNegativeValue() {
        return Stream.of(
                Arguments.of(0.1, -0.01, "Transfer amount must be at least 0.01"),
                Arguments.of(0.1, 0.0, "Transfer amount must be at least 0.01"));
    }


    @MethodSource("diffNegativeValue")
    @ParameterizedTest
    @DisplayName("Негативный тест: пользователь не может переводить сумму меньше 0.01")
    public void userCannotTransferMoneyToSomeoneElseExistedAccountLessThanMinimumLimit(Number moneyToDeposit, Number moneyToTransfer, String errorMessage) {

        depositMoney(authUserToken, userAccount, moneyToDeposit);

        final String authTokenUserSecond = createUserAndGetToken();
        final int secondUserAccount = createUserAccount(authTokenUserSecond);

        JSONObject jsonObjectToTransfer = new JSONObject();
        jsonObjectToTransfer.put("senderAccountId", userAccount);
        jsonObjectToTransfer.put("receiverAccountId", secondUserAccount);
        jsonObjectToTransfer.put("amount", moneyToTransfer);


        given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .headers("Authorization", authUserToken)
                .body(jsonObjectToTransfer.toString())
                .post(ACCOUNTS_TRANSFER_PATH)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body(equalTo(errorMessage));

        Assertions.assertEquals(moneyToDeposit, getUserBalance(authUserToken, userAccount));

        final List<Map<String, Object>> userFirstTransactions = getUserTransactions(authUserToken, userAccount);
        final Optional<Map<String, Object>> maxTransactionIdUserFirst = userFirstTransactions
                .stream()
                .max(Comparator.comparing(transactions -> (Integer) transactions
                        .get("id")));
        if (maxTransactionIdUserFirst.isPresent()) {
            Assertions.assertFalse(maxTransactionIdUserFirst.get().containsValue("TRANSFER_OUT"));
        }

        Assertions.assertEquals(0.0, getUserBalance(authTokenUserSecond, secondUserAccount));
        final List<Map<String, Object>> userSecondTransactions = getUserTransactions(authTokenUserSecond, secondUserAccount);
        Assertions.assertTrue(userSecondTransactions.isEmpty());
    }


    @Test
    @DisplayName("Негативный тест: пользователь не может переводить сумму больше 10000")
    public void userCannotTransferMoneyToSomeoneElseExistedAccountMoreThanMaximumLimit() {

        int moneyToDeposit = 5000;
        double moneyToTransfer = 10000.01;
        depositMoney(authUserToken, userAccount, moneyToDeposit);
        depositMoney(authUserToken, userAccount, moneyToDeposit);
        depositMoney(authUserToken, userAccount, moneyToDeposit);

        final String authTokenUserSecond = createUserAndGetToken();
        final int secondUserAccount = createUserAccount(authTokenUserSecond);

        JSONObject jsonObjectToTransfer = new JSONObject();
        jsonObjectToTransfer.put("senderAccountId", userAccount);
        jsonObjectToTransfer.put("receiverAccountId", secondUserAccount);
        jsonObjectToTransfer.put("amount", moneyToTransfer);


        given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .headers("Authorization", authUserToken)
                .body(jsonObjectToTransfer.toString())
                .post(ACCOUNTS_TRANSFER_PATH)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body(equalTo("Transfer amount cannot exceed 10000"));

        Assertions.assertEquals(moneyToDeposit * 3, getUserBalance(authUserToken, userAccount));

        final List<Map<String, Object>> userFirstTransactions = getUserTransactions(authUserToken, userAccount);
        final Optional<Map<String, Object>> maxTransactionIdUserFirst = userFirstTransactions
                .stream()
                .max(Comparator.comparing(transactions -> (Integer) transactions
                        .get("id")));
        if (maxTransactionIdUserFirst.isPresent()) {
            Assertions.assertFalse(maxTransactionIdUserFirst.get().containsValue("TRANSFER_OUT"));
        }

        Assertions.assertEquals(0.0, getUserBalance(authTokenUserSecond, secondUserAccount));

        final List<Map<String, Object>> userSecondTransactions = getUserTransactions(authTokenUserSecond, secondUserAccount);
        Assertions.assertTrue(userSecondTransactions.isEmpty());
    }

    @Test
    @DisplayName("Позитивный тест: пользователь может переводить деньги между своими же аккаунтами")
    public void userCanTransferMoneyBetweenHisAccounts() {
        final Double depositTransferMoney = 2500.0;

        depositMoney(authUserToken, userAccount, depositTransferMoney);

        final int userAccountSecond = createUserAccount(authUserToken);

        JSONObject jsonObjectToTransfer = new JSONObject();
        jsonObjectToTransfer.put("senderAccountId", userAccount);
        jsonObjectToTransfer.put("receiverAccountId", userAccountSecond);
        jsonObjectToTransfer.put("amount", depositTransferMoney);

        given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .headers("Authorization", authUserToken)
                .body(jsonObjectToTransfer.toString())
                .post(ACCOUNTS_TRANSFER_PATH)
                .then()
                .statusCode(SC_OK)
                .body("message", equalTo("Transfer successful"));

        Assertions.assertEquals(depositTransferMoney.doubleValue(), getUserBalance(authUserToken, userAccountSecond));
        final List<Map<String, Object>> userTransactionsSecondAccount = getUserTransactions(authUserToken, userAccountSecond);
        Assertions.assertEquals(depositTransferMoney.toString(), userTransactionsSecondAccount.get(0).get("amount").toString());
        Assertions.assertEquals("TRANSFER_IN", userTransactionsSecondAccount.get(0).get("type"));


        final List<Map<String, Object>> userTransactionsFirstAccount = getUserTransactions(authUserToken, userAccount);
        final Optional<Map<String, Object>> maxTransactionIdUserFirst = userTransactionsFirstAccount
                .stream()
                .max(Comparator.comparing(transactions -> (Integer) transactions
                        .get("id")));
        if (maxTransactionIdUserFirst.isPresent()) {
            Assertions.assertTrue(maxTransactionIdUserFirst.get().containsValue("TRANSFER_OUT"));
        }

        Assertions.assertEquals(0.0, getUserBalance(authUserToken, userAccount));

    }

    @Test
    @DisplayName("Позитивный тест: при переводе пользователем денег пользователю на один из двух аккаунтов, " +
            "другой аккаунт не пополняется")
    public void UserCanTransferMoneyToSomeoneElseUserWithTwoExistedAccounts() {

        final Double depositTransferMoney = 0.01;

        depositMoney(authUserToken, userAccount, depositTransferMoney);

        final String authTokenUserSecond = createUserAndGetToken();
        final int secondUserAccountFirst = createUserAccount(authTokenUserSecond);
        final int secondUserAccountSecond = createUserAccount(authTokenUserSecond);

        JSONObject jsonObjectToTransfer = new JSONObject();
        jsonObjectToTransfer.put("senderAccountId", userAccount);
        jsonObjectToTransfer.put("receiverAccountId", secondUserAccountSecond);
        jsonObjectToTransfer.put("amount", depositTransferMoney);


        given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .headers("Authorization", authUserToken)
                .body(jsonObjectToTransfer.toString())
                .post(ACCOUNTS_TRANSFER_PATH)
                .then()
                .statusCode(SC_OK)
                .body("message", equalTo("Transfer successful"));

        Assertions.assertEquals(0.0, getUserBalance(authUserToken, userAccount));

        final List<Map<String, Object>> userFirstTransactions = getUserTransactions(authUserToken, userAccount);
        final Optional<Map<String, Object>> maxTransactionIdUserFirst = userFirstTransactions
                .stream()
                .max(Comparator.comparing(transactions -> (Integer) transactions
                        .get("id")));
        if (maxTransactionIdUserFirst.isPresent()) {
            Assertions.assertEquals(depositTransferMoney.toString(), maxTransactionIdUserFirst.get().get("amount").toString());
            Assertions.assertEquals("TRANSFER_OUT", maxTransactionIdUserFirst.get().get("type"));
        }

        Assertions.assertEquals(depositTransferMoney, getUserBalance(authTokenUserSecond, secondUserAccountSecond));

        final List<Map<String, Object>> userSecondTransactionsSecondAccount = getUserTransactions(authTokenUserSecond, secondUserAccountSecond);
        final Optional<Map<String, Object>> maxTransactionIdUserSecond = userSecondTransactionsSecondAccount
                .stream()
                .max(Comparator.comparing(transactions -> (Integer) transactions
                        .get("id")));
        if (maxTransactionIdUserSecond.isPresent()) {
            Assertions.assertEquals(depositTransferMoney.toString(), maxTransactionIdUserSecond.get().get("amount").toString());
            Assertions.assertEquals("TRANSFER_IN", maxTransactionIdUserSecond.get().get("type"));

        }

        Assertions.assertEquals(0.0, getUserBalance(authTokenUserSecond, secondUserAccountFirst));

        final List<Map<String, Object>> userSecondTransactionsFirstAccount = getUserTransactions(authTokenUserSecond, secondUserAccountFirst);
        Assertions.assertTrue(userSecondTransactionsFirstAccount.isEmpty());

    }

    @Test
    @DisplayName("Негативный тест: пользователь не может переводить деньги при нулевом балансе")
    public void userCannotTransferMoneyToSomeoneElseExistedAccountWhenHisAccountBalanceIsZero() {

        final String authTokenUserSecond = createUserAndGetToken();
        final int secondUserAccount = createUserAccount(authTokenUserSecond);

        JSONObject jsonObjectToTransfer = new JSONObject();
        jsonObjectToTransfer.put("senderAccountId", userAccount);
        jsonObjectToTransfer.put("receiverAccountId", secondUserAccount);
        jsonObjectToTransfer.put("amount", 10);

        given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .headers("Authorization", authUserToken)
                .body(jsonObjectToTransfer.toString())
                .post(ACCOUNTS_TRANSFER_PATH)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body(equalTo("Invalid transfer: insufficient funds or invalid accounts"));

        final List<Map<String, Object>> userFirstTransactions = getUserTransactions(authUserToken, userAccount);
        Assertions.assertTrue(userFirstTransactions.isEmpty());

        Assertions.assertEquals(0.0, getUserBalance(authTokenUserSecond, secondUserAccount));

        final List<Map<String, Object>> userSecondTransactions = getUserTransactions(authTokenUserSecond, secondUserAccount);
        Assertions.assertTrue(userSecondTransactions.isEmpty());
    }


    @Test
    @DisplayName("Негативный тест: пользователь не может переводить деньги с чужого аккаунта на свой")
    public void userCannotTransferMoneyFromSomeoneElseExistedAccountToHisOwn() {
        int moneyToDepositTransfer = 5000;

        depositMoney(authUserToken, userAccount, moneyToDepositTransfer);

        final String authTokenUserSecond = createUserAndGetToken();
        final int secondUserAccount = createUserAccount(authTokenUserSecond);

        depositMoney(authTokenUserSecond, secondUserAccount, moneyToDepositTransfer);

        JSONObject jsonObjectToTransfer = new JSONObject();
        jsonObjectToTransfer.put("senderAccountId", secondUserAccount);
        jsonObjectToTransfer.put("receiverAccountId", userAccount);
        jsonObjectToTransfer.put("amount", moneyToDepositTransfer);


        given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .headers("Authorization", authUserToken)
                .body(jsonObjectToTransfer.toString())
                .post(ACCOUNTS_TRANSFER_PATH)
                .then()
                .statusCode(SC_FORBIDDEN)
                .body(equalTo("Unauthorized access to account"));

        Assertions.assertEquals(moneyToDepositTransfer, getUserBalance(authUserToken, userAccount));
        Assertions.assertEquals(moneyToDepositTransfer, getUserBalance(authTokenUserSecond, secondUserAccount));
    }

    @Test
    @DisplayName("Негативный тест: пользователь не может переводить деньги с/на один и тот же свой аккаунт")
    public void userCannotTransferMoneyFromToSameHisAccount() {
        int moneyToDepositTransfer = 5000;
        depositMoney(authUserToken, userAccount, moneyToDepositTransfer);

        JSONObject jsonObjectToTransfer = new JSONObject();
        jsonObjectToTransfer.put("senderAccountId", userAccount);
        jsonObjectToTransfer.put("receiverAccountId", userAccount);
        jsonObjectToTransfer.put("amount", moneyToDepositTransfer);


        given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .headers("Authorization", authUserToken)
                .body(jsonObjectToTransfer.toString())
                .post(ACCOUNTS_TRANSFER_PATH)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body(equalTo("Forbidden operation")); //уточнить в требованиях данный момент

        Assertions.assertEquals(moneyToDepositTransfer, getUserBalance(authUserToken, userAccount));
    }

    private static Stream<Arguments> diffNonExistedAccountsValue() {

        return Stream.of(
                Arguments.of(2500, userAccount, 99999999, "Unauthorized access to account"),
                Arguments.of(2500, 99999999, userAccount, "Unauthorized access to account"));
    }


    @MethodSource("diffNonExistedAccountsValue")
    @ParameterizedTest
    @DisplayName("Негативный тест: пользователь не может переводить деньги с/на несуществующий аккаунт")
    public void userCannotTransferMoneyFromToNonExistedAccount(Number moneyToDepositTransfer, int transferFromAccount, int transferToAccount, String errorMessage) {

        depositMoney(authUserToken, userAccount, moneyToDepositTransfer);

        JSONObject jsonObjectToTransfer = new JSONObject();
        jsonObjectToTransfer.put("senderAccountId", transferFromAccount);
        jsonObjectToTransfer.put("receiverAccountId", transferToAccount);
        jsonObjectToTransfer.put("amount", moneyToDepositTransfer);


        given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .headers("Authorization", authUserToken)
                .body(jsonObjectToTransfer.toString())
                .post(ACCOUNTS_TRANSFER_PATH)
                .then()
                .statusCode(SC_FORBIDDEN)
                .body(equalTo(errorMessage));

        Assertions.assertEquals(moneyToDepositTransfer.doubleValue(), getUserBalance(authUserToken, userAccount));

        final List<Map<String, Object>> userFirstTransactions = getUserTransactions(authUserToken, userAccount);
        final Optional<Map<String, Object>> maxTransactionIdUserFirst = userFirstTransactions
                .stream()
                .max(Comparator.comparing(transactions -> (Integer) transactions
                        .get("id")));
        if (maxTransactionIdUserFirst.isPresent()) {
            Assertions.assertFalse(maxTransactionIdUserFirst.get().containsValue("TRANSFER_OUT"));
        }
    }

}
