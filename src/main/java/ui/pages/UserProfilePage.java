package ui.pages;

import api.config.Config;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import common.retry.RetryUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.codeborne.selenide.Selenide.$;

@Getter
@NoArgsConstructor
public class UserProfilePage extends BasePage<UserProfilePage> {


    private final SelenideElement editProfileTitle = $(Selectors.byText("✏\uFE0F Edit Profile"));

    private final SelenideElement inputNameField = $(Selectors.byPlaceholder("Enter new name"));

    private final SelenideElement saveButton = $(Selectors.byText("\uD83D\uDCBE Save Changes"));

    public UserProfilePage waitUntilInputFieldStable() {
        RetryUtils.retry(
                inputNameField::getValue,
                result -> result != null,
                Integer.parseInt(Config.getProperty("max_retry_amounts")),
                Integer.parseInt(Config.getProperty("timeout_mills"))
        );
        return this;
    }

    @Override
    public String url() {
        return UiPath.EDIT_PROFILE;
    }

    public UserProfilePage checkEditPageOpened() {
        editProfileTitle.shouldBe(Condition.visible);
        return this;
    }

    public UserProfilePage inputName(String name) {
        inputNameField.setValue(name);
        return this;
    }

    public UserProfilePage clickSaveButton() {
        saveButton.click();
        return this;
    }

    public UserProfilePage checkInputNameFieldDefaultValue() {
        inputNameField.shouldBe(Condition.visible);
        inputNameField.shouldHave(Condition.exactValue(""));
        return this;
    }
}
