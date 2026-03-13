package specs;

import config.Users;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import models.LoginRequest;
import requests.LoginRequester;

import java.util.List;

import static config.ApiPath.*;

public class RequestSpecs {

    static private String adminToken = null;

    private RequestSpecs() {
    }

    private static RequestSpecBuilder defaultBuilder() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilters(List.of(new RequestLoggingFilter(), new ResponseLoggingFilter()))
                .setBaseUri(BASE_URI);
    }

    public static RequestSpecification withoutTokenSpec() {
        return defaultBuilder().build();
    }

    public static RequestSpecification withTokenSpec(String token) {
        return defaultBuilder()
                .build().header(AUTH_HEADER, token);
    }

    public static RequestSpecification adminTokenSpec() {
        if (adminToken == null) {
            LoginRequest loginRequest = LoginRequest.builder()
                    .username(Users.ADMIN_USERNAME)
                    .password(Users.ADMIN_PASSWORD)
                    .build();

            adminToken = new LoginRequester(RequestSpecs.withoutTokenSpec(), ResponseSpecs.requestReturnsOk())
                    .post(loginRequest).extract()
                    .header(AUTH_HEADER);
            System.out.println("Генерация нового токена для админа");
        }
        return defaultBuilder()
                .build().header(AUTH_HEADER, adminToken);
    }
}
