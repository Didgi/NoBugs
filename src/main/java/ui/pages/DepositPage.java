package ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.codeborne.selenide.Selenide.$;

@Getter
@NoArgsConstructor
public class DepositPage extends BasePage<DepositPage> {

    private final SelenideElement depositTitle = $(Selectors.byText("💰 Deposit Money"));

    private final SelenideElement depositButton = $(Selectors.byText("\uD83D\uDCB5 Deposit"));

    @Override
    public String url() {
        return UiPath.DEPOSIT;
    }

    public DepositPage goToDepositPage() {
        depositTitle.click();
        return this;
    }

    public DepositPage checkDepositPageOpened() {
        depositTitle.shouldBe(Condition.visible);
        return this;
    }

    public DepositPage clickDepositButton() {
        depositButton.shouldBe(Condition.visible).click();
        return this;
    }
}
