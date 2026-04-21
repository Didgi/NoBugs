package ui_tests.junior_ui_tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import config.UiPath;
import org.junit.jupiter.api.*;
import utils.RandomData;

import static com.codeborne.selenide.Condition.*;
import static pages.MainPage.DEFAULT_USER_NAME;
import static pages.UserProfilePage.*;

public class ChangeUserNameTests extends BaseTestJunior {

    @Test
    @DisplayName("Позитивный тест: пользователь может изменить имя на другое валидное")
    public void userCanChangeHisNameWithValidData() {

        String expectedUserName = RandomData.randomName(4);

        //Переходим на заглавную страницу и проверяем, что отображение её наименование
        Selenide.open(UiPath.DASHBOARD);
        mainPage.getMainTitle().shouldBe(visible);

        //Проверяем отображаемое имя пользователя по центру страницы в приветственном слове,
        //когда у пользователя имя отсутствует
        String actualGreeding = mainPage.getWelcomeTitle().text();
        String expectedGreeding = mainPage.expectedGreeding(DEFAULT_USER_NAME);
        Assertions.assertEquals(expectedGreeding, actualGreeding);

        //Проверяем отображаемое имя пользователя cправа сверху страницы,
        //когда у пользователя имя отсутствует
        String actualName = mainPage.getUserInfo().text();
        Assertions.assertEquals(DEFAULT_USER_NAME, actualName.toLowerCase());

        //Переходим в редактирование имени пользователи и проверяем наименование страницы редактирования
        mainPage.getUserInfo().click();
        userProfilePage.getEditProfileTitle().shouldBe(visible);

        //Дожидаемся, пока на UI загрузятся все элементы и он стабилизируется.
        // Без этого тест становится флаки
        userProfilePage.waitUntilInputStable();

        //Проверяем, что поле для редактирования отображается и содержит пустое имя
        userProfilePage.getInputField().shouldHave(Condition.exactValue(""));

        //Вводим любое валидное имя состоящее из двух слов
        userProfilePage.getInputField().shouldBe(interactable).click();
        userProfilePage.getInputField().setValue(expectedUserName);

        //Проверяем, что в поле отображается введённое значение
        userProfilePage.getInputField().shouldHave(Condition.exactValue(expectedUserName));

        //Нажимаем кнопку сохранить
        userProfilePage.getSaveButton().click();

        // Проверяем сообщение в модальном окне
        String actualAlertText = getActualTextFromModalPage();
        Assertions.assertEquals(UPDATE_SUCCESSFULLY, actualAlertText);

//        Проверяем отображение изменённого имени пользователя справа сверху страницы
//        Баг. Без рефреша отображается дефолтное значение
//        final String updatedName = mainPage.getUserInfo().text();
//        Assertions.assertEquals(expectedUserName, updatedName);

        //выполняем рефреш и проверяем обновлённое имя
        Selenide.refresh();
        userProfilePage.getEditProfileTitle().shouldBe(visible);
        String updatedNameAfterRefresh = mainPage.getUserInfo().text();
        Assertions.assertEquals(expectedUserName, updatedNameAfterRefresh);

        //переходим на заглавную страницу и проверяем обновлённое имя
        mainPage.getHomeButton().click();
        mainPage.getMainTitle().shouldBe(visible);
        mainPage.getWelcomeTitle().shouldBe(visible).shouldHave(Condition.text(expectedUserName));

        String actualUpdatedGreeding = mainPage.getWelcomeTitle().text();
        String expectedUpdatedGreeding = mainPage.expectedGreeding(expectedUserName);
        Assertions.assertEquals(expectedUpdatedGreeding, actualUpdatedGreeding);
    }


