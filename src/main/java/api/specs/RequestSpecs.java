package api.specs;

import api.config.Config;
import api.models.LoginRequest;
import api.requests.skelethon.EndpointRequests;
import api.requests.skelethon.requesters.CrudRequester;
import com.google.common.net.HttpHeaders;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static api.config.ApiPath.BASE_URI;

public class RequestSpecs {

    private static Map<String, String> tokenStorage = new HashMap<>();
    private static final String ADMIN_USERNAME = Config.getProperty("admin_username");
    private static final String ADMIN_PASSWORD = Config.getProperty("admin_password");

    private RequestSpecs() {
    }

    public static RequestSpecBuilder basicRequestSpec() {
        return new RequestSpecBuilder().setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilters(List.of(new RequestLoggingFilter(), new ResponseLoggingFilter(), new AllureRestAssured()))
                .setBaseUri(BASE_URI);
    }

    public static RequestSpecification withoutTokenSpec() {
        return basicRequestSpec().build();
    }

    public static RequestSpecification withToken(String token) {
        return basicRequestSpec().build().headers(HttpHeaders.AUTHORIZATION, token);
    }

    public static RequestSpecification withAdminToken() {
        if (!tokenStorage.containsKey(ADMIN_USERNAME)) {
            final LoginRequest loginRequestAdmin = LoginRequest.builder().username(ADMIN_USERNAME).
                    password(ADMIN_PASSWORD).build();
            String adminToken = new CrudRequester(withoutTokenSpec(), EndpointRequests.LOGIN, ResponseSpecs.requestReturnsOk())
                    .POST(loginRequestAdmin)
                    .assertThat()
                    .extract()
                    .header(HttpHeaders.AUTHORIZATION);
            tokenStorage.put(ADMIN_USERNAME, adminToken);
        }
        return basicRequestSpec().build().headers(HttpHeaders.AUTHORIZATION, tokenStorage.get(ADMIN_USERNAME));
    }

}
