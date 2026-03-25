package requests.skelethon.requesters;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import models.BaseModel;
import requests.skelethon.EndpointRequests;
import requests.skelethon.HttpBaseRequest;
import requests.skelethon.interfaces.CrudRequestsInterface;

import static io.restassured.RestAssured.given;

public class CrudRequester extends HttpBaseRequest implements CrudRequestsInterface {
    public CrudRequester(RequestSpecification requestSpecification, EndpointRequests endpointRequests, ResponseSpecification responseSpecification) {
        super(requestSpecification, endpointRequests, responseSpecification);
    }

    @Override
    public ValidatableResponse POST(BaseModel baseModel) {
        if (baseModel != null && !endpointRequests.getRequestModel().isInstance(baseModel)) {
            throw new RuntimeException("Ожидаемая и переданные модели различаются. " +
                    "Ожидалась модель: " + endpointRequests.getRequestModel().getSimpleName() +
                    ", но передана модель: " + baseModel.toString());
        }
        var body = baseModel != null ? baseModel : "";
        return given()
                .spec(requestSpecification)
                .body(body)
                .post(endpointRequests.getPath())
                .then()
                .spec(responseSpecification);
    }

    @Override
    public ValidatableResponse GET() {
        return given().spec(requestSpecification)
                .get(endpointRequests.getPath())
                .then()
                .spec(responseSpecification);
    }

    @Override
    public ValidatableResponse PUT(BaseModel baseModel) {
        if (baseModel != null && !endpointRequests.getRequestModel().isInstance(baseModel)) {
            throw new RuntimeException("Ожидаемая и переданные модели различаются. " +
                    "Ожидалась модель: " + endpointRequests.getRequestModel().getSimpleName() +
                    ", но передана модель: " + baseModel.toString());
        }
        var body = baseModel != null ? baseModel : "";
        return given().spec(requestSpecification)
                .body(body)
                .put(endpointRequests.getPath())
                .then()
                .spec(responseSpecification);
    }

    @Override
    public ValidatableResponse DELETE(int id) {
        return given().spec(requestSpecification)
                .delete(endpointRequests.getPath() + id)
                .then()
                .spec(responseSpecification);
    }
}
