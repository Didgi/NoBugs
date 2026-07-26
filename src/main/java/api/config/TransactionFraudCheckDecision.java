package api.config;

public enum TransactionFraudCheckDecision {
    APPROVED,
    BLOCKED,
    REVIEW_REQUIRED,
    VERIFICATION_REQUIRED,
    MANUAL_REVIEW_REQUIRED
}
