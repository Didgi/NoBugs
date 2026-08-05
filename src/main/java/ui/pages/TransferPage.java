package ui.pages;

import api.config.AccountData;
import api.config.Operations;
import api.models.UserAccountResponse;
import api.models.UserTransactionsResponse;
import api.requests.steps.user_steps.UserSteps;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ui.elements.UserTransactionHistory;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static api.config.AccountData.ACCOUNT_NUMBER_PREFIX;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.assertj.core.api.Assertions.assertThat;

@Getter
@NoArgsConstructor
public class TransferPage extends BasePage<TransferPage> {

    public static final String TRANSACTION_OWNER = "\uD83D\uDD0D Found under: ";

    private final SelenideElement transferTitle = $(byText("\uD83D\uDD04 Make a Transfer"));

    private final SelenideElement newTransferButton = $(byText("\uD83C\uDD95 New Transfer"));

    private final SelenideElement transferAgainButton = $(byText("\uD83D\uDD01 Transfer Again"));

    public final SelenideElement recipientNameField = $(Selectors.byPlaceholder("Enter recipient name"));

    public final SelenideElement recipientAccountField = $(Selectors.byPlaceholder("Enter recipient account number"));

    public final SelenideElement confirmDetailsCheckbox = $("#confirmCheck[type=checkbox]");

    private final SelenideElement transferButton = $(byText("\uD83D\uDE80 Send Transfer"));

    @Deprecated
    private final SelenideElement searchField = $(Selectors.byPlaceholder("Enter name to find transactions"));

    @Deprecated
    private final SelenideElement searchTransactionsButton = $(byText("\uD83D\uDD0D Search Transactions"));

    private final SelenideElement transactionHistoryTitle = $(byText("Transaction History"));

    public final static ElementsCollection transactionsList = $$("ul.list-group li");

    public final static String NAME_REPEAT_BUTTON = "🔁 Repeat";

    public final SelenideElement transferModalTitleInRepeatModal = $(byText("\uD83D\uDD01 Repeat Transfer"));

    public final SelenideElement transactionInfoInRepeatModal = $(".modal-body").$("p");

    public final SelenideElement accountSelectorInRepeatModal = $(".modal-body select.form-control");

    public final SelenideElement amountFieldInRepeatModal = $(".modal-body input.form-control");

    public final SelenideElement cancelButtonTransferInRepeatModal = $(byText("Cancel"));

    public final SelenideElement closeButtonTransferInRepeatModal = $("button.btn-close");

    public List<UserTransactionHistory> getTransactionsHistoryList() {
        return generateElementList(transactionsList, UserTransactionHistory::new);
    }

    public TransferPage checkTransferPageOpened() {
        transferTitle.shouldBe(Condition.visible);
        return this;
    }

    public TransferPage checkRecipientNameDefaultValue() {
        recipientNameField.shouldBe(Condition.visible);
        recipientNameField.shouldHave(Condition.exactValue(""));
        return this;
    }

    public TransferPage inputRecipientName(String name) {
        recipientNameField.shouldBe(Condition.visible);
        recipientNameField.shouldHave(Condition.exactValue(""));
        recipientNameField.setValue(name);
        return this;
    }

    public TransferPage checkRecipientNameDoesntChange(String name) {
        recipientNameField.shouldBe(Condition.visible);
        recipientNameField.shouldHave(Condition.exactValue(name));
        return this;
    }

    public TransferPage checkRecipientAccountDefaultValue() {
        recipientAccountField.shouldBe(Condition.visible);
        recipientAccountField.shouldHave(Condition.exactValue(""));
        return this;
    }

    public TransferPage inputRecipientAccount(int name) {
        recipientAccountField.shouldBe(Condition.visible);
        recipientAccountField.shouldHave(Condition.exactValue(""));
        recipientAccountField.setValue(AccountData.ACCOUNT_NUMBER_PREFIX.getValue() + name);
        return this;
    }

    public TransferPage inputRecipientAccount(String account) {
        recipientAccountField.shouldBe(Condition.visible);
        recipientAccountField.shouldHave(Condition.exactValue(""));
        recipientAccountField.setValue(account);
        return this;
    }

    public TransferPage checkRecipientAccountDoesntChange(int name) {
        recipientAccountField.shouldBe(Condition.visible);
        recipientAccountField.shouldHave(Condition.exactValue(AccountData.ACCOUNT_NUMBER_PREFIX.getValue() + name));
        return this;
    }

    public TransferPage checkRecipientAccountDoesntChange(String account) {
        recipientAccountField.shouldBe(Condition.visible);
        recipientAccountField.shouldHave(Condition.exactValue(account));
        return this;
    }

