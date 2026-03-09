package requests;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import models.BaseModel;

import static config.ApiPath.ADMIN_USERS_PATH;
import static io.restassured.RestAssured.given;

public class CreateUserRequester extends BaseRequester implements PostRequester {
    public CreateUserRequester(RequestSpecification requestSpecification, ResponseSpecification responseSpecification) {
        super(requestSpecification, responseSpecification);
    }

    @Override
    public ValidatableResponse post(BaseModel model) {
        return given().spec(requestSpecification)
                .body(model)
                .post(ADMIN_USERS_PATH)
                .then()
                .spec(responseSpecification);
    }
}
