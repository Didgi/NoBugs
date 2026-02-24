package api_tests.iteraion2;

import io.restassured.http.ContentType;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class ChangeUserName extends BaseTest {

    private static Stream<Arguments> diffPositiveData() {
        return Stream.of(
                Arguments.of("U U"),
                Arguments.of("UserUserUserUserU User"));
    }


    @MethodSource("diffPositiveData")
    @ParameterizedTest
    @DisplayName("Позитивный тест: пользователь может изменить имя на другое валидное")
    public void userCanChangeHisNameWithValidData(String updatedUserName) {
        JSONObject jsonObjectToChangeUserName = new JSONObject();

        jsonObjectToChangeUserName.put("name", updatedUserName);

        given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .headers("Authorization", authUserToken)
                .body(jsonObjectToChangeUserName.toString())
                .put(CUSTOMER_PROFILE_PATH)
                .then()
                .statusCode(SC_OK)
                .body("customer.name", equalTo(updatedUserName));

        given().accept(ContentType.JSON)
                .headers("Authorization", authUserToken)
                .get(CUSTOMER_PROFILE_PATH)
                .then()
                .statusCode(SC_OK)
                .body("name", equalTo(updatedUserName));
    }

    private static Stream<Arguments> diffNegativeData() {
        return Stream.of(
                Arguments.of("U"),
                Arguments.of("User User User"),
                Arguments.of("User User1"),
                Arguments.of("User User!"),
                Arguments.of("$%^&*()@# User"),
                Arguments.of(""),
                Arguments.of("  \"\" "));
    }

    @MethodSource("diffNegativeData")
    @ParameterizedTest
    @DisplayName("Негативный тест: пользователь не может изменить имя указав не валидное")
    public void userCannotChangeHisNameWithInvalidData(String updatedUserName) {

        JSONObject jsonObjectToChangeUserName = new JSONObject();
        jsonObjectToChangeUserName.put("name", updatedUserName);

        given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .headers("Authorization", authUserToken)
                .body(jsonObjectToChangeUserName.toString())
                .put(CUSTOMER_PROFILE_PATH)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body(equalTo("Name must contain two words with letters only"));

        given().accept(ContentType.JSON)
                .headers("Authorization", authUserToken)
                .get(CUSTOMER_PROFILE_PATH)
                .then()
                .statusCode(SC_OK)
                .body("name", equalTo(null));

    }

    @Test
    @DisplayName("Негативный тест: пользователь не может изменить имя указав null в значении")
    public void userCannotChangeHisNameWithNull() {

        given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .headers("Authorization", authUserToken)
                .body("""
                        {
                        "name": null
                        }
                        """)
                .put(CUSTOMER_PROFILE_PATH)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body(equalTo("Name must contain two words with letters only"));

        given().accept(ContentType.JSON)
                .headers("Authorization", authUserToken)
                .get(CUSTOMER_PROFILE_PATH)
                .then()
                .statusCode(SC_OK)
                .body("name", equalTo(null));

    }

    @Test
    @DisplayName("Позитивный тест: пользователь может изменить имя, как у другого пользователя")
    public void userCanChangeHisNameToAnotherUserName() {

        String newName = "Same name";
        JSONObject jsonObjectToChangeUserName = new JSONObject();
        jsonObjectToChangeUserName.put("name", newName);

        given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .headers("Authorization", authUserToken)
                .body(jsonObjectToChangeUserName.toString())
                .put(CUSTOMER_PROFILE_PATH)
                .then()
                .statusCode(SC_OK)
                .body("customer.name", equalTo(newName));

        given().accept(ContentType.JSON)
                .headers("Authorization", authUserToken)
                .get(CUSTOMER_PROFILE_PATH)
                .then()
                .statusCode(SC_OK)
                .body("name", equalTo(newName));

        final String secondUserAuthToken = createUserAndGetToken();

        given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .headers("Authorization", secondUserAuthToken)
                .body(jsonObjectToChangeUserName.toString())
                .put(CUSTOMER_PROFILE_PATH)
                .then()
                .statusCode(SC_OK)
                .body("customer.name", equalTo(newName));

        given().accept(ContentType.JSON)
                .headers("Authorization", secondUserAuthToken)
                .get(CUSTOMER_PROFILE_PATH)
                .then()
                .statusCode(SC_OK)
                .body("name", equalTo(newName));
    }
}
