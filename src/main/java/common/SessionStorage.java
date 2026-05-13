package common;

import api.models.UserAccountResponse;
import api.models.UsersResponse;
import lombok.Getter;

import java.util.*;

@Getter
public class SessionStorage {
    public static final SessionStorage INSTANCE = new SessionStorage();

    private SessionStorage() {
    }

    private LinkedHashMap<String, UsersResponse> usersInfoStorage = new LinkedHashMap<>();

    public static void addUserInfoToStorage(String token, UsersResponse userinfo) {
        INSTANCE.usersInfoStorage.put(token, userinfo);
    }

    public static void replaceUserInfoInStorage(String token, UsersResponse userinfo) {
        INSTANCE.usersInfoStorage.replace(token, userinfo);
    }

    public static String getUserTokenFromStorage(int number){
        return new ArrayList<>(INSTANCE.usersInfoStorage.keySet()).get(number - 1);
    }

    public static String getUserTokenFromStorage(){
        return getUserTokenFromStorage(1);
    }

    public static List<String> getAllUserTokensFromStorage(){
        return new LinkedList<>(INSTANCE.usersInfoStorage.keySet());
    }

    public static UsersResponse getUserInfoFromStorage(int number){
        final String userToken = getUserTokenFromStorage(number);
        return INSTANCE.usersInfoStorage.get(userToken);
    }

    public static UsersResponse getUserInfoFromStorageByUserToken(String userToken){
        return INSTANCE.usersInfoStorage.get(userToken);
    }

    public static int[] getUserAccounts(int number){
        final UsersResponse userInfoFromStorage = getUserInfoFromStorage(number);
        return userInfoFromStorage.getAccounts().stream().mapToInt(UserAccountResponse::getId).toArray();
    }

    public static int getUserAccountByUserNumber(int userNumber, int accountNumber){
        final UsersResponse userInfoFromStorage = getUserInfoFromStorage(userNumber);
        return userInfoFromStorage
                .getAccounts()
                .stream()
                .map(UserAccountResponse::getId)
                .toList()
                .get(accountNumber-1);
    }

    public static int getUserAccountByUserNumber(){
        return getUserAccountByUserNumber(1,1);
    }

    public static int getUserAccountByUserToken(String userToken, int accountNumber){
        final UsersResponse userInfoFromStorage = getUserInfoFromStorageByUserToken(userToken);
        return userInfoFromStorage
                .getAccounts()
                .stream()
                .map(UserAccountResponse::getId)
                .toList()
                .get(accountNumber-1);
    }

    public static void clearUsersInfoStorageMap(){
        INSTANCE.usersInfoStorage.clear();
    }
}
