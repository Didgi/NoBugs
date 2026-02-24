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

public class DepositMoney extends BaseTest {

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
    public void userCanDepositHisAccount(Number incomingMoney, Number expectedBalance) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", userAccount);
        jsonObject.put("balance", incomingMoney);

        given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .headers("Authorization", authUserToken)
                .body(jsonObject.toString())
                .post(ACCOUNTS_DEPOSIT_PATH)
                .then()
                .statusCode(SC_OK);

        Assertions.assertEquals(expectedBalance, getUserBalance(authUserToken, userAccount));

        final List<Map<String, Object>> userTransactions = getUserTransactions(authUserToken, userAccount);

        Assertions.assertEquals(expectedBalance.toString(), userTransactions.get(0).get("amount").toString());
        Assertions.assertEquals("DEPOSIT", userTransactions.get(0).get("type"));

    }

    @Test
    @DisplayName("Позитивный тест: пользователь может положить деньги на свой аккаунт несколько раз с общей суммой больше 5000")
    public void userCanDepositMoneyIntoHisAccountSeveralTimesWithCommonAmountMore5000() {

        final Double firstDepositValue = 4999.99;
        final Double secondDepositValue = 100.00;
        final Double totalExpectedBalance = firstDepositValue + secondDepositValue;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", userAccount);
        jsonObject.put("balance", firstDepositValue);

        given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .headers("Authorization", authUserToken)
                .body(jsonObject.toString())
                .post(ACCOUNTS_DEPOSIT_PATH)
                .then()
                .statusCode(SC_OK);

        Assertions.assertEquals(firstDepositValue, getUserBalance(authUserToken, userAccount));

        final List<Map<String, Object>> userTransactions = getUserTransactions(authUserToken, userAccount);

        Assertions.assertEquals(firstDepositValue.toString(), userTransactions.get(0).get("amount").toString());
        Assertions.assertEquals("DEPOSIT", userTransactions.get(0).get("type"));

        jsonObject.remove("balance");
        jsonObject.put("balance", secondDepositValue);

        given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .headers("Authorization", authUserToken)
                .body(jsonObject.toString())
                .post(ACCOUNTS_DEPOSIT_PATH)
                .then()
                .statusCode(SC_OK);

        Assertions.assertEquals(totalExpectedBalance, getUserBalance(authUserToken, userAccount));

        final List<Map<String, Object>> userTransactionsSecondDeposit = getUserTransactions(authUserToken, userAccount);
        final Optional<Map<String, Object>> maxTransactionId = userTransactionsSecondDeposit
                .stream()
                .max(Comparator.comparing(transactions -> (Integer) transactions
                        .get("id")));
        if (maxTransactionId.isPresent()) {
            Assertions.assertEquals(secondDepositValue.toString(), maxTransactionId.get().get("amount").toString());
            Assertions.assertEquals("DEPOSIT", maxTransactionId.get().get("type"));
        }

    }

    @Test
    @DisplayName("Позитивный тест: пользователь может положить деньги на свои любые аккаунты")
    public void userCanDepositMoneyIntoHisAccounts() {
        final Double firstDepositValue = 4999.99;
        final Double secondDepositValue = 0.02;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", userAccount);
        jsonObject.put("balance", firstDepositValue);

        given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .headers("Authorization", authUserToken)
                .body(jsonObject.toString())
                .post(ACCOUNTS_DEPOSIT_PATH)
                .then()
                .statusCode(SC_OK);

        final int userAccountSecond = createUserAccount(authUserToken);

        JSONObject jsonObjectSecond = new JSONObject();
        jsonObjectSecond.put("id", userAccountSecond);
        jsonObjectSecond.put("balance", secondDepositValue);

        given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .headers("Authorization", authUserToken)
                .body(jsonObjectSecond.toString())
                .post(ACCOUNTS_DEPOSIT_PATH)
                .then()
                .statusCode(SC_OK);

        Assertions.assertEquals(secondDepositValue, getUserBalance(authUserToken, userAccountSecond));
        final List<Map<String, Object>> userTransactionsSecond = getUserTransactions(authUserToken, userAccountSecond);
        Assertions.assertEquals(secondDepositValue.toString(), userTransactionsSecond.get(0).get("amount").toString());
        Assertions.assertEquals("DEPOSIT", userTransactionsSecond.get(0).get("type"));


        Assertions.assertEquals(firstDepositValue, getUserBalance(authUserToken, userAccount));
        final List<Map<String, Object>> userTransactions = getUserTransactions(authUserToken, userAccount);
        Assertions.assertEquals(firstDepositValue.toString(), userTransactions.get(0).get("amount").toString());
        Assertions.assertEquals("DEPOSIT", userTransactions.get(0).get("type"));

    }

    private static Stream<Arguments> diffNegativeValue() {
        return Stream.of(
                Arguments.of(-0.01, 0.0, "Deposit amount must be at least 0.01"),
                Arguments.of(0.0, 0.0, "Deposit amount must be at least 0.01"));
    }

    @MethodSource("diffNegativeValue")
    @ParameterizedTest
    @DisplayName("Негативный тест: пользователь не может пополнить свой аккаунт суммой меньше 0.01")
    public void userCannotDepositHisAccountMoneyLessThanMiniumLimit(Number incomingMoney, Number expectedBalance, String errorMessage) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", userAccount);
        jsonObject.put("balance", incomingMoney);

        given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .headers("Authorization", authUserToken)
                .body(jsonObject.toString())
                .post(ACCOUNTS_DEPOSIT_PATH)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body(equalTo(errorMessage));

        Assertions.assertEquals(expectedBalance, getUserBalance(authUserToken, userAccount));

        final List<Map<String, Object>> userTransactions = getUserTransactions(authUserToken, userAccount);
        Assertions.assertTrue(userTransactions.isEmpty());
    }

    @Test
    @DisplayName("Негативный тест: пользователь не может пополнить свой аккаунт суммой больше 5000")
    public void userCannotDepositHisAccountMoneyMoreThanMaxumumValue5000() {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", userAccount);
        jsonObject.put("balance", 5000.01);

        given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .headers("Authorization", authUserToken)
                .body(jsonObject.toString())
                .post(ACCOUNTS_DEPOSIT_PATH)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body(equalTo("Deposit amount cannot exceed 5000"));

        Assertions.assertEquals(0.0, getUserBalance(authUserToken, userAccount));

        final List<Map<String, Object>> userTransactions = getUserTransactions(authUserToken, userAccount);
        Assertions.assertTrue(userTransactions.isEmpty());

    }

    @Test
    @DisplayName("Негативный тест: пользователь не может положить деньги на чужой аккаунт")
    public void userCannotDepositMoneyIntoSomeElseAccount() {

        final String authTokenUserSecond = createUserAndGetToken();
        final int secondUserAccount = createUserAccount(authTokenUserSecond);

        final Double depositValue = 4999.99;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", secondUserAccount);
        jsonObject.put("balance", depositValue);

        given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .headers("Authorization", authUserToken)
                .body(jsonObject.toString())
                .post(ACCOUNTS_DEPOSIT_PATH)
                .then()
                .statusCode(SC_FORBIDDEN)
                .body(equalTo("Unauthorized access to account"));

        Assertions.assertEquals(0.0, getUserBalance(authUserToken, userAccount));

        final List<Map<String, Object>> userTransactions = getUserTransactions(authUserToken, userAccount);
        Assertions.assertTrue(userTransactions.isEmpty());

        Assertions.assertEquals(0.0, getUserBalance(authTokenUserSecond, secondUserAccount));

    }

    @Test
    @DisplayName("Негативный тест: пользователь при попытке положить деньги на несуществующий аккаунт не пополняет свой счёт")
    public void userCannotDepositIntoNonExistedAccount() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", 99999999);
        jsonObject.put("balance", 100);

        given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .headers("Authorization", authUserToken)
                .body(jsonObject.toString())
                .post(ACCOUNTS_DEPOSIT_PATH)
                .then()
                .statusCode(SC_FORBIDDEN)
                .body(equalTo("Unauthorized access to account"));
        Assertions.assertEquals(0.0, getUserBalance(authUserToken, userAccount));
    }
}
