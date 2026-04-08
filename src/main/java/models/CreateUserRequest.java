package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import utils.GeneratingRule;

import static utils.RegexData.*;

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
