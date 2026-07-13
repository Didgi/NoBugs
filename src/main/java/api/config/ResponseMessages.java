package api.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseMessages {

    DEPOSIT_AMOUNT_MUST_BE_AT_LEAST_01_OLD("Deposit amount must be at least 0.01"),
    INVALID_ACCOUNT_OR_AMOUNT("Invalid account or amount"),
    DEPOSIT_AMOUNT_CANNOT_EXCEED_5000_OLD("Deposit amount cannot exceed 5000"),
    DEPOSIT_AMOUNT_CANNOT_EXCEED_5000("Deposit amount exceeds the 5000 limit"),
    UNAUTHORIZED_ACCESS_TO_ACCOUNT("Unauthorized access to account"),
    TRANSFER_SUCCESSFUL("Transfer successful"),
    TRANSFER_AMOUNT_MUST_BE_AT_LEAST_01("Transfer amount must be at least 0.01"),
    TRANSFER_AMOUNT_CANNOT_EXCEED_10000("Transfer amount cannot exceed 10000"),
    TRANSFER_COMPLETE("Transfer completed"),
    INVALID_TRANSFER_INSUFFICIENT_FUNDS_OR_INVALID_ACCOUNTS("Invalid transfer: insufficient funds or invalid accounts"),
    OPERATION_IS_FORBIDDEN("Operation is forbidden"),
    PROFILE_UPDATED_SUCCESSFULLY("Profile updated successfully"),
    NAME_MUST_CONTAIN_TWO_WORDS_WITH_LETTERS_ONLY("Name must contain two words with letters only"),
    CANNOT_TRANSFER_MONEY_TO_THE_SAME_ACCOUNT("❌ You cannot transfer money to the same account."),
    TRANSFER_APPROVED("Transfer approved and processed immediately"),
    TRANSFER_BLOCKED("Transfer blocked due to fraud detection"),
    TRANSFER_REQUIRES_MANUAL_REVIEW("Transfer requires manual review"),
    TRANSFER_ADDITIONAL_VERIFICATION_REQUIRED("Additional verification required"),
    LOW_RISK_TRANSACTION("Low risk transaction");

    private final String value;

}
