package pages;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.codeborne.selenide.Selenide.$;

@Getter
@NoArgsConstructor
public class MainPage {

    public static final String DEFAULT_USER_NAME = "noname";

    private final SelenideElement mainTitle = $(Selectors.byText("User Dashboard"));

    private final SelenideElement welcomeTitle = $(".welcome-text");

    private final SelenideElement userInfo = $(".user-info .user-name");

    private final SelenideElement username = $(".user-username");

    private final SelenideElement homeButton = $(Selectors.byText("\uD83C\uDFE0 Home"));

    public String expectedGreeding(String name){
        return "Welcome, " + name + "!";
    }

}
