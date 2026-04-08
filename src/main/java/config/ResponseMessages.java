package config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseMessages {

    DEPOSIT_AMOUNT_MUST_BE_AT_LEAST_01("Deposit amount must be at least 0.01"),
    DEPOSIT_AMOUNT_CANNOT_EXCEED_5000("Deposit amount cannot exceed 5000"),
    UNAUTHORIZED_ACCESS_TO_ACCOUNT("Unauthorized access to account"),
    TRANSFER_SUCCESSFUL("Transfer successful"),
    TRANSFER_AMOUNT_MUST_BE_AT_LEAST_01("Transfer amount must be at least 0.01"),
    TRANSFER_AMOUNT_CANNOT_EXCEED_10000("Transfer amount cannot exceed 10000"),
    INVALID_TRANSFER_INSUFFICIENT_FUNDS_OR_INVALID_ACCOUNTS("Invalid transfer: insufficient funds or invalid accounts"),
    OPERATION_IS_FORBIDDEN("Operation is forbidden"),
    PROFILE_UPDATED_SUCCESSFULLY("Profile updated successfully"),
    NAME_MUST_CONTAIN_TWO_WORDS_WITH_LETTERS_ONLY("Name must contain two words with letters only");

    private final String value;

}
