package common;

import api.models.UserAccountResponse;
import api.models.UserContext;
import api.models.UserProfileResponse;
import api.models.UserTransactionsResponse;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

@Getter
public class SessionStorage {

    public static final ThreadLocal<SessionStorage> INSTANCE = ThreadLocal.withInitial(SessionStorage::new);

    private SessionStorage() {
    }

    //данный словарь используется для UI тестов
    private LinkedHashMap<String, UserContext> usersInfoStorage = new LinkedHashMap<>();
    //данный список используется для BE тестов. По сути, можно это и не делать, а использовать словарь, но тогда
    //пришлось бы переписать все BE тесты, чтобы они использовали данные из словаря
    private List<Integer> createdUsersForTests = new LinkedList<>();

    public static void addUserInfoToStorage(String token, UserProfileResponse userinfo) {
        final UserContext userInfoContext = UserContext.builder().userProfileResponse(userinfo).build();
        INSTANCE.get().usersInfoStorage.put(token, userInfoContext);
    }

    public static void replaceUserInfoInStorage(String token, UserProfileResponse userProfileResponse, List<UserAccountResponse> userAccountResponse, List<UserTransactionsResponse> userTransactionsResponse) {
        final UserContext userContext = UserContext
                .builder()
                .userProfileResponse(userProfileResponse)
                .userAccountResponse(userAccountResponse)
                .userTransactionsResponse(userTransactionsResponse)
                .build();
        INSTANCE.get().usersInfoStorage.replace(token, userContext);
    }

    public static void replaceUserInfoInStorage(String token, UserProfileResponse userProfileResponse, List<UserAccountResponse> userAccountResponse) {
        final UserContext userContext = UserContext
                .builder()
                .userProfileResponse(userProfileResponse)
                .userAccountResponse(userAccountResponse)
                .build();
        INSTANCE.get().usersInfoStorage.replace(token, userContext);
    }

    public static String getUserTokenFromStorage(int number) {
        return new ArrayList<>(INSTANCE.get().usersInfoStorage.keySet()).get(number - 1);
    }

    public static String getUserTokenFromStorage() {
        return getUserTokenFromStorage(1);
    }

    public static List<String> getAllUserTokensFromStorage() {
        return new LinkedList<>(INSTANCE.get().usersInfoStorage.keySet());
    }

    public static UserProfileResponse getUserInfoFromStorage(int number) {
        final String userToken = getUserTokenFromStorage(number);
        return INSTANCE.get().usersInfoStorage.get(userToken).getUserProfileResponse();
    }

    public static List<UserAccountResponse> getUserAccountsByToken(String userToken) {
        return INSTANCE.get().usersInfoStorage.get(userToken).getUserAccountResponse();
    }

    public static UserProfileResponse getUserInfoFromStorageByUserToken(String userToken) {
        return INSTANCE.get().usersInfoStorage.get(userToken).getUserProfileResponse();
    }

    public static UserAccountResponse getUserAccountByUserNumber(int userNumber, int accountNumber) {
        String userToken = getUserTokenFromStorage(userNumber);
        return INSTANCE.get().usersInfoStorage.get(userToken).getUserAccountResponse().get(accountNumber - 1);
    }

    public static UserAccountResponse getUserAccountByUserNumber() {
        return getUserAccountByUserNumber(1, 1);
    }

    public static int getUserAccountByUserToken(String userToken, int accountNumber) {
        return INSTANCE.get().usersInfoStorage.get(userToken).getUserAccountResponse().get(accountNumber - 1).getId();
    }

    public static void clearUsersInfoStorageMap() {
        INSTANCE.get().usersInfoStorage.clear();
    }

    public static void addCreatedUser(int id) {
        INSTANCE.get().createdUsersForTests.add(id);
    }

    public static List<Integer> getCreatedUsers() {
        return INSTANCE.get().createdUsersForTests;
    }

    public static void clearCreatedUsersList() {
        INSTANCE.get().createdUsersForTests.clear();
    }

}
