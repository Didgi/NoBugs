package api_tests;

import api.models.CreateUserRequest;
import api.models.LoginRequest;
import api.models.UserProfileResponse;
import api.requests.skelethon.EndpointRequests;
import api.requests.skelethon.requesters.CrudRequester;
import api.requests.skelethon.requesters.ValidatableCrudRequester;
import api.specs.RequestSpecs;
import api.specs.ResponseSpecs;
import api.utils.RandomData;
import api.utils.RandomModelGenerator;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static net.sf.saxon.value.BigDecimalValue.MAX_INT;

public class AdminUsersTests extends BaseTestSenior {

    @Test
    @DisplayName("Негативный тест: выполнение запроса на получение списка юзеров без указания token")
    public void getUsersWithoutToken() {
        new CrudRequester(RequestSpecs.withoutTokenSpec(),
                EndpointRequests.GET_USERS_BY_ADMIN, ResponseSpecs.requestReturnsUnauthorized()).GET();
    }

    @Test
    @DisplayName("Негативный тест: выполнение запроса на удаление юзера без указания token")
    public void deleteUserWithoutToken() {

        final UserProfileResponse userInfo = new ValidatableCrudRequester<UserProfileResponse>(RequestSpecs.withToken(authUserToken), EndpointRequests.GET_USER_INFO, ResponseSpecs.requestReturnsOk()).GET();

        new CrudRequester(RequestSpecs.withoutTokenSpec(),
                EndpointRequests.DELETE_USER, ResponseSpecs.requestReturnsUnauthorized()).DELETE(userInfo.getId());
    }

    @Test
    @DisplayName("Негативный тест: выполнение запроса на удаление несуществующего юзера")
    public void deleteNonexistentUser() {

        int nonExistentId = MAX_INT.intValue();

        new CrudRequester(RequestSpecs.withAdminToken(),
                EndpointRequests.DELETE_USER, ResponseSpecs.requestReturnsNotFound()).DELETE(nonExistentId);
    }
}
