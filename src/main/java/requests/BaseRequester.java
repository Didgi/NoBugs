package requests;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import lombok.AllArgsConstructor;
import models.BaseModel;

@AllArgsConstructor
public abstract class BaseRequester {
    protected RequestSpecification requestSpecification;
    protected ResponseSpecification responseSpecification;
}
