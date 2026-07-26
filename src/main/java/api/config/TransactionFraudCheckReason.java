package api.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TransactionFraudCheckReason {

    public static final String TRANSFER_APPROVED = "Transfer approved and processed immediately";
    public static final String TRANSFER_BLOCKED = "Transfer blocked due to fraud detection";
    public static final String TRANSFER_REQUIRES_MANUAL_REVIEW = "Transfer requires manual review";
    public static final String TRANSFER_ADDITIONAL_VERIFICATION_REQUIRED = "Additional verification required";
    public static final String LOW_RISK_TRANSACTION = "Low risk transaction";
}
