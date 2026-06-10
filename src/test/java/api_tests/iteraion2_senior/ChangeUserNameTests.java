package api_tests.iteraion2_senior;

import api.config.ResponseMessages;
import api.models.ChangeUserRequest;
import api.models.ChangeUserResponse;
import api.models.UsersResponse;
import api.requests.skelethon.EndpointRequests;
import api.requests.skelethon.requesters.CrudRequester;
import api.requests.skelethon.requesters.ValidatableCrudRequester;
import api.specs.RequestSpecs;
import api.specs.ResponseSpecs;
import api.utils.RandomModelGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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
    public void userCanChangeHisNameWithValidData(String updatedUserName) {

        final ChangeUserRequest changeUserRequest = ChangeUserRequest.builder().name(updatedUserName).build();

        final ChangeUserResponse changeUserResponse =
                new ValidatableCrudRequester<ChangeUserResponse>(RequestSpecs.withToken(authUserToken),
                        EndpointRequests.UPDATE_USER, ResponseSpecs.requestReturnsOk())
                        .PUT(changeUserRequest);

        softly.assertThat(changeUserResponse.getCustomer().getName()).isEqualTo(updatedUserName);
        softly.assertThat(changeUserResponse.getMessage()).isEqualTo(ResponseMessages.PROFILE_UPDATED_SUCCESSFULLY.getValue());

        UsersResponse userInfo = getUserInfo(authUserToken);
        softly.assertThat(userInfo.getName()).isEqualTo(updatedUserName);
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
        final String actualErrorMessage = new CrudRequester(RequestSpecs.withToken(authUserToken),
                EndpointRequests.UPDATE_USER, ResponseSpecs.requestReturnsBadRequest())
                .PUT(changeUserRequest).extract().response().asString();

        softly.assertThat(actualErrorMessage).isEqualTo(ResponseMessages.NAME_MUST_CONTAIN_TWO_WORDS_WITH_LETTERS_ONLY.getValue());

        softly.assertThat(getUserInfo(authUserToken).getName()).isNull();
    }

    @Test
    @DisplayName("Негативный тест: пользователь не может выполнить запрос на изменение имени с null значением")
    public void userCannotChangeHisNameWithNull() {

        final ChangeUserRequest changeUserRequest = ChangeUserRequest.builder().build();
        final String actualErrorMessage = new CrudRequester(RequestSpecs.withToken(authUserToken),
                EndpointRequests.UPDATE_USER, ResponseSpecs.requestReturnsInternalServiceError())
                .PUT(changeUserRequest).extract().response().asString();

        softly.assertThat(actualErrorMessage.isEmpty());
    }

    @Test
    @DisplayName("Позитивный тест: пользователь может изменить имя, как у другого пользователя")
    public void userCanChangeHisNameToAnotherUserNameUpdated() {

        final ChangeUserRequest changeUserRequest = RandomModelGenerator.generate(ChangeUserRequest.class);

        successfulChangeUserName(changeUserRequest, authUserToken);

        final String secondUserAuthToken = createUserAndGetToken();

        successfulChangeUserName(changeUserRequest, secondUserAuthToken);

    }
}