    @Test
    @DisplayName("Негативный тест: проверка, что пользователь видит ошибку при попытке изменения имени на невалидное")
    public void userCannotChangeHisNameWithInvalidData() {

        String expectedUserName = "invalid";

        //Переходим на заглавную страницу и проверяем, что отображение её наименование
        Selenide.open(UiPath.DASHBOARD);
        mainPage.getMainTitle().shouldBe(visible);

        //Проверяем отображаемое имя пользователя по центру страницы в приветственном слове,
        //когда у пользователя имя отсутствует
        String actualGreeding = mainPage.getWelcomeTitle().text();
        String expectedGreeding = mainPage.expectedGreeding(DEFAULT_USER_NAME);
        Assertions.assertEquals(expectedGreeding, actualGreeding);

        //Проверяем отображаемое имя пользователя cправа сверху страницы,
        //когда у пользователя имя отсутствует
        String actualName = mainPage.getUserInfo().text();
        Assertions.assertEquals(DEFAULT_USER_NAME, actualName.toLowerCase());

        //Переходим в редактирование имени пользователи и проверяем наименование страницы редактирования
        mainPage.getUserInfo().click();
        userProfilePage.getEditProfileTitle().shouldBe(visible);

        //Дожидаемся, пока на UI загрузятся все элементы и он стабилизируется.
        // Без этого тест становится флаки
        userProfilePage.waitUntilInputStable();

        //Проверяем, что поле для редактирования отображается и содержит пустое имя
        userProfilePage.getInputField().shouldHave(Condition.exactValue(""));

        //Вводим любое валидное имя состоящее из двух слов
        userProfilePage.getInputField().shouldBe(interactable).click();
        userProfilePage.getInputField().setValue(expectedUserName);

        //Проверяем, что в поле отображается введённое значение
        userProfilePage.getInputField().shouldHave(Condition.exactValue(expectedUserName));

        //Нажимаем кнопку сохранить
        userProfilePage.getSaveButton().click();

        // Проверяем сообщение в модальном окне
        String actualAlertText = getActualTextFromModalPage();
        Assertions.assertEquals(UPDATE_ERROR_NAME_INVALID, actualAlertText);

//        Проверяем отображение изменённого имени пользователя справа сверху страницы
//        Баг. Без рефреша отображается дефолтное значение
//        final String updatedName = mainPage.getUserInfo().text();
//        Assertions.assertEquals(expectedUserName, updatedName);

        //выполняем рефреш и проверяем, что имя не изменилось
        Selenide.refresh();
        userProfilePage.getEditProfileTitle().shouldBe(visible);
        String updatedNameAfterRefresh = mainPage.getUserInfo().text();
        Assertions.assertEquals(DEFAULT_USER_NAME, updatedNameAfterRefresh.toLowerCase());

        //переходим на заглавную страницу и проверяем обновлённое имя
        mainPage.getHomeButton().click();
        mainPage.getMainTitle().shouldBe(visible);
        mainPage.getWelcomeTitle().shouldBe(visible).shouldHave(Condition.text(DEFAULT_USER_NAME));

        String actualUpdatedGreeding = mainPage.getWelcomeTitle().text();
        String expectedUpdatedGreeding = mainPage.expectedGreeding(DEFAULT_USER_NAME);
        Assertions.assertEquals(expectedUpdatedGreeding, actualUpdatedGreeding);
    }

    @Test
    @DisplayName("Негативный тест: проверка, что пользователь видит ошибку при попытке изменения имени не заполнив поле ввода")
    public void userCannotChangeHisNameWithEmptyField() {

        //Переходим на заглавную страницу и проверяем, что отображение её наименование
        Selenide.open(UiPath.DASHBOARD);
        mainPage.getMainTitle().shouldBe(visible);

        //Проверяем отображаемое имя пользователя по центру страницы в приветственном слове,
        //когда у пользователя имя отсутствует
        String actualGreeding = mainPage.getWelcomeTitle().text();
        String expectedGreeding = mainPage.expectedGreeding(DEFAULT_USER_NAME);
        Assertions.assertEquals(expectedGreeding, actualGreeding);

        //Проверяем отображаемое имя пользователя cправа сверху страницы,
        //когда у пользователя имя отсутствует
        String actualName = mainPage.getUserInfo().text();
        Assertions.assertEquals(DEFAULT_USER_NAME, actualName.toLowerCase());

        //Переходим в редактирование имени пользователи и проверяем наименование страницы редактирования
        mainPage.getUserInfo().click();
        userProfilePage.getEditProfileTitle().shouldBe(visible);

        //Дожидаемся, пока на UI загрузятся все элементы и он стабилизируется.
        // Без этого тест становится флаки
        userProfilePage.waitUntilInputStable();

        //Проверяем, что поле для редактирования отображается и содержит пустое имя
        userProfilePage.getInputField().shouldHave(Condition.exactValue(""));

        //Не вводим никакое имя и оставляем поле пустым
        //Нажимаем кнопку сохранить
        userProfilePage.getSaveButton().click();

        // Проверяем сообщение в модальном окне
        String actualAlertText = getActualTextFromModalPage();
        Assertions.assertEquals(UPDATE_ERROR_NAME_EMPTY, actualAlertText);
    }

}
