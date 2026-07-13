package WM.client;

import api.config.Config;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.google.common.net.HttpHeaders;
import common.annotations.FraudCheckMock;
import lombok.Data;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@Data
public class WireMockClient {

    public static WireMock wireMock;
    private static final String JSON_TYPE = "application/json";

    public static WireMock setupMock(FraudCheckMock config) {
        return new WireMock(Config.getProperty("wireMockHost"), Integer.parseInt(Config.getProperty("wireMockPort")));
    }

    public static MappingBuilder buildResponse(String responseBody, int status) {
        return post(urlPathMatching(Config.getProperty("wireMockEndpoint")))
                .willReturn(aResponse()
                        .withStatus(status)
                        .withHeader(HttpHeaders.CONTENT_TYPE, JSON_TYPE)
                        .withBody(responseBody));

    }

    public static MappingBuilder buildResponse(String responseBody, int status, int timeout) {
        return post(urlPathMatching(Config.getProperty("wireMockEndpoint")))
                .willReturn(aResponse()
                        .withStatus(status)
                        .withFixedDelay(timeout)
                        .withHeader(HttpHeaders.CONTENT_TYPE, JSON_TYPE)
                        .withBody(responseBody));
    }
}
