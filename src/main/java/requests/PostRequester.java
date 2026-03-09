package requests;

import io.restassured.response.ValidatableResponse;
import models.BaseModel;

public interface PostRequester {
    ValidatableResponse post(BaseModel model);
}
