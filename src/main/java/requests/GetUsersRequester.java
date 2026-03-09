package requests;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static config.ApiPath.ADMIN_USERS_PATH;
import static config.ApiPath.CUSTOMER_PROFILE_PATH;
import static io.restassured.RestAssured.given;

public class GetUsersRequester extends BaseRequester implements GetRequester{
    public GetUsersRequester(RequestSpecification requestSpecification, ResponseSpecification responseSpecification) {
        super(requestSpecification, responseSpecification);
    }

    @Override
    public ValidatableResponse get() {
        return given().spec(requestSpecification)
                .get(ADMIN_USERS_PATH)
                        .then()
                                .spec(responseSpecification);

    }

    public ValidatableResponse getUserInfo() {
        return given().spec(requestSpecification)
                .get(CUSTOMER_PROFILE_PATH)
                .then()
                .spec(responseSpecification);

    }
}
