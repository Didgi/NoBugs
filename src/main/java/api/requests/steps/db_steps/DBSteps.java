package api.requests.steps.db_steps;

import api.dao.jdbc.AccountsDao;
import api.dao.jdbc.CustomersDao;
import api.dao.jdbc.TransactionsDao;
import api.dao.jpa.entities.AccountsEntity;
import api.dao.jpa.entities.CustomerEntity;
import api.dao.jpa.repositories.AccountsRepository;
import api.dao.jpa.repositories.CustomersRepository;
import api.db.jdbc.Condition;
import api.db.jdbc.DBRequests;
import api.db.jdbc.mapper.AccountsMapper;
import api.db.jdbc.mapper.CustomersMapper;
import api.db.jdbc.mapper.TransactionsMapper;
import io.qameta.allure.Step;

import java.sql.SQLException;
import java.util.List;

import static api.db.jdbc.DBRequests.RequestTable.CUSTOMERS;
import static api.db.jdbc.DBRequests.RequestTable.TRANSACTIONS;
import static api.db.jdbc.DBRequests.RequestType.SELECT;

public class DBSteps {

    @Step("Находим информацию о пользователе в БД по username: {username}")
    public static CustomersDao getUserByUserNameJDBC(String username) throws SQLException {
        return DBRequests.builder()
                .requestType(SELECT)
                .requestTable(CUSTOMERS)
                .where(Condition.equalTo("username", username))
                .buildRequest()
                .extractOne(new CustomersMapper());
    }

    @Step("Находим информацию об аккаунте в БД по accountNumber: {accountNumber}")
    public static AccountsDao getAccountByAccountNumberJDBC(String accountNumber) throws SQLException {
        return DBRequests.builder()
                .requestType(DBRequests.RequestType.SELECT)
                .requestTable(DBRequests.RequestTable.ACCOUNTS)
                .where(Condition.equalTo("account_number", accountNumber))
                .buildRequest()
                .extractOne(new AccountsMapper());
    }

    @Step("Находим информацию об аккаунте в БД по accountId: {accountId}")
    public static AccountsDao getAccountByAccountIdJDBC(Integer accountId) throws SQLException {
        return DBRequests.builder()
                .requestType(DBRequests.RequestType.SELECT)
                .requestTable(DBRequests.RequestTable.ACCOUNTS)
                .where(Condition.equalTo("id", accountId))
                .buildRequest()
                .extractOne(new AccountsMapper());
    }

    @Step("Находим информацию о выполненной транзакции в БД по accountId: {accountId}")
    public static TransactionsDao getTransactionInfoByAccountIdJDBC(Integer accountId) throws SQLException {
        return DBRequests.builder()
                .requestType(SELECT)
                .requestTable(TRANSACTIONS)
                .where(Condition.equalTo("account_id", accountId))
                .buildRequest()
                .extractOne(new TransactionsMapper());
    }

    @Step("Находим все выполненные транзакции в БД по accountId: {accountId}")
    public static List<TransactionsDao> getTransactionInfoListByAccountIdJDBC(Integer accountId) throws SQLException {
        return DBRequests.builder()
                .requestType(DBRequests.RequestType.SELECT)
                .requestTable(DBRequests.RequestTable.TRANSACTIONS)
                .where(Condition.equalTo("account_id", accountId))
                .buildRequest()
                .extractList(new TransactionsMapper());
    }

    @Step("Находим пользователя в БД по id: {id}")
    public static CustomerEntity getUserByIdJPA(Integer id) {
        return new CustomersRepository().findById(id);
    }

    @Step("Находим аккаунт в БД по accountId: {accountId}")
    public static AccountsEntity getAccountByAccountIdJPA(Integer accountId) {
        return new AccountsRepository().findByAccountId(accountId);
    }
}
