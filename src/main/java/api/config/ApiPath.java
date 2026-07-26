package api.config;

public class ApiPath {
    public final static String BASE_URI = Config.getProperty("api_baseurl") + Config.getProperty("api_version");
    public final static String AUTH_LOGIN_PATH = "auth/login";
    public final static String ADMIN_USERS_PATH = "admin/users";
    public final static String ACCOUNTS_PATH = "accounts";
    public final static String CUSTOMER_ACCOUNTS_PATH = "customer/accounts";
    public final static String CUSTOMER_TRANSACTIONS_PATH = "accounts/{accountId}/transactions";
    public final static String ACCOUNTS_DEPOSIT_PATH = "accounts/deposit";
    public final static String ACCOUNTS_TRANSFER_PATH = "accounts/transfer";
    public final static String ACCOUNTS_TRANSFER_FRAUD_CHECK_PATH = "accounts/transfer-with-fraud-check";
    public final static String ACCOUNTS_TRANSFER_COMPLETE_PATH = "accounts/transfers/{transactionId}/complete";
    public final static String CUSTOMER_PROFILE_PATH = "customer/profile";
}
