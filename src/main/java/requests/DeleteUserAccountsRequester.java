package requests;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static config.ApiPath.ACCOUNTS_PATH;
import static io.restassured.RestAssured.given;

public class DeleteUserAccountsRequester extends BaseRequester implements DeleteRequester{
    public DeleteUserAccountsRequester(RequestSpecification requestSpecification, ResponseSpecification responseSpecification) {
        super(requestSpecification, responseSpecification);
    }

    @Override
    public ValidatableResponse delete(int id) {
        return given()
                .spec(requestSpecification)
                .delete(ACCOUNTS_PATH + "/" + id)
                .then()
                .spec(responseSpecification);
    }
}
