package api.models;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferCompleteResponse extends BaseModel {
    private String message;
    private int transactionId;
}
