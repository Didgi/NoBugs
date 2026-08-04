package WM.mapper;

import WM.models.FraudResponse;
import api.config.TransactionFraudCheckDecision;
import api.config.TransactionFraudCheckReason;
import common.annotations.FraudCheckMock;

public class VerificationRequiredMapper implements ScenariosMapper {
    @Override
    public FraudResponse map(FraudCheckMock config) throws RuntimeException {
        return FraudResponse.builder()
                .status(config.status())
                .decision(TransactionFraudCheckDecision.VERIFICATION_REQUIRED)
                .requiresManualReview(config.requiresManualReview())
                .additionalVerificationRequired(config.additionalVerificationRequired())
                .reason(TransactionFraudCheckReason.TRANSFER_ADDITIONAL_VERIFICATION_REQUIRED)
                .riskScore(config.riskScore())
                .build();
    }
}
