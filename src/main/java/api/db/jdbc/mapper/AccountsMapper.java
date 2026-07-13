package api.db.jdbc.mapper;

import api.dao.jdbc.AccountsDao;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountsMapper implements RowMapper<AccountsDao> {

    @Override
    public AccountsDao map(ResultSet resultSet)
            throws SQLException {

        return AccountsDao.builder()
                .id(resultSet.getInt("id"))
                .accountNumber(resultSet.getString("account_number"))
                .balance(resultSet.getDouble("balance"))
                .customerId(resultSet.getInt("customer_id"))
                .createdAt(resultSet.getString("created_at"))
                .updatedAt(resultSet.getString("updated_at"))
                .build();
    }
}
