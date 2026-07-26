package api.models;

import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileResponse extends BaseModel {
    private int id;
    private String username;
    private String name;
    private String role;
}
