package WM.models;

import api.config.TransactionFraudCheckDecision;
import api.config.TransactionStatus;
import api.models.BaseModel;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FraudResponse extends BaseModel {

    private TransactionStatus status;
    private TransactionFraudCheckDecision decision;
    private double riskScore;
    private String reason;
    private boolean requiresManualReview;
    private boolean additionalVerificationRequired;
}
