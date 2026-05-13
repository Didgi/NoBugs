package ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static org.assertj.core.api.Assertions.assertThat;

@Getter
@NoArgsConstructor
public class MainPage extends BasePage<MainPage> {

    public static final String DEFAULT_USER_NAME = "noname";

    private final SelenideElement mainTitle = $(Selectors.byText("User Dashboard"));

    private final SelenideElement welcomeTitle = $(".welcome-text");

    public String expectedGreeding(String name) {
        return "Welcome, " + name + "!";
    }

    @Override
    public String url() {
        return UiPath.DASHBOARD;
    }

    public MainPage checkMainPageOpened(){
        mainTitle.shouldBe(visible);
        return this;
    }

    public MainPage checkGreedingText(String expectedText){
        welcomeTitle.shouldBe(visible).shouldHave(Condition.text(expectedText));
        assertThat(welcomeTitle.text()).isEqualTo(expectedText);
        return this;
    }
}
