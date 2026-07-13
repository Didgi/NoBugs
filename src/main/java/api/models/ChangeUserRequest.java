package api.models;

import lombok.*;
import api.utils.GeneratingRule;
import api.utils.RegexData;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeUserRequest extends BaseModel {
    @GeneratingRule(regex = RegexData.NAME_TEMPLATE)
    private String name;
}
