package common.extensions;

import api.config.Config;
import common.annotations.ApiVersion;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

public class ApiVersionExtension implements ExecutionCondition {
    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {

        String actualBackendImage = Config.getProperty("api_version_backend");

        ApiVersion backendImage = context
                .getElement()
                .map(el -> el.getAnnotation(ApiVersion.class)).orElse(null);

        if (backendImage == null) {
            return ConditionEvaluationResult.enabled("Версия API не задана");
        }

        final boolean available = backendImage.version().equalsIgnoreCase(actualBackendImage);

        if (available) {
            return ConditionEvaluationResult.enabled("Указанный образ backend: " + backendImage.version()
                    + " соответствует новому образу из конфигурации: " + Config.getProperty("api_version_backend") +
                    ". Тест будет запущен");
        } else {
            return ConditionEvaluationResult.disabled("Указанный образ backend: " + backendImage.version()
                    + " не соответствует новому образу из конфигурации: " + Config.getProperty("api_version_backend") +
                    ". Тест не будет запущен");
        }
    }
}
