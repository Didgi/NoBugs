package api_tests;

import api.requests.skelethon.EndpointRequests;
import api.requests.skelethon.requesters.CrudRequester;
import api.specs.RequestSpecs;
import api.specs.ResponseSpecs;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AccountsTransactionsTests extends BaseTestSenior {

    @Test
    @DisplayName("Негативный тест: получение информации о транзакциях по аккаунту без передачи токена пользователя ")
    public void getAccountTransactionsWithoutUserToken() {

        final ValidatableResponse get = new CrudRequester(RequestSpecs.withoutTokenSpec(), EndpointRequests.GET_USER_TRANSACTIONS,
                ResponseSpecs.requestReturnsUnauthorized()).GET(userAccount);
    }

}
