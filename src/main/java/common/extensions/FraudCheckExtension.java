package common.extensions;

import WM.mapper.*;
import WM.client.WireMockClient;
import api.config.Config;
import api.config.TransactionFraudCheckDecision;
import com.github.tomakehurst.wiremock.client.WireMock;
import common.annotations.FraudCheckMock;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Map;

import static WM.client.WireMockClient.setupMock;
import static api.config.TransactionFraudCheckDecision.*;
import static org.apache.http.HttpStatus.*;

public class FraudCheckExtension implements BeforeEachCallback, AfterEachCallback {

    private WireMock wireMock;
    private final Map<TransactionFraudCheckDecision, ScenariosMapper> mappers = Map.of(
            APPROVED, new ApprovedMapper(),
            BLOCKED, new BlockedMapper(),
            REVIEW_REQUIRED, new ReviewRequiredMapper(),
            VERIFICATION_REQUIRED, new VerificationRequiredMapper()
    );

    @Override
    public void beforeEach(ExtensionContext context) {
        FraudCheckMock mockConfig = context.getRequiredTestMethod().getAnnotation(FraudCheckMock.class);
        if (mockConfig != null) {
            wireMock = setupMock(mockConfig);
            if (mockConfig.badRequest() || mockConfig.internalServerError() || mockConfig.timeout())
                negativeScenario(mockConfig);
            else {
                positiveScenario(mockConfig);
            }
        }
    }

    public void positiveScenario(FraudCheckMock mockConfig) {
        ScenariosMapper mapper = mappers.get(mockConfig.decision());
        String responseBody = mapper.toJson(mockConfig);
        wireMock.register(WireMockClient.buildResponse(responseBody, SC_OK));
    }

    public void negativeScenario(FraudCheckMock mockConfig) {
        String responseBody = "\"Negative Scenario\"";
        if (mockConfig.badRequest()) {
            wireMock.register(WireMockClient.buildResponse(responseBody, SC_BAD_REQUEST));
        } else if (mockConfig.internalServerError()) {
            wireMock.register(WireMockClient.buildResponse(responseBody, SC_INTERNAL_SERVER_ERROR));
        } else if (mockConfig.timeout()) {
            ScenariosMapper mapper = mappers.get(mockConfig.decision());
            responseBody = mapper.toJson(mockConfig);
            final int timeoutValue = Integer.parseInt(Config.getProperty("wire_mock_timeout"));
            wireMock.register(WireMockClient.buildResponse(responseBody, SC_OK, timeoutValue));
        }
    }

    @Override
    public void afterEach(ExtensionContext context) {
        if (WireMockClient.wireMock != null) {
            WireMockClient.wireMock.resetRequests();
            WireMockClient.wireMock.resetMappings();
        }
    }
}


