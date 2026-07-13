package api.dao.jpa.entities;

import api.dao.jpa.repositories.BaseRepository;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "accounts")
public class AccountsEntity extends BaseRepository {

    @Id
    private int id;
    @Column(name = "account_number")
    private String accountNumber;
    private double balance;
    @Column(name = "customer_id")
    private int customerId;
    @Column(name = "created_at")
    private String createdAt;
    @Column(name = "updated_at")
    private String updatedAt;
}
