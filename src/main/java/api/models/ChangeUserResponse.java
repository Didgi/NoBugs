package api.models;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeUserResponse extends BaseModel {
    private UsersResponse customer;
    private String message;
}
