package pages;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.codeborne.selenide.Selenide.$;
import static config.AccountData.ACCOUNT_NUMBER_PREFIX;

@Getter
@NoArgsConstructor
public class DepositPage {

    public static final String DEFAULT_TEXT_IN_ACCOUNT_LIST_SELECTOR = "-- Choose an account --";

    public static final String DEPOSIT_ERROR_NEGATIVE_VALUE = "❌ Please enter a valid amount.";

    public static final String DEPOSIT_ERROR_EXCEEDED_MAXIMUM_VALUE = "❌ Please deposit less or equal to 5000$.";

    public static final String DEPOSIT_ERROR_WITHOUT_REQUIRED_FIELDS = "❌ Please select an account.";

    public static final String DEPOSIT_ERROR_WITHOUT_AMOUNT = "❌ Please enter a valid amount.";

    private final SelenideElement depositTitle = $(Selectors.byText("💰 Deposit Money"));

    private final SelenideElement accountSelector = $(Selectors.byClassName("account-selector"));

    private final SelenideElement accountAmount = $(Selectors.byAttribute("placeholder", "Enter amount"));

    private final SelenideElement depositButton = $(Selectors.byText("\uD83D\uDCB5 Deposit"));

    public String expectedSuccessfulDepositModalMessage(double money, int userAccount) {
        return "✅ Successfully deposited $" + money + " to account " + ACCOUNT_NUMBER_PREFIX.getValue() + userAccount + "!";
    }

}
