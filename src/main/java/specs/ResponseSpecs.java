package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;

import static org.apache.http.HttpStatus.*;

public class ResponseSpecs {

    private ResponseSpecs() {
    }

    private static ResponseSpecBuilder defaultResponseBuilder() {
        return new ResponseSpecBuilder();
    }

    public static ResponseSpecification requestReturnsOk() {
        return defaultResponseBuilder()
                .expectStatusCode(SC_OK).build();
    }

    public static ResponseSpecification entityWasCreated() {
        return defaultResponseBuilder().expectStatusCode(SC_CREATED).build();
    }

    public static ResponseSpecification requestReturnsBadRequest() {
        return defaultResponseBuilder()
                .expectStatusCode(SC_BAD_REQUEST).build();
    }

    public static ResponseSpecification requestReturnsForbidden() {
        return defaultResponseBuilder()
                .expectStatusCode(SC_FORBIDDEN).build();
    }

    public static ResponseSpecification requestReturnsInternalServerError() {
        return defaultResponseBuilder()
                .expectStatusCode(SC_INTERNAL_SERVER_ERROR).build();
    }
}
