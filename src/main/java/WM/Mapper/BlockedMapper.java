package WM.Mapper;

import WM.models.FraudResponse;
import api.config.TransactionFraudCheckDecision;
import api.config.TransactionFraudCheckReason;
import common.annotations.FraudCheckMock;

public class BlockedMapper implements ScenariosMapper {
    @Override
    public FraudResponse map(FraudCheckMock config) throws RuntimeException {
        return FraudResponse.builder()
                .status(config.status())
                .decision(TransactionFraudCheckDecision.BLOCKED)
                .requiresManualReview(config.requiresManualReview())
                .additionalVerificationRequired(config.additionalVerificationRequired())
                .reason(TransactionFraudCheckReason.TRANSFER_BLOCKED)
                .riskScore(config.riskScore())
                .build();
    }
}
