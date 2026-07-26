package api.requests.skelethon;

import api.models.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static api.config.ApiPath.*;

@AllArgsConstructor
@Getter
public enum EndpointRequests {
    LOGIN(AUTH_LOGIN_PATH, LoginRequest.class, LoginResponse.class),
    CREATE_USER(ADMIN_USERS_PATH, CreateUserRequest.class, CreateUserResponse.class),
    GET_USERS_BY_ADMIN(ADMIN_USERS_PATH, null, CreateUserResponse.class),
    CREATE_USER_ACCOUNT(ACCOUNTS_PATH, null, CreateUserAccountResponse.class),
    GET_USER_ACCOUNTS(CUSTOMER_ACCOUNTS_PATH, null, UserAccountResponse.class),
    GET_USER_TRANSACTIONS(CUSTOMER_TRANSACTIONS_PATH, null, UserTransactionsResponse.class),
    GET_USER_INFO(CUSTOMER_PROFILE_PATH, null, UserProfileResponse.class),
    DELETE_USER(ADMIN_USERS_PATH + "/", null, null),
    DELETE_USER_ACCOUNT(ACCOUNTS_PATH + "/", null, null),
    DEPOSIT_MONEY(ACCOUNTS_DEPOSIT_PATH, DepositRequest.class, DepositResponse.class),
    DEPOSIT_MONEY_ERROR(ACCOUNTS_DEPOSIT_PATH, DepositRequest.class, DepositErrorResponse.class),
    TRANSFER_MONEY(ACCOUNTS_TRANSFER_PATH, TransferRequest.class, TransferResponse.class),
    TRANSFER_MONEY_FRAUD_CHECK(ACCOUNTS_TRANSFER_FRAUD_CHECK_PATH, TransferRequest.class, TransferFraudCheckResponse.class),
    TRANSFER_MONEY_ERROR(ACCOUNTS_TRANSFER_PATH, TransferRequest.class, TransferErrorResponse.class),
    TRANSFER_MONEY_COMPLETE(ACCOUNTS_TRANSFER_COMPLETE_PATH, null, TransferCompleteResponse.class),
    UPDATE_USER(CUSTOMER_PROFILE_PATH, ChangeUserRequest.class, ChangeUserResponse.class),
    UPDATE_USER_ERROR(CUSTOMER_PROFILE_PATH, ChangeUserRequest.class, ChangeUserErrorResponse.class);

    private final String path;
    private final Class<? extends BaseModel> requestModel;
    private final Class<? extends BaseModel> responseModel;
}
