package WM.mapper;

import WM.models.FraudResponse;
import common.annotations.FraudCheckMock;

public class ApprovedMapper implements ScenariosMapper {
    @Override
    public FraudResponse map(FraudCheckMock config) throws RuntimeException {
        return FraudResponse.builder()
                .status(config.status())
                .decision(config.decision())
                .requiresManualReview(config.requiresManualReview())
                .additionalVerificationRequired(config.additionalVerificationRequired())
                .reason(config.reason())
                .riskScore(config.riskScore())
                .build();
    }
}
