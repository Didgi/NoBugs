package requests;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static config.ApiPath.CUSTOMER_ACCOUNTS_PATH;
import static io.restassured.RestAssured.given;

public class GetUserAccountsRequester extends BaseRequester implements GetRequester {
    public GetUserAccountsRequester(RequestSpecification requestSpecification, ResponseSpecification responseSpecification) {
        super(requestSpecification, responseSpecification);
    }

    @Override
    public ValidatableResponse get() {
        return given()
                .spec(requestSpecification)
                .get(CUSTOMER_ACCOUNTS_PATH)
                .then()
                .spec(responseSpecification);
    }
}
