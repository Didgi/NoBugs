package api.requests.steps.admin_steps;

import io.restassured.common.mapper.TypeRef;
import api.models.CreateUserRequest;
import api.models.LoginRequest;
import api.models.UserAccountResponse;
import api.models.UsersResponse;
import api.models.comparison.ModelAssertions;
import api.requests.skelethon.EndpointRequests;
import api.requests.skelethon.requesters.CrudRequester;
import api.requests.skelethon.requesters.ValidatableCrudRequester;
import api.specs.RequestSpecs;
import api.specs.ResponseSpecs;
import api.utils.RandomModelGenerator;

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
