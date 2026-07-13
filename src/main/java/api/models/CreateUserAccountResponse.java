package api.models;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserAccountResponse extends BaseModel {
    private int id;
    private double balance;
    private String accountNumber;
}
