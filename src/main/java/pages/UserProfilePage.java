package pages;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.codeborne.selenide.Selenide.$;

@Getter
@NoArgsConstructor
public class UserProfilePage {

    public final static String UPDATE_SUCCESSFULLY = "✅ Name updated successfully!";

    public final static String UPDATE_ERROR_NAME_INVALID = "Name must contain two words with letters only";

    public final static String UPDATE_ERROR_NAME_EMPTY = "❌ Please enter a valid name.";

    private final SelenideElement editProfileTitle = $(Selectors.byText("✏\uFE0F Edit Profile"));

    private final SelenideElement inputField = $(Selectors.byPlaceholder("Enter new name"));

    private final SelenideElement saveButton = $(Selectors.byText("\uD83D\uDCBE Save Changes"));

    public void waitUntilInputStable() {
        Selenide.Wait().until(driver -> {
            String v1 = inputField.getValue();
            Selenide.sleep(1000);
            String v2 = inputField.getValue();
            return v1 != null && v1.equals(v2);
        });
    }

}
