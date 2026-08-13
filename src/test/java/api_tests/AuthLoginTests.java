package api_tests;

import api.models.CreateUserRequest;
import api.models.LoginRequest;
import api.requests.skelethon.EndpointRequests;
import api.requests.skelethon.requesters.CrudRequester;
import api.specs.RequestSpecs;
import api.specs.ResponseSpecs;
import api.utils.RandomModelGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AuthLoginTests extends BaseTestSenior {

    @Test
    @DisplayName("Негативный тест: авторизация без указания обязательного поля password")
    public void authUserWithoutPassword() {
        final CreateUserRequest createUserRequest = RandomModelGenerator.generate(CreateUserRequest.class);
        final CrudRequester createUserCrudRequester = new CrudRequester(RequestSpecs.withAdminToken(), EndpointRequests.CREATE_USER, ResponseSpecs.entityWasCreated());
        createUserCrudRequester.POST(createUserRequest);

        final LoginRequest loginRequest = LoginRequest.builder().username(createUserRequest.getUsername()).build();
        final CrudRequester authCrudRequester = new CrudRequester(RequestSpecs.withoutTokenSpec(), EndpointRequests.LOGIN, ResponseSpecs.requestReturnsUnauthorized());
        authCrudRequester.POST(loginRequest);

    }

    @Test
    @DisplayName("Негативный тест: авторизация без указания обязательного поля username")
    public void authUserWithoutUsername() {
        final CreateUserRequest createUserRequest = RandomModelGenerator.generate(CreateUserRequest.class);
        final CrudRequester createUserCrudRequester = new CrudRequester(RequestSpecs.withAdminToken(), EndpointRequests.CREATE_USER, ResponseSpecs.entityWasCreated());
        createUserCrudRequester.POST(createUserRequest);

        final LoginRequest loginRequest = LoginRequest.builder().username(createUserRequest.getPassword()).build();
        final CrudRequester authCrudRequester = new CrudRequester(RequestSpecs.withoutTokenSpec(), EndpointRequests.LOGIN, ResponseSpecs.requestReturnsUnauthorized());
        authCrudRequester.POST(loginRequest);

    }
}
