package common;

import api.models.UserAccountResponse;
import api.models.UsersResponse;
import api.requests.steps.db_steps.DBSteps;
import lombok.Getter;

import java.util.*;

@Getter
public class SessionStorage {

    public static final ThreadLocal<SessionStorage> INSTANCE = ThreadLocal.withInitial(SessionStorage::new);

    private SessionStorage() {
    }

    //данный словарь используется для UI тестов
    private LinkedHashMap<String, UsersResponse> usersInfoStorage = new LinkedHashMap<>();
    //данный список используется для BE тестов. По сути, можно это и не делать, а использовать словарь, но тогда
    //пришлось бы переписать все BE тесты, чтобы они использовали данные из словаря
    private List<Integer> createdUsersForTests = new LinkedList<>();

    public static void addUserInfoToStorage(String token, UsersResponse userinfo) {
        INSTANCE.get().usersInfoStorage.put(token, userinfo);
    }

    public static void replaceUserInfoInStorage(String token, UsersResponse userinfo) {
        INSTANCE.get().usersInfoStorage.replace(token, userinfo);
    }

    public static String getUserTokenFromStorage(int number){
        return new ArrayList<>(INSTANCE.get().usersInfoStorage.keySet()).get(number - 1);
    }

    public static String getUserTokenFromStorage(){
        return getUserTokenFromStorage(1);
    }

    public static List<String> getAllUserTokensFromStorage(){
        return new LinkedList<>(INSTANCE.get().usersInfoStorage.keySet());
    }

    public static UsersResponse getUserInfoFromStorage(int number){
        final String userToken = getUserTokenFromStorage(number);
        return INSTANCE.get().usersInfoStorage.get(userToken);
    }

    public static Collection<UsersResponse> getAllUserInfoFromStorage(){
        return INSTANCE.get().usersInfoStorage.values();
    }

    public static UsersResponse getUserInfoFromStorageByUserToken(String userToken){
        return INSTANCE.get().usersInfoStorage.get(userToken);
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
        INSTANCE.get().usersInfoStorage.clear();
    }

    public static void addCreatedUser(int id){
        INSTANCE.get().createdUsersForTests.add(id);
    }

    public static List<Integer> getCreatedUsers(){
        return INSTANCE.get().createdUsersForTests;
    }

    public static void clearCreatedUsersList(){
        INSTANCE.get().createdUsersForTests.clear();
    }

}