    public TransferPage checkConfirmCheckboxUnchecked() {
        confirmDetailsCheckbox.shouldHave(visible);
        confirmDetailsCheckbox.should(domProperty("checked", String.valueOf(false)));
        return this;
    }

    public TransferPage checkConfirmCheckboxChecked() {
        confirmDetailsCheckbox.shouldHave(visible);
        confirmDetailsCheckbox.should(domProperty("checked", String.valueOf(true)));
        return this;
    }

    public TransferPage clickConfirmCheckboxToChecked() {
        confirmDetailsCheckbox.click();
        confirmDetailsCheckbox.should(domProperty("checked", "true"));
        return this;
    }

    public TransferPage clickConfirmCheckboxToUnchecked() {
        confirmDetailsCheckbox.click();
        confirmDetailsCheckbox.should(domProperty("checked", "false"));
        return this;
    }

    public TransferPage clickTransferButton() {
        transferButton.click();
        return this;
    }

    public TransferPage openTransferAgainTab() {
        transferAgainButton.click();
        return this;
    }

    public TransferPage openNewTransferTab() {
        newTransferButton.click();
        return this;
    }

    public TransferPage checkTransferAgainPageOpened() {
        transactionHistoryTitle.shouldBe(visible);
        return this;
    }

    public TransferPage checkTransactionsListSize(int expectedSize) {
        transactionsList.shouldHave(size(expectedSize), Duration.ofSeconds(10));
        return this;
    }

    @Deprecated
    public TransferPage inputValueInSearchField(String value) {
        searchField.click();
        searchField.setValue(value);
        return this;
    }

    @Deprecated
    public TransferPage clickSearchTransactionsButton() {
        searchTransactionsButton.click();
        return this;
    }

    public TransferPage clickRepeatButtonTransaction(Operations operation, double money) {
        getTransactionsHistoryList()
                .stream()
                .filter(el -> el.getTransactionInfo().contains(operation.name())
                        && el.getTransactionInfo().contains(String.valueOf(money)))
                .findFirst().orElseThrow(() -> new AssertionError("Транзакция не найдена"))
                .find(byText(NAME_REPEAT_BUTTON)).click();
        return this;
    }

    public boolean checkRepeatButtonNotAccessable(Operations operation) {
        boolean result = false;
        for (UserTransactionHistory userTransactionHistory : getTransactionsHistoryList()) {
            if (userTransactionHistory.getTransactionInfo().contains(operation.name())) {
                result = userTransactionHistory.getRepeatButtonText() == null;
            }
        }
        return result;

    }

    public TransferPage checkTransferModalTitleRepeatVisible() {
        transferModalTitleInRepeatModal.shouldBe(visible);
        return this;
    }

    public TransferPage checkTransferModalTitleRepeatNotVisible() {
        transferModalTitleInRepeatModal.shouldBe(not(visible));
        return this;
    }

    public TransferPage checkDefaultValueInAccountListRepeatModal() {
        accountSelectorInRepeatModal.options().findBy(Condition.exactText(DEFAULT_TEXT_IN_ACCOUNT_LIST_SELECTOR))
                .shouldBe(Condition.visible);
        return this;
    }

    public TransferPage checkAccountSizeInRepeatModal(int expectedSize) {
        accountSelectorInRepeatModal.options().shouldHave(size(expectedSize), Duration.ofSeconds(30));
        return this;
    }

    public TransferPage selectAccountInRepeatModal(int userAccount) {
        accountSelectorInRepeatModal.click();
        accountSelectorInRepeatModal.selectOptionByValue(String.valueOf(userAccount));
        return this;
    }

    public TransferPage checkAmountValueFieldRepeatModal(double value) {
        amountFieldInRepeatModal.shouldBe(Condition.visible);
        final String valueAccurate = BigDecimal.valueOf(value).stripTrailingZeros().toPlainString();
        amountFieldInRepeatModal.shouldHave(Condition.exactValue(valueAccurate));
        return this;
    }

    public TransferPage inputAmountValueRepeatModal(double value) {
        amountFieldInRepeatModal.setValue(String.valueOf(value));
        return this;
    }

    public TransferPage checkTransferButtonNotClickable() {
        transferButton.shouldBe(not(clickable));
        return this;
    }

    public TransferPage clearValueAmountRepeatModal() {
        amountFieldInRepeatModal.clear();
        return this;
    }

    public TransferPage clickCancelButton() {
        cancelButtonTransferInRepeatModal.click();
        return this;
    }

    public TransferPage clickCloseButton() {
        closeButtonTransferInRepeatModal.click();
        return this;
    }

    public boolean checkTransaction(List<UserTransactionHistory> transactionsTextTransfer, double money, Operations operation) {
        return transactionsTextTransfer
                .stream()
                .anyMatch(element -> element.getTransactionInfo().contains(operation.name()) &&
                        element.getTransactionInfo().contains(String.valueOf(money)));
    }

