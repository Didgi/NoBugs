package ui.elements;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

@Getter
public class UserTransactionHistory extends BaseElement {

    private String transactionInfo;
    private String transactionOwner;
    private String repeatButtonText;

    public UserTransactionHistory(SelenideElement element) {
        super(element);
        transactionInfo = element.text().split("\n")[0];
        transactionOwner = element.text().split("\n")[1];
        repeatButtonText = element.text().split("\n")[2];
    }
}
