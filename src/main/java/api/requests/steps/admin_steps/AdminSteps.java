package api.requests.steps.admin_steps;

import api.models.*;
import api.models.comparison.ModelAssertions;
import api.requests.skelethon.EndpointRequests;
import api.requests.skelethon.requesters.CrudRequester;
import api.requests.skelethon.requesters.ValidatableCrudRequester;
import api.requests.steps.user_steps.UserSteps;
import api.specs.RequestSpecs;
import api.specs.ResponseSpecs;
import api.utils.RandomModelGenerator;
import com.google.common.net.HttpHeaders;
import common.SessionStorage;
import io.qameta.allure.Step;
import io.restassured.common.mapper.TypeRef;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AdminSteps {

    @Step("Создаём пользователя и получаем его токен")
    public static String createUserAndGetToken() {

        final CreateUserRequest userRequest = RandomModelGenerator.generate(CreateUserRequest.class);
        final CreateUserResponse userResponse = new ValidatableCrudRequester<CreateUserResponse>(RequestSpecs.withAdminToken(),
                EndpointRequests.CREATE_USER, ResponseSpecs.entityWasCreated())
                .POST(userRequest);

        ModelAssertions.assertThatModels(userRequest, userResponse).match();

        final LoginRequest loginRequest = LoginRequest.builder()
                .username(userRequest.getUsername())
                .password(userRequest.getPassword())
                .build();

        String userToken = new CrudRequester(RequestSpecs.withAdminToken(), EndpointRequests.LOGIN, ResponseSpecs.requestReturnsOk())
                .POST(loginRequest).extract().header(HttpHeaders.AUTHORIZATION);

        final UserProfileResponse userInfo = UserSteps.getUserInfo(userToken);

        System.out.println("В storage положили инфо о созданном пользователе: " + userInfo);

        SessionStorage.addUserInfoToStorage(userToken, userInfo);

        SessionStorage.addCreatedUser(userResponse.getId());

        return userToken;
    }

    @Step("Удаляем пользователей после завершения тест")
    public static void deleteUsersById() {
        final List<Integer> usersId = SessionStorage.getCreatedUsers();
        System.out.println("Id удаляемых пользователей: " + usersId);
        if (usersId.isEmpty()) {
            System.out.println("Нет пользователей для удаления");
            return;
        }
        try {
            usersId.forEach(id -> {
                new CrudRequester(RequestSpecs.withAdminToken(), EndpointRequests.DELETE_USER, ResponseSpecs.requestReturnsOk())
                        .DELETE(id);
            });
        } catch (RuntimeException e) {
            System.out.println("При попытке удаления пользователя произошла ошибка: " + e);
        }
        SessionStorage.clearCreatedUsersList();
    }

    @Step("Находим максимальный ID существующего аккаунта")
    public static int getMaxExistedAccountId() {
        final List<CreateUserResponse> usersResponses = new CrudRequester(RequestSpecs.withAdminToken(),
                EndpointRequests.GET_USERS_BY_ADMIN, ResponseSpecs.requestReturnsOk())
                .GET().extract().as(new TypeRef<List<CreateUserResponse>>() {
                });

        final Optional<UserAccountResponse> maxExistedAccountIdOptional = usersResponses
                .stream().flatMap(usersResponse -> usersResponse.getAccounts().stream())
                .max(Comparator.comparingInt(UserAccountResponse::getId));

        assertTrue(maxExistedAccountIdOptional.isPresent());
        return maxExistedAccountIdOptional.get().getId();
    }
}
