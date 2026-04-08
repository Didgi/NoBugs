package requests.skelethon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import models.*;

import static config.ApiPath.*;

@AllArgsConstructor
@Getter
public enum EndpointRequests {
    LOGIN(AUTH_LOGIN_PATH, LoginRequest.class, LoginResponse.class),
    CREATE_USER(ADMIN_USERS_PATH, CreateUserRequest.class, UsersResponse.class),
    GET_USERS_BY_ADMIN(ADMIN_USERS_PATH, null, UsersResponse.class),
    CREATE_USER_ACCOUNT(ACCOUNTS_PATH, null, UserAccountResponse.class),
    GET_USER_ACCOUNTS(CUSTOMER_ACCOUNTS_PATH, null, UserAccountResponse.class),
    GET_USER_INFO(CUSTOMER_PROFILE_PATH, null, UsersResponse.class),
    DELETE_USER(ADMIN_USERS_PATH + "/", null, null),
    DELETE_USER_ACCOUNT(ACCOUNTS_PATH + "/", null, null),
    DEPOSIT_MONEY(ACCOUNTS_DEPOSIT_PATH, DepositRequest.class, UserAccountResponse.class),
    TRANSFER_MONEY(ACCOUNTS_TRANSFER_PATH, TransferRequest.class, TransferResponse.class),
    UPDATE_USER(CUSTOMER_PROFILE_PATH, ChangeUserRequest.class, ChangeUserResponse.class);

    private final String path;
    private final Class<? extends BaseModel> requestModel;
    private final Class<? extends BaseModel> responseModel;
}
