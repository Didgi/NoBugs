package api_tests.iteraion2;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;

public class BaseTest {
    protected final static String AUTH_LOGIN_PATH = "auth/login";
    protected final static String ADMIN_USERS_PATH = "admin/users";
    protected final static String ACCOUNTS_PATH = "accounts";
    protected final static String CUSTOMER_ACCOUNTS_PATH = "customer/accounts";
    protected final static String ACCOUNTS_DEPOSIT_PATH = "accounts/deposit";
    protected final static String ACCOUNTS_TRANSFER_PATH = "accounts/transfer";
    protected final static String CUSTOMER_PROFILE_PATH = "customer/profile";
    protected static String authAdminToken;
    protected static String authUserToken;
    protected static int userAccount;
    protected String username;
    protected String password;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost:4111/api/v1/";
        RestAssured.filters(List.of(new RequestLoggingFilter(), new ResponseLoggingFilter()));
        getAdminToken();
        deleteUserById();
        authUserToken = createUserAndGetToken();
        userAccount = createUserAccount(authUserToken);
    }

    @AfterEach
    public void cleanUp() {
        deleteUserAccounts(authUserToken);
        deleteUserById();
    }

    private void getAdminToken() {
        authAdminToken = given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .body("""
                        {
                          "username": "admin",
                          "password": "admin"
                        }
                        """)
                .post(AUTH_LOGIN_PATH)
                .then()
                .statusCode(SC_OK)
                .extract()
                .header("Authorization");
    }

    protected String createUserAndGetToken() {
        int index = new Random().nextInt(15);
        username = "user-" + index;
        password = "Password!-" + index;
        JSONObject requestBody = new JSONObject();
        requestBody.put("username", username);
        requestBody.put("password", password);
        requestBody.put("role", "USER");

        given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .headers("Authorization", authAdminToken)
                .body(requestBody.toString())
                .post(ADMIN_USERS_PATH)
                .then()
                .statusCode(SC_CREATED)
                .assertThat()
                .body("username", Matchers.equalTo(username));

        requestBody.remove("role");

        return given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .body(requestBody.toString())
                .post(AUTH_LOGIN_PATH)
                .then()
                .statusCode(SC_OK)
                .extract()
                .header("Authorization");
    }

    protected int createUserAccount(String userToken) {
        return given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .header("Authorization", userToken)
                .post(ACCOUNTS_PATH)
                .then()
                .statusCode(SC_CREATED)
                .extract()
                .jsonPath()
                .get("id");
    }

    protected static List<Integer> getUsersId() {
        return given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .headers("Authorization", authAdminToken)
                .get(ADMIN_USERS_PATH)
                .then()
                .statusCode(SC_OK)
                .assertThat()
                .extract()
                .jsonPath()
                .getList("id");

    }

    protected static void deleteUserById() {
        final List<Integer> usersId = getUsersId();
        usersId.forEach(id -> {
            given()
                    .headers("Authorization", authAdminToken)
                    .delete(ADMIN_USERS_PATH + "/" + id)
                    .then()
                    .assertThat()
                    .statusCode(SC_OK);
        });

    }

    protected List<Integer> getUserAccount(String userToken) {
        return given()
                .headers("Authorization", userToken)
                .get(CUSTOMER_ACCOUNTS_PATH)
                .then()
                .statusCode(SC_OK)
                .assertThat()
                .extract()
                .jsonPath()
                .getList("id");

    }

    protected void deleteUserAccounts(String userToken) {

        getUserAccount(userToken).forEach(integer -> {
            given()
                    .headers("Authorization", userToken)
                    .delete(ACCOUNTS_PATH + "/" + integer)
                    .then()
                    .statusCode(SC_OK);
        });

    }

    protected double getUserBalance(String userToken, int accountId) {
        return given().headers("Authorization", userToken)
                .get(CUSTOMER_ACCOUNTS_PATH)
                .then()
                .statusCode(SC_OK)
                .extract()
                .jsonPath()
                .getDouble("find { it.id == " + accountId + " }.balance");
    }

    protected List<Map<String, Object>> getUserTransactions(String userToken, int accountId) {
        return given().headers("Authorization", userToken)
                .get(CUSTOMER_ACCOUNTS_PATH)
                .then()
                .statusCode(SC_OK)
                .extract()
                .jsonPath()
                .getList("find { it.id == " + accountId + " }.transactions");
    }

    protected void depositMoney(String userToken, int accountId, Number money) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", accountId);
        jsonObject.put("balance", money);

        given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .headers("Authorization", userToken)
                .body(jsonObject.toString())
                .post(ACCOUNTS_DEPOSIT_PATH)
                .then()
                .statusCode(SC_OK);

    }
}
