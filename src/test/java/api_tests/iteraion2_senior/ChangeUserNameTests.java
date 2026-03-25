package api_tests.iteraion2_senior;

import config.ResponseMessages;
import models.ChangeUserRequest;
import models.ChangeUserResponse;
import models.UsersResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import requests.skelethon.EndpointRequests;
import requests.skelethon.requesters.ValidatableCrudRequester;
import specs.RequestSpecs;
import specs.ResponseSpecs;
import utils.RandomModelGenerator;

import java.util.stream.Stream;

import static requests.steps.admin_steps.AdminSteps.createUserAndGetToken;
import static requests.steps.user_steps.UserSteps.*;

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

        // 1. Выполняем запрос на изменение имени пользователя
        final ChangeUserRequest changeUserRequest = ChangeUserRequest.builder().name(updatedUserName).build();

        final ChangeUserResponse changeUserResponse =
                new ValidatableCrudRequester<ChangeUserResponse>(RequestSpecs.withToken(authUserToken),
                        EndpointRequests.UPDATE_USER, ResponseSpecs.requestReturnsOk())
                .PUT(changeUserRequest);

        // 2. Проверяем в ответе на запрос имя и сообщение об успешном выполнении
        softly.assertThat(changeUserResponse.getCustomer().getName()).isEqualTo(updatedUserName);
        softly.assertThat(changeUserResponse.getMessage()).isEqualTo(ResponseMessages.PROFILE_UPDATED_SUCCESSFULLY.getValue());

        // 3. Проверяем имя в информации пользователя
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

        // 1. Проверяем имя пользователя до изменений
        softly.assertThat(getUserInfo(authUserToken).getName()).isNull();

        // 2. Выполняем изменение имени пользователя на невалидное и сохраняем сообщение об ошибке
        final String actualErrorMessage = failedChangeUserName(updatedUserName, authUserToken,
                ResponseSpecs.requestReturnsBadRequest());

        // 3. Проверяем в ответе на запрос имя и сообщение об ошибке
        softly.assertThat(actualErrorMessage).isEqualTo(ResponseMessages.NAME_MUST_CONTAIN_TWO_WORDS_WITH_LETTERS_ONLY.getValue());

        // 4. проверяем имя в информации пользователя
        softly.assertThat(getUserInfo(authUserToken).getName()).isNull();
    }

    @Test
    @DisplayName("Негативный тест: пользователь не может выполнить запрос на изменение имени с null значением")
    public void userCannotChangeHisNameWithNull() {

        // 1. Выполняем изменение имени пользователя указав null и сохраняем сообщение об ошибке
        final String actualErrorMessage = failedChangeUserName(null, authUserToken,
                ResponseSpecs.requestReturnsInternalServiceError());

        // 2. Проверяем ответ
        softly.assertThat(actualErrorMessage.isEmpty());
    }

    @Test
    @DisplayName("Позитивный тест: пользователь может изменить имя, как у другого пользователя")
    public void userCanChangeHisNameToAnotherUserNameUpdated() {

        // 1. Генерируем рандомное имя
        final ChangeUserRequest changeUserRequest = RandomModelGenerator.generate(ChangeUserRequest.class);

        // 2. Выполняем запрос на изменение имени пользователя и проверяем ответ
        successfulChangeUserName(changeUserRequest, authUserToken);

        // 3. Создаём второго пользователя и получаем его токен
        final String secondUserAuthToken = createUserAndGetToken();

        // 4. Выполняем запрос на изменение имени пользователя и проверяем ответ
        successfulChangeUserName(changeUserRequest, secondUserAuthToken);

    }
}
