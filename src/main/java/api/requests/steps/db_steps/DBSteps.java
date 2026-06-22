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

import java.sql.SQLException;
import java.util.List;

import static api.db.jdbc.DBRequests.RequestTable.*;
import static api.db.jdbc.DBRequests.RequestType.SELECT;

public class DBSteps {

    public static CustomersDao getUserByUserNameJDBC(String username) throws SQLException {
        return DBRequests.builder()
                .requestType(SELECT)
                .requestTable(CUSTOMERS)
                .where(Condition.equalTo("username", username))
                .buildRequest()
                .extractOne(new CustomersMapper());
    }

    public static AccountsDao getAccountByAccountNumberJDBC(String accountNumber) throws SQLException {
        return DBRequests.builder()
                .requestType(DBRequests.RequestType.SELECT)
                .requestTable(DBRequests.RequestTable.ACCOUNTS)
                .where(Condition.equalTo("account_number", accountNumber))
                .buildRequest()
                .extractOne(new AccountsMapper());
    }

    public static AccountsDao getAccountByAccountIdJDBC(Integer accountId) throws SQLException {
        return DBRequests.builder()
                .requestType(DBRequests.RequestType.SELECT)
                .requestTable(DBRequests.RequestTable.ACCOUNTS)
                .where(Condition.equalTo("id", accountId))
                .buildRequest()
                .extractOne(new AccountsMapper());
    }

    public static TransactionsDao getTransactionInfoByAccountIdJDBC(Integer accountId) throws SQLException {
        return DBRequests.builder()
                .requestType(SELECT)
                .requestTable(TRANSACTIONS)
                .where(Condition.equalTo("account_id", accountId))
                .buildRequest()
                .extractOne(new TransactionsMapper());
    }

    public static List<TransactionsDao> getTransactionInfoListByAccountIdJDBC(Integer accountId) throws SQLException {
        return DBRequests.builder()
                .requestType(DBRequests.RequestType.SELECT)
                .requestTable(DBRequests.RequestTable.TRANSACTIONS)
                .where(Condition.equalTo("account_id", accountId))
                .buildRequest()
                .extractList(new TransactionsMapper());
    }

    public static CustomerEntity getUserByIdJPA(Integer id) {
        return new CustomersRepository().findById(id);
    }

    public static AccountsEntity getAccountByAccountIdJPA(Integer accountId) {
        return new AccountsRepository().findByAccountId(accountId);
    }
}
