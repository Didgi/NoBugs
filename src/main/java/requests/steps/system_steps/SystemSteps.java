package requests.steps.system_steps;

import com.codeborne.selenide.Selenide;

import static com.codeborne.selenide.Selenide.executeJavaScript;

public class SystemSteps {

    public static void putTokenIntoStorage(String authToken){
        Selenide.open("/");
        executeJavaScript(
                "window.localStorage.setItem(arguments[0], arguments[1]);",
                "authToken",
                authToken
        );
    }
}
