package common.extensions;

import com.codeborne.selenide.Configuration;
import common.annotations.BrowserAnnotation;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Arrays;

public class BrowserAnnotationExtension implements ExecutionCondition {
    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {

        //Получаем текущий браузер из конфига
        String actualBrowser = Configuration.browser;

        //Получаем переданный браузер в аннотации
        BrowserAnnotation browsers = context
                .getElement()
                .map(el -> el.getAnnotation(BrowserAnnotation.class)).orElse(null);

        if (browsers == null) {
            return ConditionEvaluationResult.enabled("Браузер не задан. Ограничений нет");
        }

        //сравниваем переданный браузер из аннотации с тем, что указан в конфиге
        final boolean available = Arrays.stream(browsers.value()).anyMatch(browser -> browser.equalsIgnoreCase(actualBrowser));

        if (available) {
            return ConditionEvaluationResult.enabled("Указанный браузер: " + Arrays.toString(browsers.value())
                    + " соответствует браузеру из конфигурации: " + Configuration.browser);
        } else {
            return ConditionEvaluationResult.disabled("Указанный браузер: " + Arrays.toString(browsers.value())
                    + " не соответствует браузеру из конфигурации: " + Configuration.browser);
        }
    }
}
