package api_tests;

import api.config.ResponseMessages;
import api.dao.comparison_db.ModelAssertionsDb;
import api.dao.jdbc.CustomersDao;
import api.dao.jpa.entities.CustomerEntity;
import api.models.ChangeUserErrorResponse;
import api.models.ChangeUserRequest;
import api.models.ChangeUserResponse;
import api.models.UserProfileResponse;
import api.requests.skelethon.EndpointRequests;
import api.requests.skelethon.requesters.CrudRequester;
import api.requests.skelethon.requesters.ValidatableCrudRequester;
import api.requests.steps.db_steps.DBSteps;
import api.specs.RequestSpecs;
import api.specs.ResponseSpecs;
import api.utils.RandomModelGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.SQLException;
import java.util.stream.Stream;

import static api.requests.steps.admin_steps.AdminSteps.createUserAndGetToken;
import static api.requests.steps.user_steps.UserSteps.getUserInfo;
import static api.requests.steps.user_steps.UserSteps.successfulChangeUserName;

public class ChangeUserNameTests extends BaseTestSenior {

    private static Stream<Arguments> diffPositiveData() {
        return
                Stream.of(
                        Arguments.of("U U"),
                        Arguments.of("UserUserUserUserU User"));
    }

    @MethodSource("diffPositiveData")
    @ParameterizedTest
    @DisplayName("Позитивный тест: пользователь может изменить имя на другое валидное")
    public void userCanChangeHisNameWithValidData(String updatedUserName) throws SQLException {

        final ChangeUserRequest changeUserRequest = ChangeUserRequest.builder().name(updatedUserName).build();

        final ChangeUserResponse changeUserResponse =
                new ValidatableCrudRequester<ChangeUserResponse>(RequestSpecs.withToken(authUserToken),
                        EndpointRequests.UPDATE_USER, ResponseSpecs.requestReturnsOk())
                        .PUT(changeUserRequest);

        softly.assertThat(changeUserResponse.getName()).isEqualTo(updatedUserName);

        UserProfileResponse userInfo = getUserInfo(authUserToken);

        softly.assertThat(userInfo.getName()).isEqualTo(updatedUserName);

        CustomersDao customersDao = DBSteps.getUserByUserNameJDBC(userInfo.getUsername());

        ModelAssertionsDb.assertThatModels(userInfo, customersDao).match();
    }

    private static Stream<Arguments> diffNegativeData() {
        return Stream.of(
                Arguments.of("U"),
                Arguments.of("User User User"),
                Arguments.of("User User1"),
                Arguments.of("User User!"),
                Arguments.of("$%^&*()@# User"),
                Arguments.of(""),
                Arguments.of("  \"\" "));
    }

    @MethodSource("diffNegativeData")
    @ParameterizedTest
    @DisplayName("Негативный тест: пользователь не может изменить имя указав не валидное")
    public void userCannotChangeHisNameWithInvalidData(String updatedUserName) {

        softly.assertThat(getUserInfo(authUserToken).getName()).isNull();

        final ChangeUserRequest changeUserRequest = ChangeUserRequest.builder().name(updatedUserName).build();

        final String actualErrorMessage = new ValidatableCrudRequester<ChangeUserErrorResponse>(RequestSpecs.withToken(authUserToken),
                EndpointRequests.UPDATE_USER_ERROR, ResponseSpecs.requestReturnsBadRequest())
                .PUT(changeUserRequest).getMessage();

        softly.assertThat(actualErrorMessage).isEqualTo(ResponseMessages.NAME_MUST_CONTAIN_TWO_WORDS_WITH_LETTERS_ONLY.getValue());

        softly.assertThat(getUserInfo(authUserToken).getName()).isNull();

        final UserProfileResponse userInfo = getUserInfo(authUserToken);

        final CustomerEntity userByUserNameJpa = DBSteps.getUserByIdJPA(userInfo.getId());

        ModelAssertionsDb.assertThatModels(userInfo, userByUserNameJpa).match();

    }

    @Test
    @DisplayName("Негативный тест: пользователь не может выполнить запрос на изменение имени с null значением")
    public void userCannotChangeHisNameWithNull() throws SQLException {

        final ChangeUserRequest changeUserRequest = ChangeUserRequest.builder().build();

        final String actualErrorMessage = new CrudRequester(RequestSpecs.withToken(authUserToken),
                EndpointRequests.UPDATE_USER, ResponseSpecs.requestReturnsOk())
                .PUT(changeUserRequest).extract().response().asString();

        softly.assertThat(actualErrorMessage.isEmpty());

        final UserProfileResponse userInfo = getUserInfo(authUserToken);

        CustomersDao customersDao = DBSteps.getUserByUserNameJDBC(userInfo.getUsername());

        ModelAssertionsDb.assertThatModels(userInfo, customersDao).match();

    }

    @Test
    @DisplayName("Позитивный тест: пользователь может изменить имя, как у другого пользователя")
    public void userCanChangeHisNameToAnotherUserNameUpdated() throws SQLException {

        final ChangeUserRequest changeUserRequest = RandomModelGenerator.generate(ChangeUserRequest.class);

        successfulChangeUserName(changeUserRequest, authUserToken);

        final String secondUserAuthToken = createUserAndGetToken();

        successfulChangeUserName(changeUserRequest, secondUserAuthToken);

        final UserProfileResponse firstUserInfo = getUserInfo(authUserToken);

        final UserProfileResponse secondUserInfo = getUserInfo(secondUserAuthToken);

        CustomersDao customersDaoFirst = DBSteps.getUserByUserNameJDBC(firstUserInfo.getUsername());

        ModelAssertionsDb.assertThatModels(firstUserInfo, customersDaoFirst).match();

        CustomersDao customersDaoSecond = DBSteps.getUserByUserNameJDBC(secondUserInfo.getUsername());

        ModelAssertionsDb.assertThatModels(secondUserInfo, customersDaoSecond).match();

    }
}
