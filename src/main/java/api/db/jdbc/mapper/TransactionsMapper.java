package api.db.jdbc.mapper;

import api.config.TransactionStatus;
import api.dao.jdbc.TransactionsDao;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionsMapper implements RowMapper<TransactionsDao> {

    @Override
    public TransactionsDao map(ResultSet resultSet)
            throws SQLException {

        return TransactionsDao.builder()
                .id(resultSet.getInt("id"))
                .amount(resultSet.getDouble("amount"))
                .type(resultSet.getString("type"))
                .timestamp(resultSet.getTimestamp("timestamp"))
                .accountId(resultSet.getInt("account_id"))
                .relatedAccountId(resultSet.getInt("related_account_id"))
                .createdAt(resultSet.getString("created_at"))
                .status(TransactionStatus.valueOf(resultSet.getString("status")))
                .fraudCheckRequired(resultSet.getBoolean("fraud_check_required"))
                .build();
    }
}
