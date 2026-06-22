package api.dao.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@NoArgsConstructor
@Data
@Entity
@Table(name = "transactions")
public class TransactionsEntity {

    private int id;
    private double amount;
    private String type;
    private String timestamp;
    @Id
    @Column(name = "account_id")
    private int accountId;
    @Column(name = "related_account_id")
    private int relatedAccountId;
    @Column(name = "created_at")
    private String createdAt;
}
