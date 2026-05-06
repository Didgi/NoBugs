package ui_tests.middle_ui_tests;

import api.utils.RandomData;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ui.pages.MainPage;
import ui.pages.UserProfilePage;

import static ui.pages.AlertMessages.*;
import static ui.pages.MainPage.DEFAULT_USER_NAME;

public class ChangeUserNameTests extends BaseTestMiddle {

    @Test
    @DisplayName("Позитивный тест: пользователь может изменить имя на другое валидное")
    public void userCanChangeHisNameWithValidData() {

        String expectedUserName = RandomData.randomName(4);
        String expectedGreeding = mainPage.expectedGreeding(DEFAULT_USER_NAME);

        //Переходим на страницу редактирования пользователя
        //Переходим на основную страницу кликнув по кнопке Home
        //Проверяем отображаемое имя пользователя по центру страницы в приветственном слове,
        //когда у пользователя имя отсутствует
        //Проверяем отображаемое имя пользователя cправа сверху страницы,
        //когда у пользователя имя отсутствует
        //Переходим в редактирование имени пользователи и проверяем наименование страницы редактирования
        //Дожидаемся, пока на UI загрузятся все элементы и он стабилизируется.
        // Без этого тест становится флаки
        //Проверяем, что поле для редактирования отображается и содержит пустое имя
        //Вводим любое валидное имя состоящее из двух слов
        //Проверяем, что в поле отображается введённое значение
        //Нажимаем кнопку сохранить
        // Проверяем сообщение в модальном окне
        userProfilePage
                .open()
                .clickHomeButton()
                .getPage(MainPage.class)
                .checkGreedingText(expectedGreeding)
                .checkUsernameMainPageTopRight(DEFAULT_USER_NAME)
                .getPage(UserProfilePage.class)
                .open()
                .checkEditPageOpened()
                .waitUntilInputStable()
                .checkInputNameFieldDefaultValue()
                .inputName(expectedUserName)
                .clickSaveButton()
                .checkMessageFromModalPageAndAccept(UPDATE_SUCCESSFULLY.getValue());

//        Проверяем отображение изменённого имени пользователя справа сверху страницы
//        Баг. Без рефреша отображается дефолтное значение
//        userProfilePage.checkUsernameMainPageTopRight(expectedUserName);

        //выполняем рефреш и проверяем обновлённое имя
        Selenide.refresh();
        userProfilePage
                .checkEditPageOpened()
                .checkUsernameMainPageTopRight(expectedUserName);

        String expectedUpdatedGreeding = mainPage.expectedGreeding(expectedUserName);

        //переходим на заглавную страницу и проверяем отображаемое имя пользователя по центру страницы
        // в приветственном слове
        userProfilePage
                .clickHomeButton()
                .getPage(MainPage.class)
                .checkMainPageOpened()
                .checkGreedingText(expectedUpdatedGreeding);
    }

    @Test
    @DisplayName("Негативный тест: проверка, что пользователь видит ошибку при попытке изменения имени на невалидное")
    public void userCannotChangeHisNameWithInvalidData() {

        String expectedUserName = RandomData.randomInvalidName(5);

        //Переходим на страницу редактирования имени пользователя и проверяем наименование страницы редактирования
        //Дожидаемся, пока на UI загрузятся все элементы и он стабилизируется.
        // Без этого тест становится флаки
        //Проверяем, что поле для редактирования отображается и содержит пустое имя
        //Вводим невалидное имя состоящее из одного слова
        //Нажимаем кнопку сохранить
        // Проверяем сообщение в модальном окне и закрываем его
        userProfilePage
                .open()
                .checkEditPageOpened()
                .waitUntilInputStable()
                .checkInputNameFieldDefaultValue()
                .inputName(expectedUserName)
                .clickSaveButton()
                .checkMessageFromModalPageAndAccept(UPDATE_ERROR_NAME_INVALID.getValue());

//        Проверяем отображение изменённого имени пользователя справа сверху страницы
//        Баг. Без рефреша отображается дефолтное значение
//        userProfilePage.checkUsernameMainPageTopRight(expectedUserName);

        //выполняем рефреш и проверяем, что имя не изменилось
        //переходим на заглавную страницу и проверяем отображаемое имя пользователя по центру страницы
        // в приветственном слове
        Selenide.refresh();
        String expectedUpdatedGreeding = mainPage.expectedGreeding(DEFAULT_USER_NAME);
        userProfilePage
                .checkUsernameMainPageTopRight(DEFAULT_USER_NAME)
                .clickHomeButton()
                .getPage(MainPage.class)
                .checkMainPageOpened()
                .checkGreedingText(expectedUpdatedGreeding);
    }

    @Test
    @DisplayName("Негативный тест: проверка, что пользователь видит ошибку при попытке изменения имени не заполнив поле ввода")
    public void userCannotChangeHisNameWithEmptyField() {

        //Переходим на страницу редактирования имени пользователя и проверяем наименование страницы редактирования
        //Дожидаемся, пока на UI загрузятся все элементы и он стабилизируется.
        //Без этого тест становится флаки
        //Проверяем, что поле для редактирования отображается и содержит пустое имя
        //Не вводим никакое имя и оставляем поле пустым
        //Нажимаем кнопку сохранить
        //Проверяем сообщение в модальном окне и закрываем его
        userProfilePage
                .open()
                .checkEditPageOpened()
                .waitUntilInputStable()
                .checkInputNameFieldDefaultValue()
                .clickSaveButton()
                .checkMessageFromModalPageAndAccept(UPDATE_ERROR_NAME_EMPTY.getValue());
    }
}
