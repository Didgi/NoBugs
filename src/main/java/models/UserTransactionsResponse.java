package models;

import com.fasterxml.jackson.annotation.JsonFormat;
import config.Operations;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserTransactionsResponse {
    private int id;
    private double amount;
    private Operations type;
    @JsonFormat(pattern = "EEE MMM dd HH:mm:ss zzz yyyy", locale = "en")
    private ZonedDateTime timestamp;
    private int relatedAccountId;

}
