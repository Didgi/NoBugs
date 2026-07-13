package api.models;

import api.config.TransactionFraudCheckDecision;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferFraudCheckResponse extends BaseModel{

    private int transactionId;
    private String fraudReason;
    private int senderAccountId;
    private TransactionFraudCheckDecision status;
    private double amount;
    private String message;
    private boolean requiresVerification;
    private int receiverAccountId;
    private double fraudRiskScore;
    private boolean requiresManualReview;
}
