package common.annotations;

import api.config.TransactionFraudCheckDecision;
import api.config.TransactionFraudCheckReason;
import api.config.TransactionStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface FraudCheckMock {

    TransactionStatus status() default TransactionStatus.SUCCESS;

    TransactionFraudCheckDecision decision() default TransactionFraudCheckDecision.APPROVED;

    double riskScore() default 0.5;

    String reason() default TransactionFraudCheckReason.TRANSFER_APPROVED;

    boolean requiresManualReview() default false;

    boolean additionalVerificationRequired() default false;

    boolean badRequest() default false;

    boolean internalServerError() default false;

    boolean timeout() default false;

}
