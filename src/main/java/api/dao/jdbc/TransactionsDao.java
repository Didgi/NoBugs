package api.dao.jdbc;

import api.config.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TransactionsDao {
    private int id;
    private double amount;
    private String type;
    private Timestamp timestamp;
    private int accountId;
    private int relatedAccountId;
    private String createdAt;
    private TransactionStatus status;
    private boolean fraudCheckRequired;
}
