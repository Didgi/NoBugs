package api.models;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepositResponse extends BaseModel {
    private int id;
    private String accountNumber;
    private double balance;
    private double depositAmount;
    private int transactionId;
}
