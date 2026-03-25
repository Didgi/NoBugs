package specs;

import config.Config;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import models.LoginRequest;
import requests.skelethon.EndpointRequests;
import requests.skelethon.requesters.CrudRequester;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static config.ApiPath.BASE_URI;

public class RequestSpecs {

    private static Map<String, String> tokenStorage = new HashMap<>();
    private static final String ADMIN_USERNAME = Config.getProperty("admin_username");
    private static final String ADMIN_PASSWORD = Config.getProperty("admin_username");

    private RequestSpecs() {
    }

    public static RequestSpecBuilder basicRequestSpec() {
        return new RequestSpecBuilder().setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilters(List.of(new RequestLoggingFilter(), new ResponseLoggingFilter()))
                .setBaseUri(BASE_URI);
    }

    public static RequestSpecification withoutTokenSpec() {
        return basicRequestSpec().build();
    }

    public static RequestSpecification withToken(String token) {
        return basicRequestSpec().build().headers("Authorization", token);
    }

    public static RequestSpecification withAdminToken() {
        if (!tokenStorage.containsKey(ADMIN_USERNAME)) {
            final LoginRequest loginRequestAdmin = LoginRequest.builder().username(ADMIN_USERNAME).
                    password(ADMIN_PASSWORD).build();
            String adminToken = new CrudRequester(withoutTokenSpec(), EndpointRequests.LOGIN, ResponseSpecs.requestReturnsOk())
                    .POST(loginRequestAdmin)
                    .assertThat()
                    .extract()
                    .header("Authorization");
            tokenStorage.put(ADMIN_USERNAME, adminToken);
        }
        return basicRequestSpec().build().headers("Authorization", tokenStorage.get(ADMIN_USERNAME));
    }

}
