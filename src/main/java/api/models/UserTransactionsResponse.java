package api.models;

import api.config.Operations;
import api.config.TransactionStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
