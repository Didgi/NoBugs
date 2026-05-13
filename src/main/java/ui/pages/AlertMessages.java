package ui.pages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlertMessages {
    DEPOSIT_ERROR_NEGATIVE_VALUE("❌ Please enter a valid amount."),
    DEPOSIT_ERROR_EXCEEDED_MAXIMUM_VALUE("❌ Please deposit less or equal to 5000$."),
    DEPOSIT_ERROR_WITHOUT_REQUIRED_FIELDS("❌ Please select an account."),
    DEPOSIT_ERROR_WITHOUT_AMOUNT("❌ Please enter a valid amount."),
    TRANSACTION_MESSAGE_REPEAT_MODAL("Confirm transfer to Account ID: "),
    TRANSFER_ERROR_NEGATIVE_VALUE("❌ Error: Transfer amount must be at least 0.01"),
    TRANSFER_ERROR_EXCEEDED_MAXIMUM_VALUE("❌ Error: Transfer amount cannot exceed 10000"),
    TRANSFER_ERROR_WITHOUT_REQUIRED_FIELDS("❌ Please fill all fields and confirm."),
    TRANSFER_ERROR_UNEXISTED_ACCOUNT("❌ No user found with this account number."),
    TRANSFER_ERROR_RECIPIENT_NAME_ANOTHER_CASE("❌ The recipient name does not match the registered name."),
    TRANSFER_ERROR_UNEXISTED_NAME("❌ No matching users found."),
    TRANSFER_ERROR_WITH_ZERO_AMOUNT("❌ Transfer failed: Please try again."),
    UPDATE_SUCCESSFULLY("✅ Name updated successfully!"),
    UPDATE_ERROR_NAME_INVALID("Name must contain two words with letters only"),
    UPDATE_ERROR_NAME_EMPTY("❌ Please enter a valid name.");

    private final String value;
}
