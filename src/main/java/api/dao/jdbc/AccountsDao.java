package api.dao.jdbc;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AccountsDao {
    private int id;
    private String accountNumber;
    private double balance;
    private int customerId;
    private String createdAt;
    private String updatedAt;
}
