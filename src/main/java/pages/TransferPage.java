package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import config.Operations;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static config.AccountData.ACCOUNT_NUMBER_PREFIX;

@Getter
@NoArgsConstructor
public class TransferPage {

    public static final String DEFAULT_TEXT_IN_ACCOUNT_LIST_SELECTOR = "-- Choose an account --";

    public static final String TRANSACTION_OWNER = "\uD83D\uDD0D Found under: ";

    public static final String TRANSACTION_MESSAGE_REPEAT_MODAL = "Confirm transfer to Account ID: ";

    public final static String TRANSFER_ERROR_NEGATIVE_VALUE = "❌ Error: Transfer amount must be at least 0.01";

    public final static String TRANSFER_ERROR_EXCEEDED_MAXIMUM_VALUE = "❌ Error: Transfer amount cannot exceed 10000";

    public final static String TRANSFER_ERROR_WITHOUT_REQUIRED_FIELDS = "❌ Please fill all fields and confirm.";

    public final static String TRANSFER_ERROR_UNEXISTED_ACCOUNT = "❌ No user found with this account number.";

    public final static String TRANSFER_ERROR_RECIPIENT_NAME_ANOTHER_CASE = "❌ The recipient name does not match the registered name.";

    public final static String TRANSFER_ERROR_UNEXISTED_NAME = "❌ No matching users found.";

    public final static String TRANSFER_ERROR_WITH_ZERO_AMOUNT = "❌ Transfer failed: Please try again.";

    private final SelenideElement transferTitle = $(byText("\uD83D\uDD04 Make a Transfer"));

    private final SelenideElement newTransferButton = $(byText("\uD83C\uDD95 New Transfer"));

    private final SelenideElement transferAgainButton = $(byText("\uD83D\uDD01 Transfer Again"));

    private final SelenideElement accountSelector = $(Selectors.byClassName("account-selector"));

    public final SelenideElement recipientName = $(Selectors.byPlaceholder("Enter recipient name"));

    public final SelenideElement recipientAccount = $(Selectors.byPlaceholder("Enter recipient account number"));

    public final SelenideElement amount = $(Selectors.byPlaceholder("Enter amount"));

    public final SelenideElement confirmDetailsCheckbox = $("#confirmCheck[type=checkbox]");

    public final SelenideElement confirmDetailsCheckboxTitle = $(byText("Confirm details are correct"));

    private final SelenideElement transferButton = $(byText("\uD83D\uDE80 Send Transfer"));

    private final SelenideElement searchField = $(Selectors.byPlaceholder("Enter name to find transactions"));

    private final SelenideElement searchButton = $(byText("\uD83D\uDD0D Search Transactions"));

    private final ElementsCollection transactionsList = $$("ul.list-group li");

    public final SelenideElement transferModalTitleInRepeatModal = $(byText("\uD83D\uDD01 Repeat Transfer"));

    public final SelenideElement transactionInfoInRepeatModal = $(".modal-body").$("p");

    public final SelenideElement accountSelectorInRepeatModal = $(".modal-body select.form-control");

    public final SelenideElement amountInRepeatModal = $(".modal-body input.form-control");

    public final SelenideElement sendTransferInRepeatModal = $(byText("\uD83D\uDE80 Send Transfer"));

    public final SelenideElement cancelTransferInRepeatModal = $(byText("Cancel"));

    public final SelenideElement closeTransferInRepeatModal = $("button.btn-close");


    public List<String> getTransactionsText() {
        List<String> texts = new ArrayList<>();
        transactionsList.forEach(element -> {
            texts.add(element.text());
        });
        return texts;
    }

    public void clickRepeatTransaction(Operations operation, double money) {
        transactionsList.findBy(Condition.text(operation.name())).shouldHave(Condition.text(String.valueOf(money)))
                .$(Selectors.byText("\uD83D\uDD01 Repeat")).click();
    }

    public boolean checkTransaction(List<String> transactions, double money, Operations operation) {
        return transactions.stream().anyMatch(el -> el.contains(String.valueOf(money)) &&
                el.contains(operation.name()));
    }

    public boolean checkTransaction(List<String> transactions, double money, Operations operation, String name) {
        return transactions.stream().anyMatch(el -> el.contains(String.valueOf(money)) &&
                el.contains(operation.name()) && el.contains(TRANSACTION_OWNER + name));
    }

    public String expectedSuccessfulTransferModalMessage(double money, int userAccount) {
        return "✅ Successfully transferred $" + money + " to account " + ACCOUNT_NUMBER_PREFIX.getValue() + userAccount + "!";
    }
}
