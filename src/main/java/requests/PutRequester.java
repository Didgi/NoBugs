package requests;

import io.restassured.response.ValidatableResponse;
import models.BaseModel;

public interface PutRequester {
    ValidatableResponse put(BaseModel model);
}
