package ui_tests.senior_ui_tests;

import api.dao.jdbc.CustomersDao;
import api.requests.steps.db_steps.DBSteps;
import api.requests.steps.user_steps.UserSteps;
import api.utils.RandomData;
import com.codeborne.selenide.Selenide;
import common.SessionStorage;
import common.annotations.AdminSession;
import common.annotations.BrowserAnnotation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ui.pages.MainPage;
import ui.pages.UserProfilePage;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static ui.pages.AlertMessages.*;
import static ui.pages.MainPage.DEFAULT_USER_NAME;

public class ChangeUserNameTests extends UIBaseTestSenior {

    @AdminSession
    @BrowserAnnotation({"firefox", "chrome"})
    @Test
    @DisplayName("Позитивный тест: пользователь может изменить имя на другое валидное")
    public void userCanChangeHisNameWithValidData() throws SQLException {

        String expectedUserName = RandomData.randomName(4);

        String expectedGreeding = mainPage.expectedGreeding(DEFAULT_USER_NAME);

        userProfilePage
                .open()
                .clickHomeButton()
                .getPage(MainPage.class)
                .checkGreedingText(expectedGreeding)
                .checkUsernameMainPageTopRight(DEFAULT_USER_NAME)
                .getPage(UserProfilePage.class)
                .open()
                .checkEditPageOpened()
                .waitUntilInputFieldStable()
                .checkInputNameFieldDefaultValue()
                .inputName(expectedUserName)
                .clickSaveButton()
                .checkMessageFromModalPageAndAccept(UPDATE_SUCCESSFULLY.getValue());

//        Проверяем отображение изменённого имени пользователя справа сверху страницы
//        Баг. Без рефреша отображается дефолтное значение
//        userProfilePage.checkUsernameMainPageTopRight(expectedUserName);

        Selenide.refresh();
        userProfilePage
                .checkEditPageOpened()
                .checkUsernameMainPageTopRight(expectedUserName);

        String expectedUpdatedGreeding = mainPage.expectedGreeding(expectedUserName);

        userProfilePage
                .clickHomeButton()
                .getPage(MainPage.class)
                .checkMainPageOpened()
                .checkGreedingText(expectedUpdatedGreeding);

        String userToken = SessionStorage.getUserTokenFromStorage();

        String actualNameFromApi = UserSteps.getUserInfo(userToken).getName();

        assertThat(actualNameFromApi).isEqualTo(expectedUserName);

        CustomersDao customersDaoFirst = DBSteps.getUserByUserNameJDBC(UserSteps.getUserInfo(userToken).getUsername());

        assertThat(customersDaoFirst.getName()).isEqualTo(expectedUserName);
    }

    @AdminSession
    @Test
    @DisplayName("Негативный тест: проверка, что пользователь видит ошибку при попытке изменения имени на невалидное")
    public void userCannotChangeHisNameWithInvalidData() throws SQLException {

        String expectedUserName = RandomData.randomInvalidName(5);

        userProfilePage
                .open()
                .checkEditPageOpened()
                .waitUntilInputFieldStable()
                .checkInputNameFieldDefaultValue()
                .inputName(expectedUserName)
                .clickSaveButton()
                .checkMessageFromModalPageAndAccept(UPDATE_ERROR_NAME_INVALID.getValue());

//        Проверяем отображение изменённого имени пользователя справа сверху страницы
//        Баг. Без рефреша отображается дефолтное значение
//        userProfilePage.checkUsernameMainPageTopRight(expectedUserName);

        Selenide.refresh();
        String expectedUpdatedGreeding = mainPage.expectedGreeding(DEFAULT_USER_NAME);
        userProfilePage
                .checkUsernameMainPageTopRight(DEFAULT_USER_NAME)
                .clickHomeButton()
                .getPage(MainPage.class)
                .checkMainPageOpened()
                .checkGreedingText(expectedUpdatedGreeding);


        String userToken = SessionStorage.getUserTokenFromStorage();

        String actualNameFromApi = UserSteps.getUserInfo(userToken).getName();

        assertThat(actualNameFromApi).isNull();

        CustomersDao customersDaoFirst = DBSteps.getUserByUserNameJDBC(UserSteps.getUserInfo(userToken).getUsername());

        assertThat(customersDaoFirst.getName()).isNull();


    }

    @AdminSession
    @Test
    @DisplayName("Негативный тест: проверка, что пользователь видит ошибку при попытке изменения имени не заполнив поле ввода")
    public void userCannotChangeHisNameWithEmptyField() throws SQLException {

        userProfilePage
                .open()
                .checkEditPageOpened()
                .waitUntilInputFieldStable()
                .checkInputNameFieldDefaultValue()
                .clickSaveButton()
                .checkMessageFromModalPageAndAccept(UPDATE_ERROR_NAME_EMPTY.getValue());

        String userToken = SessionStorage.getUserTokenFromStorage();

        String actualNameFromApi = UserSteps.getUserInfo(userToken).getName();

        assertThat(actualNameFromApi).isNull();

        CustomersDao customersDaoFirst = DBSteps.getUserByUserNameJDBC(UserSteps.getUserInfo(userToken).getUsername());

        assertThat(customersDaoFirst.getName()).isNull();
    }
}
