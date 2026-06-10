package api.models;

import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAccountResponse extends BaseModel {
    private int id;
    private String accountNumber;
    private double balance;
    private List<UserTransactionsResponse> transactions;
}
