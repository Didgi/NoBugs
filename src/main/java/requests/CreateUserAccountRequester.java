package requests;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import models.BaseModel;

import static config.ApiPath.ACCOUNTS_PATH;
import static io.restassured.RestAssured.given;

public class CreateUserAccountRequester extends BaseRequester implements PostRequester {
    public CreateUserAccountRequester(RequestSpecification requestSpecification, ResponseSpecification responseSpecification) {
        super(requestSpecification, responseSpecification);
    }

    @Override
    public ValidatableResponse post(BaseModel model) {
        return given().
                spec(requestSpecification)
                .post(ACCOUNTS_PATH)
                .then()
                .spec(responseSpecification);
    }
}
