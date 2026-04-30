package ui.pages;

import api.config.AccountData;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import api.config.Operations;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static api.config.AccountData.ACCOUNT_NUMBER_PREFIX;
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

    private final SelenideElement searchField = $(Selectors.byPlaceholder("Enter name to find transactions"));

    private final SelenideElement searchTransactionsButton = $(byText("\uD83D\uDD0D Search Transactions"));

    private final ElementsCollection transactionsList = $$("ul.list-group li");

    public final SelenideElement transferModalTitleInRepeatModal = $(byText("\uD83D\uDD01 Repeat Transfer"));

    public final SelenideElement transactionInfoInRepeatModal = $(".modal-body").$("p");

    public final SelenideElement accountSelectorInRepeatModal = $(".modal-body select.form-control");

    public final SelenideElement amountFieldInRepeatModal = $(".modal-body input.form-control");

    public final SelenideElement cancelButtonTransferInRepeatModal = $(byText("Cancel"));

    public final SelenideElement closeButtonTransferInRepeatModal = $("button.btn-close");


    public List<String> getTransactionsText() {
        List<String> texts = new ArrayList<>();
        transactionsList.forEach(element -> {
            texts.add(element.text());
        });
        return texts;
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
        searchField.shouldBe(visible);
        return this;
    }

    public TransferPage checkTransactionsListSize(int expectedSize) {
        transactionsList.shouldHave(size(expectedSize));
        return this;
    }

    public TransferPage inputValueInSearchField(String value) {
        searchField.click();
        searchField.setValue(value);
        return this;
    }

    public TransferPage clickSearchTransactionsButton() {
        searchTransactionsButton.click();
        return this;
    }


    public TransferPage clickRepeatTransaction(Operations operation, double money) {
        transactionsList.findBy(Condition.text(operation.name())).shouldHave(Condition.text(String.valueOf(money)))
                .$(Selectors.byText("\uD83D\uDD01 Repeat")).click();
        return this;
    }

    public TransferPage checkTransferModalTitleRepeatVisible(){
        transferModalTitleInRepeatModal.shouldBe(visible);
        return this;
    }

    public TransferPage checkTransferModalTitleRepeatNotVisible(){
        transferModalTitleInRepeatModal.shouldBe(not(visible));
        return this;
    }

    public TransferPage checkDefaultValueInAccountListRepeatModal(){
        accountSelectorInRepeatModal.options().findBy(Condition.exactText(DEFAULT_TEXT_IN_ACCOUNT_LIST_SELECTOR))
                .shouldBe(Condition.visible);
        return this;
    }

    public TransferPage checkAccountSizeInRepeatModal(int expectedSize){
        accountSelectorInRepeatModal.options().shouldHave(size(expectedSize));
        return this;
    }

    public TransferPage selectAccountInRepeatModal(int userAccount){
        accountSelectorInRepeatModal.click();
        accountSelectorInRepeatModal.selectOptionByValue(String.valueOf(userAccount));
        return this;
    }

    public TransferPage checkAmountValueFieldRepeatModal(double value) {
        amountFieldInRepeatModal.shouldBe(Condition.visible);
        final double valueAccurate = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
        amountFieldInRepeatModal.shouldHave(Condition.exactValue(String.valueOf(valueAccurate)));
        return this;
    }

    public TransferPage inputAmountValueRepeatModal(double value){
        amountFieldInRepeatModal.setValue(String.valueOf(value));
        return this;
    }

    public TransferPage checkTransferButtonNotClickable(){
        transferButton.shouldBe(not(clickable));
        return this;
    }

    public TransferPage clearValueAmountRepeatModal(){
        amountFieldInRepeatModal.clear();
        return this;
    }

    public TransferPage clickCancelButton(){
        cancelButtonTransferInRepeatModal.click();
        return this;
    }

    public TransferPage clickCloseButton(){
        closeButtonTransferInRepeatModal.click();
        return this;
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

    public TransferPage checkSelectedAccountInListRepeatModal(String userToken, int userAccount) {
        final String actualAccountInfoInListRepeatModal = accountSelectorInRepeatModal.getSelectedOptionText();
        final String expectedAccountInfoInListRepeatModal = getAccountInfoList(userToken, userAccount);
        assertThat(actualAccountInfoInListRepeatModal).isEqualTo(expectedAccountInfoInListRepeatModal);
        return this;
    }

    @Override
    public String url() {
        return UiPath.TRANSFER;
    }
}
