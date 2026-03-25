package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import utils.GeneratingRule;
import utils.RegexData;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeUserRequest extends BaseModel {
    @GeneratingRule(regex = RegexData.NAME_TEMPLATE)
    private String name;
}
