package api.models;

import api.config.Operations;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserTransactionsResponse {
    private int id;
    private double amount;
    private Operations type;
    private LocalDateTime timestamp;
    private String timestampAsString;
    private int relatedAccountId;
    private double amountAsDouble;

}
