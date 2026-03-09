package requests;

import config.ApiPath;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import models.BaseModel;

import static io.restassured.RestAssured.given;

public class TransferRequester extends BaseRequester implements PostRequester {

    public TransferRequester(RequestSpecification requestSpecification, ResponseSpecification responseSpecification) {
        super(requestSpecification, responseSpecification);
    }

    @Override
    public ValidatableResponse post(BaseModel model) {
        return given().spec(requestSpecification)
                .body(model)
                .post(ApiPath.ACCOUNTS_TRANSFER_PATH)
                .then()
                .spec(responseSpecification);
    }
}
