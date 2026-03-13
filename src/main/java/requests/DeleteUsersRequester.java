package requests;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static config.ApiPath.ADMIN_USERS_PATH;
import static io.restassured.RestAssured.given;

public class DeleteUsersRequester extends BaseRequester implements DeleteRequester {
    public DeleteUsersRequester(RequestSpecification requestSpecification, ResponseSpecification responseSpecification) {
        super(requestSpecification, responseSpecification);
    }

    @Override
    public ValidatableResponse delete(int id) {
        return given()
                .spec(requestSpecification)
                .delete(ADMIN_USERS_PATH + "/" + id)
                .then()
                .spec(responseSpecification);
    }
}
