package ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import common.retry.RetryUtils;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.codeborne.selenide.Selenide.$;

@Getter
@NoArgsConstructor
public class UserProfilePage extends BasePage<UserProfilePage> {


    private final SelenideElement editProfileTitle = $(Selectors.byText("✏\uFE0F Edit Profile"));

    private final SelenideElement inputNameField = $(Selectors.byPlaceholder("Enter new name"));

    private final SelenideElement saveButton = $(Selectors.byText("\uD83D\uDCBE Save Changes"));

    @Step("Дожидаемся стабилизации страницы редактирования пользователя")
    public UserProfilePage waitUntilInputFieldStable() {
        RetryUtils.retry(
                () -> {
                    String v1 = inputNameField.getValue();
                    Selenide.sleep(1000);
                    String v2 = inputNameField.getValue();
                    return v1 != null && v1.equals(v2);
                },
                result -> result != null && result.equals(true)
        );
        return this;
    }

    @Override
    @Step("Переходим на страницу редактирования пользователя")
    public String url() {
        return UiPath.EDIT_PROFILE;
    }

    @Step("Проверяем, что открыта страница редактирования пользователя")
    public UserProfilePage checkEditPageOpened() {
        editProfileTitle.shouldBe(Condition.visible);
        return this;
    }

    @Step("Вводим имя пользователя")
    public UserProfilePage inputName(String name) {
        inputNameField.setValue(name);
        return this;
    }

    @Step("Нажимаем на кнопку Save Changes для подтверждения изменений")
    public UserProfilePage clickSaveButton() {
        saveButton.click();
        return this;
    }

    @Step("Проверяем, что отображается дефолтное значение имени пользователя")
    public UserProfilePage checkInputNameFieldDefaultValue() {
        inputNameField.shouldBe(Condition.visible);
        inputNameField.shouldHave(Condition.exactValue(""));
        return this;
    }
}
