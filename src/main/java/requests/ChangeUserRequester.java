package requests;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import models.BaseModel;

import static config.ApiPath.CUSTOMER_PROFILE_PATH;
import static io.restassured.RestAssured.given;

public class ChangeUserRequester extends BaseRequester implements PutRequester {
    public ChangeUserRequester(RequestSpecification requestSpecification, ResponseSpecification responseSpecification) {
        super(requestSpecification, responseSpecification);
    }

    @Override
    public ValidatableResponse put(BaseModel model) {
        return given().spec(requestSpecification)
                .body(model)
                .put(CUSTOMER_PROFILE_PATH)
                .then()
                .spec(responseSpecification);
    }
}
