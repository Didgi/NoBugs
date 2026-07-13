package api.models;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferRequest extends BaseModel {
    private double amount;
    private int senderAccountId;
    private int receiverAccountId;

}
