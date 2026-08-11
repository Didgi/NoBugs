package ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
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

    @Step("Переходим на страницу Deposit")
    public DepositPage goToDepositPage() {
        depositTitle.click();
        return this;
    }

    @Step("Проверяем, что страница Deposit открыта")
    public DepositPage checkDepositPageOpened() {
        depositTitle.shouldBe(Condition.visible);
        return this;
    }

    @Step("Нажимаем по кнопке Deposit для выполнения пополнения")
    public DepositPage clickDepositButton() {
        depositButton.shouldBe(Condition.visible).click();
        return this;
    }
}
