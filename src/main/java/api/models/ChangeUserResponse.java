package api.models;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeUserResponse extends BaseModel {
    private UsersResponse customer;
    private String message;
}