    @Deprecated
    public boolean checkTransactionOld(List<UserTransactionHistory> transactionsTextTransfer,
                                       double money, Operations operation, String name) {
        transactionsTextTransfer.forEach(element -> {
            boolean operationMatch =
                    element.getTransactionInfo().contains(operation.name());

            boolean moneyMatch =
                    element.getTransactionInfo().contains(String.valueOf(money));

            boolean ownerMatch =
                    element.getTransactionOwner().contains(TRANSACTION_OWNER + name);

            boolean buttonMatch =
                    element.getRepeatButtonText().contains(NAME_REPEAT_BUTTON);
        });
        return transactionsTextTransfer
                .stream()
                .anyMatch(element -> element.getTransactionInfo().contains(operation.name()) &&
                        element.getTransactionInfo().contains(String.valueOf(money)) &&
                        name == null || element.getTransactionOwner().contains(TRANSACTION_OWNER + name)
                        && element.getRepeatButtonText().contains(NAME_REPEAT_BUTTON));
    }

    public void checkTransactionDetails(List<UserTransactionHistory> transactionsTextTransfer,
                                        double money, Operations operation,
                                        UserAccountResponse userAccountResponse, String userToken) {

        final List<UserTransactionsResponse> userTransactions = UserSteps.getUserTransactions(userToken, userAccountResponse.getId());
        final UserTransactionsResponse userTransactionsResponse = userTransactions
                .stream()
                .filter(t -> t.getType().equals(operation))
                .findFirst()
                .orElse(null);

        final UserTransactionHistory userTransactionHistory = transactionsTextTransfer
                .stream()
                .filter(
                        element -> element.getTransactionInfo().contains(operation.name()) &&
                                element.getTransactionInfo().contains(String.valueOf(money)) &&
                                element.getTransactionOwner().contains(String.valueOf(userTransactionsResponse.getRelatedAccountId())))
                .findFirst().orElseThrow();


        final LocalDateTime dbTransactionTime = userTransactionsResponse.getTimestamp();

        DateTimeFormatter uiFormatter =
                DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm:ss");

        String formattedDbDate = dbTransactionTime.format(uiFormatter);

        //проверяем первую строку в истории - тип операции и количество денег
        assertThat(userTransactionHistory.getTransactionInfo().contains(operation.name()) + " — $" + money);

        //проверяем вторую строку в истории: время операции
        assertThat(userTransactionHistory.getTransactionOwner().contains(formattedDbDate));

        //проверяем вторую строку в истории: статус выполнения транзакции
        final String transactionStatus = userTransactionsResponse.getStatus().name();
        assertThat(userTransactionHistory.getTransactionInfo().contains("Status: " + transactionStatus));

        //проверяем вторую строку в истории: статус выполнения проверки транзакции на мошенничество
        final boolean fraudCheckRequired = userTransactionsResponse.isFraudCheckRequired();
        assertThat(userTransactionHistory.getTransactionInfo().contains("Fraud: " + fraudCheckRequired));

        //проверяем вторую строку в истории: аккаунт на который производилась операция
        final int relatedAccountId = userTransactionsResponse.getRelatedAccountId();
        assertThat(userTransactionHistory.getTransactionOwner().contains("Related Account ID: " + relatedAccountId));
    }

    @Deprecated
    public String expectedSuccessfulTransferModalMessageOld(double money, int userAccount) {
        return "✅ Successfully transferred $" + money + " to account " + ACCOUNT_NUMBER_PREFIX.getValue() + userAccount + "!";
    }

    public String expectedSuccessfulTransferModalMessage(double money, String accountNumber) {
        return "✅ Successfully transferred $" + money + " to account " + accountNumber + "!";
    }

    public String expectedSuccessfulTransferModalMessageInRepeatModal(double money, int accountIdFrom, int accountIdTo) {
        final String moneyAccurate = BigDecimal.valueOf(money).stripTrailingZeros().toPlainString();
        return "✅ Transfer of $" + moneyAccurate + " successful from Account " + accountIdFrom + " to " + accountIdTo + "!";
    }

    public TransferPage checkSelectedAccountInListRepeatModal(String userToken, String userAccountNumber) {
        final String actualAccountInfoInListRepeatModal = accountSelectorInRepeatModal.getSelectedOptionText();
        final String expectedAccountInfoInListRepeatModal = getAccountInfoList(userToken, userAccountNumber);
        assertThat(actualAccountInfoInListRepeatModal).isEqualTo(expectedAccountInfoInListRepeatModal);
        return this;
    }

    @Override
    public String url() {
        return UiPath.TRANSFER;
    }
}
