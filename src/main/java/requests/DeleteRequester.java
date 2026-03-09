package requests;

import io.restassured.response.ValidatableResponse;

public interface DeleteRequester {
    ValidatableResponse delete(int id);
}
