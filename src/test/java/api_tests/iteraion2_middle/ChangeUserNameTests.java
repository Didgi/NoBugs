package api_tests.iteraion2_middle;

import config.ResponseMessages;
import models.ChangeUserResponse;
import models.UsersResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import specs.ResponseSpecs;
import utils.RandomData;

import java.util.stream.Stream;

public class ChangeUserNameTests extends BaseTestMiddle {

    private static Stream<Arguments> diffPositiveData() {
        return Stream.of(
                Arguments.of("U U"),
                Arguments.of("UserUserUserUserU User"));
    }


    @MethodSource("diffPositiveData")
    @ParameterizedTest
    @DisplayName("Позитивный тест: пользователь может изменить имя на другое валидное")
    public void userCanChangeHisNameWithValidData(String updatedUserName) {

        //выполняем запрос на изменение имени пользователя
        final ChangeUserResponse changeUserResponse = successfulChangeUserName(updatedUserName, authUserToken);

        //проверяем в ответе на запрос имя и сообщение об успешном выполнении
        softly.assertThat(changeUserResponse.getCustomer().getName()).isEqualTo(updatedUserName);
        softly.assertThat(changeUserResponse.getMessage()).isEqualTo(ResponseMessages.PROFILE_UPDATED_SUCCESSFULLY.getValue());

        //проверяем имя в информации пользователя
        UsersResponse userInfo = getUserInfo();
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

        //Проверяем имя пользователя до изменений
        UsersResponse userInfo = getUserInfo();
        softly.assertThat(userInfo.getName()).isNull();

        //выполняем изменение имени пользователя на невалидное и сохраняем сообщение об ошибке
        final String actualErrorMessage = failedChangeUserName(updatedUserName, authUserToken,
                ResponseSpecs.requestReturnsBadRequest());

        //проверяем в ответе на запрос имя и сообщение об ошибке
        softly.assertThat(actualErrorMessage).isEqualTo(ResponseMessages.NAME_MUST_CONTAIN_TWO_WORDS_WITH_LETTERS_ONLY.getValue());

        //проверяем имя в информации пользователя
        userInfo = getUserInfo();
        softly.assertThat(userInfo.getName()).isNull();
    }

    @Test
    @DisplayName("Негативный тест: пользователь не может выполнить запрос на изменение имени с null значением")
    public void userCannotChangeHisNameWithNull() {

        //выполняем изменение имени пользователя указав null и сохраняем сообщение об ошибке
        final String actualErrorMessage = failedChangeUserName(null, authUserToken,
                ResponseSpecs.requestReturnsInternalServerError());

        //проверяем в ответе на запрос имя и сообщение об успешном выполнении
        softly.assertThat(actualErrorMessage.isEmpty());
    }

    @Test
    @DisplayName("Позитивный тест: пользователь может изменить имя, как у другого пользователя")
    public void userCanChangeHisNameToAnotherUserName() {

        //генерируем рандомное имя
        String newName = RandomData.getName();

        //выполняем запрос на изменение имени пользователя
        final ChangeUserResponse changeFirstUserResponse = successfulChangeUserName(newName, authUserToken);

        //проверяем в ответе на запрос имя и сообщение об успешном выполнении
        softly.assertThat(changeFirstUserResponse.getCustomer().getName()).isEqualTo(newName);
        softly.assertThat(changeFirstUserResponse.getMessage()).isEqualTo(ResponseMessages.PROFILE_UPDATED_SUCCESSFULLY.getValue());

        //Создаём второго пользователя и получаем его токен
        final String secondUserAuthToken = createUserAndGetToken();

        //выполняем запрос на изменение имени пользователя
        final ChangeUserResponse changeSecondUserResponse = successfulChangeUserName(newName, secondUserAuthToken);

        //проверяем в ответе на запрос имя и сообщение об успешном выполнении
        softly.assertThat(changeSecondUserResponse.getCustomer().getName()).isEqualTo(newName);
        softly.assertThat(changeSecondUserResponse.getMessage()).isEqualTo(ResponseMessages.PROFILE_UPDATED_SUCCESSFULLY.getValue());

    }
}
