package api.models;

import api.config.Operations;
import api.config.TransactionStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserTransactionsResponse extends BaseModel {
    private int id;
    private double amount;
    private Operations type;
    private LocalDateTime timestamp;
    @JsonIgnore
    private String timestampAsString;
    private int relatedAccountId;
    @JsonIgnore
    private double amountAsDouble;
    private TransactionStatus status;
    private boolean fraudCheckRequired;

}
