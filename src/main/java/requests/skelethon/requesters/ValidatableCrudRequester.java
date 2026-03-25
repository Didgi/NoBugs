package requests.skelethon.requesters;

import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import models.BaseModel;
import requests.skelethon.EndpointRequests;
import requests.skelethon.HttpBaseRequest;
import requests.skelethon.interfaces.CrudRequestsInterface;

public class ValidatableCrudRequester<T extends BaseModel> extends HttpBaseRequest implements CrudRequestsInterface {
    private CrudRequester crudRequester;

    public ValidatableCrudRequester(RequestSpecification requestSpecification, EndpointRequests endpointRequests, ResponseSpecification responseSpecification) {
        super(requestSpecification, endpointRequests, responseSpecification);
        crudRequester = new CrudRequester(requestSpecification, endpointRequests, responseSpecification);
    }

    @Override
    public T POST(BaseModel baseModel) {
        return (T) crudRequester
                .POST(baseModel)
                .assertThat()
                .extract()
                .as(endpointRequests.getResponseModel());
    }

    @Override
    public T GET() {
        return (T) crudRequester.GET()
                .assertThat()
                .extract()
                .as(endpointRequests.getResponseModel());
    }

    @Override
    public T PUT(BaseModel baseModel) {
        return (T) crudRequester.PUT(baseModel)
                .assertThat()
                .extract()
                .as(endpointRequests.getResponseModel());
    }

    @Override
    public Object DELETE(int id) {
        return null;
    }
}
