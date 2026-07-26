package api.models;

import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserContext extends BaseModel {
    private UserProfileResponse userProfileResponse;
    private List<UserAccountResponse> userAccountResponse;
    private List<UserTransactionsResponse> userTransactionsResponse;
}
