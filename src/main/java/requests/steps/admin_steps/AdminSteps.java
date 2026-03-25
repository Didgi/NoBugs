package requests.steps.admin_steps;

import io.restassured.common.mapper.TypeRef;
import models.CreateUserRequest;
import models.LoginRequest;
import models.UserAccountResponse;
import models.UsersResponse;
import models.comparison.ModelAssertions;
import requests.skelethon.EndpointRequests;
import requests.skelethon.requesters.CrudRequester;
import requests.skelethon.requesters.ValidatableCrudRequester;
import specs.RequestSpecs;
import specs.ResponseSpecs;
import utils.RandomModelGenerator;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AdminSteps {
    public static String createUserAndGetToken() {

        final CreateUserRequest userRequest = RandomModelGenerator.generate(CreateUserRequest.class);
        final UsersResponse userResponse = new ValidatableCrudRequester<UsersResponse>(RequestSpecs.withAdminToken(),
                EndpointRequests.CREATE_USER, ResponseSpecs.entityWasCreated())
                .POST(userRequest);

        ModelAssertions.assertThatModels(userRequest, userResponse).match();

        final LoginRequest loginRequest = LoginRequest.builder()
                .username(userRequest.getUsername())
                .password(userRequest.getPassword())
                .build();

        return new CrudRequester(RequestSpecs.withAdminToken(), EndpointRequests.LOGIN, ResponseSpecs.requestReturnsOk())
                .POST(loginRequest).extract().header("Authorization");
    }

    private static List<Integer> getUsersId() {
        final List<UsersResponse> users = new CrudRequester(RequestSpecs.withAdminToken(),
                EndpointRequests.GET_USERS_BY_ADMIN, ResponseSpecs.requestReturnsOk())
                .GET().assertThat().extract().as(new TypeRef<List<UsersResponse>>() {
                });

        return users.stream().map(UsersResponse::getId).toList();
    }

    public static void deleteUsersById() {
        final List<Integer> usersId = getUsersId();
        usersId.forEach(id -> {
            new CrudRequester(RequestSpecs.withAdminToken(), EndpointRequests.DELETE_USER, ResponseSpecs.requestReturnsOk())
                    .DELETE(id);
        });

    }

    public static int getMaxExistedAccountId() {
        final List<UsersResponse> usersResponses = new CrudRequester(RequestSpecs.withAdminToken(),
                EndpointRequests.GET_USERS_BY_ADMIN, ResponseSpecs.requestReturnsOk())
                .GET().extract().as(new TypeRef<List<UsersResponse>>() {
                });

        final Optional<UserAccountResponse> maxExistedAccountIdOptional = usersResponses
                .stream().flatMap(usersResponse -> usersResponse.getAccounts().stream())
                .max(Comparator.comparingInt(UserAccountResponse::getId));

        assertTrue(maxExistedAccountIdOptional.isPresent());
        return maxExistedAccountIdOptional.get().getId();
    }
}
