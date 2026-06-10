package api.models;

import lombok.*;
import api.utils.GeneratingRule;

import static api.utils.RegexData.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserRequest extends BaseModel {
    @GeneratingRule(regex = USERNAME_TEMPLATE)
    private String username;
    @GeneratingRule(regex = PASSWORD_TEMPLATE)
    private String password;
    @GeneratingRule(regex = USER_TEMPLATE)
    private String role;

}
