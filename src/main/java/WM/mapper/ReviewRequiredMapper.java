package WM.mapper;

import WM.models.FraudResponse;
import api.config.TransactionFraudCheckDecision;
import api.config.TransactionFraudCheckReason;
import common.annotations.FraudCheckMock;

public class ReviewRequiredMapper implements ScenariosMapper {
    @Override
    public FraudResponse map(FraudCheckMock config) throws RuntimeException {
        return FraudResponse.builder()
                .status(config.status())
                .decision(TransactionFraudCheckDecision.REVIEW_REQUIRED)
                .requiresManualReview(config.requiresManualReview())
                .additionalVerificationRequired(config.additionalVerificationRequired())
                .reason(TransactionFraudCheckReason.TRANSFER_REQUIRES_MANUAL_REVIEW)
                .riskScore(config.riskScore())
                .build();
    }
}
